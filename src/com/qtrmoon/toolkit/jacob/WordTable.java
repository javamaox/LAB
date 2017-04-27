package com.qtrmoon.toolkit.jacob;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class WordTable extends BasicOp{
	private Dispatch table;
	private Dispatch selection;
	private Dispatch doc;
	
	/** ��ǰ����е������� */
	private Dispatch rows;

	/** ����е�ĳһ�� */
	private Dispatch row;

	/** ����е������� */
	private Dispatch columns;

	/** ����е�ĳһ�� */
	private Dispatch column;
	
	public WordTable(Dispatch table, Dispatch selection,Dispatch doc, String[] heads){
		this.table=table;
		this.selection=selection;
		this.doc=doc;
		setHeader(heads);
	}
	public WordTable(Dispatch table, Dispatch selection,Dispatch doc){
		this.table=table;
		this.selection=selection;
		this.doc=doc;
	}
	
	
//	//���õ�ǰ����ߵĴ�ϸ w��Χ��1<w<13 ������Χ��Ϊ��w=6
//	private void setTableBorderWidth(int w) {
//		if (w > 13 || w < 2) {
//			w = 6;
//		}
//		Dispatch borders = Dispatch.get(table, "Borders").toDispatch();
//		Dispatch border = null;
//		/**
//		 * ���ñ���ߵĴ�ϸ 1���������ϱ�һ���� 2�����������һ���� 3�����±�һ���� 4�����ұ�һ���� 5�������ϱ����±�֮������к���
//		 * 6������������ұ�֮����������� 7�������Ͻǵ����½ǵ�б�� 8�������½ǵ����Ͻǵ�б��
//		 */
//
//		for (int i = 1; i < 7; i++) {
//			border = Dispatch.call(borders, "Item", new Variant(i))
//					.toDispatch();
//			Dispatch.put(border, "LineWidth", new Variant(w));
//			Dispatch.put(border, "Visible", new Variant(true));
//		}
//	}

	/**
	 * ���õ�ǰ���ָ���е��п�
	 * 
	 * @param columnWidth
	 * @param columnIndex
	 * @throws �����������ı����ʹ��
	 */
	public void setColumnWidth(float columnWidth, int columnIndex) {
		if (columnWidth < 11) {
			columnWidth = 120;
		}

		if (columns == null || column == null) {
			this.getColumns();
			this.getColumn(columnIndex);
		}

		Dispatch.put(column, "Width", new Variant(columnWidth));
	}

	/**
	 * ���õ�ǰ���ָ���еı���ɫ
	 * 
	 * @param columnIndex
	 * @param color
	 *            ȡֵ��Χ 0 < color < 17 Ĭ�ϣ�16 ǳ��ɫ 1����ɫ 2����ɫ 3��ǳ�� ...............
	 */
	public void setColumnBgColor(int columnIndex, int color) {
		this.getColumn(columnIndex);

		Dispatch shading = Dispatch.get(column, "Shading").toDispatch();

		if (color > 16 || color < 1)
			color = 16;
		Dispatch
				.put(shading, "BackgroundPatternColorIndex", new Variant(color));
	}
	/**
	 * ���õ�ǰ���ָ���еı���ɫ
	 * 
	 * @param rowIndex
	 * @param color
	 *            ȡֵ��Χ 0 < color < 17 Ĭ�ϣ�16 ǳ��ɫ 1����ɫ 2����ɫ 3��ǳ�� ...............
	 */
	public void setRowBgColor(int rowIndex, int color) {
		this.getRow(rowIndex);

		Dispatch shading = Dispatch.get(row, "Shading").toDispatch();

		if (color > 16 || color < 1)
			color = 16;
		Dispatch
				.put(shading, "BackgroundPatternColorIndex", new Variant(color));
	}

	/**
	 * ��ָ����ǰ��������
	 * 
	 * @param tableIndex
	 *            word�ļ��еĵ�N�ű�(��1��ʼ)
	 * @param rowIndex
	 *            ָ���е����(��1��ʼ)
	 */
	public void addTableRow(int rowIndex) {
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch row = Dispatch.call(rows, "Item", new Variant(rowIndex))
				.toDispatch();
		Dispatch.call(rows, "Add", new Variant(row));
	}

	/**
	 * �ڵ�1��ǰ����һ��
	 * 
	 * @param tableIndex
	 *            word�ĵ��еĵ�N�ű�(��1��ʼ)
	 */
	public void addFirstTableRow() {
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch row = Dispatch.get(rows, "First").toDispatch();
		Dispatch.call(rows, "Add", new Variant(row));
	}

	/**
	 * �����1��ǰ����һ��
	 * 
	 * @param tableIndex
	 *            word�ĵ��еĵ�N�ű�(��1��ʼ)
	 */
	public void addLastTableRow(int tableIndex) {
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch row = Dispatch.get(rows, "Last").toDispatch();
		Dispatch.call(rows, "Add", new Variant(row));
	}

	/**
	 * ����һ��
	 * 
	 * @param tableIndex
	 *            word�ĵ��еĵ�N�ű�(��1��ʼ)
	 */
	public void addRow() {
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch.call(rows, "Add");
	}
	public void addRow(String[] line) {
		// ����������
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch.call(rows, "Add");
		for(int i=0;i<line.length;i++){
			select(getCell(getRowsCount(), i+1));
			write(line[i]);
		}
	}

	/**
	 * ����һ��
	 * 
	 * @param tableIndex
	 *            word�ĵ��еĵ�N�ű�(��1��ʼ)
	 */
	public void addCol(int tableIndex) {
		Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
		Dispatch.call(cols, "Add").toDispatch();
		Dispatch.call(cols, "AutoFit");
	}

	/**
	 * ��ָ����ǰ�����ӱ�����
	 * 
	 * @param tableIndex
	 *            word�ĵ��еĵ�N�ű�(��1��ʼ)
	 * @param colIndex
	 *            �ƶ��е���� (��1��ʼ)
	 */
	public void addTableCol(int tableIndex, int colIndex) {
		Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
		System.out.println(Dispatch.get(cols, "Count"));
		Dispatch col = Dispatch.call(cols, "Item", new Variant(colIndex))
				.toDispatch();
		// Dispatch col = Dispatch.get(cols, "First").toDispatch();
		Dispatch.call(cols, "Add", col).toDispatch();
		Dispatch.call(cols, "AutoFit");
	}

	/**
	 * �ڵ�1��ǰ����һ��
	 * 
	 * @param tableIndex
	 *            word�ĵ��еĵ�N�ű�(��1��ʼ)
	 */
	public void addFirstTableCol(int tableIndex) {
		Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
		Dispatch col = Dispatch.get(cols, "First").toDispatch();
		Dispatch.call(cols, "Add", col).toDispatch();
		Dispatch.call(cols, "AutoFit");
	}

	/**
	 * �����һ��ǰ����һ��
	 * 
	 * @param tableIndex
	 *            word�ĵ��еĵ�N�ű�(��1��ʼ)
	 */
	public void addLastTableCol(int tableIndex) {
		Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
		Dispatch col = Dispatch.get(cols, "Last").toDispatch();
		Dispatch.call(cols, "Add", col).toDispatch();
		Dispatch.call(cols, "AutoFit");
	}
	/**
	 * ���ҵ�ǰ���ڱ��ĳ��Ԫ��
	 * @param cellRow
	 * @param cellColumn
	 * @return
	 */
	public Dispatch getCell(int cellRow, int cellColumn) {
		return Dispatch.call(table, "Cell", new Variant(cellRow),new Variant(cellColumn)).toDispatch();
	}
	/**
	 * �ӵ�tIndex��Table��ȡ��ֵ��row�У���col�е�ֵ
	 * @param cellRowIdx cell��Table��row��
	 * @param cellColIdx cell��Talbe��col��
	 * @return cell��Ԫֵ
	 * @throws Exception
	 */
	public String getCellString(int cellRowIdx, int cellColIdx)
			throws Exception {
		Dispatch cell = getCell(cellRowIdx,cellColIdx);
		Dispatch.call(cell, "Select");
		return Dispatch.get(selection, "Text").toString();
	}

	/**
	 * ��ָ���ĵ�Ԫ������д����
	 * @param cellRowIdx cell��Table��row��
	 * @param cellColIdx cell��Talbe��col��
	 * @param txt ��д������
	 */
	public void putTxtToCell(int cellRowIdx, int cellColIdx,
			String txt) {
		Dispatch cell = getCell(cellRowIdx,cellColIdx);
		Dispatch.call(cell, "Select");
		Dispatch.put(selection, "Text", txt);
	}

	/**
	 * �ڵ�ǰ�ĵ�ָ����λ�ÿ������
	 * 
	 * @param pos
	 *            ��ǰ�ĵ�ָ����λ��
	 * @param tableIndex
	 *            �������ı����word�ĵ���������λ��
	 */
	public void copyTable(String pos, int tableIndex) {
		// ���б��
		Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
		// Ҫ���ı��
		Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
		Dispatch range = Dispatch.get(table, "Range").toDispatch();
		Dispatch.call(range, "Copy");
		Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
		Dispatch.call(textRange, "Paste");

	}

	/** ���ҵ�ǰ�б���������е�ĳһ��
	 * 
	 * @param rowIndex
	 * @return
	 */
	public Dispatch getRow(int rowIndex) {
		if (rows == null) this.getRows();
		row = Dispatch.invoke(rows, "item", Dispatch.Method,
				new Object[] { new Integer(rowIndex) }, new int[1])
				.toDispatch();
		return row;
	}

	public int getRowsCount() {
		if (rows == null)
			this.getRows();
		return Dispatch.get(rows, "Count").getInt();
	}

	/**
	 * �õ���ǰ�������е���
	 * 
	 * @return
	 */
	// ��Ҫ�ҵ�Dispatch����,�����Variant(1)����һ��Ҫ���ɱ���
	public Dispatch getColumns() {
		return this.columns = Dispatch.get(table, "Columns").toDispatch();
	}

	/**
	 * �õ���ǰ����ĳһ��
	 * 
	 * @param index
	 *            ������
	 * @return
	 */
	public Dispatch getColumn(int columnIndex) {
		if (columns == null)
			this.getColumns();
		return this.column = Dispatch.call(columns, "Item",
				new Variant(columnIndex)).toDispatch();
	}

	/**
	 * �õ���ǰ��������
	 * 
	 * @return
	 */
	public int getColumnsCount() {
		this.getColumns();
		return Dispatch.get(columns, "Count").toInt();
	}

	/**
	 * ���õ�ǰ���������е��и�
	 * 
	 * @param rowHeight
	 */
	public void setRowHeight(float rowHeight) {
		if (rowHeight > 0) {
			if (rows == null)
				this.getRows();
			Dispatch.put(rows, "Height", new Variant(rowHeight));
		}
	}

	/**
	 * ���õ�ǰ���ָ���е��и�
	 * 
	 * @param rowHeight
	 * @param rowIndex
	 */
	public void setRowHeight(float rowHeight, int rowIndex) {
		if (rowHeight > 0) {
			if (rows == null || row == null) {
				this.getRows();
				this.getRow(rowIndex);
			}
			Dispatch.put(row, "Height", new Variant(rowHeight));
		}
	}

	/**
	 * ���õ�ǰ���������е��п�
	 * 
	 * @param columnWidth
	 *            �п� ȡֵ��Χ��10<columnWidth Ĭ��ֵ��120
	 */
	public void setColumnWidth(float columnWidth) {
		if (columnWidth < 11) {
			columnWidth = 120;
		}
		if (columns == null)
			this.getColumns();
		Dispatch.put(columns, "Width", new Variant(columnWidth));
	}

	/**
	 * ���Ʊ��ĳһ��
	 * 
	 * @param tableIndex
	 * @param rowIndex
	 */
	public void copyRow(int tableIndex, int rowIndex) {
		getRows();
		row = getRow(rowIndex);
		Dispatch.call(row, "Select");
		Dispatch.call(selection, "Copy");
	}


	/**
	 * ���ҵ�ǰ���ȫ����
	 * @return
	 */
	// ��Ҫ�ҵ�Dispatch����,�����Variant(1)����һ��Ҫ���ɱ���
	public Dispatch getRows() {
		rows = Dispatch.get(table, "rows").toDispatch();
		return rows;
	}

	/**
	 * �ϲ���ǰ���ָ���ĵ�Ԫ�� �����Ҫһ�κϲ�������Ԫ��ֻ��Ҫָ����һ����Ԫ������һ����Ԫ��
	 * 
	 * @param fstCellRowIndex
	 *            ��һ����Ԫ���������
	 * @param fstCellColIndex
	 *            ��һ����Ԫ���������
	 * @param secCellRowIndex
	 *            �ڶ�����Ԫ���������
	 * @param secCellColIndex
	 *            �ڶ�����Ԫ���������
	 */
	public void mergeCell(int fstCellRowIndex, int fstCellColIndex,
			int secCellRowIndex, int secCellColIndex) {
		Dispatch fstCell = Dispatch.call(table, "Cell",
				new Variant(fstCellRowIndex), new Variant(fstCellColIndex))
				.toDispatch();
		Dispatch secCell = Dispatch.call(table, "Cell",
				new Variant(secCellRowIndex), new Variant(secCellColIndex))
				.toDispatch();
		Dispatch.call(fstCell, "Merge", secCell);
	}

	/**
	 * �ϲ���ǰ���ָ������
	 * 
	 * @param columnIndex
	 *            ������
	 */
	public void mergeColumn(int columnIndex) {
		this.getColumn(columnIndex);

		Dispatch cells = Dispatch.get(column, "Cells").toDispatch();
		Dispatch.call(cells, "Merge");
	}

	/**
	 * �ϲ���ǰ����ָ������
	 * 
	 * @param rowIndex
	 */
	public void mergeRow(int rowIndex) {
		this.getRow(rowIndex);

		Dispatch cells = Dispatch.get(row, "Cells").toDispatch();
		Dispatch.call(cells, "Merge");
	}


	private void setHeader(String[] heads) {
		for(int i=0;i<heads.length;i++){
			select(getCell(1, i+1));
			write(heads[i]);
		}
		Dispatch.put(table, "Style", new Variant("������"));
	}
}
