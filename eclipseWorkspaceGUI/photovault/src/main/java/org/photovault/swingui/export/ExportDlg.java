/*
 * ExportDlg.java
 *
 * Created on July 15, 2006, 8:35 AM
 */

package org.photovault.swingui.export;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.photovault.swingui.ImageFilter;
import org.photovault.swingui.ImagePreview;

/**
 *
 * @author  harri
 */
public class ExportDlg extends javax.swing.JDialog {
    
    /** Creates new form ExportDlg */
    public ExportDlg(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        updateResolution();
    }
    
    static final int RES_ORIG = 1;
    static final int RES_WEB = 2;
    static final int RES_SCREEN = 3;
    static final int RES_CUSTOM = 4;
    
    int resType = RES_SCREEN;
    
    int imgWidth;
    int imgHeight;
    
    int retval;
    
    /**
     Return value if user selected to do the export operation.
     */
    public static final int EXPORT_OPTION = 1;
    /**
     Return value if user selected to cancel the export operation.
     */
    public static final int CANCEL_OPTION = 1;
   
    
    /**
     Updates resolution base on user's new preference selection
     */
    void updateResolution() {
        switch( resType ) {
            case RES_ORIG:
                imgWidth = -1;
                imgHeight = -1;
                customWidthFld.setText( "" );
                customHeightFld.setText( "" );
                break;
            case RES_WEB:
                imgWidth = 500;
                imgHeight = 500;
                customWidthFld.setText( "" + imgWidth );
                customHeightFld.setText( "" + imgHeight );
                break;
            case RES_SCREEN:
                imgWidth = 1024;
                imgHeight = 768;
                customWidthFld.setText( "" + imgWidth );
                customHeightFld.setText( "" + imgHeight );
                break;
            case RES_CUSTOM:
                break;
        }
                
    }
    /**
     Get the file name entered by user      
    */        
    public String getFilename() {
        return fnameFld.getText();
    }
    
    /**
     Sets the initial file name to show
     @param fname The new file name
     */
    public void setFilename( String fname ) {
        fnameFld.setText( fname );
    }
    
    /**
     Get the width for the exported photo(s) selected by user.
     @return The maximun width or -1 if user has selected to export using 
     original resolution.
     */
    public int getImgWidth() {
        int w = imgWidth;
        if ( resType == RES_CUSTOM ) {
            String strWidth = customWidthFld.getText();
            try {
                w = Integer.parseInt( strWidth );
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return w;
    }
    
    /**
     Get the heigth for the exported photo(s) selected by user.
     @return The maximun width or -1 if user has selected to export using 
     original resolution.
     */
    public int getImgHeight() {
        int h = imgHeight;
        if ( resType == RES_CUSTOM ) {
            String strHeight = customHeightFld.getText();
            try {
                h = Integer.parseInt( strHeight );
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return h;
    }
    
    /**
     Shows the modal ExportDlg.
     @return @see EXPORT_OPTION or @see CANCEL_OPTION
     */
    public int showDialog() {
        setVisible( true );
        return retval;
    }
    
    /**
     Check that the information entered is consistent
     @return <code>true</code> if everything is OK, otherwise displays an error
     message and returns <code>false</code>
     */     
    boolean validateData() {
        File f = new File( getFilename() );
        if ( f.exists() ) {
            if ( JOptionPane.showConfirmDialog( this, f.getAbsolutePath() + 
                    " exists.\nDo you want to replace it?", "Replace file?",
                    JOptionPane.YES_NO_OPTION ) != JOptionPane.YES_OPTION ) {
                return false;
            }
        }
        if ( f.isDirectory() ) {
            JOptionPane.showMessageDialog( this, f.getAbsolutePath() + 
                    " is a directory", "Cannot create file", 
                    JOptionPane.ERROR_MESSAGE );
            return false;
        }
        File dir = f.getParentFile();
        if ( dir == null || !dir.exists() ) {
            if ( JOptionPane.showConfirmDialog( this, "Directory " + 
                    dir.getAbsolutePath() + 
                    " does not exist. Create it?", "Create directory?",
                    JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
                if ( dir == null || !dir.mkdirs() ) {
                    return false;
                }
            } else {
                // User choose not to create the directory
                return false;
            }           
        }
        return true;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        resBtnGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        fnameFld = new javax.swing.JTextField();
        selectFnameBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        origSizeBtn = new javax.swing.JRadioButton();
        webResBtn = new javax.swing.JRadioButton();
        screenResBtn = new javax.swing.JRadioButton();
        customResBtn = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        customWidthFld = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        customHeightFld = new javax.swing.JTextField();
        exportBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Export photo");
        setName("exportDlg");
        jLabel1.setText("File name");

        fnameFld.setText("jTextField1");

        selectFnameBtn.setText("Select");
        selectFnameBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectFnameBtnActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Resolution"));
        jPanel1.setName("Resolution");
        resBtnGroup.add(origSizeBtn);
        origSizeBtn.setText("Original resolution");
        origSizeBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        origSizeBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        origSizeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                origSizeBtnActionPerformed(evt);
            }
        });

        resBtnGroup.add(webResBtn);
        webResBtn.setText("Web (max. 500x500 pixels)");
        webResBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        webResBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        webResBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webResBtnActionPerformed(evt);
            }
        });

