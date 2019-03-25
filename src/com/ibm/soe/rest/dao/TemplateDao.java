package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface TemplateDao {

	public List<Map<String, String>> getIndustry(String industryId) throws Exception;
	
	public List<Map<String, String>> getTemplate(String industryId) throws Exception;
}
