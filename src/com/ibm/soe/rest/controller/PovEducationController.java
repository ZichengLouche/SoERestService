package com.ibm.soe.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.service.PovEducationService;
import com.ibm.soe.rest.util.RestUtil;
import com.ibm.soe.rest.util.ViewAllNotes;

@Controller
@RequestMapping("poveducation")
public class PovEducationController {

	private static Logger logger = Logger.getLogger(PovEducationController.class);
	
	@Autowired
	private PovEducationService povEducationService;
	@Autowired
	private ViewAllNotes viewAllNotes;

	private Map<String, String> taskIds = new ConcurrentHashMap<String, String>();
	private Map<String, String> egTaskIds = new ConcurrentHashMap<String, String>();
	
	@RequestMapping(value = "/getMainResults", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPovMainResults(@RequestBody Map<String, String> param) {
		logger.info("enter getPovMainResults method with request mapping /getMainResults");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String idUser = param.get("idUser");
			list = povEducationService.getPovMainResults(idUser);
		} catch (Exception e) {
			logger.error("/getMainResults throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/getTopicDetails", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPovTopicDetails(@RequestBody Map<String, String> param) {
		logger.info("enter getPovTopicDetails method with request mapping /getTopicDetails");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String idTopic = param.get("idTopic");// to query the resource title list
			String idState = param.get("idState");// to query the notes
			map = povEducationService.getPovTopicDetails(idTopic, idState);
		} catch (Exception e) {
			logger.error("/getTopicDetails throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getResourceContent", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPovResourceContent(@RequestBody Map<String, String> param) {
		logger.info("enter getPovResourceContent method with request mapping /getResourceContent");
		String richContent = "";
		try {
			String idResource = param.get("idResource");
			richContent = povEducationService.getPovResourceContent(idResource);
		} catch (Exception e) {
			logger.error("/getResourceContent throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(richContent);
	}
	
	@RequestMapping(value = "/checkModule", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkModule(@RequestBody Map<String, String> param) {
		logger.info("enter checkModule method with request mapping /checkModule");
		Map<String, String> moduleInfo = new HashMap<String, String>();
		try {
			String idUser = param.get("idUser");
			String topicStates = param.get("topicStates");
			String status = param.get("status");
			povEducationService.checkModule(idUser, topicStates, status);
			
			/* to refresh module information */
			String idModule = param.get("idModule");
			moduleInfo = povEducationService.getModuleState(idUser, idModule);
		} catch (Exception e) {
			logger.error("/deletePointOfView throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(moduleInfo);
	}
	
	@RequestMapping(value = "/checkTopic", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkTopic(@RequestBody Map<String, String> param) {
		logger.info("enter checkTopic method with request mapping /checkTopic");
		logger.info("enter checkModule method with request mapping /checkModule");
		Map<String, String> moduleInfo = new HashMap<String, String>();
		try {
			String idUser = param.get("idUser");
			String idState = param.get("idState");
			String idTopic = param.get("idTopic");
			String status = param.get("status");
			povEducationService.checkTopic(idUser, idState, idTopic, status);
			
			String idModule = param.get("idModule");
			moduleInfo = povEducationService.getModuleState(idUser, idModule);
		} catch (Exception e) {
			logger.error("/checkTopic throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(moduleInfo);
	}
	
	@RequestMapping(value = "/writeTopicNotes", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> writeTopicNotes(@RequestBody Map<String, String> param) {
		logger.info("enter writeTopicNotes method with request mapping /writeTopicNotes");
		Map<String, String> moduleInfo = new HashMap<String, String>();
		try {
			String idUser = param.get("idUser");
			String idState = param.get("idState");
			String idTopic = param.get("idTopic");
			String notes = param.get("notes");
			String status = param.get("status");
			povEducationService.writeTopicNotes(idUser, idState, idTopic, notes, status);
			
			String idModule = param.get("idModule");
			moduleInfo = povEducationService.getModuleState(idUser, idModule);
		} catch (Exception e) {
			logger.error("/writeTopicNotes throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(moduleInfo);
	}
	
	
	@RequestMapping(value = "/getViewAllNotes", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getViewAllNotes(@RequestBody Map<String, String> param) {
		logger.info("enter getViewAllNotes method with request mapping /getViewAllNotes");
		Map<String, String> moduleInfo = new HashMap<String, String>();

		String fromEmail = param.get("uid");
		String toEmail = param.get("toEmail");
		String type = param.get("type");
		String oppID = param.get("oppID");
		
		if("EG".equals(type)){
			if (egTaskIds.containsKey(fromEmail)) {
				moduleInfo.put("mailOK", "sending");
			} else {
				moduleInfo.put("mailOK", "sentforawhile");
				egTaskIds.put(fromEmail, "");
				this.viewAllNotes.handleViewhandleViewAllNotesAsync(fromEmail, toEmail, povEducationService, egTaskIds, type, oppID);
			}
		}else{
			if (taskIds.containsKey(fromEmail)) {
				moduleInfo.put("mailOK", "sending");
			} else {
				moduleInfo.put("mailOK", "sentforawhile");
				taskIds.put(fromEmail, "");
				this.viewAllNotes.handleViewhandleViewAllNotesAsync(fromEmail, toEmail, povEducationService, taskIds);
			}
		}

		return RestUtil.handleResult(moduleInfo);
	}
	
	
//	@RequestMapping(value = "/getViewAllNotes", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
//	public @ResponseBody Map<String, Object> getViewAllNotes(@RequestBody Map<String, String> param) {
//		logger.info("enter getViewAllNotes method with request mapping /getViewAllNotes");
//		Map<String, String> moduleInfo = new HashMap<String, String>();
//		List<Map<String, String>> allNotesList = null;
//		try {
//			String fromEmail = param.get("uid");
//			String toEmail = param.get("toEmail");
//			allNotesList = povEducationService.getViewAllNotes(fromEmail);
//			String mailOK = viewAllNotes.handleViewAllNotes(allNotesList, fromEmail, toEmail);
//			moduleInfo.put("mailOK", mailOK);
//		} catch (Exception e) {
//			logger.error("/getViewAllNotes throws exception: ", e);
//			return RestUtil.handleError(e.getMessage());
//		}
//		return RestUtil.handleResult(moduleInfo);
//	}
	
}
