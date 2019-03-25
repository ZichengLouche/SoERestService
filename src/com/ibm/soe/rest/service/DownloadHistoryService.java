package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface DownloadHistoryService {

	public String insertDownloadHistory(Map<String,String> param) throws Exception;
	
	public List<Map<String,String>> getDownloadHistory(Map<String,String> param) throws Exception;
	
	public void updateHistoryHasRated(Map<String,String> param) throws Exception;
}
