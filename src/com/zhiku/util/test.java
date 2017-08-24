package com.zhiku.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class test {
	public static void main(String[] args) {
		RMessage message = new RMessage(200,"OK");
		Data data = new Data();
		data.put("usr", "bw");
		data.put("pwd", "asdfghjkl");
		message.setData(data.getData());
		
		ObjectMapper om = new ObjectMapper();
		try {
			String result = om.writeValueAsString(message);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}
}
