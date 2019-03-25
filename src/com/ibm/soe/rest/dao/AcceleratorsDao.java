package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface AcceleratorsDao {

	public List<Map<String, String>> getInitIndustry();
	
	public List<Map<String, String>> getInitDocType();
	
}
