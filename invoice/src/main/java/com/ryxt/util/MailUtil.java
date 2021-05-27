package com.ryxt.util;

import com.ryxt.entity.SendMailEntity;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import java.net.URL;
import java.util.Properties;

@Component
public class MailUtil {

	private static String userName;

	private static String password;

	private static String host;

	private static String protocol;

	@Value("${mail.username}")
	public void setUserName(String userName) {
		MailUtil.userName = userName;
	}
	@Value("${mail.password}")
	public void setPassword(String password) {
		MailUtil.password = password;
	}
	@Value("${mail.host}")
	public void setHost(String host) {
		MailUtil.host = host;
	}
	@Value("${mail.protocol}")
	public void setProtocol(String protocol) {
		MailUtil.protocol = protocol;
	}

	public static boolean sendMail(String to, String title, String text) {
		boolean success = false;
		try {
			// 配置  
			Properties prop=new Properties();  
			// 设置邮件服务器主机名，
			prop.put("mail.host",host);
			// 发送邮件协议名称  
			prop.put("mail.transport.protocol", protocol);
			// 是否认证  
			prop.put("mail.smtp.auth", true); 			
			// SSL加密  
		    MailSSLSocketFactory sf = null;  
		    sf = new MailSSLSocketFactory();  
		    // 设置信任所有的主机  
		    sf.setTrustAllHosts(true);  
		    prop.put("mail.smtp.ssl.enable", "true");  
		    prop.put("mail.smtp.ssl.socketFactory", sf);  	
		    
		 // 创建会话对象  
		    Session session = Session.getDefaultInstance(prop, new Authenticator() {  
		      // 认证信息，需要提供"用户账号","授权码"  
		      public PasswordAuthentication getPasswordAuthentication() {  
		    	  return new PasswordAuthentication(userName, password);  
		      }  
		    });  
		    // 是否打印出debug信息  
		    session.setDebug(true);  
		  
		    // 创建邮件  
		    Message message = new MimeMessage(session);  
		    // 邮件发送者  
		    message.setFrom(new InternetAddress(userName));  
		    // 邮件接受者  
		    @SuppressWarnings("static-access")
			InternetAddress[] internetAddressTo = InternetAddress.parse(to);
		    message.setRecipients(Message.RecipientType.TO, internetAddressTo);  
		    // 邮件主题  
		    message.setSubject(title);  
//		    String content = "<html><head></head><body><h1>请点击连接激活</h1><h3><a href='http://localhost:8082/active?code="  
//		        + code + "'>http://localhost:8080/active?code=" + code + "</href></h3></body></html>";  
		    message.setContent(text, "text/html;charset=UTF-8");
		    //message.setText(text);  
		    // Transport.send(message);  
		    // 邮件发送  
		    Transport transport = session.getTransport();  
		    transport.connect();  
		    transport.sendMessage(message, message.getAllRecipients());  
		    transport.close();
			success = true;
		} catch (Exception e) {
			success = false;
		}
		return success;

	}

	public static boolean sendMailWithFile(SendMailEntity sendMailEntity) {
		boolean success = false;
		try {
			//1、连接邮件服务器的参数配置附件名称过长乱码解决，关键词false
			System.setProperty("mail.mime.splitlongparameters","false");
			System.setProperty("mail.mime.charset","UTF-8");
			// 配置
			Properties prop=new Properties();
			// 设置邮件服务器主机名，
			prop.put("mail.host",host);
			// 发送邮件协议名称
			prop.put("mail.transport.protocol", protocol);
			// 是否认证
			prop.put("mail.smtp.auth", true);
			// SSL加密
			MailSSLSocketFactory sf = null;
			sf = new MailSSLSocketFactory();
			// 设置信任所有的主机
			sf.setTrustAllHosts(true);
			prop.put("mail.smtp.ssl.enable", "true");
			prop.put("mail.smtp.ssl.socketFactory", sf);

			// 创建会话对象
			Session session = Session.getDefaultInstance(prop, new Authenticator() {
				// 认证信息，需要提供"用户账号","授权码"
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			});
			// 是否打印出debug信息
			session.setDebug(true);

			// 创建邮件
			Message message = new MimeMessage(session);
			// 邮件发送者
			message.setFrom(new InternetAddress(userName));
			// 邮件接受者
			@SuppressWarnings("static-access")
			InternetAddress[] internetAddressTo = InternetAddress.parse(sendMailEntity.getTo());
			message.setRecipients(Message.RecipientType.TO, internetAddressTo);
			// 邮件主题
			message.setSubject(sendMailEntity.getTitle());
			String downloads = "";
            for(String path:sendMailEntity.getUrls()) {
                String fileName = path.substring(path.lastIndexOf("/") + 1);
                downloads +=  "<a href='"+path+"'>"+fileName+"</a><br>\n";
            }
            String content = "<html>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "    <h1>请点击连接下载附件</h1>\n" +
					"    <h5>三个月内有效</h5>\n" +
                "    <h3>\n" +
                    downloads +
                "    </h3>\n" +
                "    <p>\n" +
               sendMailEntity.getText() +
                "    </p>\n" +
                "</body>\n" +
                "</html>";

			message.setContent(content, "text/html;charset=UTF-8");

//			// 构建一个总的邮件块
//			MimeMultipart mixed = new MimeMultipart("related");
//			// ---> 总邮件快，设置到邮件对象中
//			message.setContent(mixed);
//			MimeBodyPart content = new MimeBodyPart();
//			content.setContent(sendMailEntity.getText(), "text/html;charset=UTF-8");
//			mixed.addBodyPart(content);
//			for(String path:sendMailEntity.getUrls()) {
//				// 右侧： 附件
//				MimeBodyPart right = new MimeBodyPart();
//                /****** 附件 ********/
//                URL url = new URL(path);
//				String fileName = path.substring(path.lastIndexOf("/") + 1);
//				DataHandler attr_handler = new DataHandler(url);
//				right.setDataHandler(attr_handler);
//				fileName = java.net.URLDecoder.decode(fileName, "utf-8");
//				fileName = MimeUtility.encodeText(fileName, "UTF-8", "B");
//				right.setFileName(fileName);
//				// 设置到总邮件块
//				mixed.addBodyPart(right);
//			}
			// 邮件发送
			Transport transport = session.getTransport();
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			success = true;
		} catch (Exception e) {
			success = false;
		}
		return success;

	}
	public static void main(String[] args) {
//		MailUtil.sendMail("lwpking@126.com","测试","测试");
//		MailUtil.sendMailWithFile("liuwenpeng@ryuantech.com","测试","测试",null);
	}
}
