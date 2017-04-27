package com.qtrmoon.sysManage.transact;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.qtrmoon.common.Constant;
import com.qtrmoon.infoTransact.InfoUtil;
import com.qtrmoon.sysManage.SysUtil;
import com.qtrmoon.sysManage.bean.OrganForm;

public class InfoAction extends DispatchAction {

	/**
	 * 同步组织机构
	 */
	public ActionForward synchOrgan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("00000000000");
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		if (Constant.getBooleanConstant("usemq")) {
			if (InfoUtil.firstrun) {
				firstRegist();
				InfoUtil.firstrun = false;
			}
			// 如果配置使用mq则查询系统机构，和MQ中获取的机构进行同步。
			List<OrganForm> organList = SysUtil.getOrgan(null, null);
			List<OrganNode> mqList = InfoUtil.getOrganList("1at1");
			OrganTreeUtil organTreeUtil = new OrganTreeUtil();
			OrganNode troot = (OrganNode) organTreeUtil.getOrderTree(mqList);

			// 对比MQ中与本地库中机构,把MQ中多的机构存入本地数据库
			contrastChild(organList, troot);

			// 如果库中的机构比MQ中多,删除多出的机构
			boolean isUse = false;
			List<OrganForm> newOrganList = SysUtil.getOrgan(null, null);
			for (int i = 0; i < newOrganList.size(); i++) {
				OrganForm organForm = newOrganList.get(i);
				for (int j = 0; j < mqList.size(); j++) {
					// OrganNode中的id为机构的code;
					if (organForm.getCode().equals(mqList.get(j).getId())) {
						isUse = true;
						break;
					}
				}
				if (!isUse) {
					SysUtil.delOrgan(organForm.getId());
				}
				isUse = false;
			}

		}
		return null;
	}

	/**
	 * 对比机构
	 * 
	 * @param organList;//本地机构List
	 * @param node
	 */
	public void contrastOrgan(List<OrganForm> organList, OrganNode node) {
		boolean isExist = false;

		for (int j = 0; j < organList.size(); j++) {
			if (node.getId().equals(organList.get(j).getCode())) {
				isExist = true;
				break;
			}
		}
		if (isExist) {
			isExist = false;
		} else {
			// 往库中加机构
			OrganForm organForm = new OrganForm();
			organForm.setName(node.getName());
			List<OrganForm> organFormList;
			if (node.getParentid() == null || node.getParentid().equals("")) {
				// 空则为总机构

			} else {
				organFormList = SysUtil.getOrgan(null, " and t.code='"
						+ node.getParentid() + "'");
				if (organFormList.size() > 0) {
					// 本级有其父，转父Code为本级的Id
					organForm.setPid(organFormList.get(0).getId());
				} else {
					// 本级没有其父
					organForm.setPid("code_" + node.getParentid());
				}
			}
			organForm.setCode(node.getId());
			organForm.setType(node.getType());
			String newId = SysUtil.addOrgan(organForm);
			// 对新加入的机构的code，是否是本级未拼挂pid的机构（pid=code_code）。
			organFormList = SysUtil.getOrgan(null, " and t.pid like '%/_%' escape '/'");
			String pid;
			for (int i = 0; i < organFormList.size(); i++) {
				organForm = organFormList.get(i);
				pid = organForm.getPid();
				if(pid.indexOf("code_")>=0){
					//此记录是同步时没有找到父的纪录
					pid = pid.substring(5);
				}else{
					//此记录是初始就有_的记录，即本级的根。记录的是父级的code
					;
				}
				if (pid.equals(node.getId())) {
					organForm.setPid(newId);
					SysUtil.updOrgan(organForm);
				}
			}
		}
	}

	/**
	 * 遍历机构
	 * 
	 * @param organList
	 * @param node
	 */
	public void contrastChild(List<OrganForm> organList, OrganNode node) {
		contrastOrgan(organList, node);
		List list = node.getCnodeList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				OrganNode organNode = (OrganNode) list.get(i);
				contrastChild(organList, organNode);
			}
		}
	}

	public static void firstRegist() {
		// 第一次启动会注册本地机构一次。
		List<OrganForm> organList = SysUtil.getOrgan(null, " and t.code like '"
				+ Constant.getConstant("sysid") + "%'");
		OrganForm organ;
		try {
			List<OrganForm> orgList = null;
			for (int i = 0; i < organList.size(); i++) {
				// 注册机构
				organ = organList.get(i);
				String name = organ.getName();
				String code = organ.getCode();
				String type = organ.getType();
				String pid = organ.getPid();
				// 取父机构，获得父机构的code
				String key = "";
				if (pid != null && !pid.equals("")) {
					orgList = SysUtil.getOrgan(null, " and t.id = '" + pid
							+ "'");
					if (orgList.size() > 0) {
						key = orgList.get(0).getCode() + "," + name + ","
								+ type;
					} else {
						// pid不空且找不到对应id为此pid的机构，则为本级根机构。
						key = pid + "," + name + "," + type;
					}
				} else {
					key = "," + name + "," + type;
				}
				InfoUtil.registApp(code, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Format fo = Format.getCompactFormat();// 取得输出格式Format
		// fo.setEncoding("GB2312");
		// XMLOutputter outputter = new XMLOutputter(fo);
		// FileWriter write = new FileWriter(path);
		// outputter.output(doc, write);
		// write.close();
	}

}
