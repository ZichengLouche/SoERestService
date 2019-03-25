package com.ibm.soe.rest.controller;

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

import com.ibm.soe.rest.service.BSUService;
import com.ibm.soe.rest.service.BluePrintResultService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("bsu")
public class BSUController {
	private static Logger logger = Logger.getLogger(ExpertGuidanceController.class);
	
	@Autowired
	private BSUService bsuService;
	
	@Autowired
	private BluePrintResultService bluePrintResultService;
	
	@RequestMapping(value = "/getBSUData", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryResults(@RequestBody Map<String, Object> param) {
		Map<String, Object> allResults = new HashMap<String, Object>();
		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, Object> fileterResults = new HashMap<String, Object>();
		
        String[] paramArry = getParameters(param);
		
		try {
			String pageIndex = (String) ((param.get("pageIndex") == null || "".equals(param.get("pageIndex"))) ? "0" : param.get("pageIndex"));
			String pageLimit = (String) ((param.get("pageLimit") == null || "".equals(param.get("pageLimit"))) ? "5" : param.get("pageLimit"));
			
			results = bsuService.queryBSUResults(paramArry[0], paramArry[1], paramArry[2], paramArry[3], paramArry[4], paramArry[5], pageIndex, pageLimit, paramArry[6]);
			fileterResults = bsuService.queryBSUFilter(paramArry[0], paramArry[1], paramArry[2], paramArry[3], paramArry[4], paramArry[5], paramArry[6], paramArry[7]);

		} catch (Exception e) {
			logger.error("/productionInformation/getPIData throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		
		allResults.put("filters", fileterResults);
		allResults.put("results", results);
		
		return RestUtil.handleResult(allResults);
	}
	
	private String[] getParameters(Map<String, Object> param) {
		String keyWord = (String) (param.get("keyWord") == null || "".equals(param.get("keyWord")) ? null : param.get("keyWord"));
		String idInd = (String) (param.get("idInd") == null || "0".equals(param.get("idInd")) || "".equals(param.get("idInd")) ? null : param.get("idInd"));
		String idIsa = (String) (param.get("idIsa") == null || "0".equals(param.get("idIsa")) || "".equals(param.get("idIsa")) ? null : param.get("idIsa"));
		String idCnt = (String) (param.get("idCnt") == null || "0".equals(param.get("idCnt")) || "".equals(param.get("idCnt")) ? null : param.get("idCnt"));
		String idBuyer = (String) (param.get("idBuyer") == null || "0".equals(param.get("idBuyer")) || "".equals(param.get("idBuyer")) ? null : param.get("idBuyer"));
		String idBSU = (String) (param.get("idBSU") == null || "0".equals(param.get("idBSU")) || "".equals(param.get("idBSU")) ? null : param.get("idBSU"));
		String idIsn = (String) (param.get("idIsn") == null || "0".equals(param.get("idIsn")) || "".equals(param.get("idIsn")) ? null : param.get("idIsn"));
		
		// Andy 2016.7.21 15:08
		String multipleIndustry = (String) ("0".equals(param.get("multipleIndustry")) || "".equals(param.get("multipleIndustry")) ? null : param.get("multipleIndustry"));
		if(idInd == null && multipleIndustry != null) {
			idInd = multipleIndustry;
		}
		String[] results = new String[] { keyWord, idInd, idIsa, idCnt, idBuyer, idBSU, idIsn, multipleIndustry };
		return results;
	}
	
	// Andy 2016.1.14 22:25
	@RequestMapping(value = "/queryDetails", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryDetailLinks(@RequestBody Map<String, String> param) {
		Map<String, Object> lists = new HashMap<String, Object>();
		String idInd = param.get("idInd");//idIndustry
		String idIsa = param.get("idIsa");//idImperative
		String idIsn = param.get("idIsn");//idSolution
		String idCnt = param.get("idCnt");//idCnt
		String isLaptopEnv = param.get("isLaptopEnv");//1 - laptopEnv; 0 - other environment
		if (isLaptopEnv == null) {
			isLaptopEnv = "1";
		}
		
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
				List<Map<String, Object>> identifiedBuyers = bsuService.queryIdentifiedBuyers(idIsn);
				lists.put("identifiedBuyers", identifiedBuyers);
				
				// Andy 2016.1.18 15:39 idCnt: 1 - use case; 2 - bsu solution
				if("2".equals(idCnt)) {
					List<Map<String, Object>> enablementList = bsuService.querySolutionPart(idIsn, "I", isLaptopEnv, reg);
					lists.put("enablement", enablementList);
					
					List<Map<String, Object>> clientFacingList = bsuService.querySolutionPart(idIsn, "C", isLaptopEnv, reg);
					lists.put("clientFacing", clientFacingList);
					
					List<Map<String, Object>> applicableUseCasesList = bsuService.querySolutionMapForUseCase(idIsn);
					lists.put("applicableUseCases", applicableUseCasesList);
					
				} else if("1".equals(idCnt)) {
					List<Map<String, Object>> tearSheetList = bsuService.querySolutionPart(idIsn, "T", isLaptopEnv, reg);
					lists.put("tearSheet", tearSheetList);
					
					List<Map<String, Object>> useCaseAssetsList = bsuService.querySolutionPart(idIsn, "U", isLaptopEnv, reg);
					lists.put("useCaseAssets", useCaseAssetsList);
					
					// Andy 2016.1.26 22:35
					List<Map<String, Object>> alignedPSList = bsuService.querySolutionPart(idIsn, "A", isLaptopEnv, reg);
					lists.put("alignedPS", alignedPSList);
				}
			}
			
			if (idIsn != null && !"".equals(idIsn) && idInd != null && !"".equals(idInd) && idIsa != null && !"".equals(idIsa)) {
				List<Map<String, Object>> contactsList = bsuService.queryContacts(idIsn, idInd, idIsa);
				lists.put("contacts", contactsList);
			}
			
		} catch (Exception e) {
			logger.error("/bsu/queryResults throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(lists);
	}
}
