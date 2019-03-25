package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface SalesConnectService {
	
	public Map<String, String> checkIfUserExsitInSC(String uid) throws Exception;
	
	public Map<String, Integer> reportConnectIssue(Map<String, String> param) throws Exception;
	
}
