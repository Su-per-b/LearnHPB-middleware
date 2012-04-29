package com.sri.straylight.fmuWrapper;


import com.sun.jna.DefaultTypeMapper;



class HlTypeMapper extends DefaultTypeMapper {
	 
    HlTypeMapper() {
    	
    	CustomEnumConverter converter = new CustomEnumConverter();

    	addTypeConverter(StreamType.class, converter);
    	addTypeConverter(Enu.class, converter);
    	addTypeConverter(Elm.class, converter);
    	addTypeConverter(MessageType.class, converter);
    	
    	
    }
}

