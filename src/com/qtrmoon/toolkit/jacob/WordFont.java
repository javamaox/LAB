package com.qtrmoon.toolkit.jacob;

public class WordFont {
	boolean bold=false;//�Ƿ�Ϊ����
	boolean italic=false;//�Ƿ�Ϊб��
	boolean underLine=false;//�Ƿ���»���
	String color=COLOR_BLACK;//rgb ������ɫ ���磺��ɫ"ff0000"
	String size="12";//�����С 12:С�� 16:����
	String name="����";//�������� ���磺���壬�����壬���壬����
	String Spacing="0";//�ּ��
	
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
		//RGBתBGR
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
