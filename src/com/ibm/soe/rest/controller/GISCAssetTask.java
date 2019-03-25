package com.ibm.soe.rest.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.soe.rest.service.BluePrintResultService;
import com.ibm.soe.rest.service.CommFunctionService;
import com.ibm.soe.rest.util.HttpClientUtil;
import com.ibm.soe.rest.util.MailUtil;

@Controller
@RequestMapping("task")
@Component
public class GISCAssetTask {
	private static Logger logger = Logger.getLogger(GISCAssetTask.class);
	
	@Value("${DATA_Fail_email}")
	private String emailList;
	
	@Value("${fromEmail}")
	private String fromEmail;
	
	@Value("${hostName}")
	private String hostName;
	
	@Value("${isCountThreadTime}")
	private String isCountThreadTime;
	
	@Value("${GAAS_SERVICE_URL}")
	private String GAAS_SERVICE_URL;
	
	@Autowired @Qualifier("threadCommFunctionService")
	private CommFunctionService commFunctionService;
	
	@Autowired
	private BluePrintResultService bluePrintResultService;
	
	@Autowired
	private MailUtil mailUtil;
	
	
	// Andy 2016.5.27 16:49
//	@Scheduled(cron="0 10 2 ? * *")
	public void autoUpdateGISCAssetByParseJSON() {
		autoUpdateGISCAssetByParseJSONCommon();
	}
	
	@RequestMapping(value = "/autoUpdateGISCAssetByParseJSON/{name}", produces="application/json;charset=UTF-8", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> autoUpdateGISCAssetByParseJSON(@PathVariable("name") String param) {
		return autoUpdateGISCAssetByParseJSONCommon();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> autoUpdateGISCAssetByParseJSONCommon() {
		Map<String, Object> resultMessageMap = new HashMap<String, Object>();
		boolean result = false;
		long begin = Calendar.getInstance().getTimeInMillis();
		long getJSONTime = 0;
		long getAndParseJSONTime = 0;
		int totalData = 0;
		long successData = 0;
				
		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpClient = HttpClientUtil.initHttpClient();
			HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			RestTemplate restTemplate = new RestTemplate(httpFactory);
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			Map<String, String> urlVariables = new HashMap<String, String>();
//			urlVariables.put("requesterId", "TestUser");
//			urlVariables.put("appVersion", "2.0");
//			urlVariables.put("assetStatus", "Production");
			//HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(urlVariables, headers);
			byte[] buffer = restTemplate.getForObject("https://extbasicgaasm01.w3-969.ibm.com/GaasService/asset/getAllAssets?requesterId=TestUser&appVersion=2.0&assetStatus=Production", byte[].class);
			String result1 = new String(buffer, "UTF-8");
			getJSONTime = (Calendar.getInstance().getTimeInMillis() - begin) / 1000;
			
			ObjectMapper mapTool = new ObjectMapper();
		    List<Map<String, Object>> newGISCAssetList = mapTool.readValue(result1, ArrayList.class);
		    totalData = newGISCAssetList.size();
		    getAndParseJSONTime = (Calendar.getInstance().getTimeInMillis() - begin) / 1000;
		    if (newGISCAssetList != null && totalData > 0) {
		    	final CountDownLatch countDownLatch = new CountDownLatch(totalData);
		    	List<Map<String, Object>> idIsnRelationshipList = bluePrintResultService.queryMappingRelationshipForSolutionImperativeIndustry();
		    	
		    	boolean truncateResult = commFunctionService.truncateTableData("giscasset");
		    	if(truncateResult) {
			    	for (Map<String,Object> giscAssetMap : newGISCAssetList) {
			    		result = commFunctionService.updateGISCAsset(giscAssetMap, idIsnRelationshipList, countDownLatch);
					}
		    	}
		    	
		    	if (isCountThreadTime.equals("true") && countDownLatch != null) {
		    		try {
		    			countDownLatch.await();
		    		} catch (InterruptedException e) {
		    			logger.error(e.getMessage());
		    		}
		    		
		    		successData = totalData - countDownLatch.getCount();
		    	}
			}
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Automatic batch update GISCAsset data occured exception:", e);
			mailUtil.sendMultipleEmails("DataSynchronizationException:Automatic update GISCAsset occured exception", "Detail exception:" + e.getMessage());
		} 
	    
		long usedTime = (Calendar.getInstance().getTimeInMillis() - begin) / 1000;
		String resultMessage = "execute autoUpdateGISCAssetByParseJSON " + (result == true ? "successfully" : "failure") + ". usedTime:" + usedTime 
				+ "s, getJSONTime:" + getJSONTime + "s, getAndParseJSONTime:" + getAndParseJSONTime + "s, totalData:" + totalData + ", successData:" + successData;
		logger.error(resultMessage);
		resultMessageMap.put("result", resultMessage);
		
		// Andy 2016.6.2 21:32
		mailUtil.sendMultipleEmails("DataSynchronization" + (result == true ? "Successfully" : "Failure") + ":GISCAsset", "Detail message: " + resultMessage);
		return resultMessageMap;
	}
	
	
	public static void main(String[] args) throws Exception {
		GISCAssetTask task = new GISCAssetTask();
		task.autoUpdateGISCAssetByParseJSONCommon();
	}
}
