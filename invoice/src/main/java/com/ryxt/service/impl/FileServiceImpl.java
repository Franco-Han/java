package com.ryxt.service.impl;


import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.SysDict;
import com.ryxt.exception.BusinessException;
import com.ryxt.mapper.DictMapper;
import com.ryxt.service.DictService;
import com.ryxt.service.FileService;
import com.ryxt.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author uenpeng
 */
@Service
public class FileServiceImpl implements FileService {
	@Value("${upload.service.url}")
	private String UPLOAD_PATH;
	@Value("${upload.service.gate_path}")
	private String GATE_PATH;
	@Value("${fileserver.domain}")
	private String DOMAIN;

	private OBSHandler obsHandler;
	/**
	 * 华为云的 Access Key Id
	 */

	@Value("${huaweiOBS.AccessKeyId}")
	private String accessKeyId;
	/**
	 * 华为云的 Access Key Secret
	 */
	@Value("${huaweiOBS.SecretAccessKey}")
	private String accessKeySecret;
	/**
	 * 格式如 obs.cn-north-1.myhuaweicloud.com
	 */
	@Value("${huaweiOBS.obsEndpoint}")
	private String obsEndpoint;
	/**
	 *obs桶名
	 */
	@Value("${huaweiOBS.Bucket}")
	private String bucketName;

	/**
	 * 获取连接
	 * @return
	 */
	public OBSHandler getObsHander() {
		if(obsHandler == null) {
			obsHandler = new OBSHandler(accessKeyId,accessKeySecret,obsEndpoint);
			// 如果设置过CDN的路径测设置为CDN路径，没有设置则为桶原生的访问路径
			//obsHandler.setUrlForCDN(Global.get("ATTACHMENT_FILE_URL"));
			// 在数据库中读取进行操作的桶的明恒
			obsHandler.setObsBucketName(bucketName);
			// 对桶名称进行当前类内缓存
			bucketName = obsHandler.getObsBucketName();
		}
		return obsHandler;
	}
	@Override
	public JSONObject upload(File file) throws IOException {
		if(StringUtil.isNotNullEmpty(accessKeyId)){
			try{
				String fileName = file.getName();
				getObsHander().putLocalFile(bucketName, fileName, file);
				getObsHander().setObjectAclPubilcRead(fileName);
				String url =  getObsHander().signatureUrl(fileName);
				url = url.substring(0,url.indexOf("?AccessKeyId"));
				JSONObject jb = new JSONObject();
//				jb.put("url",url);
				jb.put("url",DOMAIN+fileName);
//				file.delete();
				return jb;
			}catch (Exception e){
				throw new BusinessException("上传失败"+e.getMessage());
			}
		}else {
			JSONObject params = new JSONObject();
			params.put("file", file);
			params.put("path", GATE_PATH);
			params.put("output", "json");
			String resp = HttpUtil.post(UPLOAD_PATH, params);
			JSONObject jb = JSON.parseObject(resp);
//			file.delete();
			return jb;
		}
	}
}
