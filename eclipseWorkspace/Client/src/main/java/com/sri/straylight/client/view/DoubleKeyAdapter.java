package com.sri.straylight.client.view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import com.sri.straylight.client.model.DoubleInputVerifier;

// TODO: Auto-generated Javadoc
/**
 * The Class DoubleKeyAdapter.
 */
public class DoubleKeyAdapter extends KeyAdapter {
	
	/** The field. */
	final JTextField field;

	/**
	 * Instantiates a new double key adapter.
	 *
	 * @param field the field
	 */
	public DoubleKeyAdapter(final JTextField field) {
		this.field = field;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(final KeyEvent e) {
		if ('e' == e.getKeyChar()) {
			e.setKeyChar('E');
		}
		final DoubleInputVerifier verifier =
				(DoubleInputVerifier) field.getInputVerifier();
		if (!verifier.isValid(field.getText(),
				e.getKeyChar(),
				field.getCaretPosition())) {
			e.consume();
		}
	}
}
