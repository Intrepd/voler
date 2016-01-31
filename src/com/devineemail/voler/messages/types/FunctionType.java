package com.devineemail.voler.messages.types;

import java.util.HashMap;
import java.util.Map;

public enum FunctionType
{
	RESPONSE("Response", 0x06),
	READ("Read", 0x0B),
	WRITE("Write", 0x0C),
	ERROR("Error", 0x15),
	UNKNOWN("Unknown", 0xFF);

	private static final Map<Integer, FunctionType> lookup = new HashMap<Integer, FunctionType>();

	private int value;
	private final String readable;

	static
	{
		for (final FunctionType function : FunctionType.values())
		{
			lookup.put(function.getValue(), function);
		}
	}

	private FunctionType(String readable, int value)
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

	public static FunctionType fromValue(int value)
	{
		FunctionType function = lookup.get(value);

		return function != null ? function : UNKNOWN;
	}
}
