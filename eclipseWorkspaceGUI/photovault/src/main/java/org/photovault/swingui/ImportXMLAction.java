/*
  Copyright (c) 2009 Harri Kaimio
 
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

package org.photovault.swingui;


import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.photovault.imginfo.DataExporter;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.xml.XmlImportListener;
import org.photovault.imginfo.xml.XmlImporter;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;

/**
 Import metadata from XML file to database
 */
class ImportXMLAction extends AbstractAction implements XmlImportListener {
    
    /**
     Constructor.
     */
    public ImportXMLAction( String text, ImageIcon icon,
            String desc, int mnemonic) {
        super( text, icon );
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, new Integer( mnemonic) );
    }
        
    public void actionPerformed( ActionEvent ev ) {    
        File importFile = null;
        JFileChooser importDlg = new JFileChooser();
        int retval = importDlg.showDialog( null, "Import from XML" );
        if ( retval == JFileChooser.APPROVE_OPTION ) {
            final File f = importDlg.getSelectedFile();
            final ImportXMLAction staticThis = this;
            Thread importThread = new Thread() {
                @Override
                public void run() {
                    Session s = null;
                    ObjectInputStream is = null;
                    try {
                        s = HibernateUtil.getSessionFactory().openSession();
                        HibernateDAOFactory df =
                                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
                        df.setSession( s );

                        DataExporter exporter = new DataExporter();
                        exporter.importChanges( f, df );
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        if ( s != null ) {
                            s.close();
                        }
                        if ( is != null ) {
                            try {
                                is.close();
                            } catch ( IOException ex ) {
                                return;
                            }
                        }
                    }
                }
            };
            importThread.start();            
        }
    }
    
    /**
     List of @see StatusChangeListener interested in changes in this object
     */
    private final Vector listeners = new Vector();
    
    public void addStatusChangeListener( StatusChangeListener l ) {
        synchronized( listeners ) {
            listeners.add( l );
        }
    }
    
    public void removeStatusChangeListener( StatusChangeListener l ) {
        synchronized( listeners ) {
            listeners.remove( l );
        }
    }
    
    void fireStatusChangeEvent( String msg ) {
        StatusChangeEvent e = new StatusChangeEvent( this, msg );
        synchronized( listeners ) {
            Iterator iter = listeners.iterator();
            while ( iter.hasNext() ) {
                StatusChangeListener l = (StatusChangeListener) iter.next();
                l.statusChanged( e );
            }
        }
    }    

    public void xmlImportStatus(XmlImporter exporter, int status) {
        switch ( status ) {
            case XmlImporter.IMPORTING_STARTED:
                fireStatusChangeEvent( "Importing started" );
                break;
            case XmlImporter.IMPORTING_COMPLETED:
                fireStatusChangeEvent( "Importing completed" );
                break;
            default:
                showError( "Invalid importer status: " + status );
        }
    }

    public void xmlImportError(XmlImporter exporter, String message) {
        showError( message );
    }

    public void xmlImportObjectImported(XmlImporter xmlImporter, Object obj) {
        if ( obj instanceof PhotoInfo ) {
            fireStatusChangeEvent( "Imported " + xmlImporter.getImportedPhotoCount() + " photos." );
        }
    }
    
    private void showError( String error ) {
        final String errorMsg = error;
        try {
            SwingUtilities.invokeAndWait( new Runnable() {
               public void run() {
                   JOptionPane.showMessageDialog( null, errorMsg, "Error importing photos", 
                           JOptionPane.ERROR_MESSAGE );
               } 
            });
        } catch (HeadlessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}