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
import java.io.*;
import javax.servlet.*;
import java.util.*;



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
        
        
        PrintWriter out = response.getWriter();
        out.println("<h1>upload "+greeting+"</h1>");
        
        
        out.println("session=" + request.getSession(true).getId());
        
        File file ;
        int maxFileSize = 5000 * 1024;
        int maxMemSize = 5000 * 1024;
        ServletContext context = this.getServletContext();
        
        
        String filePath = context.getInitParameter("file-upload");

        // Verify the content type
        String contentType = request.getContentType();
        
        if ((contentType.indexOf("multipart/form-data") >= 0)) {

           DiskFileItemFactory factory = new DiskFileItemFactory();
           // maximum size that will be stored in memory
           factory.setSizeThreshold(maxMemSize);
           // Location to save data that is larger than maxMemSize.
           factory.setRepository(new File("c:\\temp"));

           // Create a new file upload handler
           ServletFileUpload upload = new ServletFileUpload(factory);
           // maximum file size to be uploaded.
           upload.setSizeMax( maxFileSize );
           try{ 
              // Parse the request to get file items.
              List fileItems = upload.parseRequest(request);

              // Process the uploaded file items
              Iterator i = fileItems.iterator();

              out.println("<html>");
              out.println("<head>");
              out.println("<title>JSP File upload</title>");  
              out.println("</head>");
              out.println("<body>");
              while ( i.hasNext () ) 
              {
                 FileItem fi = (FileItem)i.next();
                 if ( !fi.isFormField () )	
                 {
                 // Get the uploaded file parameters
                 String fieldName = fi.getFieldName();
                 String fileName = fi.getName();
                 boolean isInMemory = fi.isInMemory();
                 long sizeInBytes = fi.getSize();
                 // Write the file
                 if( fileName.lastIndexOf("\\") >= 0 ){
                 file = new File( filePath + 
                 fileName.substring( fileName.lastIndexOf("\\"))) ;
                 }else{
                 file = new File( filePath + 
                 fileName.substring(fileName.lastIndexOf("\\")+1)) ;
                 }
                 fi.write( file ) ;
                 out.println("Uploaded Filename: " + filePath + 
                 fileName + "<br>");
                 }
              }
              out.println("</body>");
              out.println("</html>");
           }catch(Exception ex) {
              System.out.println(ex);
           }
        }else{
           out.println("<html>");
           out.println("<head>");
           out.println("<title>Servlet upload</title>");  
           out.println("</head>");
           out.println("<body>");
           out.println("<p>No file uploaded</p>"); 
           out.println("</body>");
           out.println("</html>");
        }
        
    }

}
