package com.ibm.soe.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.service.ExpertGuidanceExampleOppService;
import com.ibm.soe.rest.service.ExpertGuidanceService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("expertguidanceExampleOpp")
public class ExpertGuidanceExampleOppController {

	private static Logger logger = Logger.getLogger(ExpertGuidanceExampleOppController.class);
	
	@Autowired
	private ExpertGuidanceExampleOppService expertGuidanceExampleOppService;
	
	@RequestMapping(value = "/checkIfHaveOwnOpp", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkIfHaveOwnOpp(@RequestBody Map<String, String> param) {
		logger.info("enter checkIfHaveOwnOpp method with request mapping /checkIfHaveOwnOpp");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String idUser = param.get("uid");
			String idOpp = param.get("oid");
			map = expertGuidanceExampleOppService.checkIfHaveOwnOpp(idUser, idOpp);
		} catch (Exception e) {
			logger.error("/checkIfHaveOwnOpp throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/viewExampleOpp", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> viewExampleOpp(@RequestBody Map<String, String> param) {
		logger.info("enter viewExampleOpp method with request mapping /viewExampleOpp");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String idOpp = param.get("oid");
			map = expertGuidanceExampleOppService.viewExampleOpp(idOpp);
		} catch (Exception e) {
			logger.error("/viewExampleOpp throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getExampleOppDetails", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getExampleOppDetails(@RequestBody Map<String, String> param) {
		logger.info("enter getExampleOppDetails method with request mapping /getExampleOppDetails");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String idUser = param.get("uid");
			String idOpp = param.get("oid");
			map = expertGuidanceExampleOppService.getExampleOppDetails(idUser, idOpp);
		} catch (Exception e) {
			logger.error("/getExampleOppDetails throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getATNotesById", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getATNotesById(@RequestBody Map<String, String> param) {
		logger.info("enter getATNotesById method with request mapping /getATNotesById");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String idState = param.get("sid");
			map = expertGuidanceExampleOppService.getATNotesById(idState);
		} catch (Exception e) {
			logger.error("/getATNotesById throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getGuidanceQuestions", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getGuidanceQuestions(@RequestBody Map<String, String> param) {
		logger.info("enter getGuidanceQuestions method with request mapping /getGuidanceQuestions");
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		try {
			String idTask = param.get("tid");
			String idState = param.get("sid");
			results = expertGuidanceExampleOppService.getGuidanceQuestions(idTask, idState);
		} catch (Exception e) {
			logger.error("/getGuidanceQuestions throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
}
