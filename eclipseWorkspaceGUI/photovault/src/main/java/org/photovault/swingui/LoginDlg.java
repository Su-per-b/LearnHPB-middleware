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

package org.photovault.swingui;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.photovault.imginfo.*;
import java.io.*;
import org.photovault.common.MysqlDescriptor;
import org.photovault.common.PVDatabase;
import org.photovault.common.PhotovaultSettings;

/**
 * LoginDlg implementfs a sim?ple dialog for asking which database will be used 
 * as well as username & password for database connection. <p>
 *
 * LoginDlg works tightly together with the @see Photovault main allpication class.
 */
public class LoginDlg extends JDialog {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( PhotoInfoDlg.class.getName() );

    static final String DIALOG_TITLE = "Login";
    Photovault app;
    /**
       Constructor. 
       @param app Owner of the dialog
    */
    public LoginDlg( Photovault app ) {
	super( (JFrame) null, "Login to Photovault", true );
	this.app = app;
	SwingUtilities.invokeLater(new java.lang.Runnable() {
		public void run() {
		    createUI();
		}
	    });
    }
    
    int returnReason;
    
    public static final int RETURN_REASON_INVALID = 0;    
    public static final int RETURN_REASON_APPROVE = 1;
    public static final int RETURN_REASON_CANCEL = 2;
    public static final int RETURN_REASON_NEWDB = 3;
    
    /**
     * Shows the dialog and returns after the dialog is dismissed
     * @return Reason for dismissal, either
     * <ul>
     * <li> @see RETURN_REASON_APPROVE - user pressed OK button
     * <li> @see RETURN_REASON_CANCEL - user pressed Cancel button
     * <li> @see RETURN_REASON_NEWDB - user requested a new database to be created
     */
    public int showDialog() {
        returnReason = RETURN_REASON_INVALID;
        setVisible( true );
        return returnReason;
    }

    /**
     * Returns the user name that has been entered to the dialog.
     */
    public String getUsername() {
	return idField.getText();
    }

    /**
     * Returns the password entered to the dialog.
     */
    public String getPassword() {
	char[] pass = passField.getPassword();
	return new String( pass );
    }

    /**
     * Return the name of selected database.
     */
    public String getDb() {
	return dbField.getSelectedItem().toString();
    }
    
    /**
     Username saved when disabling credentials fields
     */
    String savedUsername = null;
    
    /**
     Enable or disable the credentials fields (username & password) - not all 
     database types support these.
     <p>
     Whend the fields are disabled, the existing username is saved to 
     savedUsername and the fields are emptied. When they are enabled, the 
     ptentially saved username is restored.
     @param enabled <code>true</code> if the fields should be enabled
     */
    public void setCredentialsEnabled( boolean enabled ) {
        if ( enabled ) {
            if ( savedUsername != null ) {
                // Restore username
                idField.setText( savedUsername );
            }
        } else {
            if ( idField.isEnabled() ) {
                savedUsername = idField.getText();
                idField.setText( "" );
                passField.setText( "" );
            } 
        }
        idField.setEnabled( enabled );
        passField.setEnabled( enabled );
    }
    
    boolean okPressed = false;
    
