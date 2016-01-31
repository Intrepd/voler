package com.devineemail.voler.messages;

import com.devineemail.voler.io.Frame;

public class ReadFurnaceStatusReply extends ReadTableMessageReply
{
	private final int unknown;
	private final int blowerCFM;
	private final int blowerRPM;

	public ReadFurnaceStatusReply(final Frame frame)
	{
		super(frame);

		unknown = (tableData[2] & 0xFF) << 24 | (tableData[3] & 0xFF) << 16 | (tableData[4] & 0xFF) << 8 | tableData[5] & 0xFF;
		blowerCFM = (tableData[6] & 0xFF) << 8 | tableData[7] & 0xFF;
		blowerRPM = (tableData[8] & 0xFF) << 8 | tableData[9] & 0xFF;
	}

	public int getUnknown()
	{
		return unknown;
	}

	public int getBlowerCFM()
	{
		return blowerCFM;
	}

	public int getBlowerRPM()
	{
		return blowerRPM;
	}
}
