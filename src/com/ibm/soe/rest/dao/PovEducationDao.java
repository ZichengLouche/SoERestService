package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface PovEducationDao {

	public List<Map<String, Object>> getPovModuleList(String idUser) throws Exception;
	
	public List<Map<String, String>> getPovTopicList(String idUser, String idModule) throws Exception;
	
	public String getPovNotes(String idState) throws Exception;
	
	public List<Map<String, String>> getPovResourceList(String idTopic) throws Exception;
	
	public String getPovResourceContent(String idResource) throws Exception;
	
	public Map<String, String> getModuleState(String idUser, String idModule) throws Exception;
	
	public long addTopicStateCompleted(String idUser, String idTopic) throws Exception;
	
	public int editTopicStateCompleted(String idState) throws Exception;
	
	public int editTopicStateInprogress(String idState) throws Exception;
	
	public long addTopicNotes(String idUser, String idTopic, String notes, String status) throws Exception;
	
	public int editTopicNotes(String idState, String notes, String status) throws Exception;
	
	public List<Map<String, String>> getViewAllNotes(String idUser) throws Exception;
	
	public List<Map<String, String>> getViewAllNotes(String idUser,String type,String oppID) throws Exception;
	
	public List<Map<String, String>> getMailSub(String oppID) throws Exception;

}
