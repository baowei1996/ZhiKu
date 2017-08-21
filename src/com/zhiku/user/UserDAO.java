package com.zhiku.user;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhiku.DB.DB;

public class UserDAO {
	/**
	 * 保存用户的注册信息
	 * @param u 用户
	 * @return 是否注册成功
	 */
	public boolean save(User u){
		boolean isDone = true;
		Connection conn = DB.getConnection();
		
		String sql = "insert into user(usr,nick,pwd,phone,mail,regip,regtime,mailtime,status) values(?,?,?,?,?,?,?,?)";
		try {
			//设置用户的基本信息
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, u.getUsr());
			pstm.setString(2, u.getNick());
			pstm.setString(3, u.getPwd());
			pstm.setString(4, u.getPhone());
			pstm.setString(5, u.getMail());
			pstm.setString(6, u.getRegip());
			pstm.setDate(7, new Date(u.getRegtime().getTime()));
			pstm.setDate(8, new Date(u.getMailtime().getTime()));
			pstm.setInt(9, u.getStatus());
			pstm.executeQuery();
		} catch (SQLException e) {
			isDone = false;
			e.printStackTrace();
		}
		
		return isDone;
	}
	
	
	/**
	 * 检查用户名和密码是否正确
	 * @param usr 用户名
	 * @param pwd 密码
	 * @return 是否正确
	 */
	public boolean check(String usr , String pwd){
		
		boolean isCorrect = true;
		Connection conn = DB.getConnection();
		
		String sql = "select usr , pwd from user where usr = \'" + usr +"\'";
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			
			String username = rs.getString("usr");
			String password = rs.getString("pwd");
			
			if(!username.equals(usr) || !password.equals(pwd)){
				isCorrect = false;
			}
		} catch (SQLException e) {
			isCorrect = false;
			e.printStackTrace();
		}
		
		return isCorrect;
	}
	
	public boolean modify(){
		//do something
		return true;
	}
}
