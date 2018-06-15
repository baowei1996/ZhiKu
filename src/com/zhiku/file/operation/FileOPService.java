package com.zhiku.file.operation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.zhiku.hibernate.HibernateSessionFactory;
import com.zhiku.util.Data;

public class FileOPService {
	
	public static final int PAGE_SIZE = 5;
	
	public static void save(FileOP fp,Session session){
		session.persist(fp);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 根据uid和分页信息，返回对应的上传信息操作！
	 * @param uid	用户编号
	 * @param page	第几页
	 * @param type 查询的类型
	 * @return	返回对应页数的fileOP信息
	 */
	public static List<FileOPView> getOperation(int uid,int page,int type){
		List<FileOPView> fileOpList = null;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "from FileOPView where file_status = 1 and uid = " + uid + " and type = " + type +
					" and (fid,optime) in(select fid ,max(optime) from FileOPView where type = " + type + " group by fid)";
			Query q = session.createQuery(sql);
			q.setFirstResult((page-1)*PAGE_SIZE);
			q.setMaxResults(PAGE_SIZE);
			fileOpList = q.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return fileOpList;
	}
	
	/**
	 * 获取用户上传下载趋势
	 * @param uid 用户名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Data> getUpdownStatistics(int uid){
		List<Data> up_down_sts = new ArrayList<Data>();
		Session session =null;
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "select month(optime) month,sum(type = 0) upload,sum(type = 1) download from file_op_view where uid = "+ uid + " and year(optime) = year(now()) group by month(optime)";
			SQLQuery q = session.createSQLQuery(sql);
			List resultSet = q.list();
			Data d = null;
			for(int i = 0;i<resultSet.size();i++){
				d = new Data();
				Object[] ob = (Object[]) resultSet.get(i);
				d.put("month", ob[0]);
				d.put("upload",ob[1]);
				d.put("download", ob[2]);
				up_down_sts.add(d);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return up_down_sts;
	}

	/**
	 * 用户的下载课程分布
	 * @param uid 用户id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Data> getdownloadStatistics(int uid){
		List<Data> download_sts = new ArrayList<Data>();
		Session session = null;
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "select cname ,cnt/total*100 weight from (select cname ,sum(1) cnt from file_op_view where uid = " + uid + " and type = 1 group by cname) a,(select sum(1) total from file_op_view where uid = " + uid + " and type = 1) b;";
			SQLQuery q = session.createSQLQuery(sql);
			List resultSet = q.list();
			Data d = null;
			for(int i = 0;i<resultSet.size();i++){
				d = new Data();
				Object[] ob = (Object[])resultSet.get(i);
				d.put("item", ob[0]);
				d.put("count", ob[1]);
				download_sts.add(d);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return download_sts;
	}
	
	/**
	 * 用户上传和下载总量统计
	 * @param uid
	 * @return
	 */
	public static List<Data> getUpdownSum(int uid){
		List<Data> up_down_sum = new ArrayList<Data>();
		Session session = null;
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "select sum(type =0) upload,sum(type =1) download from file_op_view where uid = " + uid;
			SQLQuery q = session.createSQLQuery(sql);
			Object[] resultSet = (Object[])q.list().get(0);
			Data upload = new Data();
			upload.put("content", "上传总数");
			upload.put("value", resultSet[0]);
			Data download = new Data();
			download.put("content", "下载总数");
			download.put("value", resultSet[1]);
			up_down_sum.add(upload);
			up_down_sum.add(download);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return up_down_sum;
	}
}
