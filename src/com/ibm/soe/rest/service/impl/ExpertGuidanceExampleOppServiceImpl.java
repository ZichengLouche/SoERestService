package com.ibm.soe.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.ExpertGuidanceDao;
import com.ibm.soe.rest.dao.ExpertGuidanceExampleOppDao;
import com.ibm.soe.rest.dao.SalesConnectDao;
import com.ibm.soe.rest.service.ExpertGuidanceExampleOppService;
import com.ibm.soe.rest.service.ExpertGuidanceService;
import com.ibm.soe.rest.util.Const;

@Service
@Transactional
public class ExpertGuidanceExampleOppServiceImpl implements ExpertGuidanceExampleOppService {

	@Autowired
	private ExpertGuidanceExampleOppDao expertGuidanceExampleOppDao;
	
	public Map<String, Object> checkIfHaveOwnOpp(String idUser, String idOpp) throws Exception {
		String ownOppCnt = expertGuidanceExampleOppDao.checkIfHaveOwnOpp(idUser);
		String archivedOppCnt = expertGuidanceExampleOppDao.getArchivedOppCnt(idUser);
		Map<String, Object> map = new HashMap<String, Object>();
		if (!"0".equals(ownOppCnt)) {
			map.put("ownOppCnt", ownOppCnt);
			map.put("archivedOppCnt", archivedOppCnt);
			return map;
		} else {
			map = this.getExampleOppDetails("example@xx.ibm.com", "1");
			map.put("ownOppCnt", ownOppCnt);
			map.put("archivedOppCnt", archivedOppCnt);
			return map;
		}
	}
	
	public Map<String, Object> getExampleOppDetails(String idUser, String idOpp) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> clientOpportunity = expertGuidanceExampleOppDao.getExampleClientOppDetails(idUser, idOpp);
		List<Map<String, Object>> activities = expertGuidanceExampleOppDao.getExampleOppActivityAndTaskDetails(idUser, idOpp);
		map.put("left", clientOpportunity);
		map.put("right", activities);
		return map;
	}
	
	public Map<String, String> viewExampleOpp(String idOpp) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map = expertGuidanceExampleOppDao.viewExampleOpp(idOpp);
		return map;
	}
	
	public Map<String, String> getATNotesById(String idState) {
		Map<String, String> map = new HashMap<String, String>();
		map = expertGuidanceExampleOppDao.getATNotesById(idState);
		return map;
	}
	
	public List<Map<String, String>> getGuidanceQuestions(String idTask, String idState) 
			throws Exception {
		return expertGuidanceExampleOppDao.getGuidanceQuestions(idTask, idState);
	}
}
