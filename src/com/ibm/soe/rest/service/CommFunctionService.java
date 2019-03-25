package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.dom4j.Element;

public interface CommFunctionService {
	Map<String, Object> getDropDownListData();

	Map<String, String> getMyProfile(String uid);
	Map<String, String> insertMyProfile(Map<String, String> param);
	Map<String, String> insertLoginHistory(Map<String, String> param);
	Map<String, String> updateMyProfile(Map<String, String> param);
	
	// Andy 2016.3.30 17:20
	boolean autoUpdateRedbooks(String table, String filePath);
	boolean truncateTableData(String table);
	boolean autoBatchUpdateRedbooks(String table, List<Element> childElements);
	
	boolean updateGISCAsset(Map<String,Object> giscAssetMap, List<Map<String, Object>> idIsnRelationshipList);
	boolean updateGISCAsset(Map<String, Object> giscAssetMap, List<Map<String, Object>> idIsnRelationshipList, CountDownLatch countDownLatch);
}
