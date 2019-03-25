package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface ProductionInformationDao {

	public void insertuseful(String idEntity,String entityType,String idUser,String camss,String keyword,String brand,String source,String datelastUpdate)throws Exception;

	public void deleteUseful(String idEntity,String entityType,String idUser)throws Exception;

	public List<Map<String, String>> queryProductionTeclineResults(
			String brand, String[] growthPlays, String source, String date,
			String keyword, String idUser) throws Exception;

	public List<Map<String, String>> queryProductionRedbookResults(
			String brand, String[] growthPlays, String source, String date,
			String keyword, String idUser) throws Exception;

	public List<Map<String, String>> queryTeclineBrandFilterCount(String brand,
			String[] growthPlays, String source, String date, String keyword) throws Exception;

	public List<Map<String, String>> queryRedbookBrandFilterCount(String brand,
			String[] growthPlays, String source, String date, String keyword) throws Exception;

	public List<Map<String, String>> queryTeclineSourceFilterCount(
			String brand, String[] growthPlays, String source, String date,
			String keyword) throws Exception;

	public List<Map<String, String>> queryRedbookSourceFilterCount(
			String brand, String[] growthPlays, String source, String date,
			String keyword) throws Exception;

	public List<Map<String, String>> queryTeclineDateFilterCount(String brand,
			String[] growthPlays, String source, String date, String keyword) throws Exception;

	public List<Map<String, String>> queryRedbookDateFilterCount(String brand,
			String[] growthPlays, String source, String date, String keyword) throws Exception;
}
