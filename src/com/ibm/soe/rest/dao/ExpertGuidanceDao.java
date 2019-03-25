package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface ExpertGuidanceDao {
	
	public List<Map<String, String>> getClientOpportunities(String uid) throws Exception;
	
	public Map<String, String> getClientOpportunity(String uid, String oid) throws Exception;
	
	public Map<String, String> insertClientOpportunities(Map<String, String> map) throws Exception;
	
	public void updateClientOpportunities(final Map<String, String> map);
	
	public Map<String, String> getClientOpportunityByOppId(String OppId) throws Exception;
	
	public List<Map<String, String>> getClientNames(String uid) throws Exception;
	
	public List<Map<String, Object>> getOppDetails(String uid, String oppId) throws Exception;
	
	public long insertActivityOrTask(final Map<String, String> map) throws Exception;
	
	public void updateActivityOrTask(final Map<String, String> map) throws Exception;
	
	public void syncTasksForActivity(Map<String, String> map) throws Exception;
	
	public void syncActivityForTasksAllChecked(Map<String, String> map) throws Exception;
	
	public List<Map<String, String>> getOppIdSalesConnList(String idSalesConn, String uid) throws Exception;
	
	public void updateOppLastUpdate(final String oid) throws Exception;

	public void insertShareOppRecord(final String oppId, final String shareUId) throws Exception;
	
	public Map<String, String> getOppDetailById(String sid);

	public int checkShareWith(String oppId, String shareUId) throws Exception;

	public List<String> getSharedWith(String oppId) throws Exception;
	
	public List<String> getSharedBy(String oppId, String shareUId) throws Exception;

	/*------------------ Go Dynamic: Sales Connect  -------------------*/
	public Map<String, Object> getAssociateDate(String uid) throws Exception;
	
	public List<Map<String, String>> getMyClientOpps(String uid) throws Exception;
	
	public String addSalesConnectClient(final Map<String, String> scOpp, final String uid) throws Exception;
	
	public String addSalesConnectOpportunity(final Map<String, String> scOpp, String cid, final String uid) throws Exception;
	
	public void associateSalesConnectClient(Map<String, String> scOpp, String cid) throws Exception;
	
	public void associateSalesConnectOpportunity(Map<String, String> scOpp, String cid, String oid) throws Exception;
	
	public String checkClientAssociated(String scCid, String uid) throws Exception;

	public List<Map<String, String>> getClientOppsAdded(String uid, String oidMin, String oidMax) throws Exception;

	/*------------- Guidance questions in Task notes field  -----------*/
	public List<Map<String, String>> getGuidanceQuestions(String idTask,
			String idState) throws Exception;
	
	public long insertTaskNotesAnswer(final String idState, final String idQuestion, final String choice, final String notes) throws Exception;
	
	public void updateTaskNotesAnswer(String idAnswer, String choice, String notes) throws Exception;

	
	
	public int deleteOpp(Map<String, String> map) throws Exception;
	
	public int archiveOpp(Map<String, String> map) throws Exception;
	
	public int retrieveOpps(Map<String, String> map) throws Exception;
	
	public List<Map<String, String>> getArchiveOpps(String idUser) throws Exception;

	public int transferOpps(String oppId,
			String ownerUId, String toUid, String type);

	public List<Map<String, String>> getAllTransferOpps(String uid) throws Exception;

	public int getAllTransferOppCount(String uid) throws Exception;

	public void processTransferOpp(Map<String, String> map) throws Exception;

	public int updateTransferOppStatus(String uid);

	public List<Map<String, String>> getUserClientNames(String uid) throws Exception;

	public void updateShareListForOpp(String oppId, List<String> addedUsers,
			List<String> removedUsers) throws Exception;
	
	public List<Map<String, String>> getClientIdentifier(String clientName) throws Exception;
}
