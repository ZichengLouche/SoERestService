package com.ibm.soe.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.CommFunctionDao;
import com.ibm.soe.rest.service.CommFunctionService;

@Service("commFunctionService")
@Transactional
public class CommFunctionServiceImpl implements CommFunctionService {
	@Autowired
	private CommFunctionDao commFunctionDao;
	
	public Map<String, String> getMyProfile(String uid) {
		return commFunctionDao.getMyProfile(uid);
	}
	
	public Map<String, Object> getDropDownListData() {
		Map<String, Object> result = new HashMap<String, Object>();
		
		//Construct Geography drop down list data
		Map<String, String> geoMap = new HashMap<String, String>();
		geoMap.put("All", "All");
		geoMap.put("AP", "AP");
		geoMap.put("EU", "EU");
		geoMap.put("GCG", "GCG");
		geoMap.put("JP", "JP");
		geoMap.put("MEA", "MEA");
		geoMap.put("NA", "NA");
		geoMap.put("LA", "LA");
		geoMap.put("WW", "WW");
		
		//Construct Sales Cycle drop down list data
		Map<String, String> salesCycleMap = new HashMap<String, String>();
		salesCycleMap.put("Presales", "Presales");
		salesCycleMap.put("Delivery", "Delivery");
		salesCycleMap.put("Other", "Other");
		
		
		result.put("industries", commFunctionDao.getIndustries());
		result.put("geography", geoMap);
		result.put("salesCycle", salesCycleMap);
		result.put("geography", geoMap);
		
		return result;
	}
	
	public Map<String, String> insertMyProfile(Map<String, String> param) {
		return commFunctionDao.insertProfile(param);
	}
	
	public Map<String, String> insertLoginHistory(Map<String, String> param) {
		return commFunctionDao.insertLoginHistory(param);
	}
	
	public Map<String, String> updateMyProfile(Map<String, String> param) {
		return commFunctionDao.updateProfile(param);
	}

	// Andy 2016.3.30 17:20
	@Override
	public boolean autoUpdateRedbooks(String table, String filePath) {
		commFunctionDao.truncateTableData(table);
		
		return commFunctionDao.autoUpdateRedbooks(filePath);
	}
	
	// Andy 2016.4.5 17:15
	@Override
	public boolean autoBatchUpdateRedbooks(String table, List<Element> childElements) {
		commFunctionDao.truncateTableData(table);
		
		return commFunctionDao.autoBatchUpdateRedbooks(childElements);
	}

	@Override
	public boolean truncateTableData(String table) {
		return commFunctionDao.truncateTableData(table);
	}
	
	// Andy 2016.5.26 21:19
	@SuppressWarnings("unchecked")
	@Override
	public boolean updateGISCAsset(Map<String,Object> giscAssetMap, List<Map<String, Object>> idIsnRelationshipList) {
		List<Map<String,Object>> giscAssetList = new ArrayList<Map<String,Object>>();
		Integer idISN = 0;
		
		List<Map<?,?>> solutionAreas = (ArrayList<Map<?,?>>) giscAssetMap.get("solutionAreas");
		if (solutionAreas != null && solutionAreas.size() > 0) {
			for (Map<?, ?> solutionAreaMap : solutionAreas) {
				Map<?, ?> imperativeMap = (Map<?, ?>) solutionAreaMap.get("imperative");
				Map<?, ?> industryMap = (Map<?, ?>) imperativeMap.get("industry");
				String solutionAreaName = (String) solutionAreaMap.get("solutionAreaName");
				String imperativeName = (String) imperativeMap.get("imperativeName");
				String industryName = (String) industryMap.get("industryName");
				
				Map<String,Object> newGiscAssetMap = new HashMap<String, Object>();
				newGiscAssetMap.put("idAsset", giscAssetMap.get("idAsset"));
				newGiscAssetMap.put("assetName", giscAssetMap.get("assetName"));
				giscAssetList.add(newGiscAssetMap);
				
				for (Map<String, Object> idIsnRelationshipMap : idIsnRelationshipList) {
					if(idIsnRelationshipMap.get("solutionName").equals(solutionAreaName) && idIsnRelationshipMap.get("imperativeName").equals(imperativeName) 
																						 && idIsnRelationshipMap.get("industryName").equals(industryName)) {
						idISN = (Integer) idIsnRelationshipMap.get("idISN");
						break;
					}
				}
				newGiscAssetMap.put("idISN", idISN);
			}
		} else {
			Map<String,Object> newGiscAssetMap = new HashMap<String, Object>();
			newGiscAssetMap.put("idISN", idISN);
			newGiscAssetMap.put("idAsset", giscAssetMap.get("idAsset"));
			newGiscAssetMap.put("assetName", giscAssetMap.get("assetName"));
			giscAssetList.add(newGiscAssetMap);
		}
		
		return commFunctionDao.batchUpdateGISCAsset(giscAssetList);
	}

	@Override
	public boolean updateGISCAsset(Map<String, Object> giscAssetMap, List<Map<String, Object>> idIsnRelationshipList, CountDownLatch countDownLatch) {
		return false;
	}
}
