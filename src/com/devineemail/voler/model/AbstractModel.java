package com.devineemail.voler.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.devineemail.voler.io.MessageIO;

public abstract class AbstractModel
{
	protected static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	protected ScheduledFuture<?> refreshFuture;
	protected MessageIO messageIO;

	public interface Listener
	{
		public void modelChanged();
	}

	protected List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

	public void addListener(final Listener listener)
	{
		listeners.add(listener);
	}

	public void setRefreshInterval(final int seconds)
	{
		if (refreshFuture != null)
		{
			refreshFuture.cancel(false);
		}
		if (seconds > 0)
		{
			refreshFuture = executor.scheduleAtFixedRate(new RefreshRunner(), 0, seconds, TimeUnit.SECONDS);
		}
	}

	public void register(final MessageIO messageIO) throws IOException
	{
		this.messageIO = messageIO;
		doRegister();
		refreshModel();
	}

	protected void modelChanged()
	{
		final List<Listener> clonedListeners = new ArrayList<>(listeners);
		for (final Listener listener : clonedListeners)
		{
			listener.modelChanged();
		}
	}

	public abstract void doRegister();

	public abstract void refreshModel() throws IOException;

	private class RefreshRunner implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				refreshModel();
			}
			catch (final IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
	}
}