package com.devineemail.voler.messages;

import com.devineemail.voler.io.Frame;

public class ReadConsumablesStatusReply extends ReadTableMessageReply
{
	private final int filterLevel;
	private final int uvLevel;
	private final int humidifierLevel;
	private final boolean filterReminderEnabled;
	private final boolean uvReminderEnabled;
	private final boolean humidifierReminderEnabled;
	private final int ventLevel;
	private final boolean ventReminderEnabled;

	public ReadConsumablesStatusReply(final Frame frame)
	{
		super(frame);

		filterLevel = tableData[3] & 0xFF;
		uvLevel = tableData[4] & 0xFF;
		humidifierLevel = tableData[5] & 0xFF;
		filterReminderEnabled = tableData[6] == 0x01;
		uvReminderEnabled = tableData[7] == 0x01;
		humidifierReminderEnabled = tableData[8] == 0x01;
		ventLevel = tableData[9] & 0xFF;
		ventReminderEnabled = tableData[10] == 0x01;
	}

	public int getFilterLevel()
	{
		return filterLevel;
	}

	public int getUVLevel()
	{
		return uvLevel;
	}

	public int getHumidifierLevel()
	{
		return humidifierLevel;
	}

	public boolean isFilterReminderEnabled()
	{
		return filterReminderEnabled;
	}

	public boolean isUVReminderEnabled()
	{
		return uvReminderEnabled;
	}

	public boolean isHumidifierReminderEnabled()
	{
		return humidifierReminderEnabled;
	}

	public int getVentLevel()
	{
		return ventLevel;
	}

	public boolean isVentReminderEnabled()
	{
		return ventReminderEnabled;
	}
}
