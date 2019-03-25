package com.ibm.soe.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.service.AcceleratorsService;

@Controller
@RequestMapping("accelerators")
public class AcceleratorsController {
	private static Logger logger = Logger.getLogger(AcceleratorsController.class);
	
	@Autowired
	private AcceleratorsService acceleratorsService;
	
	@RequestMapping(value = "/initIndustry", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, String>> initIndustry(@RequestBody Map<String, String> param) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		try {
			list = acceleratorsService.getInitIndustry();
		} catch (Exception e) {
			logger.error("/initIndustry throws exception: ", e);
		}
		return list;
	}
	
	@RequestMapping(value = "/initDocType", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, String>> initDocType(@RequestBody Map<String, String> param) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			list = acceleratorsService.getInitDocType();
		} catch (Exception e) {
			logger.error("/initIndustry throws exception: ", e);
		}
		return list;
	}
	
	
}
