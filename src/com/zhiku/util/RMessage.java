package com.zhiku.util;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	/**
	 * 使用状态和信息进行构造
	 * @param status
	 * @param message
	 */
	public RMessage(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	/**
	 * 将RMessage类中的信息转化为Json格式
	 * @return Json格式的字符串
	 */
	public String getJson(){
		String result = null;
		ObjectMapper om = new ObjectMapper();
		try {
			result = om.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return result;
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
