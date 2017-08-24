package com.zhiku.util;


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
	
	private Object data;
	
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
	 * 静态方法，将message的内容格式化为json
	 * @param m 要格式化的内容
	 * @return 格式化之后的json字符
	 */
	public static String getJson(RMessage m){
		String result = null;
		ObjectMapper om = new ObjectMapper();
		try {
			result = om.writeValueAsString(m);
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
