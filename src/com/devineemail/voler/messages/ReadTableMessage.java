package com.devineemail.voler.messages;

import java.io.IOException;

import com.devineemail.voler.io.Frame;
import com.devineemail.voler.messages.types.DeviceType;
import com.devineemail.voler.messages.types.FunctionType;

public class ReadTableMessage extends TableMessage
{
	public ReadTableMessage(Frame frame)
	{
		super(frame);
	}

	public ReadTableMessage(DeviceType destination, DeviceType source, int id, int row) throws IOException
	{
		super(destination, source, FunctionType.READ, id, row);
	}

	protected byte[] marshalTableData()
	{
		return new byte[] {};
	}
}
