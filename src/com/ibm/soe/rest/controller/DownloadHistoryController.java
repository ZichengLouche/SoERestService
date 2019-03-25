package com.ibm.soe.rest.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.service.DownloadHistoryService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("downloadHistory")
public class DownloadHistoryController {

	private static Logger logger = Logger.getLogger(DownloadHistoryController.class);
	@Autowired
	private DownloadHistoryService downloadService;
	
	@RequestMapping(value = "/addDownloadHistory", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertDownloadHistory(@RequestBody Map<String, String> param) {
		logger.info("enter addDownloadHistory method with request mapping /addDownloadHistory");
		String key=null;
		try {
			
			key = downloadService.insertDownloadHistory(param);
			
		} catch (Exception e) {
			logger.error("/downloadHistory throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(key);
	}
	
}
