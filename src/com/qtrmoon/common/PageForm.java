package com.qtrmoon.common;

import org.apache.struts.action.ActionForm;

/********************************************************************************************
 *  @author Javamao
 *	可以使用单字段排序，默认正序排列，可定义倒叙。
 *  例如：addOrderCol(colName);descOrder();
 *  也可使用多字段排序:
 *  例如按照colName1,colName2正序：
 *  addOrderCol(colName1);addOrderCol(colName2);
 *  再例如addOrderCol(colName1);addOrderCol(colName2);descOrder();
 *  此时为colName1正序，colName2倒叙，调用descOrder()后只会指定按照最后addOrderCol方法加入的字段倒叙。
 *  如果要指定多字段都倒叙可以使用：
 *  addOrderDescCol(colName1);addOrderDescCol(colName2);
 *  注意如果最后添加的排序字段使用了addOrderDescCol，又调用了ascOrder方法。
 *  则优先考虑addOrderDescCol而忽略ascOrder方法，即最后字段仍为倒叙排列。
 ********************************************************************************************/
public class PageForm extends ActionForm {
	// 总的数据条数0
	private int datasize = 0;

	// 请求的URL地址
	private String strUrl = "";

	// 查询条件
	private String condition = "";

	private String orderCol = ",";

	private String orderType = "";

	private int currentPage = 1;

	private int pagesize = 17;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getDatasize() {
		return datasize;
	}

	public void setDatasize(int datasize) {
		//查询时检查currentPage。删除末页所有记录后上翻一页。
		if(datasize<=(currentPage-1)*pagesize){
			currentPage--;
		}
		if(datasize>0&&currentPage==0){//首次进入currentPage=0的后续处理。
			currentPage=1;
		}
		this.datasize = datasize;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getPageNum(){
		int pageNum;
		if (datasize % pagesize > 0) {
			pageNum = datasize / pagesize + 1;
		} else {
			pageNum = datasize / pagesize;
		}
		if (pageNum == 0) {
			pageNum = 1;
		}
		return pageNum;
	}
	public String getStrUrl() {
		return strUrl;
	}

	public void setStrUrl(String strUrl) {
		this.strUrl = strUrl;
	}

	public String getOrderCol() {
		//如果末尾排列字段定义了反序，则清空orderType。
		if(!orderCol.equals(",")){
			String order = orderCol.substring(1, orderCol.length() - 1);
			if(!orderType.equals("")){
				if(order.lastIndexOf("DESC")==order.length()-4){
					orderType="";
				}
			}
			return orderCol.substring(1, orderCol.length() - 1);
		}
		return "";
	}

	public void addOrderCol(String ord) {
		if (ord != null && !ord.equals("")) {
			if (this.orderCol.indexOf("t." + ord + " ") < 0) {
				this.orderCol += "t." + ord + " ,";
			}
		}
	}

	public void setOrderCol(String ord) {
		if (ord != null && !ord.equals("")) {
			this.orderCol = ",t." + ord + " ,";
		}
	}
	
	public void addOrderDescCol(String ord) {
		if (ord != null && !ord.equals("")) {
			if (this.orderCol.indexOf("t." + ord + " ") < 0) {
				this.orderCol += "t." + ord + " DESC,";
			}
		}
	}

	public void clearOrderCol() {
		orderCol = ",";
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void ascOrder() {
		this.orderType = "ASC";
	}

	public void descOrder() {
		this.orderType = "DESC";
	}

	public void reverseOrder() {
		if (this.orderType.equals("ASC")) {
			this.orderType = "DESC";
		} else {
			this.orderType = "ASC";
		}
	}
}
