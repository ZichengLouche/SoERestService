package com.ibm.soe.rest.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.PointOfViewDao;
import com.ibm.soe.rest.service.ExpertGuidanceService;
import com.ibm.soe.rest.service.PointOfViewService;

@Service
@Transactional
public class PointOfViewServiceImpl implements PointOfViewService{

//	@Autowired
	private PointOfViewDao pointOfViewDao;
	
	@Autowired
	private ExpertGuidanceService expertGuidanceService;
	
	@Override
	public List<Map<String, String>> getPointOfViews(String uid)
			throws Exception {
		List<Map<String, String>> list = pointOfViewDao.getPointOfViews(uid);
		
		//Make the Module states calculation
		String[] seqs = null;
		String[] sts = null;
		Map<String, String> map = null;
		for(int i = 0; i < list.size(); i++) {
			map = list.get(i);
			seqs = map.get("SEQS").split(",");
			sts = map.get("STS").split(",");
			for (int j = 0; j < seqs.length; j++) {
				map.put("M" + seqs[j], sts[j]);
			}
			int M1 = getModuleStatus(map.get("M1"));
			int M2 = getModuleStatus(map.get("M2"));
			int M3 = getModuleStatus(map.get("M3"));
			int M4 = getModuleStatus(map.get("M4"));
			int M5 = getModuleStatus(map.get("M5"));
			int M6 = getModuleStatus(map.get("M6"));
			int M7 = getModuleStatus(map.get("M7"));
			int M8 = getModuleStatus(map.get("M8"));
			
			int scoreM1 = expertGuidanceService.parseScore(0, M1);
			int scoreM2 = expertGuidanceService.parseScore(M1, M2);
			int scoreM3 = expertGuidanceService.parseScore(M2, M3);
			int scoreM4 = expertGuidanceService.parseScore(M3, M4);
			int scoreM5 = expertGuidanceService.parseScore(M4, M5);
			int scoreM6 = expertGuidanceService.parseScore(0, M1);
			int scoreM7 = expertGuidanceService.parseScore(M1, M2);
			int scoreM8 = expertGuidanceService.parseScore(M2, M3);
			
//			map.put("PG", String.valueOf(scoreU + scoreE + scoreD + scoreI + scoreCV));
		}
		return list;
	}
	
	public int getModuleStatus(String oriStatus) {
		if ("0".equals(oriStatus)) {
			return 1;
		} else if ("1".equals(oriStatus)) {
			return 2;
		}
		return 0;
	}

	@Override
	public Map<String, String> insertPointOfView(Map<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> updatePointOfView(Map<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deletePointOfView(String povId) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, String>> getClientNames(String uid)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> checkSalesConn(String idSalesConn, String uid,
			String povId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getPointOfViewById(String povId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getPOVDetails(String povId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getPOVTopicDetails(String stateId, String topicId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> changeModuleStatus(Map<String, String> param)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> changeTopicStatus(Map<String, String> param)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> saveTopicNotes(Map<String, String> param)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> getAllNotes(String povId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
