package com.qtrmoon.toolkit.jacob;

public class WordFont {
	boolean bold=false;//是否为粗体
	boolean italic=false;//是否为斜体
	boolean underLine=false;//是否带下划线
	String color=COLOR_BLACK;//rgb 字体颜色 例如：红色"ff0000"
	String size="12";//字体大小 12:小四 16:三号
	String name="宋体";//字体名称 例如：宋体，新宋体，楷体，隶书
	String Spacing="0";//字间距
	
	public static final String COLOR_RED="FF0000";
	public static final String COLOR_GREEN="00FF00";
	public static final String COLOR_BLUE="0000FF";
	public static final String COLOR_BLACK = "000000";
	

	public WordFont(){
	}
	
	public WordFont(String name, String color, String size, boolean isBold) {
		super();
		this.bold = isBold;
		this.color = color;
		this.size = size;
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		//RGB转BGR
		color=color.substring(4)+color.substring(2,4)+color.substring(0,2);
		this.color = Integer.toString(Integer.parseInt(color,16));
	}

	public boolean getBold() {
		return bold;
	}

	public void setBold(boolean isBold) {
		this.bold = isBold;
	}

	public boolean getItalic() {
		return italic;
	}

	public void setItalic(boolean isItalic) {
		this.italic = isItalic;
	}

	public boolean getUnderLine() {
		return underLine;
	}

	public void setUnderLine(boolean isUnderLine) {
		this.underLine = isUnderLine;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSpacing() {
		return Spacing;
	}

	public void setSpacing(String spacing) {
		Spacing = spacing;
	}
	
	
}
