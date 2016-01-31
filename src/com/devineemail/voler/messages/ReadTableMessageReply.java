package com.devineemail.voler.messages;

import com.devineemail.voler.io.Frame;

public class ReadTableMessageReply extends TableMessage
{
	protected byte[] tableData;
	
	public ReadTableMessageReply( Frame rawFrame )
	{
		super( rawFrame );

		tableData = new byte[ data.length - 3 ];
		System.arraycopy(data, 3, tableData, 0, tableData.length);
	}

	protected byte[] marshalTableData()
	{
		return new byte[] {};
	}
}
