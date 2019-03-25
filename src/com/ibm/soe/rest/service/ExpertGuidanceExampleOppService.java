package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface ExpertGuidanceExampleOppService {

	public Map<String, Object> checkIfHaveOwnOpp(String idUser, String idOpp) throws Exception;
	
	public Map<String, String> viewExampleOpp(String idOpp) throws Exception;
	
	public Map<String, Object> getExampleOppDetails(String idUser, String idOpp) throws Exception;
	
	public Map<String, String> getATNotesById(String idState);
	
	public List<Map<String, String>> getGuidanceQuestions(String idTask, String idState) throws Exception;
}
