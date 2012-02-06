
import java.util.*;



public class FmuWrapper {

	public void run() {
		String retval = null;
		nativetest nt = new nativetest();
		retval = nt.sayHello("Beavis");
		
		System.out.println("Invocation returned " + retval);
		
	}

}
