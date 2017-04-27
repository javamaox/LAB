package com.qtrmoon.toolkit.jxl;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class JxlExcel {
	private WritableWorkbook workbook;
	private WritableCellFormat cellFormat=new WritableCellFormat(new WritableFont(
			WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
			UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
	private int currrow=0;
	private WritableSheet currsheet;
	
	public WritableWorkbook open(File file) throws IOException {
		// 1使用工作簿工厂建立工作簿(create workbook with Workbook factory.)
		workbook = Workbook.createWorkbook(file);
		return workbook;
	}
	public WritableWorkbook open(String filePath) throws IOException {
		workbook = open(new File(filePath));
		return workbook;
	}
	
	public WritableSheet newSheet(String sheetname,int index) {
		WritableSheet sheet=null;
		// 2建立工作表(create sheet with workbook.)
		sheet = workbook.createSheet(sheetname, index);
		currrow=0;
		return sheet;
	}
	
	public WritableSheet getSheet(String sheetname) {
		return workbook.getSheet(sheetname);
	}
	public WritableSheet getSheet(int index) {
		return workbook.getSheet(index);
	}
	
	public WritableSheet newSheet(String sheetname) {
		currsheet=newSheet(sheetname,workbook.getSheets().length);
		return currsheet;
	}
	
	public void newCellFormat(int fontsize,String color) {
		WritableFont wfc = new jxl.write.WritableFont(
				WritableFont.ARIAL, fontsize, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		cellFormat = new WritableCellFormat(wfc);
	}
	
	public void setValue(String value,WritableSheet sheet,int row,int col) throws WriteException, IOException {
		// 3添加Lable类型cell,创建带有样式的Cell.
		// 也可添加数字类型cell
		// sheet.addCell(new Number(1, i + 2, user.getAge()));
		sheet.addCell(new jxl.write.Label(col, row, value, cellFormat));
	}
	public void setValue(String value,String sheetname,int row,int col) throws WriteException, IOException {
		setValue(value,getSheet(sheetname),row,col);
	}
	public void addLine(String[] vals,String sheetname) throws WriteException, IOException{
		int col=0;
		for(String val:vals){
			setValue(val,getSheet(sheetname),currrow,col++);
		}
	}
	public void addLine(String[] vals) throws WriteException, IOException{
		int col=0;
		for(String val:vals){
			setValue(val,currsheet,currrow,col++);
		}
		currrow++;
	}
	public void close() throws IOException, WriteException {
		workbook.write();
		workbook.close();
	}
	
	
	public static void main(String args[]){
		JxlExcel jxlExcel=new JxlExcel();
		try {
			jxlExcel.open("c:\\aaa.xls");
			jxlExcel.newSheet("sheetOne");
			jxlExcel.addLine(new String[]{"姓名:","","性别",""});
			jxlExcel.addLine(new String[]{"姓名:","","性别",""});
			jxlExcel.addLine(new String[]{"姓名:","","性别",""});
			jxlExcel.newSheet("sheetTwo");
			jxlExcel.addLine(new String[]{"姓名:","","性别",""});
			jxlExcel.addLine(new String[]{"姓名:","","性别",""});
			jxlExcel.addLine(new String[]{"姓名:","","性别",""});
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (WriteException e2) {
			e2.printStackTrace();
		} finally{
			try {
				jxlExcel.close();
			} catch (WriteException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
