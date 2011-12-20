package com.sri.simsvr;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.util.log.Log;

/**
 * Hello world!
 *
 */
public class App2 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World App2!!" );
		
        try {

			Server server = new Server(80);
			

			
		} catch (Throwable e) {
			e.printStackTrace();
		}
        
    }
}
