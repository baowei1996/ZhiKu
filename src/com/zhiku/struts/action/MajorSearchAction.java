/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.struts.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zhiku.util.Data;
import com.zhiku.util.RMessage;
import com.zhiku.xmc.Major;
import com.zhiku.xmc.XMCService;

/** 
 * MyEclipse Struts
 * Creation date: 09-01-2017
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class MajorSearchAction extends Action {
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
			int xid = Integer.parseInt(request.getParameter("xid"));
			
			List<Major> majors = XMCService.findMajorByXid(xid);
			
			if(majors != null){
				//如果结果不为空，将结果转换为Json
				Iterator<Major> it = majors.iterator();
				List<Data> data = new ArrayList<Data>();
				Major m = null;
				Data major = null;
				while(it.hasNext()){
					major = new Data();
					m = it.next();
					major.put("mid", m.getMid());
					major.put("mname", m.getMname());
					data.add(major);
				}
				
				rmsg.setStatus(200);
				rmsg.setMessage("OK");
				rmsg.setData(data);
			}else{
				rmsg.setStatus(300);
				rmsg.setMessage("No Result");
			}
			
			out = response.getWriter();
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