package com.devineemail.voler.messages;

import java.io.IOException;

import com.devineemail.voler.messages.types.DeviceType;

public class ReadConsumablesStatus extends ReadTableMessage
{
	public ReadConsumablesStatus() throws IOException
	{
		super(DeviceType.THERMOSTAT, DeviceType.SAM, 0x3B, 0x05);
	}
}
