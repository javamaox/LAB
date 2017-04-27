package com.qtrmoon.sysManage.bean;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import com.qtrmoon.common.PageForm;
import org.apache.struts.upload.FormFile;
	//机器MacIp绑定交验Form类
public class MachineForm extends PageForm{
	//Fields
	
	private String addtime;// 添加时间
	private String addtimeBeg;//查询begin
	private String addtimeEnd;//查询end
	private String id;// 主键
	private String ip;// IP地址
	private String mac;// MAC地址
	private String organid;// 所属机构id
	
	//Constructors
	/** default constructor */
	public MachineForm() {
	
	}	
	//getter and setter
	public String getAddtime() {
		return this.addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getAddtimeBeg() {
		return this.addtimeBeg;
	}
	public void setAddtimeBeg(String addtimeBeg) {
		this.addtimeBeg = addtimeBeg;
	}
	public String getAddtimeEnd() {
		return this.addtimeEnd;
	}
	public void setAddtimeEnd(String addtimeEnd) {
		this.addtimeEnd = addtimeEnd;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIp() {
		return this.ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMac() {
		return this.mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getOrganid() {
		return organid;
	}
	public void setOrganid(String organid) {
		this.organid = organid;
	}
	
}
