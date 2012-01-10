package com.sri.straylight.socketserver;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.python.util.*;
import org.python.core.*;

public class Jmodelica {
	private static final String fmuFile = "testFMI.fmu";
	
	
	public void test() {

		

		this.listEngines();
		
		PythonInterpreter interp;
		
        ScriptEngineManager mgr = new ScriptEngineManager();
        
        List<ScriptEngineFactory> factoryList = mgr.getEngineFactories();
        Properties newProps = new Properties();
        
    	String pythonPath = "C:\\ProgramFiles\\jmodelicatest\\Python;C:\\ProgramFiles\\jmodelicatest\\CasADi";
    	newProps.setProperty("python.path", pythonPath);
    	
    	String pythonHome = "C:\\python\\Python2.7";
    	newProps.setProperty("python.home", pythonPath);
    	
    	
    	newProps.setProperty("jmodelica.home", "C:\\ProgramFiles\\jmodelicatest");
    	//JMODELICA_HOME
    	
    	
    	Properties properties = System.getProperties();
    	
    	PythonInterpreter.initialize(properties, newProps,
    	                             new String[] {""});
    	
    	
        ScriptEngine pyEngine = mgr.getEngineByName("python");
        
        try {
            pyEngine.eval("print \"Python - Hello, world!\"");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //return;
        		
   
        URL fileUrl = this.getClass().getClassLoader().getResource(fmuFile);
        String code = "myModel = FMUModel('" + fileUrl.getPath() + "')";
        
        try {

        	
        	pyEngine.eval("import os");
        	
        	pyEngine.eval("os.putenv('JMODELICA_HOME', 'C:\\ProgramFiles\\jmodelicatest')");
        	pyEngine.eval("os.putenv('IPOPT_HOME', 'C:\\ProgramFiles\\jmodelicatest\\Ipopt-MUMPS')");
        	pyEngine.eval("os.putenv('SUNDIALS_HOME', 'C:\\ProgramFiles\\jmodelicatest\\SUNDIALS')");
        	pyEngine.eval("os.putenv('CPPAD_HOME', 'C:\\ProgramFiles\\jmodelicatest\\CppAD')");
        	pyEngine.eval("os.putenv('MINGW_HOME', 'C:\\ProgramFiles\\jmodelicatest\\mingw')");


        	
        	//pyEngine.eval("import jpype");
        	 
            pyEngine.eval("from jmodelica.fmi import FMUModel");
            
            
           // pyEngine.eval(code);
            //pyEngine.eval("myModel.simulate()");
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 		/**/

        
        
        
        
        
	}
	
	
	public void run() {
		//URL fileUrl = this.getClass().getClassLoader().getResource(fmuFile);
		
		
	}
	
    private static void listEngines(){
        ScriptEngineManager mgr = new ScriptEngineManager();
        List<ScriptEngineFactory> factories =
                mgr.getEngineFactories();
        for (ScriptEngineFactory factory: factories) {
            System.out.println("ScriptEngineFactory Info");
            String engName = factory.getEngineName();
            String engVersion = factory.getEngineVersion();
            String langName = factory.getLanguageName();
            String langVersion = factory.getLanguageVersion();
            System.out.printf("\tScript Engine: %s (%s)\n",
                    engName, engVersion);
            List<String> engNames = factory.getNames();
            for(String name: engNames) {
                System.out.printf("\tEngine Alias: %s\n", name);
            }
            System.out.printf("\tLanguage: %s (%s)\n",
                    langName, langVersion);
        }
    }


}
