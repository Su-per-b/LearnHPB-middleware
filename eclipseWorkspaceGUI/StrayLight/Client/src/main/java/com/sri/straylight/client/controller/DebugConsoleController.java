package com.sri.straylight.client.controller;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ClearDebugConsoleAction;
import com.sri.straylight.client.event.SimStateNotify;
import com.sri.straylight.client.event.SimStateRequest;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;


// TODO: Auto-generated Javadoc
/**
 * The Class DebugConsoleController.
 */
public class DebugConsoleController  extends AbstractController {


	/** The text pane_. */
	private JTextPane textPane_ = new JTextPane();
	
	/** The start time_. */
	private long startTime_ = 0;
	
	/** The doc_. */
	private Document  doc_;
	
	/** The newline_. */
	private final String newline_ = "\n";


	/**
	 * Instantiates a new debug console controller.
	 *
	 * @param parentController the parent controller
	 */
	public DebugConsoleController (AbstractController parentController) {
		super(parentController);

		startTime_ = System.currentTimeMillis();

		JPanel panel = new JPanel();

		doc_ = textPane_.getDocument();
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPaneText = new JScrollPane();
		panel.add(scrollPaneText);
		scrollPaneText.setViewportView(textPane_);
		
		setView_(panel);
	}
	
	
	/**
	 * On sim state request.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateRequest.class)
	public void onSimStateRequest(SimStateRequest event) {
		outputText ("SimStateRequest: " + event.getPayload().toString());
	}
	
	/**
	 * On sim state request.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateNotify.class)
	public void onSimStateRequest(SimStateNotify event) {
		outputText ("SimStateNotify: " + event.getPayload().toString());
	}
	
	
	/**
	 * On result event.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {
		
		ScalarValueResults scalarValueResults = event.getScalarValueResults();
		
		outputText (scalarValueResults.toString());
	}
	

	/**
	 * On message event.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=MessageEvent.class)
	public void onMessageEvent(MessageEvent event) {
		outputText (event.messageStruct.msgText);
	}
	
	
	/**
	 * On clear debug console action.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ClearDebugConsoleAction.class)
	public void onClearDebugConsoleAction(ClearDebugConsoleAction event) {
		textPane_.setText("");
	}
	
	
	/**
	 * Reset.
	 */
	public void reset() {
		
		textPane_.setText("reset");
		
	}
	
	
	/**
	 * Output text.
	 *
	 * @param txt the txt
	 */
	public void  outputText(String txt) {

		long elapsedTimeMillis = System.currentTimeMillis()-startTime_;

		Long lObj = new Long(elapsedTimeMillis);

		txt = lObj.toString() + " : " + txt;

		String initString[] = {txt};

		int len = initString.length;
		SimpleAttributeSet[] attrs = initAttributes(len);

		try {
			for (int i = 0; i < len; i++) {
				doc_.insertString(doc_.getLength(), initString[i] + newline_, attrs[i]);
			}

		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text.");
		}

	}


	/**
	 * Inits the attributes.
	 *
	 * @param length the length
	 * @return the simple attribute set[]
	 */
	protected SimpleAttributeSet[] initAttributes(int length) {

		SimpleAttributeSet[] attrs = new SimpleAttributeSet[length];

		attrs[0] = new SimpleAttributeSet();
		StyleConstants.setFontSize(attrs[0], 12);
		StyleConstants.setFontFamily(attrs[0], "Sans Serif");

		return attrs;
	}





}
