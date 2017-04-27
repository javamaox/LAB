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
	 * ��ȡ��ǰ��¼�û�
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
	 * ��ȡ��ǰ��¼����
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
	 * ��ȡ�����û�
	 * 
	 * @return
	 */
	public static List<UserForm> getAllUser() {
		List<UserForm> userList = sysService.schUser(null);
		return userList;
	}
	
	/**
	 * ��ȡ��������
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
	 * ��ȡ���л���
	 * 
	 * @return
	 */
	public static List<OrganForm> getAllOrgan() {
		List<OrganForm> organList = sysService.schOrgan(null, null);
		return organList;
	}

	/**
	 *  ��ȡ����
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
	 * ��ӻ���
	 * 
	 * @param organForm
	 * @return
	 */
	public static String addOrgan(OrganForm organForm) {
		return sysService.addOrgan(organForm);
	}

	/**
	 * �޸Ļ���
	 * 
	 * @param organForm
	 */
	public static void updOrgan(OrganForm organForm) {
		sysService.updOrgan(organForm);
	}

	/**
	 * ɾ������
	 * 
	 * @param id
	 */
	public static void delOrgan(String id) {
		sysService.delOrgan(id);
	}

	/**
	 * ���ݻ���ID��ȡ����
	 * 
	 * @return
	 */
	public static OrganForm getOrganById(String organId) {
		OrganForm organForm = sysService.schOrganById(organId);
		return organForm;
	}

	/**
	 * ����Id��ȡ�û�
	 * 
	 * @param targetUserId
	 */
	public static UserForm getUserById(String id) {
		return sysService.schUserById(id);
	}

	/**
	 * ��ȡĳ�������û��б�
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
	 * ��ȡĳ�û�
	 * 
	 * @param organId
	 * @return
	 */
	public static List<UserForm> getUser(UserForm sqlForm) {
		List<UserForm> userList = sysService.schUser(sqlForm);
		return userList;
	}
	
	/**
	 * ����CODE��ѯ��֯����
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
	 * ��ȡ������ѡ��
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
	 * ��ȡ������ѡ��
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
	 * ��¼�û��ķ�����ʷ��IE�ķ�����ʷ���ڰ�ȫ�Ŀ��ǣ��������ȡ��ֻ���Լ���ɡ�
	 * ������˿��������������ܵġ����ء����ܣ�����ý���ĳ��ҳ��Aҳ��֮ǰ��ҳ������Щ���Ϳ���ʵ�ַ��أ�����һ�������ҳ��Rҳ����
	 * ����Ҫ������refererҳ��Rҳ������switch��ʶ����ֻ�������Aҳ��д���봦��
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
		if(historyList.size()>30){//�����30����¼��
			historyList.pollLast();
		}
		//historyList[0]�����µ�����·��
		historyList.push(req.getRequestURI()+"?"+req.getQueryString());
	}
	

	public SysManageService getSysService() {
		return sysService;
	}

	public void setSysService(SysManageService sysService) {
		SysUtil.sysService = sysService;
	}
}
