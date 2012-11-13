/*
  Copyright (c) 2006 Harri Kaimio
  
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


package org.photovault.swingui.indexer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.photovault.folder.PhotoFolder;

/**
 This class adds to normal file chooser an options dialog for setting the 
 root folder of the chreated folder hierarchy.
 */
public class IndexerFileChooser extends JFileChooser {
    
    /** Creates a new instance of IndexerFileChooser */
    public IndexerFileChooser() {
        createUI();
    }

    IndexerSetupDlg setupDlg = null;
    
    void createUI() {

        // Create the options button 
        JPanel optionsPane = new JPanel();
        optionsPane.setLayout( new BoxLayout( optionsPane, BoxLayout.Y_AXIS) );
        JButton btn = new JButton( "Options..." );        
        optionsPane.add( Box.createGlue());
        optionsPane.add( btn );
        optionsPane.add( Box.createGlue());
        setAccessory( optionsPane );
        final IndexerFileChooser fc = this;
        btn.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                IndexerSetupDlg setupDlg = new IndexerSetupDlg( null, true );
                if ( setupDlg.showDialog() ) {
                    fc.parentFolder = setupDlg.getExtvolParentFolder();
                }
            }
        });
        
    }
    
    PhotoFolder parentFolder = null;
    
    /**
     Get the folder user has selected as th eparent for external volume folders
     @return The folder user has selected of <code>null</code> if she has not 
     selected any folder
     */
    public PhotoFolder getExtvolParentFolder() {
        return parentFolder;
    }
}
