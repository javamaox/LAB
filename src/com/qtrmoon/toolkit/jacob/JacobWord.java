package com.qtrmoon.toolkit.jacob;

import java.awt.event.KeyEvent;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.jacob.com.ComThread;

public class JacobWord extends BasicOp{
	//	 word文档
	private Dispatch doc = null;

	// word运行程序对象
	private ActiveXComponent word = null;

	// 所有word文档集合
	private Dispatch documents = null;

	// 选定的范围或插入点
	private static Dispatch selection = null;
	
	// 设置是否保存后才退出的标志
	private boolean saveOnExit = true;

	/** 所有表格 */
	private Dispatch tables;

	/** 当前表格 */
	private Dispatch table;

	/**
	 * 打开word时同时要打开的文档，不指定时将新建一个空白文档
	 */
	// private File openDoc;
	private static Dispatch shapes;

	private static Dispatch shape;

	private static Dispatch textRange;

	private static Dispatch textframe;

	private Dispatch range;

	private Dispatch paragraphs;

	private Dispatch paragraph;

	// constructor
	public JacobWord() {
		if (word == null) {
			word = new ActiveXComponent("Word.Application");
			word.setProperty("Visible", new Variant(false));
		}
		if (documents == null) {
			documents = word.getProperty("Documents").toDispatch();
		}
	}

	/****************************************
	 ************ 文档创建及初始化 *************
	 ****************************************/
	
	/**
	 * 创建一个新的word文档
	 */
	public void createNewDocument() {
		doc = Dispatch.call(documents, "Add").toDispatch();
		selection = Dispatch.get(word, "Selection").toDispatch();
	}

	/**
	 * 打开一个已存在的文档
	 * @param docPath
	 */
	public void openDocument(String docPath) {
		if (this.doc != null) {
			this.closeDocument();
		}
		doc = Dispatch.call(documents, "Open", docPath).toDispatch();
		selection = Dispatch.get(word, "Selection").toDispatch();
		tables = Dispatch.get(doc, "Tables").toDispatch();
		super.selection=selection;
	}

	/**
	 * 文件保存或另存为
	 * @param savePath
	 *            一定要记得加上扩展名 .doc 保存或另存为路径
	 */
	public void save(String savePath) {
		Dispatch.call(
				(Dispatch) Dispatch.call(word, "WordBasic").getDispatch(),
				"FileSaveAs", savePath);
	}
	
	/**
	 * 关闭当前word文档
	 */
	public void closeDocument() {
		if (doc != null) {
			Dispatch.call(doc, "Save");
			Dispatch.call(doc, "Close", new Variant(saveOnExit));
			doc = null;
		}
	}

	/**
	 * 关闭全部应用
	 */
	public void close() {
		closeDocument();
		if (word != null) {
			Dispatch.call(word, "Quit");
			word = null;
		}
		selection = null;
		documents = null;

	}
	/**
	 * 初始化 com 线程
	 */
	public void initCom() {
		ComThread.InitSTA();
	}

	/**
	 * 释放 com 线程资源 com 的线程回收不由 java 垃圾回收机制回收
	 */
	public void releaseCom() {
		ComThread.Release();
	}
	/****************************************
	 ************ 文字写入 *************
	 ****************************************/
	/**
	 * 文件尾追加写入文本
	 * @param content
	 * @param font
	 */
	public void appendText(String content) {
		appendText(content,null);
	}
	public void appendText(String content,WordFont font) {
		goToEnd();
		super.write(content,font);
	}
	
	/**
	 * 在当前插入点插入字符串
	 * @param newText 要插入的新字符串
	 */
	public void insertText(String newText) {
		insertText(newText,null);
	}
	public void insertText(String newText,WordFont font) {
		write(newText,font);
	}
	
	/**
	 * 把选定选定内容设定为替换文本
	 * @param toFindText 查找字符串
	 * @param newText 要替换的内容
	 * @return
	 */
	public boolean replaceText(String toFindText, String newText) {
		return replaceText(toFindText,newText,null);
	}
	public boolean replaceText(String toFindText, String newText,WordFont font) {
		if (!find(toFindText)) return false;
		write(newText,font);
		return true;
	}

