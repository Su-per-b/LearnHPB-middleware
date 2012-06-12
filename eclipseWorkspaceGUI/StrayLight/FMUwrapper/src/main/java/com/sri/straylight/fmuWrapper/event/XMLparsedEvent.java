package com.sri.straylight.fmuWrapper.event;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import com.sri.straylight.fmuWrapper.FMU;
import com.sri.straylight.fmuWrapper.voManaged.Initialized;
import com.sri.straylight.fmuWrapper.voNative.MetaDataStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;

public class XMLparsedEvent extends EventObject {
	
	private static final long serialVersionUID = 1L;
	public MetaDataStruct metaDataStruct;
	public Initialized initializedStruct;
	
    //here's the constructor
    public XMLparsedEvent(Object source) {
        super(source);
    }

	public static XMLparsedEvent factoryMake(FMU fmu) {
		
		
    	ArrayList<String> strList = new ArrayList<String>();
    	
    	ArrayList<ScalarVariableStruct> svnList = fmu.getScalarVariableOutputList();
    	int size = svnList.size();
    	
        Iterator<ScalarVariableStruct> itr = svnList.iterator();
        
        strList.add("time");
        
        while (itr.hasNext()) {
        	ScalarVariableStruct svm = itr.next();
        	strList.add(svm.name);
        }
        
        Initialized struct = new Initialized();
        struct.outputVarNames = strList.toArray(new String[strList.size()]);
        
        
        ArrayList<ScalarVariableStruct> listInputVars = fmu.getScalarVariableInputList();
        struct.inputVars =  listInputVars.toArray ( new ScalarVariableStruct[listInputVars.size()] );
        
        
        ArrayList<ScalarVariableStruct> listOutputVars = fmu.getScalarVariableOutputList();
        struct.outputVars =  listOutputVars.toArray ( new ScalarVariableStruct[listOutputVars.size()] );
       
        
        ArrayList<ScalarVariableStruct> listInternalVars = fmu.getScalarVariableInternalList();
        struct.internalVars =  listInternalVars.toArray ( new ScalarVariableStruct[listInternalVars.size()] );
        
        
        XMLparsedEvent event  = new XMLparsedEvent(fmu);
        event.initializedStruct = struct;
        
        
        return event;
        
        
	}	
    

	
}
