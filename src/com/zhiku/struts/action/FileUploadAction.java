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
import com.zhiku.file.operation.FileOP;
import com.zhiku.file.operation.FileOPService;
import com.zhiku.user.User;
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
			
			FileUpDownLoad fileUpload = new FileUpDownLoad();
			Data data = fileUpload.upload(this.getServlet(), request);
			
			
			if((Integer)data.get("result") == FileUpDownLoad.SUCCESS){
				JFile file = new JFile();
				
				//依据后缀名判断是否符合要求
				String fileExtName = (String)data.get("fileExtName");
				if(fileExtName.matches("doc(x)?")){
					file.setDocformat(JFile.TYPE_DOC);
				}else{
					if(fileExtName.matches("ppt(x)?")){
						file.setDocformat(JFile.TYPE_PPT);
					}else{
						if(fileExtName.matches("xls(x)?")){
							file.setDocformat(JFile.TYPE_XSL);
						}else{
							file.setDocformat(-1);
							rmsg.setStatus(300);
							rmsg.setMessage("sorry , just accept doc(x) or xsl(x) or ppt(x) now!");
							out.write(RMessage.getJson(rmsg));
							return null;
						}
					}
				}
				
				//获取文件的相关属性
//				int module = Integer.parseInt((String)data.get("module"));
				String username = (String)data.get("upusername");
				User u = User.findByUsr(username);
				if(u == null || u.getStatus() == User.LOCKED){
					rmsg.setStatus(300);
					rmsg.setMessage("user not find or locked!");
					out.write(RMessage.getJson(rmsg));
					return null;
				}
				int upuid = u.getUid();
				int origin = Integer.parseInt((String)data.get("origin"));
				String descs = (String)data.get("desc");
				String teacher = (String)data.get("teacher");
				int course = Integer.parseInt((String)data.get("course"));
				String sha256 = (String)data.get("sha256");
				
				file.setName((String)data.get("filename"));
				System.out.println((String)data.get("filename"));
				file.setPath((String)data.get("savePath"));
				file.setSha(sha256);
//				file.setModule(module);
				file.setCourse(course);
				file.setTeacher(teacher);
				file.setStatus(JFile.NORMAL);	//暂时统一规定文件为normal状态，之后添加验证时再修改
				file.setUptime(new Date());
				file.setUpuid(upuid);
				file.setOrigin(origin);
				file.setDescs(descs);
				
				
				file.setFileformat(fileExtName);
				
				if(file.save()){
					//设置文件的上传者上传量加一！
					u.setUpcnt(u.getUpcnt() + 1);
					u.modify();
					//记录上传信息
					FileOP fp = new FileOP();
					file = JFile.findBySha(sha256);
					System.out.println(file == null);
					fp.setFid(file.getFid());
					fp.setUid(u.getUid());
					fp.setOptime(new Date());
					String opip = request.getHeader("x-forwarded-for") == null? request.getRemoteAddr():request.getHeader("x-forwarded-for");
					fp.setOpip(opip);
					fp.setType(FileOP.UPLOAD);
					if(FileOPService.save(fp)){
						//设置返回信息
						rmsg.setStatus(200);
						rmsg.setMessage("OK");
					}
				}else{
					rmsg.setStatus(300);
					rmsg.setMessage("fail ,maybe some information wrong ! please check it!");
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