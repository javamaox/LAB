package com.qtrmoon.textApplet;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Text extends Applet {
	TextField text;

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() {
		this.setLayout(null);
		int width=200,height=22,fontSize=14;
		String widthStr=getParameter("width");
		String heightStr=getParameter("height");
		String fontSizeStr=getParameter("fontSize");
		
		if(widthStr!=null){
			width=Integer.parseInt(widthStr);
		}
		if(heightStr!=null){
			height=Integer.parseInt(heightStr);
		}
		if(fontSizeStr!=null){
			fontSize=Integer.parseInt(fontSizeStr);
		}
		text=new TextField();
		System.out.println(width+":"+height);
		text.setBounds(0, 0, width, height);
		text.setText("");
		text.setEchoChar('*');
		Font font=new Font("Arial",Font.PLAIN,fontSize);
		text.setFont(font);
		text.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//回车登录系统事件。
				
			}
		});
		this.add(text);
		super.init();
	}

	@Override
	public void start() {
		super.start();
	}

	public String getText(){
		return text.getText();
	}
	
	public void setText(String txt){
		text.setText(txt);
	}
}