        resBtnGroup.add(screenResBtn);
        screenResBtn.setSelected(true);
        screenResBtn.setText("Screen (max. 1024x768 pixels)");
        screenResBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        screenResBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        screenResBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                screenResBtnActionPerformed(evt);
            }
        });

        resBtnGroup.add(customResBtn);
        customResBtn.setText("Custom, max.");
        customResBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        customResBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        customResBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customResBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("width");

        jLabel3.setText("x height");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(customResBtn)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(customWidthFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(customHeightFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(50, 50, 50))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(origSizeBtn)
                        .addContainerGap(247, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(webResBtn)
                        .addContainerGap(198, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(screenResBtn)
                        .addContainerGap(176, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(origSizeBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(webResBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(screenResBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(customResBtn)
                        .add(jLabel2)
                        .add(customWidthFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel3)
                        .add(customHeightFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        exportBtn.setText("Export");
        exportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fnameFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 221, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(selectFnameBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(exportBtn)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelBtn)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(fnameFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(selectFnameBtn))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 8, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelBtn)
                    .add(exportBtn))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        retval = CANCEL_OPTION;
        setVisible( false );
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void exportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportBtnActionPerformed
        if ( validateData() ) {
            retval = EXPORT_OPTION;
            setVisible( false );
        }
    }//GEN-LAST:event_exportBtnActionPerformed

    private void customResBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customResBtnActionPerformed
        resType = RES_CUSTOM;
        customHeightFld.setEnabled( true );
        customWidthFld.setEnabled( true );
    }//GEN-LAST:event_customResBtnActionPerformed

    private void screenResBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_screenResBtnActionPerformed
        resType = RES_SCREEN;
        customHeightFld.setEnabled( false );
        customWidthFld.setEnabled( false );
        updateResolution();
    }//GEN-LAST:event_screenResBtnActionPerformed

    private void webResBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webResBtnActionPerformed
        resType = RES_WEB;
        customHeightFld.setEnabled( false );
        customWidthFld.setEnabled( false );
        updateResolution();
    }//GEN-LAST:event_webResBtnActionPerformed

    /**
     This is called when user selects original resolution.
     */
    private void origSizeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_origSizeBtnActionPerformed
        resType = RES_ORIG;
        customHeightFld.setEnabled( false );
        customWidthFld.setEnabled( false );
        updateResolution();
    }//GEN-LAST:event_origSizeBtnActionPerformed

    /*
     This is called when "Select file name" button is pressed. Shows file selection
     dialog and updates fine name if one is selected.
     */
    private void selectFnameBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectFnameBtnActionPerformed
        // Show the file chooser dialog
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter( new ImageFilter() );
        fc.setAccessory( new ImagePreview( fc ) );
        
        int retval = fc.showDialog( this, "OK" );
        if ( retval == JFileChooser.APPROVE_OPTION ) {
            File exportFile = fc.getSelectedFile();
            fnameFld.setText( exportFile.getPath() );
        }
    }//GEN-LAST:event_selectFnameBtnActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExportDlg(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTextField customHeightFld;
    private javax.swing.JRadioButton customResBtn;
    private javax.swing.JTextField customWidthFld;
    private javax.swing.JButton exportBtn;
    private javax.swing.JTextField fnameFld;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton origSizeBtn;
    private javax.swing.ButtonGroup resBtnGroup;
    private javax.swing.JRadioButton screenResBtn;
    private javax.swing.JButton selectFnameBtn;
    private javax.swing.JRadioButton webResBtn;
    // End of variables declaration//GEN-END:variables
    
}
