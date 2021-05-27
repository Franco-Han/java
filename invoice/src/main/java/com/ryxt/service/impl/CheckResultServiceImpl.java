package com.ryxt.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckResult;
import com.ryxt.mapper.CheckResultMapper;
import com.ryxt.service.CheckResultService;
import com.ryxt.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CheckResultServiceImpl implements CheckResultService {

	@Autowired
	public CheckResultMapper checkResultMapper;

    @Override
    public CommonListResponse<CheckResult> getListPage(BaseInput record) {
        CommonListResponse<CheckResult> commonListResponse = new CommonListResponse<CheckResult>(record);
        Page<CheckResult> page = new Page<CheckResult>(record.getPage(),record.getPageSize());
        EntityWrapper<CheckResult> wrapper =  (EntityWrapper<CheckResult>) BeanTrans.beanToNormal(record,CheckResult.class);
        wrapper.orderBy("create_date",true);
        commonListResponse.setTotalCount(checkResultMapper.selectCount(wrapper));
        List<CheckResult> list = checkResultMapper.selectPage(page,wrapper);
        commonListResponse.setList(list);
        return commonListResponse;
    }

    @Override
    public CheckResult saveOrUpdate(CheckResult record) {

        String userId = BaseAuthUtil.getCurrentUserId();

        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        CheckResult record1 = checkResultMapper.selectById(record.getId());

        if(record1!=null){
            checkResultMapper.updateById(record);
        }else {
            record.setUserId(userId);
            record.setCreateDate(new Date());
            checkResultMapper.insert(record);
        }
        return record;
    }

    @Override
    public int deleteById(String id) {
        CheckResult sysCheckResult = new CheckResult();
        sysCheckResult.setId(id);
        return checkResultMapper.updateById(sysCheckResult);
    }

    @Override
    public CheckResult selectById(String id) {
        return checkResultMapper.selectById(id);
    }
    @Override
    public String downloadImages(BaseInput record) {

        CommonListResponse<CheckResult> commonListResponse = new CommonListResponse<CheckResult>(record);
        EntityWrapper<CheckResult> wrapper =  (EntityWrapper<CheckResult>) BeanTrans.beanToNormal(record,CheckResult.class);
        wrapper.orderBy("create_date",false);
        commonListResponse.setTotalCount(checkResultMapper.selectCount(wrapper));
        List<CheckResult> list = checkResultMapper.selectList(wrapper);



        return "";
    }


//    private String packageFiles(){
//
//        try{
//        String downloadFilename="";
//        String nowTimeString = String.valueOf(System.currentTimeMillis());
//        downloadFilename ="test.zip";//文件的名称
////        downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");//转换中文否则可能会产生乱码
//
//        FileInputStream ips = null;
//        File file =new File("");
//        String url="";
//        byte[] buffer = new byte[1024];
//        int r=0;
//        for (int i = 0; i < totalpassenger.size(); i++) {
//
//            if(totalpassenger.get(i).getPassport_pic()!=null){
//                       /* URL url2 = new URL(totalpassenger.get(i).getPassport_pic());
//                zos.putNextEntry(new ZipEntry(totalpassenger.get(i).getName()+"-护照"+".jpg"));
//                InputStream fis2 = url2.openConnection().getInputStream();
//                while ((r = fis2.read(buffer)) != -1) {
//                    zos.write(buffer, 0, r);
//                }
//                fis2.close();*/
//
//                zos.putNextEntry(new ZipEntry(totalpassenger.get(i).getName()+"-护照"+".jpg"));
//                url=totalpassenger.get(i).getPassport_pic();
//                //获取图片存放路径
//                file = new File(url);
//                if(file.exists()) {
//                    ips = new FileInputStream(file);
//                    while ((r = ips.read(buffer)) != -1){
//                        zos.write(buffer, 0, r);
//                    }
//                    if (null != ips) {
//                        ips.close();
//                    }
//
//                }
//
//            }
//        }
//        zos.flush();
//        zos.close();
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//    }
}
