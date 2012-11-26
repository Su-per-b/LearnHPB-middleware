package com.sri.straylight.client.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sri.straylight.client.event.ScalarValueChangeRequest;
import com.sri.straylight.client.model.DoubleInputVerifier;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;


/**
 * The Class ScalarVariableRealPanel.
 */
public class ScalarVariableRealPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/** The lbl name_. */
	private JLabel lblName_;
	
	/** The lbl min_. */
	private JLabel lblMin_;
	
	/** The lbl max_. */
	private JLabel lblMax_;

	/** The lbl start_. */
	private JLabel lblStart_;

	/** The lbl nominal_. */
	private JLabel lblNominal_;
	
	/** The lbl value_. */
	private JLabel lblValue_;
	
	/** The slider_. */
	private JSlider slider_;
	
	/** The text field_. */
	private JTextField textField_;

	/** The scalar value real struct_. */
	private ScalarValueReal scalarValueReal_;
	
	/** The btn submit_. */
	private JButton btnSubmit_;
	
	/** The double input verifier_. */
	private DoubleInputVerifier doubleInputVerifier_;

	/** The is text field initialized_. */
	private boolean isTextFieldInitialized_ = false;
	
	/** The input detail view_. */
	private InputView inputFormView_;
	
	/** The scalar variable real struct_. */
	private ScalarVariableReal scalarVariableReal_;
	
	/** The lbl description_. */
	private JLabel lblDescription_;
	
	private JLabel lblValueReference_;
	
	
	
	/**
	 * Create the panel.
	 *
	 * @param inputFormView the input detail view
	 */
	public ScalarVariableRealPanel(InputView inputFormView) {
		
		inputFormView_ = inputFormView;
				
		Font normalFont = new Font("sansserif", Font.PLAIN, 12);
		Font boldFont = new Font("sansserif", Font.BOLD, 12);

		lblName_ = new JLabel("{Name}");
		lblName_.setFont(boldFont);

		lblMin_ = new JLabel("{Min}");
		lblMin_.setFont(normalFont);

		lblMax_ = new JLabel("{Max}");
		lblMax_.setFont(normalFont);

		slider_ = new JSlider();

		lblStart_ = new JLabel("Start");
		lblStart_.setFont(normalFont);

		lblNominal_ = new JLabel("Nominal");
		lblNominal_.setFont(normalFont);

		lblValue_ = new JLabel("{Value}");
		lblValue_.setFont(new Font("SansSerif", Font.BOLD, 12));

		JLabel lblEqual = new JLabel("=");

		btnSubmit_ = new JButton("Submit");
		btnSubmit_.setEnabled(false);

		textField_ = new JTextField();
		textField_.setColumns(10);
		
		lblDescription_ = new JLabel("{Description Not Set}");
		
		lblValueReference_ = new JLabel("{Value Reference}");
		lblValueReference_.setFont(new Font("SansSerif", Font.PLAIN, 12));


		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
							//.addComponent(separator, GroupLayout.PREFERRED_SIZE, 417, GroupLayout.PREFERRED_SIZE)
							.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(textField_, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblStart_)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblNominal_))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblName_)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblEqual)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblValue_)))
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnSubmit_)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblMin_)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(slider_, GroupLayout.PREFERRED_SIZE, 349, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblMax_))
						.addComponent(lblValueReference_, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDescription_, GroupLayout.PREFERRED_SIZE, 408, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(385, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					//.addComponent(separator, GroupLayout.PREFERRED_SIZE, 6, GroupLayout.PREFERRED_SIZE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(11)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblName_)
								.addComponent(lblEqual)
								.addComponent(lblValue_, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
							.addGap(11)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblStart_)
								.addComponent(lblNominal_)
								.addComponent(textField_, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(26)
							.addComponent(btnSubmit_)))
					.addGap(22)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblMin_)
								.addComponent(slider_, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addComponent(lblValueReference_, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblMax_))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblDescription_)
					.addContainerGap(19, Short.MAX_VALUE))
		);
		setLayout(groupLayout);


		//setMaximumSize( new Dimension(400, 170) );

	}

	/**
	 * Sets the meta data.
	 *
	 * @param sv the new meta data
	 */
	public void setMetaData(ScalarVariableReal sv) {
		
		scalarVariableReal_ = sv;
		
		lblName_.setText(sv.getName());
		lblName_.setToolTipText("Name: " + sv.getName());

		TypeSpecReal typeSpecReal = sv.getTypeSpecReal();
		String minStr = String.valueOf(typeSpecReal.min);
		
		lblMin_.setText(minStr);
		lblMin_.setToolTipText("Minimum Value: " + minStr);

		String maxStr = String.valueOf(sv.getTypeSpecReal().max);
		lblMax_.setText(maxStr);
		lblMax_.setToolTipText("Maximum Value: " + maxStr);

		String startStr = String.valueOf(sv.getTypeSpecReal().start);
		lblStart_.setText("Start: " + startStr);
		lblStart_.setToolTipText("Start Value: " + startStr);

		String nominalStr = String.valueOf(sv.getTypeSpecReal().nominal);
		lblNominal_.setText("  Nominal: " + nominalStr);
		lblNominal_.setToolTipText("Nominal Value: " + nominalStr);

		String valueReferenceStr = String.valueOf(sv.getValueReference());
		
		lblValueReference_.setText("Value Reference: " + valueReferenceStr);
		lblValueReference_.setToolTipText("Value Reference: " + valueReferenceStr);
		
		slider_.setMinimum((int)sv.getTypeSpecReal().min);
		slider_.setMaximum((int)sv.getTypeSpecReal().max);

		doubleInputVerifier_ = DoubleInputVerifier.getInstance("##0.00##");
		doubleInputVerifier_.setRange(sv.getTypeSpecReal().min, sv.getTypeSpecReal().max);

		textField_.setInputVerifier(doubleInputVerifier_);
		textField_.addKeyListener(new DoubleKeyAdapter(textField_));

		textField_.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				//btnSubmit_.setEnabled(true);
				submitTextFieldChange();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});


		textField_.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				doubleInputVerifier_.verify(textField_);
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});



		slider_.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				submitSliderChange();
			}
		});


		btnSubmit_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitButtonPressed();
			}
		});

		
		lblDescription_.setText(sv.getDescription());
		
		
	}

	/**
	 * Submit button pressed.
	 */
	public void submitButtonPressed() {
		
		String newStringValue = textField_.getText();
		double newDoubleValue = Double.parseDouble(newStringValue);
		
		Vector<ScalarValueRealStruct> scalarValueList = new Vector<ScalarValueRealStruct>();
		
		ScalarValueRealStruct scalarValue = new ScalarValueRealStruct();
		scalarValue.idx = scalarVariableReal_.getIdx();
		scalarValue.value = newDoubleValue;
		
		scalarValueList.add(scalarValue);
		
		ScalarValueChangeRequest event = new ScalarValueChangeRequest(this, scalarValueList);
		inputFormView_.onDataModelUpdateRequest(event);
		
		btnSubmit_.setEnabled(false);
		
	}

	/**
	 * Submit slider change.
	 */
	public void submitSliderChange() {

		int newIntValue = slider_.getValue();
		double newDoubleValue = (double) newIntValue;

		btnSubmit_.setEnabled(newDoubleValue != scalarValueReal_.getValue());
			
		String newStringValue = String.valueOf(newDoubleValue);
		textField_.setText(newStringValue);

	}


	/**
	 * Submit text field change.
	 */
	public void submitTextFieldChange() {

		String newStringValue = textField_.getText();
		Double newDoubleValue = 0.0;
		
		try {
			newDoubleValue = Double.parseDouble(newStringValue);
			
		} catch (NumberFormatException e){
			
		}


		int valueInt = newDoubleValue.intValue();
		slider_.setValue(valueInt);
		
		btnSubmit_.setEnabled(newDoubleValue != scalarValueReal_.getValue());
		
	}


	/**
	 * Sets the value.
	 *
	 * @param scalarValueRealStruct the new value
	 */
	public void setValue(ScalarValueReal scalarValueReal) {

		scalarValueReal_ = scalarValueReal;
		String valueStr = scalarValueReal_.toString();
		
		Double doubleValue = scalarValueReal_.getValue();
		int valueInt = doubleValue.intValue();
		
		lblValue_.setText(valueStr);

		if (!isTextFieldInitialized_) {
			isTextFieldInitialized_ = true;
			slider_.setValue(valueInt);
			textField_.setText(valueStr);
		}

	}
}
