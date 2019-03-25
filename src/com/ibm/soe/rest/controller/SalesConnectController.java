package com.ibm.soe.rest.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.ibm.soe.rest.service.ExpertGuidanceService;
import com.ibm.soe.rest.service.SalesConnectService;
import com.ibm.soe.rest.util.HttpClientUtil;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("salesConnect")
public class SalesConnectController {

	private static Logger logger = Logger.getLogger(SalesConnectController.class);
	
	@Autowired
	private SalesConnectService salesConnectService;
	
	@RequestMapping(value = "/checkIfUserExsitInSC", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkIfUserExsitInSC(@RequestBody Map<String, String> param) {
		logger.info("enter checkIfUserExsitInSC method with request mapping /checkIfUserExsitInSC");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String uid = param.get("uid");
			map = salesConnectService.checkIfUserExsitInSC(uid);
		} catch (Exception e) {
			logger.error("/checkIfUserExsitInSC throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/reportConnectIssue", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> reportConnectIssue(@RequestBody Map<String, String> param) {
		logger.info("enter reportConnectIssue method with request mapping /reportConnectIssue");
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			map = salesConnectService.reportConnectIssue(param);
		} catch (Exception e) {
			logger.error("/reportConnectIssue throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
}
