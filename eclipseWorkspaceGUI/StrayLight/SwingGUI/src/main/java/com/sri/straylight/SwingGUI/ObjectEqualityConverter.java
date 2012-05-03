package com.sri.straylight.SwingGUI;

import org.jdesktop.beansbinding.Converter;


public class ObjectEqualityConverter extends Converter {
	private Object o;

	public ObjectEqualityConverter() {}

	public ObjectEqualityConverter(Object o) {
		this.o = o;
	}

	public Object convertForward(Object value) {
		return (o != null && o.equals(value)) ? Boolean.TRUE : Boolean.FALSE;
	}

	public Object convertReverse(Object value) {
		return (Boolean.TRUE.equals(value)) ? o : null;
	}
}