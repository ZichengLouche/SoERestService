package com.ibm.soe.rest.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.service.TemplateService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("pointOfView")
public class TemplateController {

	private static Logger logger = Logger.getLogger(TemplateController.class);
	@Autowired
	private TemplateService templateService;
	
	@RequestMapping(value = "/getTemplateByIndustry", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getTemplateByIndustry(@RequestBody Map<String, String> param) {
		logger.info("enter getTemplateByIndustry method with request mapping /getTemplateByIndustry");
		Map<String,Object> result=null;
		try {
			
			result = templateService.getTemplate(param);
			
		} catch (Exception e) {
			logger.error("/getTemplateByIndustry throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(result);
	}
	
	
	@RequestMapping(value = "/getAllTemplates", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAllTemplates(@RequestBody Map<String, String> param) {
		logger.info("enter getAllTemplates method with request mapping /getAllTemplates");
		Map<String,Object> result=null;
		try {
			
			result = templateService.getTemplate(param);
			
		} catch (Exception e) {
			logger.error("/getAllTemplates throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(result);
	}
	
	
	@RequestMapping(value = "/getAllIndustry", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertDownloadHistory(@RequestBody Map<String, String> param) {
		logger.info("enter getAllIndustry method with request mapping /getAllIndustry");
		Map<String,Object> result=null;
		try {
			
			result = templateService.getTemplate(param);
			
		} catch (Exception e) {
			logger.error("/getAllIndustry throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(result);
	}
	
	
}
