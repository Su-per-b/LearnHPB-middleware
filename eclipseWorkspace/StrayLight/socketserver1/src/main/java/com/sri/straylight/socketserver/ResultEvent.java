package com.sri.straylight.socketserver;

import java.util.EventListener;


public class ResultEvent extends java.util.EventObject {

	
	public String resultString;
	
    //here's the constructor
    public ResultEvent(Object source) {

        super(source);

    }
    
    

}


