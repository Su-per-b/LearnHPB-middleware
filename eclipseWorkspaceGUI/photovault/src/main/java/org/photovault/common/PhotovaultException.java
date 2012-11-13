/*
 * PhotovaultException.java
 *
 * Created on September 9, 2006, 5:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.photovault.common;

/**
 *
 * @author harri
 */
public class PhotovaultException extends Exception {
    
    /** Creates a new instance of PhotovaultException */
    public PhotovaultException() {
        super();
    }
    
    public PhotovaultException( String message, Throwable cause ) {
        super( message, cause );
    }
    
    public PhotovaultException( String message ) {
        super( message );
    }
    
    public PhotovaultException( Throwable cause ) {
        super( cause );
    }
    
    
    
}
