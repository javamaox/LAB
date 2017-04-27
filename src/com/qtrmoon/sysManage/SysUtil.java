package com.qtrmoon.sysManage;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.qtrmoon.common.Constant;
import com.qtrmoon.sysManage.action.LoginAction;
import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.sysManage.bean.User;
import com.qtrmoon.sysManage.bean.UserForm;
import com.qtrmoon.sysManage.serDao.SysManageService;

public class SysUtil {
	private static SysManageService sysService;
	public static String CURRENT_USER = "CURRENT_USER";

	/**
	 * 获取当前登录用户
	 * 
	 * @param request
	 * @return
	 */
	public static UserForm getCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserForm userForm = (UserForm) session.getAttribute(CURRENT_USER);
		return userForm;
	}
	
	/**
	 * 获取当前登录机构
	 * 
	 * @param request
	 * @return
	 */
	public static OrganForm getCurrentOrgan(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserForm userForm = (UserForm) session.getAttribute(CURRENT_USER);
		OrganForm organForm = sysService.schOrganById(
				userForm.getOrganId());
		return organForm;
	}

	/**
	 * 获取所有用户
	 * 
	 * @return
	 */
	public static List<UserForm> getAllUser() {
		List<UserForm> userList = sysService.schUser(null);
		return userList;
	}
	
	/**
	 * 获取本级机构
	 * 
	 * @return
	 */
	public static List<OrganForm> getSysOrgan(){
		OrganForm organForm=new OrganForm();
		organForm.setCode(Constant.getConstant("sysid") + "%");
		List<OrganForm> userList = sysService.schOrgan(organForm, null);
		return userList;
	}

	/**
	 * 获取所有机构
	 * 
	 * @return
	 */
	public static List<OrganForm> getAllOrgan() {
		List<OrganForm> organList = sysService.schOrgan(null, null);
		return organList;
	}

	/**
	 *  获取机构
	 *  
	 * @param sqlForm
	 * @param string
	 * @return
	 */
	public static List<OrganForm> getOrgan(OrganForm sqlForm, String string) {
		List<OrganForm> organList = sysService.schOrgan(sqlForm, string);
		return organList;
	}

	/** 
	 * 添加机构
	 * 
	 * @param organForm
	 * @return
	 */
	public static String addOrgan(OrganForm organForm) {
		return sysService.addOrgan(organForm);
	}

	/**
	 * 修改机构
	 * 
	 * @param organForm
	 */
	public static void updOrgan(OrganForm organForm) {
		sysService.updOrgan(organForm);
	}

	/**
	 * 删除机构
	 * 
	 * @param id
	 */
	public static void delOrgan(String id) {
		sysService.delOrgan(id);
	}

	/**
	 * 根据机构ID获取机构
	 * 
	 * @return
	 */
	public static OrganForm getOrganById(String organId) {
		OrganForm organForm = sysService.schOrganById(organId);
		return organForm;
	}

	/**
	 * 根据Id获取用户
	 * 
	 * @param targetUserId
	 */
	public static UserForm getUserById(String id) {
		return sysService.schUserById(id);
	}

	/**
	 * 获取某机构下用户列表
	 * 
	 * @param organId
	 * @return
	 */
	public static List<UserForm> getUserByOrgan(String organId) {
		UserForm sqlForm = new UserForm();
		sqlForm.setOrganId(organId);
		List<UserForm> userList = sysService.schUser(sqlForm);
		return userList;
	}

	/**
	 * 获取某用户
	 * 
	 * @param organId
	 * @return
	 */
	public static List<UserForm> getUser(UserForm sqlForm) {
		List<UserForm> userList = sysService.schUser(sqlForm);
		return userList;
	}
	
	/**
	 * 根据CODE查询组织机构
	 * 
	 * @param code
	 * @return
	 */
	public static OrganForm getOrganByCode(String code) {
		OrganForm organForm = new OrganForm();
		organForm.setCode(code);
		List<OrganForm> organList = sysService.schOrgan(organForm, null);
		if (organList != null && organList.size() > 0) {
			return organList.get(0);
		}
		return null;
	}

	/**
	 * 获取机构单选树
	 * 
	 * @param organId
	 * @return
	 */
	public static String getOrganTreeSingleCode(List<OrganForm> organList) {
		String treeCode = (new OrganTreeSingleUtil())
				.getOrderTreeCode(organList);
		return treeCode;
	}

	/**
	 * 获取机构多选树
	 * 
	 * @param organId
	 * @return
	 */
	public static String getOrganTreeMultiCode(List<OrganForm> organList) {
		String treeCode = (new OrganTreeMultiUtil())
				.getOrderTreeCode(organList);
		return treeCode;
	}

	/**
	 * 记录用户的访问历史，IE的访问历史出于安全的考虑，不允许获取，只好自己存吧。
	 * 这个存了可以用来做，智能的“返回”功能，定义好进入某个页（A页）之前的页面有哪些，就可以实现返回，到上一个进入的页（R页）。
	 * 不需要额外在referer页（R页）来做switch标识，而只需在这个A页面写代码处理。
	 * @param string
	 */
	public static void addRequestHistory(HttpServletRequest req) {
		Object o=req.getSession().getAttribute("REQUEST_HISTORY");
		LinkedList<String> historyList;
		if(o==null){
			historyList=new LinkedList<String>();
			req.getSession().setAttribute("REQUEST_HISTORY", historyList);
		}else{
			historyList=(LinkedList)o;
		}
		if(historyList.size()>30){//存近期30个记录。
			historyList.pollLast();
		}
		//historyList[0]是最新的请求路径
		historyList.push(req.getRequestURI()+"?"+req.getQueryString());
	}
	

	public SysManageService getSysService() {
		return sysService;
	}

	public void setSysService(SysManageService sysService) {
		SysUtil.sysService = sysService;
	}
}
