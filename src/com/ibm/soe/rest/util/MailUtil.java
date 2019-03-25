package com.ibm.soe.rest.util;

import java.net.URL;

import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailUtil {
	private static Logger logger = Logger.getLogger(MailUtil.class);
	
	@Value("${emailList}")
	private String emailList;
	
	@Value("${fromEmail}")
	private String fromEmail;
	
	@Value("${hostName}")
	private String hostName;
	
	@Async
	public void sendMail(String toEmail, String fromEmail, String subject, String msgHtml, String hostUrl) {
		try {
			ImageHtmlEmail email = new ImageHtmlEmail();
			email.setHostName("na.relay.ibm.com");
			// email.setHostName("relay.us.ibm.com");
			// email.setHostName("relay.uk.ibm.com");
			
			if (hostUrl != null && !"".endsWith(hostUrl) && hostUrl.indexOf("http") != -1) {
				URL url = new URL(hostUrl);
				email.setDataSourceResolver(new DataSourceUrlResolver(url));
			}

			email.addTo(toEmail);
			email.setFrom(fromEmail);
			email.setSubject(subject);
			
			email.setHtmlMsg(msgHtml);
			email.send();
		}  catch (Exception e) {
			logger.error(e);
		}
	}
	
	// Andy 2016.6.2 21:32
	public void sendMultipleEmails(String subject, String msg) {
		String[] emaillist = emailList.split(",");
		
		for(int i=0;i<emaillist.length;i++) {
			this.sendMail(emaillist[i], fromEmail, subject, "hostName:" + hostName + ", " + msg, null);
		}
	}
	
}
