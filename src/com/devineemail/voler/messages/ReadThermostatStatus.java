package com.devineemail.voler.messages;

import java.io.IOException;

import com.devineemail.voler.messages.types.DeviceType;

public class ReadThermostatStatus extends ReadTableMessage
{
	public ReadThermostatStatus() throws IOException
	{
		super(DeviceType.THERMOSTAT, DeviceType.SAM, 0x3B, 0x02 );
	}
}
