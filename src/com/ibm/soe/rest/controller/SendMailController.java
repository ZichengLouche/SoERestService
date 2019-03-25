package com.ibm.soe.rest.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.util.MailUtil;

@Controller
@RequestMapping("mail")
public class SendMailController {

	private static Logger logger = Logger.getLogger(SendMailController.class);
	
	@Autowired
	private MailUtil mailUtil;
	
	@RequestMapping(value = "/send", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody void send(@RequestBody Map<String, String> param) {
		
		try {
			String toEmail = param.get("toEmail");
			String fromEmail = param.get("fromEmail");
			String subject = param.get("subject");
			String msgHtml = param.get("msgHtml");
			String hostUrl = param.get("msgText");
			
			mailUtil.sendMail(toEmail, fromEmail, subject, msgHtml, hostUrl);

		} catch (Exception e) {
			logger.error(e);
		}
		
	}
	
	
}
