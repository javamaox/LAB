package com.qtrmoon.sysManage.bean;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import com.qtrmoon.common.PageForm;
import org.apache.struts.upload.FormFile;
	//ģ�黮��Form��
public class ModuleForm extends PageForm{
	//Fields
	
	private String module;// ģ���ʶ
	private String funcs;// ����ӳ��
	private String pic;// ͼƬ
	private String name;// ����
	private String link;// ���ӣ������ӻ���Թ���ӳ�䣩
	private String info;// ˵��
	private String ord;// ����
	private String id;// null
	//Constructors
	/** default constructor */
	public ModuleForm() {
	
	}	
	//getter and setter
	public String getModule() {
		return this.module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getFuncs() {
		return this.funcs;
	}
	public void setFuncs(String funcs) {
		this.funcs = funcs;
	}
	public String getPic() {
		return this.pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return this.link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getInfo() {
		return this.info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getOrd() {
		return this.ord;
	}
	public void setOrd(String ord) {
		this.ord = ord;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
