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

import com.zhiku.activity.Activity;
import com.zhiku.activity.ActivityService;
import com.zhiku.util.Data;
import com.zhiku.util.FileUpDownLoad;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 03-20-2018
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class AddNewsAction extends Action {
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
			
			/*
			 * 一段管理员登录的检验
			 */
			
			FileUpDownLoad fileUpload = new FileUpDownLoad();
			Data data = fileUpload.upload(this.getServlet(), request,FileUpDownLoad.IMAGE_UPLOAD_PATH);
			
			System.out.println(data.get("result"));
			if((Integer)data.get("result") == FileUpDownLoad.SUCCESS){
				//获取图片的相关属性
				String url = (String)data.get("savePath");
				String title = (String)data.get("title");
				String details = (String)data.get("details");
//				String date = (String)data.get("date");
				
				Activity act = new Activity();
				act.setImg(url);
				act.setPubtime(new Date());
				act.setTitle(title);
				act.setDescs(details);

				if(ActivityService.addNews(act)){
					rmsg.setStatus(200);
					rmsg.setMessage("OK");
				}else{
					rmsg.setStatus(300);
					rmsg.setMessage("发生了一个预期以外的错误，请重试!");
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