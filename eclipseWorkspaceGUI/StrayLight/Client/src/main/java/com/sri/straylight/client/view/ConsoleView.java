package com.sri.straylight.client.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import com.sri.straylight.client.controller.ConsoleController;
import com.sri.straylight.client.model.ConsoleModel;

// TODO: Auto-generated Javadoc
/**
 * The Class OutputView.
 */
public class ConsoleView extends BaseView {

	private static final String TITLE = "Console";
	
	/** The input form controller_. */
	private ConsoleController consoleController_;
	
	private ConsoleModel consoleModel_;
	
	/** The doc_. */
	private Document  doc_;
	
	/** The newline_. */
	private final String newline_ = "\n";

	private Style styleError_;
	private Style styleNormal_;
	
	/** The text pane_. */
	private JTextPane textPane_ = new JTextPane();
	
	
	
	public ConsoleView(ConsoleController consoleController, ConsoleModel theModel) {
		
		super(TITLE, 0);
		
		consoleController_ = consoleController;
		consoleModel_ = theModel;
		
		doc_ = textPane_.getDocument();
		setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPaneText = new JScrollPane();
		add(scrollPaneText);
		scrollPaneText.setViewportView(textPane_);
		
		styleError_ = textPane_.addStyle("Error Style", null);
		StyleConstants.setForeground(styleError_, Color.red);
		
		styleNormal_ = textPane_.addStyle("Normal Style", null);
		StyleConstants.setForeground(styleError_, Color.black);
		
	}
	
	
	
	/**
	 * Output text.
	 *
	 * @param txt the txt
	 */
	public void  outputText(final String txt, final Color color ) {
		
		
		final SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, color);
		
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	
		    	
		    	Long elapsedTimeMillis = consoleModel_.getElepasedTime();

				String txt2 = elapsedTimeMillis.toString() + " : " + txt;
				String initString[] = {txt2};

				int len = initString.length;
				int docLength = doc_.getLength();

				try {
					for (int i = 0; i < len; i++) {
						doc_.insertString(
								docLength + i, 
								initString[i] + newline_, 
								attributes);
					}

				} catch (BadLocationException ble) {
					System.err.println("Couldn't insert initial text.");
				}
			    
				textPane_.setCaretPosition(doc_.getLength());
				
		    }
		});
		 
	}



	public void clear() {
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
				textPane_.setText("");
		    }
		});

	}

	
	
}
