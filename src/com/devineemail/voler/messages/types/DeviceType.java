package com.devineemail.voler.messages.types;

import java.util.HashMap;
import java.util.Map;

public enum DeviceType
{
	SYS_INIT("System Init", 0x1F01),
	THERMOSTAT("Master Thermostat", 0x2001),
	FURNACE("Furnace", 0x4001),
	HUMIDIFIER("Humidifier", 0x4101), // Just a guess
	FAN_COIL("Fan Coil", 0x4201),
	AIR_CONDITIONER("Air Conditioner", 0x5001),
	HEAT_PUMP("Heat Pump", 0x5101),
	INFINITY_DAMPER("Infinity Damper", 0x6001),
	DAMPER("Damper", 0x6101),
	SAM("System Access Module", 0x9201),
	UNKNOWN("Unknown", 0xFFFF);

	private static final Map<Integer, DeviceType> lookup = new HashMap<Integer, DeviceType>();

	private int value;
	private final String readable;

	static
	{
		for (final DeviceType function : DeviceType.values())
		{
			lookup.put(function.getValue(), function);
		}
	}

	private DeviceType(final String readable, final int value)
	{
		this.readable = readable;
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public String getReadable()
	{
		return readable;
	}

	public static DeviceType fromValue(final int value)
	{
		final DeviceType function = lookup.get(value);

		return function != null ? function : UNKNOWN;
	}
}
