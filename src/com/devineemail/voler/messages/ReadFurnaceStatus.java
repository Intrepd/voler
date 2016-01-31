package com.devineemail.voler.messages;

import java.io.IOException;

import com.devineemail.voler.messages.types.DeviceType;

public class ReadFurnaceStatus extends ReadTableMessage
{
	public ReadFurnaceStatus() throws IOException
	{
		super(DeviceType.FURNACE, DeviceType.SAM, 0x04, 0x04);
	}
}
