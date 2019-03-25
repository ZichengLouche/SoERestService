package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface BluePrintFilterService {

	// Andy 2016.2.24 16:35
	List<Map<String, Object>> getIndustries(Object[] sqlParamter);
	List<Map<String, Object>> getImperatives(Object[] sqlParamter);
	List<Map<String, Object>> getSolutions(Object[] sqlParamter);
	List<Map<String, Object>> getBuyers(Object[] sqlParamter);
	List<Map<String, Object>> getGeographies(Object[] sqlParamter);
	List<Map<String, Object>> getStrategics(Object[] sqlParamter);
	
}
