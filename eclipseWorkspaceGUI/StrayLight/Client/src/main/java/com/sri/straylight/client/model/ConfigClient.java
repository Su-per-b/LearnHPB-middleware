package com.sri.straylight.client.model;

import com.sri.straylight.client.ConnectTo;

public class ConfigClient {

	public static final String VERSION = "0.3";
	public static final String WindowIcon = "blue_flame32.png";
	
	public static final String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
	public static final String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
	public static final String nativeLibFolder = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug";
	
	public ConnectTo connectTo = ConnectTo.connecTo_file;
	public String fmuFilePath = "";
		
}
