package com.qtrmoon.sysManage.serDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import com.qtrmoon.common.BaseDAO;
import com.qtrmoon.sysManage.bean.Function;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.FunctionUnit;
import com.qtrmoon.sysManage.bean.FunctionUnitForm;
import com.qtrmoon.sysManage.bean.Machine;
import com.qtrmoon.sysManage.bean.MachineForm;
import com.qtrmoon.sysManage.bean.Module;
import com.qtrmoon.sysManage.bean.ModuleForm;
import com.qtrmoon.sysManage.bean.Organ;
import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.sysManage.bean.OrganUnit;
import com.qtrmoon.sysManage.bean.Role;
import com.qtrmoon.sysManage.bean.RoleForm;
import com.qtrmoon.sysManage.bean.RoleFunction;
import com.qtrmoon.sysManage.bean.RoleUser;
import com.qtrmoon.sysManage.bean.UnitFunction;
import com.qtrmoon.sysManage.bean.User;
import com.qtrmoon.sysManage.bean.UserForm;
import com.qtrmoon.toolkit.db.Condition;
import com.qtrmoon.toolkit.db.Heb3QueryUtil;

public class SysManageDao extends BaseDAO {

	/**
	 * 查询用户
	 * 
	 * @param userForm
	 * @return
	 */
	public List<User> schUser(UserForm userForm) {
		String sql = "from User t where 1=1 ";
		String condition = "";
		List<Condition> conditions = new ArrayList<Condition>();
		if (userForm != null) {
			condition = userForm.getCondition();
			if (userForm.getId() != null && !userForm.getId().equals("")) {
				sql += " and t.id =:id ";
				conditions.add(new Condition("id", String.class, userForm
						.getId()));
			}
			if (userForm.getLoginName() != null
					&& !userForm.getLoginName().equals("")) {
				sql += " and t.loginName =:loginName";
				conditions.add(new Condition("loginName", String.class,
						userForm.getLoginName()));
			}
			if (userForm.getPassword() != null
					&& !userForm.getPassword().equals("")) {
				sql += " and t.password =:password";
				conditions.add(new Condition("password", String.class, userForm
						.getPassword()));
			}
			if (userForm.getUserName() != null
					&& !userForm.getUserName().equals("")) {
				sql += " and t.userName =:userName";
				conditions.add(new Condition("userName", String.class, userForm
						.getUserName()));
			}
			if (userForm.getOrganId() != null
					&& !userForm.getOrganId().equals("")) {
				sql += " and t.organId=:organId";
				conditions.add(new Condition("organId", String.class, userForm
						.getOrganId()));
			}
		}
		if (condition != null && !condition.equals("")) {
			sql += condition;
		}
		Session session = getSession();
		Query q = session.createQuery(sql);
		Heb3QueryUtil.createQuery(conditions, q);
		return q.list();
	}

	/**
	 * 查询机构
	 * 
	 * @param organForm
	 * @param condition
	 * @return
	 */
	public List<Organ> schOrgan(OrganForm organForm, String condition) {
		String sql = "from Organ t where 1=1 ";
		if (organForm != null) {
			if (organForm.getId() != null && !organForm.getId().equals("")) {
				sql += " and t.id = " + organForm.getId();
			}
			if (organForm.getName() != null && !organForm.getName().equals("")) {
				sql += " and t.name = '" + organForm.getName() + "'";
			}
			if (organForm.getIsMaster() != null
					&& !organForm.getIsMaster().equals("")) {
				sql += " and t.isMaster = '" + organForm.getIsMaster() + "'";
			}
			if (organForm.getPid() != null && !organForm.getPid().equals("")) {
				sql += " and t.pId = '" + organForm.getPid() + "'";
			}
			if (organForm.getCode() != null && !organForm.getCode().equals("")) {
				sql += " and t.code like '" + organForm.getCode() + "'";
			}
		}
		if (condition != null && !condition.equals("")) {
			sql += condition;
		}
		Session session = getSession();
		Query q = session.createQuery(sql);
		return q.list();
	}

