package com.ibm.soe.rest.service;

import java.util.Map;

public interface ProductInformationService {

	
	public Map<String, Object> queryProductInformationResults(String brand, String[] growthPlays, String source, String date, String keyword,
			String sortName, String sortBy, String idUser, String pageIndex, String pageLimit) throws Exception;

	public Map<String, Object> queryProductionFilter(String brand, String[] growthPlays, String source, String date, String keyword) throws Exception;
	
	public void insertUseful(Map<String,String> param)throws Exception;
}
