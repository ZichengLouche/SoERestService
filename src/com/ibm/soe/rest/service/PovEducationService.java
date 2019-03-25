package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface PovEducationService {
	
	public List<Map<String, Object>> getPovMainResults(String idUser) throws Exception;
	
	public Map<String, Object> getPovTopicDetails(String idTopic, String idState) throws Exception;
	
	public String getPovResourceContent(String idResource) throws Exception;
	
	public Map<String, String> getModuleState(String idUser, String idModule) throws Exception;
	
	public void checkModule(String idUser, String topicStates, String status) throws Exception;
	
	public void checkTopic(String idUser, String idState, String idTopic, String status) throws Exception;
	
	public void writeTopicNotes(String idUser, String idState, String idTopic, String notes, String status) throws Exception;
	
	public List<Map<String, String>> getViewAllNotes(String idUser) throws Exception;
	
	public List<Map<String, String>> getViewAllNotes(String idUser,String type,String oppID) throws Exception;
	
	public List<Map<String, String>> getMailSub(String oppID) throws Exception;
	
}
