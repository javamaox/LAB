package com.qtrmoon.toolkit.report;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts.actions.DispatchAction;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 基于itext的报表代理类
 * @author Javamao 报表代理类
 */
public class ReportUtil extends DispatchAction {

	/**
	 * Excel报表
	 * 
	 * @param headList
	 *            表头项List。(Elem is the table's head)
	 * @param dataList
	 *            数据List,List的每个元素是一个字符串List。(Elem is a String[] of data.)
	 * @param response
	 * @return int
	 */
	public static int excelReport(String tableTitle, List<String> headList,
			List<List<String>> dataList, HttpServletResponse response) {
		try {
			// 设定相应头(set response head, get file by attachment.)
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(tableTitle,"UTF-8")+".xls");
			ServletOutputStream stream = response.getOutputStream();

			// 1使用工作簿工厂建立工作簿(create workbook with Workbook factory.)
			WritableWorkbook workbook = Workbook.createWorkbook(stream);

			// 2建立工作表(create sheet with workbook.)
			WritableSheet sheet = workbook.createSheet(tableTitle, 0);

			// 3添加Lable类型cell,创建带有样式的Cell.
			// 也可添加数字类型cell
			// sheet.addCell(new Number(1, i + 2, user.getAge()));
			jxl.write.WritableFont wfc = new jxl.write.WritableFont(
					WritableFont.ARIAL, 10, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.RED);
			jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(
					wfc);
			sheet.addCell(new jxl.write.Label(0, 0, tableTitle, wcfFC));
			jxl.write.WritableCellFormat headFmt = new jxl.write.WritableCellFormat(
					new jxl.write.WritableFont(WritableFont.ARIAL, 10,
							WritableFont.BOLD, false,
							UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK));
			for (int i = 0; i < headList.size(); i++) {
				sheet.addCell(new jxl.write.Label(i, 1, headList.get(i).toString(),headFmt));
			}
			List<String> record;
			for (int i = 0; i < dataList.size(); i++) {
				record = dataList.get(i);
				for (int j = 0; j < record.size(); j++) {
					sheet.addCell(new jxl.write.Label(j, i + 2, record.get(j)));
				}
			}
			// 写入Excel工作表，输出缓存。
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	/* Pdf报表 */
	public static int pdfReport(String tableTitle, List<String> headList,
			List<List<String>> dataList, HttpServletResponse response) {
		try {
			// 设定相应头
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment;");
			ServletOutputStream stream = response.getOutputStream();
			// 创建一个文档对象
			Document doc = new Document(PageSize.A4);
			// 定义输出位置并把文档对象装入输出对象中
			PdfWriter.getInstance(doc, response.getOutputStream());

			doc.open();

			doc.add(new Paragraph("HelloWorld"));

			Table table = new Table(headList.size(), dataList.size());
			table.setBorderWidth(0.2F);
			table.setGrayFill(0.95F);// 填充灰度0黑1白。
			table.setPadding(1.5F);// cell内部的填充。
			// table.setSpacing(0.5F);// cell之间的填充。

			Cell cell;
			for (int i = 0; i < headList.size(); i++) {
				cell = new Cell(headList.get(i).toString());
				cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
				cell.setVerticalAlignment(Cell.ALIGN_CENTER);
				table.addCell(cell);
			}
			List<String> record;
			for (int i = 0; i < dataList.size(); i++) {
				record = dataList.get(i);
				for (int j = 0; j < record.size(); j++) {
					cell = new Cell(record.get(j));
					cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
					cell.setVerticalAlignment(Cell.ALIGN_TOP);
					table.addCell(cell);
				}
			}
			// 添加图片的例子
			String path = ReportUtil.class.getResource("/").getPath();
			path = path.replaceAll("%20", " ");
			path = path + "com/javamao/resource/";
			Image jpeg = Image.getInstance(path + "pic.jpg");
			// 图片居中
			jpeg.setAlignment(Image.ALIGN_CENTER);
			doc.add(jpeg);

			doc.add(table);
			doc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}
}
