package com.ibm.soe.rest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.soe.rest.service.CommFunctionService;
import com.ibm.soe.rest.service.SolutionGatewayService;
import com.ibm.soe.rest.util.Const;
import com.ibm.soe.rest.util.HttpClientUtil;
import com.ibm.soe.rest.util.MailUtil;
import com.ibm.soe.rest.vo.SOEException;

@Controller
@RequestMapping("task")
@Component
public class SolutionPartTask {
	private static Logger logger = Logger.getLogger(SolutionPartTask.class);
	public final static String RET_SUCCESS_CODE = "D00";

	@Value("${SG_FILETER}")
	private String SG_FILETER;

	@Value("${SG_GETASSETDETAIL}")
	private String SG_GETASSETDETAIL;

	@Value("${SG_CONSTRUCT_URL}")
	private String SG_CONSTRUCT_URL;

	@Value("${repotype}")
	private String repotype;

	@Value("${DATA_Fail_email}")
	private String emailList;

	@Value("${fromEmail}")
	private String fromEmail;

	@Value("${hostName}")
	private String hostName;

	@Value("${TABLE_SOLUTION_PART_TEMP}")
	private String TABLE_SOLUTION_PART_TEMP;
	
	@Autowired
	private SolutionGatewayService solutionGateWayService;
	
	@Autowired @Qualifier("threadCommFunctionService")
	private CommFunctionService commFunctionService;

	@Autowired
	private MailUtil mailUtil;
	
