/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.struts.action;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zhiku.user.User;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 09-03-2017
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class LogoutAction extends Action {
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
		PrintWriter out = null;
		
		//获取当前的ip和时间，并设置
		String lastip = request.getHeader("x-forwarded-for") == null? request.getRemoteAddr():request.getHeader("x-forwarded-for");
		Date lasttime = new Date();
		RMessage rmsg = new RMessage();
		try{
			out = response.getWriter();
			
			HttpSession session = request.getSession();
			int uid = session.getAttribute("uid")==null?-1:(Integer)session.getAttribute("uid");
			if(uid == -1){
				rmsg.setStatus(300);
				rmsg.setMessage("请先登录!");
				out.write(RMessage.getJson(rmsg));;
				return null;
			}
			User u = User.findByUid(uid);
			if(u == null){
				rmsg.setStatus(300);
				rmsg.setMessage("未知用户!");
				out.write(RMessage.getJson(rmsg));;
				return null;
			}
			
			//删除cookie的内容
			Cookie newCookie = new Cookie("username",null);
			newCookie.setMaxAge(0);
			//清除session中的所有信息
			session.invalidate();
			
			u.setLastip(lastip);
			u.setLasttime(lasttime);
			u.modify();
			
			rmsg.setStatus(200);
			rmsg.setMessage("OK");
			
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