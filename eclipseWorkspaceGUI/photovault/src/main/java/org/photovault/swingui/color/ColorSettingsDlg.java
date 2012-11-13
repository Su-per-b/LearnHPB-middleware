/*
  Copyright (c) 2006-2007 Harri Kaimio
  
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

package org.photovault.swingui.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.ColorModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.media.jai.Histogram;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.ColorProfileDesc;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.dcraw.RawImage;
import org.photovault.dcraw.RawImageChangeEvent;
import org.photovault.dcraw.RawImageChangeListener;
import org.photovault.dcraw.RawSettingsFactory;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ColorCurve;
import org.photovault.image.ImageRenderingListener;
import org.photovault.image.PhotovaultImage;
import org.photovault.imginfo.FuzzyDate;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoFields;
import org.photovault.swingui.JAIPhotoViewer;
import org.photovault.swingui.selection.PhotoSelectionController;
import org.photovault.swingui.PhotoViewChangeEvent;
import org.photovault.swingui.PhotoViewChangeListener;
import org.photovault.swingui.PreviewImageView;
import org.photovault.swingui.RawPhotoView;

/**
 * Dialog box for altering color settings of a photo. Currently only
 * raw images are supporter but in future support for normal image files
 * should be added.
 * @author Harri Kaimio
 */
