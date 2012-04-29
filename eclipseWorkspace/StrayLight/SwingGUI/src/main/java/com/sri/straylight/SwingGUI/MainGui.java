
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
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import com.sri.straylight.fmuWrapper.*;



public class MainGui extends JPanel implements FMUeventListener   {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private boolean DEBUG = true;
    
    
    private final JPanel topPanel = new JPanel();
    private final JButton btnRun = new JButton("Run");
    private final JButton btnClear = new JButton("Clear");
    
    private final JTextPane textPane = new JTextPane();;
    

    private Document  doc;
    private String newline = "\n";
    private FmuConnectLocal fmuConnectLocal;
    long startTime;
    
    public MainGui() {
    	
    	

    	setPreferredSize(new Dimension(704, 800));

        String[] columnNames = {"First Name",
                                "Last Name",
                                "Sport",
                                "# of Years",
                                "Vegetarian"};

        Object[][] data = {
	    {"Kathy", "Smith",
	     "Snowboarding", new Integer(5), new Boolean(false)},
	    {"John", "Doe",
	     "Rowing", new Integer(3), new Boolean(true)},
	    {"Sue", "Black",
	     "Knitting", new Integer(2), new Boolean(false)},
	    {"Jane", "White",
	     "Speed reading", new Integer(20), new Boolean(true)},
	    {"Joe", "Brown",
	     "Pool", new Integer(10), new Boolean(false)}
        };

	    
	    doc = textPane.getDocument();
        
        setLayout(new BorderLayout(0, 0));
        FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        topPanel.setBackground(UIManager.getColor("Viewport.background"));
        topPanel.setMaximumSize(new Dimension(32767, 60));
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(topPanel, BorderLayout.NORTH);
        topPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        
     /*   
        btnRun.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	runSimulation();
            }
        });
        
     */
        btnRun.setEnabled(false);
        
        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	runSimulation();
             }
           }
        );
        
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	clear();
             }
           }
        ); 
        

        
        btnClear.setHorizontalAlignment(SwingConstants.LEFT);
        btnRun.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(btnRun);
        topPanel.add(btnClear);
        
	    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    add(tabbedPane, BorderLayout.CENTER);
	    
	    JPanel panelText = new JPanel();
	    tabbedPane.addTab("Debug Text", null, panelText, null);
	    panelText.setLayout(new GridLayout(0, 1, 0, 0));
	    
	    JScrollPane scrollPaneText = new JScrollPane();
	    panelText.add(scrollPaneText);
	    

	    scrollPaneText.setViewportView(textPane);
	    
	    JPanel panelTable = new JPanel();
	    tabbedPane.addTab("Results Table", null, panelTable, null);
	    panelTable.setBackground(Color.PINK);
	    panelTable.setPreferredSize(new Dimension(704, 500));
	    panelTable.setLayout(new GridLayout(1, 1, 0, 0));
	    
	    final JTable resultsTable = new JTable(data, columnNames);
	    
	    resultsTable.setPreferredScrollableViewportSize(new Dimension(700, 600));
	    resultsTable.setFillsViewportHeight(true);
	    
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(resultsTable);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    panelTable.add(scrollPaneTable);
	    
        CaretListenerLabel caretListenerLabel = new CaretListenerLabel("Caret Status");
        

        	
        if (DEBUG) {
            resultsTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(resultsTable);
                }
            });
        }
        
        
    	
    }
    
    

    
    
    public void init() {
    	
        fmuConnectLocal = new FmuConnectLocal();
        fmuConnectLocal.fmuEventDispatacher.addListener(this);
        
        fmuConnectLocal.init();
        
    }
    
    
    public void clear()  {
    	textPane.setText("");
    }
    

    
    

     
     public void  outputText(String txt) {
    	 
    	 long elapsedTimeMillis = System.currentTimeMillis()-startTime;
    	 Long lObj = new Long(elapsedTimeMillis);
    	 
    	 txt = lObj.toString() + " : " + txt;
    	 
	    String initString[] = {txt};
	    
	    int len = initString.length;
	    SimpleAttributeSet[] attrs = initAttributes(len);

        try {
          for (int i = 0; i < len; i++) {
            doc.insertString(doc.getLength(), initString[i] + newline, attrs[i]);
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
    	startTime = System.currentTimeMillis();
    	fmuConnectLocal.run();
    }
    
    
	public void onResultEvent(ResultEvent event) {
		outputText (event.resultString);
	}
	
    public void onMessageEvent(MessageEvent event) {
    	outputText (event.messageStruct.msgText);
    }
    
    public void onFMUstateEvent(FMUstateEvent event) {
    	//fmuEventDispatacher.fireStateEvent(event);
    	outputText ("State Change: "+ event.fmuState.toString());
    	

    	 btnRun.setEnabled(event.fmuState == State.fmuState_level_5_initializedFMU);
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

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MyTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MainGui newContentPane = new MainGui();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        newContentPane.init();
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                
                
            }
        });
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