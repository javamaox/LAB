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
	 * 增加用户
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
				request.setAttribute("info", "登陆名已被使用,请重新设置!");
				return mapping.findForward(FORWARD_reply);
			}
			String psw = userForm.getPassword();
			if (Constant.getBooleanConstant("pswmd5")) {
				psw = MD5.getInstance().getMD5to32(psw);
			}
			userForm.setPassword(psw);
			String id = sysService.addUser(userForm);
			// 写入额外信息
			String infoBasePath = Constant.getConstant("userInfoPath");
			String infoModelPath = infoBasePath + "/userInfoModel.xml";
			String infoPath = infoBasePath + "/" + id + ".xml";
			String[] expcolname = request.getParameterValues("expcolname");
			String[] expcolvalue = request.getParameterValues("expcolvalue");
			ExpColUtil.setExpCol(infoModelPath, infoPath, expcolname,
					expcolvalue);

			request.setAttribute("info", "添加用户成功！");
			return new ActionForward(
					"/user.do?method=schUserByOrganId&organId="
							+ userForm.getOrganId());
		} else {
			String organId = request.getParameter("id");
			UserForm userForm = new UserForm();
			userForm.setOrganId(organId);
			request.setAttribute("userForm", userForm);
			// 获取额外信息
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
	 * 修改用户
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
					// 检测是否是查到的是自己
					request.setAttribute("info", "登陆名已被使用,请重新设置!");
					return mapping.findForward(FORWARD_reply);
				}
			}
			sysService.updUser(userForm);

			// 写入额外信息
			String infoBasePath = Constant.getConstant("userInfoPath");
			String infoModelPath = infoBasePath + "/userInfoModel.xml";
			String infoPath = infoBasePath + "/" + userForm.getId() + ".xml";
			String[] expcolname = request.getParameterValues("expcolname");
			String[] expcolvalue = request.getParameterValues("expcolvalue");
			ExpColUtil.setExpCol(infoModelPath, infoPath, expcolname,
					expcolvalue);

			request.setAttribute("info", "修改用户成功！");
			return new ActionForward(
					"/user.do?method=schUserByOrganId&organId="
							+ userForm.getOrganId());
		} else {
			String userId = request.getParameter("userId");
			UserForm user = sysService.schUserById(userId);
			request.setAttribute("userForm", user);
			// 获取额外信息
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
	 * 删除用户
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
		// 删除角色-用户中间表中包含该用户的信息
		sysService.delRoleUser(roleUser);
		// 删除额外信息
		String basePath = Constant.getConstant("userInfoPath");
		String absolutePath = basePath + userId + ".xml";
		File curFile = new File(absolutePath);
		curFile.delete();

		request.setAttribute("info", "删除用户成功！");
		return new ActionForward("/user.do?method=schUserByOrganId&organId="
				+ organId);
	}

	/**
	 * 列出机构下的用户列表
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
		// 如果organId为空.选择根节点
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
		// 获取组织机构名称
		String organName = sysService.schOrganById(organId).getName();

		request.setAttribute("userFormList", userFormList);
		request.setAttribute("organId", organId);
		request.setAttribute("organName", organName);
		return mapping.findForward(FORWARD_schUserByOrganId);
	}

	/**
	 * 为用户设定角色时弹出的可用角色集
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
		// 通用角色且不是超级管理员角色
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
	 * 为用户设定角色
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
	 * 选择某用户后查看
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
		// 获取额外信息
		String infoBasePath = Constant.getConstant("userInfoPath");
		String infoModelPath = infoBasePath + "/" + userId + ".xml";
		List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
		String scriptCode = ExpColUtil.getValidate(colList);
		request.setAttribute("scriptCode", scriptCode);
		request.setAttribute("colList", colList);
		return mapping.findForward(FORWARD_viewUser);
	}

	/**
	 * 选择某用户后列出该用户的角色
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
	 * 修改个人信息、密码
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
			// 修改信息
			sysService.updUser(userForm);
			// 写入额外信息
			String infoBasePath = Constant.getConstant("userInfoPath");
			String infoModelPath = infoBasePath + "/userInfoModel.xml";
			String infoPath = infoBasePath + "/" + userForm.getId() + ".xml";
			String[] expcolname = request.getParameterValues("expcolname");
			String[] expcolvalue = request.getParameterValues("expcolvalue");
			ExpColUtil.setExpCol(infoModelPath, infoPath, expcolname,
					expcolvalue);

			request.setAttribute("info", "修改信息成功");
		} else if (from != null && from.equals("updPassword")) {
			// 修改密码
			String psw = userForm.getPassword();// 修改后的密码
			String oldPassword = request.getParameter("oldpassword");

			UserForm oldUser = sysService.schUserById(userForm.getId());
			// 对比原密码是否正确
			if (!MD5.getInstance().getMD5to32(oldPassword).equals(
					oldUser.getPassword())) {
				request.setAttribute("info", "原密码不正确,请重新输入!");
				return mapping.findForward(FORWARD_perUpdPassword);
			}

			if (Constant.getBooleanConstant("pswmd5")) {
				psw = MD5.getInstance().getMD5to32(psw);
			}

			oldUser.setPassword(psw);
			sysService.updUser(oldUser);
			request.setAttribute("info", "修改密码成功");
		} else if (from != null && from.equals("perUpdPassword")) {
			userForm = SysUtil.getCurrentUser(request);
			request.setAttribute("userForm", userForm);
			return mapping.findForward(FORWARD_perUpdPassword);
		}
		userForm = SysUtil.getCurrentUser(request);
		request.setAttribute("userForm", userForm);
		// 获取额外信息
		String infoBasePath = Constant.getConstant("userInfoPath");
		String infoModelPath = infoBasePath + "/" + userForm.getId() + ".xml";
		List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
		String scriptCode = ExpColUtil.getValidate(colList);
		request.setAttribute("scriptCode", scriptCode);
		request.setAttribute("colList", colList);

		return mapping.findForward(FORWARD_updUserInfo);
	}

	/**
	 * 重置密码
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
		request.setAttribute("info", "重置密码成功!");
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
