package com.sri.straylight.socketserver;



import org.eclipse.jetty.util.log.Log;


import com.sri.straylight.fmu.JnaEnum;
import com.sri.straylight.fmu.ScalarVariableMeta3;
import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;


class CustomConvertersvm3 implements TypeConverter {
	 
	private static final Logger logger = Log.getLogger(CustomEnumConverter.class);
	
    //private static final Logger logger = LoggerFactory.getLogger(EnumConverter.class);
 
    public Object fromNative(Object input, FromNativeContext context) {
    	
    	ScalarVariableMeta3 svm = (ScalarVariableMeta3) input;
    	
        Class targetClass = context.getTargetType();
        if (!ScalarVariableMeta3.class.isAssignableFrom(targetClass)) {
            return null;
        }
        
        Object[] enums = targetClass.getEnumConstants();
        
        if (enums.length == 0) {
           // logger.error("Could not convert desired enum type (), no valid values are defined.",targetClass.getName());
            return null;
        }
        

        return svm;
 
    }
 
    public Object toNative(Object input, ToNativeContext context) {
    	ScalarVariableMeta3 svm = (ScalarVariableMeta3) input;
        return svm;
    }
 
    public Class nativeType() {
        return Integer.class;
    }
}