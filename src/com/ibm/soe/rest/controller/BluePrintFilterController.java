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

import com.ibm.soe.rest.service.BluePrintFilterService;
import com.ibm.soe.rest.service.BluePrintResultService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("blueprintfilter")
public class BluePrintFilterController {
	private static Logger logger = Logger.getLogger(BluePrintFilterController.class);
	
	@Autowired
	private BluePrintFilterService bluePrintFilterService;
	
	// Andy 2016.9.7 21:34
	@Autowired
	private BluePrintResultService bluePrintResultService;
	
	
	@RequestMapping(value = "/getIndustries", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getIndustries(@RequestBody Map<String, String> param) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String[] parameters = bluePrintResultService.getParameters(param);
			Object[] sqlParamter = bluePrintResultService.getCommonSqlParamter(parameters[0], parameters[1] , parameters[8], null, null, parameters[5], parameters[6], parameters[7], parameters[9], parameters[10]);
			
			list = bluePrintFilterService.getIndustries(sqlParamter);
		} catch (Exception e) {
			logger.error("/getIndustries throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/getImperatives", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getImperatives(@RequestBody Map<String, String> param) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String[] parameters = bluePrintResultService.getParameters(param);
			Object[] sqlParamter = bluePrintResultService.getCommonSqlParamter(parameters[0], parameters[1], parameters[2], null, parameters[4], parameters[5], parameters[6], parameters[7], parameters[9], parameters[10]);
			
			list = bluePrintFilterService.getImperatives(sqlParamter);
		} catch (Exception e) {
			logger.error("/getImperatives throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/getSolutions", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getSolutions(@RequestBody Map<String, String> param) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String[] parameters = bluePrintResultService.getParameters(param);
			Object[] sqlParamter = bluePrintResultService.getCommonSqlParamter(parameters[0], parameters[1], parameters[2], parameters[3], null, parameters[5], parameters[6], parameters[7], parameters[9], parameters[10]);
			
			list = bluePrintFilterService.getSolutions(sqlParamter);
		} catch (Exception e) {
			logger.error("/getSolutions throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list); 
	}
	
	@RequestMapping(value = "/getBuyers", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getBuyers(@RequestBody Map<String, String> param) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String[] parameters = bluePrintResultService.getParameters(param);
			Object[] sqlParamter = bluePrintResultService.getCommonSqlParamter(parameters[0], parameters[1], parameters[2], parameters[3], parameters[4], null, parameters[6], parameters[7], parameters[9], parameters[10]);
			
			list = bluePrintFilterService.getBuyers(sqlParamter);
		} catch (Exception e) {
			logger.error("/getBuyers throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/getGeographies", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getGeographies(@RequestBody Map<String, String> param) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String[] parameters = bluePrintResultService.getParameters(param);
			Object[] sqlParamter = bluePrintResultService.getCommonSqlParamter(parameters[0], parameters[1], parameters[2], parameters[3], parameters[4], parameters[5], "All", "0", parameters[9], parameters[10]);
			
			list = bluePrintFilterService.getGeographies(sqlParamter);
		} catch (Exception e) {
			logger.error("/getGeographies throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	
	
}
