
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.Box;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import com.sri.straylight.fmuWrapper.*;



public class MyTable extends JPanel implements ResultEventListener  {
	
	
    private boolean DEBUG = false;
    
    
    private final JPanel topPanel = new JPanel();
    private final JButton btnRun = new JButton("Run");
    private final JButton btnClear = new JButton("Clear");
    
    private final JTextPane textPane = new JTextPane();;
    
    private FMU fmu_;
    
    public static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
    public static String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
    public static String nativeLibFolder = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug";
    

    

   // private String debugText;
    Document  doc;
    static final int MAX_CHARACTERS = 500;
    String newline = "\n";
    
    private SwingWorker worker;
    
    public MyTable() {
    	

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
        
        
        btnRun.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
        		runFMU();
            }
        });
        
        
        btnClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	clear();
            }
        });
        
        
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
    
    public void clear()  {
    	textPane.setText("");
    }
    
    private class Task extends SwingWorker<Void, Void>
    {
        public Void doInBackground()
        {
            	
        	outputText("runFMU");
        	
    		fmu_.init(unzipFolder);
    		fmu_.run();

            return null;
            
        }
    }
    
    

     
     
     public void  outputText(String txt) {
    	 

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
     
     

	
    private void runFMU() {
    
		fmu_ = new FMU(testFmuFile, nativeLibFolder);
		
		fmu_.disp.addListener(this);
		
        Task task = new Task();
        task.execute();

    }
    
	public void eventUpdate(ResultEvent re) {
		if (re.resultType == ResultType.resultType_newResults) {
			String str = re.resultString;
			outputText (str);
		} else if (re.resultType == ResultType.resultType_debug_message) {
			
			outputText (re.resultString);
		}
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
        MyTable newContentPane = new MyTable();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
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