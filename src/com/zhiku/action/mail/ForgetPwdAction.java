/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.action.mail;

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
 * Creation date: 04-06-2018
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class ForgetPwdAction extends Action {
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
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		PrintWriter out = null;
		
		RMessage rmsg = new RMessage();
		try{
			out = response.getWriter();
			//根据用户的邮箱找到对应用户
			String mail = request.getParameter("mail");
			System.out.println(mail);
			System.out.println(request.toString());
			User u = User.findByMail(mail);
			
			if(u == null){
				rmsg.setStatus(300);
				rmsg.setMessage("邮箱不存在，可注册!");
			}else{
				Date current_time = new Date();
				//设置修改密码的结束时间修改为一天之内
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(current_time);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				u.setMailtime(calendar.getTime());
				
				if(u.modify()){
					rmsg.setStatus(200);
					rmsg.setMessage("OK");
					u = User.findByMail(mail);
					//给用户的邮箱发送一个激活邮件,激活使用用户编号！
					EMail.sendMail("你的用户名是"+u.getUsr()+",请点击下面的链接找回你的密码","find_password",u.getUsr(), u.getMail(), u.hashCode()+u.getMailtime().getTime()+"","");
				}else{
					rmsg.setStatus(300);
					rmsg.setMessage("找回密码出错，请重试");
				}
			}
			out.write(RMessage.getJson(rmsg));
			
		}catch(Exception e){
			rmsg.setStatus(300);
			rmsg.setMessage(e.getCause().toString());
			out.write(RMessage.getJson(rmsg));
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
}