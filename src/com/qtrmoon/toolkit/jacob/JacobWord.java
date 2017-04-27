package com.qtrmoon.toolkit.jacob;

import java.awt.event.KeyEvent;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.jacob.com.ComThread;

public class JacobWord extends BasicOp{
	//	 word�ĵ�
	private Dispatch doc = null;

	// word���г������
	private ActiveXComponent word = null;

	// ����word�ĵ�����
	private Dispatch documents = null;

	// ѡ���ķ�Χ������
	private static Dispatch selection = null;
	
	// �����Ƿ񱣴����˳��ı�־
	private boolean saveOnExit = true;

	/** ���б�� */
	private Dispatch tables;

	/** ��ǰ��� */
	private Dispatch table;

	/**
	 * ��wordʱͬʱҪ�򿪵��ĵ�����ָ��ʱ���½�һ���հ��ĵ�
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
	 ************ �ĵ���������ʼ�� *************
	 ****************************************/
	
	/**
	 * ����һ���µ�word�ĵ�
	 */
	public void createNewDocument() {
		doc = Dispatch.call(documents, "Add").toDispatch();
		selection = Dispatch.get(word, "Selection").toDispatch();
	}

	/**
	 * ��һ���Ѵ��ڵ��ĵ�
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
	 * �ļ���������Ϊ
	 * @param savePath
	 *            һ��Ҫ�ǵü�����չ�� .doc ��������Ϊ·��
	 */
	public void save(String savePath) {
		Dispatch.call(
				(Dispatch) Dispatch.call(word, "WordBasic").getDispatch(),
				"FileSaveAs", savePath);
	}
	
	/**
	 * �رյ�ǰword�ĵ�
	 */
	public void closeDocument() {
		if (doc != null) {
			Dispatch.call(doc, "Save");
			Dispatch.call(doc, "Close", new Variant(saveOnExit));
			doc = null;
		}
	}

	/**
	 * �ر�ȫ��Ӧ��
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
	 * ��ʼ�� com �߳�
	 */
	public void initCom() {
		ComThread.InitSTA();
	}

	/**
	 * �ͷ� com �߳���Դ com ���̻߳��ղ��� java �������ջ��ƻ���
	 */
	public void releaseCom() {
		ComThread.Release();
	}
	/****************************************
	 ************ ����д�� *************
	 ****************************************/
	/**
	 * �ļ�β׷��д���ı�
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
	 * �ڵ�ǰ���������ַ���
	 * @param newText Ҫ��������ַ���
	 */
	public void insertText(String newText) {
		insertText(newText,null);
	}
	public void insertText(String newText,WordFont font) {
		write(newText,font);
	}
	
	/**
	 * ��ѡ��ѡ�������趨Ϊ�滻�ı�
	 * @param toFindText �����ַ���
	 * @param newText Ҫ�滻������
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
	 * ȫ���滻�ı�
	 * @param toFindText �����ַ���
	 * @param newText Ҫ�滻������
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
	 * ����<br>
	 */
	public void newParagraph() {
		cmd("TypeParagraph");
	}
	public void newParagraph(int align, double lineSpace) {
		cmd("TypeParagraph");
		setParaFormat(align,lineSpace);
	}
	/**
	 * �Ե�ǰ������и�ʽ��
	 * 
	 * @param align �������з�ʽ Ĭ�ϣ�����0:���� 1:���� 2:���� 3:���˶��� 4:��ɢ����
	 * @param lineSpace �����м�� Ĭ�ϣ�1.0��0��1.0 1��1.5 2��2.0 3����Сֵ 4���̶�ֵ
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
	 * ��ԭ����Ĭ�ϵĸ�ʽ �����,�м�ࣺ1.0
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
	 * ��ӡ��ǰword�ĵ�
	 * 
	 * @throws Exception
	 *             com.jacob.com.ComFailException: Invoke of: PrintOut Source:
	 *             Microsoft Word ���޴�ӡ��
	 */
	public void printFile() {
		if (doc != null) {
			Dispatch.call(doc, "PrintOut");
		}
	}

