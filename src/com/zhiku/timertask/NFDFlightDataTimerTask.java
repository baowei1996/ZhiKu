package com.zhiku.timertask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;



import com.zhiku.file.FileDAO;
import com.zhiku.file.JFile;
import com.zhiku.util.PDF2png2;

public class NFDFlightDataTimerTask extends TimerTask{
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void run() {
        try {
             //定时执行的内容
            System.out.println("执行当前时间"+formatter.format(Calendar.getInstance().getTime()));
            ArrayList<JFile> filelist = (ArrayList<JFile>) FileDAO.getUntagPdf();
            PDF2png2.getPdfTags(filelist);
        } catch (Exception e) {
            System.out.println("-------------解析信息发生异常--------------");
            e.printStackTrace();
        }
    }
}
