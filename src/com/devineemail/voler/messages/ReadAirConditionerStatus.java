package com.devineemail.voler.messages;

import java.io.IOException;

import com.devineemail.voler.messages.types.DeviceType;

public class ReadAirConditionerStatus extends ReadTableMessage
{
	public ReadAirConditionerStatus() throws IOException
	{
		super(DeviceType.AIR_CONDITIONER, DeviceType.SAM, 0x03, 0x04);
	}
}
