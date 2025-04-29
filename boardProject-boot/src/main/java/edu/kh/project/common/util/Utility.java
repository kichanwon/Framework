package edu.kh.project.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static int seqNum = 1;

    public static String fileRename(String originFileName){
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss_");
        String date = sdf.format(new Date());

        String number = String.format("%05d", seqNum++);
        if(seqNum > 99999){
            seqNum = 1;
        }

        String ext = originFileName.substring(originFileName.lastIndexOf("."));

        return date + number + ext;
    }
}
