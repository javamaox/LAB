package com.qtrmoon.toolkit.tableapplet;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.UIManager;

import com.qtrmoon.dictEditor.beanSerDao.DictionaryForm;


public class StatTable extends Applet {
	int width=100,height=100;
	JScrollPane jsp;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void init() {
		String widthStr=getParameter("width");
		String heightStr=getParameter("height");
		if(widthStr!=null){
			width=Integer.parseInt(widthStr);
		}
		if(heightStr!=null){
			height=Integer.parseInt(heightStr);
		}
		//this.setPreferredSize(new Dimension(width, height));
		
		super.init();
	}

	@Override
	public void start() {
		String headStr=getParameter("head");
		String dataStr=getParameter("data");
		String staticHead=getParameter("staticHead");
		String staticData=getParameter("staticData");
		StoreTable storeTable=new StoreTable(headStr, dataStr,staticHead,staticData);
		jsp=storeTable.getScrollPanel();
		jsp.setPreferredSize(new Dimension(width, height-5));
		this.add(jsp,BorderLayout.CENTER);

		this.setVisible(true);
		super.start();
	}
	
	public void setSize(int w,int h){
		width=w;
		height=h;
		jsp.setPreferredSize(new Dimension(w, h-5));
	}
	
}
