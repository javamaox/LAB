package com.qtrmoon.sysManage.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.jdom.Element;

import com.qtrmoon.common.Constant;
import com.qtrmoon.dictEditor.DictBuffer;
import com.qtrmoon.dictEditor.beanSerDao.DictCatalog;
import com.qtrmoon.dictEditor.beanSerDao.DictTreeUtil;
import com.qtrmoon.dictEditor.beanSerDao.DictionaryForm;
import com.qtrmoon.sysManage.SysUtil;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.sysManage.bean.RoleForm;
import com.qtrmoon.sysManage.bean.RoleFunction;
import com.qtrmoon.sysManage.bean.UserForm;
import com.qtrmoon.sysManage.serDao.SysManageService;
import com.qtrmoon.toolkit.XmlUtil;

public class RoleAction extends DispatchAction {
	private static final String FORWARD_updRole = "updRole";

	private static final String FORWARD_updAdminRole = "updAdminRole";

	private static final String FORWARD_schRole = "schRole";

	private static final String FORWARD_addRole = "addRole";

	private static final String FORWARD_addAdminRole = "addAdminRole";

	private static final String FORWARD_schOrganTree = "schOrganTree";

	private static final String FORWARD_reply = "reply";

	private static final String FORWARD_listRole = "listRole";

	private SysManageService sysService;

