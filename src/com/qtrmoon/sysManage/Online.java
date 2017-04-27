package com.qtrmoon.sysManage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.qtrmoon.sysManage.bean.UserForm;

public class Online {
	private static Map<String, UserForm> onlineUser = new HashMap<String, UserForm>();// 在线用户

	public static Map<String, UserForm> getOnlineUser() {
		return onlineUser;
	}

	public static void addUser(UserForm user) {
		if (onlineUser.get(user.getId()) == null) {
			onlineUser.put(user.getId(), user);
		}
	}

	public static void delUser(String id) {
		onlineUser.remove(id);
	}
}
