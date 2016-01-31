package com.devineemail.voler.messages;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.devineemail.io.CRC16OutputStream;
import com.devineemail.voler.io.Frame;
import com.devineemail.voler.messages.types.DeviceType;
import com.devineemail.voler.messages.types.FunctionType;

public abstract class Message
{
	protected final DeviceType destination;
	protected final DeviceType source;
	protected final FunctionType function;
	protected byte[] payload;

	public Message(final Frame frame)
	{
		destination = DeviceType.fromValue(frame.getDestination());
		source = DeviceType.fromValue(frame.getSource());
		function = FunctionType.fromValue(frame.getFunction());
		payload = frame.getPayload();
	}

	public Message(final DeviceType destination, final DeviceType source, final FunctionType function)
	{
		this.destination = destination;
		this.source = source;
		this.function = function;
	}

	public DeviceType getDestination()
	{
		return destination;
	}

	public DeviceType getSource()
	{
		return source;
	}

	public FunctionType getFunction()
	{
		return function;
	}

	public byte[] getPayload()
	{
		if (payload == null)
		{
			payload = marshalPayload();
		}

		return payload;
	}

	protected abstract byte[] marshalPayload();

	public byte[] marshal() throws IOException
	{
		final byte[] payload = getPayload();
		final ByteArrayOutputStream baos = new CRC16OutputStream(payload.length + 8);
		@SuppressWarnings("resource")
		final DataOutputStream dos = new DataOutputStream(baos);

		dos.writeShort(destination.getValue());
		dos.writeShort(source.getValue());
		dos.writeByte(payload.length);
		dos.writeShort(0);
		dos.writeByte(function.getValue());
		dos.write(payload);

		return baos.toByteArray();
	}

	@Override
	public String toString()
	{
		return "To: " + destination + " From: " + source + " Function: " + function;
	}
}
