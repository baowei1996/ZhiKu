package com.zhiku.util;

import java.util.Map;

/**
 * 这个类封装了要返回的信息
 * 并使用Jackson格式化为json格式
 * @author DBD
 *
 */
public class RMessage {
	private int status ;
	private String message;
	
	private Map<String ,Object> data;
	
	public RMessage(){}

	public RMessage(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}
