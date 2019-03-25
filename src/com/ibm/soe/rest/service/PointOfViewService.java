package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface PointOfViewService {
	
	public List<Map<String, String>> getPointOfViews(String uid) throws Exception;
	
	public Map<String, String> insertPointOfView(Map<String, String> map) throws Exception;
	
	public Map<String, String> updatePointOfView(Map<String, String> map) throws Exception;
	
	public int deletePointOfView(String povId) throws Exception;
	
	public List<Map<String, String>> getClientNames(String uid) throws Exception;
	
	public Map<String, String> checkSalesConn(String idSalesConn, String uid, String povId) throws Exception;
	
	public Map<String, String> getPointOfViewById(String povId) throws Exception;
	
	public List<Map<String, Object>> getPOVDetails(String povId) throws Exception;
	
	public Map<String, Object> getPOVTopicDetails(String stateId, String topicId) throws Exception;

	public Map<String, Object> changeModuleStatus(Map<String, String> param) throws Exception;

	public Map<String, Object> changeTopicStatus(Map<String, String> param) throws Exception;

	public Map<String, Object> saveTopicNotes(Map<String, String> param) throws Exception;
	
	public List<Map<String, String>> getAllNotes(String povId) throws Exception;

}
