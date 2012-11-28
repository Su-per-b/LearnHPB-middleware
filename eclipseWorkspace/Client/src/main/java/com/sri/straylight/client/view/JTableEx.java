package com.sri.straylight.client.view;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class JTableEx extends JTable {
	
	
	
	private static final long serialVersionUID = 1L;
	
	private int resultsCount_ = 0;
	
	
	public JTableEx(DefaultTableModel dataModel_) {
		super (dataModel_);
	}

	public void autoResizeColWidth() {
	    setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


	    int margin = 3;

	    for (int i = 0; i < this.getColumnCount(); i++) {
	        int                     vColIndex = i;
	        DefaultTableColumnModel colModel  = (DefaultTableColumnModel) this.getColumnModel();
	        TableColumn             col       = colModel.getColumn(vColIndex);
	        int                     width     = 0;

	        // Get width of column header
	        TableCellRenderer renderer = col.getHeaderRenderer();

	        if (renderer == null) {
	            renderer = this.getTableHeader().getDefaultRenderer();
	        }

	        Component comp = renderer.getTableCellRendererComponent(this, col.getHeaderValue(), false, false, 0, 0);

	        width = comp.getPreferredSize().width;

	        // Get maximum width of column data
	        for (int r = 0; r < this.getRowCount(); r++) {
	            renderer = this.getCellRenderer(r, vColIndex);
	            comp     = renderer.getTableCellRendererComponent(this, this.getValueAt(r, vColIndex), false, false,
	                    r, vColIndex);
	            width = Math.max(width, comp.getPreferredSize().width);
	        }

	        // Add margin
	        width += 2 * margin;

	        // Set the width
	        col.setPreferredWidth(width);
	    }

	    ((DefaultTableCellRenderer) this.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(
	        SwingConstants.LEFT);

	    // this.setAutoCreateRowSorter(true);
	    this.getTableHeader().setReorderingAllowed(false);


	}

	public void updateLayout() {
		resultsCount_++;
		if (resultsCount_ == 0 || resultsCount_ == 1 || resultsCount_ % 100 == 0) {
			autoResizeColWidth();
		}
	}
	
	
	
}
