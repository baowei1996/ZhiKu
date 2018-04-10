/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.struts.action;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
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
		response.setHeader("Access-Control-Allow-Credentials", "true");
		
		//设置返回信息
		RMessage rmsg = new RMessage();
		PrintWriter out = null ;
		try {
			out = response.getWriter();
			
			//从request中获取相关信息
			String username = request.getParameter("username");
			
			//用户名长度不超过20
			//用户名只能是由字母数字下划线组成，不能以数字开头
			if(!(username != null && username.length() <20 && username.matches("[a-zA-Z_][a-zA-Z0-9_]*"))){
				rmsg.setStatus(300);
				rmsg.setMessage("用户名的格式不正确，请检查!");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			//如果用户名存在则返回
			if(User.isExist("usr", username)){
				rmsg.setStatus(300);
				rmsg.setMessage("用户名已经被占用，请重试!");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			
			String mail = request.getParameter("mail");
			
			//验证邮箱格式
			if(!mail.matches("\\w+@\\w+\\.\\w+")){
				rmsg.setStatus(300);
				rmsg.setMessage("邮箱格式错误，请重试!");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			
			if(User.isExist("mail", mail)){
				rmsg.setStatus(300);
				rmsg.setMessage("邮箱已经被占用，请重试!");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			
			String nickname = request.getParameter("nickname")==null?username:request.getParameter("nickname");
			String password = request.getParameter("password");
			//检查密码的合法性
			if(!(password!=null && password.length()>5 && password.length()<19)){
				rmsg.setStatus(300);
				rmsg.setMessage("密码不符合要求！要求密码长度6到18位。");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			//获取手机号并验证
			String phone = request.getParameter("phone")== null?"":request.getParameter("phone");
			if(!(phone.length() == 11 && phone.matches("\\d+"))){
				rmsg.setStatus(300);
				rmsg.setMessage("手机号格式错误!");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
//			//获取qq号并验证
//			String qq = request.getParameter("qq")== null?"":request.getParameter("qq");
//			if(!qq.matches("\\d+")){
//				qq = null;
//			}
			//如果xid和mid出错，默认为1
//			int xid;
//			int mid;
//			try {
//				xid = Integer.parseInt(request.getParameter("xid"));
//				mid = Integer.parseInt(request.getParameter("mid"));
//			} catch (NumberFormatException nfe) {
//				xid = 0;
//				mid = 0;
//				nfe.printStackTrace();
//			}
			String regip = request.getHeader("x-forwarded-for") == null? request.getRemoteAddr():request.getHeader("x-forwarded-for");
			
			String passwordMd5 = DigestUtils.md5Hex(password);
			
			//创建一个用户并设置信息
			User u = new User();
			u.setUsr(username);
			u.setNick(nickname);
			u.setPwd(passwordMd5);
			u.setMail(mail);
			u.setPhone(phone);
//			u.setQq(qq);
//			if(xid != 0){
//				u.setXid(xid);
//			}
//			if(mid != 0){
//				u.setMid(mid);
//			}
			u.setRegip(regip);
			Date current_time = new Date();
			u.setRegtime(current_time);
			//设置注册时间的后天为激活到期日
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(current_time);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			u.setMailtime(calendar.getTime());
			u.setStatus(User.UNACTIVE);
			
			if(u.save()){
				rmsg.setStatus(200);
				rmsg.setMessage("OK");
				//给用户的邮箱发送一个激活邮件,激活使用用户编号！
				EMail.sendMail("请激活你的邮箱","activate",u.getUsr(), u.getMail(), u.hashCode()+u.getMailtime().getTime()+"",
						"<a href = 'http://719daze.me:8080/JPidea/mail.do?act=reactivate&usr="+u.getUsr()+"'>重新发送激活邮件</a>");
			}else{
				rmsg.setStatus(300);
				rmsg.setMessage("抱歉!注册失败，请重试!");
			}
			out.write(RMessage.getJson(rmsg));
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
		
		
		
		return null;
	}
}