package com.devineemail.voler.ui;

import java.awt.GridBagLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.devineemail.voler.io.MessageIO;

public class MainFrame extends JFrame
{
	private static final Logger log = Logger.getLogger(MainFrame.class.getName());

	public MainFrame(final MessageIO messageIO)
	{
		super("Voler");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());

		final BagHints hints = new BagHints();
		final int row = 0;
		hints.set(0, row, 1, 1, 1.0D, 1.0D);
		add(new MessagePanel(this, messageIO), hints);

		setSize(1100, 600);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	@SuppressWarnings("unused")
	private void setNativeLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (final Exception e)
		{
			log.log(Level.WARNING, "Could not set Look and Feel", e);
		}
	}
}
