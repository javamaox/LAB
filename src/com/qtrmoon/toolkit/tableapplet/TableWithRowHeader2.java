package com.qtrmoon.toolkit.tableapplet;


import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TableWithRowHeader2 {

	public static void main(String[] args) {
		final JFrame f = new JFrame("有行头的表格2");
		String[][] tableData = { { "90", "89", "67", "88" },
				{ "80", "99", "77", "58" }, { "80", "99", "77", "58" } };
		String[] columnNames = { "数学", "语文", "英语", "化学" };
		String[] rowNames = { "张三", "李四", "王二" };
		String tableCorn = "姓名\\课程";

		JTable table = new JTable(tableData, columnNames);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(table);

		// 设置表格的行头和行列交界处的边角的显示
		scrollPane.setRowHeader(createRowHeader(rowNames));
		JLabel corner = new JLabel(tableCorn);
		corner.setBackground((Color) UIManager.get("TableHeader.background"));
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);

		f.add(scrollPane);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}

	/**
	 * 获取到一个使用表格加入到ScrollPane的rowheader，使其作为表的行头
	 * 
	 * @param rowNames
	 * @return
	 */
	private static JViewport createRowHeader(String[] rowNames) {
		String[][] data = new String[rowNames.length][1];
		for (int i = 0; i < rowNames.length; i++) {
			data[i][0] = rowNames[i];
		}
		// 列头随便取一个，在JViewport里是不能显示的
		JTable table = new JTable(data, new String[] { "aa" });
		table.setEnabled(false);
		table.setBackground((Color) UIManager.get("TableHeader.background"));
		JViewport view = new JViewport();
		view.setView(table);
		view.setPreferredSize(new Dimension(60, 54));
		return view;
	}
}