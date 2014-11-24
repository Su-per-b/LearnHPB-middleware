package com.sri.straylight.fmuWrapper.voManaged;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariablesAll.
 */
public class ScalarVariablesAll extends JsonSerializable{

	private ScalarVariableCollection input_;
	private ScalarVariableCollection output_;
	private ScalarVariableCollection internal_;
	
//	public ScalarVariablesAll(ScalarVariablesAllStruct struct) {
//		input_ = new ScalarVariableCollection(struct.input);
//		output_ = new ScalarVariableCollection(struct.output);
//		internal_ = new ScalarVariableCollection(struct.internal, 0);
//	}

	public ScalarVariablesAll(ScalarVariablesAllStruct struct, Boolean parseInternalVariablesFlag, Integer parseInternalVariableLimit) {
		input_ = new ScalarVariableCollection(struct.input);
		output_ = new ScalarVariableCollection(struct.output);
		
		if (parseInternalVariablesFlag) {
			internal_ = new ScalarVariableCollection(struct.internal, parseInternalVariableLimit);
		} else {
			internal_ = new ScalarVariableCollection();
		}

	}
	
	public ScalarVariablesAll() {
		
	}

	public ScalarVariableCollection getScalarVariableCollection() {
		return input_;
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
	public String serialize() {
		return JsonController.getInstance().serialize(this);
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
    



	public ScalarVariableCollection getScalarVariableCollection(Enu causality) {


		switch (causality) {
		
			case enu_input :  {
				return getInput();
			}
			case enu_output :  {
				return getOutput();
			}
			case enu_internal :  {
				return getInternal();
			}
			
			default : {
				return null;
			}

		}


	}
	
}
