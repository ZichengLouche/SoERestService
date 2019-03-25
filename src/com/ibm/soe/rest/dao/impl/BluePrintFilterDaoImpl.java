package com.ibm.soe.rest.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.dao.BluePrintFilterDao;

@Repository
public class BluePrintFilterDaoImpl implements BluePrintFilterDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messages;
	
	@Value("${DB_NAME_ACCELERATORS}")
	private String DB_NAME_ACCELERATORS;
	
	@Value("${DB_NAME_ACCELERATOR_WRITE}")
	private String DB_NAME_ACCELERATOR_WRITE;
	
	// Andy 2016.2.24 13:02
	public Map<String, Object> getAllCount(Object[] sqlParamter, final String category) {
		String idInd = (String) sqlParamter[7];
		
		String sql = "SELECT COUNT(N.ID) AS total " +
					   "FROM " +
					         "( " +
					    	   "SELECT S.idKey AS ID " + getMainSql(false, idInd) +
					         ") N " ;
		
		Map<String, Object> result = jdbcTemplate.queryForObject(sql, sqlParamter, 
				new RowMapper<Map<String, Object>>() {
					public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("ID", "0");
						map.put("ITEM", "All " + category + " (" + rs.getString("total") + ")");
						return map;
					}
				});
		
		return result;
	}
	
	public List<Map<String, Object>> getIndustries(Object[] sqlParamter) {
		String idInd = (String) sqlParamter[7];
		
		String sql = "SELECT I.idKey                                              			 AS ID, " +
				     	    "CONCAT(I.Name, ' (', COUNT(DISTINCT(S.idKey)), ')')  			 AS ITEM " + getMainSql(false, idInd);
	
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, sqlParamter);
		return result;
	}
	
	public List<Map<String, Object>> getImperatives(Object[] sqlParamter) {
		String idInd = (String) sqlParamter[7];
		
		String sql = "SELECT SA.idKey                                                         AS ID, " +
							"CONCAT(SA.Imperative_Name, ' (', COUNT(DISTINCT(S.idKey)), ')')  AS ITEM " + getMainSql(false, idInd) + 
				     "ORDER BY SA.Imperative_Name " ;	//John Zhu 2016-12-22, defect 1391477, change display order to alpha-numeric order by name
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, sqlParamter);
		return result;
	}
	
	public List<Map<String, Object>> getSolutions(Object[] sqlParamter) {
		String idInd = (String) sqlParamter[7];
		
		String sql = "SELECT S.idKey                                              			  AS ID, " +
							"CONCAT(S.Name, ' (', COUNT(DISTINCT(S.idKey)), ')')  			  AS ITEM " + getMainSql(false, idInd) + 
					 "ORDER BY S.Name " ; //John Zhu 2016-12-22, defect 1391477, change display order to alpha-numeric order by name
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, sqlParamter);
		return result;
	}
	
	public List<Map<String, Object>> getBuyers(Object[] sqlParamter) {
		String idInd = (String) sqlParamter[7];
		
		String sql = "SELECT B.idKey                                   						 AS ID, " +
						    "CONCAT(B.Name, ' (', COUNT(DISTINCT S.idKey), ')')  			 AS ITEM, " +
						    "COUNT(DISTINCT S.idKey)                            			 AS CT " + getMainSql(true, idInd) +
				   "ORDER BY CT DESC " ;
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, sqlParamter);
		return result;
	}
	
	public List<Map<String, Object>> getGeographies(Object[] sqlParamter) {
		String idInd = (String) sqlParamter[7];
		
		String sql =  "SELECT geography.geographyValue, " +
						   	 "geography.geographyText, " +
						   	 "( " +
						       "CASE " +
						           "WHEN geography.geographyValue = 'AP' " +
						           "THEN solutionCount.ap_count " +
						           "WHEN geography.geographyValue = 'EU' " +
						           "THEN solutionCount.eu_count " +
						           "WHEN geography.geographyValue = 'GCG' " +
						           "THEN solutionCount.gcg_count " +
						           "WHEN geography.geographyValue = 'JP' " +
						           "THEN solutionCount.jp_count " +
						           "WHEN geography.geographyValue = 'MEA' " +
						           "THEN solutionCount.mea_count " +
						           "WHEN geography.geographyValue = 'NA' " +
						           "THEN solutionCount.na_count " +
						           "WHEN geography.geographyValue = 'LA' " +
						           "THEN solutionCount.la_count " +
						           "WHEN geography.geographyValue = 'WW' " +
						           "THEN solutionCount.ww_count " +
						           "ELSE 0 " +
						       "END " +
						     ") AS isnCount " +
						"FROM ( " +
						      
						       "SELECT 3              					AS idKey, " +
						           	 "'AP'           					AS geographyValue, " +
						           	 "'Asia/Pacific' 					AS geographyText " +
						       "UNION ALL " +
						       "SELECT 4        						AS idKey, " +
						           	 "'EU'     							AS geographyValue, " +
						           	 "'Europe' 							AS geographyText " +
						       "UNION ALL " +
						       "SELECT 5                     			AS idKey, " +
						           	 "'GCG'                 			AS geographyValue, " +
						           	 "'Greater China Group' 			AS geographyText " +
						       "UNION ALL " +
						       "SELECT 6       							AS idKey, " +
						           	 "'JP'    							AS geographyValue, " +
						           	 "'Japan' 							AS geographyText " +
						       "UNION ALL " +
						       "SELECT 7                        		AS idKey, " +
						           	 "'MEA'                    			AS geographyValue, " +
						           	 "'Middle East and Africa' 			AS geographyText " +
						       "UNION ALL " +
						       "SELECT 8               					AS idKey, " +
						           	 "'NA'            					AS geographyValue, " +
						           	 "'North America' 					AS geographyText " +
						       "UNION ALL  " +
						       "SELECT 9               					AS idKey, " +
						           	 "'LA'            					AS geographyValue, " +
						           	 "'South America' 					AS geographyText " +
						       "UNION ALL  " +
						       "SELECT 10            					AS idKey, " +
						           	 "'WW'         						AS geographyValue, " +
						           	 "'World Wide' 						AS geographyText " +
						    ") AS geography, " +
						   "( " +
						     "SELECT " +
						            "IFNULL(SUM(IFNULL(isn.p_AP, 0)), 0)  AS ap_count, " +
						            "IFNULL(SUM(IFNULL(isn.p_EU, 0)), 0)  AS eu_count, " +
						            "IFNULL(SUM(IFNULL(isn.p_GCG, 0)), 0) AS gcg_count, " +
						            "IFNULL(SUM(IFNULL(isn.p_JP, 0)), 0)  AS jp_count, " +
						            "IFNULL(SUM(IFNULL(isn.p_MEA, 0)), 0) AS mea_count, " +
						            "IFNULL(SUM(IFNULL(isn.p_NA, 0)), 0)  AS na_count, " +
						            "IFNULL(SUM(IFNULL(isn.p_LA, 0)), 0)  AS la_count, " +
						            "IFNULL(SUM(IF((IFNULL(isn.p_NA, 0)+IFNULL(isn.p_EU, 0)+IFNULL(isn.p_JP, 0)+IFNULL(isn.p_GCG, 0)+IFNULL(isn.p_LA, 0)+IFNULL(isn.p_AP, 0)+IFNULL(isn.p_MEA, 0)) >=3, 1, 0)), 0) AS ww_count " +
						       "FROM ( " +
						              "SELECT S.idKey AS ID, " +
						                     "S.p_NA, " +
						                     "S.p_EU, " +
						                     "S.p_JP, " +
						                     "S.p_GCG, " +
						                     "S.p_LA, " +
						                     "S.p_AP, " +
						                     "S.p_MEA " + getMainSql(false, idInd) +
						    		 ") isn " +
						   ") AS solutionCount " +
				  "ORDER BY geography.idKey   " ;
	  
		List<Map<String, Object>> result = jdbcTemplate.query(sql, sqlParamter, 
					new RowMapper<Map<String, Object>>() {
						public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("ID", rs.getString("geographyValue"));
							map.put("ITEM", rs.getString("geographyText") + " (" + rs.getString("isnCount") + ")");
							return map;
						}
					});
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ID", "All");
		map1.put("ITEM", "All Geographies");
		result.add(0, map1);
		return result;
	}
	
	// Andy 2016.11.17 16:08
	public List<Map<String, Object>> getStrategics(Object[] sqlParamter, List<Map<String, Object>> initiatives) {
		String idInd = (String) sqlParamter[7];
		
		StringBuffer growthPlayColumnSql = new StringBuffer();
		StringBuffer growthPlaySumSql = new StringBuffer();
		for (Map<String, Object> initiative : initiatives) {
			String initiativeNameAbbrev = initiative.get("nameAbbrev").toString();
			
			growthPlayColumnSql.append(", S.Initiative rlike '" + initiativeNameAbbrev + "' " + initiativeNameAbbrev);
			growthPlaySumSql.append(" SUM(" + initiativeNameAbbrev + ") " + initiativeNameAbbrev + "Count,");
			
		}
		
		String sql = "SELECT " + growthPlaySumSql.toString().substring(0, growthPlaySumSql.toString().length() - 1) +
					  " FROM ( " +
					   		  "SELECT S.idKey AS ID " + growthPlayColumnSql.toString() + getMainSql(false, idInd) +
					   		") isn " ;
		
		Map<String, Object> growthPlayCountResult = jdbcTemplate.queryForMap(sql, sqlParamter);
		
		for (Map<String, Object> initiative : initiatives) {
			String initiativeNameAbbrev = initiative.get("nameAbbrev").toString();
			initiative.put("ID", initiativeNameAbbrev);
			initiative.put("isnCount", growthPlayCountResult.get(initiativeNameAbbrev + "Count"));
			initiative.put("ITEM", initiative.get("name") + " (" + initiative.get("isnCount") + ")");
		}
		return initiatives;
	}
	
	public List<Map<String, Object>> getInitiatives() {
		String sql =  "SELECT idKey, Name_abbrev nameAbbrev, name, filterIcon, icon FROM " + DB_NAME_ACCELERATORS + ".initiatives ORDER BY idKey " ;
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[] {});
		return result;
	}
		
	// Andy 2016.2.24 11:03
	private String getMainSql(boolean isQueryBuyerFilter, String idInd) {
		String innerJoinBuyers = "";
		if(isQueryBuyerFilter) {
			innerJoinBuyers = "INNER JOIN " + DB_NAME_ACCELERATORS + ".buyers AS B " +
									  "ON B.idKey = BO.idBuyer " ;
		}
		
		String mainSql =	   
							 " FROM " + DB_NAME_ACCELERATORS + ".solution_blueprint S " +
					    "INNER JOIN " + DB_NAME_ACCELERATORS + ".industry I " +
					    		    "ON S.idInd = I.idKey " +
					    "INNER JOIN " + DB_NAME_ACCELERATORS + ".solutionareas_blueprint SA " +
					    		    "ON S.idISA = SA.idKey " +
					     "LEFT JOIN " + DB_NAME_ACCELERATORS + ".buyer2offering BO " +
					     		    "ON S.idKey = BO.idISN " + innerJoinBuyers +
					         "WHERE (   I.Name RLIKE ? " +
						            "OR SA.Imperative_Name RLIKE ? " +
						            "OR S.Name RLIKE ? " +
						            "OR S.Description RLIKE ? " +
						            "OR ? IS NULL " +
						            ") " +
						       "AND (S.Initiative RLIKE ? OR  ? = '0') " +
						       "AND (S.idInd in (" + idInd + ") OR  ? IS NULL) " +
						       "AND (S.idISA = ? OR  ? IS NULL)         " +
						       "AND (S.idKey = ? OR  ? IS NULL) " +
						       "AND (S.SG_Sol_OID = ? OR  ? IS NULL) " +
						       "AND (SA.SG_Imp_OID = ? OR  ? IS NULL) " +
						       "AND (BO.idBuyer = ? OR  ? IS NULL) " +
						       "AND (   (? = 0) " +
					         	    "OR (? = 'WW' AND (IFNULL(S.p_NA, 0)+ IFNULL(S.p_EU, 0)+ IFNULL(S.p_JP, 0)+ IFNULL(S.p_GCG, 0) + IFNULL(S.p_LA, 0)+ IFNULL(S.p_AP, 0)+ IFNULL(S.p_MEA, 0)) >=3) " +
					            	"OR (? = 'NA' AND S.p_NA=1) " +
					            	"OR (? = 'EU' AND S.p_EU=1) " +
						            "OR (? = 'JP' AND S.p_JP=1) " +
						            "OR (? = 'GCG' AND S.p_GCG=1) " +
						            "OR (? = 'LA' AND S.p_LA=1) " +
						            "OR (? = 'AP' AND S.p_AP=1) " +
						            "OR (? = 'MEA' AND S.p_MEA=1) " +
						            ") " +
						  "GROUP BY ID " ;
				
		return mainSql;
	}
	
	
	
	// to-delete Andy 2016.2.26 17:30
	public List<Map<String, String>> getOfferings(String keyWord, String camss, String idInd, String idIsa, String idIsn, String idIso, String idBuyer, String geo, String priority) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		String whereClause = messages.getMessage("SQL_WHERE_CLAUSE_1", null, null);
		String sql = messages.getMessage("SQL_GET_OFFERINGS", new String[] {DB_NAME_ACCELERATORS, whereClause}, null);
		result = jdbcTemplate.query(sql, new Object[] {keyWord, keyWord, keyWord, keyWord,keyWord, keyWord,camss, 
				camss, idInd, idInd, idIsa, idIsa, idIsn, idIsn,
				idIso, idIso, idBuyer, idBuyer, priority, geo, geo, geo, geo, geo, geo, geo, geo}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("ID", rs.getString("ID"));
						map.put("ITEM", rs.getString("ITEM"));
						return map;
					}
				});
		return result;
	}
}
