package com.ibm.soe.rest.service;

import java.util.Map;


public interface HamburgerService {

	Map<String, Object> getHelpContent() throws Exception;

	Map<String, String> getAboutContent();


}
