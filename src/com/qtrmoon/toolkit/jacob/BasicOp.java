package com.qtrmoon.toolkit.jacob;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class BasicOp {

	// 选定的范围或插入点
	protected static Dispatch selection = null;
	
	/****************************************
	 ************ 插入点移动 *************
	 ****************************************/
	
	/**
	 * 把选定的内容或者插入点向下移动
	 * @param pos 移动的距离
	 */
	public void moveDown(int pos) {
		for (int i = 0; i < pos; i++)
			Dispatch.call(selection, "MoveDown");
	}

	/**
	 * 把选定的内容或插入点向上移动
	 * @param pos 移动的距离
	 */
	public void moveUp(int pos) {
		for (int i = 0; i < pos; i++)
			Dispatch.call(selection, "MoveUp");
	}

	/**
	 * 把选定的内容或者插入点向左移动
	 * @param pos 移动的距离
	 */
	public void moveLeft(int pos) {
		for (int i = 0; i < pos; i++) {
			Dispatch.call(selection, "MoveLeft");
		}
	}

	/**
	 * 把选定的内容或者插入点向右移动
	 * @param pos 移动的距离
	 */
	public void moveRight(int pos) {
		for (int i = 0; i < pos; i++)
			Dispatch.call(selection, "MoveRight");
	}
	
	/** 按下Ctrl + Home键 */
	public void goToBegin() {
		Dispatch.call(selection, "HomeKey", "6");
	}
	/** 按下Ctrl + End键 */
	public void goToEnd() {
		Dispatch.call(selection, "EndKey", "6");
	}
	/** 按下Home键 */
	public void home() {
		Dispatch.call(selection, "HomeKey", "5");
	}

	/** 按下End键 */
	public void end() {
		Dispatch.call(selection, "EndKey", "5");
	}
	
	
	/**
	 * 不反选, 直接输出文本
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
	 * 设置当前选定内容的字体
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
	 * 选中dispatch对象
	 * 
	 * @param dispatch（分配，派遣）
	 */
	public void select(Dispatch dispatch) {
		Dispatch.call(dispatch, "Select");
	}
	/**
	 * 在当前光标处做粘贴
	 */
	public void paste() {
		Dispatch.call(selection, "Paste");
	}
	/**
	 * 在当前光标处做复制
	 */
	public void copy() {
		Dispatch.call(selection, "Copy");
	}
}
