package com.sri.straylight.common;


import java.io.File;
import java.io.FileReader;
import java.net.URL;

import org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.util.Properties;

public class Banner  {
	
	private Logger logger_;
	private URL propertiesUrl_;
			
	private static final String bannerPath = "banner.txt";
	
	public Banner(Logger logger, String moduleName, URL url) {
		
		this.logger_ = logger;
		this.propertiesUrl_ = url;

	}
	
	
	public void show() {
		
		Properties properties = new java.util.Properties();
		
		try {
			properties.load(this.propertiesUrl_.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String version = properties.getProperty( "project.version" ); 
		String title = properties.getProperty( "project.title" ); 
		
		this.logger_.info(  title + " Starting" );
		
		this.logger_.info( "os.name: " + System.getProperty("os.name") );
		this.logger_.info( "os.version: " + System.getProperty("os.version") );
		this.logger_.info( "os.arch: " + System.getProperty("os.arch") );
		this.logger_.info( "java.home: " + System.getProperty("java.home") );
		this.logger_.info( "java.version: " + System.getProperty("java.version") );
		
        URL bannerUrl = this.getClass().getClassLoader().getResource(bannerPath);
        String aciiArt = fileContentsToString(bannerUrl.getFile());
        System.out.println( aciiArt );
        System.out.println( title + " v" + version );
        System.out.println( "by Raj Dye" );
        System.out.println( "Copyright (c) 2011 SRI International" );
        
	}
	

	
	
	
	  /**
	   * Read the contents of a file and place them in
	   * a string object.
	   *
	   * @param file path to file.
	   * @return String contents of the file.
	   */
	  public static String fileContentsToString(String file)
	  {
	      String contents = "";

	      File f = null;
	      try
	      {
	          f = new File(file);

	          if (f.exists())
	          {
	              FileReader fr = null;
	              try
	              {
	                  fr = new FileReader(f);
	                  char[] template = new char[(int) f.length()];
	                  fr.read(template);
	                  contents = new String(template);
	              }
	              catch (Exception e)
	              {
	                  e.printStackTrace();
	              }
	              finally
	              {
	                  if (fr != null)
	                  {
	                      fr.close();
	                  }
	              }
	          }
	      }
	      catch (Exception e)
	      {
	          e.printStackTrace();
	      }
	      return contents;
	  }
}
