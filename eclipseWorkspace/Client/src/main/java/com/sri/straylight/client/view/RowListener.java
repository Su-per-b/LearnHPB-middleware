package com.sri.straylight.client.view;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RowListener implements ListSelectionListener   {
	
	

	JTableEx table_;
	InputView view_;
   
    public RowListener(InputView view)  
    {  
    	view_ = view;
    	table_ = view.getTable();  
    }  
   
    public void valueChanged(ListSelectionEvent e)  
    {  
        if(!e.getValueIsAdjusting())  
        {  
            ListSelectionModel model = table_.getSelectionModel();  
            int lead = model.getLeadSelectionIndex();  
            displayRowValues(lead);  
        }  
    }  
   
    private void displayRowValues(int rowIndex)  
    {  
    	view_.selectRow(rowIndex);
    }  
    
    
}
