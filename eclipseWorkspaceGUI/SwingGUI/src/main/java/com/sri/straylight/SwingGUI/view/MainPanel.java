
package com.sri.straylight.SwingGUI.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


import com.sri.straylight.SwingGUI.ConnectTo;
import com.sri.straylight.SwingGUI.model.Config;
import com.sri.straylight.SwingGUI.model.FmuConnectLocal;
import com.sri.straylight.SwingGUI.model.FmuConnectRemote;
import com.sri.straylight.SwingGUI.model.IFmuConnect;
import com.sri.straylight.fmuWrapper.event.FMUeventListener;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.InitializedInfo;
import com.sri.straylight.fmuWrapper.voNative.MessageType;
import com.sri.straylight.fmuWrapper.voNative.State;




public class MainPanel extends JPanel implements FMUeventListener   {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//components
    private final JPanel topPanel_ = new JPanel();
    private final JPanel panelText_ = new JPanel();
    private final JPanel panelConfig_ = new JPanel();
    
    private final JButton btnInit_ = new JButton("Init");
    private final JButton btnRun_ = new JButton("Run Simulation");
    private final JButton btnClear_ = new JButton("Clear Debug Console");
    
    private final JTextPane textPane_ = new JTextPane();;
    private final JTabbedPane tabbedPane_ = new JTabbedPane(JTabbedPane.TOP);

    private Config configModel = new Config();
    
    private  JTable resultsTable_;
    private DefaultTableModel resultsTableModel_;
    
    private  JTable inputTable_;
    private DefaultTableModel inputTableModel_;
    
    private  JTable internalTable_;
    private DefaultTableModel internalTableModel_;
    							
    private Document  doc_;
    private final String newline_ = "\n";

    private IFmuConnect fmuConnect_;
    private long startTime_ = 0;
    
    private int numbeOfResultsTabs = 0;
    private int numbeOfInputTabs = 0;


    
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
        
        btnInit_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	btnInit_.setEnabled(false);
            	init();
             }
           }
        );
        
        
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
        
        topPanel_.add(btnInit_);
        topPanel_.add(btnRun_);
        topPanel_.add(btnClear_);

        
        add(topPanel_, BorderLayout.NORTH);
    }
    
    
    private void initInputTable_(InitializedInfo initializedStruct) {  
    	
	    JPanel panelTable = new JPanel();
	    
	    panelTable.setPreferredSize(new Dimension(704, 500));
	    panelTable.setLayout(new GridLayout(1, 1, 0, 0));
	    
		inputTableModel_ = new DefaultTableModel (
				
				initializedStruct.getInputData(),
				initializedStruct.getColumnNames()
		);
		

		inputTable_ = new JTable(inputTableModel_);
		inputTable_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		inputTable_.setFillsViewportHeight(true);
		
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(inputTable_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    panelTable.add(scrollPaneTable);
	    
	    numbeOfInputTabs++;
	    
	    tabbedPane_.addTab("Input Table " + String.valueOf(numbeOfInputTabs), null, panelTable, null);
	    
	    
    }
    
    
    private void initResultsTable_(InitializedInfo initializedStruct) {
    	
	    JPanel panelTable = new JPanel();

	    panelTable.setPreferredSize(new Dimension(704, 500));
	    panelTable.setLayout(new GridLayout(1, 1, 0, 0));
	    
		Object[][] data = {{}};
		
	    
		resultsTableModel_ = new DefaultTableModel(data,initializedStruct.columnNames);
		
	    resultsTable_ = new JTable(resultsTableModel_);
	    resultsTable_.setPreferredScrollableViewportSize(new Dimension(700, 600));
	    resultsTable_.setFillsViewportHeight(true);
		
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(resultsTable_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    panelTable.add(scrollPaneTable);
	    
	    numbeOfResultsTabs++;
	    
	    tabbedPane_.addTab("Results Table " + String.valueOf(numbeOfResultsTabs), null, panelTable, null);
        
    }
 
 
   

    
    public void init() {
    	
    	
    	switch (configModel.connectTo) {
    	
    		case connecTo_localhost :
    			fmuConnect_ = new FmuConnectRemote("localhost");
    			break;
    		case connecTo_straylightsim_com :
    			fmuConnect_ = new FmuConnectRemote("wintermute.straylightsim.com");
    			break;
    		case connecTo_file :
    			fmuConnect_ = new FmuConnectLocal();
    			break;
    	}

    	
    	//fmuConnect_.addListener(this);
    	fmuConnect_.init(this);

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

    	fmuConnect_.run();
    }
    
    
	public void onResultEvent(ResultEvent event) {
		outputText (event.resultString);
		Object[] newRow = event.resultItem.getStrings();

		resultsTableModel_.insertRow(1,newRow);
	}
	
    public void onMessageEvent(MessageEvent event) {
    	
    	//System.out.print("MainPanel.onMessageEvent Thread: "  + Thread.currentThread().getName());
    	
    	outputText (event.messageStruct.msgText);
    	
    	if (event.messageStruct.getMessageTypeEnum() == MessageType.messageType_error) {
    		btnInit_.setEnabled(true);
    	}
    	
    }
    
    public void onFMUstateEvent(FMUstateEvent event) {
    	outputText ("State Change: "+ event.fmuState.toString());
    	 btnRun_.setEnabled(event.fmuState == State.fmuState_level_5_initializedFMU);
    	 
    	 if (event.fmuState == State.fmuState_cleanedup) {
    		 
    		 btnInit_.setEnabled(true);
    	 }

    }

    
    
    public void onInitializedEvent(InitializedEvent event) {
    	outputText ("InitializedEvent: ");
    	
        initResultsTable_(event.initializedStruct);
        initInputTable_(event.initializedStruct);
        initInternalTable_(event.initializedStruct);

    }

    
    
    private void initInternalTable_(InitializedInfo initializedStruct) {
	    JPanel panelTable = new JPanel();
	    
	    panelTable.setPreferredSize(new Dimension(704, 500));
	    panelTable.setLayout(new GridLayout(1, 1, 0, 0));
	    
	    internalTableModel_ = new DefaultTableModel (
			initializedStruct.getInternalData(),
			initializedStruct.getColumnNames()
		);
		
		internalTable_ = new JTable(internalTableModel_);
		internalTable_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		internalTable_.setFillsViewportHeight(true);
		
		
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(internalTable_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    panelTable.add(scrollPaneTable);
	    
	    tabbedPane_.addTab("Internal Variables " + String.valueOf(numbeOfInputTabs), null, panelTable, null);
	    
	}



	protected class CaretListenerLabel extends JLabel implements CaretListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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