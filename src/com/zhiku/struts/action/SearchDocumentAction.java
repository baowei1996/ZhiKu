/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.struts.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zhiku.file.FileView;
import com.zhiku.util.Data;
import com.zhiku.util.RMessage;
import com.zhiku.xmc.XMCService;

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
	public static final int COURSE_SEARCH = 1;
	public static final int MAJOR_SEARCH = 2;

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
			int method, mid=0,cid=0,page;
			//设置method
			try {
				method = Integer.parseInt(request.getParameter("method"));
			} catch (NumberFormatException nfe) {
				method = 1;
				nfe.printStackTrace();
			}
			//设置cid和mid
			if(method == COURSE_SEARCH){
				try {
					cid = Integer.parseInt(request.getParameter("course"));
				} catch (NumberFormatException nfe) {
					cid = -1;
					nfe.printStackTrace();
				}
			}else if(method == MAJOR_SEARCH){
				try {
					mid = Integer.parseInt(request.getParameter("major"));
				} catch (NumberFormatException nfe) {
					mid = -1;
					nfe.printStackTrace();
				}
			}
			
			//设置page的值
			try {
				page = request.getParameter("page")==null?1:Integer.parseInt(request.getParameter("page"));
			} catch (NumberFormatException nfe) {
				page = 1;
				nfe.printStackTrace();
			}
			
			
			if(method == MAJOR_SEARCH && !XMCService.isExist("Major", "mid", mid)){	//如果不存在则返回非法专业
				rmsg.setStatus(300);
				rmsg.setMessage("Invalid major");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			
			if(method == COURSE_SEARCH && !XMCService.isExist("Course", "cid", cid)){	//如果课程不存在则返回非法课程
				rmsg.setStatus(300);
				rmsg.setMessage("Invalid course");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			
			List<FileView> filelist = null;
			
			//根据不同的搜索方式，给filelist赋值
			if(method == COURSE_SEARCH){	//关键字搜索
				filelist = FileView.findByCid(cid,page);
			}else if(method == MAJOR_SEARCH){  	//学院专业搜索
				filelist = FileView.findByCids(XMCService.findCoursesInMtoc(mid),page);
			}
			
			
			//设置返回的data信息
			List<Data> data = new ArrayList<Data>();
			Data d = null;
			for(FileView f : filelist){
				d = new Data();
				d.put("fid", f.getFid());
				d.put("upuid", f.getUid());
				
				Data fileinfo = new Data();
				fileinfo.put("name", f.getName());
				fileinfo.put("module", f.getModule());
				fileinfo.put("course", f.getCname());
				fileinfo.put("docformat", f.getDocformat());
				fileinfo.put("origin", f.getOrigin());
				fileinfo.put("uptime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(f.getUptime()));
				fileinfo.put("desc", f.getDescs());
				fileinfo.put("dncnt", f.getDncnt());
				d.put("fileinfo", fileinfo);
				
				Data upperinfo = new Data();
				upperinfo.put("nickname", f.getNick());
				upperinfo.put("xname", f.getXname());
				upperinfo.put("mname", f.getMname());
				d.put("upperinfo", upperinfo);
				
				data.add(d);
			}
			
			rmsg.setStatus(200);
			rmsg.setMessage("OK");
			rmsg.setData(data);
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