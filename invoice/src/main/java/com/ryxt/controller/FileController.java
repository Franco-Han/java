package com.ryxt.controller;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.OcrInfo;
import com.ryxt.service.DuplicateQueryService;
import com.ryxt.service.FileService;
import com.ryxt.service.OcrService;
import com.ryxt.util.*;
import io.github.yedaxia.apidocs.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/oss")
public class FileController {

    @Autowired
    private FileService fileService;


    @Autowired
    private OcrService ocrService;
	/**
	* 上传单个文件
	* @param aFile
    * @param servletRequest
	* @return com.ryxt.util.AjaxResponse<com.alibaba.fastjson.JSONObject>
	* @throws Exception
	*/

	@RequestMapping(value = "/upload")
	public AjaxResponse<JSONObject> image(@RequestParam(value="file",required=false) MultipartFile aFile, ServletRequest servletRequest) throws IOException {
        File file = null;
        // 获取文件名
        String fileName = aFile.getOriginalFilename();
        // 获取文件后缀
        String prefix=fileName.substring(fileName.lastIndexOf("."));

        // 用uuid作为文件名，防止生成的临时文件重复
        file = File.createTempFile(StringUtil.generateUUID(), prefix);

        aFile.transferTo(file);

        return AjaxResponse.success( fileService.upload(file));
	}


    @RequestMapping(value = "/upload2")
    public AjaxListResponse<Map<String, Object>> uploadPDF(@RequestParam(value="file",required=false) MultipartFile aFile, ServletRequest servletRequest) throws IOException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<File> files = new ArrayList<File>();
        // 获取文件名
        String fileName = aFile.getOriginalFilename();
        // 获取文件后缀
        String prefix=fileName.substring(fileName.lastIndexOf("."));
        if(".pdf".equals(prefix)||".PDF".equals(prefix)){
            try {
                files = PDF2ImageUtil.pdf2Image(aFile);
            } catch (Exception e) {
                return AjaxListResponse.fail("请上传正确的PDF文件",null);
            }
//            return  AjaxListResponse.fail("请上传PDF格式文件",null);
        }else{
            // 用uuid作为文件名，防止生成的临时文件重复
            File file = File.createTempFile(StringUtil.generateUUID(), prefix);
            aFile.transferTo(file);
            files.add(file);
        }
            for (File tempFile:files){
                // 用uuid作为文件名，防止生成的临时文件重复
//                File file = File.createTempFile(StringUtil.generateUUID(), prefix);
//                tempFile.transferTo(file);
                JSONObject jb = fileService.upload(tempFile);
                jb.put("tempFile",tempFile.getAbsolutePath());
                list.add(jb);
            }
        return AjaxListResponse.success(list);
    }


    @RequestMapping(value = "/uploadWithOcr")
    public AjaxListResponse<Map<String, Object>> uploadWithOcr(@RequestParam(value="file",required=false) MultipartFile aFile, ServletRequest servletRequest) throws IOException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<File> files = new ArrayList<File>();
        // 获取文件名
        String fileName = aFile.getOriginalFilename();
        // 获取文件后缀
        String prefix=fileName.substring(fileName.lastIndexOf("."));
        if(".pdf".equals(prefix)||".PDF".equals(prefix)){
            try {
                files = PDF2ImageUtil.pdf2Image(aFile);
            } catch (Exception e) {
                return AjaxListResponse.fail("请上传正确的PDF文件",null);
            }
        }else{
            // 用uuid作为文件名，防止生成的临时文件重复
            File file = File.createTempFile(StringUtil.generateUUID(), prefix);
            aFile.transferTo(file);
            files.add(file);
        }
        for (File tempFile:files){
            Map<String, Object> map = new HashMap<String, Object>();
            OcrInfo ocrInfo = ocrService.scanNoSave(tempFile.getPath());
            map.put("ocrInfo",ocrInfo);
            JSONObject jb = fileService.upload(tempFile);
            map.put("url",jb.get("url").toString());
            if(ocrInfo!=null){
                ocrInfo.setUrl(jb.get("url").toString());
                ocrService.saveOrUpdate(ocrInfo);
                map.put("ocrInfo",ocrInfo);
            }
            list.add(map);
        }
        return AjaxListResponse.success(list);
    }
	/**
	* 上传多个文件
	* @param files
	* @return com.ryxt.util.AjaxListResponse<java.util.Map<java.lang.String,java.lang.Object>>
	* @throws Exception
	*/

	@RequestMapping(value = "/uploads")
	public AjaxListResponse<Map<String, Object>> images( @RequestParam("files") MultipartFile[] files) throws IOException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if(files!=null&&files.length>0) {
            for(int i=0;i<files.length;i++){
                File file = null;
                // 获取文件名
                String fileName = files[i].getOriginalFilename();
                // 获取文件后缀
                String prefix=fileName.substring(fileName.lastIndexOf("."));
                // 用uuid作为文件名，防止生成的临时文件重复
                file = File.createTempFile(StringUtil.generateUUID(), prefix);
                files[i].transferTo(file);
                JSONObject jb = fileService.upload(file);
                list.add(jb);
            }
        }
        return AjaxListResponse.success(list);
	}

    @Ignore
    public static void main(String[] args) {
        //文件地址
        File file = new File("C:\\Users\\uenpeng\\Desktop\\测试图片\\17.txt");
        //声明参数集合
        HashMap<String, Object> paramMap = new HashMap<>();
        //文件
        paramMap.put("file", file);
        //输出
        paramMap.put("output","json");
        //自定义路径
        paramMap.put("path","image");
        //场景
        paramMap.put("scene","image");
        //上传
        String result= HttpUtil.post("http://114.116.102.81:8080/upload", paramMap);
        //输出json结果
        System.out.println(result);
    }
}
