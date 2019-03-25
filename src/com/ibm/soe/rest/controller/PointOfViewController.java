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

import com.ibm.soe.rest.service.PointOfViewService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("pointofview")
public class PointOfViewController {

	private static Logger logger = Logger.getLogger(PointOfViewController.class);
	
	@Autowired
	private PointOfViewService pointOfViewService;
	
	@RequestMapping(value = "/getPointOfViews", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPointOfViews(@RequestBody Map<String, String> param) {
		logger.info("enter getPointOfViews method with request mapping /getPointOfViews");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			String uid = param.get("uid");
			list = pointOfViewService.getPointOfViews(uid);
		} catch (Exception e) {
			logger.error("/getPointOfViews throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/insertPointOfView", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertPointOfView(@RequestBody Map<String, String> param) {
		logger.info("enter insertPointOfView method with request mapping /insertPointOfView");
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = pointOfViewService.insertPointOfView(param);
		} catch (Exception e) {
			logger.error("/insertPointOfView throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/updatePointOfView", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updatePointOfView(@RequestBody Map<String, String> param) {
		logger.info("enter updatePointOfView method with request mapping /updatePointOfView");
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = pointOfViewService.updatePointOfView(param);
		} catch (Exception e) {
			logger.error("/updatePointOfView throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/deletePointOfView", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deletePointOfView(@RequestBody Map<String, String> param) {
		logger.info("enter deletePointOfView method with request mapping /deletePointOfView");
		int deleted = 0;
		try {
			String povId = param.get("povId");
			deleted = pointOfViewService.deletePointOfView(povId);
		} catch (Exception e) {
			logger.error("/deletePointOfView throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(deleted);
	}
	
	@RequestMapping(value = "/getPointOfViewToEdit", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPointOfViewToEdit(@RequestBody Map<String, String> param) {
		logger.info("enter getPointOfViewToEdit method with request mapping /getPointOfViewToEdit");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String povId = param.get("povId");
			map = pointOfViewService.getPointOfViewById(povId);
		} catch (Exception e) {
			logger.error("/getPointOfViewToEdit throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getClientNames", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getClientNames(@RequestBody Map<String, String> param) {
		logger.info("enter getClientNames method with request mapping /getClientNames");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			String uid = param.get("uid");
			list = pointOfViewService.getClientNames(uid);
		} catch (Exception e) {
			logger.error("/getClientNames throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/checkSalesConn", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkSalesConn(@RequestBody Map<String, String> param) {
		logger.info("enter checkSalesConn method with request mapping /checkSalesConn");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String idSalesConn = map.get("idSalesConnect") == null || "".equals(map.get("idSalesConnect")) ? "" : map.get("idSalesConnect");
			String uid = map.get("uid") == null || "".equals(map.get("uid")) ? "" : map.get("uid");
			String povId = map.get("povId") == null || "".equals(map.get("povId")) ? "" : map.get("povId");
			map = pointOfViewService.checkSalesConn(idSalesConn, uid, povId);
		} catch (Exception e) {
			logger.error("/checkSalesConn throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getPOVDetails", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPOVDetails(@RequestBody Map<String, String> param) {
		logger.info("enter getPOVDetails method with request mapping /getPOVDetails");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
//			String uid = param.get("uid");
			String povId = param.get("povId");
			list = pointOfViewService.getPOVDetails(povId);
		} catch (Exception e) {
			logger.error("/getPOVDetails throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/getPOVTopicDetails", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPOVTopicDetails(@RequestBody Map<String, String> param) {
		logger.info("enter getPOVTopicDetails method with request mapping /getPOVTopicDetails");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
//			String uid = param.get("uid");
			String stateId = param.get("stateId");
			String topicId = param.get("topicId");
			map = pointOfViewService.getPOVTopicDetails(stateId, topicId);
		} catch (Exception e) {
			logger.error("/getPOVTopicDetails throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/changeModuleStatus", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> changeModuleStatus(@RequestBody Map<String, String> param) {
		logger.info("enter changeStatus method with request mapping /changeModleStatus");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
//			String stateId = param.get("stateId");
//			String moduleId = param.get("moduleId");
//			String povId = param.get("povId");
			map = pointOfViewService.changeModuleStatus(param);
		} catch (Exception e) {
			logger.error("/changeModleStatus throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/changeTopicStatus", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> changeTopicStatus(@RequestBody Map<String, String> param) {
		logger.info("enter changeStatus method with request mapping /changeTopicStatus");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
//			String stateId = param.get("stateId");
//			String topicId = param.get("topicId");
//			String moduleId = param.get("moduleId");
//			String povId = param.get("povId");
			map = pointOfViewService.changeTopicStatus(param);
		} catch (Exception e) {
			logger.error("/changeTopicStatus throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/saveTopicNotes", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveTopicNotes(@RequestBody Map<String, String> param) {
		logger.info("enter changeStatus method with request mapping /saveTopicNotes");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
//			boolean completed = Boolean.parseBoolean(param.get("completed"));
//			String notes = param.get("notes");
//			String stateId = param.get("stateId");
//			String topicId = param.get("topicId");
//			String moduleId = param.get("moduleId");
//			String povId = param.get("povId");
			map = pointOfViewService.saveTopicNotes(param);
		} catch (Exception e) {
			logger.error("/saveTopicNotes throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getAllNotes", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAllNotes(@RequestBody Map<String, String> param) {
		logger.info("enter getAllNotes method with request mapping /getAllNotes");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			String povId = param.get("povId");
			list = pointOfViewService.getAllNotes(povId);
		} catch (Exception e) {
			logger.error("/getAllNotes throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	
	
}
