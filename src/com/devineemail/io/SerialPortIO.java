package com.devineemail.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.devineemail.util.HexUtils;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialPortIO implements SerialPortEventListener
{
	private final BlockingQueue<byte[]> outboundQ = new ArrayBlockingQueue<>(100);
	private final SerialPort serialPort;
	private final OutputStream os;
	private final WriteThread writeThread = new WriteThread();

	private volatile long lastActivity = Long.MAX_VALUE;
	private final boolean run = true;

	public SerialPortIO(final String portId, final OutputStream os) throws IOException
	{
		this.os = os;

		serialPort = new SerialPort(portId);
		try
		{
			serialPort.openPort();
			serialPort.setEventsMask(SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR);
			serialPort.setParams(38400, 8, 1, 0);
			serialPort.addEventListener(this);
		}
		catch (final SerialPortException spe)
		{
			throw new IOException(spe);
		}
		writeThread.start();
	}

	public void write(final byte[] buffer)
	{
		outboundQ.add(buffer);
	}

	@Override
	public void serialEvent(final SerialPortEvent event)
	{
		synchronized (serialPort)
		{
			switch (event.getEventType())
			{
				case SerialPortEvent.RXCHAR:
					try
					{
						lastActivity = System.currentTimeMillis();
						final int bytesAvailable = event.getEventValue();
						final byte[] buffer = serialPort.readBytes(bytesAvailable);
						os.write(buffer);
					}
					catch (final Exception e)
					{
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		}
	}

	private class WriteThread extends Thread
	{
		public WriteThread()
		{
			super("SerialPortIO Writer");
		}

		@Override
		public void run()
		{
			while (run)
			{
				try
				{
					byte[] buffer = outboundQ.poll(250, TimeUnit.MILLISECONDS);
					if (buffer != null)
					{
						while (buffer != null)
						{
							if (System.currentTimeMillis() - lastActivity > 100)
							{
								lastActivity = System.currentTimeMillis();
								System.out.println("Writing: 0x" + HexUtils.toHexString(buffer));
								serialPort.writeBytes(buffer);
								buffer = null;
							}
							else
							{
								Thread.sleep(10);
							}
						}
					}
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
