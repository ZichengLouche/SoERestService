package com.ibm.soe.rest.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.ExpertGuidanceDao;
import com.ibm.soe.rest.dao.SalesConnectDao;
import com.ibm.soe.rest.service.ExpertGuidanceService;
import com.ibm.soe.rest.util.Const;

@Service
@Transactional
public class ExpertGuidanceServiceImpl implements ExpertGuidanceService {

	@Autowired
	private ExpertGuidanceDao expertGuidanceDao;
	
	@Autowired
	private SalesConnectDao salesConnectDao;
	
	public List<Map<String, String>> getClientOpportunities(String uid) throws Exception {
		List<Map<String, String>> list = expertGuidanceDao.getClientOpportunities(uid);
		
		//Make the calculation
		for(int i = 0; i < list.size(); i ++) {
			Map<String, String> map = list.get(i);
			int U = Integer.parseInt(map.get("U"));
			int E = Integer.parseInt(map.get("E"));
			int D = Integer.parseInt(map.get("D"));
			int I = Integer.parseInt(map.get("I"));
			int CV = Integer.parseInt(map.get("CV"));
			
			int scoreU = parseScore(0, U);
			int scoreE = parseScore(U, E);
			int scoreD = parseScore(E, D);
			int scoreI = parseScore(D, I);
			int scoreCV = parseScore(I, CV);
			
			map.put("PG", String.valueOf(scoreU + scoreE + scoreD + scoreI + scoreCV));
		}
		return list;
	}
	
	public Map<String, String> getClientOpportunity(String uid, String oid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map = expertGuidanceDao.getClientOpportunity(uid, oid);
		
		//Make the calculation
		int U = Integer.parseInt(map.get("U"));
		int E = Integer.parseInt(map.get("E"));
		int D = Integer.parseInt(map.get("D"));
		int I = Integer.parseInt(map.get("I"));
		int CV = Integer.parseInt(map.get("CV"));
		
		int scoreU = parseScore(0, U);
		int scoreE = parseScore(U, E);
		int scoreD = parseScore(E, D);
		int scoreI = parseScore(D, I);
		int scoreCV = parseScore(I, CV);
		
		map.put("PG", String.valueOf(scoreU + scoreE + scoreD + scoreI + scoreCV));
		return map;
	}
	
