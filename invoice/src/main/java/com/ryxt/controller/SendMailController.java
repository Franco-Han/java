package com.ryxt.controller;


import com.ryxt.entity.SendMailEntity;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.MailUtil;
import org.springframework.web.bind.annotation.*;

/**
* @Description: 发送邮件
* @Author: uenpeng
* @Date: 2020/11/3
*/
@RestController
@RequestMapping(value = "mail")
public class SendMailController {
    @PostMapping("/sendMail")
    public AjaxResponse<Object> sendMail(@RequestBody SendMailEntity sendMailEntity) {
        boolean success = MailUtil.sendMailWithFile(sendMailEntity);
        return AjaxResponse.success(success);
    }
}
