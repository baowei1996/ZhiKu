package com.zhiku.file.operation;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.zhiku.hibernate.HibernateSessionFactory;

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
			String sql = "from FileOPView where file_status = 1 and uid = " + uid + " and type = " + type + " order by fid";
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
	
}