	/****************************************
	 ************ ͼƬд�� *************
	 ****************************************/
	/**
	 * �ڵ�ǰ��괦���ͼƬ
	 * 
	 * @param imgPath
	 *            ͼƬ�ĵ�ַ
	 */
	public void addImage(String imgPath) {
		if (imgPath != "" && imgPath != null) {
			Dispatch image = Dispatch.get(selection, "InLineShapes")
					.toDispatch();
			Dispatch.call(image, "AddPicture", imgPath);
		}
	}
	/**
	 * �ڵ�ǰ��������ͼƬ
	 * 
	 * @param imagePath
	 *            ͼƬ·��
	 */
	public void insertImage(String imagePath) {
		Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(),
				"AddPicture", imagePath);
	}
	
	/**
	 * 
	 * @param toFindText
	 *            Ҫ���ҵ��ַ���
	 * @param imagePath
	 *            ͼƬ·��
	 * @return �˺������ַ����滻��ͼƬ
	 */
	public boolean replaceImage(String toFindText, String imagePath) {
		if (!find(toFindText))
			return false;
		Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(),
				"AddPicture", imagePath);
		return true;
	}

	/**
	 * ȫ���滻ͼƬ
	 * 
	 * @param toFindText
	 *            �����ַ���
	 * @param imagePath
	 *            ͼƬ·��
	 */
	public void replaceAllImage(String toFindText, String imagePath) {
		while (find(toFindText)) {
			Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(),
					"AddPicture", imagePath);
			Dispatch.call(selection, "MoveRight");
		}
	}
	
	/**
	 * �ڵ�ǰ�ĵ�ָ����λ�ÿ���������һ���ĵ��е�ͼƬ
	 * 
	 * @param anotherDocPath
	 *            ��һ���ĵ��Ĵ���·��
	 * @param shapeIndex
	 *            ��������ͼƬ����һ���ĵ��е�λ��
	 * @param pos
	 *            ��ǰ�ĵ�ָ����λ��
	 * @throws com.jacob.com.ComFailException
	 *             Invoke of: Item Source: Microsoft Word ��shapeIndex������
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
	 ************ ������ *************
	 ****************************************/
	/**
	 * �����񣬴��ĵ���ʼ������posƥ����ַ�������λ���״�ƥ��Ĵ���
	 * @param pos λ��
	 * @param heads ��ͷ
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
	 * �ı��滻Ϊ��񣬴��ĵ���ʼ������posƥ����ַ�������λ���״�ƥ��Ĵ���
	 * @param pos λ��
	 * @param heads ��ͷ
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
	 * ���ļ�β׷�ӱ��
	 * @param heads ��ͷ
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
	 * ���ұ�
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
	 * �ڵ�ǰ�ĵ�ָ����λ�ÿ���������һ���ĵ��еı��
	 * 
	 * @param anotherDocPath
	 *            ��һ���ĵ��Ĵ���·��
	 * @param tableIndex
	 *            �������ı������һ���ĵ��е�λ��
	 * @param pos
	 *            ��ǰ�ĵ�ָ����λ��
	 */
	public void copyTableFromAnotherDoc(String anotherDocPath, int tableIndex,String pos) {
		Dispatch doc2 = null;
		try {
			doc2 = Dispatch.call(documents, "Open", anotherDocPath)
					.toDispatch();
			// ���б��
			Dispatch tables = Dispatch.get(doc2, "Tables").toDispatch();
			// Ҫ���ı��
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
	 * �õ���ǰ�ĵ���tables����
	 */
	private Dispatch getTables() throws Exception {
		if (this.doc == null) {
			throw new Exception("there is not a document can't be operate!!!");
		}
		return Dispatch.get(doc, "Tables").toDispatch();
	}
	/**
	 * �õ���ǰ�ĵ��ı����
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
	 * �ڵ�ǰ�ĵ���������������
	 * 
	 * @param pos
	 */
	public void pasteExcelSheet(String pos) {
		goToBegin();
		Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
		Dispatch.call(textRange, "Paste");

	}

	/**
	 * ��ѡ�����ݻ����㿪ʼ�����ı�
	 * 
	 * @param toFindText
	 *            Ҫ���ҵ��ı�
	 * @return boolean true-���ҵ���ѡ�и��ı���false-δ���ҵ��ı�
	 */
	public boolean find(String toFindText) {
		if (toFindText == null || toFindText.equals(""))
			return false;
		// ��selection����λ�ÿ�ʼ��ѯ
		Dispatch find = word.call(selection, "Find").toDispatch();
		// ����Ҫ���ҵ�����
		Dispatch.put(find, "Text", toFindText);
		// ��ǰ����
		Dispatch.put(find, "Forward", "True");
		// ���ø�ʽ
		Dispatch.put(find, "Format", "false");
		// ��Сдƥ��
		Dispatch.put(find, "MatchCase", "True");
		// ȫ��ƥ��
		Dispatch.put(find, "MatchWholeWord", "True");
		// ���Ҳ�ѡ��
		return Dispatch.call(find, "Execute").getBoolean();
	}
	
	/**
	 * ִ��ĳ����ָ��
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
	 * �ָ�Ĭ������ ���Ӵ֣�����б��û�»��ߣ���ɫ��С�ĺ��֣�����
	 */
	public void clearFont() {
		this.setFont(new WordFont());
	}
	
	/* 
	 * /** ���Ʊ�����һ�е����а�
	 * 
	 * @param tableIndex
	 */
	/*
	 * public void copyLastRow(int tableIndex) {
	 * getRow(getRowsCount(tableIndex)); Dispatch.call(row, "select");
	 * Dispatch.call(selection, "Copy"); }
	 * 
	 * /** ���Ʊ�����һ�в�ճ������һ�У��������е����ݣ�
	 * 
	 * @param tableIndex ������� @param times ճ���Ĵ���
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

			
			//����д��
			j.replaceAllText("${aaa}", "");
			j.write("�趨��ʽ�ķ�ʽ�Է����������ͬһ����������",f);
			
			//��βд��
			j.goToEnd();
			
			j.newParagraph(1,1);
			j.appendText("��Ҫ���",f);
			
			
			j.newParagraph(0,1);
			f=new WordFont();
			f.setSize("12");
			f.setColor(WordFont.COLOR_BLACK);
			f.setName("����_GB2312");
			j.appendText("������Ҫ��Ƹ�Ҫ��ƣ���Ҫ��Ƹ�Ҫ��Ƹ�Ҫ��ơ���Ҫ��Ƹ�Ҫ��ƣ���Ҫ��Ƹ�Ҫ��Ƹ�Ҫ��ơ���Ҫ��Ƹ�Ҫ��ƣ���Ҫ��Ƹ�Ҫ��Ƹ�Ҫ��ơ���Ҫ��Ƹ�Ҫ��ƣ���Ҫ��Ƹ�Ҫ��Ƹ�Ҫ��ơ���Ҫ��Ƹ�Ҫ��ƣ���Ҫ��Ƹ�Ҫ��Ƹ�Ҫ��ơ���Ҫ��Ƹ�Ҫ��ƣ���Ҫ��Ƹ�Ҫ��Ƹ�Ҫ��ơ���Ҫ��Ƹ�Ҫ��ƣ���Ҫ��Ƹ�Ҫ��Ƹ�Ҫ��ơ�",f);
			
			j.newParagraph();
			j.clearFont();
			WordTable tb=j.appendTable(new String[]{"����","���","����"});
			tb.addRow(new String[]{"����","���","����"});
			
			j.goToBegin();
			j.moveRight(5);
			j.insertText("С�ùԹ�");
			
			WordTable wt=j.getTable(1);
			System.out.println(wt.getCellString(1, 1));
			System.out.println(wt.getColumnsCount());
			System.out.println(wt.getRowsCount());
			wt.addRow(new String[]{"ww","���","����"});
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			j.closeDocument();
			j.close();
		}
		j.releaseCom();
	}
}
