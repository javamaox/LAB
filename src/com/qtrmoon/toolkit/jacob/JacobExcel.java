package com.qtrmoon.toolkit.jacob;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobExcel {

	// word运行程序对象
	private ActiveXComponent xl = null;

	// 所有word文档集合
	private Dispatch workbooks = null;
	private Dispatch workbook = null;

	// 选定的范围或插入点
	private static Dispatch activeCell = null;

	// 设置是否保存后才退出的标志
	private boolean saveOnExit = true;

	/** 所有表格 */
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
	 ************ 文档创建及初始化 *************
	 ****************************************/
	
	/**
	 * 创建一个新的Excel文档
	 */
	public void createNewDocument() {
		if (this.workbook != null) {
			this.closeDocument();
		}
		workbook = Dispatch.call(workbooks, "Add").toDispatch();
	}
	/**
	 * 关闭当前Excel文档
	 */
	public void closeDocument() {
		if (workbook != null) {
			Dispatch.call(workbook, "Save");
			Dispatch.call(workbook, "Close", new Variant(saveOnExit));
			workbook = null;
		}
	}
	/**
	 * 打开一个已存在的文档
	 * @param docPath
	 */
	public void openDocument(String docPath,boolean readonly) {
		if (this.workbook != null) {
			this.closeDocument();
		}
		workbook = Dispatch.invoke(workbooks,"Open",Dispatch.Method, 
                                    new Object[]{docPath,new Variant(false),new Variant(readonly)},//是否以只读方式打开
                                    new int[1]).toDispatch();
	}

	/**
	 * 文件保存或另存为
	 * @param savePath
	 *            一定要记得加上扩展名 .doc 保存或另存为路径
	 */
	public void save(String savePath) {
		Dispatch.call(
				(Dispatch) Dispatch.call(xl, "ExcelBasic").getDispatch(),
				"FileSaveAs", savePath);
	}
	


	/**
	 * 关闭全部应用
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
	
	/**
	 * 添加Sheet
	 * @return
	 */
	public Dispatch addSheet(){
		return Dispatch.get(Dispatch.get(workbook,"sheets").toDispatch(),"add").toDispatch();
	}
	/**
	 * 获取当前sheet名字
	 * @return
	 */
	public String getCurrentSheetName(){
		return Dispatch.get(getCurrentSheet(),"name").toString();
	}
	/**
	 * 获取所有sheet
	 * @return
	 */
	public Dispatch getSheets(){
		if(sheets==null){
			sheets=Dispatch.get(workbook,"sheets").toDispatch();
		}
		return sheets;
	}
	/**
	 * 获取当前sheet
	 * @return
	 */
	public Dispatch getCurrentSheet(){
		return Dispatch.get(workbook,"ActiveSheet").toDispatch();
	}
	/**
	 * 按名字获取sheet
	 * @param name
	 * @return
	 */
	public Dispatch getSheetByName(String name){
		return Dispatch.invoke(getSheets(),"Item",Dispatch.Get,new Object[]{name},new int[1]).toDispatch();
	}
	/**
	 * 通过索引获取sheet
	 * @param index
	 * @return
	 */
	public Dispatch getSheetByIndex(Integer index){
		return Dispatch.invoke(getSheets(), "Item", Dispatch.Get, new Object[]{index}, new int[1]).toDispatch();
	}
	/**
	 * 获取sheet个数
	 * @return
	 */
	public int getSheetCount(){
		return Dispatch.get(getSheets(),"count").toInt();
	}
	/****************************************
	 ************ 文档编辑 *************
	 ****************************************/
	/**
	 * 设置值
	 * @param sheet sheet对象
	 * @param position 单元格位置如C1
	 * @param type 值属性如"value"
	 * @param value 值如"123"
	 */
	public void setValue(Dispatch sheet,String position,String type,Object value){
		Dispatch cell=Dispatch.invoke(sheet,"Range", Dispatch.Get, new Object[]{position}, new int[1]).toDispatch();
		Dispatch.put(cell,type,value);
	}
	// 写入值
	public void setValue(String position, String type, String value) {
		Dispatch cell = Dispatch.invoke(getCurrentSheet(), "Range",Dispatch.Get,new Object[] {position},new int[1]).toDispatch();
		Dispatch.put(cell, type, value);
	}

	// 读取值
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
			j.setValue("Sheet3!C7", "Value", "小兔乖乖");
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
