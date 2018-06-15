package com.zhiku.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PythonExe {
	
	/**
	 * 给定一个py文件地址，执行该py文件
	 * @param py_filepath 要被执行的py文件
	 * @param arg	执行文件时的参数
	 * @throws InterruptedException	文件被中断时抛出
	 * @throws IOException	未能启动时抛出
	 */
	public static void Run_Python(String py_filepath,String arg) throws InterruptedException, IOException{
		
		Process ps = Runtime.getRuntime().exec("python3 " + py_filepath + " " + arg);
		ps.waitFor();
		
		return ;
	}

	
	@SuppressWarnings("unchecked")
	public static ArrayList<Data> getJson(String key,File file) throws Exception{
		FileInputStream in = new FileInputStream(file);
		byte[] buff = new byte[1024*50];	//设置50kb的buff
		int length = in.read(buff);
		in.close();
		String json = new String(buff, 0, length, "utf8");
		ObjectMapper om = new ObjectMapper();
		ArrayList<Data> d = om.readValue(json, ArrayList.class);
		return d;
	}
}
