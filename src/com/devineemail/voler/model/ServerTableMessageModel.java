package com.devineemail.voler.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.devineemail.voler.io.MessageIO;
import com.devineemail.voler.messages.Message;
import com.devineemail.voler.messages.TableMessage;
import com.devineemail.voler.messages.types.FunctionType;

public class ServerTableMessageModel extends AbstractModel
{
	private static volatile ServerTableMessageModel instance;

	private final Map<String, TableMessage> tableMessageMap = new HashMap<>();

	public static ServerTableMessageModel getInstance()
	{
		if (instance == null)
		{
			synchronized (ServerTableMessageModel.class)
			{
				if (instance == null)
				{
					instance = new ServerTableMessageModel();
				}
			}
		}

		return instance;
	}

	private ServerTableMessageModel()
	{
	}

	@Override
	public void doRegister()
	{
		messageIO.addListener(new MessageIO.Listener()
		{
			@Override
			public void newMessage(final Message message)
			{
				if (message.getFunction() == FunctionType.RESPONSE && message instanceof TableMessage)
				{
					final TableMessage tableMessage = (TableMessage) message;
					final String key = tableMessage.getSource() + "-" + tableMessage.getId() + "-" + tableMessage.getRow();
					final TableMessage oldTableMessage = tableMessageMap.get(key);
					if (oldTableMessage == null)
					{
						tableMessageMap.put(key, tableMessage);
					}
					else
					{
						final byte[] oldPayload = oldTableMessage.getPayload();
						final byte[] newPayload = tableMessage.getPayload();
						if (!Arrays.equals(oldPayload, newPayload)) // && tableMessage.getId() == 4 && tableMessage.getRow() == 4)
						{
							tableMessageMap.put(key, tableMessage);
						}
					}
				}
			}
		});
	}

	@Override
	public void refreshModel() throws IOException
	{
	}
}
