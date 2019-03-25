package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface ExpertGuidanceExampleOppDao {
	
	public String checkIfHaveOwnOpp(String idUser) throws Exception;
	
	public String getArchivedOppCnt(String idUser) throws Exception;
	
	public Map<String, String> viewExampleOpp(final String idOpp) throws Exception;
	
	public Map<String, String> getExampleClientOppDetails(String idUser, String idOpp) throws Exception;
	
	public List<Map<String, Object>> getExampleOppActivityAndTaskDetails(String idUser, String idOpp) throws Exception;
	
	public Map<String, String> getATNotesById(String idState);
	
	public List<Map<String, String>> getGuidanceQuestions(String idTask, String idState) throws Exception;
}
