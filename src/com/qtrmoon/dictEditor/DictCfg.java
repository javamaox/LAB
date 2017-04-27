package com.qtrmoon.dictEditor;

public class DictCfg {
	/**
	 * 小数据量使用有结构查询。|大数据量的使用无结构查询。
	 */
	public static final int MINI_AI=100;
	
	/**
	 * 字典数据量超过该额度，将不使用缓存机制。
	 */
	public static final long MAX_IN_RAM=1500;

	/**
	 * 大数据量智能字典，一次查出的最大条数。
	 */
	public static final int LARGE_AI_PAGESIZE = 100;
}
