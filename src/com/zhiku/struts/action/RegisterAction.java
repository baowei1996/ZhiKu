/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zhiku.user.User;
import com.zhiku.util.EMail;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 08-21-2017
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class RegisterAction extends Action {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("pragme", "no-cache");
		
		//设置返回信息
		RMessage rmsg = new RMessage();
		PrintWriter out = null ;
		try {
			out = response.getWriter();
			
			//从request中获取相关信息
			String username = request.getParameter("username");
			
			//如果用户名存在则返回
			if(User.isExist("usr", username)){
				rmsg.setStatus(300);
				rmsg.setMessage("Username has been used");
				out.write(rmsg.getJson());
				return null;
			}
			
			String mail = request.getParameter("mail");
			
			if(User.isExist("mail", mail)){
				rmsg.setStatus(300);
				rmsg.setMessage("Mail has been used");
				out.write(rmsg.getJson());
				return null;
			}
			
			String nickname = request.getParameter("nickname");
			String password = request.getParameter("password");
			String phone = request.getParameter("phone");
			String qq = request.getParameter("qq");
			String regip = request.getHeader("x-forwarded-for") == null? request.getRemoteAddr():request.getHeader("x-forwarded-for");
			
			//创建一个用户并设置信息
			User u = new User();
			u.setUsr(username);
			u.setNick(nickname);
			u.setPwd(password);
			u.setMail(mail);
			u.setPhone(phone);
			u.setQq(qq);
			u.setRegip(regip);
			Date current_time = new Date();
			u.setRegtime(current_time);
			//设置注册时间的后天为激活到期日
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(current_time);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			u.setMailtime(calendar.getTime());
			u.setStatus(0);
			
			if(u.save()){
				rmsg.setStatus(200);
				rmsg.setMessage("OK");
				out.write(rmsg.getJson());
			}
			
			//给用户的邮箱发送一个激活邮件,激活使用用户编号！
			EMail.sendMail(u.getUsr(), u.getMail(), u.getUid()+"");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
		
		
		
		return null;
	}
}