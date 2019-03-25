package com.ibm.soe.rest.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.HamburgerDao;
import com.ibm.soe.rest.service.HamburgerService;

@Service
@Transactional
public class HamburgerServiceImpl implements HamburgerService {

	@Autowired
	private HamburgerDao hamburgerDao;

	@Override
	public Map<String, Object> getHelpContent() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> results = new HashMap<String, Object>();
		List<Map<String, String>> helps = hamburgerDao.getHelpContent();
		for (Map<String, String> obj : helps) {
			results.put(obj.get("name"), obj);
		}
		return results;
	}

	@Override
	public Map<String, String> getAboutContent() {
		// TODO Auto-generated method stub
		return hamburgerDao.getAboutContent();
	}

}
