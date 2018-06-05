package com.zhiku.token;


import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import com.zhiku.util.Data;

public class Token {
	
	//JWT的header
	public static String Header = "{\"typ\": \"JWT\",\"alg\": \"HS256\"}";
	//密钥
	public static String SECRET_KEY = "sharing_zhiku";
	//base64编码工具
	public static Base64Encoder be = new Base64Encoder();
	//json格式化工具
	public static ObjectMapper om = new ObjectMapper();
	
	public static int NORMAL = 1; 	//表示token正常
	public static int UNEQUAL = 2;	//表示token信息验证不通过
	public static int OVERTIME = 3;	//表示token消息超时
	public static int UNEXCEPTED_ERROR = 4; //意料之外的异常 
	
	public static String getToken(Data message) throws Exception{
		String header = setHeader();
		String payload = setPayload(message);
		String signature = setSignature(header + "." + payload);
		return header + "." + payload + "." + signature;
		
	}

	/**
	 * 设置JWT的荷载payload，荷载中包含主要的信息
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public static String setPayload(Data payload) throws Exception{
		String base64_payload = om.writeValueAsString(payload);
		return be.encode(base64_payload.getBytes());
	}
	
	public static Data getPayload(String base64_payload) throws Exception{
		byte[] h = be.decode(base64_payload);
		return om.readValue(h, Data.class);
	}
	
	/**
	 * 设置header
	 * 获取已有的header，然后生成base64编码
	 * @return
	 */
	public static String setHeader(){
		return be.encode(Header.getBytes());
	}
	
	/**
	 * 解析header
	 * @param base64_Header	base64编码的header部分
	 * @return	返回解码后的header，以一个data的形式返回对应的json
	 * @throws Exception	在将json转变为data对象时可能出现异常
	 */
	public static Data getHeader(String base64_Header) throws Exception{
		byte[] h = be.decode(base64_Header);
		return om.readValue(h, Data.class);
	}
	
	/**
	 * 将header和payload使用HS256加密
	 * 然后对加密信息进行base64编码
	 * 生成JWT的签名
	 * @param message	header.payload
	 * @return	JWT的签名
	 * @throws Exception
	 */
	public static String setSignature(String message) throws Exception{
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	    SecretKeySpec secret_key = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
	    sha256_HMAC.init(secret_key);

	    String hash = be.encode(sha256_HMAC.doFinal(message.getBytes()));
	    return hash;
	}
	
	/**
	 * 验证token的正确性
	 * @param token	待验证token
	 * @return	token是否正确
	 */
	public static boolean testSign(String token){
		boolean equal = false;
		String fore_message = token.substring(0,token.lastIndexOf('.'));
		String sign = token.substring(token.lastIndexOf('.')+1);
		try{
			if(sign.equals(setSignature(fore_message))){
				equal = true;
			}else{
				equal = false;
			}
		}catch(Exception e){
			equal = false;
		}
		return equal;
	}
	
	/**
	 * 验证整个token，包括如下内容：
	 * 验证token的签名
	 * 验证token是否超期
	 * @param token
	 * @return
	 */
	public static int testToken(String token){
		String p_token = token.replaceAll(" ", "+");
		int status = 0;
		try{
			if(testSign(p_token)){
				String base64_payload = p_token.substring(p_token.indexOf('.')+1, p_token.lastIndexOf('.'));
				Data payload = getPayload(base64_payload);
				Date exp = new Date((Long)payload.get("exp"));
				Date current = new Date();
				if(current.compareTo(exp) > 0){
					status = OVERTIME;
				}else{
					status = NORMAL;
				}
			}else{
				status = UNEQUAL;
			}
		}catch(Exception e){
			status = UNEXCEPTED_ERROR;
			e.printStackTrace();
		}
		
		return status;
	}
	
	
	//测试一下整个类的方法
	public static void main(String[] arg){
		Data message = new Data();
		message.put("uid", 1);
		message.put("exp", new Date());
		try {
//			String token = getToken(message);
			String token = "eyJ0eXAiOiAiSldUIiwiYWxnIjogIkhTMjU2In0=.eyJ1aWQiOjIwMCwiZXhwIjoxNTI4MjA0MzU1Nzg1LCJpYXQiOjE1MjgyMDI1NTU3ODV9.zf9W4SElD9m8JthCg6LNyzYJk7SLSIf8/B/pvEJX+v8=";
			System.out.println(token);
			System.out.println(testSign(token));
			String base64_payload = token.substring(token.indexOf('.')+1, token.lastIndexOf('.'));
			System.out.println(base64_payload);
			Data payload = getPayload(base64_payload);
			System.out.println(payload.get("uid"));
			System.out.println(new Date((Long)payload.get("exp")));
			System.out.println(testToken(token));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
