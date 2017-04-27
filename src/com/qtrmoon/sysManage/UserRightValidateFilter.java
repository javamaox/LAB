package com.qtrmoon.sysManage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.qtrmoon.common.Constant;
import com.qtrmoon.common.SystemConfigFilter;
import com.qtrmoon.sysManage.action.SysConstant;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.RoleForm;
import com.qtrmoon.sysManage.bean.UserForm;
import com.qtrmoon.sysManage.serDao.SysManageService;

public class UserRightValidateFilter implements Filter {
	private static SysManageService sysService;

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hsr = (HttpServletRequest) request;
		String myforward = hsr.getRequestURI();
		SysUtil.addRequestHistory(hsr);
		String projectName = Constant.getConstant("projectName");
		if(projectName==null){
			System.out.println("����鵱ǰworkspace·�����ã�"+Constant.getConstant("workspace"));
			if(myforward.indexOf("user_over.jsp")>0){
				chain.doFilter(request, response);
				return;
			}
			((HttpServletResponse)response).sendRedirect("sysManage/user_over.jsp");
			return;
		}
		myforward = myforward.substring(myforward.indexOf("/" + projectName
				+ "/")
				+ projectName.length() + 1);
		myforward = myforward.replaceAll("//", "/");
		if (myforward.equals("/")||myforward.indexOf(".jsp") >= 0) {//"/"�Ǹ�·��
			chain.doFilter(request, response);
			return;
		}
		
		String methodName = hsr.getParameter("method");
		if (methodName == null) {
			methodName = "";
		}
		myforward += "_method=" + methodName;

		// ������Ȩ�ޡ�
		List<FunctionForm> funcList = getBrowserFunc();
			Pattern p;
			Matcher m;
			String moban;
			String link;
			if(funcList!=null){
				for (int i = 0; i < funcList.size(); i++) {
					link = funcList.get(i).getLink();
					if (link != null && !link.equals("")) {
						moban = link.replaceAll("\\?", "_");
						p = Pattern.compile(moban);
						m = p.matcher(myforward);
						if (m.matches()) {
						chain.doFilter(request, response);
						return;
					}
				}
			}
		}
		// �ж��û��Ƿ��½���Ź�һЩ�Ƿ���ʾҳ��
		UserForm userForm = SysUtil.getCurrentUser((HttpServletRequest) request);
		if (userForm == null) {
			if(myforward.equals("/")||myforward.indexOf("user_timeout.jsp")>0||myforward.indexOf("/login.do")>0){
				chain.doFilter(request, response);
				return;
			}else{
				((HttpServletResponse)response).sendRedirect("/"+Constant.getConstant("projectName")+"/sysManage/user_timeout.jsp");
				return;
			}
		}
		//�ж��û�Ȩ��
		List<FunctionForm> functionList = null;
		functionList = userForm.getFunctionlist();
		for (int i = 0; i < functionList.size(); i++) {
			link = functionList.get(i).getLink();
			if (link != null && !link.equals("")) {
				moban = link.replaceAll("\\?", "_");
				p = Pattern.compile(moban);
				m = p.matcher(myforward);
				if (m.matches()) {
					chain.doFilter(request, response);
					return;
				}
			}
		}
//		((HttpServletResponse)response).sendRedirect("/"+Constant.getConstant("projectName")+"/sysManage/user_illegal.jsp");
		chain.doFilter(request, response);
	}

	private static List<FunctionForm> funcList = null;

	/**
	 * ��ȡ�����ɫ
	 * 
	 * @return
	 */
	private List<FunctionForm> getBrowserFunc() {
		if (funcList == null) {
			RoleForm roleForm = new RoleForm();
			roleForm.setClassify(SysConstant.ROLE_CLASSIFY_NOLIMIT);
			List<RoleForm> roleList = sysService.schRole(roleForm);
			if(roleList.size()>0){
				funcList = sysService.schFunctionsByRoleId(roleList.get(0).getId());// ��ɫΨһ
			}
		}
		return funcList;
	}

	public void init(FilterConfig config) throws ServletException {

	}

	public SysManageService getSysService() {
		return sysService;
	}

	public void setSysService(SysManageService sysService) {
		UserRightValidateFilter.sysService = sysService;
	}

}
