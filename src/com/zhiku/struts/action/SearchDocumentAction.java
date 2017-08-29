/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.struts.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zhiku.file.JFile;
import com.zhiku.user.User;
import com.zhiku.util.Data;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 08-26-2017
 * 处理关键字搜索文件的action
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class SearchDocumentAction extends Action {
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
			
			int course = Integer.parseInt(request.getParameter("course"));
			List<JFile> filelist = JFile.findByCourse(course);
			
			Data[] data = new Data[filelist.size()];
			int index = 0;
			User u = null;
			for(JFile f : filelist){
				data[index].put("fid", f.getFid());
				data[index].put("upuid", f.getUpuid());
				
				Data fileinfo = new Data();
				fileinfo.put("name", f.getName());
				fileinfo.put("module", f.getModule());
				fileinfo.put("course", f.getCourse());
				fileinfo.put("docformat", f.getDocformat());
				fileinfo.put("origin", f.getOrigin());
				fileinfo.put("uptime", f.getUptime());
				fileinfo.put("desc", f.getDesc());
				data[index].put("fileinfo", fileinfo);
				
				u = User.findByUid(f.getUpuid());
				Data upperinfo = new Data();
				upperinfo.put("nickname", u.getNick());
				upperinfo.put("xid", u.getXid());
//				upperinfo.put("xname", value);
				upperinfo.put("mid", u.getMid());
//				upperinfo.put("mname", value);
				data[index].put("upperinfo", upperinfo);
				
				index++;
			}
			
			rmsg.setStatus(200);
			rmsg.setMessage("OK");
			rmsg.setData(data);
			
		}catch(Exception e){
			rmsg.setStatus(300);
			rmsg.setMessage("wrong , please try again");
			e.printStackTrace();
		}finally{
			out.write(RMessage.getJson(rmsg));
			out.flush();
			out.close();
		}
		return null;
	}
}