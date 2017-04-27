package com.qtrmoon.sysManage.bean;

public class RemindBean {

	private String id, link, ajaxlink, ico, text;

	/**
	 * @param id;
	 * @param link;//提醒打开页面地址
	 * @param ajaxlink;//ajax同步地址
	 * @param ico;//图片
	 * @param text;//文字
	 */
	public RemindBean(String id, String link, String ajaxlink, String ico,
			String text) {
		this.id = id;
		this.link = link;
		this.ajaxlink = ajaxlink;
		this.ico = ico;
		this.text = text;
	}

	public String getAjaxlink() {
		return ajaxlink;
	}

	public void setAjaxlink(String ajaxlink) {
		this.ajaxlink = ajaxlink;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
