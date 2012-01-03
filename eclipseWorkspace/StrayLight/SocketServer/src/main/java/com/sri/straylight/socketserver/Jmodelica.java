package com.sri.straylight.socketserver;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Jmodelica {
	
	public void test() {

		

		this.listEngines();
		
        ScriptEngineManager mgr = new ScriptEngineManager();
        
        List<ScriptEngineFactory> factoryList = mgr.getEngineFactories();
				
        
        ScriptEngine pyEngine = mgr.getEngineByName("jython");
        
        try {
            pyEngine.eval("print \"Python - Hello, world!\"");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        

	}
	
    public static void listEngines(){
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
