package com.sri.straylight.socketserver;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface FMUwrapper extends Library
{
   // Add INSTANCE = (Add) Native.loadLibrary("add", Add.class);
   // int add(int x, int y);
	//FMUwrapper INSTANCE = (FMUwrapper) Native.loadLibrary("add", FMUwrapper.class);
	void testFMU();
	
}
