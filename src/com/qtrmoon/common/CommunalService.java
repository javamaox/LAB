package com.qtrmoon.common;

import java.util.List;

public class CommunalService extends BaseService {
	private CommunalDao communalDao;

	/**
	 * @param table
	 * @param pk
	 * @return ���ֵ
	 * 
	 * ��ȡtable����pk�ֶε����ֵ
	 */
	public long getMaxLongId(String table, String pk) {
		return communalDao.getMaxLongId(table, pk);
	}

	/**
	 * @param table
	 * @param pk
	 * @return ���ֵ
	 * 
	 * ��ȡtable����pk�ֶε����ֵ
	 */
	public String getMaxStringId(String table, String pk) {
		return communalDao.getMaxStringId(table, pk);
	}
	
	/**
	 * �����ֶβ�ѯ�����
	 */
	public List schData(String sql, String[] cols,String[] type) {
		return communalDao.schData(sql, cols, type);
	}
	
	public void execute(String sql) {
		communalDao.execute(sql);
	}
	
	/**
	 * ��ȡĳ����ĵ�ǰ�����к�
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
