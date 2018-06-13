/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.zhiku.recommend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zhiku.file.operation.FileOPService;
import com.zhiku.token.Token;
import com.zhiku.user.User;
import com.zhiku.util.Data;
import com.zhiku.util.PythonExe;
import com.zhiku.util.RMessage;

/** 
 * MyEclipse Struts
 * Creation date: 06-12-2018
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class StatisticsAction extends Action {
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
			
			
			//token验证
			String token = request.getParameter("token");
			if(token != null){
				int status = Token.testToken(token);
				if(status == Token.OVERTIME){
					rmsg.setStatus(401);
					rmsg.setMessage("认证过期，请重新登录!");
					out.write(RMessage.getJson(rmsg));
					return null;
				}else if(status != Token.NORMAL){
					rmsg.setStatus(300);
					rmsg.setMessage("验证失败,请尝试重新登录!");
					out.write(RMessage.getJson(rmsg));
					return null;
				}
			}else{
				rmsg.setStatus(300);
				rmsg.setMessage("请先登录!");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			
			
			int uid = (Integer)Token.getPayload(token.substring(token.indexOf('.')+1, token.lastIndexOf('.'))).get("uid");
			
			//用户信息验证
			String username = request.getParameter("username");
			User u = User.findByUid(uid);
			if(u == null || !u.getUsr().equals(username)){
				rmsg.setStatus(300);
				rmsg.setMessage("用户不存在!");
				out.write(RMessage.getJson(rmsg));
				return null;
			}
			
			rmsg.setStatus(200);
			rmsg.setMessage("OK");
			
			try{
				PythonExe.Run_Python("D:\\zhiku\\pythonTest.py", "["+uid+"]");
			}catch(Exception e){
				e.printStackTrace();
				rmsg.setStatus(300);
				rmsg.setMessage("py文件运行失败！");
			}
			
			
			//读取py文件运行后生成的文件
			ArrayList<Data> graph3 = null;
			try{
				File words = new File("D:\\csdn.json");
				if(words.exists()){
					graph3 = PythonExe.getJson("files", words);
				}
			}catch(FileNotFoundException fnfe){
				fnfe.printStackTrace();
				rmsg.setStatus(300);
				rmsg.setMessage("文件获取失败！");
			}catch(StringIndexOutOfBoundsException sibe){	//文件为空时
				sibe.printStackTrace();
				graph3 = new ArrayList<Data>();
			}catch(Exception e){
				e.printStackTrace();
				rmsg.setStatus(300);
				rmsg.setMessage("json解析失败!");
			}
			
			Data data = new Data();
			data.put("form", FileOPService.getUpdownSum(uid));
			data.put("graph1", FileOPService.getUpdownStatistics(uid));
			data.put("graph2", FileOPService.getdownloadStatistics(uid));
			data.put("graph3", graph3);
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