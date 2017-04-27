package com.qtrmoon.dictEditor.beanSerDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.qtrmoon.common.BaseDAO;
import com.qtrmoon.common.Constant;
import com.qtrmoon.dictEditor.DictBuffer;
import com.qtrmoon.dictEditor.DictCfg;
import com.qtrmoon.dictEditor.Endecrypt;

public class DictDao extends BaseDAO {

	protected void initDao() {
		// do nothing
	}

	/**
	 * 查询所有字典数据
	 * @param catalog
	 * @return
	 */
	public List<DictionaryForm> findDict(DictCatalog catalog){
		Statement stm = null;
		try {
			String sql = "select * from " + catalog.getTablename() +" t "+getSql(catalog,true);
			stm=getConn().createStatement();
			ResultSet rSet=stm.executeQuery(sql);
			return parseData(rSet,catalog);
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("字典数据表["+catalog.getTablename()+"]不存在，或字段映射配置错误。");
		}finally{
			close(stm);
		}
		return null;
	}
	
	/**
	 * 计算字典数据量
	 * @param catalog
	 * @return
	 */
	public long countDict(DictCatalog catalog){
		Statement stm = null;
		try {
			//检查字典的数据量，如果过大则不加载。
			String sql = "select count(*) as sum from " + catalog.getTablename() +" t "+getSql(catalog,false);
			Statement statement = getConn().createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet.next();
			long num=resultSet.getLong("sum");
			statement.close();
			catalog.setSize(num);
			return num;
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("字典数据表["+catalog.getTablename()+"]不存在，或字段映射配置错误。");
		}finally{
			close(stm);
		}
		return 0;
	}
	
	/**
	 * 找出前top条记录
	 * @param catalog
	 * @param top
	 * @return
	 */
	public List<DictionaryForm> findTopDict(DictCatalog catalog,int top){
		Statement stm = null;
		try {
			String sql = getSql(catalog,true);
			
			ResultSet rSet = null;
			String dbType=DictBuffer.getDbType();
			if(dbType.equalsIgnoreCase("oracle")){
				sql="select a.* from(select rownum as rn,t.* from " + catalog.getTablename() +" t "+sql+") a where rn<="+top;
			}else if(dbType.equalsIgnoreCase("mysql")){
				sql="SELECT * FROM "+catalog.getTablename()+" t "+sql+" LIMIT "+top;
			}else{
				sql="SELECT top "+top+" * FROM "+catalog.getTablename()+" t "+sql;
			}
			stm = getConn().createStatement();
			rSet = stm.executeQuery(sql);
			return parseData(rSet,catalog);
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("无法找到字典["+catalog.getId()+"]，或字典表非法。字典表字段应该是ID,LABEL,PID。");
		}finally{
			close(stm);
		}
		return null;
	}
	
	/**
	 * @param catalog
	 * @param withOrder true:带上排序
	 * @return
	 */
	private String getSql(DictCatalog catalog,boolean withOrder) {
		String sql="";
		String root=catalog.getRoot();
		if(root!=null&&!root.equals("")){
			String[] roots=root.split(",");
			if(catalog.isListType()){
				for(String r:roots){
					if(root.indexOf("-")>0){//区间配置
						String[] ft=root.split("-");
						int f=Integer.parseInt(ft[0]);
						int t=Integer.parseInt(ft[1]);
						for(int i=f;i<=t;i++){
							sql+=" t."+catalog.getMapping().id+" like '"+i+"%' or";
						}
					}else{
						sql+=" t."+catalog.getMapping().id+" like '"+r+"%' or";
					}
				}
			}else{
				for(String r:roots){
					if(root.indexOf("-")>0){//区间配置，设定了根的字典会只取两层。这里有待修改，暂时这样吧。
						String[] ft=root.split("-");
						int f=Integer.parseInt(ft[0]);
						int t=Integer.parseInt(ft[1]);
						for(int i=f;i<=t;i++){
							sql+=" t."+catalog.getMapping().id+" = '"+i+"' or";
							sql+=" t."+catalog.getMapping().pid+" like '"+r+"%' or";
						}
					}else{
						sql+=" t."+catalog.getMapping().id+" = '"+r+"' or";
						sql+=" t."+catalog.getMapping().pid+" like '"+r+"%' or";
					}
				}
			}
			sql=sql.substring(0,sql.length()-2);
			sql=" and ("+sql+") ";
		}
		if(catalog.getSql()!=null&&!catalog.getSql().equals("")){
			sql+=catalog.getSql();
		}
		sql=" where 1=1 "+sql;
		if(withOrder&&(catalog.getOrderBy()!=null&&!catalog.getOrderBy().equals(""))){
			sql+=" order by "+catalog.getOrderBy();
		}
		sql=dialect(sql);
		return sql;
	}

