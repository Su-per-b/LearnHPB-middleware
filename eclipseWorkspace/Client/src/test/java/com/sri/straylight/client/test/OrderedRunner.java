package com.sri.straylight.client.test;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;



import java.util.*;




/*                                                                             
 * Class for running in alphabetical order                                      
 */
 
public class OrderedRunner extends BlockJUnit4ClassRunner
{
    /*                                                                         
     * default initializer                                                     
     */
    public OrderedRunner(Class<?> klass) throws InitializationError
    {
        super (klass);
    }
    
    
    @Override
    protected  List<FrameworkMethod> computeTestMethods()
    {
    List<FrameworkMethod> lst = super.computeTestMethods();
    List<FrameworkMethod> cpy = new ArrayList<FrameworkMethod> (lst);
     
    Collections.sort (cpy, new AlphabeticalOrder());
     
    return cpy;
    }
	    
	    
}
