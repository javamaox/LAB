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
	 * ͬ����֯����
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
			// �������ʹ��mq���ѯϵͳ��������MQ�л�ȡ�Ļ�������ͬ����
			List<OrganForm> organList = SysUtil.getOrgan(null, null);
			List<OrganNode> mqList = InfoUtil.getOrganList("1at1");
			OrganTreeUtil organTreeUtil = new OrganTreeUtil();
			OrganNode troot = (OrganNode) organTreeUtil.getOrderTree(mqList);

			// �Ա�MQ���뱾�ؿ��л���,��MQ�ж�Ļ������뱾�����ݿ�
			contrastChild(organList, troot);

			// ������еĻ�����MQ�ж�,ɾ������Ļ���
			boolean isUse = false;
			List<OrganForm> newOrganList = SysUtil.getOrgan(null, null);
			for (int i = 0; i < newOrganList.size(); i++) {
				OrganForm organForm = newOrganList.get(i);
				for (int j = 0; j < mqList.size(); j++) {
					// OrganNode�е�idΪ������code;
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
	 * �ԱȻ���
	 * 
	 * @param organList;//���ػ���List
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
			// �����мӻ���
			OrganForm organForm = new OrganForm();
			organForm.setName(node.getName());
			List<OrganForm> organFormList;
			if (node.getParentid() == null || node.getParentid().equals("")) {
				// ����Ϊ�ܻ���

			} else {
				organFormList = SysUtil.getOrgan(null, " and t.code='"
						+ node.getParentid() + "'");
				if (organFormList.size() > 0) {
					// �������丸��ת��CodeΪ������Id
					organForm.setPid(organFormList.get(0).getId());
				} else {
					// ����û���丸
					organForm.setPid("code_" + node.getParentid());
				}
			}
			organForm.setCode(node.getId());
			organForm.setType(node.getType());
			String newId = SysUtil.addOrgan(organForm);
			// ���¼���Ļ�����code���Ƿ��Ǳ���δƴ��pid�Ļ�����pid=code_code����
			organFormList = SysUtil.getOrgan(null, " and t.pid like '%/_%' escape '/'");
			String pid;
			for (int i = 0; i < organFormList.size(); i++) {
				organForm = organFormList.get(i);
				pid = organForm.getPid();
				if(pid.indexOf("code_")>=0){
					//�˼�¼��ͬ��ʱû���ҵ����ļ�¼
					pid = pid.substring(5);
				}else{
					//�˼�¼�ǳ�ʼ����_�ļ�¼���������ĸ�����¼���Ǹ�����code
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
	 * ��������
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
		// ��һ��������ע�᱾�ػ���һ�Ρ�
		List<OrganForm> organList = SysUtil.getOrgan(null, " and t.code like '"
				+ Constant.getConstant("sysid") + "%'");
		OrganForm organ;
		try {
			List<OrganForm> orgList = null;
			for (int i = 0; i < organList.size(); i++) {
				// ע�����
				organ = organList.get(i);
				String name = organ.getName();
				String code = organ.getCode();
				String type = organ.getType();
				String pid = organ.getPid();
				// ȡ����������ø�������code
				String key = "";
				if (pid != null && !pid.equals("")) {
					orgList = SysUtil.getOrgan(null, " and t.id = '" + pid
							+ "'");
					if (orgList.size() > 0) {
						key = orgList.get(0).getCode() + "," + name + ","
								+ type;
					} else {
						// pid�������Ҳ�����ӦidΪ��pid�Ļ�������Ϊ������������
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

		// Format fo = Format.getCompactFormat();// ȡ�������ʽFormat
		// fo.setEncoding("GB2312");
		// XMLOutputter outputter = new XMLOutputter(fo);
		// FileWriter write = new FileWriter(path);
		// outputter.output(doc, write);
		// write.close();
	}

}