public class ColorSettingsDlg extends javax.swing.JDialog 
        implements RawImageChangeListener, RawPhotoView, PhotoViewChangeListener, 
        PreviewImageView, ImageRenderingListener {

    private static Log log = LogFactory.getLog( ColorSettingsDlg.class.getName() );
    
    /**
     * Creates new form ColorSettingsDlg
     * @param parent Parent frame of this dialog
     * @param modal Whether the dialog is displayed as modal
     * @param photos Array of the photos that will be edited
     */
    public ColorSettingsDlg(java.awt.Frame parent, ColorSettingsDlgController ctrl, boolean modal, PhotoInfo[] photos ) {
        super(parent, modal);
        this.ctrl = ctrl;
        initComponents();
        checkIsRawPhoto();
        final ColorSettingsDlg staticThis = this;
        this.colorCurvePanel1.addListener( new ColorCurveChangeListener() {
            public void colorCurveChanging(ColorCurvePanel p, ColorCurve c) {
            }

            public void colorCurveChangeCompleted(ColorCurvePanel p, ColorCurve c) {
                staticThis.colorCurveChanged( c );
            }
        });
    }

    /**
     Controller for the photos that will be edited
     */
    PhotoSelectionController ctrl = null;
    RawConversionSettings rawSettings = null;
    
    /**
     Color curves, in order value, red, green, blue, saturation
     */
    ColorCurve[] colorCurves = new ColorCurve[5];
    
    /**
     Curves that will be drawn as references for each channel
     */
    List refCurves[] = new ArrayList[5];
    
    /**
     Names of the color channels
     */
    static String[] colorCurveNames = {"value", "red", "green", "blue", "saturation"};
    
    /**
     Colors for the curves
     */
    static Color[] curveColors = {
        Color.BLACK, 
        Color.RED, 
        new Color(0.0f, 0.7f, 0.0f), 
        Color.BLUE,
        new Color( 0.2f, 0.2f, 0.0f )
    };

    /**
     Colors for the reference curves
     */
    static Color[] refCurveColors = {
        Color.GRAY,
        Color.PINK,
        new Color( 0.5f, 1.0f, 0.5f ),
        new Color( 0.5f, 0.5f, 1.0f ),
        new Color( 0.5f, 0.5f, 0.2f )
    };
    
    /**
     What histogram is shown with each channel
     */
    static String[] channelHistType = {
        PhotovaultImage.HISTOGRAM_RGB_CHANNELS,
        PhotovaultImage.HISTOGRAM_RGB_CHANNELS,
        PhotovaultImage.HISTOGRAM_RGB_CHANNELS,
        PhotovaultImage.HISTOGRAM_RGB_CHANNELS,
        PhotovaultImage.HISTOGRAM_IHS_CHANNELS,
    };
    /**
     The band of the histogram defined in @see channelHistTypes that is
     associated with each channel. -1 means that all histogram bands should be
     shown.
     */       
    static int[] channelHistBand = {
        -1, 0, 1, 2, 2
    };

    /**
      Color curve currently displayed
     */
    int currentColorCurve = 0;
    
    protected void applyChanges() {
        ctrl.save();
        photoChanged = true;
    }
    
    /**
     Discard changes made in the dialog & reload model values
     */
    protected void discardChanges() {
        // TODO: Hibernate refactoring
        ctrl.discard();
    }
    
    static class ModelValueAnnotation extends JPanel {
        public ModelValueAnnotation( Color color ) {
            super();
            this.color = color;
            Polygon p = new Polygon();
            p.addPoint( 5, 0 );
            p.addPoint( 9, 5 );
            p.addPoint( 5, 10 );
            p.addPoint( 1, 5 );
            shape = p;
        }
        
        Color color;
        Polygon shape;
        
        public Dimension getPreferredSize() {
            return new Dimension ( 10, 10 );
        }
        
        public void paint( Graphics g ) {
            Graphics g2 = g.create();
            g2.setColor( color );
            g2.fillPolygon( shape );
        }
    }
    
    /**
     Size of the slider labels (Size of the lowest & highest labels will affect 
     the length of slider track, therefore they must be of same size in all 
     sliders.
     */
    Dimension sliderLabelDimension = null;
    
    /**
     Get a standard sized label for the sliders.
     @param txt Text to show in label.
     @return Label.
     */
    private JComponent getSliderLabel( String txt ) {
        if ( sliderLabelDimension == null ) {
            JLabel maxLabel = new JLabel( String.valueOf( 12000 ) );
            sliderLabelDimension = maxLabel.getPreferredSize();
        }
        JLabel l = new JLabel( txt, SwingConstants.CENTER );
        l.setPreferredSize( sliderLabelDimension );
        return l;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fieldSliderCombo1 = new org.photovault.swingui.color.FieldSliderCombo();
        fieldSliderCombo2 = new org.photovault.swingui.color.FieldSliderCombo();
        dlgControlPane = new javax.swing.JPanel();
        applyBtn = new javax.swing.JButton();
        discardBtn = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();
        okBtn = new javax.swing.JButton();
        colorSettingTabs = new javax.swing.JTabbedPane();
        rawControlsPane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        evCorrSlider = new org.photovault.swingui.color.FieldSliderCombo();
        {
            Hashtable sliderLabels = new Hashtable();
            sliderLabels.put( new Double( -2.0 ), getSliderLabel( "-2" ) );
            sliderLabels.put( new Double( -1.0 ), new JLabel("-1") );
            sliderLabels.put( new Double(  0 ), new JLabel("0") );
            sliderLabels.put( new Double( 1.0 ), new JLabel("1") );
            sliderLabels.put( new Double( 1.99 ), getSliderLabel( "2" ) );
            evCorrSlider.setLabelTable( sliderLabels );
            evCorrSlider.putClientProperty("JSlider.isFilled", Boolean.FALSE);
        }
        jLabel3 = new javax.swing.JLabel();
        hlightCompSlider = new org.photovault.swingui.color.FieldSliderCombo();
        {
            Hashtable compressSliderLabels = new Hashtable();
            compressSliderLabels.put( new Double( -1.0 ), getSliderLabel( Double.toString( -1.0 ) ) );
            compressSliderLabels.put( new Double( 0.0 ), getSliderLabel( Double.toString( 0.0 ) ) );
            compressSliderLabels.put( new Double( 1.0 ), getSliderLabel( Double.toString( 1.0 ) ) );
            compressSliderLabels.put( new Double( 2.0 ), getSliderLabel( Double.toString( 2.0 ) ) );
            hlightCompSlider.setLabelTable( compressSliderLabels );
            hlightCompSlider.putClientProperty("JSlider.isFilled", Boolean.FALSE);
        }
        jLabel5 = new javax.swing.JLabel();
        blackLevelSlider = new org.photovault.swingui.color.FieldSliderCombo();
        {
            Hashtable sliderLabels = new Hashtable();
            sliderLabels.put( new Double( -500.0 ), getSliderLabel( Integer.toString( -500 ) ) );
            sliderLabels.put( new Double( 0.0 ), getSliderLabel( Integer.toString( 0 ) ) );
            sliderLabels.put( new Double( 500.0 ), getSliderLabel( Integer.toString( 500 ) ) );
            sliderLabels.put( new Double( 1000.0 ), getSliderLabel( Integer.toString( 1000 ) ) );
            blackLevelSlider.setLabelTable( sliderLabels );
            blackLevelSlider.putClientProperty("JSlider.isFilled", Boolean.FALSE);
        }
        jLabel2 = new javax.swing.JLabel();
        ctempSlider = new org.photovault.swingui.color.FieldSliderCombo();
        {
            Hashtable sliderLabels = new Hashtable();
            sliderLabels.put( new Double( 2000.0 ), getSliderLabel( Integer.toString( 2000 ) ) );
            sliderLabels.put( new Double( 5000.0 ), getSliderLabel( Integer.toString( 5000 ) ) );
            sliderLabels.put( new Double( 8000.0 ), getSliderLabel( Integer.toString( 8000 ) ) );
            sliderLabels.put( new Double( 11000.0 ), getSliderLabel( Integer.toString( 11000 ) ) );
            sliderLabels.put( new Double( 14000.0 ), getSliderLabel( Integer.toString( 14000 ) ) );
            ctempSlider.setLabelTable( sliderLabels );
            ctempSlider.putClientProperty("JSlider.isFilled", Boolean.FALSE);
        }
        jLabel4 = new javax.swing.JLabel();
        greenGainSlider = new org.photovault.swingui.color.FieldSliderCombo();
        Hashtable greenGainLabels = new Hashtable();
        greenGainLabels.put( new Double( -1.0 ), getSliderLabel("-1") );
        greenGainLabels.put( new Double( 0.0 ), getSliderLabel("0") );
        greenGainLabels.put( new Double( 1.0 ), getSliderLabel("+1") );
        greenGainSlider.setLabelTable( greenGainLabels );
        greenGainSlider.putClientProperty("JSlider.isFilled", Boolean.FALSE);
        rawHistogramPane = rawHistogramPane = new HistogramPane();
        jLabel6 = new javax.swing.JLabel();
        hlightRecoverySlider = new org.photovault.swingui.color.FieldSliderCombo();
        jLabel7 = new javax.swing.JLabel();
        waveletDenoiseSlider = new org.photovault.swingui.color.FieldSliderCombo();
        whiteLevelSlider = new org.photovault.swingui.color.FieldSliderCombo();
        jLabel8 = new javax.swing.JLabel();
        colorSettingControls = new javax.swing.JPanel();
        colorCurveSelectionCombo = new javax.swing.JComboBox();
        colorCurvePanel1 = new org.photovault.swingui.color.ColorCurvePanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Colors");

        applyBtn.setAction(ctrl.getActionAdapter( "save"));
        applyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyBtnActionPerformed(evt);
            }
        });

        discardBtn.setAction(ctrl.getActionAdapter( "discard" ));
        discardBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discardBtnActionPerformed(evt);
            }
        });

        closeBtn.setText("Close");
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });

        okBtn.setText("OK");
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout dlgControlPaneLayout = new org.jdesktop.layout.GroupLayout(dlgControlPane);
        dlgControlPane.setLayout(dlgControlPaneLayout);
        dlgControlPaneLayout.setHorizontalGroup(
            dlgControlPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, dlgControlPaneLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .add(okBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(applyBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(discardBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(closeBtn))
        );
        dlgControlPaneLayout.setVerticalGroup(
            dlgControlPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, dlgControlPaneLayout.createSequentialGroup()
                .add(dlgControlPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(closeBtn)
                    .add(discardBtn)
                    .add(applyBtn)
                    .add(okBtn))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("EV correction");

        evCorrSlider.setFractionDigits(2);
        evCorrSlider.setMajorTickSpacing(1.0);
        evCorrSlider.setMaximum(2.0);
        evCorrSlider.setMinimum(-2.0);
        evCorrSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                evCorrSliderStateChanged(evt);
            }
        });

        jLabel3.setText("Compress highlights");

        hlightCompSlider.setMajorTickSpacing(1.0);
        hlightCompSlider.setMaximum(2.0);
        hlightCompSlider.setMinimum(-1.0);
        hlightCompSlider.setValue(0.0);
        hlightCompSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hlightCompSliderStateChanged(evt);
            }
        });

        jLabel5.setText("Black level");

        blackLevelSlider.setFractionDigits(0);
        blackLevelSlider.setMajorTickSpacing(500.0);
        blackLevelSlider.setMaximum(1000.0);
        blackLevelSlider.setMinimum(-500.0);
        blackLevelSlider.setValue(0.0);
        blackLevelSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                blackLevelSliderStateChanged(evt);
            }
        });

        jLabel2.setText("Color Temperature");

        ctempSlider.setFractionDigits(0);
        ctempSlider.setMajorTickSpacing(3000.0);
        ctempSlider.setMaximum(14000.0);
        ctempSlider.setMinimum(2000.0);
        ctempSlider.setValue(5500.0);
        ctempSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ctempSliderStateChanged(evt);
            }
        });

        jLabel4.setText("Green");

        greenGainSlider.setFractionDigits(2);
        greenGainSlider.setMajorTickSpacing(1.0);
        greenGainSlider.setMinimum(-1.0);
        greenGainSlider.setValue(0.0);
        greenGainSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                greenGainSliderStateChanged(evt);
            }
        });

        rawHistogramPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout rawHistogramPaneLayout = new org.jdesktop.layout.GroupLayout(rawHistogramPane);
        rawHistogramPane.setLayout(rawHistogramPaneLayout);
        rawHistogramPaneLayout.setHorizontalGroup(
            rawHistogramPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 423, Short.MAX_VALUE)
        );
        rawHistogramPaneLayout.setVerticalGroup(
            rawHistogramPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        jLabel6.setText("Recover highlights");

        hlightRecoverySlider.setFractionDigits(0);
        hlightRecoverySlider.setMaximum(7.0);
        hlightRecoverySlider.setName("hlightRecoverySlider"); // NOI18N
        hlightRecoverySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hlightRecoverySliderStateChanged(evt);
            }
        });

        jLabel7.setText("Noise reduction");

        waveletDenoiseSlider.setFractionDigits(0);
        waveletDenoiseSlider.setMajorTickSpacing(400.0);
        waveletDenoiseSlider.setMaximum(1200.0);
        waveletDenoiseSlider.setValue(0.0);
        waveletDenoiseSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                waveletDenoiseSliderStateChanged(evt);
            }
        });

        whiteLevelSlider.setMaximum(65536.0);
        whiteLevelSlider.setMinimum(1000.0);
        whiteLevelSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                whiteLevelSliderStateChanged(evt);
            }
        });

        jLabel8.setText("White level");

        org.jdesktop.layout.GroupLayout rawControlsPaneLayout = new org.jdesktop.layout.GroupLayout(rawControlsPane);
        rawControlsPane.setLayout(rawControlsPaneLayout);
        rawControlsPaneLayout.setHorizontalGroup(
            rawControlsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(rawControlsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(rawControlsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rawControlsPaneLayout.createSequentialGroup()
                        .add(hlightCompSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 288, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(rawControlsPaneLayout.createSequentialGroup()
                        .add(rawControlsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(rawHistogramPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel1)
                            .add(jLabel3)
                            .add(jLabel7)
                            .add(jLabel6)
                            .add(jLabel4)
                            .add(jLabel2)
                            .add(rawControlsPaneLayout.createSequentialGroup()
                                .add(rawControlsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(whiteLevelSlider, 0, 0, Short.MAX_VALUE)
                                    .add(waveletDenoiseSlider, 0, 0, Short.MAX_VALUE)
                                    .add(hlightRecoverySlider, 0, 0, Short.MAX_VALUE)
                                    .add(greenGainSlider, 0, 0, Short.MAX_VALUE)
                                    .add(ctempSlider, 0, 0, Short.MAX_VALUE)
                                    .add(blackLevelSlider, 0, 0, Short.MAX_VALUE)
                                    .add(evCorrSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                                .add(63, 63, 63)
                                .add(jLabel5)))
                        .add(372, 372, 372))
                    .add(rawControlsPaneLayout.createSequentialGroup()
                        .add(jLabel8)
                        .addContainerGap(237, Short.MAX_VALUE))))
        );
        rawControlsPaneLayout.setVerticalGroup(
            rawControlsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(rawControlsPaneLayout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(evCorrSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(hlightCompSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel8)
                .add(rawControlsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rawControlsPaneLayout.createSequentialGroup()
                        .add(62, 62, 62)
                        .add(jLabel5))
                    .add(rawControlsPaneLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(whiteLevelSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(18, 18, 18)
                .add(blackLevelSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ctempSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(greenGainSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(hlightRecoverySlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(waveletDenoiseSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(rawHistogramPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        colorSettingTabs.addTab("Raw conversion", rawControlsPane);

        colorCurveSelectionCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Value", "Red", "Green", "Blue", "Saturation" }));
        colorCurveSelectionCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorCurveSelectionComboActionPerformed(evt);
            }
        });

        colorCurvePanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.white, java.awt.Color.lightGray, null, null));

        org.jdesktop.layout.GroupLayout colorCurvePanel1Layout = new org.jdesktop.layout.GroupLayout(colorCurvePanel1);
        colorCurvePanel1.setLayout(colorCurvePanel1Layout);
        colorCurvePanel1Layout.setHorizontalGroup(
            colorCurvePanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 289, Short.MAX_VALUE)
        );
        colorCurvePanel1Layout.setVerticalGroup(
            colorCurvePanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout colorSettingControlsLayout = new org.jdesktop.layout.GroupLayout(colorSettingControls);
        colorSettingControls.setLayout(colorSettingControlsLayout);
        colorSettingControlsLayout.setHorizontalGroup(
            colorSettingControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(colorSettingControlsLayout.createSequentialGroup()
                .add(colorSettingControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(colorSettingControlsLayout.createSequentialGroup()
                        .add(155, 155, 155)
                        .add(colorCurveSelectionCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, colorSettingControlsLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(colorCurvePanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        colorSettingControlsLayout.setVerticalGroup(
            colorSettingControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(colorSettingControlsLayout.createSequentialGroup()
                .add(25, 25, 25)
                .add(colorCurveSelectionCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(colorCurvePanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(308, Short.MAX_VALUE))
        );

        colorSettingTabs.addTab("Colors", colorSettingControls);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(198, Short.MAX_VALUE)
                .add(dlgControlPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(colorSettingTabs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(colorSettingTabs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(dlgControlPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     Called when use selects a different color curve for editing
     */    
    private void colorCurveSelectionComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorCurveSelectionComboActionPerformed
        int i = colorCurveSelectionCombo.getSelectedIndex();
        showCurve( i );
    }//GEN-LAST:event_colorCurveSelectionComboActionPerformed

    private void greenGainSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_greenGainSliderStateChanged
        double greenEv = greenGainSlider.getValue();
        double newGain = Math.pow( 2, greenEv );
        if ( Math.abs( newGain - this.greenGain ) > 0.005 ) {
            greenGain= newGain;
            ctrl.viewChanged( this, PhotoInfoFields.RAW_GREEN, newGain );
            if ( rawSettings != null ) {
                RawSettingsFactory f = new RawSettingsFactory( rawSettings );
                f.setGreenGain( newGain );
                try {
                    rawSettings = f.create();
                } catch (PhotovaultException ex) {
                    log.error( "Error setting color temp: " + ex.getMessage() );
                }
                firePreviewChangeEvent( new RawSettingsPreviewEvent(
                        this, ctrl.getPhotos(), rawSettings ) );
                reloadHistogram();
            }
        }
        
    }//GEN-LAST:event_greenGainSliderStateChanged
    
    /**
     * Color temperature slider value was changed
     * @param evt Event describing the change
     */
    private void ctempSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ctempSliderStateChanged
        double newCTemp = ctempSlider.getValue();
        if ( Math.abs( newCTemp - this.colorTemp ) > 10 ) {
            colorTemp= newCTemp;
            ctrl.viewChanged( this, PhotoInfoFields.RAW_CTEMP, newCTemp );
            if ( rawSettings != null ) {
                RawSettingsFactory f = new RawSettingsFactory( rawSettings );
                f.setColorTemp( newCTemp );
                try {
                    rawSettings = f.create();
                } catch (PhotovaultException ex) {
                    log.error( "Error setting color temp: " + ex.getMessage() );
                }
                firePreviewChangeEvent( new RawSettingsPreviewEvent(
                        this, ctrl.getPhotos(), rawSettings ) );
                reloadHistogram();
            }
        }
    }//GEN-LAST:event_ctempSliderStateChanged

    private void blackLevelSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_blackLevelSliderStateChanged
        int newBlack = (int) blackLevelSlider.getValue();
        if (  Math.abs( newBlack - black ) > 0.05 ) {

            black = newBlack;
            ctrl.viewChanged( this, PhotoInfoFields.RAW_BLACK_LEVEL, black );
            if ( rawSettings != null ) {
                RawSettingsFactory f = new RawSettingsFactory( rawSettings );
                f.setBlack( newBlack );
                try {
                    rawSettings = f.create();
                } catch (PhotovaultException ex) {
                    log.error( "Error setting black: " + ex.getMessage() );
                }
                firePreviewChangeEvent( new RawSettingsPreviewEvent(
                        this, ctrl.getPhotos(), rawSettings ) );
                reloadHistogram();
            }
        }

    }//GEN-LAST:event_blackLevelSliderStateChanged

    private void evCorrSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_evCorrSliderStateChanged
        double newEv = evCorrSlider.getValue();
        if ( Math.abs( newEv - evCorr ) > 0.05 ) {
            evCorr = newEv;
            ctrl.viewChanged( this, PhotoInfoFields.RAW_EV_CORR, evCorr );
            if ( rawSettings != null ) {
                RawSettingsFactory f = new RawSettingsFactory( rawSettings );
                f.setEvCorr( newEv );
                try {
                    rawSettings = f.create();
                } catch (PhotovaultException ex) {
                    log.error( "Error setting EV correction: " + ex.getMessage() );
                }
                firePreviewChangeEvent( new RawSettingsPreviewEvent(
                        this, ctrl.getPhotos(), rawSettings ) );
                reloadHistogram();
            }
        }
    }//GEN-LAST:event_evCorrSliderStateChanged

    private void hlightCompSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_hlightCompSliderStateChanged
        double newHlightComp = hlightCompSlider.getValue();
        if ( (Math.abs( newHlightComp - this.hlightComp ) > 0.001 ) ) {
            this.hlightComp = newHlightComp;
            ctrl.viewChanged( this, PhotoInfoFields.RAW_HLIGHT_COMP, hlightComp );
            if ( rawSettings != null ) {
                RawSettingsFactory f = new RawSettingsFactory( rawSettings );
                f.setHlightComp( newHlightComp );
                try {
                    rawSettings = f.create();
                } catch (PhotovaultException ex) {
                    log.error( "Error setting green gain: " + ex.getMessage() );
                }
                firePreviewChangeEvent( new RawSettingsPreviewEvent(
                        this, ctrl.getPhotos(), rawSettings ) );
                reloadHistogram();
            }
        }
        
    }//GEN-LAST:event_hlightCompSliderStateChanged
    

    /**
     * Close button was pressed. Close the window
     * @param evt The ButtonEvent
     */
    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        discardChanges();
        setVisible( false );
    }//GEN-LAST:event_closeBtnActionPerformed

    /**
     * Discard button was pressed. Instruct controller to discard changes made
     * @param evt The ButtonEvent
     */
    private void discardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discardBtnActionPerformed
        discardChanges();
    }//GEN-LAST:event_discardBtnActionPerformed

    /**
     * Apply button was pressed. Save all changes made.
     * @param evt The ButtonEvent
     */
    private void applyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyBtnActionPerformed
        applyChanges();
    }//GEN-LAST:event_applyBtnActionPerformed

    /**
     * OK button was pressed. Save changes & close window
     * @param evt Event
     */
    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
        applyChanges();
        setVisible( false );
    }//GEN-LAST:event_okBtnActionPerformed

    private void hlightRecoverySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_hlightRecoverySliderStateChanged
        int newRecovery = (int) hlightRecoverySlider.getValue();
        if (  newRecovery != hlightRecovery ) {

            hlightRecovery = newRecovery;
            ctrl.viewChanged( this, PhotoInfoFields.RAW_HLIGHT_RECOVERY, hlightRecovery );
            if ( rawSettings != null ) {
                RawSettingsFactory f = new RawSettingsFactory( rawSettings );
                f.setHlightRecovery( hlightRecovery );
                try {
                    rawSettings = f.create();
                } catch (PhotovaultException ex) {
                    log.error( "Error setting black: " + ex.getMessage() );
                }
                firePreviewChangeEvent( new RawSettingsPreviewEvent(
                        this, ctrl.getPhotos(), rawSettings ) );
                reloadHistogram();
            }
        }
    }//GEN-LAST:event_hlightRecoverySliderStateChanged

    private void waveletDenoiseSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_waveletDenoiseSliderStateChanged
        float newDenoise = (float) waveletDenoiseSlider.getValue();
        if (  newDenoise != denoise ) {

            denoise = newDenoise;
            ctrl.viewChanged( this, PhotoInfoFields.RAW_WAVELET_DENOISE_THRESHOLD, denoise );
            if ( rawSettings != null ) {
                RawSettingsFactory f = new RawSettingsFactory( rawSettings );
                f.setWaveletThreshold( denoise );
                try {
                    rawSettings = f.create();
                } catch (PhotovaultException ex) {
                    log.error( "Error setting black: " + ex.getMessage() );
                }
                firePreviewChangeEvent( new RawSettingsPreviewEvent(
                        this, ctrl.getPhotos(), rawSettings ) );
                reloadHistogram();
            }
        }
    }//GEN-LAST:event_waveletDenoiseSliderStateChanged

    private void whiteLevelSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_whiteLevelSliderStateChanged
       int newWhite = (int) whiteLevelSlider.getValue();
        if (  Math.abs( newWhite - white ) > 1 ) {

            white = newWhite;
            ctrl.viewChanged( this, PhotoInfoFields.RAW_WHITE_LEVEL, white );
            if ( rawSettings != null ) {
                RawSettingsFactory f = new RawSettingsFactory( rawSettings );
                f.setWhite( newWhite );
                try {
                    rawSettings = f.create();
                } catch (PhotovaultException ex) {
                    log.error( "Error setting black: " + ex.getMessage() );
                }
                firePreviewChangeEvent( new RawSettingsPreviewEvent(
                        this, ctrl.getPhotos(), rawSettings ) );
                reloadHistogram();
            }
        }
    }//GEN-LAST:event_whiteLevelSliderStateChanged

    /**
     Called by colorCurvePane when use has edited the curve
     @param c the curve after editing.
     */
    private void colorCurveChanged( ColorCurve c ) {
        colorCurves[currentColorCurve] = c;
        switch ( currentColorCurve ) {
            case 0:
                ctrl.viewChanged( this, PhotoInfoFields.COLOR_CURVE_VALUE, c );
                break;
            case 1:
                ctrl.viewChanged( this, PhotoInfoFields.COLOR_CURVE_RED, c );
                break;
            case 2:
                ctrl.viewChanged( this, PhotoInfoFields.COLOR_CURVE_GREEN, c );
                break;
            case 3:
                ctrl.viewChanged( this, PhotoInfoFields.COLOR_CURVE_BLUE, c );
                break;
            case 4:
                ctrl.viewChanged( this, PhotoInfoFields.COLOR_CURVE_SATURATION, c );
                break;                
            default:
                // Should never happend
                break;
        }
    }
    

    RawImage rawPreviewImage = null;
    RawImage previewCtrlImage = null;
    PhotoInfo previewCtrlPhoto = null;
    
    /**
     * Set the preview image into which the changes done in dialog are
     * immediately applied.
     * @param ri Test image
     */
    public void setPreview( RawImage ri ) {
        rawPreviewImage = ri;
        if ( ri != null ) {
            ((HistogramPane) rawHistogramPane).setHistogram( ri.getHistogramBins() );
            ((HistogramPane) rawHistogramPane).setTransferGraph( ri.getGammaLut() );
        }
    }
    
    /**
     Send event to all preview windows that the raw settings have been changed.
     @param e Event that describes the change
     */
    void firePreviewChangeEvent( RawSettingsPreviewEvent e ) {
        if ( previewCtrl != null ) {
            previewCtrl.previewRawSettingsChanged( e );
        }        
    }
    
    /**
     Inform preview control that saturation has changed
     @param newSat New saturation value
     */
    void notifyPreviewSaturationChange( double newSat ) {
        if ( previewCtrl != null ) {
            previewCtrl.setSaturation( newSat );
        }        
    }
    
    
    /**
     Reload the histogram data from RawImage displayed in preview control if
     the image matches to current model. If this is not the case, disable 
     histogram.
     */
    void reloadHistogram() {
        if ( previewCtrl == null ) {
            return;
        }
        PhotoInfo[] model = ctrl.getPhotos();
        PhotoInfo photo = previewCtrl.getPhoto();
        if ( photo != null &&
                model != null &&
                model.length == 1 &&
                model[0] == photo) {
            byte[] lut = previewCtrl.getRawConversionLut();
            ((HistogramPane)rawHistogramPane).setTransferGraph( lut );
            ((HistogramPane)rawHistogramPane).setHistogram(
                    previewCtrl.getRawImageHistogram() );
        } else {
            /* Preview control is not displaying image that matches to the current
               model, so we do not have histogram data available.
             */
            ((HistogramPane)rawHistogramPane).setTransferGraph( null );
            ((HistogramPane)rawHistogramPane).setHistogram( null );
            
        }
    }
    
    JAIPhotoViewer previewCtrl = null;
    
    /**
     Checks if the model supports raw conversion settings and disables
     or enables controls based on this. Raw settings are only allowed if all 
     photos controlled bu ctrl are raw photos.
     */
    void checkIsRawPhoto() {
        PhotoInfo[] photos = ctrl.getPhotos();
        boolean isRaw = true;
        if ( photos != null ) {
            /*
             If any of the photos is not a raw image we cannot use raw controls
             */
            for ( int n = 0; n < photos.length; n++ ) {
                if ( photos[n].getRawSettings() == null ) {
                    isRaw = false;
                    break;
                }                
            }
        } else {
            isRaw = false;
        }
        setRawControlsEnabled( isRaw );
    }

    /**
     Enable or disble all controls that affect raw conversion settings
     @param enable <code>true</code> if the controls should be enabled
     */
    void setRawControlsEnabled( boolean enable ) {
        colorSettingTabs.setEnabledAt( 0, enable );
        this.rawControlsPane.setEnabled( enable );
        evCorrSlider.setEnabled( enable );
        hlightCompSlider.setEnabled( enable );
        hlightRecoverySlider.setEnabled( enable );
        ctempSlider.setEnabled( enable );
        greenGainSlider.setEnabled( enable );
        blackLevelSlider.setEnabled( enable );
        whiteLevelSlider.setEnabled( enable );
        waveletDenoiseSlider.setEnabled( enable );
        if ( !enable && colorSettingTabs.getSelectedIndex() == 0 ) {
            colorSettingTabs.setSelectedIndex( 1 );
        }
    }
        
    /**
     Show a different color curve in colorCurvePane
     */
    private void showCurve( int chan ) {
        currentColorCurve = chan;
        if ( colorCurves[chan] == null ) {
            // The curve is not present in current profile
            ColorCurve c = colorCurves[chan] = new ColorCurve();
            c.addPoint( 0.0, 0.0 );
            c.addPoint( 1.0, 1.0 );
        }
        colorCurvePanel1.setCurve( colorCurves[chan], curveColors[chan] );
        colorCurvePanel1.clearReferenceCurves();
        if ( refCurves[chan] != null ) {
            for ( Object c : refCurves[chan] ) {
                if ( c == null ) {
                    c = new ColorCurve();
                }
                colorCurvePanel1.addReferenceCurve( (ColorCurve) c, refCurveColors[chan] );
            }
        }
        
        // Draw also the curves for other channels
        for ( int n = 0 ; n < colorCurves.length ; n++ ) {
            if ( n != chan ) {
                ColorCurve c = colorCurves[n];
                if ( c == null ) {
                    c = new ColorCurve();
                }
                colorCurvePanel1.addReferenceCurve( c, refCurveColors[n] );
            }
        }
        colorCurveSelectionCombo.setSelectedIndex( chan );
        int[] histData = histograms.get( colorCurveNames[chan] ); 
        colorCurvePanel1.setHistogram( histData, Color.BLACK );
    }
    
    /**
     Enable or disable color curves for individual red, green, blue and saturation 
     channels.
     @param isEnabled If <code>true</code>, give user possibility to select 
     curves for all channels. If <code>false</code>, force display of value curve only
     
     */
    private void setColorChannelCurvesEnabled( boolean isEnabled ) {
        ComboBoxModel newModel = colorCurveSelectionCombo.getModel();
        int newSelection = colorCurveSelectionCombo.getSelectedIndex();
        if ( isEnabled ) {
            newModel = new DefaultComboBoxModel(new String[] { "Value", "Red", "Green", "Blue", "Saturation" });
        } else {
            newModel = new DefaultComboBoxModel(new String[] { "Value" });
            newSelection = 0;
        }
        colorCurveSelectionCombo.setModel( newModel );
        colorCurveSelectionCombo.setSelectedIndex( newSelection );
    }
    
    /**
     Array of color profiles that match to the items in colorProfileCombo
     combo box.
     */
    ColorProfileDesc profiles[] = null;
    
    boolean photoChanged = false;
    
    /**
       Shows the dialog.
       @return True if the dialog modified the photo data, false otherwise.
    */
	
    public boolean showDialog() {
	photoChanged = false;
	setVisible( true );
	return photoChanged;
    }
    
    /**
     * callback that is called is the preview image changes.
     * @param ev The change event
     */
    public void rawImageSettingsChanged(RawImageChangeEvent ev) {
        
    }
    
    /*
     PhotoInfoView implementation
     */

    public void setPhotographer(String newValue) {
    }

    public String getPhotographer() {
        return null;
    }

    public void setPhotographerMultivalued(boolean mv) {
    }

    public void setFuzzyDate(FuzzyDate newValue) {
    }

    public FuzzyDate getFuzzyDate() {
        return null;
    }

    public void setFuzzyDateMultivalued(boolean mv) {
    }

    public void setQuality(Number quality) {
    }

    public Number getQuality() {
        return null;
    }

    public void setQualityMultivalued(boolean mv) {
    }

    public void setShootingPlace(String newValue) {
    }

    public String getShootingPlace() {
        return null;
    }

    public void setShootingPlaceMultivalued(boolean mv) {
    }

    public void setFocalLength(Number newValue) {
    }

    public Number getFocalLength() {
        return null;
    }

    public void setFocalLengthMultivalued(boolean mv) {
    }

    public void setFStop(Number newValue) {
    }

    public Number getFStop() {
        return null;
    }

    public void setFStopMultivalued(boolean mv) {
    }

    public void setCamera(String newValue) {
    }

    public String getCamera() {
        return null;
    }

    public void setCameraMultivalued(boolean mv) {
    }

    public void setFilm(String newValue) {
    }

    public String getFilm() {
        return null;
    }

    public void setFilmMultivalued(boolean mv) {
    }

    public void setLens(String newValue) {
    }

    public String getLens() {
        return null;
    }

    public void setLensMultivalued(boolean mv) {
    }

    public void setDescription(String newValue) {
    }

    public String getDescription() {
        return null;
    }

    public void setDescriptionMultivalued(boolean mv) {
    }

    public void setTechNotes(String newValue) {
    }

    public String getTechNotes() {
        return null;
    }

    public void setTechNotesMultivalued(boolean mv) {
    }

    public void setShutterSpeed(Number newValue) {
    }

    public Number getShutterSpeed() {
        return null;
    }

    public void setShutterSpeedMultivalued(boolean mv) {
    }

    public void setFilmSpeed(Number newValue) {
    }

    public Number getFilmSpeed() {
        return null;
    }

    public void setFilmSpeedMultivalued(boolean mv) {
    }

    public void setFolderTreeModel(TreeModel model) {
    }
 
    public void setRawSettings(RawConversionSettings rawSettings) {
        this.rawSettings = rawSettings;
        if ( rawSettings != null ) {
            double evCorr = rawSettings.getEvCorr();
            evCorrSlider.setValue( evCorr );
            double comp = rawSettings.getHighlightCompression();
            hlightCompSlider.setValue( comp );
            double recovery = rawSettings.getHlightRecovery();
            hlightRecoverySlider.setValue( recovery );
            blackLevelSlider.setValue( rawSettings.getBlack() );
            whiteLevelSlider.setValue( rawSettings.getWhite() );
            double colorTemp = rawSettings.getColorTemp();
            ctempSlider.setValue( (int) colorTemp );
            double g = rawSettings.getGreenGain();
            double logGreen = Math.log( g ) / Math.log(2);
            greenGainSlider.setValue( logGreen );
            waveletDenoiseSlider.setValue( rawSettings.getWaveletThreshold() );
            firePreviewChangeEvent( new RawSettingsPreviewEvent(
                    this, ctrl.getPhotos(), rawSettings ) );
        } 
    }
    
    public void setRawSettingsMultivalued(boolean mv) {
    }

    public RawConversionSettings getRawSettings() {
        return rawSettings;
    }

    public void setColorChannelMapping( ChannelMapOperation cm ) {
    }
    
    public void setColorChannelMappingMultivalued( boolean mv ) {
        
    }
    
    public ChannelMapOperation getColorChannelMapping() {
        return null;
    }
    
    public void expandFolderTreePath(TreePath path) {
    }

    int black = 0;
    int white = 65535;
    
    public void setRawBlack(int black) {
        this.black = black;
        blackLevelSlider.setValue( (double) black );
    }

    public void setRawBlack( int black, List refValues ) {
        this.black = black;
        blackLevelSlider.setValue( (double) black );
        if ( refValues != null && refValues.size() > 1  ) {
            double[] annotations = new double[refValues.size()];
            for ( int n = 0; n < refValues.size() ; n++ ) {
                annotations[n] = ((Number)refValues.get(n)).doubleValue();
            }
            blackLevelSlider.setAnnotations( annotations );
            blackLevelSlider.setMultivalued( true );
        } else {
            // restore the normal label table without any extra annotations
            blackLevelSlider.setAnnotations( null );
        blackLevelSlider.setMultivalued( false );
        }
    }

    public void setRawBlackMultivalued(boolean multivalued, Object[] values ) {
        if ( values != null && values.length > 1  ) {
            double[] annotations = new double[values.length];
            for ( int n = 0; n < values.length ; n++ ) {
                annotations[n] = ((Number)values[n]).doubleValue();
            }
            blackLevelSlider.setAnnotations( annotations );
        } else {
            // restore the normal label table without any extra annotations
            blackLevelSlider.setAnnotations( null );
        }
        blackLevelSlider.setMultivalued( multivalued );
    }

    public int getRawBlack() {
        return (int) blackLevelSlider.getValue();
    }

    public void setRawWhite(int white) {
        this.white = white;
        whiteLevelSlider.setValue( (double) white );
    }

    public void setRawWhite( int white, List refValues ) {
        this.white = white;
        whiteLevelSlider.setValue( (double) white );
        if ( refValues != null && refValues.size() > 1  ) {
            double[] annotations = new double[refValues.size()];
            for ( int n = 0; n < refValues.size() ; n++ ) {
                annotations[n] = ((Number)refValues.get(n)).doubleValue();
            }
            whiteLevelSlider.setAnnotations( annotations );
            whiteLevelSlider.setMultivalued( true );
        } else {
            // restore the normal label table without any extra annotations
            whiteLevelSlider.setAnnotations( null );
        whiteLevelSlider.setMultivalued( false );
        }
    }

    public void setRawWhiteMultivalued(boolean multivalued, Object[] values ) {
        if ( values != null && values.length > 1  ) {
            double[] annotations = new double[values.length];
            for ( int n = 0; n < values.length ; n++ ) {
                annotations[n] = ((Number)values[n]).doubleValue();
            }
            whiteLevelSlider.setAnnotations( annotations );
        } else {
            // restore the normal label table without any extra annotations
            whiteLevelSlider.setAnnotations( null );
        }
        whiteLevelSlider.setMultivalued( multivalued );
    }

    public int getRawWhite() {
        return (int) whiteLevelSlider.getValue();
    }

    float denoise = 0;

    public void setRawWaveletDenoiseThreshold( float denoise, List refValues ) {
        this.denoise = denoise;
        waveletDenoiseSlider.setValue( (double) denoise );
        if ( refValues != null && refValues.size() > 1  ) {
            double[] annotations = new double[refValues.size()];
            for ( int n = 0; n < refValues.size() ; n++ ) {
                annotations[n] = ((Number)refValues.get(n)).doubleValue();
            }
            waveletDenoiseSlider.setAnnotations( annotations );
            waveletDenoiseSlider.setMultivalued( true );
        } else {
            // restore the normal label table without any extra annotations
            waveletDenoiseSlider.setAnnotations( null );
            waveletDenoiseSlider.setMultivalued( false );
        }

    }

    public float getRawWaveletDenoiseThreshold() {
        return (int) waveletDenoiseSlider.getValue();
    }

    double evCorr = 0.0;
    public void setRawEvCorr(double evCorr) {
        this.evCorr = evCorr;
        evCorrSlider.setValue( evCorr );        
    }

    public void setRawEvCorr(double evCorr, List refValues ) {
        this.evCorr = evCorr;
        evCorrSlider.setValue( evCorr );        
        if ( refValues != null && refValues.size() > 1 ) {
            annotateSlider( evCorrSlider, refValues );
            evCorrSlider.setMultivalued( true );
        } else {
            evCorrSlider.setAnnotations( null );
            evCorrSlider.setMultivalued( false );
        }
    }
    
    public void setRawEvCorrMultivalued(boolean multivalued, Object[] values ) {
        if ( values != null && values.length > 1  ) {
            annotateSlider( evCorrSlider, values );
        } else {
            // restore the normal label table without any extra annotations
            evCorrSlider.setAnnotations( null );
        }
        evCorrSlider.setMultivalued( multivalued );
    }
    
    public double getRawEvCorr() {
        return evCorrSlider.getValue();
    }

    double hlightComp = 0.0;
    Hashtable hlightSliderLabels = null;
    
    public void setRawHlightComp(double comp) {
        this.hlightComp = comp;
        hlightCompSlider.setValue( comp );
    }

    public void setRawHlightComp(double comp, List refValues ) {
        this.hlightComp = comp;
        hlightCompSlider.setValue( comp );
        if ( refValues != null && refValues.size() > 1 ) {
            annotateSlider( hlightCompSlider, refValues );
            hlightCompSlider.setMultivalued( true );
        } else {
            hlightCompSlider.setAnnotations( null );
            hlightCompSlider.setMultivalued( false );
        }
    }

    int hlightRecovery;

    public int getHlightRecovery() {
        return (int) hlightRecoverySlider.getValue();
    }
    public void setRawHlightRecovery( int recovery, List refValues ) {
        this.hlightRecovery = recovery;
        hlightRecoverySlider.setValue(recovery);
        if ( refValues != null && refValues.size() != 1 ) {
            annotateSlider( hlightRecoverySlider, refValues );
            hlightRecoverySlider.setMultivalued( true );
        } else {
            hlightRecoverySlider.setAnnotations( null );
            hlightRecoverySlider.setMultivalued( false );
        }
    }

    public void setRawHlightCompMultivalued(boolean multivalued, Object[] values ) {
        if ( values != null && values.length > 1  ) {
            annotateSlider( hlightCompSlider, values );
        } else {
            // restore the normal label table without any extra annotations
            hlightCompSlider.setAnnotations( null );
        }
        hlightCompSlider.setMultivalued( multivalued );
    }

    public double getRawHlightComp() {
        double comp = hlightCompSlider.getValue();
        return comp;
    }

    double colorTemp = 0.0;
    
    public void setRawColorTemp(double ct) {
        colorTemp = ct;
        ctempSlider.setValue( ct );
    }

    public void setRawColorTemp(double ct, List refValues ) {
        colorTemp = ct;
        ctempSlider.setValue( ct );
        if ( refValues != null && refValues.size() > 1 ) {
            annotateSlider( ctempSlider, refValues );
            ctempSlider.setMultivalued( true );
        } else {
            ctempSlider.setAnnotations( null );
            ctempSlider.setMultivalued( false );
        }
    }
    
    public void setRawColorTempMultivalued(boolean multivalued, Object[] values ) {
        if ( values != null && values.length > 1  ) {
            annotateSlider( ctempSlider, values );
        } else {
            // restore the normal label table without any extra annotations
            ctempSlider.setAnnotations( null );
        }
        ctempSlider.setMultivalued( multivalued );
    }

    public double getRawColorTemp() {
        return (double) ctempSlider.getValue();
    }

    double greenGain = 1.0;
    
    public void setRawGreenGain(double g) {
        greenGain = g;
        double logGreen = Math.log( g ) / Math.log(2);
        greenGainSlider.setValue( logGreen );
    }
    
    public void setRawGreenGain( double g, List refValues ) {
        greenGain = g;
        double logGreen = Math.log( g ) / Math.log(2);
        greenGainSlider.setValue( logGreen );
        if ( refValues != null && refValues.size() > 1 ) {
            double[] annotations = new double[refValues.size()];
            for ( int n = 0; n < refValues.size(); n++ ) {
                annotations[n] = Math.log( ((Number)refValues.get(n)).doubleValue() ) / Math.log(2); 
            }
            greenGainSlider.setAnnotations( annotations );
            greenGainSlider.setMultivalued( true );
        } else {
            greenGainSlider.setAnnotations( null );
            greenGainSlider.setMultivalued( false );            
        }
    }

    public void setRawGreenGainMultivalued(boolean multivalued, Object[] values ) {
        if ( values != null && values.length > 1 ) {
            double[] annotations = new double[values.length];
            for ( int n = 0; n < values.length ; n++ ) {
                annotations[n] = Math.log( ((Number)values[n]).doubleValue() ) / Math.log(2); 
            }
            greenGainSlider.setAnnotations( annotations );
        } else {
            // restore the normal label table without any extra annotations
            greenGainSlider.setAnnotations( null );
        }
        greenGainSlider.setMultivalued( multivalued );
    }
    
    public double getRawGreenGain() {
        double greenEv = greenGainSlider.getValue();
        double green = Math.pow( 2, greenEv );
        return green;
    }

    ColorProfileDesc profile = null;
    public void setRawProfile(ColorProfileDesc p) {
        profile = p;
        // setupColorProfile();
    }

    public void setRawProfile(ColorProfileDesc p, List refValues ) {
        profile = p;
        // setupColorProfile();
    }
    
    public void setRawProfileMultivalued(boolean multivalued, Object[] values ) {
    }

    public ColorProfileDesc getRawProfile() {
        return profile;
    }
    

    public void setColorChannelCurve(String name, ColorCurve curve) {
        for ( int n = 0 ; n < colorCurveNames.length ; n++ ) {
            if ( colorCurveNames[n].equals( name ) ) {
                colorCurves[n] = curve;
                refCurves[n] = null;
                showCurve( currentColorCurve );
                break;
            }
        }        
    }    
    
    /**
     Set new value for a color channel curve
     @param name Name of the curve to set
     @param curve The new curve
     @param refCurves Reference curves to show in the background     
     */
    public void setColorChannelCurve(String name, ColorCurve curve, List refCurves ) {
        for ( int n = 0 ; n < colorCurveNames.length ; n++ ) {
            if ( colorCurveNames[n].equals( name ) ) {
                colorCurves[n] = curve;
                this.refCurves[n] = refCurves;
                showCurve( currentColorCurve );
                break;
            }
        }
    }    
    
    /**
     @deprecated
     */
    public void setColorChannelMultivalued(String name, boolean isMultivalued, ColorCurve[] values ) {
        for ( int n = 0 ; n < colorCurveNames.length ; n++ ) {
            if ( colorCurveNames[n].equals( name ) ) {
                if ( isMultivalued ) {
                    ArrayList<ColorCurve> curves = new ArrayList<ColorCurve>( values.length );
                    for ( ColorCurve c : values ) {
                        curves.add( c );
                    }
                    refCurves[n] = curves;
                    if ( currentColorCurve == n ) {
                        showCurve( n );
                    }
                    
                }
                break;
            }
        }
        
    }
    
    /**
     Get current value of a color channel curve
     @param name Name of the curve
     @return Current curve for the channel or <code>null</code> if not specified.
     */
    public ColorCurve getColorChannelCurve(String name) {
        ColorCurve ret = null;
        for ( int n = 0 ; n < colorCurveNames.length ; n++ ) {
            if ( colorCurveNames[n].equals( name ) ) {
                ret = colorCurves[n];
                break;
            }
        }        
        return ret;
    }    
    
    /**
     Get reference curves displayed for channel
     @param name Name of the channel
     @return Reference curves displayed for a channel or <code>null</code> if 
     no reference curves are displayed-
     */
    public List getRefCurves( String channel ) {
        List ret = null;
        for ( int n = 0 ; n < colorCurveNames.length ; n++ ) {
            if ( colorCurveNames[n].equals( channel ) ) {
                ret = refCurves[n];
                break;
            }
        }        
        return ret;
    }

    /**
     @deprecated
     */
    private void annotateSlider( FieldSliderCombo slider, Object [] values ) {
        double[] annotations = new double[values.length];
        for ( int n = 0; n < values.length ; n++ ) {
            annotations[n] = ((Number)values[n]).doubleValue();
        }
        slider.setAnnotations( annotations );
    }
    

    /**
     Annotate a slider with given values
     @param slider The slider to annotate
     @param values Annotation values. Note! Cannot be null!!
     */
    private void annotateSlider( FieldSliderCombo slider, List values ) {
        double[] annotations = new double[values.size()];
        for ( int n = 0; n < values.size() ; n++ ) {
            annotations[n] = ((Number)values.get(n)).doubleValue();
        }
        slider.setAnnotations( annotations );
    }    

    /**
     This callback is called by JAIPhotoViewer when the image displayed in the 
     control is changed.
     */
    public void photoViewChanged(PhotoViewChangeEvent e) {
        reloadHistogram();
    }
    
    PhotovaultImage previewImage = null;
    
    /**
     Called when preview image in some view associated with this controller changes.
     */
    public void modelPreviewImageChanged(PhotovaultImage preview) {
        if ( previewImage != null ) {
            previewImage.removeRenderingListener( this );
        }
        previewImage = preview;
        setupColorCurvesForImage();
    }

    public PhotovaultImage getPreviewImage() {
        return previewImage;
    }

    /**
     A new rendering of current image is created. Update color curves based on it.     
     */
    public void newRenderingCreated(PhotovaultImage img) {
        img.removeRenderingListener( this );        
        if ( img == previewImage ) {
            setupColorCurvesForImage();
        }
    }
    
    private void setupColorCurvesForImage() {
        if ( previewImage != null ) {
            ColorModel cm = previewImage.getCorrectedImageColorModel();
            if ( cm != null ) {
            setColorChannelCurvesEnabled( cm.getNumColorComponents() >= 3 );
            } else {
                /*
                 The image has not yet been rendered. Register a listener so that 
                 we can complete setup after rendering the image
                 */
                previewImage.addRenderingListener( this );
            }
        }
        // Update color curves with histogram data from this image.
        showCurve( currentColorCurve );
    }

    public void setField(PhotoInfoFields field, Object newValue) {
        // TODO: implement
    }

    public void setFieldMultivalued(PhotoInfoFields field, boolean isMultivalued) {
        // TODO: implement
    }

    public Object getField(PhotoInfoFields field) {
        // TODO: Implement
        return null;
    }

    public void setField(PhotoInfoFields field, Object newValue, List refValues) {
        switch ( field ) {
            case COLOR_CURVE_VALUE:
                this.setColorChannelCurve( "value", (ColorCurve) newValue, refValues );
                break;
            case COLOR_CURVE_SATURATION:
                this.setColorChannelCurve( "saturation", (ColorCurve) newValue, refValues );
                break;
            case COLOR_CURVE_RED:
                this.setColorChannelCurve( "red", (ColorCurve) newValue, refValues );
                break;
            case COLOR_CURVE_GREEN:
                this.setColorChannelCurve( "green", (ColorCurve) newValue, refValues );
                break;
            case COLOR_CURVE_BLUE:
                this.setColorChannelCurve( "blue", (ColorCurve) newValue, refValues );
                break;
            case RAW_BLACK_LEVEL:
                setRawBlack( newValue != null ? ( (Number)newValue).intValue() : 0, refValues );
                break;
            case RAW_WHITE_LEVEL:
                setRawWhite( newValue != null ? ( (Number)newValue).intValue() : 0, refValues );
                break;
            case RAW_CTEMP:
                setRawColorTemp( newValue != null ? ( (Number)newValue).doubleValue() : 0, refValues );
                break;
            case RAW_EV_CORR:
                setRawEvCorr( newValue != null ? ( (Number)newValue).doubleValue() : 0, refValues );
                break;
            case RAW_GREEN:
                setRawGreenGain( newValue != null ? ( (Number)newValue).doubleValue() : 0, refValues );
                break;
            case RAW_HLIGHT_COMP:
                setRawHlightComp( newValue != null ? ( (Number)newValue).doubleValue() : 0, refValues );
                break;
            case RAW_HLIGHT_RECOVERY:
                setRawHlightRecovery( newValue != null ? ( (Number)newValue).intValue() : 0, refValues );
                break;
            case RAW_WAVELET_DENOISE_THRESHOLD:
                setRawWaveletDenoiseThreshold( 
                        newValue != null ? ((Number)newValue).floatValue() : 0,
                        refValues );
                break;
            case RAW_COLOR_PROFILE:
                setRawProfile( (ColorProfileDesc) newValue, refValues );
                break;
            default:
                // No action for other fields
                break;
        }
    }

    /**
     * Histograms for each color channel
     */
    Map<String, int[]> histograms = new HashMap<String, int[]>();
    
    /**
     * Set histogram displayed for a certain channel
     * @param channel Name of the color channel
     * @param histData Array of values for each histogram bin
     */
    public void setHistogram( String channel, int[] histData ) {
        if ( histData == null ) { 
            histograms.remove( channel );
        } else {
            histograms.put( channel, histData );
        }
        
        if ( colorCurveNames[currentColorCurve].equals( channel ) ) {
            showCurve( currentColorCurve );
        }
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyBtn;
    private org.photovault.swingui.color.FieldSliderCombo blackLevelSlider;
    private javax.swing.JButton closeBtn;
    private org.photovault.swingui.color.ColorCurvePanel colorCurvePanel1;
    private javax.swing.JComboBox colorCurveSelectionCombo;
    private javax.swing.JPanel colorSettingControls;
    private javax.swing.JTabbedPane colorSettingTabs;
    private org.photovault.swingui.color.FieldSliderCombo ctempSlider;
    private javax.swing.JButton discardBtn;
    private javax.swing.JPanel dlgControlPane;
    private org.photovault.swingui.color.FieldSliderCombo evCorrSlider;
    private org.photovault.swingui.color.FieldSliderCombo fieldSliderCombo1;
    private org.photovault.swingui.color.FieldSliderCombo fieldSliderCombo2;
    private org.photovault.swingui.color.FieldSliderCombo greenGainSlider;
    private org.photovault.swingui.color.FieldSliderCombo hlightCompSlider;
    private org.photovault.swingui.color.FieldSliderCombo hlightRecoverySlider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JButton okBtn;
    private javax.swing.JPanel rawControlsPane;
    private javax.swing.JPanel rawHistogramPane;
    private org.photovault.swingui.color.FieldSliderCombo waveletDenoiseSlider;
    private org.photovault.swingui.color.FieldSliderCombo whiteLevelSlider;
    // End of variables declaration//GEN-END:variables

}
