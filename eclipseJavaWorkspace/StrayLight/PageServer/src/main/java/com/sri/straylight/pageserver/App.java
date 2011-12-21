package com.sri.straylight.pageserver;




/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        Svr server = new Svr();
        
        server.showBanner();
        server.start();
        
    }
    
    

    
}
