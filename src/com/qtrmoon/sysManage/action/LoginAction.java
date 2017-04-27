package com.qtrmoon.sysManage.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.qtrmoon.common.Constant;
import com.qtrmoon.sysManage.SysUtil;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.MachineForm;
import com.qtrmoon.sysManage.bean.ModuleForm;
import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.sysManage.bean.RemindBean;
import com.qtrmoon.sysManage.bean.UserForm;
import com.qtrmoon.sysManage.bean.XmlCol;
import com.qtrmoon.sysManage.serDao.SysManageService;
import com.qtrmoon.toolkit.FileUpDownUtil;
import com.qtrmoon.toolkit.MD5;
import com.qtrmoon.toolkit.tree.TreeNode;

public class LoginAction extends DispatchAction {

	private static SysManageService sysService;

	private static final String FORWARD_login = "login";
	private static final String FORWARD_login_icon = "login_icon";

	private static final String FORWARD_logout = "logout";

	private static final String FORWARD_toLogin = "toLogin";

	private static final String FORWARD_menu = "menu";

	private static final String FORWARD_title = "title";

	private static final String FORWARD_toRegister = "toRegister";

	private static final String FORWARD_bottom = "bottom";

	private static final String FORWARD_remind = "remind";

	private static final String FORWARD_showSub = "showSub";

	private static final String FORWARD_iconNav = "iconNav";

	/**
	 * 用户登录
	 */
	public ActionForward login(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserForm userForm = (UserForm) form;
		if (userForm.getLoginName() == null
				|| userForm.getLoginName().equals("")
				|| userForm.getPassword() == null
				|| userForm.getPassword().equals("")) {
			String from = request.getParameter("from");
			if (from != null && from.equals("out")) {

			} else {
				//
				request.setAttribute("info", "请填写用户名密码！");
			}
			return mapping.findForward(FORWARD_logout);
		}
		UserForm sqlForm = new UserForm();
		sqlForm.setLoginName(userForm.getLoginName());
		List<UserForm> userList = sysService.schUser(sqlForm);
		if (userList.size() > 0) {
			UserForm loginUser = userList.get(0);
			String psw = userForm.getPassword();
			if (Constant.getBooleanConstant("sysManage.pswmd5")) {
				psw = MD5.getInstance().getMD5to32(psw);
			}
			if (loginUser.getPassword().equals(psw)) {
				boolean hfmachine=true;
				if(Constant.getBooleanConstant("sysManage.useLimitMachine")){
					//机器绑定校验，登陆机器的mac与ip对与库中相应机构的机器码匹配才能通过。
					String mac=request.getParameter("mac");
					MachineForm machineForm=new MachineForm();
					machineForm.setMac(mac);
					machineForm.setOrganid(loginUser.getOrganId());
					List<MachineForm> l=sysService.schMachine(machineForm);
					String ip=request.getRemoteAddr();
					if(l.size()>0&&l.get(0).getIp().equals(ip)){
						hfmachine=true;
					}else{
						hfmachine=false;
					}
				}
				if(hfmachine){
					List<FunctionForm> functionList = sysService.schFunctionsByUserId(loginUser.getId());
				// 过滤隐藏权限
				List<FunctionForm> menuList = new ArrayList<FunctionForm>();
				for (int i = 0; i < functionList.size(); i++) {
					if (functionList.get(i).getIsShow().equals(SysConstant.FUNCTION_SHOW)) {
						menuList.add(functionList.get(i));
					}
				}
				(new FunctionTreeUtil()).getOrderTree(menuList);
				HttpSession session = request.getSession(true);
				loginUser.setFunctionlist(functionList);
				loginUser.setLoginIp(request.getRemoteAddr());
				session.setAttribute(SysUtil.CURRENT_USER, loginUser);
				String operMode=Constant.getConstant("operMode");
				if("icon".equals(operMode)){
					return mapping.findForward(FORWARD_login_icon);
				}else{
					return mapping.findForward(FORWARD_login);
				}
				}else{
					request.setAttribute("info", "请使用合法机器登陆系统");
				}
			}else{
				request.setAttribute("info", "用户名或密码不正确");
			}
		}
		userForm = null;
		request.setAttribute("userForm", userForm);
		return mapping.findForward(FORWARD_logout);
	}

