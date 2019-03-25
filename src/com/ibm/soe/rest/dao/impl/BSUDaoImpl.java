package com.ibm.soe.rest.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.dao.BSUDao;

@Repository
public class BSUDaoImpl implements BSUDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MessageSource messages;
	
	@Value("${DB_NAME_ACCELERATOR_WRITE}")
	private String DB_NAME_ACCELERATOR_WRITE;
	
	@Value("${DB_NAME_ACCELERATORS}")
	private String DB_NAME_ACCELERATORS;
	
	@Value("${TABLE_SOLUTION_PART}")
	private String TABLE_SOLUTION_PART;
	
	// Andy 2016.1.18 21:44
	public List<Map<String, Object>> queryBSUResults(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, long startIndex, int pageLimit, String idIsn) throws Exception {
		 String sql = "SELECT B.idBSU              						AS idBSU, " +
						     "I.idKey              						AS idInd, " +
						     "SA.idKey             						AS idIsa, " +
						     "S.idKey              						AS idIsn, " +                   
						     "S.idContentType      						AS idCnt, " +
						     "B.Name 									AS bsuName, " + 
						     "I.Name 									AS industryName, " +
						     "SA.Imperative_Name   						AS imperativeName, " +
						     "S.Name               						AS solutionName, " +
						     "S.description        						AS desp " + 
			     	  "FROM " + DB_NAME_ACCELERATORS + ".industry I " +
			 	"INNER JOIN " + DB_NAME_ACCELERATORS + ".solutionareas_bsu SA " +
	 					"ON I.idKey = SA.idInd " +
			 	"INNER JOIN " + DB_NAME_ACCELERATORS + ".solution_bsu S " +
	 					"ON SA.idKey = S.idISA " +
			 	"INNER JOIN " + DB_NAME_ACCELERATORS + ".bsu B " +
	 					"ON B.idBSU = S.idBSU " +
		 			 "WHERE (    	 I.Name RLIKE ? " +
	 					    	"OR  SA.Imperative_Name RLIKE ? " +
		 					    "OR  B.Name RLIKE ?	" +
		 					    "OR  S.Name RLIKE ?	" +
		 					    "OR  S.description RLIKE ?	" +
		 					    "OR  ? IS NULL " +
		 				     ") " +
		 				"AND (SA.idInd in (" + idInd + ") OR ? IS NULL) " +
		 				"AND (S.idISA = ? OR  ? IS NULL) " +
		 				"AND (S.idContentType = ? OR  ? IS NULL) " +
		 				"AND (EXISTS ( " +
	 							            "SELECT 1 " +
	 							              "FROM " + DB_NAME_ACCELERATORS + ".buyers AS buy " +
	 							        "INNER JOIN " + DB_NAME_ACCELERATORS + ".solution2buyer_bsu SB  " +
 							            		"ON (buy.idKey = SB.idBuyer) " +
	 							             "WHERE SB.idISN = S.idKey " +
	 							           	   "AND SB.idBuyer = ? " +
	 						         ") OR ? IS NULL " +
	 						 ") " +		
		 				"AND (B.idBSU = ? OR  ? IS NULL) " +
		 				"AND (S.idKey = ? OR  ? IS NULL) " +
			 	   "ORDER BY bsuName, industryName, imperativeName, solutionName LIMIT ?, ? " ;
			  
		Object[] param = new Object[]{keyword, keyword, keyword, keyword, keyword, keyword, idInd, idIsa, idIsa, idCnt, idCnt, idBuyer, idBuyer, idBSU, idBSU, idIsn, idIsn, startIndex, pageLimit};
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}

	public long queryFilterAllCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) {
		String sql ="SELECT COUNT(1)							    	AS total " +
					  "FROM ( " +
							    "SELECT S.idKey idISN " 						+ getMainSql(false, idInd) +
							  "GROUP BY S.idKey " +
						   ") N " ;
			  
		Object[] param = new String[]{keyword, keyword, keyword, keyword, keyword, keyword, idInd, idIsa, idIsa, idCnt, idCnt, idBuyer, idBuyer, idBSU, idBSU, idIsn, idIsn};
		long result = jdbcTemplate.queryForObject(sql, param, java.lang.Long.class);
		return result;
	}
	
	public List<Map<String,Object>> queryBSUFilterCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception {
		String sql ="SELECT B.idBSU                                     AS id, " +
						   "B.Name									    AS name, " +
						   "COUNT(DISTINCT S.idKey)						AS num " + getMainSql(false, idInd) +
				  "GROUP BY id " ;
					  
		Object[] param = new String[]{keyword, keyword, keyword, keyword, keyword, keyword, idInd, idIsa, idIsa, idCnt, idCnt, idBuyer, idBuyer, idBSU, idBSU, idIsn, idIsn};
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
	
	// Andy 2016.1.11 22:00
	public List<Map<String,Object>> queryIndustryFilterCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception {
		String sql ="SELECT I.idKey                                     AS id, " +
						   "I.Name									    AS name, " +
						   "COUNT(DISTINCT S.idKey)				        AS num " + getMainSql(false, idInd) +
				  "GROUP BY id " ;
		
		Object[] param = new String[]{keyword, keyword, keyword, keyword, keyword, keyword, idInd, idIsa, idIsa, idCnt, idCnt, idBuyer, idBuyer, idBSU, idBSU, idIsn, idIsn};
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
	
	public List<Map<String,Object>> queryImperativeFilterCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception {
		String sql ="SELECT SA.idKey                                     AS id, " +
						   "SA.Imperative_Name						     AS name, " +
						   "COUNT(DISTINCT S.idKey)					     AS num " + getMainSql(false, idInd) +
				  "GROUP BY id " ;
		Object[] param = new String[]{keyword, keyword, keyword, keyword, keyword, keyword, idInd, idIsa, idIsa, idCnt, idCnt, idBuyer, idBuyer, idBSU, idBSU, idIsn, idIsn};
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
	
	public List<Map<String,Object>> queryContentTypeFilterCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception {
		String sql ="SELECT S.idContentType                                     AS id, " +
						   "CASE " +
					            "WHEN S.idContentType = 1 THEN 'Use Cases' " +
					            "WHEN S.idContentType = 2 THEN 'Solutions' " +
					            "ELSE '' " +
				           "END 												AS name, " +
						   "COUNT(DISTINCT S.idKey)				        		AS num " + getMainSql(false, idInd) +
				  "GROUP BY id " ;
		Object[] param = new String[]{keyword, keyword, keyword, keyword, keyword, keyword, idInd, idIsa, idIsa, idCnt, idCnt, idBuyer, idBuyer, idBSU, idBSU, idIsn, idIsn};
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
	
	// Andy 2016.1.12 12:20
	public List<Map<String,Object>> queryBuyerFilterCount(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn) throws Exception {
		String sql ="SELECT SB.idBuyer                                     AS id, " +
						   "SB.buyerName						     	   AS name, " +
						   "COUNT(DISTINCT S.idKey)						   AS num " + getMainSql(true, idInd) +
				  "GROUP BY id " ;
		
		Object[] param = new String[]{keyword, keyword, keyword, keyword, keyword, keyword, idInd, idIsa, idIsa, idCnt, idCnt, idBuyer, idBuyer, idBSU, idBSU, idIsn, idIsn};
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
			
		
    // Andy 2016.1.12 12:20
	private String getMainSql(boolean isQueryBuyerFilter, String idInd) {
		String innerJoinBuyers = "";
		if(isQueryBuyerFilter) {
			innerJoinBuyers = "INNER JOIN " + DB_NAME_ACCELERATORS + ".buyers AS buy " +
									  "ON buy.idKey = SB.idBuyer " ;
		}
		
		String mainSql =      "FROM " + DB_NAME_ACCELERATORS + ".industry I " +
						"INNER JOIN " + DB_NAME_ACCELERATORS + ".solutionareas_bsu SA " +
								"ON I.idKey = SA.idInd " +
						"INNER JOIN " + DB_NAME_ACCELERATORS + ".solution_bsu S " +
								"ON SA.idKey = S.idISA " +
						"INNER JOIN " + DB_NAME_ACCELERATORS + ".bsu B " +
								"ON B.idBSU = S.idBSU " +
						 "LEFT JOIN " + DB_NAME_ACCELERATORS + ".solution2buyer_bsu SB " +
								"ON SB.idISN = S.idKey " + innerJoinBuyers +
							 "WHERE (    I.Name RLIKE ? " +
								    "OR  SA.Imperative_Name RLIKE ? " +
								    "OR  B.Name RLIKE ?	" +
								    "OR  S.Name RLIKE ?	" +
			 					    "OR  S.description RLIKE ?	" +
								    "OR  ? IS NULL " +
								    ") " +
								"AND (SA.idInd in (" + idInd + ") OR ? IS NULL) " +
								"AND (S.idISA = ? OR  ? IS NULL) " +
								"AND (S.idContentType = ? OR  ? IS NULL) " +
								"AND (SB.idBuyer = ? OR  ? IS NULL)		 " +
								"AND (B.idBSU = ? OR  ? IS NULL)	 " +
								"AND (S.idKey = ? OR  ? IS NULL) " 
				            ;
		return mainSql;
	}
	
	// Andy 2016.1.15 16:19
	@Override
	public List<Map<String, Object>> querySolutionPart(String idSolution, String detailsAbbrev, final String isLaptopEnv, final String reg) throws Exception {
		String sql =    "SELECT DISTINCT " +
							    "s.sg_sol_oid 						AS sgoId, " +
							    "sp.listId, " +
							    "sp.iRAM_key                       	AS assetId, " +
							    "sp.Name                           	AS title, " +
							    "IFNULL(sp.Name, sp.Description) 	AS linkText, " +
							    "sp.URL                            	AS linkURL " +
						   "FROM " + DB_NAME_ACCELERATORS + "." + TABLE_SOLUTION_PART + " AS sp " +
				     "INNER JOIN " + DB_NAME_ACCELERATORS + ".solution_bsu AS s " +
							 "ON sp.idISN = s.idKey " +
					 "INNER JOIN " + DB_NAME_ACCELERATOR_WRITE + ".listid_map_bsu AS lm " +
							 "ON sp.listId = lm.listId						  " +
						  "WHERE sp.idISN = ? " +
						    "AND lm.details_abbrev = ? " +
					   "ORDER BY sp.last_update DESC " ;
		  
		List<Map<String, Object>> result = jdbcTemplate.query(sql, new Object[] { idSolution,  detailsAbbrev },
				new RowMapper<Map<String, Object>>() {
					public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("sgoId", rs.getString("sgoId"));
						map.put("listId", rs.getString("listId"));
						map.put("assetId", rs.getString("assetId"));
						map.put("title", rs.getString("title"));
						String text = rs.getString("linkText");
						String url = rs.getString("linkURL").trim();
						if (!isLaptopEnv.equals("1")) {
							text = appendStrIfNeed(text, "(laptop only)", url, reg);
						}
						map.put("text", text);
						map.put("url", url);
						return map;
					}
				});
		return result;
	}
	
	// Andy 2016.1.14 22:25
	private String appendStrIfNeed(String source, String append, String testString, String regStr) {
		String result;
		Pattern pattern = Pattern.compile(regStr, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(testString);
		if (matcher.matches()) {
			result = source + append;
		} else {
			result = source;
		}
		return result;
	}
	
	// Andy 2016.1.15 12:12
	@Override
	public List<Map<String, Object>> queryContacts(String idSolution, String idIndustry, String idImperative) throws Exception {
		String sql = "SELECT 'Solution Contact'   								AS ownerTitle, " +
						     "isnOwner.Name        								AS ownerName, " +
						     "TRIM(isnOwner.eMail) 								AS ownerEmail, " +
						     "1                  								AS seq " +
						"FROM " + DB_NAME_ACCELERATORS + ".solution_bsu AS isn " +
			       "LEFT JOIN " + DB_NAME_ACCELERATORS + ".owner AS isnOwner " +
						  "ON (isnOwner.eMail = isn.idBusinessOwner) " +
					   "WHERE isn.idKey = ? " +
			"UNION ALL " +
				      "SELECT 'Technical Leader'   								AS ownerTitle, " +
						     "indOwner.Name        								AS ownerName, " +
						     "TRIM(indOwner.eMail) 								AS ownerEmail, " +
						     "2                  								AS seq " +
						"FROM " + DB_NAME_ACCELERATORS + ".industry AS ind " +
			       "LEFT JOIN " + DB_NAME_ACCELERATORS + ".owner AS indOwner " +
						  "ON (indOwner.idKey = ind.idCTO) " +
					   "WHERE ind.idKey = ? " +
			"UNION ALL  " +
					 "SELECT 'Imperative Owner'   								AS ownerTitle, " +
						     "isaOwner.Name        								AS ownerName, " +
						     "TRIM(isaOwner.eMail) 								AS ownerEmail, " +
						     "3                  								AS seq " +
						"FROM " + DB_NAME_ACCELERATORS + ".solutionareas_bsu AS isa " +
			      "INNER JOIN " + DB_NAME_ACCELERATORS + ".owner AS isaOwner " +
						  "ON (isaOwner.idKey = isa.idOwner) " +
					   "WHERE isa.idKey = ? " +
				    "ORDER BY seq " ;
	
		Object[] param = new Object[]{ idSolution, idIndustry, idImperative };
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
	
	// Andy 2016.1.19 18:16
	@Override
	public List<Map<String, Object>> querySolutionMapForUseCase(String idSolution) throws Exception {
		String sql = "SELECT s.idKey 							  				AS idIsn, " +
			 				"s.Name                           	  				AS name	" +
					   "FROM " + DB_NAME_ACCELERATORS + ".bsu_solution_map AS sm " +
				 "INNER JOIN " + DB_NAME_ACCELERATORS + ".solution_bsu AS s " +
						 "ON sm.mapTo = s.idKey	" +
					  "WHERE sm.mapFrom = ?				 " +
					    "AND sm.mapType = 1 " +
				   "ORDER BY name " ;
		
		Object[] param = new Object[]{ idSolution };
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
	
	@Override
	public List<Map<String, Object>> querySolutionMapForSolution(String idSolution) throws Exception {
		String sql = "SELECT s.idKey 							  				AS idIsn, " +
			 				"s.Name                           	  				AS name	" +
					   "FROM " + DB_NAME_ACCELERATORS + ".bsu_solution_map AS sm " +
				 "INNER JOIN " + DB_NAME_ACCELERATORS + ".solution_bsu AS s " +
						 "ON sm.mapFrom = s.idKey " +
					  "WHERE sm.mapTo = ?				 " +
					    "AND sm.mapType = 1 " +
				   "ORDER BY name " ;
		
		Object[] param = new Object[]{ idSolution };
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
	
	@Override
	public List<Map<String, Object>> queryIdentifiedBuyers(String idIsn) throws Exception {
		String sql ="SELECT distinct bo.idISN, bo.idBuyer, b.name " +
					  "FROM " + DB_NAME_ACCELERATORS + ".buyers b " +
				"INNER JOIN " + DB_NAME_ACCELERATORS + ".buyer2offering AS bo " +
						"ON b.idKey = bo.idBuyer " +
				 	 "WHERE bo.idISN = ? " +
				  "ORDER BY b.Last_Update DESC " ;
					
	    Object[] param = new Object[] { idIsn };
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
}
