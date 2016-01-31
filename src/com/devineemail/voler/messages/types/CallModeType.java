package com.devineemail.voler.messages.types;

import java.util.HashMap;
import java.util.Map;

public enum CallModeType
{
	HEAT("Heat", 0),
	COOL("Cool", 1),
	AUTO("Automatic", 2), // Just a guess
	OFF("Off", 5),
	UNKNOWN("Unknown", 0xFF);

	private static final Map<Integer, CallModeType> lookup = new HashMap<Integer, CallModeType>();

	private int value;
	private final String readable;

	static
	{
		for (final CallModeType function : CallModeType.values())
		{
			lookup.put(function.getValue(), function);
		}
	}

	private CallModeType(String readable, int value)
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

	public static CallModeType fromValue(int value)
	{
		CallModeType function = lookup.get(value >> 8);

		return function != null ? function : UNKNOWN;
	}
}
