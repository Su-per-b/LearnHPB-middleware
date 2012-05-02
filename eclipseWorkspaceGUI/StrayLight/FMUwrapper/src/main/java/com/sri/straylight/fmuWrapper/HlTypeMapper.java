package com.sri.straylight.fmuWrapper;


import com.sun.jna.DefaultTypeMapper;



class HlTypeMapper extends DefaultTypeMapper {
	 
    HlTypeMapper() {
    	
    	CustomEnumConverter converter = new CustomEnumConverter();

    	addTypeConverter(Enu.class, converter);
    	addTypeConverter(Elm.class, converter);
    	addTypeConverter(MessageType.class, converter);
    	addTypeConverter(State.class, converter);
    	
    }
}

