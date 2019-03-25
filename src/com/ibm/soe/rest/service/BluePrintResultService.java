package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface BluePrintResultService {
	// Andy 2016.2.25 10:33
	Map<String, Object> queryBPResults(String keyWord, String camss, String idInd, String idIsa, String idIsn, String idBuyer, String geo, String priority, String solOID, String impOID, long startIndex, int pageLimit) throws Exception;

	// Andy 2016.9.7 21:34
	Map<String, Object> queryBPFilter(String[] parameters);
	
	// Andy 2016.2.24 22:03
	String getCAMSS(String cloud, String analytics, String mobile, String social, String security);
	String[] getParameters(Map<String, String> param);	
	// Andy 2016.9.5 17:38
	Object[] getCommonSqlParamter(String keyWord, String camss, String idInd, String idIsa, String idIsn, String idBuyer, String geo, String priority, String idOID, String impOID);
		
		
	Map<String, Object> queryKeyInformation(String idISN) throws Exception;
	List<Map<String, Object>> queryIdentifiedBuyers(String idIsn) throws Exception;

	// Andy 2016.2.25 12:23
    List<Map<String, Object>> querySolutionPart(final String isLaptopEnv, final String reg, boolean isUnion, String whereSqlForUnion, Object[] args) throws Exception;
		
	List<Map<String, Object>> queryEducationMaterial(String idInd, String idIsa, String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception;
	List<Map<String, Object>> queryDemonstrations(String idInd, String idIsa, String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception;
	List<Map<String, Object>> querySolutionDesignAccelerators(String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception;

	List<Map<String, Object>> queryContacts(String idIsn, String idInd, String idIsa) throws Exception;
	
	List<Map<String, String>> queryLaptopOnlys() throws Exception;

	List<Map<String, Object>> querySolutionPartWithName(Object[] objects) throws Exception;

	// Andy 2016.5.27 15:40
	List<Map<String, Object>> queryMappingRelationshipForSolutionImperativeIndustry();

}
