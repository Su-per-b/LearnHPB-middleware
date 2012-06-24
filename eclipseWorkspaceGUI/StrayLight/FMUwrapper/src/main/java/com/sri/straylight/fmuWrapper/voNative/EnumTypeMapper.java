package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.DefaultTypeMapper;



public class EnumTypeMapper extends DefaultTypeMapper {
	 
    public EnumTypeMapper() {
    	
    	CustomEnumConverter converter = new CustomEnumConverter();

    	addTypeConverter(Enu.class, converter);
    	addTypeConverter(Elm.class, converter);
    	addTypeConverter(MessageType.class, converter);
    	addTypeConverter(SimStateNative.class, converter);
    	addTypeConverter(fmiStatus.class, converter);
    	
    }
}

