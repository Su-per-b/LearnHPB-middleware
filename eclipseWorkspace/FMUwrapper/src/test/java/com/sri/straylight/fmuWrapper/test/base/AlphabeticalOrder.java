package com.sri.straylight.fmuWrapper.test.base;

import java.util.Comparator;

import org.junit.runners.model.FrameworkMethod;

/*                                                                              
* Class for alphabetical ordering of a list                                   
*/
 
public class AlphabeticalOrder implements Comparator<Object>
{
	
public int compare (Object o1, Object o2)
{
	FrameworkMethod f1 = (FrameworkMethod)o1;
	FrameworkMethod f2 = (FrameworkMethod)o2;
 
return f1.getName().compareTo(f2.getName());
}
}
