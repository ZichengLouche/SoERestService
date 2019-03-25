package com.ibm.soe.rest.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.soe.rest.dao.BluePrintFilterDao;
import com.ibm.soe.rest.service.BluePrintFilterService;

@Service
public class BluePrintFilterServiceImpl implements BluePrintFilterService {
	@Autowired
	private BluePrintFilterDao bluePrintFilterDao;
	
	// Andy 2016.2.24 13:02
	public List<Map<String, Object>> getIndustries(Object[] sqlParamter) {		
		Map<String, Object> totalCountMap = bluePrintFilterDao.getAllCount(sqlParamter, "Industries");
		
		Object idInd = sqlParamter[7];
		// Andy 2016.8.5 15:56
		if(idInd != null) {
			String multipleIndustry = ((String)totalCountMap.get("ITEM")).replace("All", "Selected");
			totalCountMap.put("ITEM", multipleIndustry);
		}
		List<Map<String, Object>> filterResultList = bluePrintFilterDao.getIndustries(sqlParamter);
		
		filterResultList.add(0, totalCountMap);
		return filterResultList;
	}
	
	public List<Map<String, Object>> getImperatives(Object[] sqlParamter) {
		Map<String, Object> totalCountMap = bluePrintFilterDao.getAllCount(sqlParamter, "Solutions");
		List<Map<String, Object>> filterResultList = bluePrintFilterDao.getImperatives(sqlParamter);
		
		filterResultList.add(0, totalCountMap);
		return filterResultList;
	}
	
	public List<Map<String, Object>> getSolutions(Object[] sqlParamter) {
		Map<String, Object> totalCountMap = bluePrintFilterDao.getAllCount(sqlParamter, "Industry Use Cases");
		List<Map<String, Object>> filterResultList = bluePrintFilterDao.getSolutions(sqlParamter);
		
		filterResultList.add(0, totalCountMap);
		return filterResultList;		
	}
	
	public List<Map<String, Object>> getBuyers(Object[] sqlParamter) {		
		Map<String, Object> totalCountMap = bluePrintFilterDao.getAllCount(sqlParamter, "Buyers");
		List<Map<String, Object>> filterResultList = bluePrintFilterDao.getBuyers(sqlParamter);
		
		filterResultList.add(0, totalCountMap);
		return filterResultList;		
	}
	
	public List<Map<String, Object>> getGeographies(Object[] sqlParamter) {		
		List<Map<String, Object>> filterResultList = bluePrintFilterDao.getGeographies(sqlParamter);
		return filterResultList;
	}
	
	// Andy 2016.11.17 21:47
	public List<Map<String, Object>> getStrategics(Object[] sqlParamter) {		
		Map<String, Object> totalCountMap = bluePrintFilterDao.getAllCount(sqlParamter, "Strategic Imperatives");
		List<Map<String, Object>> initiatives = bluePrintFilterDao.getInitiatives();
		List<Map<String, Object>> strategics = bluePrintFilterDao.getStrategics(sqlParamter, initiatives);
		
		List<Map<String, Object>> filterResultList = new ArrayList<Map<String,Object>>();
		filterResultList.add(0, totalCountMap);
		for (Map<String, Object> strategic : strategics) {
			if (strategic.get("isnCount") != null && !"0".equals(strategic.get("isnCount").toString()) ) {
				filterResultList.add(strategic);
			}
		}
		return filterResultList;		
	}

}
