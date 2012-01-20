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

import com.sri.straylight.common.Banner;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class PageServer {


	private static final String webRoot = "webroot/";
	private static final String defaultFile = "index.html";
	public static Logger logger = Log.getLogger("PageServer");
	
	
	public void showBanner() {

        
		java.net.URL url = PageServer.class.getClassLoader().getResource("PageServer.properties");
		
		Banner banner = new Banner(logger, "Page Server!", url);
		banner.show();
		
		
	}
	
	public void start() {

		try {

	        URL webRootUrl = this.getClass().getClassLoader().getResource(webRoot);
	        String webRootString = webRootUrl.getFile();
			
			Server server = new Server(80);
			
	        ResourceHandler staticResourceHandler  = new ResourceHandler();
	        staticResourceHandler.setDirectoriesListed(true);
	        staticResourceHandler.setWelcomeFiles(new String[]{ defaultFile });   
	        staticResourceHandler.setResourceBase(webRootString);
	     
	       // ContextHandler staticContextHandler = new ContextHandler();
	      //  staticContextHandler.setContextPath(webRootString);       
	       // staticContextHandler.setHandler(staticResourceHandler); 
	        
	        // Create servlet context handler for main servlet.
	        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        context.setContextPath("/upload");
	        server.setHandler(context);
	 
	        context.addServlet(new ServletHolder(new HelloServlet()),"/*");
	        context.addServlet(new ServletHolder(new HelloServlet("Buongiorno Mondo")),"/it/*");
	       // context.addServlet(new ServletHolder(new HelloServlet("Bonjour le Monde")),"/fr/*");
	        
	      //  ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
	      //  servletContextHandler.setContextPath("/");       
	       // servletContextHandler.addServlet(new ServletHolder(new HelloServlet()),"/");

	        
	        
	       // logger.info("Webroot is: " + staticResourceHandler .getBaseResource());
	        
	        HandlerList handlers = new HandlerList();
	        handlers.setHandlers(new Handler[] { staticResourceHandler ,  context });
	        
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


