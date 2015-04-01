package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

public class InitialState extends JsonSerializable{
	
	private ScalarValueCollection parameters_;
	private ConfigStruct configStruct_;
	private SerializableVector<StringPrimitive> outputVarList_;
	private SerializableVector<StringPrimitive> inputVarList_;
	
	
	public InitialState() {
	}
	
	public InitialState(ScalarValueCollection parameters, ConfigStruct configStruct, SerializableVector<StringPrimitive> outputVarList) {
		
	  this.parameters_ = parameters;
	  this.configStruct_ = configStruct;
	  
	  this.setOutputVarList(outputVarList);
  
	}
	

	public ScalarValueCollection getParameters() {
		return parameters_;
	}
	
	public void setParameters(ScalarValueCollection parameters) {
		this.parameters_ = parameters;
	}
	
	
	
	public ConfigStruct getConfigStruct() {
		return configStruct_;
	}
	public void setConfigStruct(ConfigStruct configStruct) {
		this.configStruct_ = configStruct;
	}
	
	
	public SerializableVector<StringPrimitive> getOutputVarList() {
		return outputVarList_;
	}
	
	public SerializableVector<StringPrimitive> getInputVarList() {
		return inputVarList_;
	}
	
	public void setOutputVarList(SerializableVector<StringPrimitive> outputVarList) {
		this.outputVarList_ = outputVarList;
	}
	
	public void setInputVarList(SerializableVector<StringPrimitive> inputVarList) {
		this.inputVarList_ = inputVarList;
	}
	
	
}
