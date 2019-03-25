package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface BSUDao {
	
	// Andy 2016.1.12 16:32
	List<Map<String, Object>> queryBSUResults(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, long startIndex, int pageLimit, String idIsn) throws Exception;

	long queryFilterAllCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception;

	// Andy 2016.1.11 22:00
	List<Map<String, Object>> queryBSUFilterCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception;
	List<Map<String, Object>> queryIndustryFilterCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception;
	List<Map<String, Object>> queryImperativeFilterCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception;
	List<Map<String, Object>> queryContentTypeFilterCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception;
	List<Map<String, Object>> queryBuyerFilterCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception;

	// Andy 2016.1.14 22:25
	List<Map<String, Object>> querySolutionPart(String idIsn, String detailsAbbrev, final String isLaptopEnv, final String reg) throws Exception;
	List<Map<String, Object>> queryContacts(String idIsn, String idInd, String idISA) throws Exception;
	
	// Andy 2016.1.19 18:16
	List<Map<String, Object>> querySolutionMapForUseCase(String idIsn) throws Exception;
	List<Map<String, Object>> querySolutionMapForSolution(String idIsn) throws Exception;

	List<Map<String, Object>> queryIdentifiedBuyers(String idIsn)
			throws Exception;
}
