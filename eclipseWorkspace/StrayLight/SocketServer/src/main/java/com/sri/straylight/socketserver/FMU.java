package com.sri.straylight.socketserver;


import com.sri.straylight.common.Unzip;
import java.io.*;

public class FMU {

	private String fmuFilePath_;
	private String fmuFileName_;
	
	private String unzipfolder_;
	private String dllPath_;
	private JNIinterface jniInterface;
	
	
	public FMU(String fmuFileName) {
		
		fmuFilePath_ = fmuFileName;
	}
	
	public void unzip() {
		
		String name = new File(fmuFilePath_).getName();
		
		
		name = name.substring(0, name.length()-4); 
		
		dllPath_ = unzipfolder_ + File.pathSeparator + 
				"binaries" + File.pathSeparator + "win32" + 
				File.pathSeparator + name + ".dll";
 				
		
		String unzipfolder_ = Main.config.unzipFolderBase + 
				File.pathSeparator + name  + "_unzipped";
		
		
		Unzip.unzip(fmuFilePath_, unzipfolder_);
		
	}

	public void load() {
		// TODO Auto-generated method stub
		//dllPath_ = unzipfolder_ + File.pathSeparator + 
				//"binaries" + File.pathSeparator + "win32";
		
		jniInterface.load(unzipfolder_);
	}
	
	
	
}
