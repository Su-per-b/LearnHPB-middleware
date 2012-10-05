package com.sri.straylight.fmuWrapper.framework;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

// TODO: Auto-generated Javadoc
/**
 * A table model for Swing that works easily with rows of JavaBeans.
 *
 * @param <T> the generic type
 * @author Christian Bauer
 */
public class EntityTableModel<T> extends AbstractTableModel {

    /** The entity class. */
    private Class<T> entityClass;
    
    /** The properties. */
    private List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>();
    
    /** The rows. */
    private List<T> rows;

    /**
     * Instantiates a new entity table model.
     *
     * @param entityClass the entity class
     * @param rows the rows
     */
    public EntityTableModel(Class<T> entityClass, Collection<T> rows) {
        this.entityClass = entityClass;
        this.rows = new ArrayList<T>(rows);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        return properties.get(column).getDisplayName();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return properties.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return rows.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column) {
        Object value = null;
        T entityInstance = rows.get(row);
        if (entityInstance != null) {
            PropertyDescriptor property = properties.get(column);
            Method readMethod = property.getReadMethod();
            try {
                value = readMethod.invoke(entityInstance);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return value;
    }

    /**
     * Adds the column.
     *
     * @param displayName the display name
     * @param propertyName the property name
     */
    public void addColumn(String displayName, String propertyName) {
        try {
            PropertyDescriptor property =
                    new PropertyDescriptor(propertyName, entityClass, propertyName, null);
            property.setDisplayName(displayName);
            properties.add(property);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Reset columns.
     */
    public void resetColumns() {
        properties = new ArrayList<PropertyDescriptor>();
    }

    /**
     * Sets the row.
     *
     * @param row the row
     * @param entityInstance the entity instance
     */
    public void setRow(int row, T entityInstance) {
        rows.remove(row);
        rows.add(row, entityInstance);
    }

    /**
     * Sets the rows.
     *
     * @param rows the new rows
     */
    public void setRows(Collection<T> rows) {
        this.rows = new ArrayList<T>(rows);
    }

}