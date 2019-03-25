package com.ibm.soe.rest.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.soe.rest.dao.GlobalTechSalesDao;
import com.ibm.soe.rest.service.GlobalTechSalesService;

@Service
public class GlobalTechSalesServiceImpl implements GlobalTechSalesService {
	
	@Autowired
	private GlobalTechSalesDao globalTechSalesDao;
	
	public Map<String, Object> getAllWebNavigators() throws Exception {
		Map<String, Object> results = new HashMap<String, Object>();
		List<Map<String, String>> allNavigators = globalTechSalesDao.getAllWebNavigators();
		for (Map<String, String> obj : allNavigators) {
			results.put(obj.get("iconName"), obj);
		}
		return results;
	}
}
