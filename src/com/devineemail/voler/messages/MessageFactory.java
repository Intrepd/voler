package com.devineemail.voler.messages;

import com.devineemail.voler.io.Frame;
import com.devineemail.voler.messages.types.DeviceType;
import com.devineemail.voler.messages.types.FunctionType;

public class MessageFactory
{
	public static Message create(final Frame frame)
	{
		Message message = null;
		final FunctionType function = FunctionType.fromValue(frame.getFunction());

		switch (function)
		{
			case READ:
				message = new ReadTableMessage(frame);
				break;
			case RESPONSE:
				message = handleResponse(frame);
				break;
			case WRITE:
				message = new WriteTableMessage(frame);
				break;
			case ERROR:
				break;
			default:
				break;
		}

		if (message == null)
		{
			message = new UnknownMessage(frame);
		}

		return message;
	}

	private static ReadTableMessageReply handleResponse(final Frame frame)
	{
		ReadTableMessageReply messageResponse = null;
		final DeviceType source = DeviceType.fromValue(frame.getSource());
		final byte[] data = frame.getPayload();

		if (data.length > 2)
		{
			final int id = (data[0] & 0xFF) << 8 | data[1] & 0xFF;
			final int row = data[2] & 0xFF;

			switch (source)
			{
				case AIR_CONDITIONER:
					if (id == 3 && row == 4)
					{
						messageResponse = new ReadAirConditionerStatusReply(frame);
					}
					break;
				case FURNACE:
					if (id == 4 && row == 4)
					{
						messageResponse = new ReadFurnaceStatusReply(frame);
					}
					break;
				case THERMOSTAT:
					if (id == 0x3B)
					{
						if (row == 2)
						{
							messageResponse = new ReadThermostatStatusReply(frame);
						}
						else if (row == 3)
						{
							messageResponse = new ReadThermostatSettingsReply(frame);
						}
						else if (row == 5)
						{
							messageResponse = new ReadConsumablesStatusReply(frame);
						}
					}
					break;
				default:
					break;
			}

			if (messageResponse == null)
			{
				messageResponse = new ReadTableMessageReply(frame);
			}
		}

		return messageResponse;
	}
}
