package com.sri.straylight.client.controller;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.action.ClearDebugConsoleAction;
import com.sri.straylight.client.event.action.InitAction;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.client.model.FmuConnectLocal;
import com.sri.straylight.client.model.FmuConnectRemote;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;

public class DebugConsoleController  extends AbstractController {


	private JTextPane textPane_ = new JTextPane();
	private long startTime_ = 0;
	private Document  doc_;
	private final String newline_ = "\n";


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
	
	
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {
		outputText (event.resultString);
	}
	

	@EventSubscriber(eventClass=MessageEvent.class)
	public void onMessageEvent(MessageEvent event) {
		outputText (event.messageStruct.msgText);
	}
	
	
	@EventSubscriber(eventClass=ClearDebugConsoleAction.class)
	public void onClearDebugConsoleAction(ClearDebugConsoleAction event) {
		textPane_.setText("");
	}
	

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


	protected SimpleAttributeSet[] initAttributes(int length) {

		SimpleAttributeSet[] attrs = new SimpleAttributeSet[length];

		attrs[0] = new SimpleAttributeSet();
		StyleConstants.setFontSize(attrs[0], 12);
		StyleConstants.setFontFamily(attrs[0], "Sans Serif");

		return attrs;
	}





}
