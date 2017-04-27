/**
 * @Bulid in 2008-6-20
 * @PrjectName is TJDYZHWeb
 * @Create by Administrator
 */
package com.qtrmoon.sysManage.transact;

import java.util.ArrayList;
import java.util.List;

import com.ddr.agent.Agent;
import com.ddr.agent.Client;
import com.ddr.agent.Message;
import com.ddr.agent.MsgListener;
import com.qtrmoon.common.Constant;
import com.qtrmoon.infoTransact.InfoBean;
import com.qtrmoon.infoTransact.InfoUtil;
import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.sysManage.serDao.SysManageService;

/**
 * @author Administrator
 * 
 */
public class OrganListener extends MsgListener {

	private static SysManageService sysService;

	public void init() {
	}

	public void newMsgNotify(Client c) {
		InfoBean infoBean = (InfoBean) InfoUtil.infoLinstenerMap.get("organ");
		Agent agent = InfoUtil.getAgent(infoBean.getAppid());
		Message msg = agent.getMessage(this.getName());
		byte[] message = msg.getContent();
		OrganForm organMQForm = OrganConvertor.xml2Form_Organ(message);
		// ��id�д洢��codeתΪId
		OrganForm sqlForm = new OrganForm();
		sqlForm.setCode(organMQForm.getId());
		List<OrganForm> organList = sysService.schOrgan(sqlForm, null);
		if (organList.size() > 0) {
			// ����ڱ���ϵͳ�ҵ���ƥ��code�Ļ���,���������޸������
			if (!organMQForm.getName().equals(organList.get(0).getName())) {
				organList.get(0).setName(organMQForm.getName());
				sysService.addOrgan(organList.get(0));
			}
		} else {
			// ���û���ҵ���׼���洢����������洢ǰ�쿴����ϵͳ����û��pidΪmq�����������������������pcode
			List<OrganForm> wtOrganList = sysService.schOrgan(null,
					" and t.pid like 'code_'");// waitOrganList
			// ��������pid�Ļ�������Щ����pid����'code_'��ͷ�ĵȴ�״̬������Ϊ����ӵĻ�����id������������mq����״̬��
			List<OrganForm> willBeSetOrganList = new ArrayList<OrganForm>();
			String pcode;
			for (int i = 0; i < wtOrganList.size(); i++) {
				pcode = wtOrganList.get(i).getPid().substring(5);// �е�ǰ׺"code_"
				if (pcode.equals(organMQForm.getCode())) {
					willBeSetOrganList.add(wtOrganList.get(i));
				}
			}
			// �쿴�¼�¼�ĸ��Ƿ����⼶ϵͳ�С�
			sqlForm = new OrganForm();
			sqlForm.setCode(organMQForm.getPid());
			organList = sysService.schOrgan(sqlForm, null);
			String organId;// �»�����id��
			if (organList.size() > 0) {
				// ����ڱ����ҵ���code=mq��������pcode�ļ�¼��������pcode
				organMQForm.setPid(organList.get(0).getId());
				organId = sysService.addOrgan(organMQForm);
			} else {
				// ����ڱ����Ҳ���code=mq��������pcode�ļ�¼��˵���ü�¼�ĸ���û�е������ݴ�������������Ժ�������pcode��
				organMQForm.setPid("code_" + organMQForm.getPid());
				organId = sysService.addOrgan(organMQForm);
			}
			// ���ñ���ϵͳ�Ĵ����û�����pid
			OrganForm organForm;
			for (int i = 0; i < willBeSetOrganList.size(); i++) {
				organForm = willBeSetOrganList.get(i);
				organForm.setPid(organId);
				sysService.updOrgan(organForm);
			}
		}
	}

	public SysManageService getSysService() {
		return sysService;
	}

	public void setSysService(SysManageService sysService) {
		OrganListener.sysService = sysService;
	}

	public static SysManageService getSysSer() {
		return sysService;
	}
}
