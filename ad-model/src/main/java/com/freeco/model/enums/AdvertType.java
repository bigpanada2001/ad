package com.freeco.model.enums;

public enum AdvertType {

	DOWNLOAD("网页下载APP"),
	REDIRECT("跳转到应用商城"),
	UNKOWN("未知");
	
	AdvertType(String name){
		this.name = name;
	}
	
	private String name;
	
	public String getName(){
		return name;
	}
}
