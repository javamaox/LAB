package com.qtrmoon.sysManage.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.qtrmoon.common.Constant;
import com.qtrmoon.infoTransact.InfoUtil;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.FunctionUnitForm;
import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.sysManage.bean.OrganUnit;
import com.qtrmoon.sysManage.bean.RoleForm;
import com.qtrmoon.sysManage.bean.RoleFunction;
import com.qtrmoon.sysManage.bean.UserForm;
import com.qtrmoon.sysManage.bean.XmlCol;
import com.qtrmoon.sysManage.serDao.SysManageService;

public class OrganAction extends DispatchAction {
	private SysManageService sysService;

	private String FORWARD_addOrgan = "addOrgan";

	private String FORWARD_updOrgan = "updOrgan";

	private String FORWARD_schOrganTree = "schOrganTree";

	private static final String FORWARD_reply = "reply";

	public ActionForward addOrgan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		OrganForm organForm = (OrganForm) form;
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {// add
			// 这里organ的code是系统标识，service添加时会拼接Id。
			String organId = sysService.addOrgan(organForm);

			// 写入额外信息
			String infoBasePath = Constant.getConstant("organInfoPath");
			String infoModelPath = infoBasePath + "/organInfoModel.xml";
			String infoPath = infoBasePath + "/" + organId + ".xml";
			String[] expcolname = request.getParameterValues("expcolname");
			String[] expcolvalue = request.getParameterValues("expcolvalue");
			ExpColUtil.setExpCol(infoModelPath, infoPath, expcolname,
					expcolvalue);

			request.setAttribute("res", "添加机构成功！");
			if (Constant.getBooleanConstant("usemq")) {
				// 注册机构
				String name = organForm.getName();
				String code = Constant.getConstant("sysid") + organId;
				String type = organForm.getType();
				String pid = organForm.getPid();
				// 取父机构，获得父机构的code
				OrganForm orgForm = sysService.schOrganById(pid);
				String key = orgForm.getCode() + "," + name + "," + type;
				try {
					InfoUtil.registApp(code, key);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return mapping.findForward(FORWARD_reply);
		} else {// preAdd
			String parentId = request.getParameter("id");
			organForm = new OrganForm();
			organForm.setPid(parentId);
			organForm.setIsMaster(SysConstant.ORGAN_ORGAN);
			// 获取系统标识，标识该机构属于该级系统
			organForm.setCode(Constant.getConstant("sysid"));
			// 获取父机构的名称
			String pOrganName = sysService.schOrganById(parentId).getName();
			request.setAttribute("pOrganName", pOrganName);
			// 获取额外信息
			String infoBasePath = Constant.getConstant("organInfoPath");
			String infoModelPath = infoBasePath + "/organInfoModel.xml";
			List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
			String scriptCode = ExpColUtil.getValidate(colList);
			request.setAttribute("scriptCode", scriptCode);
			request.setAttribute("colList", colList);

			request.setAttribute("organForm", organForm);
			return mapping.findForward(FORWARD_addOrgan);
		}
	}

	public ActionForward updOrgan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		OrganForm organForm = (OrganForm) form;
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {// upd
			sysService.updOrgan(organForm);

			// 写入额外信息
			String infoBasePath = Constant.getConstant("organInfoPath");
			String infoModelPath = infoBasePath + "/organInfoModel.xml";
			String infoPath = infoBasePath + "/" + organForm.getId() + ".xml";
			String[] expcolname = request.getParameterValues("expcolname");
			String[] expcolvalue = request.getParameterValues("expcolvalue");
			ExpColUtil.setExpCol(infoModelPath, infoPath, expcolname,
					expcolvalue);

			request.setAttribute("res", "修改机构成功！");
			return new ActionForward("/organ.do?method=updOrgan&from=&organId="
					+ organForm.getId());
			// return mapping.findForward(FORWARD_reply);
		} else if (from != null && from.equals("setFuncUnit")) {
			// 修改机构的功能单元
			// 删除组织机构-功能单元中间表中含有该组织机构的信息

			String organId = request.getParameter("organId");
			List<FunctionUnitForm> funcUnitList = sysService
					.schFunctionUnitByOrganId(organId);

			// 获取要更改后的功能单元ID
			String[] unitIds = request.getParameter("unitIds").split(",");
			int isAdd = 0;// 用于判断是否去掉了功能单元
			for (int i = 0; i < funcUnitList.size(); i++) {
				for (int j = 0; j < unitIds.length; j++) {
					if (funcUnitList.get(i).getId().equals(unitIds[j])) {
						isAdd++;
						break;
					}
				}
			}

			if (isAdd != funcUnitList.size()) {
				// 如果去掉了功能单元
				// 判断该机构的角色是否拥有功能,如果拥有,不给予修改
				RoleForm roleForm = new RoleForm();
				roleForm.setOrganId(organId);
				roleForm.setIsLocal(SysConstant.ROLE_ISLOCAL);
				List<RoleForm> roleList = sysService.schRole(roleForm);
				boolean falg = false;
				if (roleList != null && roleList.size() > 0) {
					for (int i = 0; i < roleList.size(); i++) {
						List funcList = sysService
								.schFunctionsByRoleId(roleList.get(i).getId());
						if (funcList.size() > 0) {
							falg = true;
							break;
						}
					}
				}

				if (falg) {
					request.setAttribute("res", "功能单元已被使用,不能删除");
					return new ActionForward(
							"/organ.do?method=updOrgan&from=&organId="
									+ organId);
				}
			}

			OrganUnit organUnit = new OrganUnit();
			organUnit.setOrganId(organId);
			sysService.delOrganUnit(organUnit);
			// 往中间表中加入该机构的功能单元

			if (unitIds != null) {
				for (int i = 0; i < unitIds.length; i++) {
					organUnit.setUnitId(unitIds[i]);
					sysService.addOrganUnit(organUnit);
				}
			}
			request.setAttribute("res", "修改机构功能单元成功！");
			return new ActionForward("/organ.do?method=updOrgan&from=&organId="
					+ organId);
		} else {// preUpd
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
			organForm = sysService.schOrganById(organId);

			request.setAttribute("organForm", organForm);
			// 查询机构下的功能单元
			List<FunctionUnitForm> fUnitList = sysService
					.schFunctionUnitByOrganId(organId);
			request.setAttribute("organFunctionUnitList", fUnitList);

			// 查询所有功能单元
			List<FunctionUnitForm> allFUnitList = sysService.schFunctionUnit(
					null, null);
			List<FunctionUnitForm> delFUnitList = new ArrayList<FunctionUnitForm>();
			// 去除已选择的功能单元
			for (int i = 0; i < allFUnitList.size(); i++) {
				for (int m = 0; m < fUnitList.size(); m++) {
					if (allFUnitList.get(i).getId().equals(
							fUnitList.get(m).getId())) {
						delFUnitList.add(allFUnitList.get(i));
					}
				}
			}
			allFUnitList.removeAll(delFUnitList);
			request.setAttribute("allFUnitList", allFUnitList);

			// 获取额外信息
			String infoBasePath = Constant.getConstant("organInfoPath");
			String infoModelPath = infoBasePath + "/" + organId + ".xml";
			List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
			String scriptCode = ExpColUtil.getValidate(colList);
			request.setAttribute("scriptCode", scriptCode);
			request.setAttribute("colList", colList);

			return mapping.findForward(FORWARD_updOrgan);
		}
	}

