package com.ryxt.service.impl;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.OcrInfo;
import com.ryxt.entity.ocr.OcrResult;
import com.ryxt.exception.BusinessException;
import com.ryxt.mapper.OcrMapper;
import com.ryxt.service.OcrService;
import com.ryxt.task.CheckThread;
import com.ryxt.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OcrServiceImpl implements OcrService {

	@Autowired
	public OcrMapper ocrMapper;

    private Logger logger = LoggerFactory.getLogger(OcrServiceImpl.class);

    @Value("${ocr.service.url}")
    private String URL;
    @Override
    public CommonListResponse<OcrInfo> getListPage(BaseInput record) {

        CommonListResponse<OcrInfo> commonListResponse = new CommonListResponse<OcrInfo>(record);
        Page<OcrInfo> page = new Page<OcrInfo>(record.getPage(),record.getPageSize());
        EntityWrapper<OcrInfo> wrapper =  (EntityWrapper<OcrInfo>) BeanTrans.beanToNormal(record,OcrInfo.class);
        wrapper.where("to_days(create_date) = to_days(now())");
        wrapper.orderBy("create_date",false);
        commonListResponse.setTotalCount(ocrMapper.selectCount(wrapper));
        List<OcrInfo> list = ocrMapper.selectPage(page,wrapper);
        commonListResponse.setList(list);
        return commonListResponse;
    }
    @Override
    @Transactional
    public OcrInfo saveOrUpdate(OcrInfo record) {

        String userId = BaseAuthUtil.getCurrentUserId();

        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        OcrInfo record1 = ocrMapper.selectById(record.getId());

        if(record1!= null){
            record.setUserId(userId);
            ocrMapper.updateById(record);
        }else {

            record.setUserId(userId);
            record.setCreateDate(new Date());
            record.setDeleteFlag(Const.NORMAL_STATUS);
            ocrMapper.insert(record);
        }
        return record;
    }

    @Override
    public int deleteById(String id) {
        OcrInfo record = new OcrInfo();
        record.setId(id);
        record.setDeleteFlag(Const.DELETE_STATUS);
        return ocrMapper.updateById(record);
    }

    @Override
    public OcrInfo selectById(String id) {
        return ocrMapper.selectById(id);
    }

    @Override
    @Transactional
    public OcrInfo scan(String filePath) {

        logger.info("ocr识别:"+filePath);
        JSONObject params = new JSONObject();
        String base64 = Base64Util.getImgStrToBase64(filePath);
        logger.info("ocr识别base64长度:"+base64.length());
        params.put("cardtype", "20090");
        params.put("imgbase64", base64);
        params.put("outvalue", "1");
        params.put("username", "test");
        String resp = HttpUtil.post(URL,params.toJSONString());
        Console.log("resp: {}", resp);
        logger.info("ocr识别 resp: ", resp);

        OcrInfo ocrInfo = analysisOcrData(filePath,resp);

        if(ocrInfo==null){
            throw new BusinessException("识别失败");
        }
//        OcrInfo ocrInfo = new OcrInfo();
//        ocrInfo.setId(StringUtil.generateUUID());
//        ocrInfo.setUrl(filePath);
//        try{
//            JSONObject data = (JSONObject)jb.get("data");
//            JSONObject message = (JSONObject)data.get("message");
//
//            Integer status = message.getInteger("status");
//
//            if(status ==0){
//                JSONObject cardsinfo = (JSONObject)data.get("cardsinfo");
//                JSONObject card = (JSONObject)cardsinfo.get("card");
//                List<OcrResult> dataArr = JSON.parseArray(card.getString("item"),OcrResult.class);
//                // 先确定一下发票类型
//                // 确定是否有二维码，如果有二维码，以二维码为准
//                String qrcode = "";
//
//                String invoiceCode1="";
//                String invoiceNumber1="";
//                String invoiceCode2="";
//                String invoiceNumber2="";
//                String invoiceCode3="";
//                String invoiceNumber3="";
//                String invoiceCode4="";
//                String invoiceNumber4="";
//                for(OcrResult result :dataArr){
//                    if("发票类型".equals(result.getDesc())){
//                        if(result.getContent().contains("增值税专用发票")){
//                            //增值税专用发票
//                            ocrInfo.setInvoiceType("01");
//                        }else  if(result.getContent().contains("电子发票")){
//                            // 增值税电子普通发票
//                            ocrInfo.setInvoiceType("10");
//                        }else  if(result.getContent().contains("增值税普通发票")){
//                            //增值税普通发票
//                            ocrInfo.setInvoiceType("04");
//                        }else{
//                            throw  new BusinessException("发票类型识别失败");
//                        }
//                    }
//                    if("二维码".equals(result.getDesc())){
//                        qrcode = result.getContent();
//                    }
//                    if("发票印刷代码".equals(result.getDesc())){
//                        invoiceCode1 = result.getContent();
//                    }
//                    if("发票印刷号码".equals(result.getDesc())){
//                        invoiceNumber1 = result.getContent();
//                    }
//                    if("发票机打代码".equals(result.getDesc())){
//                        invoiceCode2 = result.getContent();
//                    }
//                    if("发票机打号码".equals(result.getDesc())){
//                        invoiceNumber2 = result.getContent();
//                    }
//                    if("发票综合代码".equals(result.getDesc())){
//                        invoiceCode3 = result.getContent();
//                    }
//                    if("发票综合号码".equals(result.getDesc())){
//                        invoiceNumber3 = result.getContent();
//                    }
//
//                    if("发票二维码代码".equals(result.getDesc())){
//                        invoiceCode4 = result.getContent();
//                    }
//                    if("发票二维码号码".equals(result.getDesc())){
//                        invoiceNumber4 = result.getContent();
//                    }
//                }
//
//                String[] qrcodeArr = new String[0];
//                boolean hasQrcode = false;
//                if(StringUtil.isNotNullEmpty(qrcode)){
//                    qrcodeArr = qrcode.split(",");
//                    if(qrcodeArr.length>=8){
//                        hasQrcode = true;
//                    }
//                }
//              // ↑↑↑↑↑   二维码OCR结果>发票印刷代码、发票印刷号码（或发票综合代码、发票综合号码）>发票机打代码、发票机打号码。
//                if(!invoiceCode1.equals(invoiceCode2)||!invoiceNumber1.equals(invoiceNumber2)){
//                    // 发票机打XX
//                    ocrInfo.setInvoiceCode(invoiceCode2);
//                    ocrInfo.setInvoiceNumber(invoiceNumber2);
//                }else{
//                    // 发票印刷XX
//                    ocrInfo.setInvoiceCode(invoiceCode1);
//                    ocrInfo.setInvoiceNumber(invoiceNumber1);
//                }
//                if(!invoiceCode3.equals(ocrInfo.getInvoiceCode())||!invoiceNumber3.equals(ocrInfo.getInvoiceNumber())){
//                   if(!"".equals(invoiceCode3)&& !"".equals(invoiceNumber3)) {
//
//                       // 发票综合XX
//                       ocrInfo.setInvoiceCode(invoiceCode3);
//                       ocrInfo.setInvoiceNumber(invoiceNumber3);
//                   }
//                }
//                if(!invoiceCode4.equals(ocrInfo.getInvoiceCode())||!invoiceNumber4.equals(ocrInfo.getInvoiceNumber())){
//                    if(!"".equals(invoiceCode4)&& !"".equals(invoiceNumber4)) {
//                        // 发票二维码XX
//                        ocrInfo.setInvoiceCode(invoiceCode4);
//                        ocrInfo.setInvoiceNumber(invoiceNumber4);
//                    }
//                }
//                if(hasQrcode){
//                    if(!qrcodeArr[2].equals(ocrInfo.getInvoiceCode())||!qrcodeArr[3].equals(ocrInfo.getInvoiceNumber())){
//                        // 发票二维码 识别
//                        ocrInfo.setInvoiceCode(qrcodeArr[2]);
//                        ocrInfo.setInvoiceNumber(qrcodeArr[3]);
//                    }
//                }
//                for(OcrResult result :dataArr){
//                    if("发票印刷代码".equals(result.getDesc())){
////                        String invoiceCode = "";
////                        if(hasQrcode && !result.getContent().equals(qrcodeArr[2])){
////                            invoiceCode = qrcodeArr[2];
////                        }else{
////                            invoiceCode = result.getContent();
////                        }
////                        ocrInfo.setInvoiceCode(invoiceCode);
//                    }
//                    else if("发票印刷号码".equals(result.getDesc())){
////                        String invoiceNumber = "";
////                        if(hasQrcode && !result.getContent().equals(qrcodeArr[3])){
////                            invoiceNumber = qrcodeArr[3];
////                        }else{
////                            invoiceNumber = result.getContent();
////                        }
////                        ocrInfo.setInvoiceNumber(invoiceNumber);
//                    }
//                    else if("发票类型".equals(result.getDesc())){
//                        if(hasQrcode && !ocrInfo.getInvoiceType().equals(qrcodeArr[1])){
//                            ocrInfo.setInvoiceType(qrcodeArr[1]);
//                        }
//                    }
//                    else if("开票日期".equals(result.getDesc())){
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        String invoiceDate = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[5])){
//                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
//                            invoiceDate = sdf.format(sdf2.parse(qrcodeArr[5]));
//                        }else{
//                            invoiceDate = result.getContent();
//                        }
//                        ocrInfo.setInvoiceDate(sdf.parse(invoiceDate));
//                    }
//                    else if("购方税号".equals(result.getDesc())){
//                        ocrInfo.setTin(result.getContent());
//                    }
//                    else if("销方税号".equals(result.getDesc())){
//                        ocrInfo.setSellerTin(result.getContent());
//                    }
//                    else if("金额".equals(result.getDesc())){
//                        String excludingTaxPrice = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[4])){
//                            excludingTaxPrice = qrcodeArr[4];
//                        }else{
//                            excludingTaxPrice = result.getContent();
//                        }
//                        ocrInfo.setExcludingTaxPrice(excludingTaxPrice);
//                        ocrInfo.setTotalAmount(excludingTaxPrice);
//                    }
//                    else if("税额".equals(result.getDesc())){
//
//                        // 实际为税额合计金额
//                        ocrInfo.setTotalAmountOfTax(result.getContent());
//                    }
//                    else if("合计金额".equals(result.getDesc())){
//                        ocrInfo.setTotalAmountOfPriceAndTax(result.getContent());
//                    }
//                    else if("校验码".equals(result.getDesc())){
//                        String checkCode = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[6])){
//                            checkCode = qrcodeArr[6];
//                        }else{
//                            checkCode = result.getContent();
//                        }
//                        ocrInfo.setCheckCode(checkCode);
//                    }
//                    else if("购方名称".equals(result.getDesc())){
//                        ocrInfo.setPurchaser(result.getContent());
//                    }
//                    else if("销方名称".equals(result.getDesc())){
//                        ocrInfo.setSellerName(result.getContent());
//                    }
//                    else if("大写金额".equals(result.getDesc())){
//                        ocrInfo.setTotalAmountOfPriceAndTaxEx(result.getContent());
//                    }
//                    else if("购方地址、电话".equals(result.getDesc())){
//                        ocrInfo.setAddressAndTelephone(result.getContent());
//                    }
//                    else if("购方开户行及账号".equals(result.getDesc())){
//                        ocrInfo.setAccountBankAndNumber(result.getContent());
//                    }
//                    else if("销方地址、电话".equals(result.getDesc())){
//                        ocrInfo.setSellerAddressAndTelephone(result.getContent());
//                    }
//                    else if("销方开户行及账号".equals(result.getDesc())){
//                        ocrInfo.setSellerAccountBankAndNumber(result.getContent());
//                    }
//                    else if("收款人".equals(result.getDesc())){
//                        ocrInfo.setPayee(result.getContent());
//                    }
//                    else if("复核".equals(result.getDesc())){
//                        ocrInfo.setReview(result.getContent());
//                    }
//                    else if("开票人".equals(result.getDesc())){
//                        ocrInfo.setDrawer(result.getContent());
//                    }
//                    else if("备注".equals(result.getDesc())){
//                        ocrInfo.setRemarks(result.getContent());
//                    }
//                    else if("明细内容".equals(result.getDesc())){
//                        ocrInfo.setNameOfGoodsOrTaxableServices(result.getContent());
//                    }
//                }
//                ocrInfo = saveOrUpdate(ocrInfo);
//
//            }else{
//                throw  new BusinessException("识别失败");
//            }
//
//        }catch (Exception e){
//            throw new BusinessException("识别失败"+e.getMessage());
//        }

        return saveOrUpdate(ocrInfo);
    }
    @Override
    @Transactional
    public OcrInfo scan2(String filePath,String tempFile) {

        logger.info("ocr识别:"+filePath);
        JSONObject params = new JSONObject();
        String base64 = Base64Util.getImgStrToBase64(tempFile);
        logger.info("ocr识别base64长度:"+base64.length());
        params.put("cardtype", "20090");
        params.put("imgbase64", base64);
        params.put("outvalue", "1");
        params.put("username", "test");
        String resp = HttpUtil.post(URL,params.toJSONString());
        Console.log("resp: {}", resp);
        logger.info("ocr识别 resp: ", resp);
        OcrInfo ocrInfo = analysisOcrData(filePath,resp);

        if(ocrInfo==null){
            throw new BusinessException("识别失败");
        }
//        ocrInfo.setId(StringUtil.generateUUID());
//        ocrInfo.setUrl(filePath);
//        try{
//            JSONObject data = (JSONObject)jb.get("data");
//            JSONObject message = (JSONObject)data.get("message");
//
//            Integer status = message.getInteger("status");
//
//            if(status ==0){
//                JSONObject cardsinfo = (JSONObject)data.get("cardsinfo");
//                JSONObject card = (JSONObject)cardsinfo.get("card");
//                List<OcrResult> dataArr = JSON.parseArray(card.getString("item"),OcrResult.class);
//                // 先确定一下发票类型
//                // 确定是否有二维码，如果有二维码，以二维码为准
//                String qrcode = "";
//
//                String invoiceCode1="";
//                String invoiceNumber1="";
//                String invoiceCode2="";
//                String invoiceNumber2="";
//                String invoiceCode3="";
//                String invoiceNumber3="";
//                String invoiceCode4="";
//                String invoiceNumber4="";
//                for(OcrResult result :dataArr){
//                    if("发票类型".equals(result.getDesc())){
//                        if(result.getContent().contains("增值税专用发票")){
//                            //增值税专用发票
//                            ocrInfo.setInvoiceType("01");
//                        }else  if(result.getContent().contains("电子发票")){
//                            // 增值税电子普通发票
//                            ocrInfo.setInvoiceType("10");
//                        }else  if(result.getContent().contains("增值税普通发票")){
//                            //增值税普通发票
//                            ocrInfo.setInvoiceType("04");
//                        }else{
//                            throw  new BusinessException("发票类型识别失败");
//                        }
//                    }
//                    if("二维码".equals(result.getDesc())){
//                        qrcode = result.getContent();
//                    }
//                    if("发票印刷代码".equals(result.getDesc())){
//                        invoiceCode1 = result.getContent();
//                    }
//                    if("发票印刷号码".equals(result.getDesc())){
//                        invoiceNumber1 = result.getContent();
//                    }
//                    if("发票机打代码".equals(result.getDesc())){
//                        invoiceCode2 = result.getContent();
//                    }
//                    if("发票机打号码".equals(result.getDesc())){
//                        invoiceNumber2 = result.getContent();
//                    }
//                    if("发票综合代码".equals(result.getDesc())){
//                        invoiceCode3 = result.getContent();
//                    }
//                    if("发票综合号码".equals(result.getDesc())){
//                        invoiceNumber3 = result.getContent();
//                    }
//
//                    if("发票二维码代码".equals(result.getDesc())){
//                        invoiceCode4 = result.getContent();
//                    }
//                    if("发票二维码号码".equals(result.getDesc())){
//                        invoiceNumber4 = result.getContent();
//                    }
//                }
//
//                String[] qrcodeArr = new String[0];
//                boolean hasQrcode = false;
//                if(StringUtil.isNotNullEmpty(qrcode)){
//                    qrcodeArr = qrcode.split(",");
//                    if(qrcodeArr.length>=8){
//                        hasQrcode = true;
//                    }
//                }
//              // ↑↑↑↑↑   二维码OCR结果>发票印刷代码、发票印刷号码（或发票综合代码、发票综合号码）>发票机打代码、发票机打号码。
//                if(!invoiceCode1.equals(invoiceCode2)||!invoiceNumber1.equals(invoiceNumber2)){
//                    // 发票机打XX
//                    ocrInfo.setInvoiceCode(invoiceCode2);
//                    ocrInfo.setInvoiceNumber(invoiceNumber2);
//                }else{
//                    // 发票印刷XX
//                    ocrInfo.setInvoiceCode(invoiceCode1);
//                    ocrInfo.setInvoiceNumber(invoiceNumber1);
//                }
//                if(!invoiceCode3.equals(ocrInfo.getInvoiceCode())||!invoiceNumber3.equals(ocrInfo.getInvoiceNumber())){
//                   if(!"".equals(invoiceCode3)&& !"".equals(invoiceNumber3)) {
//
//                       // 发票综合XX
//                       ocrInfo.setInvoiceCode(invoiceCode3);
//                       ocrInfo.setInvoiceNumber(invoiceNumber3);
//                   }
//                }
//                if(!invoiceCode4.equals(ocrInfo.getInvoiceCode())||!invoiceNumber4.equals(ocrInfo.getInvoiceNumber())){
//                    if(!"".equals(invoiceCode4)&& !"".equals(invoiceNumber4)) {
//                        // 发票二维码XX
//                        ocrInfo.setInvoiceCode(invoiceCode4);
//                        ocrInfo.setInvoiceNumber(invoiceNumber4);
//                    }
//                }
//                if(hasQrcode){
//                    if(!qrcodeArr[2].equals(ocrInfo.getInvoiceCode())||!qrcodeArr[3].equals(ocrInfo.getInvoiceNumber())){
//                        // 发票二维码 识别
//                        ocrInfo.setInvoiceCode(qrcodeArr[2]);
//                        ocrInfo.setInvoiceNumber(qrcodeArr[3]);
//                    }
//                }
//                for(OcrResult result :dataArr){
//                    if("发票印刷代码".equals(result.getDesc())){
////                        String invoiceCode = "";
////                        if(hasQrcode && !result.getContent().equals(qrcodeArr[2])){
////                            invoiceCode = qrcodeArr[2];
////                        }else{
////                            invoiceCode = result.getContent();
////                        }
////                        ocrInfo.setInvoiceCode(invoiceCode);
//                    }
//                    else if("发票印刷号码".equals(result.getDesc())){
////                        String invoiceNumber = "";
////                        if(hasQrcode && !result.getContent().equals(qrcodeArr[3])){
////                            invoiceNumber = qrcodeArr[3];
////                        }else{
////                            invoiceNumber = result.getContent();
////                        }
////                        ocrInfo.setInvoiceNumber(invoiceNumber);
//                    }
//                    else if("发票类型".equals(result.getDesc())){
//                        if(hasQrcode && !ocrInfo.getInvoiceType().equals(qrcodeArr[1])){
//                            ocrInfo.setInvoiceType(qrcodeArr[1]);
//                        }
//                    }
//                    else if("开票日期".equals(result.getDesc())){
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        String invoiceDate = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[5])){
//                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
//                            invoiceDate = sdf.format(sdf2.parse(qrcodeArr[5]));
//                        }else{
//                            invoiceDate = result.getContent();
//                        }
//                        ocrInfo.setInvoiceDate(sdf.parse(invoiceDate));
//                    }
//                    else if("购方税号".equals(result.getDesc())){
//                        ocrInfo.setTin(result.getContent());
//                    }
//                    else if("销方税号".equals(result.getDesc())){
//                        ocrInfo.setSellerTin(result.getContent());
//                    }
//                    else if("金额".equals(result.getDesc())){
//                        String excludingTaxPrice = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[4])){
//                            excludingTaxPrice = qrcodeArr[4];
//                        }else{
//                            excludingTaxPrice = result.getContent();
//                        }
//                        ocrInfo.setExcludingTaxPrice(excludingTaxPrice);
//                        ocrInfo.setTotalAmount(excludingTaxPrice);
//                    }
//                    else if("税额".equals(result.getDesc())){
//
//                        // 实际为税额合计金额
//                        ocrInfo.setTotalAmountOfTax(result.getContent());
//                    }
//                    else if("合计金额".equals(result.getDesc())){
//                        ocrInfo.setTotalAmountOfPriceAndTax(result.getContent());
//                    }
//                    else if("校验码".equals(result.getDesc())){
//                        String checkCode = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[6])){
//                            checkCode = qrcodeArr[6];
//                        }else{
//                            checkCode = result.getContent();
//                        }
//                        ocrInfo.setCheckCode(checkCode);
//                    }
//                    else if("购方名称".equals(result.getDesc())){
//                        ocrInfo.setPurchaser(result.getContent());
//                    }
//                    else if("销方名称".equals(result.getDesc())){
//                        ocrInfo.setSellerName(result.getContent());
//                    }
//                    else if("大写金额".equals(result.getDesc())){
//                        ocrInfo.setTotalAmountOfPriceAndTaxEx(result.getContent());
//                    }
//                    else if("购方地址、电话".equals(result.getDesc())){
//                        ocrInfo.setAddressAndTelephone(result.getContent());
//                    }
//                    else if("购方开户行及账号".equals(result.getDesc())){
//                        ocrInfo.setAccountBankAndNumber(result.getContent());
//                    }
//                    else if("销方地址、电话".equals(result.getDesc())){
//                        ocrInfo.setSellerAddressAndTelephone(result.getContent());
//                    }
//                    else if("销方开户行及账号".equals(result.getDesc())){
//                        ocrInfo.setSellerAccountBankAndNumber(result.getContent());
//                    }
//                    else if("收款人".equals(result.getDesc())){
//                        ocrInfo.setPayee(result.getContent());
//                    }
//                    else if("复核".equals(result.getDesc())){
//                        ocrInfo.setReview(result.getContent());
//                    }
//                    else if("开票人".equals(result.getDesc())){
//                        ocrInfo.setDrawer(result.getContent());
//                    }
//                    else if("备注".equals(result.getDesc())){
//                        ocrInfo.setRemarks(result.getContent());
//                    }
//                    else if("明细内容".equals(result.getDesc())){
//                        ocrInfo.setNameOfGoodsOrTaxableServices(result.getContent());
//                    }
//                }
//                ocrInfo = saveOrUpdate(ocrInfo);
//
//            }else{
//                throw  new BusinessException("识别失败");
//            }
//
//        }catch (Exception e){
//            throw new BusinessException("识别失败"+e.getMessage());
//        }
        File file = new File(tempFile);
        if(file.exists()){
            file.delete();
        }
        return saveOrUpdate(ocrInfo);
    }
    @Override
    @Transactional
    public OcrInfo scanNoSave(String filePath) {

        logger.info("ocr识别:"+filePath);
        JSONObject params = new JSONObject();
        String base64 = Base64Util.getImgStrToBase64(filePath);
        logger.info("ocr识别base64长度:"+base64.length());
        params.put("cardtype", "20090");
        params.put("imgbase64", base64);
        params.put("outvalue", "1");
        params.put("username", "test");
        String resp = HttpUtil.post(URL,params.toJSONString());
        Console.log("resp: {}", resp);
        logger.info("ocr识别 resp: ", resp);

//        OcrInfo ocrInfo = new OcrInfo();
//        ocrInfo.setId(StringUtil.generateUUID());
//        ocrInfo.setUrl(filePath);
//        try{
//            JSONObject data = (JSONObject)jb.get("data");
//            JSONObject message = (JSONObject)data.get("message");
//
//            Integer status = message.getInteger("status");
//
//            if(status ==0){
//                JSONObject cardsinfo = (JSONObject)data.get("cardsinfo");
//                JSONObject card = (JSONObject)cardsinfo.get("card");
//                List<OcrResult> dataArr = JSON.parseArray(card.getString("item"),OcrResult.class);
//                // 先确定一下发票类型
//                // 确定是否有二维码，如果有二维码，以二维码为准
//                String qrcode = "";
//
//                String invoiceCode1="";
//                String invoiceNumber1="";
//                String invoiceCode2="";
//                String invoiceNumber2="";
//                String invoiceCode3="";
//                String invoiceNumber3="";
//                String invoiceCode4="";
//                String invoiceNumber4="";
//                for(OcrResult result :dataArr){
//                    if("发票类型".equals(result.getDesc())){
//                        if(result.getContent().contains("增值税专用发票")){
//                            //增值税专用发票
//                            ocrInfo.setInvoiceType("01");
//                        }else  if(result.getContent().contains("电子发票")){
//                            // 增值税电子普通发票
//                            ocrInfo.setInvoiceType("10");
//                        }else  if(result.getContent().contains("增值税普通发票")){
//                            //增值税普通发票
//                            ocrInfo.setInvoiceType("04");
//                        }else{
//                            throw  new BusinessException("发票类型识别失败");
//                        }
//                    }
//                    if("二维码".equals(result.getDesc())){
//                        qrcode = result.getContent();
//                    }
//                    if("发票印刷代码".equals(result.getDesc())){
//                        invoiceCode1 = result.getContent();
//                    }
//                    if("发票印刷号码".equals(result.getDesc())){
//                        invoiceNumber1 = result.getContent();
//                    }
//                    if("发票机打代码".equals(result.getDesc())){
//                        invoiceCode2 = result.getContent();
//                    }
//                    if("发票机打号码".equals(result.getDesc())){
//                        invoiceNumber2 = result.getContent();
//                    }
//                    if("发票综合代码".equals(result.getDesc())){
//                        invoiceCode3 = result.getContent();
//                    }
//                    if("发票综合号码".equals(result.getDesc())){
//                        invoiceNumber3 = result.getContent();
//                    }
//
//                    if("发票二维码代码".equals(result.getDesc())){
//                        invoiceCode4 = result.getContent();
//                    }
//                    if("发票二维码号码".equals(result.getDesc())){
//                        invoiceNumber4 = result.getContent();
//                    }
//                }
//
//                String[] qrcodeArr = new String[0];
//                boolean hasQrcode = false;
//                if(StringUtil.isNotNullEmpty(qrcode)){
//                    qrcodeArr = qrcode.split(",");
//                    if(qrcodeArr.length>=8){
//                        hasQrcode = true;
//                    }
//                }
//              // ↑↑↑↑↑   二维码OCR结果>发票印刷代码、发票印刷号码（或发票综合代码、发票综合号码）>发票机打代码、发票机打号码。
//                if(!invoiceCode1.equals(invoiceCode2)||!invoiceNumber1.equals(invoiceNumber2)){
//                    // 发票机打XX
//                    ocrInfo.setInvoiceCode(invoiceCode2);
//                    ocrInfo.setInvoiceNumber(invoiceNumber2);
//                }else{
//                    // 发票印刷XX
//                    ocrInfo.setInvoiceCode(invoiceCode1);
//                    ocrInfo.setInvoiceNumber(invoiceNumber1);
//                }
//                if(!invoiceCode3.equals(ocrInfo.getInvoiceCode())||!invoiceNumber3.equals(ocrInfo.getInvoiceNumber())){
//                    if(!"".equals(invoiceCode3)&& !"".equals(invoiceNumber3)) {
//
//                        // 发票综合XX
//                        ocrInfo.setInvoiceCode(invoiceCode3);
//                        ocrInfo.setInvoiceNumber(invoiceNumber3);
//                    }
//                }
//                if(!invoiceCode4.equals(ocrInfo.getInvoiceCode())||!invoiceNumber4.equals(ocrInfo.getInvoiceNumber())){
//                    if(!"".equals(invoiceCode4)&& !"".equals(invoiceNumber4)) {
//                        // 发票二维码XX
//                        ocrInfo.setInvoiceCode(invoiceCode4);
//                        ocrInfo.setInvoiceNumber(invoiceNumber4);
//                    }
//                }
//                if(hasQrcode){
//                    if(!qrcodeArr[2].equals(ocrInfo.getInvoiceCode())||!qrcodeArr[3].equals(ocrInfo.getInvoiceNumber())){
//                        // 发票二维码 识别
//                        ocrInfo.setInvoiceCode(qrcodeArr[2]);
//                        ocrInfo.setInvoiceNumber(qrcodeArr[3]);
//                    }
//                }
//                for(OcrResult result :dataArr){
//                    if("发票印刷代码".equals(result.getDesc())){
////                        String invoiceCode = "";
////                        if(hasQrcode && !result.getContent().equals(qrcodeArr[2])){
////                            invoiceCode = qrcodeArr[2];
////                        }else{
////                            invoiceCode = result.getContent();
////                        }
////                        ocrInfo.setInvoiceCode(invoiceCode);
//                    }
//                    else if("发票印刷号码".equals(result.getDesc())){
////                        String invoiceNumber = "";
////                        if(hasQrcode && !result.getContent().equals(qrcodeArr[3])){
////                            invoiceNumber = qrcodeArr[3];
////                        }else{
////                            invoiceNumber = result.getContent();
////                        }
////                        ocrInfo.setInvoiceNumber(invoiceNumber);
//                    }
//                    else if("发票类型".equals(result.getDesc())){
//                        if(hasQrcode && !ocrInfo.getInvoiceType().equals(qrcodeArr[1])){
//                            ocrInfo.setInvoiceType(qrcodeArr[1]);
//                        }
//                    }
//                    else if("开票日期".equals(result.getDesc())){
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        String invoiceDate = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[5])){
//                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
//                            invoiceDate = sdf.format(sdf2.parse(qrcodeArr[5]));
//                        }else{
//                            invoiceDate = result.getContent();
//                        }
//                        ocrInfo.setInvoiceDate(sdf.parse(invoiceDate));
//                    }
//                    else if("购方税号".equals(result.getDesc())){
//                        ocrInfo.setTin(result.getContent());
//                    }
//                    else if("销方税号".equals(result.getDesc())){
//                        ocrInfo.setSellerTin(result.getContent());
//                    }
//                    else if("金额".equals(result.getDesc())){
//                        String excludingTaxPrice = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[4])){
//                            excludingTaxPrice = qrcodeArr[4];
//                        }else{
//                            excludingTaxPrice = result.getContent();
//                        }
//                        ocrInfo.setExcludingTaxPrice(excludingTaxPrice);
//                        ocrInfo.setTotalAmount(excludingTaxPrice);
//                    }
//                    else if("税额".equals(result.getDesc())){
//
//                        // 实际为税额合计金额
//                        ocrInfo.setTotalAmountOfTax(result.getContent());
//                    }
//                    else if("合计金额".equals(result.getDesc())){
//                        ocrInfo.setTotalAmountOfPriceAndTax(result.getContent());
//                    }
//                    else if("校验码".equals(result.getDesc())){
//                        String checkCode = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[6])){
//                            checkCode = qrcodeArr[6];
//                        }else{
//                            checkCode = result.getContent();
//                        }
//                        ocrInfo.setCheckCode(checkCode);
//                    }
//                    else if("购方名称".equals(result.getDesc())){
//                        ocrInfo.setPurchaser(result.getContent());
//                    }
//                    else if("销方名称".equals(result.getDesc())){
//                        ocrInfo.setSellerName(result.getContent());
//                    }
//                    else if("大写金额".equals(result.getDesc())){
//                        ocrInfo.setTotalAmountOfPriceAndTaxEx(result.getContent());
//                    }
//                    else if("购方地址、电话".equals(result.getDesc())){
//                        ocrInfo.setAddressAndTelephone(result.getContent());
//                    }
//                    else if("购方开户行及账号".equals(result.getDesc())){
//                        ocrInfo.setAccountBankAndNumber(result.getContent());
//                    }
//                    else if("销方地址、电话".equals(result.getDesc())){
//                        ocrInfo.setSellerAddressAndTelephone(result.getContent());
//                    }
//                    else if("销方开户行及账号".equals(result.getDesc())){
//                        ocrInfo.setSellerAccountBankAndNumber(result.getContent());
//                    }
//                    else if("收款人".equals(result.getDesc())){
//                        ocrInfo.setPayee(result.getContent());
//                    }
//                    else if("复核".equals(result.getDesc())){
//                        ocrInfo.setReview(result.getContent());
//                    }
//                    else if("开票人".equals(result.getDesc())){
//                        ocrInfo.setDrawer(result.getContent());
//                    }
//                    else if("备注".equals(result.getDesc())){
//                        ocrInfo.setRemarks(result.getContent());
//                    }
//                    else if("明细内容".equals(result.getDesc())){
//                        ocrInfo.setNameOfGoodsOrTaxableServices(result.getContent());
//                    }
//                }
//            }else{
//                return null;
//            }
//
//        }catch (Exception e){
//            return null;
//        }

        return analysisOcrData(filePath,resp);
    }
    private OcrInfo analysisOcrData(String filePath,String resp) {
        JSONObject jb = JSON.parseObject(resp);

        OcrInfo ocrInfo = new OcrInfo();
        ocrInfo.setId(StringUtil.generateUUID());
        ocrInfo.setUrl(filePath);
        try{
            JSONObject data = (JSONObject)jb.get("data");
            JSONObject message = (JSONObject)data.get("message");

            Integer status = message.getInteger("status");

            if(status ==0){
                JSONObject cardsinfo = (JSONObject)data.get("cardsinfo");
                JSONObject card = (JSONObject)cardsinfo.get("card");
                List<OcrResult> dataArr = JSON.parseArray(card.getString("item"),OcrResult.class);
                // 先确定一下发票类型
                // 确定是否有二维码，如果有二维码，以二维码为准
                String qrcode = "";

                String invoiceCode1="";
                String invoiceNumber1="";
                String invoiceCode2="";
                String invoiceNumber2="";
                String invoiceCode3="";
                String invoiceNumber3="";
                String invoiceCode4="";
                String invoiceNumber4="";
                for(OcrResult result :dataArr){
                    if("发票类型".equals(result.getDesc())){
                        if(result.getContent().contains("增值税专用发票")){
                            //增值税专用发票
                            ocrInfo.setInvoiceType("01");
                        }else  if(result.getContent().contains("电子发票")){
                            // 增值税电子普通发票
                            ocrInfo.setInvoiceType("10");
                        }else  if(result.getContent().contains("增值税普通发票")){
                            //增值税普通发票
                            ocrInfo.setInvoiceType("04");
                        }else{
                            throw  new BusinessException("发票类型识别失败");
                        }
                    }
                    if("二维码".equals(result.getDesc())){
                        qrcode = result.getContent();
                    }
                    if("发票印刷代码".equals(result.getDesc())){
                        invoiceCode1 = result.getContent();
                    }
                    if("发票印刷号码".equals(result.getDesc())){
                        invoiceNumber1 = result.getContent();
                    }
                    if("发票机打代码".equals(result.getDesc())){
                        invoiceCode2 = result.getContent();
                    }
                    if("发票机打号码".equals(result.getDesc())){
                        invoiceNumber2 = result.getContent();
                    }
                    if("发票综合代码".equals(result.getDesc())){
                        invoiceCode3 = result.getContent();
                    }
                    if("发票综合号码".equals(result.getDesc())){
                        invoiceNumber3 = result.getContent();
                    }

                    if("发票二维码代码".equals(result.getDesc())){
                        invoiceCode4 = result.getContent();
                    }
                    if("发票二维码号码".equals(result.getDesc())){
                        invoiceNumber4 = result.getContent();
                    }
                }

                String[] qrcodeArr = new String[0];
                boolean hasQrcode = false;
                if(StringUtil.isNotNullEmpty(qrcode)){
                    qrcodeArr = qrcode.split(",");
                    if(qrcodeArr.length>=8){
                        hasQrcode = true;
                    }
                }
              // ↑↑↑↑↑   二维码OCR结果>发票印刷代码、发票印刷号码（或发票综合代码、发票综合号码）>发票机打代码、发票机打号码。
                if(!invoiceCode1.equals(invoiceCode2)||!invoiceNumber1.equals(invoiceNumber2)){
                    // 发票机打XX
                    ocrInfo.setInvoiceCode(invoiceCode2);
                    ocrInfo.setInvoiceNumber(invoiceNumber2);
                }else{
                    // 发票印刷XX
                    ocrInfo.setInvoiceCode(invoiceCode1);
                    ocrInfo.setInvoiceNumber(invoiceNumber1);
                }
                if(!invoiceCode3.equals(ocrInfo.getInvoiceCode())||!invoiceNumber3.equals(ocrInfo.getInvoiceNumber())){
                    if(!"".equals(invoiceCode3)&& !"".equals(invoiceNumber3)) {

                        // 发票综合XX
                        ocrInfo.setInvoiceCode(invoiceCode3);
                        ocrInfo.setInvoiceNumber(invoiceNumber3);
                    }
                }
                if(!invoiceCode4.equals(ocrInfo.getInvoiceCode())||!invoiceNumber4.equals(ocrInfo.getInvoiceNumber())){
                    if(!"".equals(invoiceCode4)&& !"".equals(invoiceNumber4)) {
                        // 发票二维码XX
                        ocrInfo.setInvoiceCode(invoiceCode4);
                        ocrInfo.setInvoiceNumber(invoiceNumber4);
                    }
                }
                if(hasQrcode){
                    if(!qrcodeArr[2].equals(ocrInfo.getInvoiceCode())||!qrcodeArr[3].equals(ocrInfo.getInvoiceNumber())){
                        // 发票二维码 识别
                        ocrInfo.setInvoiceCode(qrcodeArr[2]);
                        ocrInfo.setInvoiceNumber(qrcodeArr[3]);
                    }
                }
                for(OcrResult result :dataArr){
                    if("发票印刷代码".equals(result.getDesc())){
//                        String invoiceCode = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[2])){
//                            invoiceCode = qrcodeArr[2];
//                        }else{
//                            invoiceCode = result.getContent();
//                        }
//                        ocrInfo.setInvoiceCode(invoiceCode);
                    }
                    else if("发票印刷号码".equals(result.getDesc())){
//                        String invoiceNumber = "";
//                        if(hasQrcode && !result.getContent().equals(qrcodeArr[3])){
//                            invoiceNumber = qrcodeArr[3];
//                        }else{
//                            invoiceNumber = result.getContent();
//                        }
//                        ocrInfo.setInvoiceNumber(invoiceNumber);
                    }
                    else if("发票类型".equals(result.getDesc())){
                        if(hasQrcode && !ocrInfo.getInvoiceType().equals(qrcodeArr[1])){
                            ocrInfo.setInvoiceType(qrcodeArr[1]);
                        }
                    }
                    else if("开票日期".equals(result.getDesc())){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String invoiceDate = "";
                        if(hasQrcode && !result.getContent().equals(qrcodeArr[5])){
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                            invoiceDate = sdf.format(sdf2.parse(qrcodeArr[5]));
                        }else{
                            invoiceDate = result.getContent();
                        }
                        ocrInfo.setInvoiceDate(sdf.parse(invoiceDate));
                    }
                    else if("购方税号".equals(result.getDesc())){
                        ocrInfo.setTin(result.getContent());
                    }
                    else if("销方税号".equals(result.getDesc())){
                        ocrInfo.setSellerTin(result.getContent());
                    }
                    else if("金额".equals(result.getDesc())){
                        String excludingTaxPrice = "";
                        if(hasQrcode && !result.getContent().equals(qrcodeArr[4])){
                            excludingTaxPrice = qrcodeArr[4];
                        }else{
                            excludingTaxPrice = result.getContent();
                        }
                        ocrInfo.setExcludingTaxPrice(excludingTaxPrice);
                        ocrInfo.setTotalAmount(excludingTaxPrice);
                    }
                    else if("税额".equals(result.getDesc())){

                        // 实际为税额合计金额
                        ocrInfo.setTotalAmountOfTax(result.getContent());
                    }
                    else if("合计金额".equals(result.getDesc())){
                        ocrInfo.setTotalAmountOfPriceAndTax(result.getContent());
                    }
                    else if("校验码".equals(result.getDesc())){
                        String checkCode = "";
                        if(hasQrcode && !result.getContent().equals(qrcodeArr[6])){
                            checkCode = qrcodeArr[6];
                        }else{
                            checkCode = result.getContent();
                        }
                        ocrInfo.setCheckCode(checkCode);
                    }
                    else if("购方名称".equals(result.getDesc())){
                        ocrInfo.setPurchaser(result.getContent());
                    }
                    else if("销方名称".equals(result.getDesc())){
                        ocrInfo.setSellerName(result.getContent());
                    }
                    else if("大写金额".equals(result.getDesc())){
                        ocrInfo.setTotalAmountOfPriceAndTaxEx(result.getContent());
                    }
                    else if("购方地址、电话".equals(result.getDesc())){
                        ocrInfo.setAddressAndTelephone(result.getContent());
                    }
                    else if("购方开户行及账号".equals(result.getDesc())){
                        ocrInfo.setAccountBankAndNumber(result.getContent());
                    }
                    else if("销方地址、电话".equals(result.getDesc())){
                        ocrInfo.setSellerAddressAndTelephone(result.getContent());
                    }
                    else if("销方开户行及账号".equals(result.getDesc())){
                        ocrInfo.setSellerAccountBankAndNumber(result.getContent());
                    }
                    else if("收款人".equals(result.getDesc())){
                        ocrInfo.setPayee(result.getContent());
                    }
                    else if("复核".equals(result.getDesc())){
                        ocrInfo.setReview(result.getContent());
                    }
                    else if("开票人".equals(result.getDesc())){
                        ocrInfo.setDrawer(result.getContent());
                    }
                    else if("备注".equals(result.getDesc())){
                        ocrInfo.setRemarks(result.getContent());
                    }
                    else if("明细内容".equals(result.getDesc())){
                        ocrInfo.setNameOfGoodsOrTaxableServices(result.getContent());
                    }
                }
            }else{
                return null;
            }

        }catch (Exception e){
            return null;
        }

        return ocrInfo;
    }
}
