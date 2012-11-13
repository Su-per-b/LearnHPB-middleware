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


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.photovault.imginfo.DataExporter;
import org.photovault.imginfo.xml.XmlExportListener;
import org.photovault.imginfo.xml.XmlExporter;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;

/**
 */
class ExportMetadataAction extends AbstractAction implements XmlExportListener {
    
    /**
     Constructor.
     */
    public ExportMetadataAction( String text, ImageIcon icon,
            String desc, int mnemonic) {
        super( text, icon );
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, new Integer( mnemonic) );
    }
        
    public void actionPerformed( ActionEvent ev ) {    
        File exportFile = null;
        JFileChooser saveDlg = new JFileChooser();
        int retval = saveDlg.showDialog( null, "Export database to XML" );
        if ( retval == JFileChooser.APPROVE_OPTION ) {
            final File f = saveDlg.getSelectedFile();
            progressDlg = new ProgressDlg( null, true );
            progressDlg.setTitle( "Exporting..." );
            progressDlg.setStatus( "Gathering photos...");
            progressDlg.setProgressPercent( 0 );
            final ExportMetadataAction tthis = this;
            Thread exportThread = new Thread() {
                @Override
                public void run() {
                    Session s = null;
                    ObjectOutputStream os = null;
                    try {
                        s = HibernateUtil.getSessionFactory().openSession();
                        HibernateDAOFactory df =
                                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
                        df.setSession( s );

                        // os = new ObjectOutputStream( new FileOutputStream( f ) );
                        DataExporter exporter = new DataExporter();
                        xmlExportStatus( null, XmlExporter.EXPORTER_STATE_EXPORTING_FOLDERS );
                        // exporter.exportFolders( os, df );
                        xmlExportStatus( null, XmlExporter.EXPORTER_STATE_EXPORTING_PHOTOS );
                        exporter.exportPhotos( f, df );
                        xmlExportStatus( null, XmlExporter.EXPORTER_STATE_COMPLETED );

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        if ( s != null ) {
                            s.close();
                        }
                        if ( os != null ) {
                            try {
                                os.close();
                            } catch ( IOException ex ) {
                            }
                        }
                    }
                    
                }
            };
            
            exportThread.start();
            progressDlg.setVisible( true );
        }
    }

    ProgressDlg progressDlg;
    
    public void xmlExportStatus(XmlExporter exporter, int status) {
        switch ( status ) {
            case XmlExporter.EXPORTER_STATE_EXPORTING_FOLDERS:
                progressDlg.setStatus( "Exporting folder hierarchy..." );
                break;
            case XmlExporter.EXPORTER_STATE_EXPORTING_PHOTOS:
                progressDlg.setStatus( "Exporting photos..." );
                break;
            case XmlExporter.EXPORTER_STATE_COMPLETED:
                progressDlg.completed();
                break;
            default:
                JOptionPane.showMessageDialog( null, 
                        "Invalid state for exporter: " + status, 
                        "Exporting error", JOptionPane.ERROR_MESSAGE );
        }
    }

    public void xmlExportError(XmlExporter exporter, String message) {
                JOptionPane.showMessageDialog( null, 
                        message, 
                        "Exporting error", JOptionPane.ERROR_MESSAGE );
    }

    public void xmlExportObjectExported(XmlExporter exporter, Object obj) {
        int totalPhotoCount = exporter.getTotalPhotoCount();
        int exportedPhotoCount = exporter.getExportedPhotoCount();
        if ( totalPhotoCount > 0 ) {
            int percentage = (exportedPhotoCount * 100 ) / totalPhotoCount;
            progressDlg.setProgressPercent( percentage );
        }
    }
}