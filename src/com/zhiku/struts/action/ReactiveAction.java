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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zhiku.user.User;
import com.zhiku.util.EMail;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 01-27-2018
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class ReactiveAction extends Action {
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
			
			String usr = request.getParameter("usr");
			User u = User.findByUsr(usr);
			
			//重置邮箱激活到期日
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			u.setMailtime(calendar.getTime());
			
			//重新发送激活邮件
			if(u.getStatus() == 0 && u.modify()){
				rmsg.setStatus(200);
				rmsg.setMessage("OK");
				u = User.findByUsr(usr);
				EMail.sendMail("请激活你的邮箱","activate",u.getUsr(), u.getMail(), u.hashCode()+u.getMailtime().getTime()+"",
						"<a href = 'http://719daze.me:8080/JPidea/mail.do?act=reactivate&usr="+u.getUsr()+"'>重新发送激活邮件</a>");
			}else{
				rmsg.setStatus(300);
				rmsg.setMessage("已激活或邮件发送失败!");
			}
			
			out.write(RMessage.getJson(rmsg));
		}catch(Exception e){
			rmsg.setStatus(300);
			rmsg.setMessage(e.getCause().toString());
			RMessage.getJson(rmsg);
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
}