package com.sri.straylight.socketserver;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
 
/** Simple example of native library declaration and usage. */
public class HelloWorld {
	
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary(
            (Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);
        void printf(String format, Object... args);
    }
 
    public void run() {
    	
        CLibrary.INSTANCE.printf("Hello, World\n");
      //  for (int i = 0; i < args.length; i++) {
          //  CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
       // }
    }
    public void run2() {
    	
    	System.setProperty("jna.library.path", "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug\\fmutest.dll");
    	
    	JNAwrapper lib  = (JNAwrapper) Native.loadLibrary("fmutest", JNAwrapper.class);
    	
    	
        //Add lib = Add.INSTANCE;
       // System.out.println(lib.add(10, 20));
    	
    	
    	//int foo(int bar);
    	//int a = myLib.INSTANCE(); 
    	
    }
    
}