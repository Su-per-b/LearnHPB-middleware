package com.sri.straylight.client.model;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;

public class ScalarVariableRealDataModel extends BaseModel {

	
	protected ScalarVariableReal scalarVariableReal_;
	
	protected ScalarValueReal scalarValueReal_;
	
	public ScalarVariableRealDataModel(String title, ScalarVariableReal scalarVariableReal,
			ScalarValueReal scalarValueReal) {
		
		super(title);
		
		scalarVariableReal_ = scalarVariableReal;
		scalarValueReal_ = scalarValueReal;
		
	}
	
	
	
	public ScalarVariableReal getScalarVariableReal() {
		return scalarVariableReal_;
	}
	
	public ScalarValueReal getScalarValueReal() {
		return scalarValueReal_;
	}



	public void setScalarValueReal(ScalarValueReal scalarValueReal) {
		scalarValueReal_ = scalarValueReal;
		
	}
	

}
