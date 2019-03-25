package com.ibm.soe.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.ibm.soe.rest.service.BluePrintResultService;
import com.ibm.soe.rest.util.HttpClientUtil;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("blueprintresult")
public class BluePrintResultController {
	private static Logger logger = Logger.getLogger(BluePrintResultController.class);
	
	@Value("${REPORT_DOWNLOAD_SG}")
	private String REPORT_DOWNLOAD_SG;

	@Autowired
	private BluePrintResultService bluePrintResultService;
	
	
	@RequestMapping(value = "/queryResults", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryResults(@RequestBody Map<String, String> param) {
		Map<String, Object> allResults = new HashMap<String, Object>();
		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, Object> fileterResults = new HashMap<String, Object>();
		
		try {
			String pageIndex = (param.get("pageIndex") == null || "".equals(param.get("pageIndex"))) ? "0" : param.get("pageIndex");//default:limit 0, 5
			String pageLimit = (param.get("pageLimit") == null || "".equals(param.get("pageLimit"))) ? "5" : param.get("pageLimit");//5 records per page
			int intPageLimit = Integer.parseInt(pageLimit);
			long startIndex = Long.parseLong(pageIndex) * intPageLimit;
			
			String[] parameters = bluePrintResultService.getParameters(param);
			results = bluePrintResultService.queryBPResults(parameters[0], parameters[1], parameters[2], parameters[3], parameters[4], parameters[5], parameters[6], parameters[7], parameters[9], parameters[10], startIndex, intPageLimit);
			fileterResults = bluePrintResultService.queryBPFilter(parameters);
		} catch (Exception e) {
			logger.error("/blueprintresult/queryResults throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}

		allResults.put("filters", fileterResults);
		allResults.put("results", results);
		
		return RestUtil.handleResult(allResults);
	}
	
	@RequestMapping(value = "/queryDetails", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryDetailLinks(@RequestBody Map<String, String> param) {
		String idIsn = param.get("idIsn");
		String idInd = param.get("idInd");
		String idIsa = param.get("idIsa");
		String isLaptopEnv = param.get("isLaptopEnv");//1 - laptopEnv; 0 - other environment
		if (isLaptopEnv == null) {
			isLaptopEnv = "1";
		}
		
		Map<String, Object> lists = new HashMap<String, Object>();
		try {
			String reg = null;
			if (!isLaptopEnv.equals("1")) {
				List<Map<String, String>> laptopOnlys = bluePrintResultService.queryLaptopOnlys();
				if (laptopOnlys.size() == 0) {
					isLaptopEnv = "1";
				} else {
					reg = RestUtil.constructReg(laptopOnlys);
				}
			}
			
			if (idIsn != null && !"".equals(idIsn)) {
				List<Map<String, Object>> identifiedBuyers = bluePrintResultService.queryIdentifiedBuyers(idIsn);
				lists.put("identifiedBuyers", identifiedBuyers);
				
				Map<String, Object> keyInformation = bluePrintResultService.queryKeyInformation(idIsn);
				lists.put("keyInformation", keyInformation);
				
				List<Map<String, Object>> marketingInformation = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "M"});
				lists.put("marketingInformation", marketingInformation);
				
				List<Map<String, Object>> technicalInformation = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "T"});
				lists.put("technicalInformation", technicalInformation);
				
				List<Map<String, Object>> clientReferences = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "C"});
				lists.put("clientReferences", clientReferences);
				
				List<Map<String, Object>> educationMaterial = bluePrintResultService.queryEducationMaterial(idInd, idIsa, idIsn, "E", isLaptopEnv, reg);
				lists.put("educationMaterial", educationMaterial);
				
				List<Map<String, Object>> demonstrations = bluePrintResultService.queryDemonstrations(idInd, idIsa, idIsn, "D", isLaptopEnv, reg);
				lists.put("demonstrations", demonstrations);
				
				List<Map<String, Object>> solutionDesignAccelerators = bluePrintResultService.querySolutionDesignAccelerators(idIsn, "A", isLaptopEnv, reg);
				lists.put("solutionDesignAccelerators", solutionDesignAccelerators);
				
				//preIntegrated
				List<Map<String, Object>> preIntegrated_apple = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "PP"});
				lists.put("preIntegrated_apple", preIntegrated_apple);
				
				List<Map<String, Object>> preIntegrated_analytics = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "PA"});
				lists.put("preIntegrated_analytics", preIntegrated_analytics);
				
				List<Map<String, Object>> preIntegrated_cloud = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "PC"});
				lists.put("preIntegrated_cloud", preIntegrated_cloud);
				
				//John Zhu 2016-12-01 for story 1360051
				List<Map<String, Object>> preIntegrated_cognitive = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "PG"});
				lists.put("preIntegrated_cognitive", preIntegrated_cognitive);
				
				List<Map<String, Object>> preIntegrated_other = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "PO"});
				lists.put("preIntegrated_other", preIntegrated_other);
				//end preIntegrated
				
				//Business Unit solution Components
				List<Map<String, Object>> bus_analytics = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "BA"});
				lists.put("bus_analytics", bus_analytics);
				
				List<Map<String, Object>> bus_cloud = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "BC"});
				lists.put("bus_cloud", bus_cloud);
				
				List<Map<String, Object>> bus_commerce = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "BM"});
				lists.put("bus_commerce", bus_commerce);
				
				List<Map<String, Object>> bus_gts = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "BG"});
				lists.put("bus_gts", bus_gts);
				
				List<Map<String, Object>> bus_health = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "BH"});
				lists.put("bus_health", bus_health);
				
				List<Map<String, Object>> bus_isv = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "BP"});
				lists.put("bus_isv", bus_isv);
				
				List<Map<String, Object>> bus_security = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "BS"});
				lists.put("bus_security", bus_security);
				
				List<Map<String, Object>> bus_systems = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "BT"});
				lists.put("bus_systems", bus_systems);
				
				List<Map<String, Object>> bus_watson = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "BW"});
				lists.put("bus_watson", bus_watson);
				
				List<Map<String, Object>> bus_other = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "BO"});
				lists.put("bus_other", bus_other);
				//end Business Unit solution Components
				
				//GBS Solution Components
				List<Map<String, Object>> gbs_assets = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "GA"});
				lists.put("gbs_assets", gbs_assets);
				
				List<Map<String, Object>> gbs_gps = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "GG"});
				lists.put("gbs_gps", gbs_gps);
				
				List<Map<String, Object>> gbs_other = bluePrintResultService.querySolutionPart(isLaptopEnv, reg, false, null, new Object[] {idIsn, "GO"});
				lists.put("gbs_other", gbs_other);
				//end GBS Solution Components
				
				//Deal progression
				List<Map<String, Object>> dp_averageDealSize = bluePrintResultService.querySolutionPartWithName(new Object[] {idIsn, "AVERAGE_DEAL_SIZE"});
				lists.put("dp_averageDealSize", dp_averageDealSize);
				
				List<Map<String, Object>> dp_keyQuestion = bluePrintResultService.querySolutionPartWithName(new Object[] {idIsn, "QUALIFYING_QUESTIONS"});
				lists.put("dp_keyQuestion", dp_keyQuestion);

				List<Map<String, Object>> dp_KeyUserCases = bluePrintResultService.querySolutionPartWithName(new Object[] {idIsn, "Use Case"});
				lists.put("dp_KeyUserCases", dp_KeyUserCases);
				
				List<Map<String, Object>> dp_sellerAction = bluePrintResultService.querySolutionPartWithName(new Object[] {idIsn, "SELLER_ACTION"});
				lists.put("dp_sellerAction", dp_sellerAction);
				//end Deal progression
			}
			
			if (idIsn != null && !"".equals(idIsn) && idInd != null && !"".equals(idInd) && idIsa != null && !"".equals(idIsa)) {
				List<Map<String, Object>> contacts = bluePrintResultService.queryContacts(idIsn, idInd, idIsa);
				lists.put("contacts", contacts);
			}
			
		} catch (Exception e) {
			logger.error("/blueprintresult/queryResults throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(lists);
	}
	
	@RequestMapping(value = "/reportDownloadSG", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> reportDownloadSG(@RequestBody Map<String, String> param) {
		logger.info("enter reportDownloadSG method with request mapping /blueprintresult/reportDownloadSG");
		try {
			HttpClient httpClient = HttpClientUtil.initHttpClient();
			HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			RestTemplate template = new RestTemplate(httpFactory);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			Map<String, String> map = new HashMap<String, String>();
			map.put("eventType", param.get("eventType"));
			map.put("userId", param.get("userId"));
			map.put("geo", param.get("geo"));
			map.put("lang", param.get("lang"));
			map.put("source", param.get("source"));
			map.put("operation", param.get("operation"));
			map.put("oid", param.get("oid"));
			map.put("additionaltext", param.get("additionaltext"));
			map.put("version", param.get("version"));
			map.put("listID", param.get("listID"));
			map.put("assetID", param.get("assetID"));
			map.put("collDispTxt", param.get("collDispTxt"));
			map.put("collUrl", param.get("collUrl"));
			HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
			template.postForObject(REPORT_DOWNLOAD_SG, request,String.class);
		} catch (Exception e) {
			logger.info("report download sg Exception",e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult("");
	}
}
