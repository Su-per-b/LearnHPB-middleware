package com.sri.straylight.socketserver;


import com.sri.straylight.common.Unzip;
import java.io.*;

public class FMU {

	private String fmuFilePath_;
	private String fmuFileName_;
	
	private String unzipfolder_;
	private String dllPath_;
	private JNIinterface jniInterface_;
	
	
	public FMU(String fmuFileName) {
		
		fmuFilePath_ = fmuFileName;
		jniInterface_ = new JNIinterface();
	}
	
	public void unzip() {
		
		String name = new File(fmuFilePath_).getName();
		
		
		name = name.substring(0, name.length()-4); 
		
		//dllPath_ = unzipfolder_ + File.separator   + 
			//	"binaries" + File.separator + "win32" + 
			//	File.separator + name + ".dll";
 				
		
		unzipfolder_ = Main.config.unzipFolderBase + File.separator + name;
		
		
		Unzip.unzip(fmuFilePath_, unzipfolder_);
		
	}

	public void load() {
		// TODO Auto-generated method stub
		//dllPath_ = unzipfolder_ + File.pathSeparator + 
				//"binaries" + File.pathSeparator + "win32";
		
		String res = jniInterface_.load(unzipfolder_);
		
		
	}
	
	
	
}
