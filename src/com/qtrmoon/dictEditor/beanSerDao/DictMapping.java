package com.qtrmoon.dictEditor.beanSerDao;

public class DictMapping{
	public String id,pid,label;//MODE1模式字典的字段影射。[id,pid,label,trace]
	public String code,sequence;//MODE2模式字典的字段影射。[code,sequence,label,depth]
	/**
	 * MODE2模式的树型层的编码深度。如10,1001深度是2。
	 * 特殊情况下，有些树型字典各层不是同等位数的，例如：10,1001,1001001。深度是2,2,3
	 */
	public int[] depth;
	
	
	public DictMapping(String mode){
		if(mode==null||mode.equals(DictCatalog.MODE1)){
			id="id";
			pid="pid";
			label="label";
			sequence="id";
		}else{
			code="code";
			sequence="sequence";
			label="label";
			depth=new int[]{2};
		}
	}
	public DictMapping(String id,String pid,String label){
		this.id=id;
		this.pid=pid;
		this.label=label;
		//this.trace=trace;
	}
	public DictMapping(String code,String sequence,String label,String depth){
		this.id=code;
		this.code=code;
		this.sequence=sequence;
		this.label=label;
		if(depth!=null&&!depth.equals("")){
			String[] deps=depth.split(",");
			int[] dep2s=new int[deps.length];
			for(int i=0;i<deps.length;i++){
				dep2s[i]=Integer.parseInt(deps[i]);
			}
			this.depth=dep2s;
		}
	}
	//序列化
	public String toString(){
		return id+pid+label+code+sequence;
	}
}