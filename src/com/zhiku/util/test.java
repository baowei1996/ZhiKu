package com.zhiku.util;

import java.util.ArrayList;


public class test {
	public static void main(String[] args) {
		RMessage message = new RMessage(200,"OK");
		ArrayList<Data> data = new ArrayList<Data>();
		
		Data d = null;
		Data fileinfo = null;
		for(int i = 0; i<2 ;i++){
			d = new Data();
			d.put("fid", 0);
			d.put("upuid", 0);
			fileinfo = new Data();
			fileinfo.put("name", "bw");
			fileinfo.put("module", i);
			d.put("fileinfo", fileinfo);
			data.add(d);
		}
		
		message.setData(data);
		System.out.println(RMessage.getJson(message));
//		输出结果如下：
//		{
//			"status":200,
//			"message":"OK",
//			"data":
//			[
//				{"fid":0,
//				"upuid":0,
//				"fileinfo":
//					{
//					"module":1,
//					"name":"bw"
//					}
//				},
//				{"fid":0,
//				"upuid":0,
//				"fileinfo":
//					{
//					"module":2,
//					"name":"hp"
//					}
//				}
//			]
//		}
	}
}
