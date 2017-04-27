package com.qtrmoon.toolkit.jacob;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class WordTable extends BasicOp{
	private Dispatch table;
	private Dispatch selection;
	private Dispatch doc;
	
	/** 当前表格中的所有行 */
	private Dispatch rows;

	/** 表格中的某一行 */
	private Dispatch row;

	/** 表格中的所有列 */
	private Dispatch columns;

	/** 表格中的某一列 */
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
	
	
//	//设置当前表格线的粗细 w范围：1<w<13 超过范围设为：w=6
//	private void setTableBorderWidth(int w) {
//		if (w > 13 || w < 2) {
//			w = 6;
//		}
//		Dispatch borders = Dispatch.get(table, "Borders").toDispatch();
//		Dispatch border = null;
//		/**
//		 * 设置表格线的粗细 1：代表最上边一条线 2：代表最左边一条线 3：最下边一条线 4：最右边一条线 5：除最上边最下边之外的所有横线
//		 * 6：除最左边最右边之外的所有竖线 7：从左上角到右下角的斜线 8：从左下角到右上角的斜线
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
	 * 设置当前表格指定列的列宽
	 * 
	 * @param columnWidth
	 * @param columnIndex
	 * @throws 如果不是整齐的表格不能使用
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
	 * 设置当前表格指定列的背景色
	 * 
	 * @param columnIndex
	 * @param color
	 *            取值范围 0 < color < 17 默认：16 浅灰色 1：黑色 2：蓝色 3：浅蓝 ...............
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
	 * 设置当前表格指定行的背景色
	 * 
	 * @param rowIndex
	 * @param color
	 *            取值范围 0 < color < 17 默认：16 浅灰色 1：黑色 2：蓝色 3：浅蓝 ...............
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
	 * 在指定行前面增加行
	 * 
	 * @param tableIndex
	 *            word文件中的第N张表(从1开始)
	 * @param rowIndex
	 *            指定行的序号(从1开始)
	 */
	public void addTableRow(int rowIndex) {
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch row = Dispatch.call(rows, "Item", new Variant(rowIndex))
				.toDispatch();
		Dispatch.call(rows, "Add", new Variant(row));
	}

	/**
	 * 在第1行前增加一行
	 * 
	 * @param tableIndex
	 *            word文档中的第N张表(从1开始)
	 */
	public void addFirstTableRow() {
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch row = Dispatch.get(rows, "First").toDispatch();
		Dispatch.call(rows, "Add", new Variant(row));
	}

	/**
	 * 在最后1行前增加一行
	 * 
	 * @param tableIndex
	 *            word文档中的第N张表(从1开始)
	 */
	public void addLastTableRow(int tableIndex) {
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch row = Dispatch.get(rows, "Last").toDispatch();
		Dispatch.call(rows, "Add", new Variant(row));
	}

	/**
	 * 增加一行
	 * 
	 * @param tableIndex
	 *            word文档中的第N张表(从1开始)
	 */
	public void addRow() {
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch.call(rows, "Add");
	}
	public void addRow(String[] line) {
		// 表格的所有行
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch.call(rows, "Add");
		for(int i=0;i<line.length;i++){
			select(getCell(getRowsCount(), i+1));
			write(line[i]);
		}
	}

	/**
	 * 增加一列
	 * 
	 * @param tableIndex
	 *            word文档中的第N张表(从1开始)
	 */
	public void addCol(int tableIndex) {
		Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
		Dispatch.call(cols, "Add").toDispatch();
		Dispatch.call(cols, "AutoFit");
	}

	/**
	 * 在指定列前面增加表格的列
	 * 
	 * @param tableIndex
	 *            word文档中的第N张表(从1开始)
	 * @param colIndex
	 *            制定列的序号 (从1开始)
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
	 * 在第1列前增加一列
	 * 
	 * @param tableIndex
	 *            word文档中的第N张表(从1开始)
	 */
	public void addFirstTableCol(int tableIndex) {
		Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
		Dispatch col = Dispatch.get(cols, "First").toDispatch();
		Dispatch.call(cols, "Add", col).toDispatch();
		Dispatch.call(cols, "AutoFit");
	}

	/**
	 * 在最后一列前增加一列
	 * 
	 * @param tableIndex
	 *            word文档中的第N张表(从1开始)
	 */
	public void addLastTableCol(int tableIndex) {
		Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
		Dispatch col = Dispatch.get(cols, "Last").toDispatch();
		Dispatch.call(cols, "Add", col).toDispatch();
		Dispatch.call(cols, "AutoFit");
	}
	/**
	 * 查找当前所在表的某单元格
	 * @param cellRow
	 * @param cellColumn
	 * @return
	 */
	public Dispatch getCell(int cellRow, int cellColumn) {
		return Dispatch.call(table, "Cell", new Variant(cellRow),new Variant(cellColumn)).toDispatch();
	}
	/**
	 * 从第tIndex个Table中取出值第row行，第col列的值
	 * @param cellRowIdx cell在Table第row行
	 * @param cellColIdx cell在Talbe第col列
	 * @return cell单元值
	 * @throws Exception
	 */
	public String getCellString(int cellRowIdx, int cellColIdx)
			throws Exception {
		Dispatch cell = getCell(cellRowIdx,cellColIdx);
		Dispatch.call(cell, "Select");
		return Dispatch.get(selection, "Text").toString();
	}

	/**
	 * 在指定的单元格里填写数据
	 * @param cellRowIdx cell在Table第row行
	 * @param cellColIdx cell在Talbe第col列
	 * @param txt 填写的数据
	 */
	public void putTxtToCell(int cellRowIdx, int cellColIdx,
			String txt) {
		Dispatch cell = getCell(cellRowIdx,cellColIdx);
		Dispatch.call(cell, "Select");
		Dispatch.put(selection, "Text", txt);
	}

	/**
	 * 在当前文档指定的位置拷贝表格
	 * 
	 * @param pos
	 *            当前文档指定的位置
	 * @param tableIndex
	 *            被拷贝的表格在word文档中所处的位置
	 */
	public void copyTable(String pos, int tableIndex) {
		// 所有表格
		Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
		// 要填充的表格
		Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
		Dispatch range = Dispatch.get(table, "Range").toDispatch();
		Dispatch.call(range, "Copy");
		Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
		Dispatch.call(textRange, "Paste");

	}

	/** 查找当前行表格所有行中的某一行
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
	 * 得到当前表格的所有的列
	 * 
	 * @return
	 */
	// 需要找到Dispatch对象,这里的Variant(1)不行一定要做成变量
	public Dispatch getColumns() {
		return this.columns = Dispatch.get(table, "Columns").toDispatch();
	}

	/**
	 * 得到当前表格的某一列
	 * 
	 * @param index
	 *            列索引
	 * @return
	 */
	public Dispatch getColumn(int columnIndex) {
		if (columns == null)
			this.getColumns();
		return this.column = Dispatch.call(columns, "Item",
				new Variant(columnIndex)).toDispatch();
	}

	/**
	 * 得到当前表格的列数
	 * 
	 * @return
	 */
	public int getColumnsCount() {
		this.getColumns();
		return Dispatch.get(columns, "Count").toInt();
	}

	/**
	 * 设置当前表格的所有行的行高
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
	 * 设置当前表格指定行的行高
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
	 * 设置当前表格的所有列的列宽
	 * 
	 * @param columnWidth
	 *            列宽 取值范围：10<columnWidth 默认值：120
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
	 * 复制表的某一行
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
	 * 查找当前表的全部行
	 * @return
	 */
	// 需要找到Dispatch对象,这里的Variant(1)不行一定要做成变量
	public Dispatch getRows() {
		rows = Dispatch.get(table, "rows").toDispatch();
		return rows;
	}

	/**
	 * 合并当前表格指定的单元格 如果需要一次合并几个单元格只需要指出第一个单元格和最后一个单元格
	 * 
	 * @param fstCellRowIndex
	 *            第一个单元格的行索引
	 * @param fstCellColIndex
	 *            第一个单元格的列索引
	 * @param secCellRowIndex
	 *            第二个单元格的行索引
	 * @param secCellColIndex
	 *            第二个单元格的列索引
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
	 * 合并当前表格指定的列
	 * 
	 * @param columnIndex
	 *            列索引
	 */
	public void mergeColumn(int columnIndex) {
		this.getColumn(columnIndex);

		Dispatch cells = Dispatch.get(column, "Cells").toDispatch();
		Dispatch.call(cells, "Merge");
	}

	/**
	 * 合并当前表格的指定的行
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
		Dispatch.put(table, "Style", new Variant("网格型"));
	}
}
