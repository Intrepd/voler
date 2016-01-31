package com.devineemail.voler.model;

import java.io.IOException;
import java.util.Date;

import com.devineemail.voler.messages.Message;
import com.devineemail.voler.messages.ReadThermostatSettings;
import com.devineemail.voler.messages.ReadThermostatSettingsReply;
import com.devineemail.voler.messages.ReadThermostatStatus;
import com.devineemail.voler.messages.ReadThermostatStatusReply;
import com.devineemail.voler.messages.types.CallModeType;
import com.devineemail.voler.messages.types.FanModeType;

public class ServerThermostatModel extends AbstractModel
{
	private static volatile ServerThermostatModel instance;

	private CallModeType callMode;
	private Date time;
	private FanModeType fanMode;
	private int coolSetPoint;
	private int heatSetPoint;
	private int holdDuration;
	private int humidity;
	private int indoorTemp;
	private int outdoorTemp;
	private boolean onHold;

	public static ServerThermostatModel getInstance()
	{
		if (instance == null)
		{
			synchronized (ServerThermostatModel.class)
			{
				if (instance == null)
				{
					instance = new ServerThermostatModel();
				}
			}
		}

		return instance;
	}

	private ServerThermostatModel()
	{
	}

	public int getIndoorTemp()
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

	public FanModeType getFanMode()
	{
		return fanMode;
	}

	public int getCoolSetPoint()
	{
		return coolSetPoint;
	}

	public int getHeatSetPoint()
	{
		return heatSetPoint;
	}

	public int getHoldDuration()
	{
		return holdDuration;
	}

	public boolean isOnHold()
	{
		return onHold;
	}

	@Override
	public void doRegister()
	{
		messageIO.addHandler(new ThermostatStatusHandler());
		messageIO.addHandler(new ThermostatSettingsHandler());
	}

	@Override
	public void refreshModel() throws IOException
	{
		messageIO.sendMessage(new ReadThermostatStatus());
		messageIO.sendMessage(new ReadThermostatSettings());
	}

	private class ThermostatStatusHandler extends MessageHandler
	{
		@Override
		public Class<? extends Message> getMessageClass()
		{
			return ReadThermostatStatusReply.class;
		}

		@Override
		public void handleMessage(final Message message)
		{
			final ReadThermostatStatusReply replyMessage = (ReadThermostatStatusReply) message;

			indoorTemp = replyMessage.getIndoorTempurature();
			outdoorTemp = replyMessage.getOutdoorTemp();
			humidity = replyMessage.getHumidity();
			callMode = replyMessage.getCallMode();
			time = replyMessage.getTime();
		}
	}

	private class ThermostatSettingsHandler extends MessageHandler
	{
		@Override
		public Class<? extends Message> getMessageClass()
		{
			return ReadThermostatSettingsReply.class;
		}

		@Override
		public void handleMessage(final Message message)
		{
			final ReadThermostatSettingsReply replyMessage = (ReadThermostatSettingsReply) message;

			fanMode = replyMessage.getFanMode();
			coolSetPoint = replyMessage.getCoolSetPoint();
			heatSetPoint = replyMessage.getHeatSetPoint();
			holdDuration = replyMessage.getHoldDuration();
			onHold = replyMessage.getZoneHoldBits() == 1;

			modelChanged();
		}
	}
}
