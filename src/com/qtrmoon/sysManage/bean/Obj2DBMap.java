package com.qtrmoon.sysManage.bean;

/**
 * ͳһ�������������ݿ��Ӱ��
 */
public class Obj2DBMap {
	/**
	 * ͨ��ʵ����Ӱ�����ݿ�����
	 * 
	 * @param ObjName
	 * @return
	 */
	public static String getDBName(String ObjName) {
		if (ObjName.equals("Function")) {
			return "sys_function";
		} else if (ObjName.equals("Organ")) {
			return "sys_organ";
		} else if (ObjName.equals("User")) {
			return "sys_user";
		} else if (ObjName.equals("FunctionUnit")) {
			return "sys_functionunit";
		} else if (ObjName.equals("Role")) {
			return "sys_role";
		} else if (ObjName.equals("UnitFunction")) {
			return "sys_relation_unit_function";
		} else if (ObjName.equals("RoleUser")) {
			return "sys_relation_role_user";
		} else if (ObjName.equals("RoleFunction")) {
			return "sys_relation_role_function";
		} else if (ObjName.equals("OrganUnit")) {
			return "sys_relation_organ_unit";
		}
		return null;
	}
}
