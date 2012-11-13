/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.photovault.swingui.conflict;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;
import org.photovault.replication.Change;
import org.photovault.replication.FieldConflictBase;
import org.photovault.replication.ObjectHistory;
import org.photovault.replication.SetFieldConflict;
import org.photovault.replication.SetOperation;
import org.photovault.replication.ValueChangeXmlConverter;
import org.photovault.replication.ValueFieldConflict;

/**
 *
 * @author harri
 */
public class ResolveConflictTableModel extends AbstractTableModel {

    public ResolveConflictTableModel() {

    }

    Change ch1 = null;
    Change ch2 = null;
    Change merged = null;
    ArrayList<FieldConflictBase> conflicts = new ArrayList();
    ArrayList<String> fieldNames = new ArrayList();
    ArrayList<ConflictCellDesc> ch1Values = new ArrayList();
    ArrayList<ConflictCellDesc> ch2Values = new ArrayList();

    static enum ResolveState {
        ACCEPTED,
        REJECTED,
        UNDECIDED
    }

    static class ConflictCellDesc {
        ConflictCellDesc( ResolveState state, Object value ) {
            this.state = state;
            this.cellValue = value;
        }
        
        ResolveState state;
        Object cellValue;
        
        ResolveState getState() {
            return state;
        }
        
        void setState( ResolveState state ) {
            this.state = state;
        }
        
        Object getValue() {
            return cellValue;
        }

        public String toString() {
            return cellValue.toString();
        }
    }

    public void setChanges( Change ch1, Change ch2 ) {
        this.ch1 = ch1;
        this.ch2 = ch2;
        merged = ch1.merge( ch2 );
        conflicts = new ArrayList();
        rebuildTable();
        fireTableStructureChanged();
    }

    private void rebuildTable() {
        fieldNames.clear();
        ch1Values.clear();
        ch2Values.clear();
        for ( Object o: merged.getFieldConficts() ) {
            FieldConflictBase c = (FieldConflictBase) o;
            conflicts.add( c );
            String fieldName = c.getFieldName();
            Object val1 = null;
            Object val2 = null;
            if ( c instanceof ValueFieldConflict ) {
                ValueFieldConflict vc = (ValueFieldConflict) c;
                if ( vc.getProperty() != null && vc.getProperty().length() > 0 ) {
                    fieldName += "." + vc.getProperty();
                }
                val1 = vc.getConflictingValues().get( 0 );
                val2 = vc.getConflictingValues().get( 1 );
            } else if ( c instanceof SetFieldConflict ) {
                SetFieldConflict sc = (SetFieldConflict) c;
                fieldName += ": " + sc.getItem().toString();
                val1 = ( sc.getOperations().get( 0 ) == SetOperation.ADD ) ?
                    "added" : "removed";
                val2 = ( sc.getOperations().get( 1 ) == SetOperation.ADD ) ?
                    "added" : "removed";
            }
            fieldNames.add( fieldName );
            ch1Values.add( new ConflictCellDesc( ResolveState.UNDECIDED, val1 ) );
            ch2Values.add( new ConflictCellDesc( ResolveState.UNDECIDED, val2 ) );
        }

    }

    public int getRowCount() {
        return fieldNames.size();
    }

    public int getColumnCount() {
        return 3;
    }

    public Object getValueAt( int rowIndex, int columnIndex ) {
        switch( columnIndex ) {
            case 0:
                return fieldNames.get( rowIndex );
            case 1:
                return ch1Values.get( rowIndex );
            case 2:
                return ch2Values.get( rowIndex );
        }
        return null;
    }

}
