package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface ExpertGuidanceService {

	public List<Map<String, String>> getClientOpportunities(String uid)
			throws Exception;

	public Map<String, String> insertClientOpportunities(Map<String, String> map)
			throws Exception;

	public Map<String, String> updateClientOpportunities(Map<String, String> map)
			throws Exception;

	public Map<String, String> getClientOpportunityByOppId(String oid)
			throws Exception;

	public List<Map<String, String>> getClientNames(String uid)
			throws Exception;

	public List<Map<String, Object>> getOppDetails(String uid, String oid)
			throws Exception;

	public Map<String, Object> changeStatus(Map<String, Object> map)
			throws Exception;

	public Map<String, String> checkSalesConn(Map<String, String> map)
			throws Exception;

	public int parseScore(int i, int m1);

	public Map<String, String> addShareOppRecord(String oppId, String ownerUId,
			String shareUId) throws Exception;

	public int checkShareWith(String oppId, String shareUId) throws Exception;

	public List<String> getSharedWith(String oppId) throws Exception;

	public List<String> getSharedBy(String oppId, String shareUId)
			throws Exception;

	public Map<String, String> getOppDetailById(String sid);

	// GO DYNAMIC WITH SALES CONNECT
	public Map<String, Object> getSalesConnectOpps(String uid, Map<String, String> pageParams) throws Exception;
	
	public Map<String, Object> getSalesConnectOpps(String uid) throws Exception;

	public List<Map<String, String>> getTeamSDNextClientOpps(String uid)
			throws Exception;

	public List<String> addAllSalesConnectOpps(
			List<Map<String, String>> map, String uid) throws Exception;

	public String addSalesConnectOpp(Map<String, String> map,
			String uid) throws Exception;

	public void associateSalesConnectOpp(
			Map<String, String> map, String oid, String uid) throws Exception;
	
	public Map<String, String> getClientOpportunity(String uid, String oid) throws Exception;
	
	public List<Map<String, String>> getTheClientOpps(String uid, List<String> oids) throws Exception;

	public List<Map<String, String>> getGuidanceQuestions(String idTask, String idState) throws Exception;
	
	
	public int deleteOpp(Map<String, String> map) throws Exception;
	
	public int archiveOpp(Map<String, String> map) throws Exception;
	
	public int retrieveOpp(Map<String, String> map) throws Exception;
	
	public List<Map<String, String>> getArchiveOpps(String idUser) throws Exception;

	public Map<String, String> transferOpps(String oppId, String ownerUId,
			String toUid, String type) throws Exception;

	public List<Map<String, String>> getAllTransferOpps(String uid) throws Exception;

	public int getAllTransferOppCount(String uid) throws Exception;

	public void processTransferOpp(Map<String, String> map) throws Exception;

	public int updateTransferOppStatus(String uid);

	public List<Map<String, String>> getUserClientNames(String uid) throws Exception;

	public Map<String, String> updateShareListForOpp(String ownerUId, String oppId, List<String> addedUsers,
			List<String> removedUsers) throws Exception;
	
	public List<Map<String, String>> getClientIdentifier(String clientName) throws Exception;
}
