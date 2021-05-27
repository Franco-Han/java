package com.ryxt.util;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.service.FileService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static JSONObject DownloadUrl(Set<String> urlArr, String fileName) throws Exception {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        ZipOutputStream zos = null;
        ZipEntry zipEntry = null;
        int len;
        byte[] bytes = new byte[1024*20];
        try {
            File zipFile = File.createTempFile(fileName, ".zip");
            if(!zipFile.exists()){
                zipFile.createNewFile();
            }
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));

                for(String i : urlArr){
                    if(i.startsWith("http://") ||i.startsWith("https://")){
                        // 创建URL
                        URL url = new URL(i);
                        // 创建链接
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        InputStream inputStream = conn.getInputStream();
                        //创建ZIP实体，并添加进压缩包
                        zipEntry = new ZipEntry(i.substring(i.lastIndexOf("/")+1));
                        zos.putNextEntry(zipEntry);
                        //读取待压缩的文件并写进压缩包里
                        bis = new BufferedInputStream(inputStream, 1024*10);
                        // 将内容读取内存中
                        while ((len = bis.read(bytes, 0, 1024*10))>0){
                            zos.write(bytes, 0, len);
                        }
                    }else {
                        File readfile = new File(i);
                        if(!readfile.isDirectory()){
                        //创建ZIP实体，并添加进压缩包
                        zipEntry = new ZipEntry(readfile.getName());
                        zos.putNextEntry(zipEntry);
                        //读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(readfile);
                        bis = new BufferedInputStream(fis, 1024*10);
                        while ((len = bis.read(bytes, 0, 1024*10))>0){
                            zos.write(bytes, 0, len);
                        }
                        fis.close();
                    }
                }

            }
            zos.close();
            FileService fileService  = (FileService) SpringContextUtil.getBean("fileServiceImpl");
            JSONObject jb = fileService.upload(zipFile);
            return jb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
