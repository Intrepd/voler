package com.devineemail.voler.model;

import java.util.logging.Logger;

import com.devineemail.voler.messages.Message;

public abstract class MessageHandler
{
	protected static final Logger logger = Logger.getLogger(MessageHandler.class.getName());

	public abstract Class<? extends Message> getMessageClass();

	public abstract void handleMessage(final Message message);
}
