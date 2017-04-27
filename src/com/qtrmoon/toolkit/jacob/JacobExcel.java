package com.qtrmoon.toolkit.jacob;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobExcel {

	// word���г������
	private ActiveXComponent xl = null;

	// ����word�ĵ�����
	private Dispatch workbooks = null;
	private Dispatch workbook = null;

	// ѡ���ķ�Χ������
	private static Dispatch activeCell = null;

	// �����Ƿ񱣴����˳��ı�־
	private boolean saveOnExit = true;

	/** ���б�� */
	private Dispatch sheets;
	
	public JacobExcel() {
		if (xl == null) {
			xl = new ActiveXComponent("Excel.Application");
			xl.setProperty("Visible", new Variant(false));
		}
		if (workbooks == null) {
			workbooks = xl.getProperty("Workbooks").toDispatch();
		}
	}
	
	/****************************************
	 ************ �ĵ���������ʼ�� *************
	 ****************************************/
	
	/**
	 * ����һ���µ�Excel�ĵ�
	 */
	public void createNewDocument() {
		if (this.workbook != null) {
			this.closeDocument();
		}
		workbook = Dispatch.call(workbooks, "Add").toDispatch();
	}
	/**
	 * �رյ�ǰExcel�ĵ�
	 */
	public void closeDocument() {
		if (workbook != null) {
			Dispatch.call(workbook, "Save");
			Dispatch.call(workbook, "Close", new Variant(saveOnExit));
			workbook = null;
		}
	}
	/**
	 * ��һ���Ѵ��ڵ��ĵ�
	 * @param docPath
	 */
	public void openDocument(String docPath,boolean readonly) {
		if (this.workbook != null) {
			this.closeDocument();
		}
		workbook = Dispatch.invoke(workbooks,"Open",Dispatch.Method, 
                                    new Object[]{docPath,new Variant(false),new Variant(readonly)},//�Ƿ���ֻ����ʽ��
                                    new int[1]).toDispatch();
	}

	/**
	 * �ļ���������Ϊ
	 * @param savePath
	 *            һ��Ҫ�ǵü�����չ�� .doc ��������Ϊ·��
	 */
	public void save(String savePath) {
		Dispatch.call(
				(Dispatch) Dispatch.call(xl, "ExcelBasic").getDispatch(),
				"FileSaveAs", savePath);
	}
	


	/**
	 * �ر�ȫ��Ӧ��
	 */
	public void close() {
		closeDocument();
		if (xl != null) {
			Dispatch.call(xl, "Quit");
			xl = null;
		}
		workbooks = null;
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
	
	/**
	 * ���Sheet
	 * @return
	 */
	public Dispatch addSheet(){
		return Dispatch.get(Dispatch.get(workbook,"sheets").toDispatch(),"add").toDispatch();
	}
	/**
	 * ��ȡ��ǰsheet����
	 * @return
	 */
	public String getCurrentSheetName(){
		return Dispatch.get(getCurrentSheet(),"name").toString();
	}
	/**
	 * ��ȡ����sheet
	 * @return
	 */
	public Dispatch getSheets(){
		if(sheets==null){
			sheets=Dispatch.get(workbook,"sheets").toDispatch();
		}
		return sheets;
	}
	/**
	 * ��ȡ��ǰsheet
	 * @return
	 */
	public Dispatch getCurrentSheet(){
		return Dispatch.get(workbook,"ActiveSheet").toDispatch();
	}
	/**
	 * �����ֻ�ȡsheet
	 * @param name
	 * @return
	 */
	public Dispatch getSheetByName(String name){
		return Dispatch.invoke(getSheets(),"Item",Dispatch.Get,new Object[]{name},new int[1]).toDispatch();
	}
	/**
	 * ͨ��������ȡsheet
	 * @param index
	 * @return
	 */
	public Dispatch getSheetByIndex(Integer index){
		return Dispatch.invoke(getSheets(), "Item", Dispatch.Get, new Object[]{index}, new int[1]).toDispatch();
	}
	/**
	 * ��ȡsheet����
	 * @return
	 */
	public int getSheetCount(){
		return Dispatch.get(getSheets(),"count").toInt();
	}
	/****************************************
	 ************ �ĵ��༭ *************
	 ****************************************/
	/**
	 * ����ֵ
	 * @param sheet sheet����
	 * @param position ��Ԫ��λ����C1
	 * @param type ֵ������"value"
	 * @param value ֵ��"123"
	 */
	public void setValue(Dispatch sheet,String position,String type,Object value){
		Dispatch cell=Dispatch.invoke(sheet,"Range", Dispatch.Get, new Object[]{position}, new int[1]).toDispatch();
		Dispatch.put(cell,type,value);
	}
	// д��ֵ
	public void setValue(String position, String type, String value) {
		Dispatch cell = Dispatch.invoke(getCurrentSheet(), "Range",Dispatch.Get,new Object[] {position},new int[1]).toDispatch();
		Dispatch.put(cell, type, value);
	}

	// ��ȡֵ
	public String getValue(String position) {
		Dispatch cell = Dispatch.invoke(getCurrentSheet(), "Range", Dispatch.Get,new Object[] { position }, new int[1]).toDispatch();
		String value = Dispatch.get(cell, "Value").toString();
		return value;
	}
	public String getValue(Dispatch sheet,String position) {
		Dispatch cell = Dispatch.invoke(sheet, "Range", Dispatch.Get,new Object[] { position }, new int[1]).toDispatch();
		String value = Dispatch.get(cell, "Value").toString();
		return value;
	}
	
	public static void main(String[] args) {

		JacobExcel j = new JacobExcel();
		j.initCom();
		try {
			j.openDocument("C:\\ABC.xls",false);
			j.setValue("Sheet3!C7", "Value", "С�ùԹ�");
			System.out.println(j.getValue("C3"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			j.closeDocument();
			j.close();
		}
		j.releaseCom();
	}
}
