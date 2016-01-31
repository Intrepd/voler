package com.devineemail.voler.messages;

import com.devineemail.voler.io.Frame;

public class ReadAirConditionerStatusReply extends ReadTableMessageReply
{
	private final int lineVoltage;

	public ReadAirConditionerStatusReply(final Frame frame)
	{
		super(frame);

		lineVoltage = (tableData[6] & 0xFF) << 8 | tableData[7] & 0xFF;
	}

	public int getLineVoltage()
	{
		return lineVoltage;
	}
}
