package com.devineemail.voler.model;

import java.io.IOException;

import com.devineemail.voler.messages.Message;
import com.devineemail.voler.messages.ReadAirConditionerStatusReply;
import com.devineemail.voler.messages.ReadFurnaceStatus;

public class ServerAirConditionerStatusModel extends AbstractModel
{
	private static volatile ServerAirConditionerStatusModel instance;

	private int lineVoltage;

	public static ServerAirConditionerStatusModel getInstance()
	{
		if (instance == null)
		{
			synchronized (ServerAirConditionerStatusModel.class)
			{
				if (instance == null)
				{
					instance = new ServerAirConditionerStatusModel();
				}
			}
		}

		return instance;
	}

	private ServerAirConditionerStatusModel()
	{
	}

	public int getLineVoltage()
	{
		return lineVoltage;
	}

	@Override
	public void doRegister()
	{
		messageIO.addHandler(new AirConditionerStatusHandler());
	}

	@Override
	public void refreshModel() throws IOException
	{
		messageIO.sendMessage(new ReadFurnaceStatus());
	}

	private class AirConditionerStatusHandler extends MessageHandler
	{
		@Override
		public Class<? extends Message> getMessageClass()
		{
			return ReadAirConditionerStatusReply.class;
		}

		@Override
		public void handleMessage(final Message message)
		{
			final ReadAirConditionerStatusReply replyMessage = (ReadAirConditionerStatusReply) message;

			lineVoltage = replyMessage.getLineVoltage();

			modelChanged();
		}
	}
}
