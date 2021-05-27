package com.ryxt.base.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ryxt.base.entity.QrInfoOutputBean;
import com.ryxt.base.entity.QrLoginInfoOutputBean;
import com.ryxt.base.util.AES;
import com.ryxt.base.util.AES256EncryptionUtil;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.BaseAuthUtil;
import com.ryxt.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
* @Description: 二维码扫描登录
* @Author: uenpeng
* @Date: 2020/10/15
*/
@SuppressWarnings({"unchecked","rawtypes"})
@Api(value = "二维码扫描登录", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController("OauthLoginQrController")
@RequestMapping("/oauth/login/qr")
public class OauthLoginQrController {
	
	private static final String CHARACTER_SET = "UTF-8";
	private static final int QR_WIDTH = 300;
	private static final int QR_HEIGHT = 300;
	
	private static String QR_REDIS_KEY_PREFIX = "_rq_login_uuid_";

	@Value("${server.host}")
	private String host;

	@Value(":${server.port}")
	private String port;

	@Value("${server.servlet.context-path}")
	private String serverName;

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate redisTemplate;
	
	@Autowired
	private TokenStore tokenStore;
	
	/**
	 * 获取二维码
	 * @throws Exception 
	 */
	@ApiOperation(value = "获取二维码")
	@RequestMapping(value = "/getQrCode", method = RequestMethod.POST)
	public AjaxResponse<QrInfoOutputBean> getQrCode(){
		boolean success = true;
		int statusCode = HttpStatus.OK.value();
		String msg = "";
		
		String uuid = RandomStringUtils.randomAlphanumeric(32);
		String qrCode = "";

		String loginUrl = host+port+serverName;
		Map<String, String> qrData = new HashMap<String, String>();
		qrData.put("uuid", uuid);
		qrData.put("host", loginUrl);
		qrData.put("token", BaseAuthUtil.getToken());
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARACTER_SET);
		hints.put(EncodeHintType.MARGIN, 1);

		try {
			/** 生成二维码 **/
				String content = JsonUtil.toJson(qrData);
			content = new String(AES256EncryptionUtil.encrypt(content,"5278525586982485"));

			AES256EncryptionUtil.decrypt(content,"5278525586982485");
				BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);

				/** 转换为图片 **/
				int width = bitMatrix.getWidth();
				int height = bitMatrix.getHeight();
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
					}
	        }
	        
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        ImageIO.write(image, "PNG", out);
	        
	        /** 生成二位码文件字符串 **/
	        qrCode = Base64.encodeBase64String(out.toByteArray());
	        
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			statusCode = HttpStatus.BAD_REQUEST.value();
			msg = "生成二维码异常！"+e.getMessage();
		}
		AjaxResponse<QrInfoOutputBean> aRes = new AjaxResponse<QrInfoOutputBean>();
		aRes.setSuccess(success);
		aRes.setMessage(msg);
		aRes.setStatusCode(statusCode);
		
		if(success) {
			QrInfoOutputBean qrInfoOutputBean = new QrInfoOutputBean();
			qrInfoOutputBean.setImage(qrCode);
			aRes.setData(qrInfoOutputBean);
		}
		
		return aRes;
	}
}
