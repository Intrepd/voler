package com.devineemail.voler.model;

import java.io.IOException;

import com.devineemail.voler.messages.AckMessage;
import com.devineemail.voler.messages.Message;
import com.devineemail.voler.messages.WriteTableMessage;
import com.devineemail.voler.messages.types.DeviceType;

public class ServerSAMModel extends AbstractModel
{
	private static volatile ServerSAMModel instance;

	public static ServerSAMModel getInstance()
	{
		if (instance == null)
		{
			synchronized (ServerSAMModel.class)
			{
				if (instance == null)
				{
					instance = new ServerSAMModel();
				}
			}
		}

		return instance;
	}

	private ServerSAMModel()
	{
	}

	@Override
	public void doRegister()
	{
		messageIO.addHandler(new SAMAckHandler());
	}

	@Override
	public void refreshModel() throws IOException
	{
	}

	private class SAMAckHandler extends MessageHandler
	{
		@Override
		public Class<? extends Message> getMessageClass()
		{
			return WriteTableMessage.class;
		}

		@Override
		public void handleMessage(final Message message)
		{
			if (message.getDestination() == DeviceType.SAM)
			{
				try
				{
					messageIO.sendMessage(new AckMessage(message.getSource(), DeviceType.SAM, true));
				}
				catch (final IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		}
	}
}
