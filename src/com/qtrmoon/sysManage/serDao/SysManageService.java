package com.qtrmoon.sysManage.serDao;

import java.util.ArrayList;
import java.util.List;

import com.qtrmoon.common.BaseService;
import com.qtrmoon.common.Constant;
import com.qtrmoon.sysManage.bean.Function;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.FunctionUnit;
import com.qtrmoon.sysManage.bean.FunctionUnitForm;
import com.qtrmoon.sysManage.bean.Module;
import com.qtrmoon.sysManage.bean.ModuleForm;
import com.qtrmoon.sysManage.bean.Obj2DBMap;
import com.qtrmoon.sysManage.bean.Organ;
import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.sysManage.bean.OrganUnit;
import com.qtrmoon.sysManage.bean.Role;
import com.qtrmoon.sysManage.bean.RoleForm;
import com.qtrmoon.sysManage.bean.RoleFunction;
import com.qtrmoon.sysManage.bean.RoleUser;
import com.qtrmoon.sysManage.bean.TypeChange;
import com.qtrmoon.sysManage.bean.UnitFunction;
import com.qtrmoon.sysManage.bean.User;
import com.qtrmoon.sysManage.bean.UserForm;
import com.qtrmoon.sysManage.bean.Machine;
import com.qtrmoon.sysManage.bean.MachineForm;
import com.qtrmoon.toolkit.keyBuilder.StrKey36;

public class SysManageService extends BaseService {
	private SysManageDao sysDao;

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	/* 用户基本方法 */
	public List<UserForm> schUser(UserForm userForm) {
		List<User> userList = sysDao.schUser(userForm);
		List<UserForm> userFormList = new ArrayList<UserForm>();
		User user;
		for (int i = 0; i < userList.size(); i++) {
			user = userList.get(i);
			userFormList.add(TypeChange.userObj2Form(user));
		}
		return userFormList;
	}

	public UserForm schUserById(String userId) {
		User user = (User) sysDao.getObject(User.class, userId);
		UserForm userForm = TypeChange.userObj2Form(user);
		return userForm;
	}

	public String addUser(UserForm userForm) {
		User user = TypeChange.userForm2Obj(userForm);
		String id = StrKey36.getNextSeq(sysDao.getMaxStringId(Obj2DBMap
				.getDBName("User"), "id"));
		user.setId(id);
		saveObject(user);
		return id;
	}

	public void updUser(UserForm userForm) {
		User user = TypeChange.userForm2Obj(userForm);
		saveObject(user);
	}

	public void delUser(String userId) {
		removeObject(User.class, userId);
	}