    /**
       Creates the UI components needed for this dialog.
    */
    protected void createUI() {

        GridBagConstraints labelConstraints = new GridBagConstraints();
	labelConstraints.insets = new Insets( 2, 20, 2, 2 );
	labelConstraints.anchor = GridBagConstraints.EAST;
	labelConstraints.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
	labelConstraints.fill = GridBagConstraints.NONE;      //reset to default
	labelConstraints.weightx = 0.0;                       //reset to default
	
        GridBagConstraints fieldConstraints = new GridBagConstraints();
	fieldConstraints.insets = new Insets( 2, 2, 2, 20 );
	fieldConstraints.anchor = GridBagConstraints.WEST;
	fieldConstraints.gridwidth = GridBagConstraints.REMAINDER;     //end row
	fieldConstraints.weightx = 1.0;
	
	JPanel loginPane = new JPanel();
	GridBagLayout gb = new GridBagLayout();
	loginPane.setLayout( gb );

	JLabel idLabel = new JLabel( "Username" );
	labelConstraints.insets = new Insets( 20, 20, 4, 4 );
	gb.setConstraints( idLabel, labelConstraints );
	loginPane.add( idLabel );
	idField = new JTextField( 15 );
	fieldConstraints.insets = new Insets( 20, 4, 4, 20 );
	gb.setConstraints( idField, fieldConstraints );
	loginPane.add( idField );

	JLabel passLabel = new JLabel( "Password" );
	labelConstraints.insets = new Insets( 4, 20, 4, 4 );
	gb.setConstraints( passLabel, labelConstraints );
	loginPane.add( passLabel );
	passField = new JPasswordField( 15 );
	fieldConstraints.insets = new Insets( 4, 4, 4, 20 );
	gb.setConstraints( passField, fieldConstraints );
	loginPane.add( passField );

        PhotovaultSettings settings = PhotovaultSettings.getSettings();
        Collection databases = settings.getDatabases();
        Vector dbNames = new Vector();
        Iterator iter = databases.iterator();
        while ( iter.hasNext() ) {
            PVDatabase db = (PVDatabase) iter.next();
            dbNames.add( db.getName() );
        }
        Collections.sort( dbNames, Collator.getInstance() );
        Object[] dbs = dbNames.toArray();
	JLabel dbLabel = new JLabel( "Database" );
	gb.setConstraints( dbLabel, labelConstraints );
	loginPane.add( dbLabel );
	dbField = new JComboBox( dbs );
        dbField.addItemListener( new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                if ( itemEvent.getStateChange() == ItemEvent.SELECTED ) {
                    // Enable/disable the username & password fields based on
                    // database type
                    Object item = itemEvent.getItem();
                    String dbName = item.toString();
                    PhotovaultSettings settings = PhotovaultSettings.getSettings();
                    PVDatabase db = settings.getDatabase( dbName );
                    if ( db != null ) {
                        setCredentialsEnabled( 
                                db.getDbDescriptor() instanceof MysqlDescriptor );
                    }
                }
            }
        });
        
        // Select the 1st item - this will also call the selection callback
        // and enable/disable credentials field.
        dbField.setSelectedIndex( -1 );
        dbField.setSelectedIndex( 0 );
	gb.setConstraints( dbField, fieldConstraints );
	loginPane.add( dbField );

 	getContentPane().add( loginPane, BorderLayout.NORTH );

        JButton newDbBtn = new JButton( "New database..." );
        newDbBtn.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                returnReason = RETURN_REASON_NEWDB;
                setVisible( false );
            }
        });
        
	JButton okBtn = new JButton( "OK" );
	okBtn.addActionListener( new ActionListener() {
		public void actionPerformed( ActionEvent e ) {
                    returnReason = RETURN_REASON_APPROVE;
                    setVisible( false );
		}
	    } );
		    
	JButton cancelBtn = new JButton( "Cancel" );
	cancelBtn.addActionListener( new ActionListener() {
		public void actionPerformed( ActionEvent e ) {
		    returnReason = RETURN_REASON_CANCEL;
                    setVisible( false );
		}
	    } );
	    
	JPanel buttonPane = new JPanel();
	buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
	buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	buttonPane.add(Box.createHorizontalGlue());
	buttonPane.add( newDbBtn );
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	buttonPane.add(okBtn);
	buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	buttonPane.add(cancelBtn);
	getContentPane().add( buttonPane, BorderLayout.SOUTH );

	getRootPane().setDefaultButton( okBtn );

	pack();
	// setResizable( false );
        
        // Center the dialog on screen
        int w = getSize().width;
        int h = getSize().height;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width-w)/2;
        int y = (screenSize.height-h)/2;
        setLocation( x, y );
                
    }

    

    JTextField idField;
    JPasswordField passField;
    JComboBox dbField;
}