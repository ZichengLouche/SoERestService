package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;

public interface CommFunctionDao {
	
	List<Map<String, String>> getIndustries();
	
	Map<String, String> getMyProfile(String uid);
	Map<String, String> insertProfile(Map<String, String> param);
	Map<String, String> insertLoginHistory(Map<String, String> param);
	Map<String, String> updateProfile(Map<String, String> param);

	// Andy 2016.3.30 17:20
	boolean autoUpdateRedbooks(String filePath);
	boolean truncateTableData(String table);
	boolean autoBatchUpdateRedbooks(final List<Element> childElements);

	// Andy 2016.5.27 16:49
	boolean updateGISCAsset(Integer idIsn, Integer idAsset, String assetName);
	boolean batchUpdateGISCAsset(final List<Map<String,Object>> giscAssetList);
}
