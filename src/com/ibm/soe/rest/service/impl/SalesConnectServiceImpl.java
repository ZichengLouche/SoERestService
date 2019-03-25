package com.ibm.soe.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.SalesConnectDao;
import com.ibm.soe.rest.service.SalesConnectService;

@Service
@Transactional
public class SalesConnectServiceImpl implements SalesConnectService {

	@Autowired
	private SalesConnectDao salesConnectDao;
	
	public Map<String, String> checkIfUserExsitInSC(String uid) throws Exception {
		Map<String, String> map = salesConnectDao.checkIfUserExsitInSC(uid);
		return map;
	}
	
	public Map<String, Integer> reportConnectIssue(Map<String, String> param) throws Exception {
		Map<String, Integer> map = salesConnectDao.reportConnectIssue(param);
		return map;
	}
}
