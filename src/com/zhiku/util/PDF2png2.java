package com.zhiku.util;

//2.0
/** 
* @author ���� E-mail: yanjiaxin8410@163.com
* @version ����ʱ�䣺2018��5��18�� ����8:57:07 
* ��˵�� 
*/

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.zhiku.file.JFile;

public class PDF2png2 {
	
	public void WriteStringToFile(String filePath) {  
        
    }  
	

	// ת��ȫ����pdf
	public static void pdf2png(String fileAddress, String filename) {
		// ��pdfװͼƬ �����Զ���ͼƬ�ø�ʽ��С
		File file = new File(fileAddress + "/" + filename + ".pdf");
		try {
			PDDocument doc = PDDocument.load(file);
			PDFRenderer renderer = new PDFRenderer(doc);
			int pageCount = doc.getNumberOfPages();
			for (int i = 0; i < pageCount; i++) {
				BufferedImage image = renderer.renderImageWithDPI(i, 144); // Windows native DPI
				// BufferedImage srcImage = resize(image, 240, 240);//��������ͼ
				ImageIO.write(image, "PNG", new File(fileAddress + "/" + filename + "_" + (i + 1) + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getPdfTags(ArrayList<JFile> data) {

		String fileAddress = null;
		String filename = null;
		int fid = 0;
		for (int i = 0; i < data.size(); i++) {
			int index = data.get(i).getPath().lastIndexOf("/");
			fileAddress = data.get(i).getPath().substring(0, index + 1);
			filename = data.get(i).getName().replaceAll(".pdf", "");
			fid = data.get(i).getFid();

			// System.out.println(data[i][0]);
			// System.out.println(data[i][1]);
			
			pdf2png(fileAddress, filename);
			
			//运行py
			//参数["fileaddress","filename"]
			try {
	             //定时执行的内容
				String relativelyPath=System.getProperty("user.dir"); 
				relativelyPath = relativelyPath.replaceAll("/bin", "/webapps/JPidea/py/get_pdf_tags.py");
	            PythonExe.Run_Python(relativelyPath,"[" + fid + "," + fileAddress + "," + filename +"]");
	        } catch (Exception e) {
	            System.out.println("-------------解析信息发生异常--------------");
	            e.printStackTrace();
	        }
			
		}

		

	}

}