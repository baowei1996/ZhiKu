package com.zhiku.util;

import java.io.File;  
import java.net.ConnectException;
//import java.util.Date;  
import java.util.regex.Pattern;  
  





//import org.artofsolving.jodconverter.OfficeDocumentConverter;  
//import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;  
//import org.artofsolving.jodconverter.office.OfficeManager;  
import com.artofsolving.jodconverter.*;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
  
/** 
 * 这是一个工具类，主要是为了使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 
 * 转化为pdf文件<br> 
 * Office2010的没测试<br> 
 *  
 * @date 2012-11-5 
 * @author xhw 
 *  
 */  
public class Office2PDF {  
	
	//可以转化的格式类型
	public static String MATCHTYPE = "docx|doc|xlsx|xls|pptx|ppt|txt|pdf";
	
	//不同系统下openoffice的安装路径
	public static String WINDOWS_PATH = "C:\\Program Files (x86)\\OpenOffice 4\\program\\soffice.exe";
	public static String LINUX_PATH = "";
	public static String MAC_PATH = "";

	
	//openoffice的一个进程
	public static Process p = null;
	//openoffice的一个连接
	public static OpenOfficeConnection connection = null;
  
    /** 
     * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件<br> 
     *  
     * @param inputFilePath 
     *            源文件路径，如："e:/test.docx" 
     * @param outputFilePath 
     *            目标文件路径，如："e:/test_docx.pdf" 
     * @return 
     * @throws ConnectException 
     */  
    public static boolean openOfficeToPDF(String inputFilePath, String outputFilePath) throws ConnectException {  
        return office2pdf(inputFilePath, outputFilePath);  
    }  
  
    /** 
     * 根据操作系统的名称，获取OpenOffice的安装目录<br> 
     * 如我的OpenOffice安装在：C:/Program Files (x86)/OpenOffice 4/program<br> 
     *  
     * @return OpenOffice的安装目录 
     */  
    public static String getOfficeHome() {  
        String osName = System.getProperty("os.name");  
        if (Pattern.matches("Linux.*", osName)) {  
            return LINUX_PATH;  
        } else if (Pattern.matches("Windows.*", osName)) {  
            return WINDOWS_PATH;  
        } else if (Pattern.matches("Mac.*", osName)) {  
            return MAC_PATH;  
        }  
        return null;  
    }  
    
    
    /**
     * 启动openoffice服务，并使用8100端口
     * @return
     */
    public static boolean startOpenOffice() {
    	boolean start = false;
    	
    	Runtime rt = Runtime.getRuntime();  
    	
        try  
        {  
            //执行的文件的位置
        	//启动命令soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
        	String[] instruction = new String[]{getOfficeHome(), "-headless", "-accept=\"socket,host=127.0.0.1,port=8100;urp;\"", "-nofirststartwizard"};
            p = rt.exec(instruction);
            start = true;
        }catch (Exception e) {
        	start = false;
            e.printStackTrace();              
        }  
    	
    	return start;
    }
  
    /**
     * 判断是否是默认可以转pdf的文件后缀
     * @param inputFilePath 文件路径
     * @return 满足格式的进行转化
     */
    public static boolean isConvert(String inputFilePath){
    	boolean can = false;
    	String type = getPostfix(inputFilePath);
    	if(type.matches(MATCHTYPE)){
    		can = true;
    	}
    	return can;
    }
    
    
    /**
     * 获取连接  
     * @return 返回一个soffice的连接
     * @throws ConnectException 
     */
    public static OpenOfficeConnection getConnection() throws ConnectException {  
    	if(p == null){
    		startOpenOffice();
    	}
    	
    	if (connection == null){
    		connection = new SocketOpenOfficeConnection("127.0.0.1",8100);
    	}else{
    		if(!connection.isConnected()){
    			connection.connect();
    		}
    	}
        return connection;  
    }  
    
    
    
    /**
     * 关闭连接
     * @return 是否关闭连接
     */
    public static boolean closeConnection(){
    	if(connection != null && connection.isConnected()){
    		connection.disconnect();
    		return true;
    	}else{
    		return false;
    	}
    }
    
    
    /** 
     * 获取输出文件 
     *  
     * @param inputFilePath 
     * @return 
     */  
    public static String getOutputFilePath(String inputFilePath) {  
    	String outputFilePath = inputFilePath.replaceAll("." + getPostfix(inputFilePath), ".pdf");  
    	return outputFilePath;  
    }  
    
    
    /** 
     * 获取inputFilePath的后缀名，如："e:/test.pptx"的后缀名为："pptx"<br> 
     *  
     * @param inputFilePath 
     * @return 
     */  
    public static String getPostfix(String inputFilePath) {  
    	return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);  
    }  
  
    
    /** 
     * 转换文件 
     *  
     * @param inputFile 
     * @param outputFilePath_end 
     * @param inputFilePath 
     * @param outputFilePath 
     * @param converter 
     */  
    
    public static void converterFile(File inputFile, String outputFilePath_end, String inputFilePath, String outputFilePath, DocumentConverter converter) {  
        File outputFile = new File(outputFilePath_end);  
        // 假如目标路径不存在,则新建该路径  
        if (!outputFile.getParentFile().exists()) {  
            outputFile.getParentFile().mkdirs();  
        }  
        //如果文件已存在，则不进行转化
        if(!outputFile.exists()){
        	converter.convert(inputFile, outputFile);  
        }
    }  
  
    /** 
     * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件<br> 
     *  
     * @param inputFilePath 
     *            源文件路径，如："e:/test.docx" 
     * @param outputFilePath 
     *            目标文件路径，如："e:/test_docx.pdf" 
     * @return 
     * @throws ConnectException 
     */  
    public static boolean office2pdf(String inputFilePath, String outputFilePath) throws ConnectException {  
        boolean flag = false;  
        // 连接OpenOffice  
        DocumentConverter converter = new  OpenOfficeDocumentConverter(getConnection());
//        long begin_time = new Date().getTime();
        if (null != inputFilePath) {  
            File inputFile = new File(inputFilePath);  
            if (inputFile.exists()) {// 找不到源文件, 则返回  
            	converterFile(inputFile, outputFilePath, inputFilePath, outputFilePath, converter);  
            	flag = true;  
            }  
        } else {  
            System.out.println("con't find the resource");  
        }  
//        long end_time = new Date().getTime();  
//        System.out.println("文件转换耗时：[" + (end_time - begin_time) + "]ms");  
        return flag;  
    }  
  
  
    public static void main(String[] args) throws ConnectException {
    	String inputPath = "E:"+File.separator + "config1.pdf";
		if(isConvert(inputPath)){
			openOfficeToPDF("E:"+File.separator + "config1.doc", getOutputFilePath(inputPath));
		}else{
			System.out.println("不支持预览");
		}
	}
    
} 
