package com.sri.straylight.fmuWrapper.voManaged;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;

public class SessionControlModel 
		extends JsonSerializable
{

	protected SessionControlAction action_;
	protected String value_ = "";
	
	
	//constructor 1
	public SessionControlModel() {
		super();
	}
	
	
	//constructor 2
	public SessionControlModel(SessionControlAction action, String value) {
		super();
		
		action_ = action;
		value_ = value;
	}
	
	//constructor 3
	public SessionControlModel(SessionControlAction action) {
		super();
		setAction(action);

	}
	
	
	
	public String getValue() {
		return value_;
	}
	
	
	public SessionControlAction getAction() {
		return action_;
	}

	public int getActionAsInt() {
		return action_.getIntValue();
	}

	public String getActionAsString() {
		return action_.toString();
	}
	

	@Override
	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	
	
	 @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers

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

        SessionControlModel typedObj = (SessionControlModel) obj;
        
        return new EqualsBuilder().
                append(this.action_, typedObj.getAction()).
        		append(this.value_, typedObj.getValue()).
                isEquals();
        
    }


	public void setAction(SessionControlAction action) {
		action_ = action;
	}





}
