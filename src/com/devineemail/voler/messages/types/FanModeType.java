package com.devineemail.voler.messages.types;

import java.util.HashMap;
import java.util.Map;

public enum FanModeType
{
	AUTO("Auto", 0),
	LOW("Low", 1),
	MEDIUM("Medium", 2),
	HIGH("High", 3),
	UNKNOWN("Unknown", 0xFF);

	private static final Map<Integer, FanModeType> lookup = new HashMap<Integer, FanModeType>();

	private int value;
	private final String readable;

	static
	{
		for (final FanModeType function : FanModeType.values())
		{
			lookup.put(function.getValue(), function);
		}
	}

	private FanModeType(String readable, int value)
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

	public static FanModeType fromValue(int value)
	{
		FanModeType function = lookup.get(value);

		return function != null ? function : UNKNOWN;
	}
}
