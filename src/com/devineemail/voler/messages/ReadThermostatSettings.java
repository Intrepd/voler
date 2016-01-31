package com.devineemail.voler.messages;

import java.io.IOException;

import com.devineemail.voler.messages.types.DeviceType;

public class ReadThermostatSettings extends ReadTableMessage
{
	public ReadThermostatSettings() throws IOException
	{
		super(DeviceType.THERMOSTAT, DeviceType.SAM, 0x3B, 0x03);
	}
}
