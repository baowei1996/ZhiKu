package com.zhiku.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhiku.token.Token;
import com.zhiku.user.User;


public class test {
	public static void main(String[] args) throws Exception {
		
		FileInputStream in = new FileInputStream(new File("D:\\csdn推荐信息.json"));
		byte[] buff = new byte[1024*20];
		int length = in.read(buff);
		String blog = new String(buff, 0, length);
		ObjectMapper om = new ObjectMapper();
		ArrayList<Data> d = om.readValue(blog, ArrayList.class);
		Data data = new Data();
		data.put("blog", d);
		RMessage rmsg = new RMessage();
		rmsg.setData(data);
		rmsg.setStatus(200);
		rmsg.setMessage("ok");
		System.out.println(RMessage.getJson(rmsg));
		
		//sha256只针对内容进行哈希，所有文件名的修改不影响sha256的值
//		FileInputStream fi1 = new FileInputStream(new File("E:") + File.separator + "config.txt");
//		FileInputStream fi2 = new FileInputStream(new File("E:") + File.separator + "config1.doc");
//		
		
		
//		System.out.println(DigestUtils.sha256Hex(fi1));
//		System.out.println(DigestUtils.sha256Hex(fi2));
		
//		RMessage message = new RMessage(200,"OK");
//		ArrayList<Data> data = new ArrayList<Data>();
//		
//		Data d = null;
//		Data fileinfo = null;
//		for(int i = 0; i<2 ;i++){
//			d = new Data();
//			d.put("fid", 0);
//			d.put("upuid", 0);
//			fileinfo = new Data();
//			fileinfo.put("name", "bw");
//			fileinfo.put("module", i);
//			fileinfo.put("time",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
//			d.put("fileinfo", fileinfo);
//			data.add(d);
//		}
//		
//		message.setData(data);
//		System.out.println(RMessage.getJson(message));
//		System.out.println(DigestUtils.md5Hex("12345"));
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
