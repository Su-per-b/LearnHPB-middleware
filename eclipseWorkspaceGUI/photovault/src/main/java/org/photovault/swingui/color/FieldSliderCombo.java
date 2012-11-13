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


package org.photovault.swingui.color;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * A combined UI control that allows changing of a double parameter using either
 * {@link JSlider} or {@link javax.swing.JFormattedTextField}. API of the control represents
 * JSlider but it has the following additional features:
 * <ul>
 *    <li>Possibility to add annotations (small blue diamond figures) at given
 *        positions under the slider. These can be used to express the previous values.
 *    </li>
 *    <li>
 *        The value is expressed as double instead of integer.
 *    </li>
 *    <li>
 *        It is possible to set the accuracy of the slider isung setFractionDigits.
 *    </li>
 * </ul>
 * @author Harri Kaimio
 * @since 0.4.0
 */
public class FieldSliderCombo extends javax.swing.JPanel {
    
    /**
     * Creates new FieldSliderCombo.
     */
    public FieldSliderCombo() {
        initComponents();
        setupSliderBoundaries();
        setupNumberFormat();
        setupLabels();
    }
    
    /**
     * Number of decimal digits used when showing current value and converting from 
     * between position and field value.
     */
    int fractionDigits = 1;
    /**
     * Mutliplier used to convert between field value (double) and slider position 
     * (integer). Must be 10**fractionDigits.
     */
    int sliderMult = 10;
    
    /**
     * Set the number of decimals used whend representing/rounding the value.
     */
    public void setFractionDigits( int newVal ) {
        double value = getValue();
        fractionDigits = newVal;
        int newMult = 1;
        for ( int n = 0; n < fractionDigits ; n++, newMult *= 10 );
        sliderMult = newMult;
        setupSliderBoundaries();
        setupNumberFormat();
        setupLabels();
        valueSlider.setValue( getSliderPos( value ) );
    }
    
    public int getSliderAccuracy() {
        return fractionDigits;
    }
    
    /**
     * Minimum value in slider
     */
    double minVal = 0.0;
    
    /**
     * Set the minimum value in slider. Note that value can be smaller, it just cannot 
     * be shown correctly in slider then.
     * @param val New minimum value
     */
    public void setMinimum( double val ) {
        minVal = val;
        setupSliderBoundaries();
    }
    
    /**
     * Get the minimum value that can be shown on slider.
     * @return Minimum value.
     */
    public double getMinimum() {
        return minVal;
    }
    
    /**
     * Maximum value that can be shown on slider.
     */
    double maxVal = 1.0;
    
    /**
     * Set the minimum value in slider. Note that value can be smaller, it just cannot 
     * be shown correctly in slider then.
     * @param val New maximum value
     */
    public void setMaximum( double val ) {
        maxVal = val;
        setupSliderBoundaries();
    }
    
    /**
     * Get the maximum value of the slider
     * @return Maximum value
     */
    public double getMaximum() {
        return maxVal;
    }
    
    /**
     *     Sets the value of the control.
     * @param val New value for the control
     */
    public void setValue( double val ) {
        valueField.setValue( new Double( val ) );
    }
    
    /**
     * Get the current value
     * @return Current value of the control
     */
    public double getValue() {
        double ret = minVal;
        Number v = (Number) valueField.getValue();
        if ( v != null ) {
            ret = v.doubleValue();
        }
        return ret;
    }
    
    /**
     * Labels displayed under slider. The keys are Double objects representing the 
     * place there the label is displayed. Values are Swing Components that are 
     * displayed as labels.
     */
    Map labels = null;
    /**
     * Array of values where annotations are shown.
     */
    double[] annotations = null;
    
    /**
     * Set the label table
     * @param labels New label table. See {@link #labels} for details of the content of the Hashtable.
     */
    public void setLabelTable( Map labels ) {
        this.labels = labels;
        setupLabels();
    }
    
    /**
     * Get the current lables.
     * @return See {@link #labels} for details of the content of the Hashtable.
     */
    public Map getLableTable( ) {
        return labels;
    }
    
