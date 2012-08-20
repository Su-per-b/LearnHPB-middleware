package com.sri.straylight.client.model;




import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Locale;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;


import org.apache.commons.validator.routines.DoubleValidator;

import ch.ethz.polyql.jql.domain.shared.Assert;


public class DoubleInputVerifier extends InputVerifier  {

	private final DoubleValidator validator = new DoubleValidator();
	private final String pattern;
	private final boolean scientific;
	private final Locale locale = ConfigProperties.DEFAULT_LOCALE;

	private BigDecimal min;
	private BigDecimal max;
	private boolean isValid_; 

	public DoubleInputVerifier(final String pattern) {
		Assert.assertNotNullOrEmpty(pattern, "pattern");
		this.pattern = pattern;
		scientific = this.pattern.indexOf('E') != -1;
		
        this.min = new BigDecimal(Double.toString(-Double.MAX_VALUE));
        this.max = new BigDecimal(Double.toString(Double.MAX_VALUE));

	}

	
	public void setRange(double min, double max) {

        this.min = new BigDecimal(Double.toString(min));
        this.max = new BigDecimal(Double.toString(max));
	}
	
	

	@Override
	public boolean verify(final JComponent input) {

		Assert.assertTrue(input instanceof JTextField, "input instanceof JTextField");

		final JTextField field = (JTextField) input;
		final String stringValue = field.getText();
		
		//validate based on pattern
		boolean isValid = validator.isValid(stringValue, pattern, locale);
		
		//validate based on range
		if (isValid) {
			BigDecimal bigDecimalValue = new BigDecimal(stringValue);
			isValid = bigDecimalValue.compareTo(max) <= 0 && bigDecimalValue.compareTo(min) >= 0;
		}
		
		
		//show state
		if (isValid) {
			input.setBackground(Constants.BACKGROUND_VERIFICATION_OK);
			field.setText(validateAndFormat(stringValue));
			return true;
		} else {
			input.setBackground(Constants.BACKGROUND_VERIFICATION_ERROR);
			return false;
		}
	}

	public boolean isValid(final String existing, final char newChar, final int insertPos) {
		// no spaces allowed:
		if (Character.isSpaceChar(newChar)) {
			return false;
		}
		// must be possible to delete:
		if (KeyEvent.VK_BACK_SPACE == newChar) {
			return true;
		}
		// E is ok if it is scientific and inserted at the end (validator.isValid returns false for this case)
		if ('E' == newChar && scientific
				&& (existing.indexOf('E') == -1)
				&& (existing.length() == insertPos)) {
			return true;
		}
		final String result = new StringBuffer(existing).insert(insertPos, newChar).toString();
		
		
		isValid_ = validator.isValid(result, pattern, locale);
				
		return isValid_;
	}
	
	
	public boolean getIsValid() {
		
		return isValid_;
	}

	private String validateAndFormat(final String s) {
		return validator.format(validator.validate(s, pattern, locale), pattern, locale);
	}

	public String format(final Double d) {
		return validator.format(d, pattern, locale);
	}

	public static DoubleInputVerifier getInstance(final String formatProperty) {
		return new DoubleInputVerifier("##0.0##");
	}
}