	public ActionForward schOrgan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String organId = request.getParameter("organId");
		OrganForm organForm = sysService.schOrganById(organId);

		// 查询所有功能单元
		List<FunctionUnitForm> allFUnitList = sysService.schFunctionUnit(null,
				null);

		// 查询组织机构拥有的功能单元
		List<FunctionUnitForm> fUnitList = sysService
				.schFunctionUnitByOrganId(organId);

		request.setAttribute("allFUnitList", allFUnitList);
		request.setAttribute("fUnitList", fUnitList);
		request.setAttribute("organForm", organForm);
		return null;
	}

	/**
	 * 删除机构
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward delOrgan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		OrganForm organForm = (OrganForm) form;
		String organId = request.getParameter("id");
		// 查询机构的子机构,如果机构下有子机构,不能删除
		List<OrganForm> organList = sysService.schOrgan(null, " and t.pid='"
				+ organId + "'");
		if (organList != null && organList.size() > 0) {
			request.setAttribute("info", "请先删除子机构");
			return mapping.findForward(FORWARD_reply);
		}

		// 查询机构下的人员,如果存在人员,提示不能删除
		UserForm sqlUserForm = new UserForm();
		sqlUserForm.setOrganId(organId);
		List userList = sysService.schUser(sqlUserForm);
		if (userList != null && userList.size() > 0) {
			request.setAttribute("info", "先删除机构下的人员");
		}

		// 查询机构下的角色,如果有,先把角色删除
		RoleForm sqlForm = new RoleForm();
		sqlForm.setOrganId(organId);
		List<RoleForm> roleList = sysService.schRole(sqlForm);
		if (roleList != null && roleList.size() > 0) {
			for (int i = 0; i < roleList.size(); i++) {
				RoleFunction roleFunction = new RoleFunction();
				roleFunction.setRoleId(roleList.get(i).getId());
				sysService.delRoleFunction(roleFunction);
				sysService.delRole(roleList.get(i).getId());
			}
		}
		if (Constant.getBooleanConstant("usemq")) {
			String code = request.getParameter("code");
			try {
				InfoUtil.removeApp(code);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 删除额外信息
		String basePath = Constant.getConstant("organInfoPath");
		String absolutePath = basePath + organId + ".xml";
		File curFile = new File(absolutePath);
		curFile.delete();

		sysService.delOrgan(organId);
		request.setAttribute("res", "删除成功");
		return mapping.findForward(FORWARD_reply);
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	public SysManageService getSysService() {
		return sysService;
	}

	public void setSysService(SysManageService sysService) {
		this.sysService = sysService;
	}

}
