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
		// 将id中存储的code转为Id
		OrganForm sqlForm = new OrganForm();
		sqlForm.setCode(organMQForm.getId());
		List<OrganForm> organList = sysService.schOrgan(sqlForm, null);
		if (organList.size() > 0) {
			// 如果在本级系统找到了匹配code的机构,而且做了修改则更新
			if (!organMQForm.getName().equals(organList.get(0).getName())) {
				organList.get(0).setName(organMQForm.getName());
				sysService.addOrgan(organList.get(0));
			}
		} else {
			// 如果没有找到则准备存储这个机构。存储前察看本级系统中有没有pid为mq传来的这个机构。有则重置pcode
			List<OrganForm> wtOrganList = sysService.schOrgan(null,
					" and t.pid like 'code_'");// waitOrganList
			// 将被重置pid的机构。这些机构pid将由'code_'打头的等待状态被重置为新添加的机构的id进入正常参与mq树的状态。
			List<OrganForm> willBeSetOrganList = new ArrayList<OrganForm>();
			String pcode;
			for (int i = 0; i < wtOrganList.size(); i++) {
				pcode = wtOrganList.get(i).getPid().substring(5);// 切掉前缀"code_"
				if (pcode.equals(organMQForm.getCode())) {
					willBeSetOrganList.add(wtOrganList.get(i));
				}
			}
			// 察看新记录的父是否在这级系统中。
			sqlForm = new OrganForm();
			sqlForm.setCode(organMQForm.getPid());
			organList = sysService.schOrgan(sqlForm, null);
			String organId;// 新机构的id。
			if (organList.size() > 0) {
				// 如果在本级找到了code=mq传来机构pcode的记录。则重置pcode
				organMQForm.setPid(organList.get(0).getId());
				organId = sysService.addOrgan(organMQForm);
			} else {
				// 如果在本级找不到code=mq传来机构pcode的记录。说明该记录的父还没有到。则暂存下这个机构，以后再重置pcode。
				organMQForm.setPid("code_" + organMQForm.getPid());
				organId = sysService.addOrgan(organMQForm);
			}
			// 重置本级系统的待重置机构的pid
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