    /**
     * Set the annotations displayed.
     * @param annotations Array of the values that are annotated or <code>null</code>.
     */
    public void setAnnotations( double[] annotations ) {
        this.annotations = annotations;
        setupLabels();
    }

    /**
     * Spacing between major ticks
     */
    double majorTickSpacing = 0.0;
    
    /**
     * Set the spacing between major ticks in slider
     * @param spacing Value for spacing
     */
    public void setMajorTickSpacing( double spacing ) {
        majorTickSpacing = spacing;
        valueSlider.setMajorTickSpacing( (int)(spacing*sliderMult) );
    }
    
    /**
     * Get the spacing between major ticks
     * @return Major tick spacing
     */
    public double getMajorTickSpacing() {
        return majorTickSpacing;
    }

    /**
     * Spacing between minor ticks
     */
    double minorTickSpacing = 0.0;
    
    /**
     * Set the spacing between minor ticks
     * @param spacing Spacing of minor ticks (in the same units as value is specified)
     */
    public void setMinorTickSpacing( double spacing ) {
        minorTickSpacing = spacing;
        valueSlider.setMinorTickSpacing( (int)(spacing*sliderMult) );
    }
    
    /**
     * Get the spacing between minor ticks
     * @return Minor tick spacing
     */
    public double getMinorTickSpacing() {
        return minorTickSpacing;
    }
    
    /**
     * Select whether labels are painted or not. Note that the samoe parameter controls
     * also whether annotations are painted.
     * @param b <code>true</code> to paint labels, <code>false</code> to not paint them
     */
    public void setPaintLabels( boolean b ) {
        valueSlider.setPaintLabels( b );
    }
    
    /**
     * Are labels painted or not?
     * @return <code>true</code> if labels are painted, <code>false</code> otherwise
     */
    public boolean getPaintLabels() {
        return valueSlider.getPaintLabels();
    }
        
    /**
     * Select whether to paint ticks
     * @param b <code>true</code> to paint ticks, <code>false</code> to not paint them
     */
    public void setPaintTicks( boolean b ) {
        valueSlider.setPaintTicks( b );
    }
    
    /**
     * Check whether ticks are painted
     * @return <code>true</code> if ticks are painted, <code>false</code> otherwise
     */
    public boolean getPaintTicks() {
        return valueSlider.getPaintTicks();
    }
    
    /**
     * Select whether slider track is painted
     * @param b <code>true</code> to paint track, <code>false</code> to not paint it
     */
    public void setPaintTrack( boolean b ) {
        valueSlider.setPaintTrack( b );
    }
    
    /**
     * Check whether slider track is painted
     * @return <code>true</code> if track is painted, <code>false</code> otherwise
     */
    public boolean getPaintTrack() {
        return valueSlider.getPaintTrack();
    }
    
    /**
     * Is this field show as multivalued or not.
     */
    boolean multivalued = false;
    
    /**
     * Set the field to single/multivalued state. Ithe field is in multivalued state, 
     * the text edit field is grayed out and does not show any value.
     * @param b <code>true</code> for multivalued state, <code>false</code> for singlevalued 
     * state.
     */
    public void setMultivalued( boolean b ) {
        multivalued = b;
        if ( b ) {
            valueField.setBackground( Color.LIGHT_GRAY );
            valueField.setText( "" );
        } else {
            valueField.setBackground( Color.WHITE );
        }
    }
    
    /**
     * Is the field in multivalued state?
     * @return <code>true</code> for multivalued state, <code>false</code> for singlevalued 
     * state.
     */
    public boolean isMultivalued() {
        return multivalued;
    }
    
    /**
     * Enable/disable field
     * @param b <code>true</code> to enable, <code>false</code> to disable
     */
    public void setEnabled( boolean b ) {
        super.setEnabled( b );
        valueField.setEnabled( b );
        valueSlider.setEnabled( b );
    }
    
