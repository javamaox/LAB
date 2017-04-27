package com.qtrmoon.sysManage.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.qtrmoon.common.Constant;
import com.qtrmoon.common.PageForm;
import com.qtrmoon.sysManage.Online;
import com.qtrmoon.sysManage.action.ExpColUtil;

/**
 * SysUser generated by MyEclipse - Hibernate Tools
 */

public class UserForm extends PageForm implements HttpSessionBindingListener {

	// Fields

	private String id;//

	private String loginName;// 登陆名

	private String password;// 密码

	private String userName;// 用户名字

	private String classify;// 类型

	private String state;// 状态

	private Date regTime;// 注册时间

	private String organId;
	
	private Date birthday;

	private List<FunctionForm> functionlist; // 功能集合
	
	private Map<String,String> expMap;
	
	private String loginIp; // 登陆IP
	private String sessionId; // 在线用户sessionid，重复登陆会更新在线用户列表对象的用户sessionid，同一帐号在最后一次登陆的用户对象退出时才会删除在线表。
	
	/**
	 * 获取额外信息
	 * @param key
	 * @return
	 */
	public String exp(String key){
		if(expMap==null){
			expMap = new HashMap<String,String>();
			String infoBasePath = Constant.getConstant("userInfoPath");
			String infoModelPath = infoBasePath + "/" + id + ".xml";
			List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
			if(colList!=null){
				for(XmlCol col:colList){
					expMap.put(col.getName(),col.getValue());
				}
			}
		}
		return expMap.get(key);
	}
	
	// Constructors
	/** default constructor */
	public UserForm() {
	}

	/** minimal constructor */
	public UserForm(String id) {
		this.id = id;
	}

	/** full constructor */
	public UserForm(String id, String loginname, String password,
			String username) {
		this.id = id;
		this.loginName = loginname;
		this.password = password;
		this.userName = username;
	}

	// Property accessors

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public List<FunctionForm> getFunctionlist() {
		return functionlist;
	}

	public void setFunctionlist(List functionlist) {
		this.functionlist = functionlist;
	}

	public void valueBound(HttpSessionBindingEvent event) {
		exp(id);
		UserForm userForm =Online.getOnlineUser().get(id);
		if(userForm!=null){//更新用户的sessionID
			userForm.setSessionId(event.getSession().getId());
		}else{//记录用户的sessionID
			this.setSessionId(event.getSession().getId());
			Online.addUser(this);
		}

	}

	/**
	 * 同一用户多次登陆时，sessionid记录的是最近一次登陆的用户的sessionid。只有取出的当前用户的sid与该sid匹配时才会删除用户。
	 * 这样避免了，后登陆的用户对象，被上一次登陆而超时的unbound方法删除。
	 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void valueUnbound(HttpSessionBindingEvent event) {
		UserForm userForm =Online.getOnlineUser().get(id);
		if(userForm!=null&&userForm.getSessionId().equals(event.getSession().getId())){
			Online.delUser(id);
		}
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}