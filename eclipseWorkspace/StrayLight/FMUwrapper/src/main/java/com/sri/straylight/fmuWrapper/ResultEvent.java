package com.sri.straylight.fmuWrapper;

import java.util.EventListener;



public class ResultEvent extends java.util.EventObject {

	
	public String resultString;
	public ResultItemStruct resultItemStruct;
	public ResultType resultType = ResultType.resultType_newResults;
	
	
    //here's the constructor
    public ResultEvent(Object source) {
        super(source);
    }
    
    

}


