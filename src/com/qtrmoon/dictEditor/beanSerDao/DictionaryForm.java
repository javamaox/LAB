package com.qtrmoon.dictEditor.beanSerDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

import com.qtrmoon.dictEditor.DictBuffer;
import com.qtrmoon.toolkit.tree.TreeNode;

/**
 * DictCatalog generated by MyEclipse - Hibernate Tools
 */

public class DictionaryForm extends TreeNode {

	// Fields

	private String id;

	private String label;

	private String pid;
	
	public Integer sort;
	
	public String exp;
	
	private FormFile file;

	// Constructors

	/** default constructor */
	public DictionaryForm() {
	}

	/** minimal constructor */
	public DictionaryForm(String id) {
		this.id = id;
	}

	/** full constructor */
	public DictionaryForm(String id, String label, String pid,Integer sort,String exp) {
		this(id,label,pid,sort);
		this.exp=exp;
	}
	public DictionaryForm(String id, String label, String pid,Integer sort) {
		this(id,label,pid);
		this.sort=sort;
	}
	public DictionaryForm(String id, String label, String pid) {
		this.id = id;
		this.label = label;
		this.pid = pid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id==null?id:id.trim();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid==null?pid:pid.trim();
	}

	public String getTreeId() {
		return this.id;
	}

	public String getTreePId() {
		return this.pid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
		super.order = sort;
	}
	
	/**
	 * ����һ���ֵ�ֵ 
	 * @param dictFrom Դ
	 * @param dictTo ���Ƶ������ڽ���һ��DictionaryForm�����ࡣ
	 * @return ���ظ��ƽ��
	 */
	public DictionaryForm clone(DictionaryForm dictTo) {
		if(dictTo==null){
			dictTo=new DictionaryForm();
		}
		dictTo.setId(id);
		dictTo.setPid(pid);
		dictTo.setLabel(label);
		dictTo.setOrder(order);
		return dictTo;
	}

	public List<DictionaryForm> getChildren(String dictId) {
		return DictBuffer.getChildDict(dictId,id);
	}
	
	public DictionaryForm getParent(String dictId) {
		return DictBuffer.getParentDict(dictId,id);
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public String getExp() {
		return exp==null?"":exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	
	
}