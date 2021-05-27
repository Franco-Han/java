package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* @Description: OCR信息
* @Author: uenpeng
* @Date: 2020/11/12
*/
@Data

public class OcrScanParam {
    /**
     图片地址
     */
    private String url	;
    private String tempFile	;
}
