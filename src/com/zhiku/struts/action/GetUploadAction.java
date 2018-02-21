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
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zhiku.file.FileView;
import com.zhiku.file.operation.FileOP;
import com.zhiku.file.operation.FileOPService;
import com.zhiku.file.operation.FileOPView;
import com.zhiku.user.User;
import com.zhiku.util.Data;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 09-10-2017
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class GetUploadAction extends Action {
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
			
			//获取用户的session，判断用户的登录状态
			HttpSession session = request.getSession();
			int uid = session.getAttribute("uid")==null?-1:(Integer)session.getAttribute("uid");
			
			if (uid == -1){
				rmsg.setStatus(300);
				rmsg.setMessage("请先登录!");
				out.write(RMessage.getJson(rmsg));;
				return null;
			}
			
			String username = request.getParameter("username");
			User u = User.findByUsr(username);
			if(u == null || u.getUid() != uid){
				rmsg.setStatus(300);
				rmsg.setMessage("用户不存在!");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			
			int page ;
			try{
				page = Integer.parseInt(request.getParameter("page"));
			}catch(NumberFormatException nfe){
				page = 1;
				nfe.printStackTrace();
			}
			
			List<FileOPView> opList = FileOPService.getOperation(u.getUid(), page, FileOP.UPLOAD);
			
			if (opList != null){
				//设置返回的data信息
				List<Data> data = new ArrayList<Data>();
				FileView f = null;
				Data d = null;
				for(FileOPView op : opList){
					f = FileView.findByFid(op.getFid());
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
				rmsg.setData(data);
			}
			
			rmsg.setStatus(200);
			rmsg.setMessage("OK");
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