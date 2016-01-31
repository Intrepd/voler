package com.devineemail.voler.ui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class FormattedTable extends JXTable
{
	protected final TableColumnAdjuster tableColumnAdjuster = new TableColumnAdjuster(this);

	protected FormattedTable(final TableModel tableModel)
	{
		this(tableModel, ListSelectionModel.SINGLE_SELECTION);
	}

	protected FormattedTable(final TableModel tableModel, final int selectionMode)
	{
		super(tableModel);

		tableHeader.setFont(tableHeader.getFont().deriveFont(Font.BOLD));
		tableHeader.setReorderingAllowed(false);

		setAutoscrolls(true);
		setEditable(false);
		setAutoCreateRowSorter(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setHighlighters(HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));
		setFillsViewportHeight(true);
		setHorizontalScrollEnabled(true);
		setRowSelectionAllowed(true);
		setSelectionMode(selectionMode);
		setRowMargin(0);
		setRowHeight(30);
		setShowGrid(false);
		setShowHorizontalLines(false);
		setShowVerticalLines(false);
		setIntercellSpacing(new Dimension(0, 0));
	}
}