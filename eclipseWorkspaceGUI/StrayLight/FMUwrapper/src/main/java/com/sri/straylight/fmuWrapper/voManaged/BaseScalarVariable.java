package com.sri.straylight.fmuWrapper.voManaged;

import java.text.DecimalFormat;

import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStructBase;

public class BaseScalarVariable {
	
	/** The name. */
	protected String name_;
	
	/** The idx. */
	protected int idx_;
	
	/** The causality. */
	protected int causality_;
	
	/** The variability. */
	protected int variability_;
	
	/** The description. */
	protected String description_;
	
	/** The value reference. */
	protected int valueReference_;
	
	
	public BaseScalarVariable() {
		
	}


	public BaseScalarVariable(ScalarVariableStructBase struct) {
		
		setName(struct.name);
		setIdx(struct.idx);
		setCausality(struct.causality);
		setVariability(struct.variability);
		setDescription(struct.description);
		setValueReference(struct.valueReference);
		
	}
	
	public static String[] getColumnNamesArray() {
		
		String[] ary  = {
				"name",
				"value",
				"type",
				"start",
				"nominal",
				"min",
				"max",
				"causality",
				"variability",
				"description"
		};
		
		return ary;
		
	}
	
	/**
	 * @return the name_
	 */
	public String getName() {
		return name_;
	}



	/**
	 * @param name_ the name_ to set
	 */
	public void setName(String name_) {
		this.name_ = name_;
	}



	/**
	 * @return the idx_
	 */
	public int getIdx() {
		return idx_;
	}



	/**
	 * @param idx_ the idx_ to set
	 */
	public void setIdx(int idx_) {
		this.idx_ = idx_;
	}



	/**
	 * @return the causality_
	 */
	public int getCausality() {
		return causality_;
	}



	/**
	 * @param causality_ the causality_ to set
	 */
	public void setCausality(int causality_) {
		this.causality_ = causality_;
	}



	/**
	 * @return the variability_
	 */
	public int getVariability() {
		return variability_;
	}



	/**
	 * @param variability_ the variability_ to set
	 */
	public void setVariability(int variability_) {
		this.variability_ = variability_;
	}



	/**
	 * @return the description_
	 */
	public String getDescription() {
		return description_;
	}



	/**
	 * @param description_ the description_ to set
	 */
	public void setDescription(String description_) {
		this.description_ = description_;
	}



	/**
	 * @return the valueReference_
	 */
	public int getValueReference() {
		return valueReference_;
	}



	/**
	 * @param valueReference_ the valueReference_ to set
	 */
	public void setValueReference(int valueReference_) {
		this.valueReference_ = valueReference_;
	}
	
	
	/**
	 * Gets the causality enum.
	 *
	 * @return the causality enum
	 */
	public Enu getCausalityEnum() {

		Enu p = Enu.enu_constant;
		Enu theEnum  = p.getForValue (causality_);
		
		return theEnum;
	}
	
	/**
	 * Gets the variability enum.
	 *
	 * @return the variability enum
	 */
	public Enu getVariabilityEnum() {

		Enu p = Enu.enu_constant;
		Enu theEnum  = p.getForValue (variability_);
		
		return theEnum;
	}
	
	public String doubleToString(double inValue){
		DecimalFormat threeDec = new DecimalFormat("0.000");
		String shortString = (threeDec.format(inValue));
		return shortString;
	}
	
	
	
}
