package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface SalesConnectDao {

	public List<Map<String, String>> getClientOpportunities(String uid) throws Exception;
	
	public List<Map<String, String>> getClientOpportunities(String uid, String orderBy, String order,
			int seqFrom, int seqTo) throws Exception;

	public long getClientOpportunitiesCount(String uid) throws Exception;
	
	public Map<String, String> checkIfUserExsitInSC(String uid) throws Exception;
	
	public Map<String, Integer> reportConnectIssue(Map<String, String> param) throws Exception;
}
