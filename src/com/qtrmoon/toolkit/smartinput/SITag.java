package com.qtrmoon.toolkit.smartinput;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class SITag extends TagSupport {
	private String name;
	private String table;
	private String colsch;
	private String colback;
	private String feedback;
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter out;
		try {
			out = pageContext.getOut();
			out.print("<input type='text' style='width:400px;' class='smartInput' name='"+name+"' "+
					"table='"+table+"' colsch='"+colsch+"' colback='"+colback+"' feedback='"+feedback+"'"+
					" onkeyup='smartInput_nameChange(this)'/>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getColback() {
		return colback;
	}

	public void setColback(String colback) {
		this.colback = colback;
	}

	public String getColsch() {
		return colsch;
	}

	public void setColsch(String colsch) {
		this.colsch = colsch;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	
	
}
