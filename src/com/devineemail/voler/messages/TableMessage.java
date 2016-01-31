package com.devineemail.voler.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.devineemail.util.HexUtils;
import com.devineemail.voler.io.Frame;
import com.devineemail.voler.messages.types.DeviceType;
import com.devineemail.voler.messages.types.FunctionType;

public abstract class TableMessage extends Message
{
	protected int id;
	protected int row;
	protected byte[] data;

	public TableMessage(final Frame frame)
	{
		super(frame);

		data = frame.getPayload();
		if (data.length > 2)
		{
			id = (data[0] & 0xFF) << 8 | data[1] & 0xFF;
			row = data[2] & 0xFF;
		}
	}

	public TableMessage(final DeviceType destination, final DeviceType source, final FunctionType function, final int id, final int row) throws IOException
	{
		super(destination, source, function);

		this.id = id;
		this.row = row;
	}

	public int getId()
	{
		return id;
	}

	public int getRow()
	{
		return row;
	}

	protected abstract byte[] marshalTableData();

	@Override
	protected byte[] marshalPayload()
	{
		final byte[] tableData = marshalTableData();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(tableData.length + 3);

		baos.write((id >>> 8) & 0xFF);
		baos.write((id >>> 0) & 0xFF);
		baos.write(row);
		baos.write(tableData, 0, tableData.length);

		return baos.toByteArray();
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(" Table: 0x" + HexUtils.toHexString(id, 1) + " Row: 0x" + HexUtils.toHexString(row, 1));
		if (data.length > 3)
		{
			builder.append(" Payload: 0x" + HexUtils.toHexString(data, 3, data.length - 3));
		}

		return builder.toString();
	}
}
