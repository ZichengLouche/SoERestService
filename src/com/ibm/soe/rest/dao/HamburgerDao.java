package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;


public interface HamburgerDao {

	List<Map<String, String>> getHelpContent() throws Exception;

	Map<String, String> getAboutContent();
	
}
