package com.qtrmoon.sysManage.action;
import com.qtrmoon.sysManage.serDao.SysManageService;
import com.qtrmoon.sysManage.bean.*;
import com.qtrmoon.sysManage.serDao.*;
import com.qtrmoon.common.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.actions.*;
import java.util.*;
public class MachineAction extends DispatchAction {

	private SysManageService sysService;
	private CommunalService communalService;
	private final String FORWARD_schMachine = "schMachine";
	private final String FORWARD_vieMachine = "vieMachine";
	private final String FORWARD_addMachine = "addMachine";
	private final String FORWARD_updMachine = "updMachine";
	private final String FORWARD_doSchMachine = "doSchMachine";
	public ActionForward schMachine(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) {
		MachineForm machineForm = (MachineForm) form;
		HttpSession session = request.getSession();
		String from = (String) request.getParameter("from");
		String currentPage=request.getParameter("currentPage");
		List machineList = null;
		if(from!=null && from.equals("submit")){
			//来自查询页面
			String schcfg=request.getParameter("schcfg");
			if(schcfg==null||schcfg.equals("")){//非区段
				String schTxt=request.getParameter("schTxt");
				if(schTxt.indexOf(".")>0){
					machineForm.setIp(schTxt);
				}else{
					machineForm.setMac(schTxt);
				}
			}else if(schcfg.equals("1")){//区段
				String schTxt1=request.getParameter("schTxt1");
				String schTxt2=request.getParameter("schTxt2");
				if(schTxt1.indexOf(".")>0){
					machineForm.setCondition(" and(t.ip<'"+schTxt2+"' and t.ip>'"+schTxt1+"')");
				}else{
					machineForm.setCondition(" and(t.mac<'"+schTxt2+"' and t.mac>'"+schTxt1+"')");
				}
			}
			machineList = sysService.schMachine(machineForm);
			session.setAttribute(Constant.CONDITION_IN_SESSION,machineForm);
		}else if(from!=null && from.equals("page")){
			//来自分页页码
			Object o=session.getAttribute(Constant.CONDITION_IN_SESSION);
			if(o instanceof MachineForm){
				machineForm = (MachineForm)o;
			}
			if(machineForm==null){
				machineForm = new MachineForm();
				session.setAttribute(Constant.CONDITION_IN_SESSION, machineForm);
			}
			if (currentPage != null && !currentPage.equals("")) {
				machineForm.setCurrentPage(Integer.parseInt(currentPage));
			}
			machineList = sysService.schMachine(machineForm);
		}else{
			//来自其他页面的链接
			session.setAttribute(Constant.CONDITION_IN_SESSION, new MachineForm());
		}
		request.setAttribute("machineList", machineList);
		request.setAttribute("machineForm",machineForm);
		return mapping.findForward(FORWARD_schMachine);
	}
	public ActionForward vieMachine(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) {
		String id = (String) request.getParameter("id");
		String realId = id;
		MachineForm machineForm = sysService.schMachineById(realId);
		request.setAttribute("machineForm", machineForm);
		return mapping.findForward(FORWARD_vieMachine);
	}
	public ActionForward addMachine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {// add
			MachineForm machineForm = (MachineForm) form;
			sysService.addMachine(machineForm);
			request.setAttribute("info", "添加机器MacIp绑定交验成功！");
			return mapping.findForward(FORWARD_addMachine);
		} else {// preAdd
			MachineForm machineForm = new MachineForm();
			request.setAttribute("machineForm", machineForm);
		}
		return mapping.findForward(FORWARD_addMachine);
	}
	public ActionForward updMachine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {
			MachineForm machineForm = (MachineForm) form;
			sysService.updMachine(machineForm);
			request.setAttribute("info", "修改成功！");
			return mapping.findForward(FORWARD_doSchMachine);
		} else {
			String id = request.getParameter("id");
		String realId = id;
			MachineForm machineForm = sysService.schMachineById(realId);
			request.setAttribute("machineForm", machineForm);
			return mapping.findForward(FORWARD_updMachine);
		}
	}
	public ActionForward delMachine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		sysService.delMachine(id);
		request.setAttribute("info", "删除成功");
		return mapping.findForward(FORWARD_doSchMachine);
	}

	public SysManageService getSysService() {
		return sysService;
	}
	public void setSysService(SysManageService sysService) {
		this.sysService = sysService;
	}
	public CommunalService getCommunalService() {
		return communalService;
	}
	public void setCommunalService(CommunalService communalService) {
		this.communalService = communalService;
	}
}
