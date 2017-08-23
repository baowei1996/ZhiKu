package com.zhiku.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EMail {
	
	/**
	 * 
	 * @param usr 发送给哪个用户
	 * @param to 用户的邮箱
	 * @param code 用户的激活码
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendMail(String usr ,String to,String code) throws AddressException, MessagingException{  
        /** 
         * 1.获得一个Session对象 
         * 2.创建一个代表邮件的对象 Message 
         * 3.发送邮件 Transport 
         */  
        //1.获得连接对象  
        Properties props = new Properties(); 
        //发送邮件服务器163
        props.put("mail.smtp.host", "smtp.163.com");
        //发送端口
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props, new Authenticator(){  
  
            @Override  
            protected PasswordAuthentication getPasswordAuthentication() {  
                //使用我的邮箱，和授权密码  
                return new PasswordAuthentication("m17864154913@163.com","forjavasake78647");  
            }  
              
        });  
        //2.创建邮件对象  
        Message message = new MimeMessage(session);  
        try{  
            //设置发件人  
            message.setFrom(new InternetAddress("m17864154913@163.com"));  
            //设置收件人  
            message.addRecipient(RecipientType.TO, new InternetAddress(to));  
            // 抄送 CC 暗送 BCC  
              
            //设置邮件主题  
            message.setSubject("来自吉鹏智库的激活邮件");  
            //设置邮件正文  
            message.setContent("<h1>吉鹏智库激活邮件！点下面连接完成激活操作</h1><h3><a href='http://localhost:8080/zhiku/mailcheck.action?usr="+usr+"&code="+code+"'>http://localhost:8080/zhiku/mailcheck.action?code="+code+"</a></h3>","text/html;charset=UTF-8");  
          
            //3.发送邮件  
            Transport.send(message);  
        }catch(AddressException e){  
            e.printStackTrace();  
        }catch(MessagingException e){  
            e.printStackTrace();  
        }  
                  
    }
}
