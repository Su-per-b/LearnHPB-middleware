package com.sri.straylight.socketserver;


import com.sri.straylight.fmu.*;
import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.TypeConverter;
import com.thoughtworks.xstream.converters.enums.EnumConverter;


class HlTypeMapper extends DefaultTypeMapper {
	 
    HlTypeMapper() {
    	
    	CustomEnumConverter converter = new CustomEnumConverter();

    	addTypeConverter(StreamType.class, converter);
    	addTypeConverter(Enu.class, converter);
    	addTypeConverter(Elm.class, converter);

    }
}

