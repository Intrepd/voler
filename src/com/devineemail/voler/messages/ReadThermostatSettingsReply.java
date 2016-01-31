package com.devineemail.voler.messages;

import com.devineemail.voler.io.Frame;
import com.devineemail.voler.messages.types.FanModeType;

public class ReadThermostatSettingsReply extends ReadTableMessageReply
{
	private FanModeType fanMode;
	private int zoneHoldBits;
	private int heatSetPoint;
	private int coolSetPoint;
	private int holdDuration;
	
	public ReadThermostatSettingsReply( Frame frame )
	{
		super(frame);
		
		fanMode = FanModeType.fromValue( tableData[3] & 0xFF );
		zoneHoldBits = tableData[11] & 0xFF;
		heatSetPoint = tableData[12] & 0xFF;
		coolSetPoint = tableData[20] & 0xFF;
		holdDuration = (tableData[38] & 0xFF) << 8 | tableData[39] & 0xFF;
	}
		
	public FanModeType getFanMode()
	{
		return fanMode;
	}

	public int getZoneHoldBits()
	{
		return zoneHoldBits;
	}

	public int getHeatSetPoint()
	{
		return heatSetPoint;
	}

	public int getCoolSetPoint()
	{
		return coolSetPoint;
	}

	public int getHoldDuration()
	{
		return holdDuration;
	}

	public String toString()
	{
		return "Fan Mode: " + getFanMode() + " Zone Hold: " + getZoneHoldBits() + " Heat at: " + getHeatSetPoint() + " Cool at: " + getCoolSetPoint() + " Hold Duration: " + getHoldDuration();
	}
}
