package com.ibm.soe.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.service.ProductInformationService;
import com.ibm.soe.rest.util.RestUtil;
import com.ibm.soe.rest.vo.AssistVoLocal;

@Controller
@RequestMapping("productInformation")
public class ProductionInfoController {
	private static Logger logger = Logger.getLogger(ExpertGuidanceController.class);
	
	@Autowired
	private ProductInformationService productInformationService;
	
	@RequestMapping(value = "/getPIData", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryResults(@RequestBody Map<String, Object> param) {
		Map<String, Object> invocationResultInfo = new HashMap<String, Object>();
		AssistVoLocal.setAssistVo(invocationResultInfo);
		Map<String, Object> allResults = new HashMap<String, Object>();
		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, Object> fileterResults = new HashMap<String, Object>();
		
        String[] paramarr = getParameters(param);
		
		try {
			String pageIndex = (String) ((param.get("pageIndex") == null || "".equals(param.get("pageIndex"))) ? "0" : param.get("pageIndex"));
			String pageLimit = (String) ((param.get("pageLimit") == null || "".equals(param.get("pageLimit"))) ? "5" : param.get("pageLimit"));
			
			List<String> growthPlayslist=new ArrayList<String>();
 			if(paramarr[1]!=null) {
 				growthPlayslist.add("cloud".toUpperCase());
			}
 			if(paramarr[2]!=null) {
 				growthPlayslist.add("analytics".toUpperCase());
			}
 			if(paramarr[3]!=null) {
 				growthPlayslist.add("mobile".toUpperCase());
			}
 			if(paramarr[4]!=null) {
 				growthPlayslist.add("social".toUpperCase());
			}
 			if(paramarr[5]!=null) {
 				growthPlayslist.add("security".toUpperCase());
			}
 			
 			String[] growthPlays=null;
 			if(growthPlayslist.size()>0) {
 				growthPlays = new String[growthPlayslist.size()];
 				growthPlayslist.toArray(growthPlays);
 			}
 			
			if (paramarr[11] != null) {
				results = productInformationService.queryProductInformationResults(paramarr[6], growthPlays, paramarr[7], paramarr[8], paramarr[0], paramarr[9], paramarr[10],paramarr[11], pageIndex, pageLimit);
			}
			
			fileterResults = productInformationService.queryProductionFilter(paramarr[6], growthPlays, paramarr[7], paramarr[8], paramarr[0]);
//			if (results.containsKey("total")){
//				List<Map<String,String>> brandlist = (List<Map<String,String>>)fileterResults.get("brandFilter");
//				Object total = results.get("total");
//				Map<String,String> totalMap = brandlist.get(0);
//				totalMap.put("num", String.valueOf(total));
//			}

		} catch (Exception e) {
			logger.error("/productionInformation/getPIData throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		
		allResults.put("filters", fileterResults);
		allResults.put("results", results);
		if(CollectionUtils.isNotEmpty(AssistVoLocal.getAssistVo().values())) allResults.put("warnings", AssistVoLocal.getAssistVo().values());
		
		return RestUtil.handleResult(allResults);
	}
	
	@SuppressWarnings("unchecked")
	private String[] getParameters(Map<String, Object> param) {
		String keyWord = (String) ( "".equals(param.get("keyWord")) ? null : param.get("keyWord") );
		
		String cloud = (String) ("".equals(param.get("cloud")) || "N".equals(param.get("cloud")) ? null : param.get("cloud"));
		String analytics = (String) ("0".equals(param.get("analytics"))|| "".equals(param.get("analytics")) || "N".equals(param.get("analytics")) ? null : param.get("analytics"));
		String mobile = (String) ("0".equals(param.get("mobile")) || "".equals(param.get("mobile")) || "N".equals(param.get("mobile")) ? null : param.get("mobile"));
		String social = (String) ("0".equals(param.get("social")) || "".equals(param.get("social")) || "N".equals(param.get("social")) ? null : param.get("social"));
		String security = (String) ("0".equals(param.get("security")) || "".equals(param.get("security")) || "N".equals(param.get("security")) ? null : param.get("security"));
		
		String brand = (String) ("0".equals(param.get("brand")) || "".equals(param.get("brand")) ? null : param.get("brand"));
		String source = (String) ("0".equals(param.get("source")) || "".equals(param.get("source")) ? null : param.get("source"));
		String date = (String) ("0".equals(param.get("date")) || "".equals(param.get("date")) ? null : param.get("date"));
		
		List<String> sortby = (List<String>) (param.get("sortBy") == null ? null : param.get("sortBy"));
		String uid = (String) ("0".equals(param.get("uid")) || "".equals(param.get("uid")) ? null : param.get("uid"));
		
		String[] results = new String[] { keyWord, cloud, analytics, mobile, social, security, brand, source, date, sortby.get(0), sortby.get(1), uid };
		return results;
	}
	
	@RequestMapping(value = "/login", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(@RequestBody Map<String, String> param) {
	    try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    
	    return RestUtil.handleResult(param);
	}  
	
	@RequestMapping(value = "/insertUseful", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertUseful(@RequestBody Map<String, String> param) {
	    try {
	    	   productInformationService.insertUseful(param);
		} catch (Exception e) {
			logger.error("/productionInformation/insertUseful throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
	    
	    return RestUtil.handleResult("");
	}  
	
}
