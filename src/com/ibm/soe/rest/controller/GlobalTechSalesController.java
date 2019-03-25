package com.ibm.soe.rest.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.service.GlobalTechSalesService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("globaltechsales")
public class GlobalTechSalesController {

	private static Logger logger = Logger.getLogger(GlobalTechSalesController.class);
	
	@Autowired
	private GlobalTechSalesService globalTechSalesService;
	
	@RequestMapping(value = "/getAllWebNavigators", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAllWebNavigators(@RequestBody Map<String, String> param) {
		logger.info("enter getAllWebNavigators method with request mapping /getAllWebNavigators");
		Map<String, Object> results = new HashMap<String, Object>();
		try {
			results = globalTechSalesService.getAllWebNavigators();
		} catch (Exception e) {
			logger.error("/getAllWebNavigators throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
}
