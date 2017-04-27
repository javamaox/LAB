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
		final JFrame f = new JFrame("����ͷ�ı��2");
		String[][] tableData = { { "90", "89", "67", "88" },
				{ "80", "99", "77", "58" }, { "80", "99", "77", "58" } };
		String[] columnNames = { "��ѧ", "����", "Ӣ��", "��ѧ" };
		String[] rowNames = { "����", "����", "����" };
		String tableCorn = "����\\�γ�";

		JTable table = new JTable(tableData, columnNames);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(table);

		// ���ñ�����ͷ�����н��紦�ı߽ǵ���ʾ
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
	 * ��ȡ��һ��ʹ�ñ����뵽ScrollPane��rowheader��ʹ����Ϊ�����ͷ
	 * 
	 * @param rowNames
	 * @return
	 */
	private static JViewport createRowHeader(String[] rowNames) {
		String[][] data = new String[rowNames.length][1];
		for (int i = 0; i < rowNames.length; i++) {
			data[i][0] = rowNames[i];
		}
		// ��ͷ���ȡһ������JViewport���ǲ�����ʾ��
		JTable table = new JTable(data, new String[] { "aa" });
		table.setEnabled(false);
		table.setBackground((Color) UIManager.get("TableHeader.background"));
		JViewport view = new JViewport();
		view.setView(table);
		view.setPreferredSize(new Dimension(60, 54));
		return view;
	}
}