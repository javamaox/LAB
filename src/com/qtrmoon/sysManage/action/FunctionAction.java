package com.qtrmoon.sysManage.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.qtrmoon.dictEditor.DictBuffer;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.FunctionUnitForm;
import com.qtrmoon.sysManage.bean.RoleForm;
import com.qtrmoon.sysManage.serDao.SysManageService;

public class FunctionAction extends DispatchAction {
	private static final String FORWARD_updFunction = "updFunction";

	private static final String FORWARD_schFunctionTree = "schFunctionTree";

	private static final String FORWARD_menu = "menu";

	private static final String FORWARD_title = "title";

	private static final String FORWARD_addFunction = "addFunction";

	private static final String FORWARD_reply = "reply";

	private SysManageService sysService;

	public ActionForward schFunctionById(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String funcId = request.getParameter("id");
		FunctionForm functionForm = sysService.schFunctionById(funcId);
		request.setAttribute("functionForm", functionForm);
		return mapping.findForward(FORWARD_updFunction);
	}

	public ActionForward addFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {// add
			FunctionForm functionForm = (FunctionForm) form;
			sysService.addFunction(functionForm);
			request.setAttribute("res", "添加功能成功！");
			return mapping.findForward(FORWARD_reply);
		} else {// preAdd
			String parentId = request.getParameter("id");
			FunctionForm functionForm = new FunctionForm();
			functionForm.setPid(parentId);
			functionForm.setIsShow(SysConstant.FUNCTION_SHOW);
			request.setAttribute("functionForm", functionForm);
		}
		return mapping.findForward(FORWARD_addFunction);
	}

	/**
	 * 删除功能，当角色或功能单元使用了本功能的话，给与提示，不做删除。
	 */
	public ActionForward delFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String funcId = request.getParameter("id");
		// 查询拥有本功能的角色
		List<RoleForm> roleList = sysService.schRoleByFunction(funcId);
		if (roleList.size() > 0) {
			request.setAttribute("info", "该功能已被角色使用！");
			request.setAttribute("roleList", roleList);
			return mapping.findForward(FORWARD_reply);
		}
		// 查询拥有本功能的功能单元
		List<FunctionUnitForm> functionUnitList = sysService
				.schFunctionUnitByFunctionId(funcId);
		if (functionUnitList.size() > 0) {
			request.setAttribute("info", "该功能已被功能单元使用！");
			request.setAttribute("functionUnitList", functionUnitList);
			return mapping.findForward(FORWARD_reply);
		}
		// 判断是否拥有子功能
		List<FunctionForm> funList = sysService.schFunction(null,
				" and t.pid='" + funcId + "'");
		if (funList != null && funList.size() > 0) {
			request.setAttribute("info", "请先删除子功能!");
			return mapping.findForward(FORWARD_reply);
		}
		sysService.delFunction(funcId);
		request.setAttribute("res", "删除成功");
		return mapping.findForward(FORWARD_reply);
	}
	//递归删除
	private void deleteFunction(String funcId){
		List<FunctionForm> funList = sysService.schFunction(null,
				" and t.pid='" + funcId + "'");
		for(FunctionForm fun:funList){
			deleteFunction(fun.getId());
		}
		sysService.delFunction(funcId);
	}
	public ActionForward updFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {
			FunctionForm functionForm = (FunctionForm) form;
			sysService.updFunction(functionForm);
			request.setAttribute("res", "功能修改成功！");
		} else {
			String funcId = request.getParameter("funcId");
			FunctionForm functionForm = sysService.schFunctionById(funcId);
			request.setAttribute("functionForm", functionForm);
		}
		return mapping.findForward(FORWARD_updFunction);
	}

	/**
	 * 更新功能树，在配置功能时使用。
	 */
	public ActionForward freshFunctionTree(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DictBuffer.updCache("sys_function");
		return mapping.findForward(FORWARD_schFunctionTree);
	}

	/**
	 * 向上移
	 */
	public ActionForward upFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		FunctionForm funcForm = (FunctionForm) form;
		String funcId = request.getParameter("id");// ID
		FunctionForm thisFunc = sysService.schFunctionById(funcId);
		String funcPid = thisFunc.getPid();// 父ID
		// 查出该功能单元及其同级所有功能的信息
		List<FunctionForm> brotherFunc = sysService.schFunction(null,
				" and t.pid='" + funcPid + "'");
		sortById(brotherFunc);// 对功能进行排序
		long orgLocation = 0;// 功能当前位置
		long order = 0;// 功能排序号

		FunctionForm nextFunc = null;
		if (brotherFunc.size() > 0) {
			for (int i = 0; i < brotherFunc.size(); i++) {
				if (brotherFunc.get(i).getId().equals(funcId)) {
					orgLocation = i;
					break;
				}
			}
			order = thisFunc.getOrd();
			if (orgLocation - 1 >= 0) {
				// 获得下一个组织机构信息
				nextFunc = brotherFunc.get((int) orgLocation - 1);
				// 交换组织机构的排序号
				if (nextFunc.getOrd() == order) {
					nextFunc.setOrd(order + 1);
				} else {
					thisFunc.setOrd(nextFunc.getOrd());
					nextFunc.setOrd(order);
				}
				sysService.updFunction(thisFunc);
				sysService.updFunction(nextFunc);
				request.setAttribute("res", "移动成功");
			} else {
				request.setAttribute("info", "不能再向上移动");
			}
		} else {
			request.setAttribute("info", "根节点不能移动！");
		}
		return new ActionForward("/function.do?method=updFunction&funcId="
				+ thisFunc.getId());
	}

	/**
	 * 向下移
	 */
	public ActionForward downFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String funcId = request.getParameter("id");// ID
		FunctionForm thisFunc = sysService.schFunctionById(funcId);
		String funcPid = thisFunc.getPid();// 父ID
		// 查出该功能单元及其同级所有功能的信息
		List<FunctionForm> brotherFunc = sysService.schFunction(null,
				" and t.pid='" + funcPid + "'");
		sortById(brotherFunc);// 对功能进行排序
		long orgLocation = 0;// 该功能当前位置
		long order = 0;// 该功能排序号

		FunctionForm nextFunc = null;
		if (brotherFunc.size() > 0) {
			for (int i = 0; i < brotherFunc.size(); i++) {
				if (brotherFunc.get(i).getId().equals(funcId)) {
					orgLocation = i;
					break;
				}
			}
			order = thisFunc.getOrd();
			if (orgLocation + 1 < brotherFunc.size()) {
				// 获得下一个组织机构信息
				nextFunc = brotherFunc.get((int) orgLocation + 1);
				// 交换组织机构的排序号
				if (nextFunc.getOrd() == order) {
					thisFunc.setOrd(order + 1);
				} else {
					thisFunc.setOrd(nextFunc.getOrd());
				}
				nextFunc.setOrd(order);

				sysService.updFunction(thisFunc);
				sysService.updFunction(nextFunc);
				request.setAttribute("res", "移动成功");
			} else {
				request.setAttribute("info", "不能再向下移动");
			}
		} else {
			request.setAttribute("info", "根节点不能移动！");
		}
		return new ActionForward("/function.do?method=updFunction&funcId="
				+ thisFunc.getId());
	}

	/** 排序 */
	private void sortById(List list) {
		FunctionForm temp = null;
		long n;
		Object o = null;
		int index = -1;
		for (int i = 0; i < list.size() - 1; i++) {
			n = ((FunctionForm) list.get(i)).getOrd();
			for (int j = i + 1; j < list.size(); j++) {
				temp = (FunctionForm) list.get(j);
				if (n > temp.getOrd()) {
					n = temp.getOrd();
					index = j;
				}
			}
			// 交换i<->index;
			if (index > 0) {
				o = list.get(i);
				list.set(i, list.get(index));
				list.set(index, o);
			}
			index = -1;
		}
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	public SysManageService getSysService() {
		return sysService;
	}

	public void setSysService(SysManageService sysService) {
		this.sysService = sysService;
	}

}
