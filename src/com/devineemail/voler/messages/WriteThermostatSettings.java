package com.devineemail.voler.messages;

import java.io.IOException;

import com.devineemail.util.HexUtils;
import com.devineemail.voler.messages.types.DeviceType;

public class WriteThermostatSettings extends WriteTableMessage
{
	public WriteThermostatSettings() throws IOException
	{
		super(DeviceType.THERMOSTAT, DeviceType.SAM, 0x3B, 0x03);
	}

	@Override
	protected byte[] marshalTableData()
	{
		final byte[] buffer = HexUtils.fromHexString(
				"00000F0000000000000000003C000000000000004E000000000000002D2D2D2D2D2D2D2D0000000000000000000000000000000000005A4F4E4520312020202020005A4F4E4520322020202020005A4F4E4520332020202020005A4F4E4520342020202020005A4F4E4520352020202020005A4F4E4520362020202020005A4F4E4520372020202020005A4F4E452038202020202000");

		return buffer;
	}
}
