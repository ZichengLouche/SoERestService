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
import com.ibm.soe.rest.util.HttpClientUtil;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("expertguidance")
public class ExpertGuidanceController {

	private static Logger logger = Logger.getLogger(ExpertGuidanceController.class);
	
	@Autowired
	private ExpertGuidanceService expertGuidanceService;
	
	@Value("${facedUrl}")
	private String FACE_URL;
	
	@Value("${facePicUrl}")
	private String FACE_PIC_URL;
	
	@RequestMapping(value = "/getClientOpportunities", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getClientOpportunities(@RequestBody Map<String, String> param) {
		logger.info("enter getClientOpportunities method with request mapping /getClientOpportunities");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			String uid = param.get("uid");
			list = expertGuidanceService.getClientOpportunities(uid);
		} catch (Exception e) {
			logger.error("/getClientOpportunities throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/insertClientOpportunities", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertClientOpportunities(@RequestBody Map<String, String> param) {
		logger.info("enter insertClientOpportunities method with request mapping /insertClientOpportunities");
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = expertGuidanceService.insertClientOpportunities(param);
		} catch (Exception e) {
			logger.error("/insertClientOpportunities throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/updateClientOpportunities", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateClientOpportunities(@RequestBody Map<String, String> param) {
		logger.info("enter updateClientOpportunities method with request mapping /updateClientOpportunities");
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = expertGuidanceService.updateClientOpportunities(param);
		} catch (Exception e) {
			logger.error("/updateClientOpportunities throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getClientOpportunityByOppId", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getClientOpportunityByOppId(@RequestBody Map<String, String> param) {
		logger.info("enter getClientOpportunityByOppId method with request mapping /getClientOpportunityByOppId");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String oid = param.get("oid");
			map = expertGuidanceService.getClientOpportunityByOppId(oid);
		} catch (Exception e) {
			logger.error("/getClientOpportunityByOppId throws exception: ", e);
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
			list = expertGuidanceService.getClientNames(uid);
		} catch (Exception e) {
			logger.error("/getClientNames throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/getOppDetails", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getOppDetails(@RequestBody Map<String, String> param) {
		logger.info("enter getOppDetails method with request mapping /getOppDetails");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String uid = param.get("uid");
			String oid = param.get("oid");
			list = expertGuidanceService.getOppDetails(uid, oid);
		} catch (Exception e) {
			logger.error("/getOppDetails throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/changeStatus", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> changeStatus(@RequestBody Map<String, Object> param) {
		logger.info("enter changeStatus method with request mapping /changeStatus");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = expertGuidanceService.changeStatus(param);
		} catch (Exception e) {
			logger.error("/changeStatus throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/checkSalesConn", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkSalesConn(@RequestBody Map<String, String> param) {
		logger.info("enter checkSalesConn method with request mapping /checkSalesConn");
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = expertGuidanceService.checkSalesConn(param);
		} catch (Exception e) {
			logger.error("/checkSalesConn throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/shareOpportunity", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> shareOpportunity(@RequestBody Map<String, String> param) {
		logger.info("enter shareOpportunity method with request mapping /shareOpportunity");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String oppId = param.get("oid");
			String ownerUId = param.get("uid");
			String shareUId = param.get("uid_share");
			map = expertGuidanceService.addShareOppRecord(oppId, ownerUId, shareUId);
		} catch (Exception e) {
			logger.error("/shareOpportunity throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveOppShareWith", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveOppShareWith(@RequestBody Map<String, Object> param) {
		logger.info("enter saveOppShareWith method with request mapping /saveSharewithUsers");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String ownerUId = (String)param.get("uid");
			String oppId = (String) param.get("oid");
			List<String> add = (List<String>) param.get("add");
			List<String> remove = (List<String>) param.get("remove");
			map = expertGuidanceService.updateShareListForOpp(ownerUId, oppId, add, remove);
			
		} catch (Exception e) {
			logger.error("/saveOppShareWith throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	
	@RequestMapping(value = "/checkShareWith", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkShareWith(@RequestBody Map<String, String> param) {
		logger.info("enter checkWhomtoShareWith method with request mapping /checkShareWith");
		int status = 0;
		try {
			String oppId = param.get("oid");
			String shareUId = param.get("uid_share");
			status = expertGuidanceService.checkShareWith(oppId, shareUId);
		} catch (Exception e) {
			logger.error("/checkShareWith throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(status);
	}
	
	@RequestMapping(value = "/getSharedWith", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getSharedWith(@RequestBody Map<String, String> param) {
		logger.info("enter getSharedWith method with request mapping /getSharedWith");
		List<String> list = new ArrayList<String>();
		try {
			String oppId = param.get("oid");
			list = expertGuidanceService.getSharedWith(oppId);
		} catch (Exception e) {
			logger.error("/getSharedWith throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getSharedWithMoreInfo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getSharedWithMoreInfo(@RequestBody Map<String, String> param) {
		logger.info("enter getSharedWithMoreInfo method with request mapping /getSharedWithMoreInfo");
		List<String> list = new ArrayList<String>();
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		try {
			String oppId = param.get("oid");
			list = expertGuidanceService.getSharedWith(oppId);

			for (String email : list) {
				Map<String, String> user = new HashMap<String, String>();
				user.put("email", email);
//				user.put("pic", String.format(FACE_PIC_URL, email, "55"));
				HttpClient httpClient = HttpClientUtil.initHttpClient();
				HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
				RestTemplate template = new RestTemplate(httpFactory);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				Map<String, String> map = new HashMap<String, String>();
				HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
				String result = template.postForObject(String.format(FACE_URL, email), request, String.class);
				
		        JSONArray jsonArray = JSONArray.fromObject(result);
		        List<Map<String,Object>> mapListJson = (List)jsonArray;
		        for(Map<String,Object> obj : mapListJson){
		            for(Entry<String, Object> entry : obj.entrySet()){
		                String strkey1 = entry.getKey();
		                if (strkey1.equals("name")){
		                	String name = entry.getValue().toString();
		                	user.put("name", name);
		                }
		                if(strkey1.equals("bio")){
		                	String title = entry.getValue().toString();
		                	user.put("title", title);
		                }
		                if(strkey1.equals("uid")){
		                	String uid = entry.getValue().toString();
		                	user.put("pic", String.format(FACE_PIC_URL, uid, "50"));
		                }
		            }  
		        }
		        if (mapListJson.size() > 0) {
			        results.add(user);
		        }
			}
	        Collections.sort(results, new Comparator<Map<String, String>>() {
	            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
	            	return arg0.get("name").compareTo(arg1.get("name"));
	            }
	        });
		} catch (Exception e) {
			logger.error("/getSharedWithMoreInfo throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	@RequestMapping(value = "/getSharedBy", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getSharedBy(@RequestBody Map<String, String> param) {
		logger.info("enter getSharedBy method with request mapping /getSharedBy");
		List<String> list = new ArrayList<String>();
		try {
			String oppId = param.get("oid");
			String shareUId = param.get("uid_share");
			list = expertGuidanceService.getSharedBy(oppId, shareUId);
		} catch (Exception e) {
			logger.error("/getSharedBy throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/getOppDetailById", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getOppDetailById(@RequestBody Map<String, String> param) {
		logger.info("enter getOppDetailById method with request mapping /getOppDetailById");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String sid = param.get("sid");
			map = expertGuidanceService.getOppDetailById(sid);
		} catch (Exception e) {
			logger.error("/getOppDetailById throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getSalesConnectOpps", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getSalesConnectOpps(@RequestBody Map<String, String> param) {
		logger.info("enter getClientOpportunities method with request mapping /getSalesConnectOpps");
		Map<String, Object> results = new HashMap<String, Object>();
		try {
			String uid = param.get("uid");
			if (param.get("pageSize") == null || "".equals(param.get("pageSize"))) {
				//query All SC OPPs
				results = expertGuidanceService.getSalesConnectOpps(uid);
			} else {
				//query one page of SC OPPs
				results = expertGuidanceService.getSalesConnectOpps(uid, param);
			}
		} catch (Exception e) {
			logger.error("/getSalesConnectOpps throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	@RequestMapping(value = "/addAllSalesConnectOpps", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> addAllSalesConnectOpps(@RequestBody Map<String, Object> param) {
		logger.info("enter addAllSalesConnectOpps method with request mapping /addAllSalesConnectOpps");
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		try {
			String uid = (String) param.get("uid");
			List scOpps = (List) param.get("opps");
			List<String> oids = expertGuidanceService.addAllSalesConnectOpps(scOpps, uid);
			results = expertGuidanceService.getTheClientOpps(uid, oids);
		} catch (Exception e) {
			logger.error("/addAllSalesConnectOpps throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	@RequestMapping(value = "/addSalesConnectOpp", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> addSalesConnectOpp(@RequestBody Map<String, String> param) {
		logger.info("enter addSalesConnectOpp method with request mapping /addSalesConnectOpp");
		Map<String, String> results = new HashMap<String, String>();
		try {
			String uid = (String) param.get("uid");
			String oid = expertGuidanceService.addSalesConnectOpp(param, uid);
			results = expertGuidanceService.getClientOpportunity(uid, oid);
		} catch (Exception e) {
			logger.error("/addAllSalesConnectOpps throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	@RequestMapping(value = "/associateSalesConnectOpp", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> associateSalesConnectOpp(@RequestBody Map<String, String> param) {
		logger.info("enter associateSalesConnectOpp method with request mapping /associateSalesConnectOpp");
		Map<String, String> results = new HashMap<String, String>();
		try {
			String uid = param.get("uid");
			String oid = param.get("oid");
			expertGuidanceService.associateSalesConnectOpp(param, oid, uid);
			results = expertGuidanceService.getClientOpportunity(uid, oid);
		} catch (Exception e) {
			logger.error("/associateSalesConnectOpp throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	@RequestMapping(value = "/getTeamSDNextOpps", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getTeamSDNextOpps(@RequestBody Map<String, String> param) {
		logger.info("enter getTeamSDNextOpps method with request mapping /getTeamSDNextOpps");
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		try {
			String uid = param.get("uid");
			results = expertGuidanceService.getTeamSDNextClientOpps(uid);
		} catch (Exception e) {
			logger.error("/getTeamSDNextOpps throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	@RequestMapping(value = "/getGuidanceQuestions", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getGuidanceQuestions(@RequestBody Map<String, String> param) {
		logger.info("enter getGuidanceQuestions method with request mapping /getGuidanceQuestions");
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		try {
			String idTask = param.get("tid");
			String idState = param.get("sid");
			results = expertGuidanceService.getGuidanceQuestions(idTask, idState);
		} catch (Exception e) {
			logger.error("/getGuidanceQuestions throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	
	
	
	
	
	// Remove Opportunity from the List start by LiYang
	@RequestMapping(value = "/deleteOpp", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteOpp(@RequestBody Map<String, String> param) {
		logger.info("enter deleteOpp method with request mapping /deleteOpp");
		int results = 0;
		try {
			results = expertGuidanceService.deleteOpp(param);
		} catch (Exception e) {
			logger.error("/deleteOpp throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	@RequestMapping(value = "/archiveOpp", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> archiveOpp(@RequestBody Map<String, String> param) {
		logger.info("enter archiveOpp method with request mapping /archiveOpp");
		int results = 0;
		try {
			results = expertGuidanceService.archiveOpp(param);
		} catch (Exception e) {
			logger.error("/archiveOpp throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	@RequestMapping(value = "/retrieveOpps", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> retrieveOpps(@RequestBody Map<String, String> param) {
		logger.info("enter retrieveOpps method with request mapping /retrieveOpps");
		int results = 0;
		try {
			results = expertGuidanceService.retrieveOpp(param);
		} catch (Exception e) {
			logger.error("/retrieveOpps throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	@RequestMapping(value = "/getArchiveOpps", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getArchiveOpps(@RequestBody Map<String, String> param) {
		logger.info("enter getArchiveOpps method with request mapping /getArchiveOpps");
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		try {
			String idUser = param.get("UID");
			results = expertGuidanceService.getArchiveOpps(idUser);
		} catch (Exception e) {
			logger.error("/getArchiveOpps throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	// Remove Opportunity from the List end by LiYang
	
	//Transfer opportunity
	@RequestMapping(value = "/transferOpp", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> transferOpp(@RequestBody Map<String, String> param) {
		logger.info("enter transferOpps method with request mapping /shareOpportunity");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String oppId = param.get("oid");
			String ownerUId = param.get("uid");
			String toUid = param.get("to_uid");
			String type = param.get("type");
			map = expertGuidanceService.transferOpps(oppId, ownerUId, toUid, type);
		} catch (Exception e) {
			logger.error("/transferOpps throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}
	
	@RequestMapping(value = "/getAllTransferOpps", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAllTransferOpps(@RequestBody Map<String, String> param) {
		logger.info("enter getAllTransferOpps method with request mapping /getAllTransferOpps");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String uid = param.get("uid");
			List<Map<String, String>> opps = expertGuidanceService.getAllTransferOpps(uid);
			List<Map<String, String>> clients = expertGuidanceService.getUserClientNames(uid);
			Map<String, String> first = clients.get(0);
			first.put("CNM", "Select one");
			first.put("CID", "0");
			map.put("opps", opps);
			map.put("clients", clients);
		} catch (Exception e) {
			logger.error("/getAllTransferOpps throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(map);
	}

	@RequestMapping(value = "/getAllTransferOppCount", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAllTransferOppCount(@RequestBody Map<String, String> param) {
		logger.info("enter getAllTransferOppCount method with request mapping /getAllTransferOppCount");
		int results = 0;
		try {
			String uid = param.get("uid");
			expertGuidanceService.updateTransferOppStatus(uid);
			results = expertGuidanceService.getAllTransferOppCount(uid);
		} catch (Exception e) {
			logger.error("/getAllTransferOppCount throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	@RequestMapping(value = "/processTransferOpp", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> processTransferOpp(@RequestBody Map<String, String> param) {
		logger.info("enter processTransferOpp method with request mapping /processTransferOpp");
		int results = -1;
		try {
			expertGuidanceService.processTransferOpp(param);
			results = 0;
		} catch (Exception e) {
			logger.error("/processTransferOpp throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}

	@RequestMapping(value = "/updateTransferOppStatus", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateTransferOppStatus(@RequestBody Map<String, String> param) {
		logger.info("enter updateTransferOppStatus method with request mapping /updateTransferOppStatus");
		int result = -1;
		try {
			String uid = param.get("uid");
			result = expertGuidanceService.updateTransferOppStatus(uid);
		} catch (Exception e) {
			logger.error("/updateTransferOppStatus throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(result);
	}
	
	// get client identifier from GBG & GC start
	@RequestMapping(value = "/getClientIdentifier", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getClientIdentifier(@RequestBody Map<String, String> param) {
		logger.info("enter getClientIdentifier method with request mapping /getClientIdentifier");
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		long st = 0 ;
		long totalTime = 0;
		try {
			String clientName = param.get("clientName");
			st = System.currentTimeMillis();
			results = expertGuidanceService.getClientIdentifier(clientName);
			totalTime = (System.currentTimeMillis() - st) / 1000; 
		} catch (Exception e) {
			logger.error("/getClientIdentifier throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(results);
	}
	
	// get client identifier from GBG & GC end
}
