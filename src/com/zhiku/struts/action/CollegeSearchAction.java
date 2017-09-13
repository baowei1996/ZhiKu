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
import com.zhiku.xmc.College;
import com.zhiku.xmc.XMCService;

/** 
 * MyEclipse Struts
 * Creation date: 09-01-2017
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class CollegeSearchAction extends Action {
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
			//获得所有的学院
			List<College> collegelist = XMCService.getAllCollege();
			//将学院的信息添加到data中
			Iterator<College> it = collegelist.iterator();
			List<Data> data = new ArrayList<Data>();
			College c = null;
			Data d = null;
			while(it.hasNext()){
				d = new Data();
				c = it.next();
				d.put("xid", c.getXid());
				d.put("xname", c.getXname());
				data.add(d);
			}
			
			//设置rmsg的信息
			rmsg.setStatus(200);
			rmsg.setMessage("OK");
			rmsg.setData(data);
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