package com.zhiku.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个类中封装了返回信息的data部分，采用一个map将所有信息添加进来
 * @author DBD
 *
 */
public class Data {
	private Map<String, Object> data = new HashMap<String ,Object>();

	public Map<String, Object> getData() {
		return data;
	}
	
	public void put(String key , Object value){
		data.put(key, value);
	}
}
