package com.qtrmoon.sysManage.transact;

import java.util.List;

import com.ddr.agent.Client;
import com.qtrmoon.toolkit.tree.TreeNode;

public class OrganNode extends TreeNode {
	// ͨѶ������ID
	private String id;

	// ͨѶ������ע����
	private String name;

	// ͨѶ�������ĸ�������ID
	private String parentid;

	// ͨѶ���������ӷ������б�
	private List<String> childids;

	// �ͻ����б�
	private List<Client> clients;

	// �������
	private String type;

	// ����key
	private String key;

	public List<String> getChildids() {
		return childids;
	}

	public void setChildids(List<String> childids) {
		this.childids = childids;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTreeId() {
		return id;
	}

	public String getTreePId() {
		return parentid;
	}

	
}
