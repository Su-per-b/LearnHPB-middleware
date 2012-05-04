
package com.sri.straylight.SwingGUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
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

import com.sri.straylight.fmuWrapper.InitializedStruct;
import com.sri.straylight.fmuWrapper.MessageType;
import com.sri.straylight.fmuWrapper.State;
import com.sri.straylight.fmuWrapper.event.FMUeventListener;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;




public class MainPanel extends JPanel implements FMUeventListener   {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final boolean DEBUG_TABLE = true;
	
	//components
    private final JPanel topPanel_ = new JPanel();
    private final JPanel panelText_ = new JPanel();
    private final JPanel panelConfig_ = new JPanel();
    
    private final JButton btnInit_ = new JButton("Init");
    private final JButton btnRun_ = new JButton("Run Simulation");
    private final JButton btnClear_ = new JButton("Clear Debug Console");
    
    private final JTextPane textPane_ = new JTextPane();;
    private final JTabbedPane tabbedPane_ = new JTabbedPane(JTabbedPane.TOP);

    private ConfigModel configModel = new ConfigModel();
    
    private  JTable resultsTable_;
    
    private Document  doc_;
    private final String newline_ = "\n";

    
    private IFmuConnect fmuConnect_;
    
    
    private long startTime_ = 0;
    
    private DefaultTableModel resultsTableModel_;

    private int numbeOfResultsTabs = 0;
    
    
    public MainPanel() {
    	initMain_();
        initTopPanel_();
        initDebugConsole_();
        initConfig_();
    }
    
    
    private void initConfig_() {
    	FlowLayout flowLayout = new FlowLayout();
    	flowLayout.setAlignment(FlowLayout.LEFT);
    	
    	panelConfig_.setLayout(flowLayout);
    	
    	JPanel serverSelectionPanel = new JPanel();
    	serverSelectionPanel.setSize(100, 200);
    	
	    Border border = BorderFactory.createTitledBorder("Connect to:");
	    ButtonGroup serverSelectionGroup = new ButtonGroup();
	    
	    
	    JRadioButton rb_localhost = new JRadioButton("localhost");
	    rb_localhost.addActionListener(
    		new ActionListener() {
    		      public void actionPerformed(ActionEvent actionEvent) {
    		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
    		        System.out.println("Selected: " + aButton.getText());
    		        
    		        configModel.connectTo = ConnectTo.connecTo_localhost;
    		      }
    		    }
	    		
	    );
	    

	    JRadioButton rb_straylightsim_com = new JRadioButton("wintermute.straylightsim.com");
	   // rb_straylightsim_com.setSelected(true);
	    rb_straylightsim_com.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
	    		        System.out.println("Selected: " + aButton.getText());
	    		        configModel.connectTo = ConnectTo.connecTo_straylightsim_com;
	    		      }
	    		    }
		    		
		    );
	    
	    

	    JRadioButton rb_fmu_file = new JRadioButton("FMU file");
	    rb_fmu_file.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
	    		        System.out.println("Selected: " + aButton.getText());
	    		        configModel.connectTo = ConnectTo.connecTo_file;
	    		      }
	    		    }
	    		
		    );
	    
	    
	    serverSelectionGroup.add(rb_localhost);
	    serverSelectionGroup.add(rb_straylightsim_com);
	    serverSelectionGroup.add(rb_fmu_file);
	    
	    serverSelectionPanel.setBorder(border);
	    serverSelectionPanel.add(rb_localhost);
	    serverSelectionPanel.add(rb_straylightsim_com);
	    serverSelectionPanel.add(rb_fmu_file);
	    
	    rb_localhost.setSelected(configModel.connectTo == ConnectTo.connecTo_localhost);
	    rb_straylightsim_com.setSelected(configModel.connectTo == ConnectTo.connecTo_straylightsim_com);
	    rb_fmu_file.setSelected(configModel.connectTo == ConnectTo.connecTo_file);
	    
	    
	    panelConfig_.add(serverSelectionPanel);
	    tabbedPane_.addTab("Config", null, panelConfig_, null);
		
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
    
    
 private void initTable2_(InitializedStruct initializedStruct) {
    	
	    JPanel panelTable = new JPanel();

	    panelTable.setPreferredSize(new Dimension(704, 500));
	    panelTable.setLayout(new GridLayout(1, 1, 0, 0));
	    
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
    	
    	//if (!isTableInit) {
        	initTable2_(event.initializedStruct);
    	//}

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