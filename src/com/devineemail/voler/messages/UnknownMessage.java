package com.devineemail.voler.messages;

import com.devineemail.util.HexUtils;
import com.devineemail.voler.io.Frame;

public class UnknownMessage extends Message
{
	private final byte[] payload;
	private final int crc;
	
	public UnknownMessage(Frame frame)
	{
		super(frame);
		
		payload = frame.getPayload();
		crc = frame.getCRC();
	}

	@Override
	protected byte[] marshalPayload()
	{
		return payload;
	}

	public String toString()
	{
		return super.toString() + " Payload: " + HexUtils.toHexString(payload) + " CRC: " + HexUtils.toHexString(crc, 2);
	}
}
