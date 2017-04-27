package com.qtrmoon.common;

import java.util.List;

public class CommunalService extends BaseService {
	private CommunalDao communalDao;

	/**
	 * @param table
	 * @param pk
	 * @return 最大值
	 * 
	 * 获取table表中pk字段的最大值
	 */
	public long getMaxLongId(String table, String pk) {
		return communalDao.getMaxLongId(table, pk);
	}

	/**
	 * @param table
	 * @param pk
	 * @return 最大值
	 * 
	 * 获取table表中pk字段的最大值
	 */
	public String getMaxStringId(String table, String pk) {
		return communalDao.getMaxStringId(table, pk);
	}
	
	/**
	 * 按照字段查询任意表。
	 */
	public List schData(String sql, String[] cols,String[] type) {
		return communalDao.schData(sql, cols, type);
	}
	
	public void execute(String sql) {
		communalDao.execute(sql);
	}
	
	/**
	 * 获取某个表的当前的序列号
	 * 
	 * @return
	 */
	public Long getSequences(String seq) {
		Long l = communalDao.getSequences(seq);
		return l;
	}
	
	public CommunalDao getCommunalDao() {
		return communalDao;
	}

	public void setCommunalDao(CommunalDao communalDao) {
		this.communalDao = communalDao;
	}

}
