package com.ryxt.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.CheckResult;
import com.ryxt.mapper.CheckResultMapper;
import com.ryxt.service.CheckResultService;
import com.ryxt.service.ExecuteService;
import com.ryxt.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ExecuteServiceImpl implements ExecuteService {

    @Value("${pythonPath}")
    private String pythonPath;
    @Override
    public Object execute(CheckList record) {
        log.debug("开始执行查验脚本");
        // 发票日期格式为yyyyMMdd
        String invoiceDate = DateUtils.formatDate(record.getInvoiceDate(),"yyyyMMdd");
        // 验证码要取后六位
        // 防止有空格
        String checkCode = "0";
        if(StringUtil.isNotNullEmpty(record.getCheckCode())){
            checkCode =  record.getCheckCode().replaceAll(" ","");
        }
        if(checkCode.length()>=6){
            checkCode = checkCode.substring(checkCode.length()-6);
        }else{
            checkCode = "0";
        }

        String kjje = record.getExcludingTaxPrice();

        if(!StringUtil.isNotNullEmpty(kjje)){
            kjje = "0";
        }
        String command = "python "+pythonPath+" --fplx %s --key %s --fpdm %s --fphm %s --kprq %s --kjje %s --jym %s";


        command = String.format(command,record.getInvoiceType(),record.getId(),record.getInvoiceCode(),record.getInvoiceNumber(),invoiceDate,kjje,checkCode);
        log.debug("查验脚本命令:"+command);
        System.out.println(command);
        PythonScript.executeRestart(command);
        return null;
    }
}
