/*
  Copyright (c) 2010 Harri Kaimio

  This file is part of Photovault.

  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
*/


package org.photovault.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.replication.Change;
import org.photovault.replication.ChangeDTO;
import org.photovault.replication.ChangeFactory;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.ObjectHistoryDTO;
import org.photovault.replication.VersionedObjectEditor;

/**
 * This command adds a set of changes to the database
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class ApplyChangeCommand extends DataAccessCommand {

    static private Log log = LogFactory.getLog( ApplyChangeCommand.class );

    /**
     * Changes taht will be applied
     */
    private List<ChangeDTO> changes = new ArrayList();

    public ApplyChangeCommand() {

    }

    /**
     * Constructor
     * @param change The change that will be applied
     */
    public ApplyChangeCommand( ChangeDTO change ) {
        changes.add( change );
    }

    /**
     * Add a change to the list of changes that will be applied by this command.
     * @param change The change
     */
    public void addChange( ChangeDTO change ) {
        changes.add(change );
    }

    public void execute() throws CommandException {

        Map<UUID, ObjectHistoryDTO> history = new HashMap();

        for ( ChangeDTO ch: changes ) {
            ObjectHistoryDTO h = history.get( ch.getTargetUuid() );
            if ( h == null ) {
                try {
                    Class targetClass = Class.forName( ch.getTargetClassName() );
                    h = new ObjectHistoryDTO( targetClass, ch.getTargetUuid() );
                    history.put( ch.getTargetUuid(), h );
                } catch ( ClassNotFoundException e ) {
                    throw new CommandException( "Could not find target class", e );
                }
            }
            h.addChange( ch );
        }
        DTOResolverFactory df = daoFactory.getDTOResolverFactory();
        ChangeFactory cf = new ChangeFactory( daoFactory.getChangeDAO() );
        for ( ObjectHistoryDTO h: history.values() ) {
            try {
                Class targetClass = Class.forName( h.getTargetClassName() );
                HibernateDAOFactory hdf = (HibernateDAOFactory) daoFactory;
                Object target =
                        hdf.getSession().get( targetClass, h.getTargetUuid() );
                VersionedObjectEditor e = null;
                Change oldVersion = null;
                boolean wasAtHead = true;
                if ( target != null ) {
                    // The object already exists in this database
                    e = new VersionedObjectEditor(  target, df );
                    oldVersion = e.getHistory().getVersion();
                    wasAtHead = oldVersion.isHead();
                } else {
                    // The object does not exist in this database, so create it
                    e = new VersionedObjectEditor( targetClass, h.getTargetUuid(), df );
                }
                e.addToHistory( h, cf );
                Set<Change> newHeads = e.getHistory().getHeads();
                if ( newHeads.size() > 1 ) {
                    tryMergeHeads( newHeads );
                }
                if ( wasAtHead ) {
                    /* If the latest version was head of a branch, move to the
                     * new head of that branch
                     */
                    Change newTip = oldVersion;
                    while ( !newTip.isHead() ) {
                        newTip = (Change) newTip.getChildChanges().iterator().next();
                    }
                    if ( newTip != oldVersion ) {
                        e.changeToVersion( newTip );
                    }
                }
            } catch ( ClassNotFoundException e ) {
                throw new CommandException( "Cannot find target class", e );
            } catch ( InstantiationException e ) {
                throw new CommandException( "Cannot instantiate target class", e );
            } catch ( IllegalAccessException e ) {
                throw new CommandException( "Cannot access target object", e );
            } catch ( IOException e ) {
                throw new CommandException( "IOException while adding change: ", e );
            }
        }
    }

    /**
     * Make an attemp to merge given changes to a new head. The method attemps
     * merging the changes one-by-one to the previous merge result until either
     * all changes are merged ot a conflict occurs.
     * @param heads List of current heads that should be merged.
     */
    private void tryMergeHeads( Collection<Change> heads ) {
        List<Change> headList = new ArrayList( heads );

        Change ch1 = headList.get( 0 );
        for ( Change ch2 : headList.subList( 1, headList.size() ) ) {
            Change merged = ch1.merge( ch2 );
            if ( merged.hasConflicts() ) {
                return;
            }
            merged.freeze();
            ch1 = merged;
        }
    }

}
