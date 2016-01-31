package com.devineemail.voler.messages;

import java.io.IOException;

import com.devineemail.voler.io.Frame;
import com.devineemail.voler.messages.types.DeviceType;
import com.devineemail.voler.messages.types.FunctionType;

public class WriteTableMessage extends TableMessage
{
	public WriteTableMessage(Frame frame)
	{
		super(frame);
	}
	
	public WriteTableMessage(DeviceType destination, DeviceType source, int id, int row) throws IOException
	{
		super(destination, source, FunctionType.WRITE, id, row);
	}

	@Override
	protected byte[] marshalTableData()
	{
		return new byte[] {};
	}
}
