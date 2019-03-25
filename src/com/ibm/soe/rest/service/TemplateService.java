package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface TemplateService {

	public List<Map<String, String>> getIndustry(Map<String,String> param) throws Exception;
	
	public Map<String, Object> getTemplate(Map<String,String> param) throws Exception;
}
