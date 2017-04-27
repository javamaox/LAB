package com.qtrmoon.sysManage.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.qtrmoon.common.Constant;
import com.qtrmoon.sysManage.SysUtil;
import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.sysManage.bean.RoleForm;
import com.qtrmoon.sysManage.bean.RoleUser;
import com.qtrmoon.sysManage.bean.UserForm;
import com.qtrmoon.sysManage.bean.XmlCol;
import com.qtrmoon.sysManage.serDao.SysManageService;
import com.qtrmoon.toolkit.MD5;

public class UserAction extends DispatchAction {
	private SysManageService sysService;

	private static final String FORWARD_schUserByOrganId = "schUser";

	private static final String FORWARD_viewUser = "view";

	private static final String FORWARD_addUser = "addUser";

	private static final String FORWARD_updUser = "updUser";

	private static final String FORWARD_updUserInfo = "updUserInfo";

	private static final String FORWARD_perUpdPassword = "perUpdPassword";

	private static final String FORWARD_reply = "reply";

	/**
	 * �����û�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * 
	 */
	public ActionForward addUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {// add
			UserForm userForm = (UserForm) form;
			userForm.setState(SysConstant.USER_AUDIT);
			UserForm sqlForm = new UserForm();
			sqlForm.setLoginName(userForm.getLoginName());
			List list = sysService.schUser(sqlForm);
			if (list != null && list.size() > 0) {
				request.setAttribute("info", "��½���ѱ�ʹ��,����������!");
				return mapping.findForward(FORWARD_reply);
			}
			String psw = userForm.getPassword();
			if (Constant.getBooleanConstant("pswmd5")) {
				psw = MD5.getInstance().getMD5to32(psw);
			}
			userForm.setPassword(psw);
			String id = sysService.addUser(userForm);
			// д�������Ϣ
			String infoBasePath = Constant.getConstant("userInfoPath");
			String infoModelPath = infoBasePath + "/userInfoModel.xml";
			String infoPath = infoBasePath + "/" + id + ".xml";
			String[] expcolname = request.getParameterValues("expcolname");
			String[] expcolvalue = request.getParameterValues("expcolvalue");
			ExpColUtil.setExpCol(infoModelPath, infoPath, expcolname,
					expcolvalue);

			request.setAttribute("info", "����û��ɹ���");
			return new ActionForward(
					"/user.do?method=schUserByOrganId&organId="
							+ userForm.getOrganId());
		} else {
			String organId = request.getParameter("id");
			UserForm userForm = new UserForm();
			userForm.setOrganId(organId);
			request.setAttribute("userForm", userForm);
			// ��ȡ������Ϣ
			String infoBasePath = Constant.getConstant("userInfoPath");
			String infoModelPath = infoBasePath + "/userInfoModel.xml";
			List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
			String scriptCode = ExpColUtil.getValidate(colList);
			request.setAttribute("scriptCode", scriptCode);
			request.setAttribute("colList", colList);
		}
		return mapping.findForward(FORWARD_addUser);
	}

	/**
	 * �޸��û�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {
			UserForm userForm = (UserForm) form;
			UserForm sqlForm = new UserForm();
			sqlForm.setLoginName(userForm.getLoginName());
			List<UserForm> list = sysService.schUser(sqlForm);
			if (list != null && list.size() > 0) {
				if (!list.get(0).getId().equals(userForm.getId())) {
					// ����Ƿ��ǲ鵽�����Լ�
					request.setAttribute("info", "��½���ѱ�ʹ��,����������!");
					return mapping.findForward(FORWARD_reply);
				}
			}
			sysService.updUser(userForm);

			// д�������Ϣ
			String infoBasePath = Constant.getConstant("userInfoPath");
			String infoModelPath = infoBasePath + "/userInfoModel.xml";
			String infoPath = infoBasePath + "/" + userForm.getId() + ".xml";
			String[] expcolname = request.getParameterValues("expcolname");
			String[] expcolvalue = request.getParameterValues("expcolvalue");
			ExpColUtil.setExpCol(infoModelPath, infoPath, expcolname,
					expcolvalue);

			request.setAttribute("info", "�޸��û��ɹ���");
			return new ActionForward(
					"/user.do?method=schUserByOrganId&organId="
							+ userForm.getOrganId());
		} else {
			String userId = request.getParameter("userId");
			UserForm user = sysService.schUserById(userId);
			request.setAttribute("userForm", user);
			// ��ȡ������Ϣ
			String infoBasePath = Constant.getConstant("userInfoPath");
			String infoModelPath = infoBasePath + "/" + userId + ".xml";
			List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
			String scriptCode = ExpColUtil.getValidate(colList);
			request.setAttribute("scriptCode", scriptCode);
			request.setAttribute("colList", colList);
		}
		return mapping.findForward(FORWARD_updUser);
	}

	/**
	 * ɾ���û�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward delUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		String organId = request.getParameter("organId");
		sysService.delUser(userId);
		RoleUser roleUser = new RoleUser();
		roleUser.setUserId(userId);
		// ɾ����ɫ-�û��м���а������û�����Ϣ
		sysService.delRoleUser(roleUser);
		// ɾ��������Ϣ
		String basePath = Constant.getConstant("userInfoPath");
		String absolutePath = basePath + userId + ".xml";
		File curFile = new File(absolutePath);
		curFile.delete();

		request.setAttribute("info", "ɾ���û��ɹ���");
		return new ActionForward("/user.do?method=schUserByOrganId&organId="
				+ organId);
	}

	/**
	 * �г������µ��û��б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward schUserByOrganId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String organId = request.getParameter("organId");
		// ���organIdΪ��.ѡ����ڵ�
		if (organId == null || organId.equals("")) {
			OrganForm sqlForm = new OrganForm();
			sqlForm.setCode("%" + Constant.getConstant("sysid") + "%");
			List<OrganForm> resultList = sysService.schOrgan(sqlForm,null);//" and t.treeTrack not like '%-%'"
			if (resultList.size() > 0) {
				organId = resultList.get(0).getId();
			}
		}
		UserForm sqlForm = new UserForm();
		sqlForm.setOrganId(organId);
		sqlForm.setState(SysConstant.USER_AUDIT);
		List<UserForm> userFormList = sysService.schUser(sqlForm);
		// ��ȡ��֯��������
		String organName = sysService.schOrganById(organId).getName();

		request.setAttribute("userFormList", userFormList);
		request.setAttribute("organId", organId);
		request.setAttribute("organName", organName);
		return mapping.findForward(FORWARD_schUserByOrganId);
	}

	/**
	 * Ϊ�û��趨��ɫʱ�����Ŀ��ý�ɫ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward schRoleByOrganId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String organId = request.getParameter("organId");
		String userId = request.getParameter("userId");

		List<RoleForm> roleFormList = sysService.schRoleByUserId(userId);
		// ͨ�ý�ɫ�Ҳ��ǳ�������Ա��ɫ
		RoleForm sqlForm = new RoleForm();
		sqlForm.setIsLocal(SysConstant.ROLE_NOTLOCAL);
		sqlForm.setCondition(" and t.classify!='1'");
		List roleList = sysService.schRole(sqlForm);
		String roleIds = "";
		for (int i = 0; i < roleFormList.size(); i++) {
			roleIds += roleFormList.get(i).getId() + ",";
		}
		if (!roleIds.equals("")) {
			roleIds = roleIds.substring(0, roleIds.length() - 1);
		}

		sqlForm = new RoleForm();
		sqlForm.setOrganId(organId);
		sqlForm.setIsLocal(SysConstant.ROLE_ISLOCAL);
		List<RoleForm> localRole = sysService.schRole(sqlForm);
		request.setAttribute("localRole", localRole);
		request.setAttribute("roleList", roleList);
		request.setAttribute("roleIds", roleIds);
		request.setAttribute("organId", organId);
		request.setAttribute("userId", userId);
		return mapping.findForward("setRoles");
	}

	/**
	 * Ϊ�û��趨��ɫ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward addRoleForUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] roleIds = request.getParameterValues("roles");
		String organId = request.getParameter("organId");
		String userId = request.getParameter("userId");
		RoleUser roleUser = new RoleUser();
		roleUser.setUserId(userId);
		sysService.delRoleUser(roleUser);
		if (roleIds != null) {
			for (int i = 0; i < roleIds.length; i++) {
				roleUser.setRoleId(roleIds[i]);
				sysService.addRoleUser(roleUser);
			}
		}
		return new ActionForward("/user.do?method=schUserByOrganId&organId="
				+ organId);
	}

	public ActionForward schUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	/**
	 * ѡ��ĳ�û���鿴
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward viewUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		UserForm userForm = sysService.schUserById(userId);
		request.setAttribute("userForm", userForm);
		// ��ȡ������Ϣ
		String infoBasePath = Constant.getConstant("userInfoPath");
		String infoModelPath = infoBasePath + "/" + userId + ".xml";
		List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
		String scriptCode = ExpColUtil.getValidate(colList);
		request.setAttribute("scriptCode", scriptCode);
		request.setAttribute("colList", colList);
		return mapping.findForward(FORWARD_viewUser);
	}

	/**
	 * ѡ��ĳ�û����г����û��Ľ�ɫ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward schRoleByUserId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String userId = request.getParameter("userId");
		List<RoleForm> roleFormList = sysService.schRoleByUserId(userId);
		request.setAttribute("roleFormList", roleFormList);
		return null;
	}

	/**
	 * �޸ĸ�����Ϣ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updUserInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserForm userForm = (UserForm) form;
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {
			// �޸���Ϣ
			sysService.updUser(userForm);
			// д�������Ϣ
			String infoBasePath = Constant.getConstant("userInfoPath");
			String infoModelPath = infoBasePath + "/userInfoModel.xml";
			String infoPath = infoBasePath + "/" + userForm.getId() + ".xml";
			String[] expcolname = request.getParameterValues("expcolname");
			String[] expcolvalue = request.getParameterValues("expcolvalue");
			ExpColUtil.setExpCol(infoModelPath, infoPath, expcolname,
					expcolvalue);

			request.setAttribute("info", "�޸���Ϣ�ɹ�");
		} else if (from != null && from.equals("updPassword")) {
			// �޸�����
			String psw = userForm.getPassword();// �޸ĺ������
			String oldPassword = request.getParameter("oldpassword");

			UserForm oldUser = sysService.schUserById(userForm.getId());
			// �Ա�ԭ�����Ƿ���ȷ
			if (!MD5.getInstance().getMD5to32(oldPassword).equals(
					oldUser.getPassword())) {
				request.setAttribute("info", "ԭ���벻��ȷ,����������!");
				return mapping.findForward(FORWARD_perUpdPassword);
			}

			if (Constant.getBooleanConstant("pswmd5")) {
				psw = MD5.getInstance().getMD5to32(psw);
			}

			oldUser.setPassword(psw);
			sysService.updUser(oldUser);
			request.setAttribute("info", "�޸�����ɹ�");
		} else if (from != null && from.equals("perUpdPassword")) {
			userForm = SysUtil.getCurrentUser(request);
			request.setAttribute("userForm", userForm);
			return mapping.findForward(FORWARD_perUpdPassword);
		}
		userForm = SysUtil.getCurrentUser(request);
		request.setAttribute("userForm", userForm);
		// ��ȡ������Ϣ
		String infoBasePath = Constant.getConstant("userInfoPath");
		String infoModelPath = infoBasePath + "/" + userForm.getId() + ".xml";
		List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
		String scriptCode = ExpColUtil.getValidate(colList);
		request.setAttribute("scriptCode", scriptCode);
		request.setAttribute("colList", colList);

		return mapping.findForward(FORWARD_updUserInfo);
	}

	/**
	 * ��������
	 */
	public ActionForward resetPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		UserForm userForm = sysService.schUserById(userId);
		String password = "000";
		if (Constant.getBooleanConstant("pswmd5")) {
			password = MD5.getInstance().getMD5to32(password);
		}
		userForm.setPassword(password);
		sysService.updUser(userForm);
		request.setAttribute("info", "��������ɹ�!");
		return new ActionForward("/user.do?method=schUserByOrganId&organId="
				+ userForm.getOrganId());
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	public SysManageService getSysService() {
		return sysService;
	}

	public void setSysService(SysManageService sysService) {
		this.sysService = sysService;
	}

}
