
package com.sri.straylight.SwingGUI;

import javax.swing.JPanel;




/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 


/*
 * SimpleTableDemo.java requires no other files.
 */

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import java.awt.Component;
import java.awt.BorderLayout;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import com.sri.straylight.fmuWrapper.InitializedStruct;
import com.sri.straylight.fmuWrapper.State;
import com.sri.straylight.fmuWrapper.event.FMUeventListener;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.test.*;



public class MainPanel extends JPanel implements FMUeventListener   {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final boolean DEBUG_TABLE = true;
    
	private final boolean IS_REMOTE = false;
	
	//components
    private final JPanel topPanel_ = new JPanel();
    private final JButton btnRun_ = new JButton("Run Simulation");
    private final JButton btnClear_ = new JButton("Clear Debug Console");
    private final JTextPane textPane_ = new JTextPane();;
    private final JTabbedPane tabbedPane_ = new JTabbedPane(JTabbedPane.TOP);
    private final JPanel panelText_ = new JPanel();;
    
    private  JTable resultsTable_;
    
    private Document  doc_;
    private final String newline_ = "\n";
    private FmuConnectLocal fmuConnectLocal_;
    private FmuConnectRemote fmuConnectRemote_ ;
    
    
    private long startTime_ = 0;
    
    private DefaultTableModel resultsTableModel_;

    
    public MainPanel() {
    	initMain_();
        initTopPanel_();
        initDebugConsole_();
    }
    
    
    private void initMain_() {
    	
    	startTime_ = System.currentTimeMillis();
    	
    	setPreferredSize(new Dimension(704, 800));
        setLayout(new BorderLayout(0, 0));
        FlowLayout flowLayout = (FlowLayout) topPanel_.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
    	
	    add(tabbedPane_, BorderLayout.CENTER);
    
    }
    
    private void initDebugConsole_() {
    	
    	doc_ = textPane_.getDocument();
	    panelText_.setLayout(new GridLayout(0, 1, 0, 0));
	    
	    JScrollPane scrollPaneText = new JScrollPane();
	    panelText_.add(scrollPaneText);
	    scrollPaneText.setViewportView(textPane_);
	    
	    tabbedPane_.addTab("Debug Console", null, panelText_, null);
    }
    
    
    
    
    //initializes the panel with the buttons on it
    private void initTopPanel_() {
    	
        topPanel_.setMaximumSize(new Dimension(32767, 60));
        topPanel_.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel_.setAlignmentY(Component.TOP_ALIGNMENT);
        btnRun_.setEnabled(false);
        
        btnRun_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	runSimulation();
             }
           }
        );
        
        btnClear_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	clear();
             }
           }
        );
        

        topPanel_.add(btnRun_);
        topPanel_.add(btnClear_);
        
        add(topPanel_, BorderLayout.NORTH);
    }
    
    
 private void initTable2_(InitializedStruct initializedStruct) {
    	
	    JPanel panelTable = new JPanel();

	    panelTable.setPreferredSize(new Dimension(704, 500));
	    panelTable.setLayout(new GridLayout(1, 1, 0, 0));
	    

		//String columnNames[] = initializedStruct.columnNames;
		Object[][] data = {{}};
		
	    
		resultsTableModel_ = new DefaultTableModel(data,initializedStruct.columnNames);
		
	    resultsTable_ = new JTable(resultsTableModel_);
	    resultsTable_.setPreferredScrollableViewportSize(new Dimension(700, 600));
	    resultsTable_.setFillsViewportHeight(true);
		
        if (DEBUG_TABLE) {
        	resultsTable_.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(resultsTable_);
                }
            });
        }
        
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(resultsTable_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    panelTable.add(scrollPaneTable);
	    
	    tabbedPane_.addTab("Results Table", null, panelTable, null);
        
    }
 
 
   

    
    public void init() {
    	if (IS_REMOTE) {
            fmuConnectRemote_ = new FmuConnectRemote();
            fmuConnectRemote_.fmuEventDispatacher.addListener(this);
            fmuConnectRemote_.init();
            
    	} else {
            fmuConnectLocal_ = new FmuConnectLocal();
            fmuConnectLocal_.fmuEventDispatacher.addListener(this);
            fmuConnectLocal_.init();
            
    	}
    }
    
    
    public void clear()  {
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
     

    private void runSimulation() {

    	fmuConnectLocal_.run();
    }
    
    
	public void onResultEvent(ResultEvent event) {
		outputText (event.resultString);
		Object[] newRow = event.resultItem.getStrings();

		resultsTableModel_.insertRow(1,newRow);
	}
	
    public void onMessageEvent(MessageEvent event) {
    	outputText (event.messageStruct.msgText);
    }
    
    public void onFMUstateEvent(FMUstateEvent event) {
    	outputText ("State Change: "+ event.fmuState.toString());
    	 btnRun_.setEnabled(event.fmuState == State.fmuState_level_5_initializedFMU);
    }

    
    
    public void onInitializedEvent(InitializedEvent event) {
    	outputText ("InitializedEvent: ");
    	initTable2_(event.initializedStruct);
    }
    
    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }



    
    
    protected class CaretListenerLabel extends JLabel implements CaretListener {
        public CaretListenerLabel(String label) {
          super(label);
        }

        // Might not be invoked from the event dispatch thread.
        public void caretUpdate(CaretEvent e) {
          displaySelectionInfo(e.getDot(), e.getMark());
        }

        // This method can be invoked from any thread. It
        // invokes the setText and modelToView methods, which
        // must run on the event dispatch thread. We use
        // invokeLater to schedule the code for execution
        // on the event dispatch thread.
        protected void displaySelectionInfo(final int dot, final int mark) {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	setText("status bar");
            }
          });
        }
      }
    
    
}