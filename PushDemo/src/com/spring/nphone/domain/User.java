package com.spring.nphone.domain;

public class User {

	private long channelId;
	
	private String userId;

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return "User [channelId=" + channelId + ", userId=" + userId + "]";
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
