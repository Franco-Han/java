package com.ryxt.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value="获取二维码返回信息实体")
@Data
public class QrInfoOutputBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "二维码文件（base64编码字符串）")
	private String image;

}
