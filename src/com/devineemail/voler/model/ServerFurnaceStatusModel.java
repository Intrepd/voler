package com.devineemail.voler.model;

import java.io.IOException;

import com.devineemail.voler.messages.Message;
import com.devineemail.voler.messages.ReadFurnaceStatus;
import com.devineemail.voler.messages.ReadFurnaceStatusReply;

public class ServerFurnaceStatusModel extends AbstractModel
{
	private static volatile ServerFurnaceStatusModel instance;

	private int unknown;
	private int blowerCFM;
	private int blowerRPM;

	public static ServerFurnaceStatusModel getInstance()
	{
		if (instance == null)
		{
			synchronized (ServerFurnaceStatusModel.class)
			{
				if (instance == null)
				{
					instance = new ServerFurnaceStatusModel();
				}
			}
		}

		return instance;
	}

	private ServerFurnaceStatusModel()
	{
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

	@Override
	public void doRegister()
	{
		messageIO.addHandler(new FurnaceStatusHandler());
	}

	@Override
	public void refreshModel() throws IOException
	{
		messageIO.sendMessage(new ReadFurnaceStatus());
	}

	private class FurnaceStatusHandler extends MessageHandler
	{
		@Override
		public Class<? extends Message> getMessageClass()
		{
			return ReadFurnaceStatusReply.class;
		}

		@Override
		public void handleMessage(final Message message)
		{
			final ReadFurnaceStatusReply replyMessage = (ReadFurnaceStatusReply) message;

			unknown = replyMessage.getUnknown();
			blowerCFM = replyMessage.getBlowerCFM();
			blowerRPM = replyMessage.getBlowerRPM();

			modelChanged();
		}
	}
}
