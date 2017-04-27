package com.qtrmoon.tagLib;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.common.PageForm;

public class PageSeparate extends TagSupport {
	private String url;
	private PageForm pageForm;

	private int pageSize=17;

	private int pageNum;

	private int dataNum;

	private int currentPage;

	private String style;//default:button,0,1/block,2
	
	private String manualPageSize="false";

	public int doStartTag() {
		if(pageForm!=null){
			pageSize = pageForm.getPagesize();
			dataNum = pageForm.getDatasize();
			currentPage = pageForm.getCurrentPage();
		}
		JspWriter out = pageContext.getOut();
		if (dataNum % pageSize > 0) {
			pageNum = dataNum / pageSize + 1;
		} else {
			pageNum = dataNum / pageSize;
		}
		if (pageNum == 0) {
			pageNum = 1;
		}
		if(currentPage==0){
			currentPage=1;
		}
		try {
//			if(pageNum>1){
				if("block".equals(style)||"2".equals(style)){
					out.println(getCodeBlock());
				}else{
					out.println(getCode());
				}
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

	private CharSequence getCodeBlock() {
		StringBuffer res=new StringBuffer();
		res.append("<div class='blockPageSep'>");
		if (currentPage > 1) {
			res.append("<a href=\""+url+"&currentPage=1&from=page\" title='首页'>|&lt;&lt;</a> ");
			res.append("<a href=\""+url+"&currentPage="+(currentPage - 1)+"&from=page\" title='上一页'>&lt;&lt;&lt;</a>");
		} else {
			res.append("<a href='javascript:void(0)' title='首页'>|&lt;&lt;</a> ");
			res.append("<a href='javascript:void(0)' title='上一页'>&lt;&lt;&lt;</a>");
		}
		int start, end;
		String startStr = "", endStr = "";
		int width = 10;//页宽度
		if (pageNum <= width) {
			//页码小于跨度，跨度为(1,pageNum)
			start = 1;
			end = pageNum;
		} else {
			if (currentPage > width / 2) {
				//页码大于跨度，游标大于半跨度，左限开始增加
				if (currentPage < (pageNum - width / 2)) {
					//游标未进入右半跨度，左半有足够空间做减。跨度为(curr-w/2,curr+w/2)
					start = currentPage - width / 2;
					end = currentPage + width / 2;
				} else {
					//游标进入右半跨度，不左右界再加，跨度以pageNum为终点，为(pNum-width,pNum)
					start = pageNum - width;
					end = pageNum;
				}
			} else {
				//页码大于跨度，游标小于半跨，左右不加，跨度以1为起点，为(1,width)
				start = 1;
				end = width;
			}
		}
		if (start > 1) {
			startStr = "...";
		}
		if (end < pageNum) {
			endStr = "...";
		}
		res.append(startStr + " ");
		for (int i = start; i <= end; i++) {
			if(i==currentPage){
				res.append("<a href='"+url+"&currentPage="+i+"&from=page' class='blockPageSep_selected'>&nbsp;" + i+ "&nbsp;</a>");
			}else{
				res.append("<a href='"+url+"&currentPage="+i+"&from=page' class='blockPageSep_normal'>&nbsp;" + i+ "&nbsp;</a>");
			}
		}
		res.append(endStr + " ");
		if (currentPage < pageNum) {
			res.append("<a href=\""+url+ "&currentPage="+ (currentPage + 1)+"&from=page\" title='下一页'>&gt;&gt;&gt;</a> ");
			res.append("<a href=\""+url+ "&currentPage="+ pageNum+"&from=page\" title='末页'>&gt;&gt;|</a>");
		} else {
			res.append("<a href='javascript:void(0)' title='下一页'>&gt;&gt;&gt;</a> ");
			res.append("<a href='javascript:void(0)' title='末页'>&gt;&gt;|</a>");
		}
		res.append("&nbsp;" + currentPage + "/" + pageNum + "页");
		res.append("</div>");
		return res.toString();
	}

	private String getCode() {
		StringBuffer res=new StringBuffer();
		res.append("<script>");
		res.append("function checkPage(frm){");
		res.append("	var currentPage = frm.currentPage.value;");
		res.append("	if(!/^\\d{1,}$/.test(currentPage)){");
		res.append("		alert('请填写数字!');");
		res.append("		return false;");
		res.append("	}");
		res.append("	if(currentPage<1||currentPage>" + pageNum + "){");
		res.append("		alert('所选择页码越界！');");
		res.append("		return false;");
		res.append("	}");
		res.append("	return true;");
		res.append("}");
		res.append("</script>");
		res.append("<div class='updownPageSep'>"
						+ "<form action=\""
						+ url
						+ "&from=page\" method='post' onsubmit='return checkPage(this)'>");

		if (currentPage > 1) {
			res.append("<input type='button' value='上 页' onclick=\"document.location='"
							+ url
							+ "&currentPage="
							+ (currentPage - 1)
							+ "&from=page'\"/>");
		} else {
			res.append("<input type='button' value='上 页' disabled='disabled'/>");
		}
		if (currentPage < pageNum) {
			res.append("<input type='button' value='下 页' onclick=\"document.location='"
							+ url
							+ "&currentPage="
							+ (currentPage + 1)
							+ "&from=page'\"/>");
		} else {
			res.append("<input type='button' value='下 页' disabled='disabled'/>");
		}
		res.append(" 跳转<input type='text' size='3' name='currentPage' value='"+currentPage+"'/>");
		if("true".equals(manualPageSize)){
			res.append(" 每页<input type='text' size='3' name='pagesize' value='"+pageSize+"'/>");
		}
		res.append("<input type='submit' value='Go'/>");
		res.append("&nbsp;第" + currentPage + "/" + pageNum + "页 共"+dataNum+"条");
		res.append("</form></div>");
		return res.toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getDataNum() {
		return dataNum;
	}

	public void setDataNum(int dataNum) {
		this.dataNum = dataNum;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public PageForm getPageForm() {
		return pageForm;
	}

	public void setPageForm(PageForm pageForm) {
		this.pageForm = pageForm;
	}

	public String getManualPageSize() {
		return manualPageSize;
	}

	public void setManualPageSize(String manualPageSize) {
		this.manualPageSize = manualPageSize;
	}



}