	public int parseScore(int prevStatus, int CurrStatus) {
		int score = 0;
		if (CurrStatus == Const.STATUS_NOT_START) {
			score = 0;
		} else if (CurrStatus == Const.STATUS_IN_PROGRESS) {
			if (prevStatus == Const.STATUS_COMPLETED) {
				score = 10;
			} else {
				score = 5;
			}
		} else if (CurrStatus == Const.STATUS_COMPLETED) {
			score = 20;
		}
		return score;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> changeStatus(Map<String, Object> param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		@SuppressWarnings("unchecked")
		List<Map<String, String>> questions = (List<Map<String, String>>) param.get("questions");
		param.remove("questions");
		Map<String, String> map = new HashMap<String, String>();
		for (String key : param.keySet()) {
			map.put(key, (String) param.get(key));
		}
		//nodeActivity = true means Activity, nodeActivity = false means Task
		boolean nodeActivity = map.get("tid") == null || "".equals(map.get("tid")) ? true : false;
		boolean exists =  map.get("sid") != null && !"".equals(map.get("sid")) ? true : false;
		
		if (exists) {
			expertGuidanceDao.updateActivityOrTask(map);
		} else {
			long sid = expertGuidanceDao.insertActivityOrTask(map);
			map.put("sid", String.valueOf(sid));
		}
		
		if (nodeActivity) {
			expertGuidanceDao.syncTasksForActivity(map);//update the children tasks status
		} else {
			expertGuidanceDao.syncActivityForTasksAllChecked(map);//update the parent activity status if all brother tasks are complete
		}
		
		expertGuidanceDao.updateOppLastUpdate(map.get("oid"));//update opportunity
		
		/* save Guidance Questions in Notes Field */
		if (questions != null && !questions.isEmpty()) {
			String idAnswer = null, idQuestion = null, choice = null, notes = null;
			String idState = map.get("sid");
			for (Map<String, String> qu : questions) {
				idAnswer = qu.get("idAnswer");
				idQuestion = qu.get("idQuestion");
				choice = qu.get("choice");
				notes = qu.get("notes");
				if (idAnswer == null || "".equals(idAnswer)) {
					expertGuidanceDao.insertTaskNotesAnswer(idState, idQuestion, choice, notes);
				} else {
					expertGuidanceDao.updateTaskNotesAnswer(idAnswer, choice, notes);
				}
			}
		}
		
		resultMap.put("left", getClientOpportunity(map.get("uid"), map.get("oid")));
		resultMap.put("right", getOppDetails(map.get("uid"), map.get("oid")));
		
		return resultMap;
	}
	
	public Map<String, String> checkSalesConn(Map<String, String> map) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		boolean exists = true;
		
		String idSalesConn = map.get("idSalesConnect") == null || "".equals(map.get("idSalesConnect")) ? "" : map.get("idSalesConnect");
		String uid = map.get("uid") == null || "".equals(map.get("uid")) ? "" : map.get("uid");
		String oid = map.get("oid") == null || "".equals(map.get("oid")) ? "" : map.get("oid");
		
		List<Map<String, String>> list = expertGuidanceDao.getOppIdSalesConnList(idSalesConn, uid);
		
		if ("".equals(oid)) {
			if (list.size() > 0) {
				exists = true;
			} else {
				exists = false;
			}
		} else {
			Map<String, String> containsMap = new HashMap<String, String>();
			containsMap.put("OID", oid);
			if (list.contains(containsMap)) {
				if (list.size() == 1) {
					exists = false;
				} else {
					exists = true;
				}
			} else {
				if (list.size() > 0) {
					exists = true;
				} else {
					exists = false;
				}
			}
		}
		result.put("exists", exists + "");
		return result;
	}
	
	@Transactional
	public Map<String, String> insertClientOpportunities(Map<String, String> map) throws Exception {
		return expertGuidanceDao.insertClientOpportunities(map);
	}
	
	@Transactional
	public Map<String, String> updateClientOpportunities(Map<String, String> map) throws Exception {
		expertGuidanceDao.updateClientOpportunities(map);
		return getClientOpportunity(map.get("uid"), map.get("oid"));
	}
	
	public Map<String, String> getClientOpportunityByOppId(String oid) throws Exception {
		return expertGuidanceDao.getClientOpportunityByOppId(oid);
	}
	
	public List<Map<String, String>> getClientNames(String uid) throws Exception {
		return expertGuidanceDao.getClientNames(uid);
	}
	
	public List<Map<String, Object>> getOppDetails(String uid, String oid) throws Exception {
		return expertGuidanceDao.getOppDetails(uid, oid);
	}

	public Map<String, String> addShareOppRecord(String oppId, String ownerUId, String shareUId) throws Exception {
		expertGuidanceDao.insertShareOppRecord(oppId, shareUId);
		return getClientOpportunity(ownerUId, oppId);
	}
	
	public int checkShareWith(String oppId, String shareUId)
			throws Exception {
		return expertGuidanceDao.checkShareWith(oppId, shareUId);
	}
	
	public List<String> getSharedWith(String oppId) throws Exception {
		return expertGuidanceDao.getSharedWith(oppId);
	}
	
	public List<String> getSharedBy(String oppId, String shareUId) throws Exception {
		return expertGuidanceDao.getSharedBy(oppId, shareUId);
	}

	public Map<String, String> getOppDetailById(String sid) {
		return expertGuidanceDao.getOppDetailById(sid);
	}
	
