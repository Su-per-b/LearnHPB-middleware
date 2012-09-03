package com.sri.straylight.client.view;

import java.awt.Dimension;
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
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sri.straylight.client.event.ScalarValueChangeRequest;
import com.sri.straylight.client.model.DoubleInputVerifier;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;

public class ScalarVariableRealPanel extends JPanel {

	private JLabel lblName_;
	private JLabel lblMin_;
	private JLabel lblMax_;

	private JLabel lblStart_;

	private JTextField textField;
	private JLabel lblNominal_;
	private JLabel lblValue_;
	private JSlider slider_;
	private JTextField textField_;

	private ScalarValueRealStruct scalarValueRealStruct_;
	private JButton btnSubmit_;
	private DoubleInputVerifier doubleInputVerifier_;

	private boolean isTextFieldInitialized_ = false;
	private InputDetailView inputDetailView_;
	
	private ScalarVariableRealStruct scalarVariableRealStruct_;
	private JLabel lblDescription_;
	
	/**
	 * Create the panel.
	 */
	public ScalarVariableRealPanel(InputDetailView inputDetailView) {
		
		inputDetailView_ = inputDetailView;
				
		Font normalFont = new Font("sansserif", Font.PLAIN, 12);
		Font boldFont = new Font("sansserif", Font.BOLD, 12);

		lblName_ = new JLabel("{Name}");
		lblName_.setFont(boldFont);

		lblMin_ = new JLabel("{Min}");
		lblMin_.setFont(normalFont);

		lblMax_ = new JLabel("{Max}");
		lblMax_.setFont(normalFont);


		JSeparator separator = new JSeparator();
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


		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblMin_)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(slider_, GroupLayout.PREFERRED_SIZE, 349, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblMax_))
								.addComponent(lblDescription_, GroupLayout.PREFERRED_SIZE, 408, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(separator, GroupLayout.PREFERRED_SIZE, 417, GroupLayout.PREFERRED_SIZE)
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
									.addComponent(btnSubmit_)))))
					.addContainerGap(47, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 6, GroupLayout.PREFERRED_SIZE)
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
					.addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblMin_)
						.addComponent(lblMax_)
						.addComponent(slider_, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblDescription_)
					.addContainerGap())
		);
		setLayout(groupLayout);


		setMaximumSize( new Dimension(400, 120) );

	}

	public void setMetaData(ScalarVariableRealStruct sv) {
		
		scalarVariableRealStruct_ = sv;
		
		lblName_.setText(sv.name);
		lblName_.setToolTipText("Name: " + sv.name);

		String minStr = String.valueOf(sv.typeSpecReal.min);
		lblMin_.setText(minStr);
		lblMin_.setToolTipText("Minimum Value: " + minStr);

		String maxStr = String.valueOf(sv.typeSpecReal.max);
		lblMax_.setText(maxStr);
		lblMax_.setToolTipText("Maximum Value: " + maxStr);

		String startStr = String.valueOf(sv.typeSpecReal.start);
		lblStart_.setText("Start: " + startStr);
		lblStart_.setToolTipText("Start Value: " + startStr);

		String nominalStr = String.valueOf(sv.typeSpecReal.nominal);
		lblNominal_.setText("  Nominal: " + nominalStr);
		lblNominal_.setToolTipText("Nominal Value: " + nominalStr);

		slider_.setMinimum((int)sv.typeSpecReal.min);
		slider_.setMaximum((int)sv.typeSpecReal.max);


		doubleInputVerifier_ = DoubleInputVerifier.getInstance("##0.00##");
		doubleInputVerifier_.setRange(sv.typeSpecReal.min, sv.typeSpecReal.max);

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
				boolean flag = doubleInputVerifier_.verify(textField_);
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

		
		lblDescription_.setText(sv.description);
		
		
	}

	public void submitButtonPressed() {
		
		String newStringValue = textField_.getText();
		double newDoubleValue = Double.parseDouble(newStringValue);
		
		Vector<ScalarValueRealStruct> scalarValueList = new Vector<ScalarValueRealStruct>();
		
		ScalarValueRealStruct scalarValue = new ScalarValueRealStruct();
		scalarValue.idx = scalarVariableRealStruct_.idx;
		scalarValue.value = newDoubleValue;
		
		scalarValueList.add(scalarValue);
		
		ScalarValueChangeRequest event = new ScalarValueChangeRequest(this, scalarValueList);
		inputDetailView_.onDataModelUpdateRequest(event);
		
		btnSubmit_.setEnabled(false);
		
	}

	public void submitSliderChange() {

		int newIntValue = slider_.getValue();
		double newDoubleValue = (double) newIntValue;
		

		btnSubmit_.setEnabled(newDoubleValue != scalarValueRealStruct_.value);
			


		String newStringValue = String.valueOf(newDoubleValue);
		textField_.setText(newStringValue);

	}


	public void submitTextFieldChange() {

		String newStringValue = textField_.getText();
		double newDoubleValue = Double.parseDouble(newStringValue);

		int valueInt = (int) newDoubleValue;
		slider_.setValue(valueInt);
		btnSubmit_.setEnabled(newDoubleValue != scalarValueRealStruct_.value);
		
		

	}


	public void setValue(ScalarValueRealStruct scalarValueRealStruct) {

		scalarValueRealStruct_ = scalarValueRealStruct;
		
		String valueStr = String.valueOf(scalarValueRealStruct.value);
		int valueInt = (int) scalarValueRealStruct.value;
		
		lblValue_.setText(valueStr);


		if (!isTextFieldInitialized_) {
			isTextFieldInitialized_ = true;
			slider_.setValue(valueInt);
			textField_.setText(valueStr);
		}

	}
}
