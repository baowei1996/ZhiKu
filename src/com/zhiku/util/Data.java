package com.zhiku.util;

import java.util.HashMap;

/**
 * 这个类中封装了返回信息的data部分，采用一个map将所有信息添加进来
 * @author DBD
 *
 */
@SuppressWarnings("serial")
public class Data  extends HashMap<String, Object>{

	//仅仅将hashmap封装成Data类，这样方便其他人使用，主要的就是一个put方法
	
	@Override
	public Object put(String key, Object value) {
		return super.put(key, value);
	}
	
}
