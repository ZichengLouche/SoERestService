package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface BluePrintFilterDao {
	
	Map<String, Object> getAllCount(Object[] sqlParamter, String category);
	
	List<Map<String, Object>> getIndustries(Object[] sqlParamter);
	List<Map<String, Object>> getImperatives(Object[] sqlParamter);
	List<Map<String, Object>> getSolutions(Object[] sqlParamter);
	List<Map<String, Object>> getBuyers(Object[] sqlParamter);
	List<Map<String, Object>> getGeographies(Object[] sqlParamter);
	
	// Andy 2016.11.17 16:08
	List<Map<String, Object>> getInitiatives();
	List<Map<String, Object>> getStrategics(Object[] sqlParamter, List<Map<String, Object>> initiatives);
	
	// to-delete Andy 2016.2.26 17:30
	List<Map<String, String>> getOfferings(String keyWord, String camss, String idInd, String idIsa, String idIsn, String idIso, String idBuyer, String geo, String priority);
	
}
