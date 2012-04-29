package com.sri.straylight.fmuWrapper;

import java.util.EventObject;



public class ResultEvent extends EventObject {

	
	public String resultString;
	public ResultItemStruct resultItemStruct;
	//public ResultType resultType = ResultType.resultType_newResults;
	
	
    //here's the constructor
    public ResultEvent(Object source) {
        super(source);
    }
    
    

}


