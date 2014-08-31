package com.spring.nphone.domain;

/**
 * 推送objce基类
 * @author zhanlang
 *
 */
public class BaseObjce {

	String title;
	
	String description;

	private String custom_content;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return "BaseObjce [title=" + title + ", description=" + description + ", custom_content=" + custom_content + "]";
	}

	public String getCustom_content() {
		return custom_content;
	}

	public void setCustom_content(String custom_content) {
		this.custom_content = custom_content;
	}
	
	
}