	/**
	 * 超级用户添加角色
	 */
	public ActionForward addAdminRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		RoleForm roleForm = (RoleForm) form;
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {
			String roleId = sysService.addRole(roleForm);

			// 获取已被选中的功能ID
			String[] funcIds = request.getParameterValues("multi");
			// 往中间表中添加信息
			if (funcIds != null && funcIds.length > 0) {
				RoleFunction roleFunction = new RoleFunction();
				roleFunction.setRoleId(roleId);
				for (int i = 0; i < funcIds.length; i++) {
					List<FunctionForm> funcList = sysService.schFunction(null,
							"and t.pid='" + funcIds[i] + "'");
					// 添加该功能隐藏的子功能
					if (funcList != null) {
						for (int j = 0; j < funcList.size(); j++) {
							if (funcList.get(j).getIsShow().equals(SysConstant.FUNCTION_HIDE)) {
								roleFunction.setFunctionId(funcList.get(j)
										.getId());
								sysService.addRoleFunction(roleFunction);
							}
						}
					}
					roleFunction.setFunctionId(funcIds[i]);
					sysService.addRoleFunction(roleFunction);
				}
			}
			request.setAttribute("info", "添加角色成功！");
			request.setAttribute("organId", roleForm.getOrganId());
			return mapping.findForward(FORWARD_listRole);
		} else {
			// preAdd
			roleForm.setIsLocal(SysConstant.ROLE_ISLOCAL);
			roleForm.setClassify(SysConstant.ROLE_CLASSIFY_COMMON);
			String organId = request.getParameter("organId");
			// 查询该机构下的功能
			FunctionForm sqlForm = new FunctionForm();
			sqlForm.setIsShow(SysConstant.FUNCTION_SHOW);
			List<FunctionForm> funcList = sysService.schFunction(sqlForm, null);
			MultiFunctionTreeUtil treeUtil = new MultiFunctionTreeUtil();
			String code = "该机构没有功能!";
			if (funcList.size() > 0) {
				code = treeUtil.getOrderTreeCode(funcList);
			}
			request.setAttribute("code", code);
			request.setAttribute("organId", organId);
			return mapping.findForward(FORWARD_addAdminRole);
		}
	}

	/**
	 * 删除角色
	 */
	public ActionForward delRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String roleId = request.getParameter("id");
		String organId = request.getParameter("organId");
		List<UserForm> userFormList = sysService.schUserByRoleId(roleId);
		if (userFormList.size() > 0) {
			request.setAttribute("res", "该角色已被用户["
					+ userFormList.get(0).getUserName() + "]使用!不能删除!");
		} else {
			request.setAttribute("res", "删除角色成功！");

			// 删除 角色-功能中间表 中有关的角色信息
			RoleFunction roleFunction = new RoleFunction();
			roleFunction.setRoleId(roleId);
			sysService.delRoleFunction(roleFunction);

			sysService.delRole(roleId);
		}
		request.setAttribute("organId", organId);
		return mapping.findForward(FORWARD_listRole);
	}

	/**
	 * 超级用户修改角色
	 */
	public ActionForward updAdminRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {
			RoleForm roleForm = (RoleForm) form;
			String type=request.getParameter("type"); 
			if("dataAuth".equals(type)){
				String[] dataAuths=request.getParameterValues("dataAuth");
				if(dataAuths!=null&&dataAuths.length>0){
					String res="";
					for(String data:dataAuths){
						res+=data+",";
					}
					res=res.substring(0,res.length()-1);
					XmlUtil xmlUtil = new XmlUtil();
					String dataAuthorityPath = Constant.getConstant("dataAuthorityPath");
					Element root = xmlUtil.loadXml(dataAuthorityPath);
					List<Element> authorities = root.getChildren("authority");
					Element editElem=null;
					label:for(Element elem:authorities){
						for(String data:dataAuths){
							if(elem.getAttributeValue("role").equals(roleForm.getId())){
								editElem=elem;
								break label;
							}
						}
					}
					if(editElem==null){
						editElem = (Element)XmlUtil.simpleElem("authority", res);
						editElem.setAttribute("role", roleForm.getId());
						root.addContent(editElem);
					}else{
						editElem.setText(res);
					}
					xmlUtil.saveXml();
				}
			}else{
				sysService.updRole(roleForm);
				// 获取已被选中的功能ID
	
				RoleFunction roleFunction = new RoleFunction();
				roleFunction.setRoleId(roleForm.getId());
				// 先删除中间表中该角色的所有信息
				sysService.delRoleFunction(roleFunction);
				// 再往中间表添加修改后的信息
				String[] funcIds = request.getParameterValues("multi");
	
				if (funcIds != null && funcIds.length > 0) {
					for (int i = 0; i < funcIds.length; i++) {
						List<FunctionForm> funcList = sysService.schFunction(null,
								"and t.pid='" + funcIds[i] + "'");
						// 添加该功能隐藏的子功能
						if (funcList != null) {
							for (int j = 0; j < funcList.size(); j++) {
								if (funcList.get(j).getIsShow().equals(SysConstant.FUNCTION_HIDE)) {
									roleFunction.setFunctionId(funcList.get(j)
											.getId());
									sysService.addRoleFunction(roleFunction);
								}
							}
						}
						roleFunction.setFunctionId(funcIds[i]);
						sysService.addRoleFunction(roleFunction);
					}
				}
			}
			request.setAttribute("organId", roleForm.getOrganId());
			request.setAttribute("info", "修改成功");
			return mapping.findForward(FORWARD_listRole);
		} else {
			String roleId = new String(request.getParameter("id"));
			String organId = request.getParameter("organId");
			RoleForm roleForm = sysService.schRole(roleId);

			// 该角色拥有的功能
			List<FunctionForm> funcForm = sysService
					.schFunctionsByRoleId(roleId);
			// 该机构拥有的功能
			List<FunctionForm> funcList = sysService.schFunction(null, " and t.isShow>=1");
			String code = "该机构没有功能!";
			if (funcList.size() > 0) {
				MultiFunctionTreeUtil treeUtil = new MultiFunctionTreeUtil();
				code = treeUtil.getOrderTreeCode(funcList);
			}
			String funcIds = "";// 机构拥有的角色ID串,回显时使用
			for (int i = 0; i < funcForm.size(); i++) {
				funcIds += funcForm.get(i).getId() + ",";
			}
			if (!funcIds.equals("")) {
				funcIds = funcIds.substring(0, funcIds.length() - 1);
			}
			//数据权限，数据分类字典
			String useDataAuthority = Constant.getConstant("useDataAuthority");
			request.setAttribute("useDataAuthority", useDataAuthority);
			if(useDataAuthority!=null&&useDataAuthority.equals("true")){
				String dictId = Constant.getConstant("dataAuthorityDict");
				if(dictId==null||dictId.equals("")){
					request.setAttribute("useOrgan", true);
					dictId="sysOrgan";
				}
				DictCatalog cf = DictBuffer.findDictCatalogById(dictId);
				List<DictionaryForm> list = DictBuffer.getDict(dictId);
				List<DictionaryForm> copylist = new ArrayList<DictionaryForm>();
				DictionaryForm tmpForm;
				for (DictionaryForm dict:list) {
					tmpForm = new DictionaryForm();
					tmpForm.setId(dict.getId());
					tmpForm.setPid(dict.getPid());
					tmpForm.setLabel(dict.getLabel());
					if (tmpForm.getPid() == null || tmpForm.getPid().equals("")) {
						tmpForm.setPid("0");
					}else if (tmpForm.getPid().trim().equals("0")) {
						tmpForm.setPid("0");
					}
					copylist.add(tmpForm);
				}
				// 创建根节点
				DictionaryForm root = new DictionaryForm();
				root.setId("0");
				root.setLabel(cf.getTabledesc() + "类别");
				root.setPid("");
				copylist.add(root);
				String dataCode = (new DictTreeUtil(true)).getTreeCode(copylist);
				request.setAttribute("dataCode", dataCode);
				String dataAuthorityPath = Constant.getConstant("dataAuthorityPath");
				XmlUtil xmlUtil = new XmlUtil();
				List<Element> authorities = xmlUtil.loadXml(dataAuthorityPath).getChildren("authority");
				String dataAuthorites="";
				for(Element auth:authorities){
					if(auth.getAttributeValue("role").equals(roleId)){
						dataAuthorites=auth.getText();
						break;
					}
				}
				request.setAttribute("dataAuthorites", dataAuthorites);
			}
			request.setAttribute("funcIds", funcIds);
			request.setAttribute("roleForm", roleForm);
			request.setAttribute("code", code);
		}
		return mapping.findForward(FORWARD_updAdminRole);
	}

	/**
	 * 查询机构下拥有的角色
	 * 
	 * @return
	 */
	public ActionForward schRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String organId = request.getParameter("organId");
		// 如果organId为空.选择根节点
		if (organId == null || organId.equals("")) {
			OrganForm sqlForm = new OrganForm();
			sqlForm.setCode("%" + Constant.getConstant("sysid") + "%");
			List<OrganForm> resultList = sysService.schOrgan(sqlForm,
					" and t.treeTrack not like '%-%'");
			if (resultList.size() > 0) {
				organId = resultList.get(0).getId();
			}
		}

		// 如果是超级管理员
		if (isAdmin(SysUtil.getCurrentUser(request).getId())) {
			RoleForm sqlForm = new RoleForm();
			sqlForm.setIsLocal(SysConstant.ROLE_NOTLOCAL);
			// sqlForm.setOrganId(organId);
			List<RoleForm> roleAdminList = sysService.schRole(sqlForm);
			request.setAttribute("roleAdminList", roleAdminList);
			request.setAttribute("isAdmin", "1");
		}
		// 查询机构下角色
		RoleForm sqlForm = new RoleForm();
		sqlForm.setIsLocal(SysConstant.ROLE_ISLOCAL);
		sqlForm.setOrganId(organId);
		List<RoleForm> roleList = sysService.schRole(sqlForm);

		request.setAttribute("organId", organId);
		request.setAttribute("roleList", roleList);
		return mapping.findForward(FORWARD_schRole);
	}

	/**
	 * 查询是否是系统管理员
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public boolean isAdmin(String userId) {
		// 
		List<RoleForm> userList = sysService.schRoleByUserId(userId);
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getClassify().equals(
					SysConstant.ROLE_CLASSIFY_ADMIN)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 添加角色
	 */
	public ActionForward addRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {
			RoleForm roleForm = (RoleForm) form;
			roleForm.setClassify(SysConstant.ROLE_CLASSIFY_COMMON);
			String roleId = sysService.addRole(roleForm);

			// 获取已被选中的功能ID
			String[] funcIds = request.getParameterValues("multi");
			// 往中间表中添加信息
			if (funcIds != null && funcIds.length > 0) {
				RoleFunction roleFunction = new RoleFunction();
				roleFunction.setRoleId(roleId);
				for (int i = 0; i < funcIds.length; i++) {
					List<FunctionForm> funcList = sysService.schFunction(null,
							"and t.pid='" + funcIds[i] + "'");
					// 添加该功能隐藏的子功能
					if (funcList != null) {
						for (int j = 0; j < funcList.size(); j++) {
							if (funcList.get(j).getIsShow().equals(SysConstant.FUNCTION_HIDE)) {
								roleFunction.setFunctionId(funcList.get(j)
										.getId());
								sysService.addRoleFunction(roleFunction);
							}
						}
					}
					roleFunction.setFunctionId(funcIds[i]);
					sysService.addRoleFunction(roleFunction);
				}
			}
			request.setAttribute("info", "添加角色成功！");
			request.setAttribute("organId", roleForm.getOrganId());
			return mapping.findForward(FORWARD_listRole);
		} else {
			// preAdd
			String organId = request.getParameter("organId");
			List<RoleForm> userList = sysService.schRoleByUserId(SysUtil
					.getCurrentUser(request).getId());
			for (int i = 0; i < userList.size(); i++) {
				if (userList.get(i).getClassify().equals(SysConstant.ROLE_CLASSIFY_ADMIN)) {
					request.setAttribute("isAdmin", 1);
					break;
				}
			}
			// 查询该机构下的功能
			List<FunctionForm> funcList = sysService
					.schFunctionsByOrganId(organId);
			MultiFunctionTreeUtil treeUtil = new MultiFunctionTreeUtil();
			String code = "该机构没有功能!";
			if (funcList.size() > 0) {
				code = treeUtil.getOrderTreeCode(funcList);
			}
			request.setAttribute("code", code);
			request.setAttribute("organId", organId);
			return mapping.findForward(FORWARD_addRole);
		}
	}

	/**
	 * 修改角色
	 */
	public ActionForward updRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {
			RoleForm roleForm = (RoleForm) form;
			sysService.updRole(roleForm);
			// 获取已被选中的功能ID

			RoleFunction roleFunction = new RoleFunction();
			roleFunction.setRoleId(roleForm.getId());
			// 先删除中间表中该角色的所有信息
			sysService.delRoleFunction(roleFunction);
			// 再往中间表添加修改后的信息
			String[] funcIds = request.getParameterValues("multi");
			if (funcIds != null && funcIds.length > 0) {
				for (int i = 0; i < funcIds.length; i++) {
					List<FunctionForm> funcList = sysService.schFunction(null,
							"and t.pid='" + funcIds[i] + "'");
					// 添加该功能隐藏的子功能
					if (funcList != null) {
						for (int j = 0; j < funcList.size(); j++) {
							if (funcList.get(j).getIsShow().equals("0")) {
								roleFunction.setFunctionId(funcList.get(j)
										.getId());
								sysService.addRoleFunction(roleFunction);
							}
						}
					}
					roleFunction.setFunctionId(funcIds[i]);
					sysService.addRoleFunction(roleFunction);
				}
			}

			request.setAttribute("organId", roleForm.getOrganId());
			request.setAttribute("info", "修改成功");
			return mapping.findForward(FORWARD_listRole);
		} else {
			String roleId = new String(request.getParameter("id"));
			String organId = request.getParameter("organId");
			RoleForm roleForm = sysService.schRole(roleId);
			// 该角色拥有的功能
			List<FunctionForm> funcForm = sysService
					.schFunctionsByRoleId(roleId);
			// 该机构拥有的功能
			List<FunctionForm> funcList = sysService
					.schFunctionsByOrganId(organId);
			String code = "该机构没有功能!";
			if (funcList.size() > 0) {
				MultiFunctionTreeUtil treeUtil = new MultiFunctionTreeUtil();
				code = treeUtil.getOrderTreeCode(funcList);
			}
			String funcIds = "";// 机构拥有的角色ID串,回显时使用
			for (int i = 0; i < funcForm.size(); i++) {
				funcIds += funcForm.get(i).getId() + ",";
			}
			if (!funcIds.equals("")) {
				funcIds = funcIds.substring(0, funcIds.length() - 1);
			}
			request.setAttribute("funcIds", funcIds);
			request.setAttribute("roleForm", roleForm);
			request.setAttribute("code", code);
		}
		return mapping.findForward(FORWARD_updRole);
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	public SysManageService getSysService() {
		return sysService;
	}

	public void setSysService(SysManageService sysService) {
		this.sysService = sysService;
	}

}
