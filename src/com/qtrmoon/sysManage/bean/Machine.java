package com.qtrmoon.sysManage.bean;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
	//机器MacIp绑定交验Object类
	public class Machine{
	//Fields
	
	private Date addtime;// 添加时间
	private String id;// 主键
	private String ip;// IP地址
	private String mac;// MAC地址
	private String organid;// 所属机构id
	
	//Constructors
	/** default constructor */
	public Machine() {
	
	}	
	//getter and setter
	public Date getAddtime() {
		return this.addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
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
