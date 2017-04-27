package com.qtrmoon.infoTransact;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;

import com.ddr.agent.Agent;
import com.ddr.agent.Client;
import com.ddr.agent.MsgListener;
import com.ddr.agent.Server;
import com.qtrmoon.sysManage.serDao.SysManageService;
import com.qtrmoon.sysManage.transact.OrganNode;
import com.qtrmoon.sysManage.transact.OrganTreeUtil;
import com.qtrmoon.toolkit.tree.TreeNode;

public class InfoUtil {
	public static String ip;// 通讯服务IP地址

	public static int port;// 端口号

	public static boolean firstrun;// 是否需要初始化注册本地机构

	public static Map<String, Object> infoLinstenerMap = new HashMap<String, Object>();

	/** 初始化Agent信息服务平台服务应用 */
	public static void regAgent() throws NumberFormatException, Exception {
		String path = InfoUtil.class.getResource("mqcfg.xml").getPath();
		path = path.replaceAll("%20", " ");
		FileInputStream fi = new FileInputStream(path);
		Document doc = (new SAXBuilder()).build(fi);
		Element root = doc.getRootElement();
		ip = root.getChildText("agent-ip");
		port = Integer.valueOf(root.getChildText("agent-port"));
		firstrun = Boolean.parseBoolean(root.getChildText("firstrun"));
		List appList = root.getChild("agent-app").getChildren("appid");
		for (int i = 0; i < appList.size(); i++) {
			root = (Element) appList.get(i);
			String name = root.getAttributeValue("name");
			String listener = root.getAttributeValue("listener");
			String appid = root.getText();
			InfoBean infoBean = new InfoBean();
			infoBean.setAppid(appid);
			infoBean.setListener(listener);
			infoBean.setName(name);
			infoLinstenerMap.put(name, infoBean);
			Agent.initAgent(ip, port, appid);
		}
	}

	public static void main(String args[]) {
		String a[] = {
				"file:E:\\workspace5.5\\SysManage\\WebRoot\\sysManage\\cfg_sysManage\\applicationContext.xml",
				"file:E:\\workspace5.5\\SysManage\\WebRoot\\WEB-INF\\applicationContext.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(a);
		SysManageService ser = (SysManageService) context.getBean("sysService");
	}

	/**
	 * 获取平台代理，使平台服务器能够重新连接而不重启服务。
	 */
	public static Agent getAgent(String appid) {
		Agent agent = null;
		try {
			agent = Agent.getAgent(appid);
		} catch (Exception e) {
			try {
				System.out.println("平台初始化！");
				InfoUtil.regAgent();
				agent = Agent.getAgent(appid);
			} catch (Exception e2) {

			}
		}
		if (agent == null) {
			try {
				System.out.println("平台初始化！");
				InfoUtil.regAgent();
				agent = Agent.getAgent(appid);
			} catch (Exception e2) {

			}
		}
		return agent;
	}

	/**
	 * @param orgName
	 *            组织机构名称
	 * @param orgCode
	 *            组织机构编码
	 * @throws Exception
	 * 
	 * 为组织机构注册应用服务
	 */
	public static void registApp(String orgName, String orgCode)
			throws Exception {

		Iterator iterator = infoLinstenerMap.values().iterator();
		while (iterator.hasNext()) {
			InfoBean infoBean = (InfoBean) iterator.next();
			Agent agent = Agent.getAgent(infoBean.getAppid());
			MsgListener listenerOrgan = (MsgListener) Class.forName(
					infoBean.getListener()).newInstance();
			listenerOrgan.setName(orgName);
			listenerOrgan.setKey(orgCode);
			agent.registerTerminal(listenerOrgan);
		}
	}

	/**
	 * 
	 * @param orgCode
	 *            组织机构编码
	 * @throws Exception
	 * 
	 * 删除组织机构已经注册的应用
	 */
	public static void removeApp(String orgCode) throws Exception {

		Iterator iterator = infoLinstenerMap.values().iterator();
		while (iterator.hasNext()) {
			InfoBean infoBean = (InfoBean) iterator.next();
			Agent agent = Agent.getAgent(infoBean.getAppid());
			agent.removeTerminal(orgCode);
		}
	}

	/**
	 * 获取信息服务平台组织机构
	 * 
	 * @return
	 */
	public static List<OrganNode> getOrganList(String appid) {
		List<OrganNode> nodeList = new ArrayList<OrganNode>();
		Agent agent = null;
		agent = InfoUtil.getAgent(appid);
		if (agent != null) {
			List list = agent.getWebInfo();
			Server server;
			for (int i = 0; i < list.size(); i++) {
				server = (Server) list.get(i);
				// nodeList.add(typeChangeServerToOrganNode(server));
				List clients = server.getClients();
				for (int j = 0; j < clients.size(); j++) {
					Client client = (Client) clients.get(j);
					client.getKey();
					nodeList.add(typeChangeClientToOrganNode(client));
				}
			}
		}
		return nodeList;
	}

	/**
	 * @param server
	 * @return
	 * 
	 * server与organNode的类转换
	 */
	private static OrganNode typeChangeServerToOrganNode(Server server) {
		OrganNode organ = new OrganNode();

		organ.setChildids(server.getChildids());
		organ.setClients(server.getClients());
		organ.setId(server.getId());
		organ.setName(server.getName());
		organ.setParentid(server.getParentid());
		organ.setKey(server.getKey());
		organ.setType("AREA");
		return organ;
	}

	/**
	 * @param client
	 * @return
	 * 
	 * client与organNode的类转换
	 */
	private static OrganNode typeChangeClientToOrganNode(Client client) {
		OrganNode organ = new OrganNode();
		String[] key = client.getKey().split(",");
		// organ.setChildids(server.getChildids());
		// organ.setClients(server.getClients());
		organ.setId(client.getName());
		organ.setName(key[1]);
		organ.setParentid(key[0]);
		organ.setType(key[2]);
		organ.setKey(client.getId());
		return organ;
	}

	/**
	 * 获取机构树HTML代码
	 * 
	 * @return
	 */
	public static String getOrganTreeCode(String appid) {
		List list = getOrganList(appid);
		TreeNode root = null;
		for (int i = 0; i < list.size(); i++) {
			root = (TreeNode) list.get(i);
			if (root.getTreePId() == null) {
				// 找到根节点
				break;
			}
		}
		String treeCode = (new OrganTreeUtil()).getTreeCode(list, root);
		return treeCode;
	}

	/**
	 * 根据信息平台Id找回UnitCode
	 * 
	 * @param serList;
	 *            //server列表
	 * @param targetUnitId;
	 *            //信息平台Id
	 * @return
	 */
	public static String getInfoIdByUnitCode(List<Server> serList,
			String targetUnitId) {
		Server ser;
		Client client;
		List<Client> clientList;
		for (int i = 0; i < serList.size(); i++) {
			ser = serList.get(i);
			clientList = ser.getClients();
			for (int m = 0; m < clientList.size(); m++) {
				client = clientList.get(m);
				if (client.getName().equals(targetUnitId)) {
					return client.getId();
				}
			}
		}
		return null;
	}
}
