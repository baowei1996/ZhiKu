package com.zhiku.util;

import java.util.ArrayList;


public class test {
	public static void main(String[] args) {
		RMessage message = new RMessage(200,"OK");
		ArrayList<Data> data = new ArrayList<Data>();
		
		Data data1 = new Data();
		data1.put("fid", 0);
		data1.put("upuid", 0);
		Data fileinfo = new Data();
		fileinfo.put("name", "bw");
		fileinfo.put("module", 1);
		data1.put("fileinfo", fileinfo);
		
		Data data2 = new Data();
		data2.put("fid", 0);
		data2.put("upuid", 0);
		Data fileinfo2 = new Data();
		fileinfo2.put("name", "hp");
		fileinfo2.put("module", 2);
		data2.put("fileinfo", fileinfo2);
		
		data.add(data1);
		data.add(data2);
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
