package com.ibm.soe.rest.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.service.CommFunctionService;
import com.ibm.soe.rest.util.DOM4JUtil;
import com.ibm.soe.rest.util.MailUtil;

@Controller
@RequestMapping("task")
@Component
public class RedbooksTask {
	private static Logger logger = Logger.getLogger(RedbooksTask.class);
	
	@Value("${DATA_Fail_email}")
	private String emailList;
	
	@Value("${fromEmail}")
	private String fromEmail;
	
	@Value("${hostName}")
	private String hostName;
	
	@Value("${TABLE_REDBOOKS}")
	private String TABLE_REDBOOKS;
	
	@Value("${XML_FILE_PATH_REDBOOKS}")
	private String XML_FILE_PATH_REDBOOKS;
	
	@Autowired
	private CommFunctionService commFunctionService;
	
	@Autowired
	private MailUtil mailUtil;
	
	
//	@Scheduled(cron="* 49 16 ? * *")
	public void autoUpdateRedbooksByLoadData() {
		autoUpdateRedbooksByLoadDataCommon();
	}
	
	@RequestMapping(value = "/autoUpdateRedbooksByLoadData/{name}", produces="application/json;charset=UTF-8", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> autoUpdateRedbooksByLoadData(@PathVariable("name") String param) {
		return autoUpdateRedbooksByLoadDataCommon();
	}

	private Map<String, Object> autoUpdateRedbooksByLoadDataCommon() {
		Map<String, Object> resultMessageMap = new HashMap<String, Object>();
		boolean result = false;
		long begin = Calendar.getInstance().getTimeInMillis();
		long getXmlTime = 0;
		
		try {
			String filePath = DOM4JUtil.getFilePath(XML_FILE_PATH_REDBOOKS);
			getXmlTime = (Calendar.getInstance().getTimeInMillis() - begin) / 1000;
			
			if(filePath != null && !"".equals(filePath)) {
				result = commFunctionService.autoUpdateRedbooks(TABLE_REDBOOKS, filePath.replace('\\', '/'));
			}
		} catch (Exception e) {
			// Andy 2016.6.2 21:32
			logger.error("Automatic update Redbooks by load data occured exception", e);
			mailUtil.sendMultipleEmails("DataSynchronizationException:Automatic update Redbooks by load data occured exception", "Detail exception:" + e.getMessage());
		}
		
		long usedTime = (Calendar.getInstance().getTimeInMillis() - begin) / 1000;
		String resultMessage = "execute autoUpdateRedbooksByLoadData " + (result == true ? "Successfully" : "Failure") + ". return:" + result + ", usedTime:" + usedTime 
								+ "s, getXmlTime:" + getXmlTime + "s";
		logger.info(resultMessage);
		resultMessageMap.put("result", resultMessage);
		
		mailUtil.sendMultipleEmails("DataSynchronization" + (result == true ? "Successfully" : "Failure") + ":Redbooks", "Detail message: " + resultMessage);
		return resultMessageMap;
	}
	
	// Andy 2016.4.5 18:56
//	@Scheduled(cron="0 0 2 ? * *")
	public void autoUpdateRedbooksByParseXmlFile() {
		autoUpdateRedbooksByParseXmlFileCommon();
	}
	
	@RequestMapping(value = "/autoUpdateRedbooksByParseXmlFile/{name}", produces="application/json;charset=UTF-8", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> autoUpdateRedbooksByParseXmlFile(@PathVariable("name") String param) {
		return autoUpdateRedbooksByParseXmlFileCommon();
	}

	private Map<String, Object> autoUpdateRedbooksByParseXmlFileCommon() {
		Map<String, Object> resultMessageMap = new HashMap<String, Object>();
		boolean result = false;
		long begin = Calendar.getInstance().getTimeInMillis();
		long parseXmlTime = 0;
		int totalData = 0;
		
		try {
			List<Element> childElements = DOM4JUtil.getChildElements(XML_FILE_PATH_REDBOOKS);
			parseXmlTime = (Calendar.getInstance().getTimeInMillis() - begin) / 1000;
			totalData = childElements.size();
			
			if(childElements != null && totalData > 0) {
				result = commFunctionService.autoBatchUpdateRedbooks("redbooks", childElements);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Automatic batch update Redbooks data Exception", e);
			mailUtil.sendMultipleEmails("DataSynchronizationException:Automatic update Redbooks by parse xml file occured exception", "Detail exception:" + e.getMessage());
		}
		
		long usedTime = (Calendar.getInstance().getTimeInMillis() - begin) / 1000;
		String resultMessage = "execute autoUpdateRedbooksByParseXmlFile " + (result == true ? "successfully" : "failure") +  ". totalData:" + totalData + ", usedTime:" + usedTime 
								+ "s, parseXmlTime:" + parseXmlTime + "s, dataVersion:" + DOM4JUtil.DATA_VERSION;
		logger.info(resultMessage);
		resultMessageMap.put("result", resultMessage);
		
		// Andy 2016.6.2 21:32
		mailUtil.sendMultipleEmails("DataSynchronization" + (result == true ? "Successfully" : "Failure") + ":Redbooks", new Date().toLocaleString() + " Detail message: " + resultMessage);
		return resultMessageMap;
	}
	
	
}
