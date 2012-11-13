/*
  Copyright (c) 2005, Christian Bauer <christian@hibernate.org>
  Copyright (c) 2007, Harri Kaimio
  
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

 
 
  Based on SWING/Hibernate framework by Christian Bauer. Original SW 
  licensed with the following conditions:

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are met:

  - Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

  - Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

  Neither the name of the original author nor the names of contributors may be
  used to endorse or promote products derived from this software without
  specific prior written permission.
 */

package org.photovault.swingui.framework;

import javax.swing.ImageIcon;
import org.photovault.persistence.HibernateUtil;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.event.ActionEvent;

/**
 * Overrides executeInController() and adds transaction demarcation.
 *
 * @author Christian Bauer
 */
public abstract class DataAccessAction extends DefaultAction {

    private static Log log = LogFactory.getLog(DataAccessAction.class);

    public DataAccessAction() { 
        this( null, null );
    }
    
    public DataAccessAction( String name ) {
        this( name, null );
    }
    
    public DataAccessAction( String name, ImageIcon icon ) {
        super( name, icon );
    }

    public void actionPerformed(ActionEvent actionEvent) {
        actionPerformed(actionEvent, HibernateUtil.getSessionFactory().getCurrentSession());
    }

    public void executeInController(AbstractController controller, ActionEvent event) {
        PersistenceController persistenceController;
        if (controller instanceof PersistenceController)
            persistenceController = (PersistenceController) controller;
        else
            throw new IllegalArgumentException(
                "Controller: " + controller.getClass() + " is not a PersistenceController"
            );

        if (preTransaction()) { return; }
        try {
            log.debug("Beginning database transaction");
            persistenceController.getPersistenceContext().beginTransaction();

            log.debug("Executing action event");
            actionPerformed(event);

            log.debug("Committing database transaction");
            persistenceController.getPersistenceContext().getTransaction().commit();
        } catch (RuntimeException ex) {
            log.debug("Rolling back database transaction");
            persistenceController.getPersistenceContext().getTransaction().rollback();
            failedTransaction();
            throw ex;
        }
        postTransaction();
    }

    /**
     * Executes after transaction completion, Session is in auto-commit mode.
     *
     * @return Return <tt>true</tt> to abort execution.
     */
    protected boolean preTransaction() { return false; }

    /**
     * Executes before transaction begins, Session is in auto-commit mode.
     */
    protected void postTransaction() {}

    /**
     * Executes when transaction failed, after rollback. Session can't be used anymore.
     */
    protected void failedTransaction() {}

    /**
     * The main execute routine of this action.
     * 
     * @param actionEvent
     * @param currentSession Use this Session to access the database.
     */
    public abstract void actionPerformed(ActionEvent actionEvent, Session currentSession);

}
