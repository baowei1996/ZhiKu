package com.zhiku.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.codec.digest.DigestUtils;

import com.zhiku.user.User;


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
			fileinfo.put("time",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
			d.put("fileinfo", fileinfo);
			data.add(d);
		}
		
		message.setData(data);
		System.out.println(RMessage.getJson(message));
		System.out.println(DigestUtils.md5Hex("12345"));
//		try {
//			User u = new User();
//			u.setUsr("12345");
//			u.setMail("143256@qq.com");
//			EMail.sendMail("12345", "1368183370@qq.com", u.hashCode()+"");
//		} catch (AddressException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
