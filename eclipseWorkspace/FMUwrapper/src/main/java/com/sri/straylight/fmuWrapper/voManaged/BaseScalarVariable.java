package com.sri.straylight.fmuWrapper.voManaged;

import java.text.DecimalFormat;

import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStructBase;

public class BaseScalarVariable extends JsonSerializable {

	protected String name_;
	protected int idx_;
	protected int causality_;
	protected int variability_;
	protected String description_;
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

		String[] ary = { "name", "value", "unit", "type", "start", "nominal", "min",
				"max", "causality", "variability", "description" };

		return ary;

	}

	public String getName() {
		return name_;
	}


	public void setName(String name_) {
		this.name_ = name_;
	}


	

	public int getIdx() {
		return idx_;
	}

	public void setIdx(int idx_) {
		this.idx_ = idx_;
	}

	public int getCausalityAsInt() {
		return causality_;
	}

	public Enu getCausalityAsEnum() {
		Enu p = Enu.enu_constant;
		Enu theEnum = p.getForValue(causality_);

		return theEnum;
	}
	
	public String getCausalityAsString() {
		Enu theEnum = getCausalityAsEnum();

		return theEnum.toString();
	}
	

	public void setCausality(int causality) {
		this.causality_ = causality;
	}

	public void setCausality(Enu causality) {
		this.causality_ = causality.getIntValue();
	}

	public int getVariabilityAsInt() {
		return variability_;
	}
	public Enu getVariabilityAsEnum() {
		Enu p = Enu.enu_constant;
		Enu theEnum = p.getForValue(variability_);

		return theEnum;
	}
	public String getVariabilityAsString() {
		Enu theEnum = getVariabilityAsEnum();

		return theEnum.toString();
	}
	
	public void setVariability(int variability) {
		variability_ = variability;
	}
	public void setVariability(Enu variability) {
		variability_ = variability.getIntValue();
	}

	
	
	
	public String getDescription() {
		return description_;
	}

	public void setDescription(String description_) {
		this.description_ = description_;
	}

	public int getValueReference() {
		return valueReference_;
	}

	public void setValueReference(int valueReference_) {
		this.valueReference_ = valueReference_;
	}


	public String doubleToString(double inValue) {
		DecimalFormat threeDec = new DecimalFormat("0.000");
		String shortString = (threeDec.format(inValue));
		return shortString;
	}

}
