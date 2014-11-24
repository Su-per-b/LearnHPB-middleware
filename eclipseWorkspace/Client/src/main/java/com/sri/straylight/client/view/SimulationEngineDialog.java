package com.sri.straylight.client.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import com.sri.straylight.client.ConnectTo;
import com.sri.straylight.client.controller.SimulationController;
import com.sri.straylight.client.model.ClientConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class SimulationEngineDialog.
 */
public class SimulationEngineDialog extends JDialog implements DragGestureListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientConfig configModel_;

	private JLabel fmuPathLbl_;
	private JPanel serverSelectionPanel_;
	private JPanel dropTargetPanel_;
	private JPanel mainPanel_;
	private SimulationController simulationController_;
	private File fmuFile_;

	
	/**
	 * Instantiates a new simulation engine dialog.
	 * 
	 * @param parent
	 *            the parent
	 * @param configModel
	 *            the config model
	 */
	public SimulationEngineDialog(JFrame parent, ClientConfig configModel, SimulationController simulationController ) {
		super(parent, "Select Simulation engine", true);

		this.simulationController_ = simulationController;
		
		configModel_ = configModel;

		setContent_();

		// Show it.
		this.setSize(new Dimension(400, 150));
		this.setLocationRelativeTo(parent);
		this.setVisible(true);

	}

	public void dragGestureRecognized(DragGestureEvent event) {
		Cursor cursor = null;
		JPanel panel = (JPanel) event.getComponent();

		Color color = panel.getBackground();
		if (event.getDragAction() == DnDConstants.ACTION_COPY) {
			cursor = DragSource.DefaultCopyDrop;
		}
		event.startDrag(cursor, new TransferableColor(color));
	}

	/**
	 * Sets the content_.
	 */
	private void setContent_() {

		// drop target
		dropTargetPanel_ = new JPanel();
		dropTargetPanel_.setSize(600, 150);
		fmuPathLbl_ = new JLabel("FMU File");
		dropTargetPanel_.add(fmuPathLbl_);
		dropTargetPanel_.setBackground(Color.red);

		new MyDropTargetListener(dropTargetPanel_, this);

	    DragSource ds = new DragSource();
	    ds.createDefaultDragGestureRecognizer(dropTargetPanel_, DnDConstants.ACTION_COPY, this);
		
		// server selection
		serverSelectionPanel_ = new JPanel();
		serverSelectionPanel_.setSize(600, 150);

		Border border = BorderFactory.createTitledBorder("Connect to:");
		ButtonGroup serverSelectionGroup = new ButtonGroup();

		JRadioButton rb_localhost = new JRadioButton("localhost");
		rb_localhost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton aButton = (AbstractButton) actionEvent
						.getSource();
				System.out.println("Selected: " + aButton.getText());

				configModel_.connectTo = ConnectTo.connectTo_localhost;
			}
		}

		);

		JRadioButton rb_pfalco_local = new JRadioButton("Pfalco Local");
		rb_pfalco_local.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton aButton = (AbstractButton) actionEvent
						.getSource();
				System.out.println("Selected: " + aButton.getText());
				configModel_.connectTo = ConnectTo.connectTo_pfalco_local;
			}
		}

		);

		JRadioButton rb_pfalco_global = new JRadioButton("Pfalco global");
		rb_pfalco_global.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton aButton = (AbstractButton) actionEvent
						.getSource();
				System.out.println("Selected: " + aButton.getText());
				configModel_.connectTo = ConnectTo.connectTo_pfalco_global;
			}
		}

		);

		JRadioButton rb_fmu_file = new JRadioButton("FMU file");
		rb_fmu_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton aButton = (AbstractButton) actionEvent
						.getSource();
				System.out.println("Selected: " + aButton.getText());
				configModel_.connectTo = ConnectTo.connectTo_file;
			}
		}

		);

		serverSelectionGroup.add(rb_localhost);
		serverSelectionGroup.add(rb_pfalco_global);
		serverSelectionGroup.add(rb_pfalco_local);
		serverSelectionGroup.add(rb_fmu_file);

		serverSelectionPanel_.setBorder(border);
		serverSelectionPanel_.add(rb_localhost);
		serverSelectionPanel_.add(rb_pfalco_global);
		serverSelectionPanel_.add(rb_pfalco_local);
		serverSelectionPanel_.add(rb_fmu_file);

		// main panel
		mainPanel_ = new JPanel();
		mainPanel_.setSize(600, 300);
		mainPanel_.add(serverSelectionPanel_);
		mainPanel_.add(dropTargetPanel_);
		setContentPane(mainPanel_);

		setLayout(new FlowLayout());

		// set the default
		rb_localhost
				.setSelected(configModel_.connectTo == ConnectTo.connectTo_localhost);
		rb_pfalco_global
				.setSelected(configModel_.connectTo == ConnectTo.connectTo_pfalco_global);
		rb_pfalco_local
				.setSelected(configModel_.connectTo == ConnectTo.connectTo_pfalco_local);
		rb_fmu_file
				.setSelected(configModel_.connectTo == ConnectTo.connectTo_file);

	}
	

	public void selectFile(File theFile) {
		
		
		fmuPathLbl_.setText(theFile.toString());
		fmuFile_ = theFile;
		simulationController_.setFMUfile(fmuFile_);
	
	}

}
