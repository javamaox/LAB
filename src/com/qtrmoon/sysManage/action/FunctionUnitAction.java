package com.qtrmoon.sysManage.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.FunctionUnitForm;
import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.sysManage.bean.UnitFunction;
import com.qtrmoon.sysManage.serDao.SysManageService;

public class FunctionUnitAction extends DispatchAction {
	private static final String FORWARD_updFunctionUnit = "updFunctionUnit";

	private static final String FORWARD_addFunctionUnit = "addFunctionUnit";

	private static final String FORWARD_schFunctionUnit = "schFunctionUnit";

	private static final String FORWARD_doSchFunctionUnit = "/sysManage/organ.do?method=schFunctionUnit";

	private static final String FORWARD_reply = "reply";

	private SysManageService sysService;

	public ActionForward addFunctionUnit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		FunctionUnitForm functionUnitForm = (FunctionUnitForm) form;
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {// add
			String unitId = sysService.addFunctionUnit(functionUnitForm);
			
			if (unitId != null && !unitId.equals("")) {
				// ��ȡ���ܵ�Ԫӵ�еĹ���ID
				String[] functionIds = request.getParameter("multi").split(",");
				addUnit(functionUnitForm.getId(),functionIds);
				request.setAttribute("res", "��ӹ��ܳɹ���");
			} else {
				request.setAttribute("res", "��ӹ���ʧ�ܣ�");
			}
			return mapping.findForward(FORWARD_reply);
		} else {// preAdd
			return mapping.findForward(FORWARD_addFunctionUnit);
		}
	}

	public ActionForward updFunctionUnit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {
			FunctionUnitForm functionUnitForm = (FunctionUnitForm) form;
			sysService.updFunctionUnit(functionUnitForm);

			// ��ɾ���ù��ܵ�Ԫ���м���еļ�¼
			UnitFunction unitFunction = new UnitFunction();
			unitFunction.setUnitId(functionUnitForm.getId());
			sysService.delUnitFunction(unitFunction);
			
			//��ӹ��ܵ�Ԫ
			String[] functionIds = request.getParameter("multi").split(",");
			addUnit(functionUnitForm.getId(),functionIds);
			
			return mapping.findForward(FORWARD_doSchFunctionUnit);
		} else {
			String funcUnitId = request.getParameter("funcUnitId");
			FunctionUnitForm functionUnitForm = sysService.schFunctionUnitById(funcUnitId);

			List<FunctionForm> funcFormList = sysService.schFunctionsByFunctionUnitId(funcUnitId);
			String funcIds = "";
			if (funcFormList.size() > 0) {
				for (int i = 0; i < funcFormList.size(); i++) {
					funcIds += funcFormList.get(i).getId() + ",";
				}
			}
			if (!funcIds.equals("")) {
				funcIds = funcIds.substring(0, funcIds.length() - 1);
			}
			request.setAttribute("funcIds", funcIds);
			request.setAttribute("functionUnitForm", functionUnitForm);
			return mapping.findForward(FORWARD_updFunctionUnit);
		}
	}

	/**
	 * ��ӹ��ܵ�Ԫ�������ܹ�ϵ������
	 * @param functionUnitId ���ܵ�Ԫ
	 * @param functionIds ����id����
	 */
	private void addUnit(String functionUnitId,String[] functionId) {
		UnitFunction unitFunction = new UnitFunction();
		unitFunction.setUnitId(functionUnitId);
		if (functionId != null) {
			for (int i = 0; i < functionId.length; i++) {
				List<FunctionForm> funcList = sysService.schFunction(null," and t.pid='" + functionId[i] + "'");
				if (funcList != null) {
					for (int j = 0; j < funcList.size(); j++) {
						if (funcList.get(j).getIsShow().equals(SysConstant.FUNCTION_HIDE)) {
							unitFunction.setFunctionId(funcList.get(j).getId());
							sysService.addUnitFunction(unitFunction);
						}
					}
				}
				unitFunction.setFunctionId(functionId[i]);
				sysService.addUnitFunction(unitFunction);
			}
		}
	}

	public ActionForward delFunctionUnit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String funcUnitId = request.getParameter("funcUnitId");

		List<OrganForm> organList = sysService.schOrganByUnitId(funcUnitId);
		if (organList != null && organList.size() > 0) {
			request.setAttribute("info", "�ù��ܵ�Ԫ�ѱ�ʹ��");
		} else {
			UnitFunction unitFunction = new UnitFunction();
			unitFunction.setUnitId(funcUnitId);
			// ��ɾ�����ܵ�Ԫ-�����м�������д˹��ܵ�Ԫ����Ϣ
			sysService.delUnitFunction(unitFunction);
			// ɾ�����ܵ�Ԫ
			sysService.delFunctionUnit(funcUnitId);
			request.setAttribute("res", "ɾ���ɹ�");
		}
		return mapping.findForward(FORWARD_reply);
	}

	public ActionForward schFunctionUnit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		List<FunctionUnitForm> functionUnitList = sysService.schFunctionUnit(
				null, null);
		request.setAttribute("functionUnitList", functionUnitList);
		return mapping.findForward(FORWARD_schFunctionUnit);
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	public SysManageService getSysService() {
		return sysService;
	}

	public void setSysService(SysManageService sysService) {
		this.sysService = sysService;
	}

}
