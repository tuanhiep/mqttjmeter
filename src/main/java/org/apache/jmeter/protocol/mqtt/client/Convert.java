package org.apache.jmeter.protocol.mqtt.client;

import java.nio.ByteBuffer;


public class Convert {

	public static byte [] long2ByteArray (long value)
	{
	    return ByteBuffer.allocate(8).putLong(value).array();
	}

	public static byte [] float2ByteArray (float value)
	{  
	     return ByteBuffer.allocate(4).putFloat(value).array();
	}
	public static byte[] int2ByteArray( int value ) {
		
		return ByteBuffer.allocate(4).putInt(value).array(); 
	    
	}
    public static byte[] double2ByteArray( double value ) {
		
		return ByteBuffer.allocate(8).putDouble(value).array(); 
	    
	}
    public static byte[] string2ByteArray( String s ) {
		
		return s.getBytes(); 
	    
	}
	
}
