package com.devineemail.voler.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TableColumnAdjuster implements PropertyChangeListener, TableModelListener
{
	private final JTable table;
	private final int spacing;
	private boolean isColumnHeaderIncluded;
	private boolean isColumnDataIncluded;
	private boolean isOnlyAdjustLarger;
	private boolean isDynamicAdjustment;
	private final Map<TableColumn, Integer> columnSizes = new HashMap<TableColumn, Integer>();

	public TableColumnAdjuster(final JTable table)
	{
		this(table, 6);
	}

	public TableColumnAdjuster(final JTable table, final int spacing)
	{
		this.table = table;
		this.spacing = spacing;
		setColumnHeaderIncluded(true);
		setColumnDataIncluded(true);
		setOnlyAdjustLarger(true);
		setDynamicAdjustment(false);
		// installActions();
	}

	public void adjustColumns()
	{
		final TableColumnModel tcm = table.getColumnModel();

		for (int i = 0; i < tcm.getColumnCount(); i++)
		{
			adjustColumn(i);
		}
	}

	public void adjustColumn(final int column)
	{
		final TableColumn tableColumn = table.getColumnModel().getColumn(column);

		if (!tableColumn.getResizable())
		{
			return;
		}

		final int columnHeaderWidth = getColumnHeaderWidth(column);
		final int columnDataWidth = getColumnDataWidth(column);
		final int preferredWidth = Math.max(columnHeaderWidth, columnDataWidth);

		updateTableColumn(column, preferredWidth);
	}

	private int getColumnHeaderWidth(final int column)
	{
		if (!isColumnHeaderIncluded)
		{
			return 0;
		}

		final TableColumn tableColumn = table.getColumnModel().getColumn(column);
		final Object value = tableColumn.getHeaderValue();
		TableCellRenderer renderer = tableColumn.getHeaderRenderer();

		if (renderer == null)
		{
			renderer = table.getTableHeader().getDefaultRenderer();
		}

		final Component c = renderer.getTableCellRendererComponent(table, value, false, false, -1, column);
		return c.getPreferredSize().width;
	}

	private int getColumnDataWidth(final int column)
	{
		if (!isColumnDataIncluded)
		{
			return 0;
		}

		int preferredWidth = 0;
		final int maxWidth = table.getColumnModel().getColumn(column).getMaxWidth();

		for (int row = 0; row < table.getRowCount(); row++)
		{
			preferredWidth = Math.max(preferredWidth, getCellDataWidth(row, column));

			// We've exceeded the maximum width, no need to check other rows

			if (preferredWidth >= maxWidth)
			{
				break;
			}
		}

		return preferredWidth;
	}

	private int getCellDataWidth(final int row, final int column)
	{
		// Invoke the renderer for the cell to calculate the preferred width

		final TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
		final Component c = table.prepareRenderer(cellRenderer, row, column);
		final int width = c.getPreferredSize().width + table.getIntercellSpacing().width;

		return width;
	}

	private void updateTableColumn(final int column, int width)
	{
		final TableColumn tableColumn = table.getColumnModel().getColumn(column);

		if (!tableColumn.getResizable())
		{
			return;
		}

		width += spacing;

		// Don't shrink the column width

		if (isOnlyAdjustLarger)
		{
			width = Math.max(width, tableColumn.getPreferredWidth());
		}

		columnSizes.put(tableColumn, new Integer(tableColumn.getWidth()));
		table.getTableHeader().setResizingColumn(tableColumn);
		tableColumn.setWidth(width);
	}

	public void restoreColumns()
	{
		final TableColumnModel tcm = table.getColumnModel();

		for (int i = 0; i < tcm.getColumnCount(); i++)
		{
			restoreColumn(i);
		}
	}

	private void restoreColumn(final int column)
	{
		final TableColumn tableColumn = table.getColumnModel().getColumn(column);
		final Integer width = columnSizes.get(tableColumn);

		if (width != null)
		{
			table.getTableHeader().setResizingColumn(tableColumn);
			tableColumn.setWidth(width.intValue());
		}
	}

	public void setColumnHeaderIncluded(final boolean isColumnHeaderIncluded)
	{
		this.isColumnHeaderIncluded = isColumnHeaderIncluded;
	}

	public void setColumnDataIncluded(final boolean isColumnDataIncluded)
	{
		this.isColumnDataIncluded = isColumnDataIncluded;
	}

	public void setOnlyAdjustLarger(final boolean isOnlyAdjustLarger)
	{
		this.isOnlyAdjustLarger = isOnlyAdjustLarger;
	}

	public void setDynamicAdjustment(final boolean isDynamicAdjustment)
	{
		// May need to add or remove the TableModelListener when changed

		if (this.isDynamicAdjustment != isDynamicAdjustment)
		{
			if (isDynamicAdjustment)
			{
				table.addPropertyChangeListener(this);
				table.getModel().addTableModelListener(this);
			}
			else
			{
				table.removePropertyChangeListener(this);
				table.getModel().removeTableModelListener(this);
			}
		}

		this.isDynamicAdjustment = isDynamicAdjustment;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent e)
	{
		// When the TableModel changes we need to update the listeners
		// and column widths

		if ("model".equals(e.getPropertyName()))
		{
			TableModel model = (TableModel) e.getOldValue();
			model.removeTableModelListener(this);

			model = (TableModel) e.getNewValue();
			model.addTableModelListener(this);
			adjustColumns();
		}
	}

	@Override
	public void tableChanged(final TableModelEvent e)
	{
		if (!isColumnDataIncluded)
		{
			return;
		}

		// A cell has been updated

		if (e.getType() == TableModelEvent.UPDATE)
		{
			final int column = table.convertColumnIndexToView(e.getColumn());

			// Only need to worry about an increase in width for this cell

			if (isOnlyAdjustLarger)
			{
				final int row = e.getFirstRow();
				final TableColumn tableColumn = table.getColumnModel().getColumn(column);

				if (tableColumn.getResizable())
				{
					final int width = getCellDataWidth(row, column);
					updateTableColumn(column, width);
				}
			}

			// Could be an increase of decrease so check all rows

			else
			{
				adjustColumn(column);
			}
		}

		// The update affected more than one column so adjust all columns

		else
		{
			adjustColumns();
		}
	}

	class ColumnAction extends AbstractAction
	{
		private final boolean isSelectedColumn;
		private final boolean isAdjust;

		public ColumnAction(final boolean isSelectedColumn, final boolean isAdjust)
		{
			this.isSelectedColumn = isSelectedColumn;
			this.isAdjust = isAdjust;
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			// Handle selected column(s) width change actions

			if (isSelectedColumn)
			{
				final int[] columns = table.getSelectedColumns();

				for (int i = 0; i < columns.length; i++)
				{
					if (isAdjust)
					{
						adjustColumn(columns[i]);
					}
					else
					{
						restoreColumn(columns[i]);
					}
				}
			}
			else
			{
				if (isAdjust)
				{
					adjustColumns();
				}
				else
				{
					restoreColumns();
				}
			}
		}
	}

	class ToggleAction extends AbstractAction
	{
		private final boolean isToggleDynamic;
		private final boolean isToggleLarger;

		public ToggleAction(final boolean isToggleDynamic, final boolean isToggleLarger)
		{
			this.isToggleDynamic = isToggleDynamic;
			this.isToggleLarger = isToggleLarger;
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			if (isToggleDynamic)
			{
				setDynamicAdjustment(!isDynamicAdjustment);
				return;
			}

			if (isToggleLarger)
			{
				setOnlyAdjustLarger(!isOnlyAdjustLarger);
				return;
			}
		}
	}
}