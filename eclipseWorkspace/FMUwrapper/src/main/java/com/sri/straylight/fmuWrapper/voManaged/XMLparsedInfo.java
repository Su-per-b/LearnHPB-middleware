package com.sri.straylight.fmuWrapper.voManaged;




import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.Enu;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLparsed.
 */
public class XMLparsedInfo implements JsonSerializable  {

	/** The scalar variables all_. */
	private ScalarVariablesAll scalarVariablesAll_;
	private String sessionID_= "xxo";
	
	//constructor 1
	public XMLparsedInfo() {

	}
	
	//constructor 2
	public XMLparsedInfo(ScalarVariablesAll scalarVariablesAll) {
		scalarVariablesAll_ = scalarVariablesAll;	
	}
	

	public void setScalarVariablesAll(ScalarVariablesAll scalarVariablesAll) {
		scalarVariablesAll_ = scalarVariablesAll;
	}
	
	public void setSessionID(String sessionID) {
		sessionID_ = sessionID;
	}
	
	public ScalarVariablesAll getScalarVariablesAll() {
		return scalarVariablesAll_;
	}
	
	public String getSessionID() {
		return sessionID_;
	}



	
	public Vector<ScalarVariableReal> getInputVariables() {
		Vector<ScalarVariableReal> realVarList = scalarVariablesAll_.getInput().getRealVarList();
		return realVarList;
	}
	
	public Vector<ScalarVariableReal> getInternalVariables() {
		Vector<ScalarVariableReal> realVarList = scalarVariablesAll_.getInternal().getRealVarList();
		return realVarList;
	}
	
	public Vector<ScalarVariableReal> getOutputVariables() {
		Vector<ScalarVariableReal> realVarList = scalarVariablesAll_.getOutput().getRealVarList();
		return realVarList;
	}
	

	

	@Override
	public String toJson() {
		return JsonController.getInstance().toJson(this);
	}
	
	
	 @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers

            append(scalarVariablesAll_.hashCode()).
            toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        XMLparsedInfo typedObj = (XMLparsedInfo) obj;
        
        return new EqualsBuilder().
            append(this.scalarVariablesAll_, typedObj.getScalarVariablesAll()).
            isEquals();
    }

    
	public Vector<ScalarVariableReal> getVariables(Enu causality) {
		
		ScalarVariableCollection svc = scalarVariablesAll_.getScalarVariableCollection(causality);
		
		return svc.getRealVarList();
	}





}
