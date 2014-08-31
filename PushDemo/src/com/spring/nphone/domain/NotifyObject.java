package com.spring.nphone.domain;

public class NotifyObject  {

	private NotifyType notifyType;
	
	private String phone;
	
	private String content;
	
	private String name;
	
	private long time;

	public NotifyType getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(NotifyType notifyType) {
		this.notifyType = notifyType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "NotiftObject [notifyType=" + notifyType + ", phone=" + phone + ", content=" + content + ", name=" + name + ", time=" + time + "]";
	}
	
}
