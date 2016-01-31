package com.devineemail.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.devineemail.util.CRC16;

public class CRC16OutputStream extends ByteArrayOutputStream
{
	private CRC16 crc = new CRC16();
	
	public CRC16OutputStream(int size)
	{
		super(size + 2);
	}

	@Override
	public void write(int b)
	{
		super.write(b);
		crc.update(b);		
	}

	@Override
	public void write(byte[] b) throws IOException
	{
		super.write(b);
		crc.update(b);
		
	}

	@Override
	public void write(byte[] b, int off, int len)
	{
		super.write(b, off, len);
		crc.update(b);
	}

	@Override
	public byte[] toByteArray()
	{
		int crcValue = crc.getCRC();
		
		super.write( crcValue );
		super.write( crcValue >> 8 );
		
		return super.toByteArray();
	}
}
