package com.sri.straylight.client.model;




import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Locale;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;


import org.apache.commons.validator.routines.DoubleValidator;

import ch.ethz.polyql.jql.domain.shared.Assert;


// TODO: Auto-generated Javadoc
/**
 * The Class DoubleInputVerifier.
 */
public class DoubleInputVerifier extends InputVerifier  {

	/** The validator. */
	private final DoubleValidator validator = new DoubleValidator();
	
	/** The pattern. */
	private final String pattern;
	
	/** The scientific. */
	private final boolean scientific;
	
	/** The locale. */
	private final Locale locale = ConfigProperties.DEFAULT_LOCALE;

	/** The min. */
	private BigDecimal min;
	
	/** The max. */
	private BigDecimal max;
	
	/** The is valid_. */
	private boolean isValid_; 

	/**
	 * Instantiates a new double input verifier.
	 *
	 * @param pattern the pattern
	 */
	public DoubleInputVerifier(final String pattern) {
		Assert.assertNotNullOrEmpty(pattern, "pattern");
		this.pattern = pattern;
		scientific = this.pattern.indexOf('E') != -1;
		
        this.min = new BigDecimal(Double.toString(-Double.MAX_VALUE));
        this.max = new BigDecimal(Double.toString(Double.MAX_VALUE));

	}

	
	/**
	 * Sets the range.
	 *
	 * @param min the min
	 * @param max the max
	 */
	public void setRange(double min, double max) {

        this.min = new BigDecimal(Double.toString(min));
        this.max = new BigDecimal(Double.toString(max));
	}
	
	

	/* (non-Javadoc)
	 * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
	 */
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

	/**
	 * Checks if is valid.
	 *
	 * @param existing the existing
	 * @param newChar the new char
	 * @param insertPos the insert pos
	 * @return true, if is valid
	 */
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
	
	
	/**
	 * Gets the checks if is valid.
	 *
	 * @return the checks if is valid
	 */
	public boolean getIsValid() {
		
		return isValid_;
	}

	/**
	 * Validate and format.
	 *
	 * @param s the s
	 * @return the string
	 */
	private String validateAndFormat(final String s) {
		return validator.format(validator.validate(s, pattern, locale), pattern, locale);
	}

	/**
	 * Format.
	 *
	 * @param d the d
	 * @return the string
	 */
	public String format(final Double d) {
		return validator.format(d, pattern, locale);
	}

	/**
	 * Gets the single instance of DoubleInputVerifier.
	 *
	 * @param formatProperty the format property
	 * @return single instance of DoubleInputVerifier
	 */
	public static DoubleInputVerifier getInstance(final String formatProperty) {
		return new DoubleInputVerifier("##0.0##");
	}
}