	/**
	 * 全局替换文本
	 * @param toFindText 查找字符串
	 * @param newText 要替换的内容
	 */
	public void replaceAllText(String toFindText, String newText) {
		replaceAllText(toFindText,newText,null);
	}
	public void replaceAllText(String toFindText, String newText,WordFont font) {
		goToBegin();
		while (find(toFindText)) {
			write(newText,font);
			Dispatch.call(selection, "MoveRight");
		}
	}
	/**
	 * 换行<br>
	 */
	public void newParagraph() {
		cmd("TypeParagraph");
	}
	public void newParagraph(int align, double lineSpace) {
		cmd("TypeParagraph");
		setParaFormat(align,lineSpace);
	}
	/**
	 * 对当前段落进行格式化
	 * 
	 * @param align 设置排列方式 默认：居左。0:居左 1:居中 2:居右 3:两端对齐 4:分散对齐
	 * @param lineSpace 设置行间距 默认：1.0。0：1.0 1：1.5 2：2.0 3：最小值 4：固定值
	 */
	public void setParaFormat(int align, double lineSpace) {
		if (align < 0 || align > 4) {
			align = 0;
		}
		if (lineSpace < 0 || lineSpace > 4) {
			lineSpace = 0;
		}
		Dispatch alignment = Dispatch.get(selection, "ParagraphFormat").toDispatch();
		Dispatch.put(alignment, "Alignment", align);
		Dispatch.put(alignment, "LineSpacingRule", new Variant(lineSpace));
	}
	/**
	 * 还原段落默认的格式 左对齐,行间距：1.0
	 */
	public void clearParaFormat() {
		this.setParaFormat(0, 0);
	}
	public Dispatch getParagraph(int tIndex) throws Exception {
		return Dispatch.call(paragraphs, "Item", new Variant(tIndex)).toDispatch();
	}
	public int getParagraphsCount() throws Exception {
		return Dispatch.get(paragraphs, "Count").toInt();
	}
	
	/**
	 * 打印当前word文档
	 * 
	 * @throws Exception
	 *             com.jacob.com.ComFailException: Invoke of: PrintOut Source:
	 *             Microsoft Word 若无打印机
	 */
	public void printFile() {
		if (doc != null) {
			Dispatch.call(doc, "PrintOut");
		}
	}

