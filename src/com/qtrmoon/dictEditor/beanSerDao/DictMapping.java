package com.qtrmoon.dictEditor.beanSerDao;

public class DictMapping{
	public String id,pid,label;//MODE1ģʽ�ֵ���ֶ�Ӱ�䡣[id,pid,label,trace]
	public String code,sequence;//MODE2ģʽ�ֵ���ֶ�Ӱ�䡣[code,sequence,label,depth]
	/**
	 * MODE2ģʽ�����Ͳ�ı�����ȡ���10,1001�����2��
	 * ��������£���Щ�����ֵ���㲻��ͬ��λ���ģ����磺10,1001,1001001�������2,2,3
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
	//���л�
	public String toString(){
		return id+pid+label+code+sequence;
	}
}