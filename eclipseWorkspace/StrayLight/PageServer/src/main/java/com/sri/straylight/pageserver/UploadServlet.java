package com.sri.straylight.pageserver;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.io.output.*;



public class UploadServlet extends HttpServlet {
	
    private String greeting="Hello World";
    
    public UploadServlet(){}
    
    
    
    public UploadServlet(String greeting)
    {
        this.greeting=greeting;
    }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>"+greeting+"</h1>");
        
        
        response.getWriter().println("session=" + request.getSession(true).getId());
    }

}