	/****************************************
	 ************ 图片写入 *************
	 ****************************************/
	/**
	 * 在当前光标处添加图片
	 * 
	 * @param imgPath
	 *            图片的地址
	 */
	public void addImage(String imgPath) {
		if (imgPath != "" && imgPath != null) {
			Dispatch image = Dispatch.get(selection, "InLineShapes")
					.toDispatch();
			Dispatch.call(image, "AddPicture", imgPath);
		}
	}
	/**
	 * 在当前插入点插入图片
	 * 
	 * @param imagePath
	 *            图片路径
	 */
	public void insertImage(String imagePath) {
		Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(),
				"AddPicture", imagePath);
	}
	
	/**
	 * 
	 * @param toFindText
	 *            要查找的字符串
	 * @param imagePath
	 *            图片路径
	 * @return 此函数将字符串替换成图片
	 */
	public boolean replaceImage(String toFindText, String imagePath) {
		if (!find(toFindText))
			return false;
		Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(),
				"AddPicture", imagePath);
		return true;
	}

	/**
	 * 全局替换图片
	 * 
	 * @param toFindText
	 *            查找字符串
	 * @param imagePath
	 *            图片路径
	 */
	public void replaceAllImage(String toFindText, String imagePath) {
		while (find(toFindText)) {
			Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(),
					"AddPicture", imagePath);
			Dispatch.call(selection, "MoveRight");
		}
	}
	
	/**
	 * 在当前文档指定的位置拷贝来自另一个文档中的图片
	 * 
	 * @param anotherDocPath
	 *            另一个文档的磁盘路径
	 * @param shapeIndex
	 *            被拷贝的图片在另一格文档中的位置
	 * @param pos
	 *            当前文档指定的位置
	 * @throws com.jacob.com.ComFailException
	 *             Invoke of: Item Source: Microsoft Word 若shapeIndex不存在
	 */
	public void copyImageFromAnotherDoc(String anotherDocPath, int shapeIndex,
			String pos) {
		Dispatch doc2 = null;
		try {
			doc2 = Dispatch.call(documents, "Open", anotherDocPath)
					.toDispatch();
			Dispatch shapes = Dispatch.get(doc2, "InLineShapes").toDispatch();
			Dispatch shape = Dispatch.call(shapes, "Item",
					new Variant(shapeIndex)).toDispatch();
			Dispatch imageRange = Dispatch.get(shape, "Range").toDispatch();
			Dispatch.call(imageRange, "Copy");

			Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
			moveDown(4);
			Dispatch.call(textRange, "Paste");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (doc2 != null) {
				Dispatch.call(doc2, "Close", new Variant(saveOnExit));
				doc2 = null;
			}
		}
	}

	/****************************************
	 ************ 表格操作 *************
	 ****************************************/
	/**
	 * 插入表格，从文档开始处查找pos匹配的字符串，定位在首次匹配的串。
	 * @param pos 位置
	 * @param heads 表头
	 */
	public WordTable insertTable(String pos, String[] heads) {
		goToBegin();
		if (find(pos)) {
			moveRight(1);
			return writeTable(heads);
		}else{
			return null;
		}
	}
	/**
	 * 文本替换为表格，从文档开始处查找pos匹配的字符串，定位在首次匹配的串。
	 * @param pos 位置
	 * @param heads 表头
	 */
	public WordTable replaceTable(String pos,String[] heads) {
		goToBegin();
		if (find(pos)) {
			return writeTable(heads);
		}else{
			return null;
		}
	}
	/**
	 * 在文件尾追加表格。
	 * @param heads 表头
	 */
	public WordTable appendTable(String[] heads) {
		goToEnd();
		return writeTable(heads);
	}
	private WordTable writeTable(String[] heads) {
		Dispatch range = Dispatch.get(selection, "Range").toDispatch();
		table = Dispatch.call(tables, "Add", range,
				new Variant(1), new Variant(heads.length)).toDispatch();
		WordTable wTable=new WordTable(table,selection,doc,heads);
		moveRight(1);
		return wTable;
	}
	/**
	 * 查找表
	 * @param tableIndex
	 * @return
	 */
	public WordTable getTable(int tableIndex) {
		tables = Dispatch.get(doc, "Tables").toDispatch();
		table = Dispatch.call(tables, "Item", new Variant(tableIndex))
				.toDispatch();
		return new WordTable(table,selection,doc);
	}
	/**
	 * 在当前文档指定的位置拷贝来自另一个文档中的表格
	 * 
	 * @param anotherDocPath
	 *            另一个文档的磁盘路径
	 * @param tableIndex
	 *            被拷贝的表格在另一格文档中的位置
	 * @param pos
	 *            当前文档指定的位置
	 */
	public void copyTableFromAnotherDoc(String anotherDocPath, int tableIndex,String pos) {
		Dispatch doc2 = null;
		try {
			doc2 = Dispatch.call(documents, "Open", anotherDocPath)
					.toDispatch();
			// 所有表格
			Dispatch tables = Dispatch.get(doc2, "Tables").toDispatch();
			// 要填充的表格
			Dispatch table = Dispatch.call(tables, "Item",
					new Variant(tableIndex)).toDispatch();
			Dispatch range = Dispatch.get(table, "Range").toDispatch();
			Dispatch.call(range, "Copy");

			Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
			moveDown(1);
			Dispatch.call(textRange, "Paste");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (doc2 != null) {
				Dispatch.call(doc2, "Close", new Variant(saveOnExit));
				doc2 = null;
			}
		}
	}
	/**
	 * 得到当前文档的tables集合
	 */
	private Dispatch getTables() throws Exception {
		if (this.doc == null) {
			throw new Exception("there is not a document can't be operate!!!");
		}
		return Dispatch.get(doc, "Tables").toDispatch();
	}
	/**
	 * 得到当前文档的表格数
	 * @param Dispatch
	 */
	public int getTablesCount(Dispatch tables) throws Exception {
		int count = 0;
		try {
			this.getTables();
		} catch (Exception e) {
			throw new Exception("there is not any table!!");
		}
		count = Dispatch.get(tables, "Count").toInt();
		return count;
	}

	/**
	 * 在当前文档拷贝剪贴板数据
	 * 
	 * @param pos
	 */
	public void pasteExcelSheet(String pos) {
		goToBegin();
		Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
		Dispatch.call(textRange, "Paste");

	}

	/**
	 * 从选定内容或插入点开始查找文本
	 * 
	 * @param toFindText
	 *            要查找的文本
	 * @return boolean true-查找到并选中该文本，false-未查找到文本
	 */
	public boolean find(String toFindText) {
		if (toFindText == null || toFindText.equals(""))
			return false;
		// 从selection所在位置开始查询
		Dispatch find = word.call(selection, "Find").toDispatch();
		// 设置要查找的内容
		Dispatch.put(find, "Text", toFindText);
		// 向前查找
		Dispatch.put(find, "Forward", "True");
		// 设置格式
		Dispatch.put(find, "Format", "false");
		// 大小写匹配
		Dispatch.put(find, "MatchCase", "True");
		// 全字匹配
		Dispatch.put(find, "MatchWholeWord", "True");
		// 查找并选中
		return Dispatch.call(find, "Execute").getBoolean();
	}
	
	/**
	 * 执行某条宏指令
	 * 
	 * @param cmd
	 */
	private void cmd(String cmd) {
		Dispatch.call(selection, cmd);
	}

	public Dispatch getShapes() throws Exception {
		return Dispatch.get(doc, "Shapes").toDispatch();
	}

	public int getShapesCount() throws Exception {
		int count = 0;

		count = Dispatch.get(shapes, "Count").toInt();

		return count;
	}

	public Dispatch getShape(int tIndex) throws Exception {

		return Dispatch.call(shapes, "Item", new Variant(tIndex)).toDispatch();
		// return Dispatch.invoke(shapes,"item",Dispatch.Method,new
		// Object[]{
		// new Integer(tIndex)},new int[1]).toDispatch();
	}

	public Dispatch getTextFrame() throws Exception {
		return Dispatch.get(shape, "TextFrame").toDispatch();
	}

	public Dispatch getTextRange() throws Exception {
		return Dispatch.get(textframe, "TextRange").toDispatch();
	}
	
	/**
	 * 恢复默认字体 不加粗，不倾斜，没下划线，黑色，小四号字，宋体
	 */
	public void clearFont() {
		this.setFont(new WordFont());
	}
	
	/* 
	 * /** 复制表的最后一行到剪切板
	 * 
	 * @param tableIndex
	 */
	/*
	 * public void copyLastRow(int tableIndex) {
	 * getRow(getRowsCount(tableIndex)); Dispatch.call(row, "select");
	 * Dispatch.call(selection, "Copy"); }
	 * 
	 * /** 复制表的最后一行并粘贴到下一行（包括行中的数据）
	 * 
	 * @param tableIndex 表的索引 @param times 粘贴的次数
	 */
	/*
	 * public void duplicateLastRow(int tableIndex, int times) {
	 * this.copyLastRow(tableIndex); for (int i = 0; i < times; i++) {
	 * Dispatch.call(selection, "Paste"); } }
	 */
	 
	public Dispatch getRangeParagraphs() throws Exception {
		return Dispatch.get(range, "Paragraphs").toDispatch();
	}

	public Dispatch getParagraphRange() throws Exception {

		return Dispatch.get(paragraph, "range").toDispatch();
	}

	public String getRangeText() throws Exception {
		return Dispatch.get(range, "Text").toString();
	}


	public static void main(String[] args) {

		JacobWord j = new JacobWord();
		j.initCom();
		try {
			j.openDocument("C:\\ABC.doc");
			WordFont f=new WordFont();
			f.setSize("20");
			f.setBold(true);
			f.setColor(WordFont.COLOR_RED);

			
			//插入写入
			j.replaceAllText("${aaa}", "");
			j.write("设定方式的方式对方玩儿玩儿玩儿同一天与体育。",f);
			
			//结尾写入
			j.goToEnd();
			
			j.newParagraph(1,1);
			j.appendText("概要设计",f);
			
			
			j.newParagraph(0,1);
			f=new WordFont();
			f.setSize("12");
			f.setColor(WordFont.COLOR_BLACK);
			f.setName("楷体_GB2312");
			j.appendText("　　概要设计概要设计，概要设计概要设计概要设计。概要设计概要设计，概要设计概要设计概要设计。概要设计概要设计，概要设计概要设计概要设计。概要设计概要设计，概要设计概要设计概要设计。概要设计概要设计，概要设计概要设计概要设计。概要设计概要设计，概要设计概要设计概要设计。概要设计概要设计，概要设计概要设计概要设计。",f);
			
			j.newParagraph();
			j.clearFont();
			WordTable tb=j.appendTable(new String[]{"名称","类别","数量"});
			tb.addRow(new String[]{"名称","类别","数量"});
			
			j.goToBegin();
			j.moveRight(5);
			j.insertText("小兔乖乖");
			
			WordTable wt=j.getTable(1);
			System.out.println(wt.getCellString(1, 1));
			System.out.println(wt.getColumnsCount());
			System.out.println(wt.getRowsCount());
			wt.addRow(new String[]{"ww","类别","数量"});
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			j.closeDocument();
			j.close();
		}
		j.releaseCom();
	}
}
