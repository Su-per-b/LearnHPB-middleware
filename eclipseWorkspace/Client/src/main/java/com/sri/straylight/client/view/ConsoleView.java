package com.sri.straylight.client.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import ch.ethz.polyql.jql.domain.shared.Assert;

import com.sri.straylight.client.controller.ConsoleController;
import com.sri.straylight.client.model.ConsoleModel;

// TODO: Auto-generated Javadoc
/**
 * The Class OutputView.
 */
public class ConsoleView extends BaseView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	/** The doc_. */
	private Document  doc_;
	
	/** The newline_. */
	private final String newline_ = "\n";

	
	/** The text pane_. */
	private JTextPane textPane_ = new JTextPane();
	
	protected ConsoleModel dataModel_;
	
	
	
	public ConsoleView(ConsoleModel dataModel, ConsoleController parentController) {
		
		super(dataModel, parentController);
		
		
		dataModel_ = dataModel;
		
		doc_ = textPane_.getDocument();
		setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPaneText = new JScrollPane();
		add(scrollPaneText);
		scrollPaneText.setViewportView(textPane_);
		
	}
	
	
	
	/**
	 * Output text.
	 *
	 * @param txt the txt
	 */
	public void outputText(final String txt, final Color color ) {
		
		
		final SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, color);
		
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	
		    	Assert.assertNotNull(dataModel_, "dataModel_");
		    	
		    	Long elapsedTimeMillis = dataModel_.getElepasedTime();

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