	/**
	 * 根据角色查询用户
	 * 
	 * @param roleId
	 * @return
	 */
	public List<UserForm> schUserByRoleId(String roleId) {
		List<User> userList = sysDao.schUserByRoleId(roleId);
		List<UserForm> userFormList = new ArrayList<UserForm>();
		for (int i = 0; i < userList.size(); i++) {
			userFormList.add(TypeChange.userObj2Form(userList.get(i)));
		}
		return userFormList;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	// 机构基本方法
	/**
	 * 查询组织机构
	 * 
	 * @param organForm
	 * @param condition
	 * @return
	 */
	public List<OrganForm> schOrgan(OrganForm organForm, String condition) {
		List<Organ> organList = sysDao.schOrgan(organForm, condition);
		List<OrganForm> organFormList = new ArrayList<OrganForm>();
		for (int i = 0; i < organList.size(); i++) {
			organFormList.add(TypeChange.organObj2Form(organList.get(i)));
		}
		return organFormList;
	}

	/**
	 * 增加组织机构 机构的添加和机构同步都调用此方法，organ.code在机构同步时已经拼接了id无需再拼。
	 * 
	 * @param organForm
	 */
	public String addOrgan(OrganForm organForm) {
		Organ organ = TypeChange.organForm2Obj(organForm);
		String organId = StrKey36.getNextSeq(sysDao.getMaxStringId(Obj2DBMap
				.getDBName("Organ"), "id"));
		organ.setId(organId);
		if (organ.getCode() != null && organ.getCode().indexOf("_") >= 0) {
			// 已经拼接过id;
		} else {
			organ.setCode(Constant.getConstant("sysid") + organId);
		}
		String pid = organForm.getPid();
		if (pid != null && pid.equals("")) {
			List<OrganForm> orgList = schOrgan(null, " and t.id = '" + pid
					+ "'");
			if (orgList.size() > 0) {
				organ.setTreeTrack(orgList.get(0).getTreeTrack() + "-"
						+ organId);
			}
		}
		organ
				.setOrd(sysDao
						.getMaxLongId(Obj2DBMap.getDBName("Organ"), "ord") + 1);
		sysDao.saveObject(organ);
		return organId;
	}

	/**
	 * 修改组织机构
	 * 
	 * @param organForm
	 */
	public void updOrgan(OrganForm organForm) {
		Organ organ = TypeChange.organForm2Obj(organForm);
		sysDao.saveObject(organ);
	}

	/**
	 * 删除组织机构
	 * 
	 * @param organId
	 */
	public void delOrgan(String organId) {
		sysDao.removeObject(Organ.class, organId);
	}

	/**
	 * 根据ID查询组织机构
	 * 
	 * @param organId
	 * @return
	 */
	public OrganForm schOrganById(String organId) {
		Organ organ = (Organ) sysDao.getObject(Organ.class, organId);
		OrganForm organForm = TypeChange.organObj2Form(organ);
		return organForm;
	}

	/**
	 * 根据功能单元查询组织机构
	 * 
	 * @param unitId
	 * @return
	 */
	public List<OrganForm> schOrganByUnitId(String unitId) {
		List<Organ> organList = sysDao.schOrganByUnitId(unitId);
		List<OrganForm> organFormList = new ArrayList<OrganForm>();
		for (int i = 0; i < organList.size(); i++) {
			organFormList.add(TypeChange.organObj2Form(organList.get(i)));
		}
		return organFormList;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	// 功能基本方法
	public List<FunctionForm> schFunction(FunctionForm functionForm,
			String condition) {
		List<Function> functionList = sysDao.schFunction(functionForm,
				condition);
		List<FunctionForm> functionFormList = new ArrayList<FunctionForm>();
		for (int i = 0; i < functionList.size(); i++) {
			functionFormList.add(TypeChange.functionObj2Form(functionList
					.get(i)));
		}
		return functionFormList;
	}

	public FunctionForm schFunctionById(String funcId) {
		Function function = (Function) getObject(Function.class, funcId);
		FunctionForm funcForm = TypeChange.functionObj2Form(function);
		return funcForm;
	}

	public void addFunction(FunctionForm functionForm) {
		Function function = TypeChange.functionForm2Obj(functionForm);
		String funcId = StrKey36.getNextSeq(sysDao.getMaxStringId(Obj2DBMap
				.getDBName("Function"), "id"));
		function.setId(funcId);
		FunctionForm parent = schFunctionById(function.getPid());
		function.setTreeTrack(parent.getTreeTrack() + "-" + funcId);
		function.setOrd(sysDao.getMaxLongId(Obj2DBMap.getDBName("Function"),
				"ord") + 1);
		sysDao.saveObject(function);
	}

	public void updFunction(FunctionForm functionForm) {
		Function function = TypeChange.functionForm2Obj(functionForm);
		sysDao.saveObject(function);
	}

	public void delFunction(String functionId) {
		sysDao.removeObject(Function.class, functionId);
	}

	// 根据角色查找功能列表，角色定义时用到
	public List<FunctionForm> schFunctionsByRoleId(String roleId) {
		List<Function> functionList = sysDao.schFunctionsByRoleId(roleId);
		List<FunctionForm> functionFormList = new ArrayList<FunctionForm>();
		for (int i = 0; i < functionList.size(); i++) {
			functionFormList.add(TypeChange.functionObj2Form(functionList
					.get(i)));
		}
		return functionFormList;
	}

	// /**
	// * 查询某用户下的功能集合
	// *
	// * @param roleId
	// * 角色Id
	// * @return 功能List
	// */
	// public List<FunctionForm> schFunctionByUser(String userId) {
	// List<FunctionForm> functionFormList = new ArrayList<FunctionForm>();
	// List<RoleForm> roleFormList = schRoleByUserId(userId);
	// RoleForm roleForm;
	// Map<String, String> functionMap = new HashMap<String, String>();//
	// 用来过滤重复的funcId
	// for (int i = 0; i < roleFormList.size(); i++) {
	// roleForm = roleFormList.get(i);
	// List<FunctionForm> functionList = schFunctionsByRoleId(roleForm
	// .getId());
	//
	// for (int m = 0; m < functionList.size(); m++) {
	// if (functionList.get(m) != null
	// && !functionList.get(m).equals("")) {
	// functionMap.put(functionList.get(m).getId(), functionList
	// .get(m).getId());
	// }
	// }
	// }
	// String functionId;
	// String sql = "";
	// for (Iterator iter = functionMap.keySet().iterator(); iter.hasNext();) {
	// functionId = (String) iter.next();
	// sql += " or t.id = '" + functionId + "'";
	// }
	// List<Function> functionList = sysDao.schFunction(null, " and (1!=1 "
	// + sql + ")");
	// for (int i = 0; i < functionList.size(); i++) {
	// functionFormList.add(TypeChange.functionObj2Form(functionList
	// .get(i)));
	// }
	// return functionFormList;
	// }

	// 用户登陆时用到
	/**
	 * 根据用户获取功能
	 * 
	 * @param userId
	 * @return 功能列表<FunctionForm>
	 */
	public List<FunctionForm> schFunctionsByUserId(String userId) {
		List<Function> functionList = sysDao.schFunctionsByUserId(userId);
		List<FunctionForm> functionFormList = new ArrayList<FunctionForm>();
		for (int i = 0; i < functionList.size(); i++) {
			functionFormList.add(TypeChange.functionObj2Form(functionList
					.get(i)));
		}

		return functionFormList;
	}

	// 功能单元定义时用到
	/**
	 * 根据功能单元查询功能列表
	 * 
	 * @param unitId
	 * @return 功能列表<FunctionForm>
	 */
	public List<FunctionForm> schFunctionsByFunctionUnitId(String unitId) {
		List<Function> functionList = sysDao
				.schFunctionsByFunctionUnitId(unitId);
		List<FunctionForm> functionFormList = new ArrayList<FunctionForm>();
		for (int i = 0; i < functionList.size(); i++) {
			functionFormList.add(TypeChange.functionObj2Form(functionList
					.get(i)));
		}
		return functionFormList;
	}

	// 制定机构下的角色时用到，列出机构下哪些功能可用。
	/**
	 * 根据机构通过功能单元查询功能
	 * 
	 * @param organId
	 * @return 功能列表<FunctionForm>
	 */
	public List<FunctionForm> schFunctionsByOrganId(String organId) {
		List<Function> functionList = sysDao.schFunctionsByOrganId(organId);
		List<FunctionForm> functionFormList = new ArrayList<FunctionForm>();
		for (int i = 0; i < functionList.size(); i++) {
			functionFormList.add(TypeChange.functionObj2Form(functionList
					.get(i)));
		}
		return functionFormList;
	}

	// 制定机构下的角色时用到
	/**
	 * 根据机构通过角色查询功能;
	 * 
	 * @param organId
	 * @return 功能列表<FunctionForm>
	 */
	public List<FunctionForm> schFuncByOrganId(String organId) {
		List<Function> functionList = sysDao.schFuncByOrganId(organId);
		List<FunctionForm> functionFormList = new ArrayList<FunctionForm>();
		for (int i = 0; i < functionList.size(); i++) {
			functionFormList.add(TypeChange.functionObj2Form(functionList
					.get(i)));
		}
		return functionFormList;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	// 角色基本方法
	public String addRole(RoleForm roleForm) {
		Role role = TypeChange.roleForm2Obj(roleForm);
		String roleId = StrKey36.getNextSeq(sysDao.getMaxStringId(Obj2DBMap
				.getDBName("Role"), "id"));
		role.setId(roleId);
		role
				.setOrd(sysDao.getMaxLongId(Obj2DBMap.getDBName("Role"), "ord") + 1);
		sysDao.saveObject(role);
		return roleId;
	}

	public void delRole(String roleId) {
		sysDao.removeObject(Role.class, roleId);
	}

	public void updRole(RoleForm roleForm) {
		Role role = TypeChange.roleForm2Obj(roleForm);
		sysDao.saveObject(role);
	}

	public RoleForm schRole(String roleId) {
		Role role = (Role) sysDao.getObject(Role.class, roleId);
		RoleForm roleForm = TypeChange.roleObj2Form(role);
		return roleForm;
	}

	public List<RoleForm> schRole(RoleForm roleForm) {
		List<Role> roleList = sysDao.schRole(roleForm);
		List<RoleForm> roleFormList = new ArrayList<RoleForm>();
		for (int i = 0; i < roleList.size(); i++) {
			roleForm = TypeChange.roleObj2Form(roleList.get(i));
			roleFormList.add(roleForm);
		}
		return roleFormList;
	}

	/**
	 * 根据功能ID查询角色
	 * 
	 * @param functionId
	 * @return
	 */
	public List<RoleForm> schRoleByFunction(String functionId) {
		List<Role> roleList = sysDao.schRoleByFunction(functionId);
		List<RoleForm> roleFormList = new ArrayList<RoleForm>();
		for (int i = 0; i < roleList.size(); i++) {
			roleFormList.add(TypeChange.roleObj2Form(roleList.get(i)));
		}
		return roleFormList;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	// 功能单元基本方法
	public String addFunctionUnit(FunctionUnitForm functionUnitForm) {
		FunctionUnit functionUnit = TypeChange
				.functionUnitForm2Obj(functionUnitForm);
		String unitId = StrKey36.getNextSeq(sysDao.getMaxStringId(Obj2DBMap
				.getDBName("FunctionUnit"), "id"));
		functionUnit.setId(unitId);
		functionUnit.setOrd(sysDao.getMaxLongId(Obj2DBMap
				.getDBName("FunctionUnit"), "ord") + 1);
		sysDao.saveObject(functionUnit);
		return unitId;
	}

	public void updFunctionUnit(FunctionUnitForm functionUnitForm) {
		FunctionUnit functionUnit = TypeChange
				.functionUnitForm2Obj(functionUnitForm);
		sysDao.saveObject(functionUnit);
	}

	public List<FunctionUnitForm> schFunctionUnit(
			FunctionUnitForm functionUnitForm, String condition) {
		List<FunctionUnit> functionUnitList = sysDao.schFunctionUnit(
				functionUnitForm, condition);
		List<FunctionUnitForm> functionUnitFormList = new ArrayList<FunctionUnitForm>();
		for (int i = 0; i < functionUnitList.size(); i++) {
			functionUnitForm = TypeChange.functionUnitObj2Form(functionUnitList
					.get(i));
			functionUnitFormList.add(functionUnitForm);
		}
		return functionUnitFormList;
	}

	public FunctionUnitForm schFunctionUnitById(String funcUnitId) {
		FunctionUnit obj = (FunctionUnit) sysDao.getObject(FunctionUnit.class,
				funcUnitId);
		FunctionUnitForm form = TypeChange.functionUnitObj2Form(obj);
		return form;
	}

	public void delFunctionUnit(String funcUnitId) {
		sysDao.removeObject(FunctionUnit.class, funcUnitId);
	}

	/**
	 * 根据组织机构ID查询功能单元列表
	 * 
	 * @param organId;//机构Id
	 * @return 功能单元列表
	 */
	public List<FunctionUnitForm> schFunctionUnitByOrganId(String organId) {
		List<FunctionUnit> fUnit = sysDao.schFunctionUnitByOrganId(organId);
		List<FunctionUnitForm> fUnitForm = new ArrayList<FunctionUnitForm>();
		for (int i = 0; i < fUnit.size(); i++) {
			fUnitForm.add(TypeChange.functionUnitObj2Form(fUnit.get(i)));
		}
		return fUnitForm;
	}

	/**
	 * 根据功能ID查询功能单元
	 * 
	 * @param functionId
	 * @return
	 */
	public List<FunctionUnitForm> schFunctionUnitByFunctionId(String functionId) {
		List<FunctionUnit> fUnit = sysDao
				.schFunctionUnitByFunctionId(functionId);
		List<FunctionUnitForm> fUnitForm = new ArrayList<FunctionUnitForm>();
		for (int i = 0; i < fUnit.size(); i++) {
			fUnitForm.add(TypeChange.functionUnitObj2Form(fUnit.get(i)));
		}
		return fUnitForm;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	/**
	 * 查询某用户的角色集合。
	 * 
	 * @param userId
	 *            用户Id
	 * @return 角色List
	 */
	public List<RoleForm> schRoleByUserId(String userId) {
		List<RoleForm> roleFormList = new ArrayList<RoleForm>();
		List<Role> roleList = sysDao.schRoleByUserId(userId);
		for (int i = 0; i < roleList.size(); i++) {
			roleFormList.add(TypeChange.roleObj2Form(roleList.get(i)));
		}
		return roleFormList;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	// 功能单元-功能中间表基本方法
	/**
	 * 添加
	 * 
	 * @param unitFunction
	 */
	public void addUnitFunction(UnitFunction unitFunction) {
		unitFunction.setId(StrKey36.getNextSeq(sysDao.getMaxStringId(Obj2DBMap
				.getDBName("UnitFunction"), "id")));
		sysDao.saveObject(unitFunction);
	}

	/**
	 * 删除
	 * 
	 * @param unitFunction
	 */
	public void delUnitFunction(UnitFunction unitFunction) {
		sysDao.removeUnitFunction(unitFunction);
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
	// 角色-用户中间表基本方法
	/**
	 * 添加
	 * 
	 * @param roleUser
	 */
	public void addRoleUser(RoleUser roleUser) {
		roleUser.setId(StrKey36.getNextSeq(sysDao.getMaxStringId(Obj2DBMap
				.getDBName("RoleUser"), "id")));
		sysDao.saveObject(roleUser);
	}

	/**
	 * 删除
	 * 
	 * @param roleUser
	 */
	public void delRoleUser(RoleUser roleUser) {
		sysDao.removeRoleUser(roleUser);
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	// 角色-功能中间表基本方法
	/**
	 * 添加
	 * 
	 * @param roleFunction
	 */
	public void addRoleFunction(RoleFunction roleFunction) {
		roleFunction.setId(StrKey36.getNextSeq(sysDao.getMaxStringId(Obj2DBMap
				.getDBName("RoleFunction"), "id")));
		sysDao.saveObject(roleFunction);
	}

	/**
	 * 删除
	 * 
	 * @param roleFunction
	 */
	public void delRoleFunction(RoleFunction roleFunction) {
		sysDao.removeRoleFunction(roleFunction);
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	// 机构-功能单元中间表基本方法
	/**
	 * 添加
	 * 
	 * @param organUnit
	 */
	public void addOrganUnit(OrganUnit organUnit) {
		organUnit.setId(StrKey36.getNextSeq(sysDao.getMaxStringId(Obj2DBMap
				.getDBName("OrganUnit"), "id")));
		sysDao.saveObject(organUnit);
	}

	/**
	 * 删除
	 * 
	 * @param organUnit
	 */
	public void delOrganUnit(OrganUnit organUnit) {
		sysDao.removeOrganUnit(organUnit);
	}

	public List<ModuleForm> schModule(ModuleForm moduleForm) {
		List<Module> moduleList = sysDao.schModule(moduleForm);
		List<ModuleForm> moduleFormList = new ArrayList<ModuleForm>();
		for (Module module:moduleList) {
			moduleFormList.add(TypeChange.moduleObj2Form(module));
		}
		return moduleFormList;
	}

	public ModuleForm schModuleById(String moduleId) {
		Module module = (Module) sysDao.getObject(Module.class, moduleId);
		ModuleForm moduleForm = TypeChange.moduleObj2Form(module);
		return moduleForm;
	}

	public String addModule(ModuleForm moduleForm) {
		Module module = TypeChange.moduleForm2Obj(moduleForm);
		module.setId(StrKey36.getNextSeq(sysDao.getMaxStringId("SYS_MODULE","ID")));
		String id = module.getId();
		saveObject(module);
		return id;
	}

	public void updModule(ModuleForm moduleForm) {
		Module module = TypeChange.moduleForm2Obj(moduleForm);
		saveObject(module);
	}

	public void delModule(String moduleId) {
		removeObject(Module.class, moduleId);
	}
	
	public List<MachineForm> schMachine(MachineForm machineForm) {
		List<Machine> machineList = sysDao.schMachine(machineForm);
		List<MachineForm> machineFormList = new ArrayList<MachineForm>();
		for (Machine machine:machineList) {
			machineFormList.add(TypeChange.machineObj2Form(machine));
		}
		return machineFormList;
	}

	public MachineForm schMachineById(String machineId) {
		Machine machine = (Machine) sysDao.getObject(Machine.class, machineId);
		MachineForm machineForm = TypeChange.machineObj2Form(machine);
		return machineForm;
	}

	public String addMachine(MachineForm machineForm) {
		Machine machine = TypeChange.machineForm2Obj(machineForm);
		machine.setId(StrKey36.getNextSeq(sysDao.getMaxStringId("SYS_MACHINE","ID")));
		String id = machine.getId();
		saveObject(machine);
		return id;
	}

	public void updMachine(MachineForm machineForm) {
		Machine machine = TypeChange.machineForm2Obj(machineForm);
		saveObject(machine);
	}

	public void delMachine(String machineId) {
		removeObject(Machine.class, machineId);
	}
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public SysManageDao getSysDao() {
		return sysDao;
	}

	public void setSysDao(SysManageDao sysDao) {
		this.sysDao = sysDao;
	}

}
