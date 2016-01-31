package com.devineemail.voler.io;

import com.devineemail.util.CRC16;
import com.devineemail.util.HexUtils;

public class Frame
{
	private final int destination;
	private final int source;
	private final int function;
	private final byte[] payload;
	private final int crc;
	private final boolean correctCRC;

	public Frame(final byte[] buffer)
	{
		this( buffer, 0, buffer.length);
	}
	
	public Frame(final byte[] buffer, int offset, int length)
	{
		destination = ((buffer[offset] & 0xFF) << 8) | (buffer[offset+1] & 0xFF);
		source = ((buffer[offset+2] & 0xFF) << 8) | (buffer[offset+3] & 0xFF);
		payload = new byte[buffer[offset+4] & 0xFF];
		// 5-6 Reserved?
		function = buffer[offset+7] & 0xFF;
		System.arraycopy(buffer, offset+8, payload, 0, payload.length);
		
		crc = buffer[offset+ 8 + payload.length] & 0xFF | ((buffer[offset + 9 + payload.length] & 0xFF) << 8);
		correctCRC = crc == CRC16.calculateCRC(buffer, offset, length - 2);
	}
	
	public int getDestination()
	{
		return destination;
	}
	
	public int getSource()
	{
		return source;
	}
	
	public int getFunction()
	{
		return function;
	}
	
	public byte[] getPayload()
	{
		return payload;
	}
	
	public int getCRC()
	{
		return crc;
	}
	
	public boolean isCRCCorrect()
	{
		return correctCRC;
	}
	
	public String toString()
	{
		return "To: " + HexUtils.toHexString(getDestination(), 4) + " From: " + HexUtils.toHexString(getSource(), 4) + " Function: " + HexUtils.toHexString(getFunction(), 2) + " Payload: " + HexUtils.toHexString(getPayload()) + " CRC: " + HexUtils.toHexString(crc,4);
	}
}
