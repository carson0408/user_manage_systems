package com.carson.cachedemo.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName CsvUtil
 *
 * @author zhanghangfeng5
 * @description
 * @Version V1.0
 * @createTime
 */
public class CsvUtil {
    public static boolean exportCsv(File file, List<String> dataList){
        boolean isSucess=false;

        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out, "UTF-8");//解决FileOutputStream中文乱码问题  解决MS office乱码问题
            bw =new BufferedWriter(osw);
            if(dataList!=null && !dataList.isEmpty()){
                for(String data : dataList){
                    bw.append(data).append("\r");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSucess;
    }

    /**
     * 导入
     * @author kpzc
     * @date 2018年12月29日 下午3:48:11
     * @param file  csv文件(路径+文件)
     * @return 返回List<String>列表
     */
    public static List<String> importCsv(File file){
        List<String> dataList=new ArrayList<String>();
        BufferedReader br=null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }
}

