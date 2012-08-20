package com.sri.straylight.client.view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import com.sri.straylight.client.model.DoubleInputVerifier;

public class DoubleKeyAdapter extends KeyAdapter {
	final JTextField field;

	public DoubleKeyAdapter(final JTextField field) {
		this.field = field;
	}

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
