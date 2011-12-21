package com.sri.straylight.pageserver;

import java.net.URL;
import java.io.File;
import java.io.FileReader;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;


public class Svr {

	private static final String bannerPath = "banner.txt";
	private static final String webRoot = "webroot/";
	private static final String defaultFile = "index.html";
	public static Logger logger = Log.getLogger("PageServer");
	
	
	public void showBanner() {

		logger.info( "Straylight PageServer Starting" );

        URL bannerUrl = this.getClass().getClassLoader().getResource(bannerPath);
        String aciiArt = fileContentsToString(bannerUrl.getFile());
      
        System.out.println( aciiArt );
	}
	
	public void start() {

		try {

	        URL webRootUrl = this.getClass().getClassLoader().getResource(webRoot);
	        String webRootString = webRootUrl.getFile();
			
			Server server = new Server(80);
			
	        ResourceHandler resource_handler = new ResourceHandler();
	        resource_handler.setDirectoriesListed(true);
	        resource_handler.setWelcomeFiles(new String[]{ defaultFile });
	        
	        resource_handler.setResourceBase(webRootString);
	        	
	        logger.info("Webroot is: " + resource_handler.getBaseResource());
	        
	        HandlerList handlers = new HandlerList();
	        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
	        server.setHandler(handlers);

	        server.start();
	        server.join();
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
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


