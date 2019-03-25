package com.ibm.soe.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.soe.rest.dao.BluePrintResultDao;
import com.ibm.soe.rest.service.BluePrintFilterService;
import com.ibm.soe.rest.service.BluePrintResultService;

@Service
public class BluePrintResultServiceImpl implements BluePrintResultService {
	@Autowired
	private BluePrintResultDao bluePrintResultDao;
	
	@Autowired
	private BluePrintFilterService bluePrintFilterService;

	@Override
	public Map<String, Object> queryBPResults(String keyWord, String camss, String idInd, String idIsa, String idIsn, String idBuyer, String geo, String priority, 
																						  String solOID, String impOID, long startIndex, int pageLimit) throws Exception {
		List<Map<String, Object>> list = bluePrintResultDao.queryBluePrintResults(keyWord, camss, idInd, idIsa, idIsn, idBuyer, geo, priority, solOID, impOID, startIndex, pageLimit);
		long total = bluePrintResultDao.queryBluePrintResultTotal(keyWord, camss, idInd, idIsa, idIsn, idBuyer, geo, priority, solOID, impOID);
		
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("list", list);
		results.put("total", total);
		return results;
	}
	
	@Override
	public Map<String, Object> queryBPFilter(String[] parameters) {
		Map<String, Object> filterResultMap = new HashMap<String, Object>();
		
		Object[] sqlParamter = this.getCommonSqlParamter(parameters[0], parameters[1] , parameters[8], null, null, parameters[5], parameters[6], parameters[7], parameters[9], parameters[10]);
		List<Map<String, Object>> industryFilterList = bluePrintFilterService.getIndustries(sqlParamter);
		filterResultMap.put("industry", industryFilterList);
		
		sqlParamter = this.getCommonSqlParamter(parameters[0], parameters[1], parameters[2], null, parameters[4], parameters[5], parameters[6], parameters[7], parameters[9], parameters[10]);
		List<Map<String, Object>> imperativeFilterList = bluePrintFilterService.getImperatives(sqlParamter);
		filterResultMap.put("imperative", imperativeFilterList);
		
		sqlParamter = this.getCommonSqlParamter(parameters[0], parameters[1], parameters[2], parameters[3], null, parameters[5], parameters[6], parameters[7], parameters[9], parameters[10]);
		List<Map<String, Object>> solutionFilterList = bluePrintFilterService.getSolutions(sqlParamter);
		filterResultMap.put("solution", solutionFilterList);
		
		sqlParamter = this.getCommonSqlParamter(parameters[0], parameters[1], parameters[2], parameters[3], parameters[4], null, parameters[6], parameters[7], parameters[9], parameters[10]);
		List<Map<String, Object>> buyerFilterList = bluePrintFilterService.getBuyers(sqlParamter);
		filterResultMap.put("buyer", buyerFilterList);
		
		sqlParamter = this.getCommonSqlParamter(parameters[0], parameters[1], parameters[2], parameters[3], parameters[4], parameters[5], "All", "0", parameters[9], parameters[10]);
		List<Map<String, Object>> geoFilterList = bluePrintFilterService.getGeographies(sqlParamter);
		filterResultMap.put("geography", geoFilterList);
		
		sqlParamter = this.getCommonSqlParamter(parameters[0], "0", parameters[2], parameters[3], parameters[4], parameters[5], parameters[6], parameters[7], parameters[9], parameters[10]);
		List<Map<String, Object>> strategicList = bluePrintFilterService.getStrategics(sqlParamter);
		filterResultMap.put("strategicImperative", strategicList);

		return filterResultMap;
	}
	
	// Andy 2016.9.5 17:38
	public Object[] getCommonSqlParamter(String keyWord, String camss, String idInd, String idIsa, String idIsn, String idBuyer, String geo, String priority, String solOID, String impOID) {
		Object[] param = new Object[] { keyWord, keyWord, keyWord, keyWord,keyWord, camss, camss, idInd, idIsa, idIsa, idIsn, idIsn, solOID, solOID, impOID, impOID, idBuyer, idBuyer, 
										priority, geo, geo, geo, geo, geo, geo, geo, geo };
		return param;
	}
		
