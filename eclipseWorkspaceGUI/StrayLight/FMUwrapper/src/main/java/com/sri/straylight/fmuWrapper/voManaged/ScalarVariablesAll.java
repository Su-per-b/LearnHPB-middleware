package com.sri.straylight.fmuWrapper.voManaged;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariablesAll.
 */
public class ScalarVariablesAll implements JsonSerializable{

	private ScalarVariableCollection input_;
	private ScalarVariableCollection output_;
	private ScalarVariableCollection internal_;
	
	public ScalarVariablesAll(ScalarVariablesAllStruct struct) {
		input_ = new ScalarVariableCollection(struct.input);
		output_ = new ScalarVariableCollection(struct.output);
		internal_ = new ScalarVariableCollection(struct.internal, 100);
	}


	public ScalarVariablesAll() {
		
	}


	//input
	public ScalarVariableCollection getInput() {
		return input_;
	}
	public void setInput(ScalarVariableCollection input) {
		this.input_ = input;
	}
	
	//output
	public ScalarVariableCollection getOutput() {
		return output_;
	}
	public void setOutput(ScalarVariableCollection output) {
		output_ = output;
	}
	
	//internal
	public ScalarVariableCollection getInternal() {
		return internal_;
	}
	public void setInternal(ScalarVariableCollection internal) {
		internal_ = internal;
	}
	
	
	@Override
	public String toJson() {
		return JsonController.getInstance().toJson(this);
	}
	
	
	 @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers

            append(input_.hashCode()).
            append(output_.hashCode()).
            append(internal_.hashCode()).
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

        ScalarVariablesAll typedObj = (ScalarVariablesAll) obj;
        
        return new EqualsBuilder().
            append(this.input_, typedObj.getInput()).
    		append(this.output_, typedObj.getOutput()).
			append(this.internal_, typedObj.getInternal()).
            isEquals();
    }
	
}
