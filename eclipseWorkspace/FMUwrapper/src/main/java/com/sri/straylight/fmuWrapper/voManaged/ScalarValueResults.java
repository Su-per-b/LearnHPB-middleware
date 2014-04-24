package com.sri.straylight.fmuWrapper.voManaged;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;

public class ScalarValueResults extends JsonSerializable {
	

	private ScalarValueCollection input_;
	private ScalarValueCollection output_;
	private double time_;
	
	
	public ScalarValueResults(ScalarValueResultsStruct struct) {
		time_ = struct.time;
		input_ = new ScalarValueCollection(struct.input);
		output_ = new ScalarValueCollection(struct.output);
	}
	
	public ScalarValueResults() {

	}

	public ScalarValueCollection getInput() {
		return input_;
	}
	public ScalarValueCollection getOutput() {
		return output_;
	}
	
	public void setInput(ScalarValueCollection input) {
		input_ = input;
	}
	public void setOutput(ScalarValueCollection output) {
		output_ = output;
	}
	public void setTime(double time) {
		time_ = time;
	}

	public double getTime() {
		return time_;
	}
	
	public String toString() {
		String str = "time: " + Double.toString(time_) + " " +  output_.toString();
		return str;
	}




	
	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(this.input_).
            append(this.output_).
            append(this.time_).
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

        ScalarValueResults typedObj = (ScalarValueResults) obj;
        
        EqualsBuilder eqb = new EqualsBuilder().
                append(this.time_, typedObj.getTime()).
                append(this.input_, typedObj.getInput()).
                append(this.output_, typedObj.getOutput());
        
        
        boolean isEqual = eqb.isEquals();
        
        return isEqual;

    }
	
}
