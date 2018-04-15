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

import com.zhiku.DB.Transaction;
import com.zhiku.file.JFile;
import com.zhiku.user.User;
import com.zhiku.util.FileUpDownLoad;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 09-03-2017
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class FileDownloadAction extends Action {
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
		PrintWriter out = null;
		
		RMessage rmsg = new RMessage();
		try{
			
			//获取用户的session，判断用户的登录状态
			HttpSession session = request.getSession();
			int uid = session.getAttribute("uid")==null?-1:(Integer)session.getAttribute("uid");
			User u = User.findByUid(uid);
			
			if (uid == -1 || u == null){
				rmsg.setStatus(300);
				rmsg.setMessage("需要登录才可下载文件!");
				response.setContentType("application/json;charset=utf-8");
				response.setHeader("pragme", "no-cache");
				response.setHeader("Access-Control-Allow-Credentials", "true");
				response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
				out = response.getWriter();
				out.write(RMessage.getJson(rmsg));;
				return null;
			}
			
			//检查fid是否正确
			String url = request.getRequestURL().toString();
			int fid = -1;
			try{
				//根据URL获取fid
				fid = Integer.parseInt(url.substring(url.lastIndexOf("/")+1, url.lastIndexOf(".")));
			}catch(NumberFormatException ne){
				fid = -1;
			}
			//根据fid找到对应文件的信息
			JFile f = JFile.findByFid(fid);
			if(fid == -1 || f == null || f.getStatus() != JFile.NORMAL){
				rmsg.setStatus(300);
				rmsg.setMessage("无法找到对应文件!");
				response.setContentType("application/json;charset=utf-8");
				response.setHeader("pragme", "no-cache");
				response.setHeader("Access-Control-Allow-Credentials", "true");
				out = response.getWriter();
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			
			FileUpDownLoad filedownload = new FileUpDownLoad();
			boolean ex = filedownload.download(this.getServlet(), request, response, f);
			

			
			//设置返回信息
			response.setContentType("application/json;charset=utf-8");
			response.setHeader("pragme", "no-cache");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			
			//文件不存在信息提示
			if(!ex){
				JFile.C_delete(fid);
				rmsg.setStatus(300);
				rmsg.setMessage("文件不存在！");
			}else{
				String opip = request.getHeader("x-forwarded-for") == null? request.getRemoteAddr():request.getHeader("x-forwarded-for");
				if(Transaction.saveDownloadInfo(f, u, opip)){
					rmsg.setStatus(200);
					rmsg.setMessage("OK");
				}else{
					rmsg.setStatus(300);
					rmsg.setMessage("发生一个预期以外的错误，请重试!");
				}
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