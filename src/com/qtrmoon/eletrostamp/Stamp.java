package com.qtrmoon.eletrostamp;

import java.applet.Applet;
import java.io.File;

public class Stamp extends Applet {
	String path;
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() {
		super.init();
	}

	

	@Override
	public void start() {
		super.start();
		File[] fs=File.listRoots();
		for(File f:fs){
			path=f.getAbsolutePath();
			path=path+File.separator+"sign"+File.separator+"sign.png";
			if(new File(path).exists()){
				break;
			}
		}
	}

	public String getPath(){
		return path;
	}
}