	// Andy 2016.9.7 21:34
	@Override
	public String[] getParameters(Map<String, String> param) {
		String keyWord = "".equals(param.get("keyWord")) ? null : param.get("keyWord");
		String idInd = "0".equals(param.get("idInd")) || "".equals(param.get("idInd")) ? null : param.get("idInd");
		String idIsa = "0".equals(param.get("idIsa")) || "".equals(param.get("idIsa")) ? null : param.get("idIsa");
		String idIsn = "0".equals(param.get("idIsn")) || "".equals(param.get("idIsn")) ? null : param.get("idIsn");
		String solOID = "0".equals(param.get("solOID")) || "".equals(param.get("solOID")) ? null : param.get("solOID");
		String impOID = "0".equals(param.get("impOID")) || "".equals(param.get("impOID")) ? null : param.get("impOID");
		String idBuyer = "0".equals(param.get("idBuyer")) || "".equals(param.get("idBuyer")) ? null : param.get("idBuyer");
		String priority = param.get("priority") == null || "".equals(param.get("priority")) ? "0" : param.get("priority");
		String idGeo = param.get("idGeo") == null || "".equals(param.get("idGeo")) ? "All" : param.get("idGeo");
		String camss = param.get("idStrategicImperatives") == null || "".equals(param.get("idStrategicImperatives")) ? "0" : param.get("idStrategicImperatives");
		
//		if ("All".equals(idGeo)) {
//			priority = "0"; 
//		}
		// annotated by Charles 2016.12.23
		
		// Andy 2016.7.21 15:08
		String multipleIndustry = "0".equals(param.get("multipleIndustry")) || "".equals(param.get("multipleIndustry")) ? null : param.get("multipleIndustry");
		if(idInd == null && multipleIndustry != null) {
			idInd = multipleIndustry;
		}
		
		String[] results = new String[] { keyWord, camss, idInd, idIsa, idIsn, idBuyer, idGeo, priority, multipleIndustry, solOID, impOID };
		return results;
	}
	
	// Andy 2016.2.24 16:26
	@Override
	public String getCAMSS(String cloud, String analytics, String mobile, String social, String security) {
		List<String> result = new ArrayList<String>();
		if (cloud != null && cloud.equals("Y")) {
			result.add("C");
		} 
		
		if (analytics != null && analytics.equals("Y")) {
			result.add("A");
		} 
		
		if (mobile != null && mobile.equals("Y")) {
			result.add("M");
		} 
		
		if (social != null && social.equals("Y")) {
			result.add("S");
		} 
		
		if (security != null && security.equals("Y")) {
			result.add("Z");
		} 
		
		StringBuffer sb = new StringBuffer();
		if (result.size() > 0) {
			for (int i = 0; i < result.size(); i ++) {
				sb.append(result.get(i)).append("|");
			}
			
		} else {
			return "EMPTY";
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}
	
		
	@Override
	public Map<String, Object> queryKeyInformation(String idIsn) throws Exception {
		return bluePrintResultDao.queryKeyInformation(idIsn);
	}

	@Override
	public List<Map<String, Object>> queryIdentifiedBuyers(String idIsn) throws Exception {
		return bluePrintResultDao.queryIdentifiedBuyers(idIsn);
	}

	// Andy 2016.2.25 12:23
    public List<Map<String, Object>> querySolutionPart(final String isLaptopEnv, final String reg, boolean isUnion, String whereSqlForUnion, Object[] args) throws Exception {
    	return bluePrintResultDao.querySolutionPart(isLaptopEnv, reg, isUnion, whereSqlForUnion, args);
    }
    
	@Override
	public List<Map<String, Object>> queryEducationMaterial(String idInd, String idIsa, String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception {
		return bluePrintResultDao.queryEducationMaterial(idInd, idIsa, idIsn, detailsAbbrev, isLaptopEnv, reg);
	}
	@Override
	public List<Map<String, Object>> queryDemonstrations(String idInd, String idIsa, String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception {
		List<Map<String, Object>> solutionPartResult = bluePrintResultDao.queryDemonstrations(idInd, idIsa, idIsn, detailsAbbrev, isLaptopEnv, reg);
		// Andy 2016.5.12 12:42
		List<Map<String, Object>> giscResult = bluePrintResultDao.queryGISCAsset(idIsn);
		solutionPartResult.addAll(giscResult);
		return solutionPartResult;
	}
	@Override
	public List<Map<String, Object>> querySolutionDesignAccelerators(String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception {
		return bluePrintResultDao.querySolutionDesignAccelerators(idIsn, detailsAbbrev, isLaptopEnv, reg);
	}
	
	public List<Map<String, Object>> queryContacts(String idIsn, String idInd, String idIsa) throws Exception {
		return bluePrintResultDao.queryContacts(idIsn, idInd, idIsa);
	}

	public List<Map<String, String>> queryLaptopOnlys() throws Exception {
		return bluePrintResultDao.queryLaptopOnlys();
	}
	
    public List<Map<String, Object>> querySolutionPartWithName(Object[] args) throws Exception {
    	return bluePrintResultDao.querySolutionPartWithName(args);
    }

    // Andy 2016.5.27 15:40
	@Override
	public List<Map<String, Object>> queryMappingRelationshipForSolutionImperativeIndustry() {
		return bluePrintResultDao.queryMappingRelationshipForSolutionImperativeIndustry();
	}

	
}
