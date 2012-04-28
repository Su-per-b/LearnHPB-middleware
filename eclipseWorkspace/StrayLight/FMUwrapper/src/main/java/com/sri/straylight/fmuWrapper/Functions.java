package com.sri.straylight.fmuWrapper;


import com.sun.jna.Callback;
import com.sun.jna.Structure;


public class Functions extends Structure {
	
	  public static interface OpenFunc extends Callback {
	    int invoke(String name, int options);
	  }
	  
	  public static interface CloseFunc extends Callback {
	    int invoke(int fd);
	  }
	  
	  public OpenFunc open;
	  public CloseFunc close;
}
