/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.struts.action;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zhiku.user.User;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 08-22-2017
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class MailCheckAction extends Action {
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
		RMessage rmsg = new RMessage();
		PrintWriter out = null;
		
		try{
			out = response.getWriter();
			
			String username = request.getParameter("usr");
			int key = Integer.parseInt(request.getParameter("key"));
			User u = User.findByUsr(username);
			
			//如果用户不存在，或者用户的验证码不正确，则返回错误链接
			if(u == null || key != u.hashCode()){
				rmsg.setStatus(300);
				rmsg.setMessage("Invalid mailcheck link");
			}else {
				Date current = new Date();
				//如果激活时间过期，则返回注册过期
				if(current.compareTo(u.getMailtime()) < 0){
					rmsg.setStatus(300);
					rmsg.setMessage("MailCheck link time out");
				}else{
					rmsg.setStatus(200);
					rmsg.setMessage("OK");
				}
			}
			
			out.write(RMessage.getJson(rmsg));
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
		
		
		
		return null;
	}
}