	/**
	 * 登陆后图标页面导航
	 */
	public ActionForward iconNav(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserForm user=SysUtil.getCurrentUser(request);
		List<ModuleForm> resList = new ArrayList<ModuleForm>();
		List<FunctionForm> flist = user.getFunctionlist();
		ModuleForm sqlForm = new ModuleForm();
		sqlForm.addOrderCol("ord");
		List<ModuleForm> mlist = sysService.schModule(sqlForm);
		String funids;
		String[] funidArr;
		for(ModuleForm mod:mlist){
			funids=mod.getFuncs();
			if(funids.indexOf(",")>0){
				funidArr=funids.split(",");
			}else{
				funidArr=new String[]{funids};
			}
			Arrays.sort(funidArr);
			for(FunctionForm fun:flist){
				if(Arrays.binarySearch(funidArr, fun.getId())>=0){
					resList.add(mod);
					break;
				}
			}
		}
		request.setAttribute("moduleList", resList);
		//
		if (remindList == null) {
			try {
				remindList = new ArrayList<RemindBean>();
				String path = LoginAction.class.getResource("/").getPath()
						+ "remind-config.xml";
				path = path.replaceAll("%20", " ");
				Element groupERoot;
				groupERoot = (new SAXBuilder(false)).build(path)
						.getRootElement();
				List<Element> elemList = groupERoot.getChildren("remind");
				for (Element elem : elemList) {
					remindList
							.add(new RemindBean(elem.getChildText("id"), elem
									.getChildText("link"), elem
									.getChildText("ajaxlink"), elem
									.getChildText("ico"), elem
									.getChildText("text")));
				}
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("list", remindList);
		
		return mapping.findForward(FORWARD_iconNav);
	}
	
	
	/**
	 * 用户注销
	 */
	public ActionForward logout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.setAttribute(SysUtil.CURRENT_USER, null);
		return mapping.findForward(FORWARD_toLogin);
	}

	/**
	 * 显示权限菜单，转向menu.jsp
	 */
	public ActionForward menu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserForm userForm = SysUtil.getCurrentUser(request);
		List<FunctionForm> funcList = userForm.getFunctionlist();
		if (funcList.size() > 0) {
			FunctionTreeUtil funcTreeUtil = new FunctionTreeUtil();
			FunctionForm menuRoot = (FunctionForm) funcTreeUtil.getTree(funcList);
			String operMode=Constant.getConstant("operMode");
			request.setAttribute("operMode", operMode);
			if("icon".equals(operMode)){
				String moduleId=request.getParameter("moduleId");
				ModuleForm module=sysService.schModuleById(moduleId);
				String targetFuncs=module.getFuncs();
				String[] funcArr;
				if(targetFuncs.indexOf(",")>0){
					funcArr=targetFuncs.split(",");
				}else{
					funcArr=new String[]{targetFuncs};
				}
				List<FunctionForm> resFunc=new ArrayList<FunctionForm>();
				for(FunctionForm fun:funcList){
					if(Arrays.binarySearch(funcArr, fun.getId())>=0){
						resFunc.add(fun);
					}
				}
				request.setAttribute("menu",resFunc);
			}else{
				request.setAttribute("menu", menuRoot.getCnodeList());
			}
		}
		return mapping.findForward(FORWARD_menu);
	}

	/**
	 * 查询title，依function的order序
	 * 
	 * @return
	 */
	public ActionForward title(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String funcId = request.getParameter("id");
		String showIco = request.getParameter("showIco");//是否显示图标导航页面
		if("true".equals(showIco)){//将首连接置为图标导航地址。
			request.setAttribute("link", "/sysManage/login.do?method=showSub&id="+funcId);
			return mapping.findForward(FORWARD_title);
		}
		UserForm user = SysUtil.getCurrentUser(request);
		if(user==null){
			return mapping.findForward(FORWARD_toLogin);
		}
		List<FunctionForm> funcList=user.getFunctionlist();
		List<TreeNode> childList=null;
		List<FunctionForm> functionList = new ArrayList<FunctionForm>();
		for(FunctionForm func:funcList){
			if(funcId.equals(func.getId())){
				childList=func.cnodeList;
			}
		}
		for(TreeNode node:childList){
			FunctionForm func=(FunctionForm)node;
			if(func.getIsShow().equals("1")){
				functionList.add(func);
			}
		}
		request.setAttribute("functionList", functionList);
		FunctionForm funcForm = null;
		if (functionList.size() == 0) {
			// 如果没有三级菜单则使用当前二级的连接刷新
			funcForm = sysService.schFunctionById(funcId);
		} else {
			// 使用第一个Title链接刷新。
			funcForm = functionList.get(0);
		}
		request.setAttribute("link", funcForm.getLink());
		return mapping.findForward(FORWARD_title);
	}

