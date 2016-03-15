package com.kevin.mmitestx;

public class TestItem {
	//private String icon;
	private String name;
	private String title;
	private Boolean bTest;
	//private static final String STATUS_GOOD = "status_good";
	//private static final String STATUS_NG = "status_not_good";
	//private static final String STATUS_NOT_TEST = "status_not_test";
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getTest() {
		return bTest;
	}
	
	public void setTest(Boolean bTest) {
		this.bTest = bTest;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}	
}
