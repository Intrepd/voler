package com.devineemail.voler.io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.Ostermiller.util.CircularByteBuffer;
import com.devineemail.io.SerialPortIO;
import com.devineemail.voler.messages.Message;
import com.devineemail.voler.messages.MessageFactory;
import com.devineemail.voler.model.MessageHandler;

public class MessageIO
{
	public interface Listener
	{
		public void newMessage(Message message);
	}

	private final CircularByteBuffer buffer = new CircularByteBuffer(65535);
	private final List<Listener> listenerList = new CopyOnWriteArrayList<>();
	private final Map<Class<? extends Message>, List<MessageHandler>> handlerMap = new HashMap<>();
	private final ReadThread readThread;

	private final SerialPortIO serialIO;

	private final boolean run = true;

	public MessageIO(final String inputDevice) throws IOException
	{
		serialIO = new SerialPortIO(inputDevice, buffer.getOutputStream());
		readThread = new ReadThread(buffer.getInputStream());
	}

	public void start()
	{
		readThread.start();
	}

	public void sendMessage(final Message message) throws IOException
	{
		if (serialIO != null)
		{
			serialIO.write(message.marshal());
		}
	}

	public void addListener(final Listener listener)
	{
		listenerList.add(listener);
	}

	public void addHandler(final MessageHandler handler)
	{
		List<MessageHandler> handlerList = handlerMap.get(handler.getMessageClass());
		if (handlerList == null)
		{
			handlerList = new CopyOnWriteArrayList<>();
			handlerMap.put(handler.getMessageClass(), handlerList);
		}
		handlerList.add(handler);
	}

	private void fireNewMessage(final Message message)
	{
		final List<Listener> clonedListeners = new ArrayList<>(listenerList);
		for (final Listener listener : clonedListeners)
		{
			listener.newMessage(message);
		}
	}

	private void invokeHandlers(final Message message)
	{
		final List<MessageHandler> handlerList = handlerMap.get(message.getClass());
		if (handlerList != null)
		{
			final List<MessageHandler> clonedHandlerList = new ArrayList<>(handlerList);
			for (final MessageHandler handler : clonedHandlerList)
			{
				handler.handleMessage(message);
			}
		}
	}

	private class ReadThread extends Thread
	{
		private final byte[] buffer = new byte[257];
		private final PushbackInputStream pbis;
		private final DataInputStream dis;

		private ReadThread(final InputStream is)
		{
			super("Read Thread");

			pbis = new PushbackInputStream(new BufferedInputStream(is), 1024);
			dis = new DataInputStream(pbis);
		}

		@Override
		public void run()
		{
			try
			{
				while (run)
				{
					Frame frame = null;
					while (frame == null)
					{
						dis.readFully(buffer, 0, 8);

						// TODO Is a 0 length valid?
						if (buffer[1] != 0x01 || buffer[3] != 0x01 || buffer[5] != 0x00 || buffer[6] != 0x00)
						{
							pbis.unread(buffer, 1, 7);
						}
						else
						{
							final int length = (buffer[4] & 0xFF) + 2;
							dis.readFully(buffer, 8, length);
							frame = new Frame(buffer, 0, length + 8);
							if (!frame.isCRCCorrect())
							{
								pbis.unread(buffer, 1, length + 7);
								frame = null;
							}
						}
					}

					final Message message = MessageFactory.create(frame);
					fireNewMessage(message);
					invokeHandlers(message);
				}
			}
			catch (final IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
	}
}
