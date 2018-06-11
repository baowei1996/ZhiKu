package com.zhiku.util;

import java.io.IOException;

public class PythonExe {
	
	public static void Exe_Python(String py_filepath) throws InterruptedException, IOException{
		
		Process ps = Runtime.getRuntime().exec("python " + py_filepath);
		ps.waitFor();
		
		return ;
	}

}