	/**
	 * 用户注册
	 */
	public ActionForward userRegister(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserForm userForm = (UserForm) form;
		String from = request.getParameter("from");
		if (from != null && from.equals("submit")) {

			UserForm sqlForm = new UserForm();
			sqlForm.setLoginName(userForm.getLoginName());
			List list = sysService.schUser(sqlForm);
			if (list != null && list.size() > 0) {
				userForm = null;
				request.setAttribute("userForm", userForm);
				request.setAttribute("info", "登陆名已被使用,请重新设置!");
				// 查询组织机构
				OrganForm organForm = new OrganForm();
				organForm.setCode(Constant.getConstant("sysid") + "%");
				List<OrganForm> organList = sysService
						.schOrgan(organForm, null);
				request.setAttribute("organList", organList);

				// 获取额外信息
				String infoBasePath = Constant.getConstant("userInfoPath");
				String infoModelPath = infoBasePath + "/userInfoModel.xml";
				List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
				String scriptCode = ExpColUtil.getValidate(colList);
				request.setAttribute("scriptCode", scriptCode);
				request.setAttribute("colList", colList);
				return mapping.findForward(FORWARD_toRegister);
			}
			userForm.setState(SysConstant.USER_AUDIT);
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

			userForm = null;
			request.setAttribute("userForm", userForm);
			request.setAttribute("info", "注册成功");
		} else {
			// 查询组织机构
			OrganForm organForm = new OrganForm();
			organForm.setCode(Constant.getConstant("sysid") + "%");
			List<OrganForm> organList = sysService.schOrgan(organForm, null);
			request.setAttribute("organList", organList);

			// 获取额外信息
			String infoBasePath = Constant.getConstant("userInfoPath");
			String infoModelPath = infoBasePath + "/userInfoModel.xml";
			List<XmlCol> colList = ExpColUtil.getExpCol(infoModelPath);
			String scriptCode = ExpColUtil.getValidate(colList);
			request.setAttribute("scriptCode", scriptCode);
			request.setAttribute("colList", colList);
			return mapping.findForward(FORWARD_toRegister);
		}
		return mapping.findForward(FORWARD_logout);
	}

	public ActionForward bottom(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userName = SysUtil.getCurrentUser(request).getUserName();
		String organName = SysUtil.getCurrentOrgan(request).getName();
		request.setAttribute("userName", userName);
		request.setAttribute("organName", organName);
		return mapping.findForward(FORWARD_bottom);
	}

	/**
	 * 提醒
	 */
	public static List<RemindBean> remindList = null;

	public ActionForward remind(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (remindList == null) {
			try {
				remindList = new ArrayList<RemindBean>();
				String path = LoginAction.class.getResource("/").getPath()
						+ "remind-config.xml";
				path = path.replaceAll("%20", " ");
				Element groupERoot;
				groupERoot = (new SAXBuilder(false)).build(path)
						.getRootElement();
				List<Element> elemList = groupERoot.getChildren("remind");
				for (Element elem : elemList) {
					remindList
							.add(new RemindBean(elem.getChildText("id"), elem
									.getChildText("link"), elem
									.getChildText("ajaxlink"), elem
									.getChildText("ico"), elem
									.getChildText("text")));
				}
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("list", remindList);
		return mapping.findForward(FORWARD_remind);
	}
	
	public ActionForward showSub(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		UserForm user = SysUtil.getCurrentUser(request);
		List<FunctionForm> funcList = user.getFunctionlist();
		List<FunctionForm> resList = new ArrayList<FunctionForm>();
		for(FunctionForm fun:funcList){
			if(fun.getPid()!=null&&fun.getIsShow().equals(SysConstant.FUNCTION_SHOW)&&fun.getPid().equals(id)){
				resList.add(fun);
			}
		}
		TreeNode node,obj;
		int index=-1;
		for (int i = 0; i < resList.size() - 1; i++) {
			node = (TreeNode) resList.get(i);
			Integer order = node.getOrder();
			for (int m = i + 1; m < resList.size(); m++) {
				TreeNode node1 = (TreeNode) resList.get(m);
				if (order > node1.getOrder()) {
					order = node1.getOrder();
					index = m;
				}
			}
			if (index > 0) {
				obj = resList.get(i);
				resList.set(i, resList.get(index));
				resList.set(index, (FunctionForm)obj);
			}
			index = -1;
		}
		request.setAttribute("funcList", resList);
		return mapping.findForward(FORWARD_showSub);
	}

	public SysManageService getSysService() {
		return sysService;
	}

	public void setSysService(SysManageService sysService) {
		this.sysService = sysService;
	}

	public static SysManageService getService() {
		return sysService;
	}

}
