package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface DownloadHistoryDao {

	public String insertDownloadHistory(Map<String, String> param) throws Exception ;
	
	public List<Map<String, String>> getDownloadHistory(Map<String, String> param) throws Exception;
	
	public boolean  updateHistoryHasRated(String idEntity,String idUser,String status,String entityType ) throws Exception ;
}