    /**
     * Helper method to create lables for each major tick mark.
     * @param t Hashtable into which the labels are created.
     */
    public void createStandardLabels( Hashtable t ) {
        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMinimumFractionDigits( fractionDigits );
        double labelSpacing = (majorTickSpacing > 0 ) ? 
            majorTickSpacing : 
            Math.abs(maxVal - minVal);
        for ( double d = minVal; d <= maxVal ; d += labelSpacing ) {
            int p = getSliderPos( d );
            String labelStr = fmt.format( d );
            t.put( Integer.valueOf( p ), new JLabel( labelStr ) );
        }
    }
    
    /**
     * Helper function to set up slider labels based on {@link labels} and {@link 
     * annotations} tables.
     */
    private void setupLabels() {
        Hashtable sliderLabels = new Hashtable();
        if ( labels != null ) {
            Iterator iter = labels.entrySet().iterator();
            while ( iter.hasNext() ) {
                Map.Entry e = (Entry) iter.next();
                double vp = ((Number)e.getKey()).doubleValue();
                sliderLabels.put( Integer.valueOf( getSliderPos(vp) ), e.getValue() );
            }
        } else {
            createStandardLabels( sliderLabels );
        }
        if ( annotations != null ) {
            for ( int n = 0; n < annotations.length; n++ ) {
                int v = getSliderPos( annotations[n] );
                if ( v >= valueSlider.getMinimum() && v <= valueSlider.getMaximum() ) {
                Integer k = Integer.valueOf( v );
                if ( sliderLabels.containsKey( k ) ) {
                    if ( v == valueSlider.getMaximum() ) {
                        v--;
                    } else {
                        v++;
                    }
                    k = Integer.valueOf( v );
                }
                sliderLabels.put( k, new ColorSettingsDlg.ModelValueAnnotation( Color.BLUE ) );
                }
            }
        }
        /*
         JSlider crashes if label table is empty. In Java 6 it crashes also if 
         none of the labels happens to be in the range of the slider. So let's 
         create empty labels at the ends if there are no such ones.
         */
        Integer minValue = Integer.valueOf( valueSlider.getMinimum() );
        if ( !sliderLabels.containsKey( minValue ) ) {
            sliderLabels.put( minValue, new JLabel( "" ) );
        }
        Integer maxValue = Integer.valueOf( valueSlider.getMaximum() );
        if ( !sliderLabels.containsKey( maxValue ) ) {
            sliderLabels.put( maxValue, new JLabel( "" ) );
        }        
        valueSlider.setLabelTable( sliderLabels );
    }
    
    
    /**
     * Set the slider minimum & maximum to match minVal and maxVal.
     */
    private void setupSliderBoundaries() {
        int sliderMin = getSliderPos( minVal );
        int sliderMax = getSliderPos( maxVal );
        valueSlider.setMinimum( sliderMin );
        valueSlider.setMaximum( sliderMax );
        valueSlider.setMinorTickSpacing( (int)(sliderMult*minorTickSpacing) );
        valueSlider.setMajorTickSpacing( (int)(sliderMult*majorTickSpacing) );
    }
    
    /**
     * Setu up the muber formatter for valueField.
     */
    private void setupNumberFormat() {
        // Determine the number of decimals to show
        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMinimumFractionDigits( fractionDigits );
        valueField.setFormatterFactory( new DefaultFormatterFactory( 
                new NumberFormatter( fmt ) ) );
    }
    
    /**
     * Get the slider position that matches a certain value.
     * @param value Value for which a slider position is calculated
     * @return The correct slider position.
     */
    private int getSliderPos( double value ) {
        return (int)(value * sliderMult);
    }
    
