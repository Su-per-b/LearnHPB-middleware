package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.DefaultTypeMapper;



// TODO: Auto-generated Javadoc
/**
 * The Class EnumTypeMapper.
 */
public class EnumTypeMapper extends DefaultTypeMapper {
	 
    /**
     * Instantiates a new enum type mapper.
     */
    public EnumTypeMapper() {
    	
    	CustomEnumConverter converter = new CustomEnumConverter();

    	addTypeConverter(Enu.class, converter);
    	addTypeConverter(Elm.class, converter);
    	addTypeConverter(MessageType.class, converter);
    	addTypeConverter(SimStateNative.class, converter);
    	addTypeConverter(fmiStatus.class, converter);
    	
    }
}

