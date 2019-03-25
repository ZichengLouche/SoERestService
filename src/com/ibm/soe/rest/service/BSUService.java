package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface BSUService {

	// Andy 2016.1.12 22:02
	Map<String, Object> queryBSUResults(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String pageIndex, String pageLimit, String idIsn) throws Exception;

	Map<String, Object> queryBSUFilter(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn, String multipleIndustry) throws Exception;
	
	// Andy 2016.1.14 22:25
	List<Map<String, Object>> querySolutionPart(String idSolution, String detailsAbbrev, final String isLaptopEnv, final String reg) throws Exception;
	List<Map<String, Object>> queryContacts(String idIsn, String idInd, String idISA) throws Exception;
	
	// Andy 2016.1.19 18:16
	List<Map<String, Object>> querySolutionMapForUseCase(String idIsn) throws Exception;
	List<Map<String, Object>> querySolutionMapForSolution(String idIsn) throws Exception;

	List<Map<String, Object>> queryIdentifiedBuyers(String idIsn)
			throws Exception;
}
