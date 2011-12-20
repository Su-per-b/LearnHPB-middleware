package com.sri.torqueflite;

import java.net.URL;
import java.io.File;
import java.io.FileReader;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;



public class SocketServer {

	private static final String bannerPath = "banner.txt";
	public static Logger logger = Log.getLogger("SocketServer");
	
	
	public void showBanner() {

		logger.info( "Torqueflite SocketServer Starting" );

        URL bannerUrl = this.getClass().getClassLoader().getResource(bannerPath);
        String aciiArt = fileContentsToString(bannerUrl.getFile());
      
        System.out.println( aciiArt );
	}
	
	public void start() {

		try {
			// 1) Create a Jetty server with the 8080 port.
			Server server = new Server(8080);
			
			// 2) Register ChatWebSocketHandler in the Jetty server instance.
			SocketHandler theHandler = new SocketHandler();
			theHandler.setHandler(new DefaultHandler());
			server.setHandler(theHandler);
			
			// 2) Start the Jetty server.
			server.start();
			// Jetty server is stopped when the Thread is interruped.
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
