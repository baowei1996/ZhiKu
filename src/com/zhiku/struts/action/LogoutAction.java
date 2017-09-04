/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.struts.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
		PrintWriter out = null;
		
		RMessage rmsg = new RMessage();
		try{
			out = response.getWriter();
			
			HttpSession session = request.getSession();
			//清楚session中的所有信息
			session.invalidate();
			
			rmsg.setStatus(200);
			rmsg.setMessage("OK");
			
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