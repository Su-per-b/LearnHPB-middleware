

public class nativetest
{
	static {
		System.loadLibrary("nativetest");
	}
	public native String sayHello(String s);
	

}