	//query one page of Sales Connect Opportunities
	public Map<String, Object> getSalesConnectOpps(String uid, Map<String, String> pageParams) throws Exception {
		long total = 0;
		List<Map<String, String>> list = null;
		
		int pageIndex = pageParams.get("pageIndex") == null ? 0 : Integer.parseInt(pageParams.get("pageIndex"));
		int pageSize = Integer.parseInt(pageParams.get("pageSize"));
		String orderBy = pageParams.get("orderBy");
		String order = pageParams.get("order");
		int to = (pageIndex + 1) * pageSize;	
		int from  = to - pageSize + 1;	
		list = salesConnectDao.getClientOpportunities(uid, orderBy, order, from, to);
		total = salesConnectDao.getClientOpportunitiesCount(uid);
		
		this.getAssociateDate(list, uid);
		
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("list", list);
		results.put("total", total);
		return results;
	}

	//query all Sales Connect Opportunities
	public Map<String, Object> getSalesConnectOpps(String uid) throws Exception {
		
		List<Map<String, String>> list = salesConnectDao.getClientOpportunities(uid);
		long total = list.size();
		
		this.getAssociateDate(list, uid);
		
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("list", list);
		results.put("total", total);
		return results;
	}
	
	//get the Date that OPPS added/associated to TeamSD Next
	@SuppressWarnings("unchecked")
	private void getAssociateDate(List<Map<String, String>> scOpps, String uid) throws Exception {
		if (!scOpps.isEmpty()) {
			Map<String, Object> mapAssociateDate = expertGuidanceDao.getAssociateDate(uid);
			Map<String, Object> associateDate = null;
			for (Map<String, String> item : scOpps) {
				associateDate =  (Map<String, Object>) mapAssociateDate.get(item.get("SC_OID"));
				item.put("AD", associateDate == null ? "" : (String)associateDate.get("AD"));
				item.put("AD_SORT", associateDate == null ? "" : (String)associateDate.get("associateDate"));
			}
		}
	}

	public List<Map<String, String>> getTeamSDNextClientOpps(String uid)
			throws Exception {
		return expertGuidanceDao.getMyClientOpps(uid);
	}

	@Transactional
	public List<String> addAllSalesConnectOpps(
			List<Map<String, String>> list, String uid) throws Exception {
		List<String> oids = new ArrayList<String>();
		for (Map<String, String> opp : list) {
			String cid = expertGuidanceDao.checkClientAssociated(opp.get("sc_cid"), uid);
			if (cid == null) {
				cid = expertGuidanceDao.addSalesConnectClient(opp, uid);
			} else {
				expertGuidanceDao.associateSalesConnectClient(opp, cid);
			}
			oids.add(expertGuidanceDao.addSalesConnectOpportunity(opp, cid, uid));
		}
		
		return oids;
	}
	
	public List<Map<String, String>> getTheClientOpps(String uid, List<String> oids) throws Exception {
		List<Map<String, String>> opps = expertGuidanceDao.getClientOppsAdded(uid, oids.get(0), oids.get(oids.size() - 1));
		//Make the calculation
		for(int i = 0; i < opps.size(); i ++) {
			Map<String, String> map = opps.get(i); 
			int U = Integer.parseInt(map.get("U"));
			int E = Integer.parseInt(map.get("E"));
			int D = Integer.parseInt(map.get("D"));
			int I = Integer.parseInt(map.get("I"));
			int CV = Integer.parseInt(map.get("CV"));
			
			int scoreU = parseScore(0, U);
			int scoreE = parseScore(U, E);
			int scoreD = parseScore(E, D);
			int scoreI = parseScore(D, I);
			int scoreCV = parseScore(I, CV);
			
			map.put("PG", String.valueOf(scoreU + scoreE + scoreD + scoreI + scoreCV));
		}
		return opps;
	}

