package com.zhiku.DB;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 * 
 * @author DBD
 *
 */
public class DB {

	private static String drivername = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/zhiku";
	private static String username = "root";
	private static String password = "root";
	
	static{
		try{
			Class.forName(drivername);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 用于建立数据库连接
	 * @return 返回一个Connection对象
	 */
	public static Connection getConnection(){
		Connection conn = null;
		
		try{
			conn = DriverManager.getConnection(url,username,password);
			System.out.println("connected!");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return conn;
	}
	
}
