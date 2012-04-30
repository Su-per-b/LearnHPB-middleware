package com.sri.straylight.SwingGUI;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

class SimpleTableModel extends AbstractTableModel {
	
	public String[] m_colNames;
   // 

    public Class[] m_colTypes = { String.class, String.class, String.class, String.class,
        String.class, String.class, String.class, String.class, String.class, 
        String.class, String.class, String.class };

    Vector m_macDataVector;

    public SimpleTableModel(String[] colNames) {
      super();
      
      m_colNames = colNames;
      m_macDataVector = new Vector(10, 10);
     // m_macDataVector.addElement(new Data(new Integer(100), "A", "1","C", "E"));
      
    }
    
    
    public int getColumnCount() {
      return m_colNames.length;
    }
    public int getRowCount() {
      return m_macDataVector.size();
    }
    
    public void setValueAt(Object value, int row, int col) {
      Data macData = (Data) (m_macDataVector.elementAt(row));

      switch (col) {
      case 0:
        macData.setA((Integer) value);
        break;
      case 1:
        macData.setB((String) value);
        break;
      case 2:
        macData.setC((String) value);
        break;
      case 3:
        macData.setD((String) value);
        break;
      case 4:
        macData.setE((String) value);
        break;
      }
    }

    public String getColumnName(int col) {
      return m_colNames[col];
    }

    public Class getColumnClass(int col) {
      return m_colTypes[col];
    }
    
    
    public Object getValueAt(int row, int col) {
      Data macData = (Data) (m_macDataVector.elementAt(row));

      switch (col) {
      case 0:
        return macData.getA();
      case 1:
        return macData.getB();
      case 2:
        return macData.getC();
      case 3:
        return macData.getD();
      case 4:
        return macData.getE();
      }

      return new String();
    }
  }




class Data {
	  private Integer a;

	  private String b;

	  private String c;

	  private String d;

	  private String e;

	  public Data(String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11, String string12) {
	  
	  
	  }

	  public Data(Integer aa, String bb, String cc, String dd, String ee) {
	    a = aa;
	    b = bb;
	    c = cc;
	    d = dd;
	    e = ee;
	  }

	  public Integer getA() {
	    return a;
	  }

	  public String getB() {
	    return b;
	  }

	  public String getC() {
	    return c;
	  }

	  public String getD() {
	    return d;
	  }

	  public String getE() {
	    return e;
	  }

	  public void setA(Integer aa) {
	    a = aa;
	  }

	  public void setB(String macName) {
	    b = macName;
	  }

	  public void setC(String cc) {
	    c = cc;
	  }

	  public void setD(String dd) {
	    d = dd;
	  }

	  public void setE(String ee) {
	    e = ee;
	  }
	}