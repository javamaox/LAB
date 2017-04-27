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
			request.setAttribute("res", "��ӹ��ܳɹ���");
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
	 * ɾ�����ܣ�����ɫ���ܵ�Ԫʹ���˱����ܵĻ���������ʾ������ɾ����
	 */
	public ActionForward delFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String funcId = request.getParameter("id");
		// ��ѯӵ�б����ܵĽ�ɫ
		List<RoleForm> roleList = sysService.schRoleByFunction(funcId);
		if (roleList.size() > 0) {
			request.setAttribute("info", "�ù����ѱ���ɫʹ�ã�");
			request.setAttribute("roleList", roleList);
			return mapping.findForward(FORWARD_reply);
		}
		// ��ѯӵ�б����ܵĹ��ܵ�Ԫ
		List<FunctionUnitForm> functionUnitList = sysService
				.schFunctionUnitByFunctionId(funcId);
		if (functionUnitList.size() > 0) {
			request.setAttribute("info", "�ù����ѱ����ܵ�Ԫʹ�ã�");
			request.setAttribute("functionUnitList", functionUnitList);
			return mapping.findForward(FORWARD_reply);
		}
		// �ж��Ƿ�ӵ���ӹ���
		List<FunctionForm> funList = sysService.schFunction(null,
				" and t.pid='" + funcId + "'");
		if (funList != null && funList.size() > 0) {
			request.setAttribute("info", "����ɾ���ӹ���!");
			return mapping.findForward(FORWARD_reply);
		}
		sysService.delFunction(funcId);
		request.setAttribute("res", "ɾ���ɹ�");
		return mapping.findForward(FORWARD_reply);
	}
	//�ݹ�ɾ��
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
			request.setAttribute("res", "�����޸ĳɹ���");
		} else {
			String funcId = request.getParameter("funcId");
			FunctionForm functionForm = sysService.schFunctionById(funcId);
			request.setAttribute("functionForm", functionForm);
		}
		return mapping.findForward(FORWARD_updFunction);
	}

	/**
	 * ���¹������������ù���ʱʹ�á�
	 */
	public ActionForward freshFunctionTree(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DictBuffer.updCache("sys_function");
		return mapping.findForward(FORWARD_schFunctionTree);
	}

	/**
	 * ������
	 */
	public ActionForward upFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		FunctionForm funcForm = (FunctionForm) form;
		String funcId = request.getParameter("id");// ID
		FunctionForm thisFunc = sysService.schFunctionById(funcId);
		String funcPid = thisFunc.getPid();// ��ID
		// ����ù��ܵ�Ԫ����ͬ�����й��ܵ���Ϣ
		List<FunctionForm> brotherFunc = sysService.schFunction(null,
				" and t.pid='" + funcPid + "'");
		sortById(brotherFunc);// �Թ��ܽ�������
		long orgLocation = 0;// ���ܵ�ǰλ��
		long order = 0;// ���������

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
				// �����һ����֯������Ϣ
				nextFunc = brotherFunc.get((int) orgLocation - 1);
				// ������֯�����������
				if (nextFunc.getOrd() == order) {
					nextFunc.setOrd(order + 1);
				} else {
					thisFunc.setOrd(nextFunc.getOrd());
					nextFunc.setOrd(order);
				}
				sysService.updFunction(thisFunc);
				sysService.updFunction(nextFunc);
				request.setAttribute("res", "�ƶ��ɹ�");
			} else {
				request.setAttribute("info", "�����������ƶ�");
			}
		} else {
			request.setAttribute("info", "���ڵ㲻���ƶ���");
		}
		return new ActionForward("/function.do?method=updFunction&funcId="
				+ thisFunc.getId());
	}

	/**
	 * ������
	 */
	public ActionForward downFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String funcId = request.getParameter("id");// ID
		FunctionForm thisFunc = sysService.schFunctionById(funcId);
		String funcPid = thisFunc.getPid();// ��ID
		// ����ù��ܵ�Ԫ����ͬ�����й��ܵ���Ϣ
		List<FunctionForm> brotherFunc = sysService.schFunction(null,
				" and t.pid='" + funcPid + "'");
		sortById(brotherFunc);// �Թ��ܽ�������
		long orgLocation = 0;// �ù��ܵ�ǰλ��
		long order = 0;// �ù��������

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
				// �����һ����֯������Ϣ
				nextFunc = brotherFunc.get((int) orgLocation + 1);
				// ������֯�����������
				if (nextFunc.getOrd() == order) {
					thisFunc.setOrd(order + 1);
				} else {
					thisFunc.setOrd(nextFunc.getOrd());
				}
				nextFunc.setOrd(order);

				sysService.updFunction(thisFunc);
				sysService.updFunction(nextFunc);
				request.setAttribute("res", "�ƶ��ɹ�");
			} else {
				request.setAttribute("info", "�����������ƶ�");
			}
		} else {
			request.setAttribute("info", "���ڵ㲻���ƶ���");
		}
		return new ActionForward("/function.do?method=updFunction&funcId="
				+ thisFunc.getId());
	}

	/** ���� */
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
			// ����i<->index;
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
