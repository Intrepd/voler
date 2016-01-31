package com.devineemail.voler.messages;

import java.util.Calendar;
import java.util.Date;

import com.devineemail.voler.io.Frame;
import com.devineemail.voler.messages.types.CallModeType;

public class ReadThermostatStatusReply extends ReadTableMessageReply
{
	private final int indoorTemp;
	private final int outdoorTemp;
	private final int humidity;
	private final CallModeType callMode;
	private final Date time;

	public ReadThermostatStatusReply(final Frame frame)
	{
		super(frame);

		indoorTemp = tableData[3] & 0xFF;
		humidity = tableData[11] & 0xFF;
		outdoorTemp = tableData[20] & 0xFF;
		callMode = CallModeType.fromValue(tableData[22] & 0xFF);

		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_WEEK, tableData[25] + 1);
		cal.add(Calendar.MINUTE, (tableData[26] & 0xFF) << 8 | tableData[27] & 0xFF);
		time = cal.getTime();
	}

	public int getIndoorTempurature()
	{
		return indoorTemp;
	}

	public int getOutdoorTemp()
	{
		return outdoorTemp;
	}

	public int getHumidity()
	{
		return humidity;
	}

	public CallModeType getCallMode()
	{
		return callMode;
	}

	public Date getTime()
	{
		return time;
	}
}
