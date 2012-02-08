public class Main 
{
    public static void main( String[] args )
    {

    	JNIinterface jniInterface = new JNIinterface();
    	String result = jniInterface.initAll();
    	boolean isSimulationComplete = jniInterface.isSimulationComplete();
    	
    	while(!jniInterface.isSimulationComplete()) {
    		
    		result = jniInterface.runStep();
    		double r = jniInterface.getResultSnapshot();
    		
    		String str = Double.toString(r);
    		System.out.println(str);
    	}
    	
    	jniInterface.simulateHelperCleanup();
    	
    }

    
}
