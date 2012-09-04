package com.sri.straylight.client.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.sri.straylight.client.model.DoubleInputVerifier;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableBooleanPanel.
 */
public class ScalarVariableBooleanPanel extends JPanel {

	/** The lbl name_. */
	private JLabel lblName_;
	
	/** The lbl value_. */
	private JLabel lblValue_;

	/** The value_. */
	private ScalarValueBooleanStruct theValue_;
	
	/** The btn submit_. */
	private JButton btnSubmit_;

	/** The scalar variable boolean struct_. */
	private ScalarVariableBooleanStruct scalarVariableBooleanStruct_;
	
	/** The lbl description_. */
	private JLabel lblDescription_;
	
	/** The check box_. */
	private JCheckBox checkBox_;
	
	/**
	 * Create the panel.
	 *
	 * @param inputDetailView the input detail view
	 */
	public ScalarVariableBooleanPanel(InputDetailView inputDetailView) {
		
				
		Font normalFont = new Font("sansserif", Font.PLAIN, 12);
		Font boldFont = new Font("sansserif", Font.BOLD, 12);

		lblName_ = new JLabel("{Name}");
		lblName_.setFont(boldFont);

		JSeparator separator = new JSeparator();

		lblValue_ = new JLabel("{Value}");
		lblValue_.setFont(new Font("SansSerif", Font.BOLD, 12));

		JLabel lblEqual = new JLabel("=");

		btnSubmit_ = new JButton("Submit");
		btnSubmit_.setEnabled(false);
		
		lblDescription_ = new JLabel("{Description Not Set}");
		
		checkBox_ = new JCheckBox("New check box");
		

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
							.addComponent(separator, GroupLayout.PREFERRED_SIZE, 417, GroupLayout.PREFERRED_SIZE)
							.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblName_)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblEqual)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblValue_))
									.addComponent(checkBox_))
								.addPreferredGap(ComponentPlacement.RELATED, 245, Short.MAX_VALUE)
								.addComponent(btnSubmit_)))
						.addComponent(lblDescription_, GroupLayout.PREFERRED_SIZE, 408, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(23, Short.MAX_VALUE))
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
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(checkBox_))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(26)
							.addComponent(btnSubmit_)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblDescription_)
					.addContainerGap(43, Short.MAX_VALUE))
		);
		setLayout(groupLayout);


		setMaximumSize( new Dimension(400, 120) );

	}

	/**
	 * Sets the meta data.
	 *
	 * @param sv the new meta data
	 */
	public void setMetaData(ScalarVariableBooleanStruct sv) {
		
		scalarVariableBooleanStruct_ = sv;
		
		lblName_.setText(sv.name);
		lblName_.setToolTipText("Name: " + sv.name);

		btnSubmit_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitButtonPressed();
			}
		});

		
		lblDescription_.setText(sv.description);
		
	}
	

	/**
	 * Submit button pressed.
	 */
	public void submitButtonPressed() {
		
		boolean isSelected = checkBox_.isSelected();
		ScalarValueBooleanStruct scalarValue = new ScalarValueBooleanStruct();
		scalarValue.idx = scalarVariableBooleanStruct_.idx;

	}



	/**
	 * Sets the value.
	 *
	 * @param scalarValueBooleanStruct the new value
	 */
	public void setValue(ScalarValueBooleanStruct scalarValueBooleanStruct) {

		theValue_ = scalarValueBooleanStruct;
		String valueStr = String.valueOf(scalarValueBooleanStruct.value);
		
		lblValue_.setText(valueStr);
		checkBox_.setText(valueStr);
		

	}
	
}
