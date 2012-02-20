package com.sri.straylight.webapp;

import java.io.File;
import java.util.Comparator;


public class FileComparator implements Comparator<File>
{
	
	//sorts by the most recent first
    public int compare(File f1, File f2)
    {
    	
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        
       // return f1.lastModified().compareTo(f2.lastModified());
    	long mod1 =  f1.lastModified();
    	long mod2 =  f2.lastModified();
    	
    	if (mod1 == mod2) {
    		return EQUAL;
    	} else if (mod1 > mod2) {
    		return BEFORE;
    	}  else {
    		return AFTER;
    	}

    	
    	
    }
}
