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

import com.ibm.soe.rest.service.CommFunctionService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("common")
public class CommFunctionController {
	private static Logger logger = Logger.getLogger(CommFunctionController.class);
	
	@Autowired
	private CommFunctionService commFunctionService;
	
	@RequestMapping(value = "/getMyProfile", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getMyProfile(@RequestBody Map<String, String> param) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String uid = param.get("uid");
			map = commFunctionService.getMyProfile(uid);
		} catch (Exception e) {
			logger.error("/getMyProfile throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getDropDownListData", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getDropDownListData(@RequestBody Map<String, String> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = commFunctionService.getDropDownListData();
		} catch (Exception e) {
			logger.error("/getDropDownListData throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/insertMyProfile", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertMyProfile(@RequestBody Map<String, String> param) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = commFunctionService.insertMyProfile(param);
		} catch (Exception e) {
			logger.error("/insertMyProfile throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/updateMyProfile", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateMyProfile(@RequestBody Map<String, String> param) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = commFunctionService.updateMyProfile(param);
		} catch (Exception e) {
			logger.error("/updateMyProfile throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/insertLoginHistory", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertLoginHistory(@RequestBody Map<String, String> param) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = commFunctionService.insertLoginHistory(param);
		} catch (Exception e) {
			logger.error("/insertLoginHistory throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
}
