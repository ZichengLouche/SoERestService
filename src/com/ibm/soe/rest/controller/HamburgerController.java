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

import com.ibm.soe.rest.service.HamburgerService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("hamburger")
public class HamburgerController {

	private static Logger logger = Logger.getLogger(HamburgerController.class);
	
	@Autowired
	private HamburgerService hamburgerService;
	
	@RequestMapping(value = "/getHelpContent", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getHelpContent(@RequestBody Map<String, String> param) {
		logger.info("enter getHelpContent method with request mapping /getHelpContent");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = hamburgerService.getHelpContent();
		} catch (Exception e) {
			logger.error("/getHelpContent throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}

	@RequestMapping(value = "/getAboutContent", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAboutContent(@RequestBody Map<String, String> param) {
		logger.info("enter getAboutContent method with request mapping /getAboutContent");
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = hamburgerService.getAboutContent();
		} catch (Exception e) {
			logger.error("/getAboutContent throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
}
