package com.devineemail.voler.ui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.devineemail.util.HexUtils;
import com.devineemail.voler.io.MessageIO;
import com.devineemail.voler.messages.Message;

public class MessagePanel extends JPanel
{
	// private static final Logger log = Logger.getLogger(MessagePanel.class.getName());

	private final MessageIO messageIO;
	private final JTable messageTable;

	public MessagePanel(final JFrame owner, final MessageIO messageIO)
	{
		super(new GridBagLayout());
		this.messageIO = messageIO;

		setBorder(new TitledBorder("Raw"));

		final AbstractTableModel tableModel = new MessageTableModel();
		messageTable = new MessageTable(tableModel);

		final JScrollPane scrollPane = new JScrollPane(messageTable);
		new SmartScroller(scrollPane);
		final BagHints hints = new BagHints();
		int row = 0;
		hints.set(0, row, 1, 1, 1.0D, 1.0D, 1, 11);
		add(scrollPane, hints);
		row++;
	}

	private class MessageTable extends FormattedTable
	{
		private MessageTable(final AbstractTableModel tableModel)
		{
			super(tableModel);

			int columnIndex = 0;
			TableColumn column;

			column = columnModel.getColumn(columnIndex++);
			column.setPreferredWidth(100);
			column.setHeaderValue("Destination");

			column = columnModel.getColumn(columnIndex++);
			column.setPreferredWidth(100);
			column.setHeaderValue("Source");

			column = columnModel.getColumn(columnIndex++);
			column.setPreferredWidth(100);
			column.setHeaderValue("Length");

			column = columnModel.getColumn(columnIndex++);
			column.setPreferredWidth(100);
			column.setHeaderValue("Function");

			column = columnModel.getColumn(columnIndex++);
			column.setHeaderValue("Payload");

			tableColumnAdjuster.adjustColumns();
			setPreferredScrollableViewportSize(new Dimension(columnModel.getTotalColumnWidth(), 200));
		}
	}

	private class MessageTableModel extends AbstractTableModel
	{
		private final List<Message> messageList = new ArrayList<>();

		private MessageTableModel()
		{
			messageIO.addListener(new MessageIO.Listener()
			{
				@Override
				public void newMessage(final Message message)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							messageList.add(message);
							fireTableRowsInserted(messageList.size() - 1, messageList.size() - 1);
						}
					});
				}
			});
		}

		@Override
		public int getColumnCount()
		{
			return 5;
		}

		@Override
		public int getRowCount()
		{
			return messageList.size();
		}

		@Override
		public Object getValueAt(final int row, final int column)
		{
			Object value = null;

			if (row >= 0 && row < getRowCount() && column >= 0 && column < getColumnCount())
			{
				final Message message = messageList.get(row);
				switch (column)
				{
					case 0:
						value = message.getDestination();
						break;
					case 1:
						value = message.getSource();
						break;
					case 2:
						value = message.getPayload().length;
						break;
					case 3:
						value = message.getFunction();
						break;
					case 4:
						value = HexUtils.toHexString(message.getPayload());
						break;
					default:
						// nothing
						break;
				}
			}

			return value;
		}
	}
}