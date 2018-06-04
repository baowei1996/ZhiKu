/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.struts.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zhiku.file.JFile;
import com.zhiku.util.Office2PDF;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 05-21-2018
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class GetFileDetailsAction extends Action {
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
			
			String url = request.getRequestURL().toString();
			int fid = -1;
			
			//给fid赋值
			try{
				fid = Integer.parseInt(url.substring(url.lastIndexOf("/")+1, url.lastIndexOf(".")));
			}catch(Exception e){
				fid = -1;
			}

			
			//检查文件
			JFile file = JFile.findByFid(fid);
			if(fid == -1 || file == null){
				response.setContentType("application/json;charset=utf-8");
				response.setHeader("pragme", "no-cache");
				rmsg.setStatus(300);
				rmsg.setMessage("文件出错，请重试!");
				out = response.getWriter();
				out.write(RMessage.getJson(rmsg));
				return null;
			}else{
				if(Office2PDF.isConvert(file.getPath())){
					String outputFilePath = Office2PDF.getOutputFilePath(file.getPath());
					if(Office2PDF.openOfficeToPDF(file.getPath(), outputFilePath)){
						
						Office2PDF.closeConnection();
						
						response.setContentType("application/pdf;charset=utf-8");
						response.setHeader("pragme", "no-cache");
						@SuppressWarnings("resource")
						FileInputStream in = new FileInputStream(new File(outputFilePath));
						OutputStream outer = response.getOutputStream();
						byte[] buffer = new byte[1024];
						int len = 0;
						while((len = in.read(buffer))>0){
							outer.write(buffer, 0, len);
						}
						return null;
					}
				}else{
					response.setContentType("application/json;charset=utf-8");
					response.setHeader("pragme", "no-cache");
					rmsg.setStatus(300);
					rmsg.setMessage("暂不支持本格式的预览！");
					out = response.getWriter();
					out.write(RMessage.getJson(rmsg));
					return null;
				}
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			response.setContentType("application/json;charset=utf-8");
			response.setHeader("pragme", "no-cache");
			rmsg.setStatus(300);
			rmsg.setMessage("发生了一个未知错误，请重试！\\n" + e.toString());
			try {
				out = response.getWriter();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			out.write(RMessage.getJson(rmsg));
		}finally{
			
		}
		return null;
	}
}