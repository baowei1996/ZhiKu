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

import com.zhiku.file.JFile;
import com.zhiku.util.Data;
import com.zhiku.util.FileUpDownLoad;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 08-24-2017
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class FileUploadAction extends Action {
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
			
			//获取文件的相关属性
			int module = Integer.parseInt(request.getParameter("module"));
			int upuid = Integer.parseInt(request.getParameter("upuid"));
			int origin = Integer.parseInt(request.getParameter("origin"));
			String desc = request.getParameter("desc");
			
			FileUpDownLoad fileUpload = new FileUpDownLoad();
			Data data = fileUpload.upload(this.getServlet(), request);
			
			if((Integer)data.get("result") == FileUpDownLoad.SUCCESS){
				JFile file = new JFile();
				file.setName((String)data.get("filename"));
				file.setPath((String)data.get("savePath"));
				file.setModule(module);
				file.setUptime(new Date());
				file.setUpuid(upuid);
				file.setOrigin(origin);
				file.setDesc(desc);
				
				if(file.save()){
					rmsg.setStatus(200);
					rmsg.setMessage("OK");
				}else{
					rmsg.setStatus(300);
					rmsg.setMessage("fail ,please try again");
				}
			}else{
				rmsg.setStatus(300);
				rmsg.setMessage((String)data.get("message"));
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