	@Transactional
	public String addSalesConnectOpp(Map<String, String> scOpp, String uid)
			throws Exception {
		String cid = expertGuidanceDao.checkClientAssociated(scOpp.get("sc_cid"), uid);
		if (cid == null) {
			cid = expertGuidanceDao.addSalesConnectClient(scOpp, uid);
		} else {
			expertGuidanceDao.associateSalesConnectClient(scOpp, cid);
		}
		return expertGuidanceDao.addSalesConnectOpportunity(scOpp, cid, uid);
//		return getClientOpportunity(uid, oid);
	}

	@Transactional
	public void associateSalesConnectOpp(Map<String, String> scOpp, String oid, String uid)
			throws Exception {
		String cid = expertGuidanceDao.checkClientAssociated(scOpp.get("sc_cid"), uid);
		if (cid == null) {
			cid = expertGuidanceDao.addSalesConnectClient(scOpp, uid);
		} else {
			expertGuidanceDao.associateSalesConnectClient(scOpp, cid);
		}
		expertGuidanceDao.associateSalesConnectOpportunity(scOpp, cid, oid);
//		return getClientOpportunity(uid, oid);
	}
	
	public List<Map<String, String>> getGuidanceQuestions(String idTask, String idState) 
			throws Exception {
		return expertGuidanceDao.getGuidanceQuestions(idTask, idState);
	}

	
	
	@Transactional
	public int deleteOpp(Map<String, String> map) 
			throws Exception {
		return expertGuidanceDao.deleteOpp(map);
	}
	
	@Transactional
	public int archiveOpp(Map<String, String> map) 
			throws Exception {
		return expertGuidanceDao.archiveOpp(map);
	}
	
	@Transactional
	public int retrieveOpp(Map<String, String> map) 
			throws Exception {
		return expertGuidanceDao.retrieveOpps(map);
	}
	
	public List<Map<String, String>> getArchiveOpps(String idUser) 
			throws Exception {
		return expertGuidanceDao.getArchiveOpps(idUser);
	}

	public Map<String, String> transferOpps(String oppId, String ownerUId, String toUid, String type) 
			throws Exception {
		expertGuidanceDao.transferOpps(oppId, ownerUId, toUid, type);
		return getClientOpportunity(ownerUId, oppId);
	}

	@Override
	public List<Map<String, String>> getAllTransferOpps(String uid) throws Exception {
		return expertGuidanceDao.getAllTransferOpps(uid);
	}

	@Override
	public int getAllTransferOppCount(String uid) throws Exception {
		return expertGuidanceDao.getAllTransferOppCount(uid);
	}

	@Override
	@Transactional
	public void processTransferOpp(Map<String, String> map) throws Exception {
		expertGuidanceDao.processTransferOpp(map);
	}

	@Override
	@Transactional
	public int updateTransferOppStatus(String uid) {
		// TODO Auto-generated method stub
		return expertGuidanceDao.updateTransferOppStatus(uid);
	}
	
	public List<Map<String, String>> getUserClientNames(String uid) throws Exception {
		return expertGuidanceDao.getUserClientNames(uid);
	}

	public Map<String, String> updateShareListForOpp(String ownerUId, String oppId, List<String> addedUsers, List<String> removedUsers) throws Exception {
		expertGuidanceDao.updateShareListForOpp(oppId, addedUsers, removedUsers);
		return getClientOpportunity(ownerUId, oppId);
	}
	
	public List<Map<String, String>> getClientIdentifier(String clientName) throws Exception {
		List<Map<String, String>> list_clients = expertGuidanceDao.getClientIdentifier(clientName);
		//John Zhu 2016.11.21. list the GBG results first and then Global Clients. In each group, name is in alphabetic order. See story 1270999
		//Sorting work already done in DB SQL, remove the list sorting code here.
		
		if (list_clients.size() > 100) {
			return list_clients.subList(0, 100);
		} else {
			return list_clients;
		}
	}
}
