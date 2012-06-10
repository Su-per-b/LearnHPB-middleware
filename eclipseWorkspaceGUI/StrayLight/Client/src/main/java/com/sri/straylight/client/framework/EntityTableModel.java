package com.sri.straylight.client.framework;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * A table model for Swing that works easily with rows of JavaBeans.
 *
 * @author Christian Bauer
 */
public class EntityTableModel<T> extends AbstractTableModel {

    private Class<T> entityClass;
    private List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>();
    private List<T> rows;

    public EntityTableModel(Class<T> entityClass, Collection<T> rows) {
        this.entityClass = entityClass;
        this.rows = new ArrayList<T>(rows);
    }

    public String getColumnName(int column) {
        return properties.get(column).getDisplayName();
    }

    public int getColumnCount() {
        return properties.size();
    }

    public int getRowCount() {
        return rows.size();
    }

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

    public void resetColumns() {
        properties = new ArrayList<PropertyDescriptor>();
    }

    public void setRow(int row, T entityInstance) {
        rows.remove(row);
        rows.add(row, entityInstance);
    }

    public void setRows(Collection<T> rows) {
        this.rows = new ArrayList<T>(rows);
    }

}