	/**
	 * 根据功能单元查询机构
	 * 
	 * @param unitId
	 * @return
	 */
	public List<Organ> schOrganByUnitId(String unitId) {
		String sql = "select t.* from sys_organ t,sys_relation_organ_unit tt where t.id=tt.organId and tt.unitId='"
				+ unitId + "'";
		PreparedStatement stm = null;
		ResultSet rSet = null;
		List<Organ> organList = new ArrayList<Organ>();
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			Organ organ = null;
			while (rSet.next()) {
				organ = new Organ();
				organ.setId(rSet.getString("id"));
				organ.setInfo(rSet.getString("info"));
				organ.setIsMaster(rSet.getString("isMaster"));
				organ.setName(rSet.getString("name"));
				organ.setOrd(rSet.getLong("ord"));
				organ.setPid(rSet.getString("pid"));
				organ.setTreeTrack(rSet.getString("treeTrack"));
				organList.add(organ);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return organList;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	// 功能基本方法
	/**
	 * 查询功能
	 * 
	 * @param functionForm
	 * @param condition
	 * @return
	 */
	public List<Function> schFunction(FunctionForm functionForm,
			String condition) {
		String sql = "from Function t where 1=1 ";
		if (functionForm != null) {
			if (functionForm.getId() != null
					&& !functionForm.getId().equals("")) {
				sql += " and t.id = " + functionForm.getId();
			}
			if (functionForm.getName() != null
					&& !functionForm.getName().equals("")) {
				sql += " and t.name = '" + functionForm.getName() + "'";
			}
			if (functionForm.getLink() != null
					&& !functionForm.getLink().equals("")) {
				sql += " and t.link like '%" + functionForm.getLink() + "%'";
			}
			if (functionForm.getIsShow() != null
					&& !functionForm.getIsShow().equals("")) {
				sql += " and t.isShow = '" + functionForm.getIsShow() + "'";
			}
			if (functionForm.getPid() != null
					&& !functionForm.getPid().equals("")) {
				sql += " and t.pid = '" + functionForm.getPid() + "'";
			}
		}
		if (condition != null && !condition.equals("")) {
			sql += condition;
		}
		Session session = getSession();
		Query q = session.createQuery(sql);
		return q.list();
	}

	public List<Module> schModule(ModuleForm moduleForm) {
		String condition = "";
		int pageSize=0,firstRes=0;
		List<Condition> conditions = new ArrayList<Condition>();
		if (moduleForm != null) {
			if (moduleForm.getModule() != null && !moduleForm.getModule().equals("")) {
				condition += " and t.module = :module";
				conditions.add(new Condition("module", String.class, moduleForm.getModule()));
			}
			if (moduleForm.getFuncs() != null && !moduleForm.getFuncs().equals("")) {
				condition += " and t.funcs = :funcs";
				conditions.add(new Condition("funcs", String.class, moduleForm.getFuncs()));
			}
			if (moduleForm.getPic() != null && !moduleForm.getPic().equals("")) {
				condition += " and t.pic = :pic";
				conditions.add(new Condition("pic", String.class, moduleForm.getPic()));
			}
			if (moduleForm.getName() != null && !moduleForm.getName().equals("")) {
				condition += " and t.name = :name";
				conditions.add(new Condition("name", String.class, moduleForm.getName()));
			}
			if (moduleForm.getLink() != null && !moduleForm.getLink().equals("")) {
				condition += " and t.link = :link";
				conditions.add(new Condition("link", String.class, moduleForm.getLink()));
			}
			if (moduleForm.getInfo() != null && !moduleForm.getInfo().equals("")) {
				condition += " and t.info = :info";
				conditions.add(new Condition("info", String.class, moduleForm.getInfo()));
			}
			if (moduleForm.getOrd() != null && !moduleForm.getOrd().equals("")) {
				condition += " and t.ord = :ord";
				conditions.add(new Condition("ord", String.class, moduleForm.getOrd()));
			}
			if (moduleForm.getId() != null && !moduleForm.getId().equals("")) {
				condition += " and t.id = :id";
				conditions.add(new Condition("id", String.class, moduleForm.getId()));
			}
			String formCond = moduleForm.getCondition();
			if (formCond != null && !formCond.equals("")) {
				condition += formCond;
			}
		}
		Session session = getSession();
		Query query;
		if(moduleForm!=null){
			String order="";//排序字段
			if(moduleForm.getOrderCol()!=null&&!moduleForm.getOrderCol().equals("")){
				order=" order by "+moduleForm.getOrderCol()+" "+moduleForm.getOrderType();
			}
			query = session.createQuery("select count(*) from Module t where 1=1 "+condition + order);
			Heb3QueryUtil.createQuery(conditions, query);
			int datasize = (Integer)query.list().get(0);
			moduleForm.setDatasize(datasize);
			query = session.createQuery("from Module t where 1=1 "+condition + order);
			Heb3QueryUtil.createQuery(conditions, query);
			pageSize = moduleForm.getPagesize();
			firstRes = (moduleForm.getCurrentPage()-1) * pageSize;
			query.setMaxResults(pageSize);
			query.setFirstResult(firstRes);
		}else{
			query = session.createQuery("from Module t");
		}
		return query.list();
	}
	
	/**
	 * 根据角色查询用户
	 * 
	 * @param roleId
	 * @return
	 */
	public List<User> schUserByRoleId(String roleId) {
		String sql = "select t.* from sys_user t,sys_relation_role_user tt where t.id=tt.userid and tt.roleid='"
				+ roleId + "'";
		PreparedStatement stm = null;
		ResultSet rSet = null;
		List<User> userList = new ArrayList<User>();
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			User user = null;
			while (rSet.next()) {
				user = new User();
				user.setClassify(rSet.getString("classify"));
				user.setId(rSet.getString("id"));
				user.setLoginName(rSet.getString("loginName"));
				user.setOrganId(rSet.getString("organId"));
				user.setPassword(rSet.getString("password"));
				user.setRegTime(rSet.getDate("regTime"));
				user.setState(rSet.getString("state"));
				user.setUserName(rSet.getString("userName"));
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	/**
	 * @param roleId
	 * @return List<Function>
	 * 
	 * 根据角色查找功能列表
	 */
	public List<Function> schFunctionsByRoleId(String roleId) {
		String sql = "select t.* from sys_function t,sys_relation_role_function tt where t.id=tt.functionid and tt.roleid='"
				+ roleId + "'";
		PreparedStatement stm = null;
		ResultSet rSet = null;
		List<Function> functionList = new ArrayList<Function>();
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			Function function = null;
			while (rSet.next()) {
				function = new Function();
				function.setId(rSet.getString("id"));
				function.setInfo(rSet.getString("info"));
				function.setIsShow(rSet.getString("isShow"));
				function.setModuleName(rSet.getString("moduleName"));
				function.setName(rSet.getString("name"));
				function.setOrd(rSet.getLong("ord"));
				function.setPicIco(rSet.getString("picico"));
				function.setPicImg(rSet.getString("picImg"));
				function.setPid(rSet.getString("pid"));
				function.setTreeTrack(rSet.getString("treeTrack"));
				function.setLink(rSet.getString("link"));
				functionList.add(function);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return functionList;
	}

	/**
	 * @param userId
	 * @return List<Function>
	 * 
	 * 根据用户获取功能
	 */
	public List<Function> schFunctionsByUserId(String userId) {
		String sql = "select distinct t.* from sys_function t,sys_relation_role_function tt,sys_relation_role_user ttt where t.id=tt.functionid and tt.roleid=ttt.roleid and ttt.userid='"
				+ userId + "'";
		List<Function> functionList = new ArrayList<Function>();
		PreparedStatement stm = null;
		ResultSet rSet = null;
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			Function function = null;
			while (rSet.next()) {
				function = new Function();
				function.setId(rSet.getString("id"));
				function.setInfo(rSet.getString("info"));
				function.setIsShow(rSet.getString("isShow"));
				function.setLink(rSet.getString("link"));
				function.setModuleName(rSet.getString("moduleName"));
				function.setName(rSet.getString("name"));
				function.setOrd(rSet.getLong("ord"));
				function.setPicIco(rSet.getString("picico"));
				function.setPicImg(rSet.getString("picImg"));
				function.setPid(rSet.getString("pid"));
				function.setTreeTrack(rSet.getString("treeTrack"));
				functionList.add(function);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return functionList;
	}

	/**
	 * @param unitId
	 * @return List<Function>
	 * 
	 * 根据功能单元查询功能列表
	 */
	public List<Function> schFunctionsByFunctionUnitId(String unitId) {
		String sql = "select t.* from sys_function t,sys_relation_unit_function tt where t.id=tt.functionid and tt.unitid='"
				+ unitId + "'";
		List<Function> functionList = new ArrayList<Function>();
		PreparedStatement stm = null;
		ResultSet rSet = null;
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			Function function = null;
			while (rSet.next()) {
				function = new Function();
				function.setId(rSet.getString("id"));
				function.setInfo(rSet.getString("info"));
				function.setIsShow(rSet.getString("isShow"));
				function.setModuleName(rSet.getString("moduleName"));
				function.setName(rSet.getString("name"));
				function.setOrd(rSet.getLong("ord"));
				function.setPicIco(rSet.getString("picico"));
				function.setPicImg(rSet.getString("picImg"));
				function.setPid(rSet.getString("pid"));
				function.setTreeTrack(rSet.getString("treeTrack"));
				functionList.add(function);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return functionList;
	}

	/**
	 * @param organId
	 * @return List<Function>
	 * 
	 * 根据机构查询功能
	 */
	public List<Function> schFunctionsByOrganId(String organId) {
		String sql = "select distinct t.* from sys_function t,sys_relation_unit_function tt,sys_relation_organ_unit ttt where t.isshow='1' and t.id=tt.functionid and tt.unitid=ttt.unitid and ttt.organid='"
				+ organId + "'";
		List<Function> functionList = new ArrayList<Function>();
		PreparedStatement stm = null;
		ResultSet rSet = null;
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			Function function = null;
			while (rSet.next()) {
				function = new Function();
				function.setId(rSet.getString("id"));
				function.setInfo(rSet.getString("info"));
				function.setIsShow(rSet.getString("isShow"));
				function.setModuleName(rSet.getString("moduleName"));
				function.setName(rSet.getString("name"));
				function.setOrd(rSet.getLong("ord"));
				function.setPicIco(rSet.getString("picico"));
				function.setPicImg(rSet.getString("picImg"));
				function.setPid(rSet.getString("pid"));
				function.setTreeTrack(rSet.getString("treeTrack"));
				functionList.add(function);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return functionList;
	}

	/**
	 * @param organId
	 * @return List<Function>
	 * 
	 * 根据机构查询功能
	 */
	public List<Function> schFuncByOrganId(String organId) {
		String sql = "select distinct t.* from sys_function t,sys_relation_unit_function tt,sys_relation_organ_unit ttt where t.isshow='1' and t.id=tt.functionid and tt.unitid=ttt.unitid and ttt.organid='"
				+ organId + "'";
		List<Function> functionList = new ArrayList<Function>();
		PreparedStatement stm = null;
		ResultSet rSet = null;
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			Function function = null;
			while (rSet.next()) {
				function = new Function();
				function.setId(rSet.getString("id"));
				function.setInfo(rSet.getString("info"));
				function.setIsShow(rSet.getString("isShow"));
				function.setModuleName(rSet.getString("moduleName"));
				function.setName(rSet.getString("name"));
				function.setOrd(rSet.getLong("ord"));
				function.setPicIco(rSet.getString("picico"));
				function.setPicImg(rSet.getString("picImg"));
				function.setPid(rSet.getString("pid"));
				function.setTreeTrack(rSet.getString("treeTrack"));
				functionList.add(function);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return functionList;
	}

	/**
	 * 根据组织机构查询功能单元
	 * 
	 * @param organId
	 * @return
	 */
	public List<FunctionUnit> schFunctionUnitByOrganId(String organId) {
		String sql = "select t.* from sys_functionunit t,sys_relation_organ_unit tt where t.id=tt.unitid and tt.organid='"
				+ organId + "'";
		List<FunctionUnit> fUnitList = new ArrayList<FunctionUnit>();
		PreparedStatement stm = null;
		ResultSet rSet = null;
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			FunctionUnit fUnit = null;
			while (rSet.next()) {
				fUnit = new FunctionUnit();
				fUnit.setId(rSet.getString("id"));
				fUnit.setInfo(rSet.getString("info"));
				fUnit.setName(rSet.getString("name"));
				fUnit.setOrd(rSet.getLong("ord"));
				fUnit.setPicIco(rSet.getString("picIco"));
				fUnitList.add(fUnit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fUnitList;
	}

	/**
	 * 根据功能ID查询功能单元
	 * 
	 * @param unitId
	 * @return
	 */
	public List<FunctionUnit> schFunctionUnitByFunctionId(String functionId) {
		String sql = "select t.* from sys_functionunit t,sys_relation_unit_function tt where t.id=tt.unitid and tt.functionId='"
				+ functionId + "'";
		List<FunctionUnit> fUnitList = new ArrayList<FunctionUnit>();
		PreparedStatement stm = null;
		ResultSet rSet = null;
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			FunctionUnit fUnit = null;
			while (rSet.next()) {
				fUnit = new FunctionUnit();
				fUnit.setId(rSet.getString("id"));
				fUnit.setInfo(rSet.getString("info"));
				fUnit.setName(rSet.getString("name"));
				fUnit.setOrd(rSet.getLong("ord"));
				fUnit.setPicIco(rSet.getString("picIco"));
				fUnitList.add(fUnit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fUnitList;
	}

	/**
	 * 根据功能查询角色
	 * 
	 * @param functionId
	 * @return
	 */
	public List<Role> schRoleByFunction(String functionId) {
		String sql = "select t.* from sys_role t ,sys_relation_role_function tt where t.id=tt.roleId and tt.functionId='"
				+ functionId + "'";
		List<Role> roleList = new ArrayList<Role>();
		PreparedStatement stm = null;
		ResultSet rSet = null;
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			Role role = null;
			while (rSet.next()) {
				role = new Role();
				role.setClassify(rSet.getString("classify"));
				role.setId(rSet.getString("id"));
				role.setInfo(rSet.getString("info"));
				role.setIsLocal(rSet.getString("isLocal"));
				role.setName(rSet.getString("name"));
				role.setOrd(rSet.getLong("ord"));
				role.setOrganId(rSet.getString("organId"));

				roleList.add(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roleList;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	/**
	 * 查询功能单元
	 * 
	 * @param functionUnitForm
	 * @param condition
	 * @return
	 */
	public List<FunctionUnit> schFunctionUnit(
			FunctionUnitForm functionUnitForm, String condition) {
		String sql = "from FunctionUnit t where 1=1 ";
		if (functionUnitForm != null) {
			if (functionUnitForm.getId() != null
					&& !functionUnitForm.getId().equals("")) {
				sql += " and t.id = " + functionUnitForm.getId();
			}
			if (functionUnitForm.getName() != null
					&& !functionUnitForm.getName().equals("")) {
				sql += " and t.name = '" + functionUnitForm.getName() + "'";
			}

		}
		if (condition != null && !condition.equals("")) {
			sql += condition;
		}
		Session session = getSession();
		Query q = session.createQuery(sql);
		return q.list();
	}

	/**
	 * 查询角色
	 * 
	 * @param roleForm
	 * @return
	 */
	public List<Role> schRole(RoleForm roleForm) {
		String sql = "from Role t where 1=1 ";
		String condition = roleForm.getCondition();
		if (roleForm != null) {
			if (roleForm.getId() != null && !roleForm.getId().equals("")) {
				sql += " and t.id = " + roleForm.getId();
			}
			if (roleForm.getName() != null && !roleForm.getName().equals("")) {
				sql += " and t.name = '" + roleForm.getName() + "'";
			}
			if (roleForm.getIsLocal() != null
					&& !roleForm.getIsLocal().equals("")) {
				sql += " and t.isLocal = '" + roleForm.getIsLocal() + "'";
			}
			if (roleForm.getOrganId() != null
					&& !roleForm.getOrganId().equals("")) {
				sql += " and t.organId='" + roleForm.getOrganId() + "'";
			}
			if (roleForm.getClassify() != null
					&& !roleForm.getClassify().equals("")) {
				sql += " and t.classify='" + roleForm.getClassify() + "'";
			}
		}
		if (condition != null && !condition.equals("")) {
			sql += condition;
		}
		Session session = getSession();
		Query q = session.createQuery(sql);
		return q.list();
	}

	/**
	 * 根据用户查询角色
	 * 
	 * @param userId
	 * @return
	 */
	public List<Role> schRoleByUserId(String userId) {
		String sql = "select t.* from sys_role t ,sys_relation_role_user tt where t.id=tt.roleId and tt.userId='"
				+ userId + "'";
		List<Role> roleList = new ArrayList<Role>();
		PreparedStatement stm = null;
		ResultSet rSet = null;
		try {
			stm = getSession().connection().prepareStatement(sql);
			rSet = stm.executeQuery();
			Role role = null;
			while (rSet.next()) {
				role = new Role();
				role.setClassify(rSet.getString("classify"));
				role.setId(rSet.getString("id"));
				role.setInfo(rSet.getString("info"));
				role.setIsLocal(rSet.getString("isLocal"));
				role.setName(rSet.getString("name"));
				role.setOrd(rSet.getLong("ord"));
				role.setOrganId(rSet.getString("organId"));

				roleList.add(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roleList;
	}

	/**
	 * 根据角色查询功能
	 * 
	 * @param roleId
	 * @param condition
	 * @return
	 */
	public List<RoleFunction> schFunctionByRole(String roleId, String condition) {
		String sql = "from RoleFunction t where 1=1 ";

		if (roleId != null && !roleId.equals("")) {
			sql += " t.roleId=" + roleId;
		}
		if (condition != null && !condition.equals("")) {
			sql += condition;
		}
		Session session = getSession();
		Query q = session.createQuery(sql);
		return q.list();
	}

	/**
	 * 删除功能单元-功能中间表
	 * 
	 * @param unitFunction
	 */
	public void removeUnitFunction(UnitFunction unitFunction) {

		if (unitFunction != null) {
			String sql = "";
			if (unitFunction.getId() != null
					&& !unitFunction.getId().equals("")) {
				sql += " and t.id='" + unitFunction.getId() + "'";
			}
			if (unitFunction.getFunctionId() != null
					&& !unitFunction.getFunctionId().equals("")) {
				sql += " and t.functionId='" + unitFunction.getFunctionId()
						+ "'";
			}
			if (unitFunction.getUnitId() != null
					&& !unitFunction.getUnitId().equals("")) {
				sql += " and t.unitId='" + unitFunction.getUnitId() + "'";
			}
			if (!sql.equals("")) {
				sql = "delete SYS_RELATION_UNIT_FUNCTION t where 1=1" + sql;
				PreparedStatement stm = null;
				try {
					stm = getSession().connection().prepareStatement(sql);
					stm.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除角色-用户中间表
	 * 
	 * @param roleUser
	 */
	public void removeRoleUser(RoleUser roleUser) {
		if (roleUser != null) {
			String sql = "";
			if (roleUser.getId() != null && !roleUser.getId().equals("")) {
				sql += " and t.id='" + roleUser.getId() + "'";
			}
			if (roleUser.getRoleId() != null
					&& !roleUser.getRoleId().equals("")) {
				sql += " and t.roleId='" + roleUser.getRoleId() + "'";
			}
			if (roleUser.getUserId() != null
					&& !roleUser.getUserId().equals("")) {
				sql += " and t.userId='" + roleUser.getUserId() + "'";
			}
			if (!sql.equals("")) {
				sql = "delete SYS_RELATION_ROLE_USER t where 1=1" + sql;
				PreparedStatement stm = null;
				try {
					stm = getSession().connection().prepareStatement(sql);
					stm.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除角色-功能中间表
	 * 
	 * @param roleFunction
	 */
	public void removeRoleFunction(RoleFunction roleFunction) {
		if (roleFunction != null) {
			String sql = "";
			if (roleFunction.getId() != null
					&& !roleFunction.getId().equals("")) {
				sql += " and t.id='" + roleFunction.getId() + "'";
			}
			if (roleFunction.getRoleId() != null
					&& !roleFunction.getRoleId().equals("")) {
				sql += " and t.roleId='" + roleFunction.getRoleId() + "'";
			}
			if (roleFunction.getFunctionId() != null
					&& !roleFunction.getFunctionId().equals("")) {
				sql += " and t.functionid='" + roleFunction.getFunctionId()
						+ "'";
			}
			if (!sql.equals("")) {
				sql = "delete SYS_RELATION_ROLE_FUNCTION t where 1=1" + sql;
				PreparedStatement stm = null;
				try {
					stm = getSession().connection().prepareStatement(sql);
					stm.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除机构-功能单元中间表
	 * 
	 * @param organUnit
	 */
	public void removeOrganUnit(OrganUnit organUnit) {
		if (organUnit != null) {
			String sql = " ";
			if (organUnit.getId() != null && !organUnit.getId().equals("")) {
				sql = " and t.id='" + organUnit.getId() + "'";
			}
			if (organUnit.getOrganId() != null
					&& !organUnit.getOrganId().equals("")) {
				sql += " and t.organId='" + organUnit.getOrganId() + "'";
			}
			if (organUnit.getUnitId() != null
					&& !organUnit.getUnitId().equals("")) {
				sql += " and t.unitId='" + organUnit.getUnitId() + "'";
			}
			if (!sql.equals("")) {
				sql = "delete SYS_RELATION_ORGAN_UNIT t where 1=1" + sql;
				PreparedStatement stm = null;
				try {
					stm = getSession().connection().prepareStatement(sql);
					stm.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public List<Machine> schMachine(MachineForm machineForm) {
		String condition = "";
		int pageSize=0,firstRes=0;
		List<Condition> conditions = new ArrayList<Condition>();
		if (machineForm != null) {
			if (machineForm.getAddtimeBeg() != null && !machineForm.getAddtimeBeg().equals("")) {
				condition += " and t.addtime >= to_Date(:addtimeBeg,'yyyy-mm-dd')";
				conditions.add(new Condition("addtimeBeg", String.class, machineForm.getAddtimeBeg()));
			}
			if (machineForm.getAddtimeEnd() != null && !machineForm.getAddtimeEnd().equals("")) {
				condition += " and t.addtime <= to_Date(:addtimeEnd,'yyyy-mm-dd')";
				conditions.add(new Condition("addtimeEnd", String.class, machineForm.getAddtimeEnd()));
			}
			if (machineForm.getId() != null && !machineForm.getId().equals("")) {
				condition += " and t.id = :id";
				conditions.add(new Condition("id", String.class, machineForm.getId()));
			}
			if (machineForm.getIp() != null && !machineForm.getIp().equals("")) {
				condition += " and t.ip = :ip";
				conditions.add(new Condition("ip", String.class, machineForm.getIp()));
			}
			if (machineForm.getMac() != null && !machineForm.getMac().equals("")) {
				condition += " and t.mac = :mac";
				conditions.add(new Condition("mac", String.class, machineForm.getMac()));
			}
			String formCond = machineForm.getCondition();
			if (formCond != null && !formCond.equals("")) {
				condition += formCond;
			}
		}
		Session session = getSession();
		Query query;
		if(machineForm!=null){
			String order="";//排序字段
			if(machineForm.getOrderCol()!=null&&!machineForm.getOrderCol().equals("")){
				order=" order by "+machineForm.getOrderCol()+" "+machineForm.getOrderType();
			}
			query = session.createQuery("select count(*) from Machine t where 1=1 "+condition + order);
			Heb3QueryUtil.createQuery(conditions, query);
			int datasize = (Integer)query.list().get(0);
			machineForm.setDatasize(datasize);
			query = session.createQuery("from Machine t where 1=1 "+condition + order);
			Heb3QueryUtil.createQuery(conditions, query);
			pageSize = machineForm.getPagesize();
			firstRes = (machineForm.getCurrentPage()-1) * pageSize;
			query.setMaxResults(pageSize);
			query.setFirstResult(firstRes);
		}else{
			query = session.createQuery("from Machine t");
		}
		return query.list();
	}
	
	/**
	 * @param table
	 * @param pk
	 * @return 最大值
	 * 
	 * 获取table表中pk字段的最大值
	 */
	public long getMaxLongId(String table, String pk) {
		String sql = "select max(t." + pk + ") id from " + table + " t";
		Long l = (Long) getSession().createSQLQuery(sql).addScalar("id",
				Hibernate.LONG).uniqueResult();
		if (l == null) {
			l = 1L;
		}
		return l;
	}

	/**
	 * @param table
	 * @param pk
	 * @return 最大值
	 * 
	 * 获取table表中pk字段的最大值
	 */
	public String getMaxStringId(String table, String pk) {
		String sql = "select max(t." + pk + ") id from " + table
				+ " t where length(t." + pk + ")=(select max (length(t." + pk
				+ ")) id from " + table + " t)";
		String l = (String) getSession().createSQLQuery(sql).addScalar("id",
				Hibernate.STRING).uniqueResult();
		if (l == null) {
			l = "1";
		}
		return l;
	}

}
