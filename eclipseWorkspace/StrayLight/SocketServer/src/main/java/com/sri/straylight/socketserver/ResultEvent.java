package com.sri.straylight.socketserver;

import java.util.EventListener;

import com.sri.straylight.fmu.ResultItemStruct;


public class ResultEvent extends java.util.EventObject {

	
	public String resultString;
	public ResultItemStruct resultItemStruct;
	
    //here's the constructor
    public ResultEvent(Object source) {

        super(source);

    }
    
    

}


