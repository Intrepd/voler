package com.devineemail.voler.messages;

import com.devineemail.voler.messages.types.DeviceType;
import com.devineemail.voler.messages.types.FunctionType;

public class AckMessage extends Message
{
	private final boolean success;

	public AckMessage(final DeviceType destination, final DeviceType source, final boolean success)
	{
		super(destination, source, FunctionType.RESPONSE);

		this.success = success;
	}

	@Override
	protected byte[] marshalPayload()
	{
		return new byte[] { (byte) (success ? 0 : 1) };
	}
}