	private Map<String, Object> resultMessageMap = new HashMap<String, Object>();

	
	@RequestMapping(value = "/autoUpdateSolutionPartByParseJSON/{name}", produces="application/json;charset=UTF-8", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> autoUpdateSolutionPartByParseJSON(@PathVariable("name") String param) {
		return autoUpdateSolutionpartByParseJSONCommon();
	}
	
//	@Scheduled(cron = "0 20 2 ? * SAT")
	public void getDataFromSG() {
		autoUpdateSolutionpartByParseJSONCommon();
	}

	private Map<String, Object> autoUpdateSolutionpartByParseJSONCommon() {
		boolean result = true;
		resultMessageMap.put("startTime", new Date().toLocaleString());
		long begin = Calendar.getInstance().getTimeInMillis();
		
		try {
			Const.initSGCode(repotype);
			commFunctionService.truncateTableData(TABLE_SOLUTION_PART_TEMP);
			getBluePrintData();
			getBSUData();
			copyDataToSolutionPart(0);
			resultMessageMap.put("copyDataToSolutionPartTime", new Date().toLocaleString());
		
		} catch (SOEException e) {
			// Andy 2016.6.8 21:41
			result = false;
			resultMessageMap.put("returnCode", e.getCode());
			resultMessageMap.put("returnMessage", e.getMessage());
			logger.error("Automatic update Solutionpart by parse JSON occured exception", e);
			mailUtil.sendMultipleEmails("DataSynchronizationException:Automatic update Solutionpart by parse JSON occured exception", "Detail exception:" + ExceptionUtils.getFullStackTrace(e));
		} catch (Exception e) {
			result = false;
			resultMessageMap.put("returnCode", "ERROR-999");
			resultMessageMap.put("returnMessage", "SolutionPartTask run inner error:" + e.getMessage());
			logger.error("Automatic update Solutionpart by parse JSON occured exception", e);
			mailUtil.sendMultipleEmails("DataSynchronizationException:Automatic update Solutionpart by parse JSON occured exception", "Detail exception:" + ExceptionUtils.getFullStackTrace(e));
		}
		
		long usedTime = (Calendar.getInstance().getTimeInMillis() - begin) / 1000;
		String resultMessage = "execute autoUpdateSolutionPartByParseJSON " + (result == true ? "successfully" : "failure") + ". usedTime:" + usedTime + "s. " ;
		resultMessageMap.put("result", resultMessage);
		
		mailUtil.sendMultipleEmails("DataSynchronization" + (result == true ? "Successfully" : "Failure") + ":Solutionpart", "Detail message: " + resultMessage);
		return resultMessageMap;
	}
	
	private void getBSUData() {
		RestTemplate template = null;
		HttpHeaders headers = null;
		
		Map<String, Map<String, String>> bsuListIdMap = solutionGateWayService.getBSUListIds();
		List<Map<String, String>> bsuToolIdList = solutionGateWayService.getBSUToolID();
		Map<String, Map<String, String>> bsuOIDMap = solutionGateWayService.getSolutionBsuOID();
		resultMessageMap.put("prepareIdBSUDataTime", new Date().toLocaleString());
		
		// Andy 2016.6.7 22:04
		if(bsuToolIdList != null && !bsuToolIdList.isEmpty()) {
			Map<String, Map<String,String>> fourLevelCascadeIdsMapForBSU = solutionGateWayService.getFourLevelCascadeIds(2);
			
			try {
				HttpClient httpClient = HttpClientUtil.initHttpClient();
				HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
				template = new RestTemplate(httpFactory);
				headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
			} catch (Exception e) {
				throw new SOEException("ERROR-001", "initialize httpClient occured exception:" + e.getMessage(), e);
			}
			
			List<Map<String, String>> solutionpartList = new ArrayList<Map<String,String>>();
			for (Map<String, String> toolIdMap : bsuToolIdList) {
				String toolID = toolIdMap.get("ToolID");
				String IDBSU = toolIdMap.get("IDBSU");
				if (toolID != null && !toolID.trim().equals("")) {
					Map<String, String> currentIdBsuListidMap = bsuListIdMap.get(IDBSU);
					Map<String, String> currentIdBsuOIDMap = bsuOIDMap.get(IDBSU);

					// Andy 2016.6.8 11:21
					if (currentIdBsuOIDMap == null || currentIdBsuOIDMap.isEmpty()) continue;
					Set<String> oidSet = currentIdBsuOIDMap.keySet();
					for (String oid : oidSet) {
						try {
							Map<String, String> map = new HashMap<String, String>();
							map.put("toolID", toolID);
							map.put("spec version", "");
							map.put("oid", oid);
							HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
							String result = template.postForObject(SG_FILETER, request, String.class);
						
							// Andy 2016.6.7 23:04
							List<Map<String, String>> currentSolutionpartList = parseData(result, currentIdBsuOIDMap.get(oid), currentIdBsuListidMap, new HashMap<String, String>(), new ArrayList<String>());
							
							boolean ret = solutionGateWayService.processSGData(currentSolutionpartList, 2, fourLevelCascadeIdsMapForBSU);
							if (!ret) {
								break;
							}
							
							solutionpartList.addAll(currentSolutionpartList);
						} catch (Exception e) {
							logger.error("Current oid [" + oid + "], toolID [" + toolID + "] calling API and parsing json data occured exception:" + ExceptionUtils.getFullStackTrace(e));
						}
					}
				}
			}
			resultMessageMap.put("getBSUDataTime", new Date().toLocaleString());
			
			//boolean ret = solutionGateWayService.processSGData(solutionpartList, 2, fourLevelCascadeIdsMapForBSU);
			resultMessageMap.put("updateSolutionPartForBSUTotalData", solutionpartList.size());
			resultMessageMap.put("updateSolutionPartForBSUDataTime", new Date().toLocaleString());
		}
			
		
	}

	private void getBluePrintData() {
		HttpClient httpClient = null;
		try {
			logger.info("Automatic execute blueprint data synchronization: start" + new Date());
			// get list id
			Map<String, String> blueprintListMap = new HashMap<String, String>();
			List<String> blueprint_listIds = solutionGateWayService.getBLUEPRINTListIds();

			Map<String, String> blueprintexceptionListMap = new HashMap<String, String>();
			List<String> blueprint_exception_listIds = solutionGateWayService.getBLUEPRINTExceptionListIds();

			List<String> blueprint_listIds_0 = solutionGateWayService.getBLUEPRINT0ListIds();

			for (String lisid : blueprint_listIds) {
				blueprintListMap.put(lisid, lisid);
			}

			for (String lisid : blueprint_exception_listIds) {
				blueprintexceptionListMap.put(lisid, lisid);
			}

			List<String> offeringIDs = solutionGateWayService.getOfferingOID();
			logger.info("Automatic execute data synchronization:" + SG_FILETER + "******************************" + offeringIDs.size());
			
			// Andy 2016.6.7 22:04
			if(offeringIDs != null && !offeringIDs.isEmpty()) {
				Map<String, Map<String,String>> fourLevelCascadeIdsMapForBlueprint = solutionGateWayService.getFourLevelCascadeIds(1);
				
				for (String o : offeringIDs) {
					httpClient = HttpClientUtil.initHttpClient();
					HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
					RestTemplate template = new RestTemplate(httpFactory);
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					Map<String, String> map = new HashMap<String, String>();
					map.put("toolID", "iSolutions");
					map.put("spec version", "");
					map.put("oid", o);
					HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
					String result = template.postForObject(SG_FILETER, request, String.class);
	
					List<Map<String, String>> resultMap = parseData(result, o, blueprintListMap, blueprintexceptionListMap, blueprint_listIds_0);
					boolean ret = solutionGateWayService.processSGData(resultMap, 1, fourLevelCascadeIdsMapForBlueprint);
					if (!ret) {
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.info("Automatic Call API and Parse data Exception", e);
			sendFailedEmail("Automatic Call API and Parse data Exception:" + e.getMessage());
		}
	}

	private void sendFailedEmail(String msg) {
		String[] emaillist = emailList.split(",");

		for (int i = 0; i < emaillist.length; i++) {
			mailUtil.sendMail(emaillist[i], fromEmail, "SG Data synchronization exception", hostName + msg, null);
		}
	}

	/**
	 * 解析json
	 * 
	 * @param json
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> parseData(String json, String offeringid, Map<String, String> currentIdBsuListidMap, Map<String, String> exceptionMap, 
												List<String> description_listIds_0) throws JsonParseException, JsonMappingException, IOException {
		List<Map<String, String>> solutionpartList = new ArrayList<Map<String, String>>();
		ObjectMapper mapTool = new ObjectMapper();
		Map<?, ?> resultMap = mapTool.readValue(json, Map.class);
		String retStatus = (String) resultMap.get("rc");
		if (RET_SUCCESS_CODE.equals(retStatus)) {
			Map<String, Object> item = (Map<String, Object>) resultMap.get("item data");
			Map<String, Object> portfolioItemMap = (Map<String, Object>) item.get("PortfolioItem");

			// attributes
			Map<String, Object> attributes = (Map<String, Object>) portfolioItemMap.get("attributes");
			List<Map<String, Object>> attributelist = (List<Map<String, Object>>) attributes.get("attribute");
			for (Map<String, Object> listItem : attributelist) {
				String name = (String) listItem.get("name");
				String content = (String) listItem.get("content").toString().trim();
				if (description_listIds_0.contains(name) && !content.equals("")) {
					Map<String, String> itemMap = new HashMap<String, String>();
					itemMap.put("listid", "0");
					itemMap.put("Description", content);
					itemMap.put("Name", name);
					itemMap.put("lastModified", "");
					itemMap.put("url", null);
					itemMap.put("idOffering", offeringid);
					itemMap.put("assetID", null);
					solutionpartList.add(itemMap);
				}
			}

			// boms
			Map<String, Object> bomsMap = (Map<String, Object>) portfolioItemMap.get("boms");
			List<Map<String, Object>> bomList = (List<Map<String, Object>>) bomsMap.get("bom");

			for (Map<String, Object> bom : bomList) {
				String listid = (String) bom.get("listid");
				if (currentIdBsuListidMap.containsKey(listid)) {
					Map<String, String> processedAssetIdMap = new HashMap<String, String>();
					List<Map<String, Object>> itemList = (List<Map<String, Object>>) bom.get("item");

					for (Map<String, Object> itemMap : itemList) {
						Integer repotype = (Integer) itemMap.get("repotype");

						if (Const.repotypeMap.containsKey(String.valueOf(repotype))) {
							String description = (String) itemMap.get("assetDescription");

							String assetID = (String) itemMap.get("assetid");
							if (assetID == null || assetID.trim().equals("") || processedAssetIdMap.containsKey(assetID.trim())) {
								continue;
							} else {
								processedAssetIdMap.put(assetID.trim(), assetID);
								if (String.valueOf(repotype).equals("1")) {
									if (exceptionMap.containsKey(listid)) {
										String title = (String) itemMap.get("name");
										String oid = (String) itemMap.get("oid");
										if (oid == null || oid.trim().equals("")) {
											continue;
										} else {
											String url = SG_CONSTRUCT_URL + oid;
											Map<String, String> solutionpartMap = new HashMap<String, String>();
											solutionpartMap.put("listid", listid);
											solutionpartMap.put("Description", description);
											solutionpartMap.put("Name", title);
											solutionpartMap.put("lastModified", "");
											solutionpartMap.put("url", url);
											solutionpartMap.put("idOffering", offeringid);
											solutionpartMap.put("assetID", assetID);
											solutionpartList.add(solutionpartMap);
										}
									}
								} else {
									if (!assetID.startsWith("AHUB")) {
										try {
											HttpClient httpClient = HttpClientUtil.initHttpClient();
											HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
											RestTemplate template = new RestTemplate(httpFactory);
											HttpHeaders headers = new HttpHeaders();
											headers.setContentType(MediaType.APPLICATION_JSON);
											Map<String, String> map = new HashMap<String, String>();
											map.put("assetID", assetID);
											HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
											String result = template.postForObject(SG_GETASSETDETAIL, request, String.class);

											Map<?, ?> detailMap = mapTool.readValue(result, Map.class);
											Map<String, Object> assetData = (Map<String, Object>) detailMap.get("asset data");
											String lastModified = (String) assetData.get("lastModified");
											List<Map<String, Object>> linklist = (List<Map<String, Object>>) assetData.get("link");
											if (linklist != null) {
												for (Map<String, Object> link : linklist) {
													String url = (String) link.get("url");
													String title = (String) link.get("title");
													if (url != null) {
														Map<String, String> solutionpartMap = new HashMap<String, String>();
														solutionpartMap.put("listid", listid);
														solutionpartMap.put("Description", description);
														solutionpartMap.put("Name", title);
														solutionpartMap.put("lastModified", lastModified);
														solutionpartMap.put("url", url);
														solutionpartMap.put("idOffering", offeringid);
														solutionpartMap.put("assetID", assetID);
														solutionpartList.add(solutionpartMap);
													}
												}
											}
										} catch (Exception e) {
											logger.error("Automatic Call API and Parse data Exception", e);
											sendFailedEmail("Automatic Call API and Parse data Exception" + e.getMessage());
										}
									} else {
										String name = (String) itemMap.get("name");
										Map<String, Object> asset = (Map<String, Object>) itemMap.get("asset");
										if (asset == null || description == null || name == null) {
											continue;
										} else {
											String lastModified = (String) asset.get("lastModified");
											List<Map<String, Object>> linklist = (List<Map<String, Object>>) asset.get("link");
											if (linklist != null) {
												Map<String, Object> linkMap = linklist.get(0);
												String url = (String) linkMap.get("url");
												if (url == null) {

												} else {
													Map<String, String> solutionpartMap = new HashMap<String, String>();
													solutionpartMap.put("listid", listid);
													solutionpartMap.put("Description", description);
													solutionpartMap.put("Name", name);
													solutionpartMap.put("lastModified", lastModified);
													solutionpartMap.put("url", url);
													solutionpartMap.put("idOffering", offeringid);
													solutionpartMap.put("assetID", assetID);
													solutionpartList.add(solutionpartMap);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return solutionpartList;
	}

	public void copyDataToSolutionPart(int type) {
		boolean result;
		try {
			result = solutionGateWayService.copyDataToSolutionpart(type);
			if (!result) {
				sendFailedEmail("Copy data from the Solutionpart_temp to the Solutionpart failed, executed result:" + result);
			} 
		} catch (Exception e) {
			sendFailedEmail("Copy data from the Solutionpart_temp to the Solutionpart occured exception:" + e.getMessage());
			throw new SOEException("ERROR-002", "Copy data from the Solutionpart_temp to the Solutionpart occured exception:" + e.getMessage(), e);
		}
	}

}