    /**
     * Get the value that corresponds to a given slider position.
     * @param pos Slider positon.
     * @return Corresponding value.
     */
    private double getValueAtSliderPos( int pos ) {
        return (double)pos/(double)sliderMult;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        valueField = new javax.swing.JFormattedTextField();
        /*
        Set an action map that creates action event when user
        presses enter.
        */

        final FieldSliderCombo staticThis = this;
        Action enterPressedAction = new AbstractAction() {
            public void actionPerformed( ActionEvent evt ) {
                staticThis.valueFieldEnterPressed();
            }
        };

        valueField.getInputMap().put(KeyStroke.getKeyStroke(
            KeyEvent.VK_ENTER, 0),
        "check");
    valueField.getActionMap().put("check", enterPressedAction );
    valueSlider = new javax.swing.JSlider();

    valueField.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            valueFieldActionPerformed(evt);
        }
    });
    valueField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            valueFieldPropertyChange(evt);
        }
    });

    valueSlider.setPaintLabels(true);
    valueSlider.setPaintTicks(true);
    valueSlider.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            valueSliderStateChanged(evt);
        }
    });

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .add(valueSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(valueField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .add(12, 12, 12)
            .add(valueField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(20, 20, 20))
        .add(layout.createSequentialGroup()
            .add(valueSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
            .addContainerGap())
    );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evend handler that is called when valueSlider is moved.
     * @param evt Event descriptor
     */
    private void valueSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_valueSliderStateChanged
        JSlider source = (JSlider)evt.getSource();
        int pos = (int)source.getValue();
        double value = getValueAtSliderPos( pos );
        // Handle situation in which value is outside the bounds of the slider
        double currentVal = getValue();
        if ( ( value == minVal && currentVal < minVal ) || 
                ( value == maxVal && currentVal > maxVal ) ) {
            return;
        }
        if (!source.getValueIsAdjusting()) { //done adjusting
            valueField.setValue(new Double( value )); //update ftf value
            fireChangeEvent();
        } else {
            //value is adjusting; just set the text
            valueField.setText(String.valueOf( value ) );
        }
    }//GEN-LAST:event_valueSliderStateChanged
    
    /**
     * Event handler that is called e.g. value of the valueField changes.
     * @param evt Event descriptor
     */
    private void valueFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_valueFieldPropertyChange
        if ("value".equals(evt.getPropertyName())) {
            Number value = (Number)evt.getNewValue();
            if (valueSlider != null && value != null) {
                double newVal = value.doubleValue();
                newVal = Math.min( maxVal, Math.max( minVal, newVal ) );
                valueSlider.setValue(getSliderPos( newVal ) );
            }
        }
    }//GEN-LAST:event_valueFieldPropertyChange

    /**
     * Event handler that is called e.g. when user presses Enter in valueField.
     * @param evt Event descriptor
     */
    private void valueFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valueFieldActionPerformed

    }//GEN-LAST:event_valueFieldActionPerformed

    /**
     * Event handler that is called when user presses Enter in valueField.
     */
    private void valueFieldEnterPressed() {
        if (!valueField.isEditValid()) {
            //The text is invalid.
            Toolkit.getDefaultToolkit().beep();
            valueField.selectAll();
        } else try {                    //The text is valid,
            valueField.commitEdit();     //so use it.
        } catch (java.text.ParseException exc) {
            
        }// TODO add your handling code here:
    }
    
    /**
     * List of objects that need to be notified about value change
     */
    Vector changeListeners = new Vector();
    
    /**
     * Add a new lister that will be notified about value changes.
     * @param l The new listener
     */
    public void addChangeListener( ChangeListener l ) {
        changeListeners.add( l );
    }

    /**
     * Remove an existing listener
     * @param l The listener that will be removed from change listeners
     */
    public void removeChangeListener( ChangeListener l ) {
        changeListeners.remove( l );
    }

    /**
     * Informa all listeners about a value change.
     */
    private void fireChangeEvent() {
        ChangeEvent e = new ChangeEvent( this );
        Iterator iter = changeListeners.iterator();
        while ( iter.hasNext() ) {
            ChangeListener l = (ChangeListener) iter.next();
            l.stateChanged( e );
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField valueField;
    private javax.swing.JSlider valueSlider;
    // End of variables declaration//GEN-END:variables
    
}
