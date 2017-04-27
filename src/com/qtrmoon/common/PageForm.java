package com.qtrmoon.common;

import org.apache.struts.action.ActionForm;

/********************************************************************************************
 *  @author Javamao
 *	����ʹ�õ��ֶ�����Ĭ���������У��ɶ��嵹��
 *  ���磺addOrderCol(colName);descOrder();
 *  Ҳ��ʹ�ö��ֶ�����:
 *  ���簴��colName1,colName2����
 *  addOrderCol(colName1);addOrderCol(colName2);
 *  ������addOrderCol(colName1);addOrderCol(colName2);descOrder();
 *  ��ʱΪcolName1����colName2���𣬵���descOrder()��ֻ��ָ���������addOrderCol����������ֶε���
 *  ���Ҫָ�����ֶζ��������ʹ�ã�
 *  addOrderDescCol(colName1);addOrderDescCol(colName2);
 *  ע����������ӵ������ֶ�ʹ����addOrderDescCol���ֵ�����ascOrder������
 *  �����ȿ���addOrderDescCol������ascOrder������������ֶ���Ϊ�������С�
 ********************************************************************************************/
public class PageForm extends ActionForm {
	// �ܵ���������0
	private int datasize = 0;

	// �����URL��ַ
	private String strUrl = "";

	// ��ѯ����
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
		//��ѯʱ���currentPage��ɾ��ĩҳ���м�¼���Ϸ�һҳ��
		if(datasize<=(currentPage-1)*pagesize){
			currentPage--;
		}
		if(datasize>0&&currentPage==0){//�״ν���currentPage=0�ĺ�������
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
		//���ĩβ�����ֶζ����˷��������orderType��
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