	/**
	 * 根据字典配置，解析字典数据。
	 * @param rSet
	 * @param catalog
	 * @return
	 */
	private List<DictionaryForm> parseData(ResultSet rSet, DictCatalog catalog) {
		List<DictionaryForm> dictList = new ArrayList<DictionaryForm>();
		try{
			boolean hasOrder=false;
			Integer sort=null;
			if(catalog.getOrderBy()!=null&&!catalog.getOrderBy().equals("")){
				hasOrder=true;
			}
			
			boolean hasExp=false;
			String expCols[]=null;
			if(catalog.getExp()!=null&&!catalog.getExp().equals("")){
				expCols=catalog.getExp().split(",");
				hasExp=true;
			}
			String expValue="";
			if(catalog.getMapping()!=null&&catalog.getMode().equals(DictCatalog.MODE1)){
				String id,label,pid;
				while (rSet.next()) {
					id = rSet.getString(catalog.getMapping().id).trim();
					label = rSet.getString(catalog.getMapping().label);
					if(label!=null)label=label.trim();
					if(catalog.isEncrypt()){
						label=Endecrypt.decrypt(label);
					}
					pid = catalog.getMapping().pid;
					if(pid==null||pid.equals("")){
						pid="";
					}else{
						pid = rSet.getString(pid);
						if(pid!=null)pid=pid.trim();
					}
					sort = null;
					if(hasOrder){
						sort = rSet.getInt(catalog.getOrderBy());
					}
					if(hasExp){
						expValue=getExpValue(rSet,expCols);
					}
					dictList.add(new DictionaryForm(id, label, pid, sort,expValue));
				}
			}else if(catalog.getMapping()!=null&&catalog.getMode().equals(DictCatalog.MODE2)){
				List<DictionaryForm> list = new ArrayList<DictionaryForm>();
				while (rSet.next()) {
					String id = rSet.getString(catalog.getMapping().code).trim();
					String label = rSet.getString(catalog.getMapping().label).trim();
					String sequence = rSet.getString(catalog.getMapping().sequence).trim();
					sort = null;
					if(hasOrder){
						sort = rSet.getInt(catalog.getOrderBy());
					}
					if(hasExp){
						expValue=getExpValue(rSet,expCols);
					}
					list.add(new DictionaryForm(id,label,sequence,sort,expValue));
				}
				//树形字典需处理sequence，转化其为Pid。
				if(!catalog.isListType()){
					int[] depth = catalog.getMapping().depth;
					if(depth.length == 1){//等位数的树型序号编码
						int dep=depth[0];
						for(int i = 0;i<list.size();i++){
							DictionaryForm dict = list.get(i);
							String pid = dict.getPid();
							if(pid!=null&&pid.length()>=dep){
								pid=pid.substring(0,pid.length()-dep);
								for(int j = 0;j<list.size();j++){
									String sequence = list.get(j).getPid();
									if(sequence!=null&&sequence.equals(pid)){
										pid = list.get(j).getId();
										break;
									}
								}
							}
							dictList.add(new DictionaryForm(dict.getId(),dict.getLabel(),pid,dict.getSort(),dict.getExp()));
						}
					}else if(depth.length>1){//异位数的树型序号编码
						for(int i = 0;i<list.size();i++){
							DictionaryForm dict = list.get(i);
							String pid = dict.getPid();
							int len=0;
							boolean findit=false;
							for(int dep:depth){
								len+=dep;
								if(len==pid.length()){
									pid=pid.substring(0,pid.length()-dep);
									findit=true;
									break;
								}
							}
							if(!findit){
								System.out.println("未找到合适的pid，检查字典"+catalog.getId()+":["+dict.getId()+","+dict.getLabel()+"]的depth的配置是否符合字典数据。");
							}
							for(int j = 0;j<list.size();j++){
								String sequence = list.get(j).getPid();
								if(sequence!=null&&sequence.equals(pid)){
									pid=list.get(j).getId();//这里找到真正的父了。
									break;
								}
							}
							dictList.add(new DictionaryForm(dict.getId(),dict.getLabel(),pid,dict.getSort(),dict.getExp()));
						}
					}
				}else{
					dictList=list;
				}
			}else {
				while (rSet.next()) {
					sort = null;
					if(hasOrder){
						sort = rSet.getInt(catalog.getOrderBy());
					}
					if(hasExp){
						expValue=getExpValue(rSet,expCols);
					}
					dictList.add(new DictionaryForm(rSet.getString("id"), rSet
							.getString("label"), rSet.getString("pid"),sort,expValue));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("无法找到字典["+catalog.getId()+"]，或字典表非法。字典表字段应该是ID,LABEL,PID。");
		}
		return dictList;
	}

	private String getExpValue(ResultSet rSet,String[] expCols) throws SQLException {
		String expValue="",value;
		for(String expcol:expCols){
			value=rSet.getString(expcol);
			if(value!=null&&!value.equals("")){
				expValue+=rSet.getString(expcol)+DictBuffer.EXP_SEP;
			}
		}
		if(!expValue.equals("")){
			expValue=expValue.substring(0,expValue.length()-DictBuffer.EXP_SEP.length());
		}
		return expValue;
	}

	/** 查询字典记录 */
	public DictionaryForm findDictById(DictCatalog catalog, String id) {
		String sql = "select * from " + catalog.getTablename() + " t where t."+catalog.getMapping().id+"='" + id + "'";
		Statement stm = null;
		ResultSet rSet = null;
		DictionaryForm form = null;
		try {
			stm = getConn().createStatement();
			rSet = stm.executeQuery(sql);
			List<DictionaryForm> l=parseData(rSet, catalog);
			if(l.size()>0){
				return l.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
			System.err.println(sql);
			e.printStackTrace();
		}finally{
			try {
				if(stm!=null){
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return form;
	}
	
	public List<DictionaryForm> findDictByPid(DictCatalog catalog, String pid) {
		Statement stm = null;
		try {
			ResultSet rSet = null;
			String sql="";
			if(catalog.getMode().equals(DictCatalog.MODE1)){
				if(DictBuffer.dummyRootId.equals(pid)){
					sql="select * from "+catalog.getTablename()+" t where t."+catalog.getMapping().pid+" is null and "+catalog.getMapping().id+" is not null";
				}else{
					sql="select * from "+catalog.getTablename()+" t where t."+catalog.getMapping().pid+" ='"+pid+"'";
				}
			}else{
				String tn=catalog.getTablename();
				String seq=catalog.getMapping().sequence;
				int[] depth=catalog.getMapping().depth;
				int pidLength=pid.length();
				int idLength=0,i=0;
				while(idLength<=pidLength){
					if(i<depth.length){
						idLength+=depth[i++];
					}else{//到底了
						idLength+=99;
						break;
					}
				}
				if(DictBuffer.dummyRootId.equals(pid)){
					sql="select * from "+tn+" where length("+seq+")=(select min(LENGTH(a."+seq+")) from "+tn+" a)";
				}else{
					sql="select * from "+tn+" t where t."+seq+" like '"+pid+"%' and length("+seq+")="+idLength;
				}
			}
			sql=dialect(sql);
			stm = getConn().createStatement();
			rSet = stm.executeQuery(sql);
			return parseData(rSet,catalog);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(stm!=null){
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public DictionaryForm findDictByLabel(DictCatalog catalog, String label) {
		Statement stm = null;
		try {
			ResultSet rSet = null;
			String sql="select * from "+catalog.getTablename()+" t where t."+catalog.getMapping().label+" ='"+label+"'";
			stm = getConn().createStatement();
			rSet = stm.executeQuery(sql);
			List<DictionaryForm> dictList = parseData(rSet,catalog);
			if(dictList.size()>0){
				return dictList.get(0);
			}else{
				return null; 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(stm!=null){
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/** 存储字典表记录 */
	public void saveDict(DictCatalog ca, List<DictionaryForm> dictList) {
		String sql=null;
		Statement stm = null;
		try {
			stm = getConn().createStatement();
			for(DictionaryForm df:dictList){
				if(ca.getOrderBy()!=null&&!ca.getOrderBy().equals("")){
					sql = "insert into " + ca.getTablename() + "(id,label,pid,"+ca.getOrderBy()+") values('"
					+ df.getId() + "','" + df.getLabel() + "','" + df.getPid()+ "','"+df.getSort()+"')";
				}else{
					sql = "insert into " + ca.getTablename() + "(id,label,pid) values('"
					+ df.getId() + "','" + df.getLabel() + "','" + df.getPid()
					+ "')";
				}
				stm.addBatch(sql);
			}
			stm.executeBatch();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(stm!=null){
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/** 修改字典表记录 */
	public void updateDict(DictCatalog catalog, DictionaryForm df) {
		String sql = "update " + catalog.getTablename() + " set label='" + df.getLabel()
				+ "',pid='" + df.getPid() + "'";
		if(catalog.getOrderBy()!=null&&!catalog.getOrderBy().equals("")){
			sql+=","+catalog.getOrderBy()+"='"+df.getSort()+"'";
		}
		sql+=" where id='" + df.getId() + "'";
		execute(sql);
	}
	public void updateSort(DictCatalog catalog, String[] ids) {
		String sql="";
		for(int idx=0;idx<ids.length;idx++){
			sql="update " + catalog.getTablename() + " t set t."+catalog.getOrderBy()+"="+idx+" where t.id='"+ids[idx]+"'";
		}
		execute(sql);
	}

	/** 删除字典记录 */
	public void removeDict(DictCatalog catalog, String id) {
		String sql = "delete " + catalog.getTablename() + " where id='" + id + "'";
		execute(sql);
	}

	public void removeDict(DictCatalog catalog, String[] ids) {
		String sql="";
		for(String id:ids){
			if(id!=null&&!id.equals("")){
				sql+=" id='"+id+"' or";
			}
		}
		if(sql!=""){
			sql=sql.substring(0,sql.length()-2);
			sql=" where "+sql;
		}
		sql = "delete " + catalog.getTablename() + sql;
		execute(sql);
	}

	/** 获取字典表Id的最大记录，无记录返回null */
	public String getMaxId(String tableName,String condition,String len) {
		String cond1="",cond2="";
		if(condition!=null){
			cond1=" 1=1 "+condition +" and ";
		}
		if(len!=null){
			cond2=" where length(t.id)="+len;
		}
		String sql = "select max(t.id) id from " + tableName + " t where "+cond1+" length(t.id)=(select max (length(t.id)) id from " + tableName + " t "+cond2+")";
		Statement stm = null;
		ResultSet rSet = null;
		String maxId = null;
		try {
			stm = getConn().createStatement();
			rSet = stm.executeQuery(sql);
			while (rSet.next()) {
				maxId = rSet.getString("id");
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			close(stm);
		}
		return maxId;
	}
	
	/** 创建字典表 */
	public void creatDictCatalog(DictCatalog form) {
		String tableName = form.getTablename().toUpperCase();
		String sql = "";
		sql += "create table \""+Constant.getConstant("projectName")+"\".\"" + tableName + "\"(";
		sql += "\"ID\" VARCHAR2(20) not null,";
		sql += "\"LABEL\" VARCHAR2(255),";
		sql += "\"PID\" VARCHAR2(20),";
		sql += "constraint \"PK_" + tableName + "\" primary key (\"ID\")";
		sql += ");";
		sql += "comment on table \"TJDY\".\"" + tableName + "\" is '"
				+ form.getTabledesc() + "';";
		sql += "create unique index \"TJDY\".\"PK_" + tableName
				+ "\" on \"TJDY\".\"" + tableName + "\"(\"ID\");";
		execute(sql);
	}
	private String dialect(String sql){
		String dbType=DictBuffer.getDbType();
		if(dbType.equalsIgnoreCase("oracle")||dbType.equalsIgnoreCase("mysql")){
			;
		}else{
			sql=sql.replaceAll("%", "*");
		}
		return sql;
	}
}