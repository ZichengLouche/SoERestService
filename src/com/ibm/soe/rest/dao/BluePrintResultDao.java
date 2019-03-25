package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface BluePrintResultDao {

	List<Map<String, Object>> queryBluePrintResults(String keyWord, String camss, String idInd, String idIsa, String idIsn, String idBuyer, String geo, String priority, String solOID, String impOID, long startIndex, int pageLimit) throws Exception;
	long queryBluePrintResultTotal(String keyWord, String camss, String idInd, String idIsa, String idIsn, String idBuyer, String geo, String priority, String solOID, String impOID) throws Exception;
	
	Map<String, Object> queryKeyInformation(String idIsn) throws Exception;
	List<Map<String, Object>> queryIdentifiedBuyers(String idIsn) throws Exception;

	// Andy 2016.2.25 12:23
    List<Map<String, Object>> querySolutionPart(final String isLaptopEnv, final String reg, boolean isUnion, String whereSqlForUnion, Object[] args) throws Exception;
    
	List<Map<String, Object>> queryEducationMaterial(String idInd, String idIsa, String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception;
	List<Map<String, Object>> queryDemonstrations(String idInd, String idIsa, String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception;
	List<Map<String, Object>> querySolutionDesignAccelerators(String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception;
	
	List<Map<String, Object>> queryContacts(String idIsn, String idInd, String idIsa) throws Exception;
	
	List<Map<String, String>> queryLaptopOnlys() throws Exception;
	List<Map<String, Object>> querySolutionPartWithName(Object[] args) throws Exception;

	// Andy 2016.5.12 12:42
	List<Map<String, Object>> queryGISCAsset(String idIsn) throws Exception;
	List<Map<String, Object>> queryMappingRelationshipForSolutionImperativeIndustry();
}
