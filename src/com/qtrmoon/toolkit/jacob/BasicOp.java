package com.qtrmoon.toolkit.jacob;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class BasicOp {

	// ѡ���ķ�Χ������
	protected static Dispatch selection = null;
	
	/****************************************
	 ************ ������ƶ� *************
	 ****************************************/
	
	/**
	 * ��ѡ�������ݻ��߲���������ƶ�
	 * @param pos �ƶ��ľ���
	 */
	public void moveDown(int pos) {
		for (int i = 0; i < pos; i++)
			Dispatch.call(selection, "MoveDown");
	}

	/**
	 * ��ѡ�������ݻ����������ƶ�
	 * @param pos �ƶ��ľ���
	 */
	public void moveUp(int pos) {
		for (int i = 0; i < pos; i++)
			Dispatch.call(selection, "MoveUp");
	}

	/**
	 * ��ѡ�������ݻ��߲���������ƶ�
	 * @param pos �ƶ��ľ���
	 */
	public void moveLeft(int pos) {
		for (int i = 0; i < pos; i++) {
			Dispatch.call(selection, "MoveLeft");
		}
	}

	/**
	 * ��ѡ�������ݻ��߲���������ƶ�
	 * @param pos �ƶ��ľ���
	 */
	public void moveRight(int pos) {
		for (int i = 0; i < pos; i++)
			Dispatch.call(selection, "MoveRight");
	}
	
	/** ����Ctrl + Home�� */
	public void goToBegin() {
		Dispatch.call(selection, "HomeKey", "6");
	}
	/** ����Ctrl + End�� */
	public void goToEnd() {
		Dispatch.call(selection, "EndKey", "6");
	}
	/** ����Home�� */
	public void home() {
		Dispatch.call(selection, "HomeKey", "5");
	}

	/** ����End�� */
	public void end() {
		Dispatch.call(selection, "EndKey", "5");
	}
	
	
	/**
	 * ����ѡ, ֱ������ı�
	 * @param s
	 */
	public void write(String s) {
		write(s,null);
	}
	public void write(String s,WordFont font) {
		if(font!=null){
			setFont(font);
			Dispatch.call(selection, "TypeText",s);
		}else{
			Dispatch.put(selection, "Text", s);
		}
	}
	/**
	 * ���õ�ǰѡ�����ݵ�����
	 */
	public void setFont(WordFont wf) {
		Dispatch font = Dispatch.get(selection, "Font").toDispatch();
		Dispatch.put(font, "Name", new Variant(wf.getName()));
		Dispatch.put(font, "Bold", new Variant(wf.getBold()));
		Dispatch.put(font, "Italic", new Variant(wf.getItalic()));
		Dispatch.put(font, "Underline", new Variant(wf.getUnderLine()));
		Dispatch.put(font, "Color", wf.getColor());
		Dispatch.put(font, "Size", wf.getSize());
		Dispatch.put(font, "Spacing", wf.getSpacing());
		
	}
	/**
	 * ѡ��dispatch����
	 * 
	 * @param dispatch�����䣬��ǲ��
	 */
	public void select(Dispatch dispatch) {
		Dispatch.call(dispatch, "Select");
	}
	/**
	 * �ڵ�ǰ��괦��ճ��
	 */
	public void paste() {
		Dispatch.call(selection, "Paste");
	}
	/**
	 * �ڵ�ǰ��괦������
	 */
	public void copy() {
		Dispatch.call(selection, "Copy");
	}
}
