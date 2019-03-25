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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.dao.BluePrintResultDao;

@Repository
public class BluePrintResultDaoImpl implements BluePrintResultDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${DB_NAME_ACCELERATORS}")
	private String DB_NAME_ACCELERATORS;
	
	@Value("${DB_NAME_ACCELERATOR_WRITE}")
	private String DB_NAME_ACCELERATOR_WRITE;
	
	@Value("${TABLE_SOLUTION_PART}")
	private String TABLE_SOLUTION_PART;

	// Andy 2016.2.25 10:33
	public List<Map<String, Object>> queryBluePrintResults(String keyWord, String camss, String idInd, String idIsa, String idIsn, String idBuyer, String geo, String priority,
																									   String solOID, String impOID, long startIndex, int pageLimit) throws Exception {
		String sql = "SELECT S.idKey             	AS idIsn, " +
						    "S.Name              	AS solution_Name, " +
						    "isnOwner.Name         	AS solution_Owner, " +
						    "TRIM(isnOwner.eMail)  	AS solution_OwnerEmail, " +
						    "SA.idKey             	AS idIsa, " +
						    "SA.Imperative_Name		AS imperative_Name, " +
						    "isaOwner.Name         	AS imperative_Owner, " +
						    "TRIM(isaOwner.eMail)  	AS imperative_OwnerEmail, " +
						    "I.idKey             	AS idInd, " +
						    "I.Name              	AS industry_Name, " +
						    "indOwner.Name         	AS industry_Owner, " +
						    "TRIM(indOwner.eMail) 	AS industry_OwnerEmail,						     " +
						    "S.Description 		  	AS desp, " +						   
						    "S.Initiative 			AS initiative, " +						   
						    "IF(  " +
						       "(  " +
 								" (? = 'ALL' AND (IFNULL(S.p_NA, 0)+IFNULL(S.p_EU, 0)+IFNULL(S.p_JP, 0)+IFNULL(S.p_GCG, 0)+IFNULL(S.p_LA, 0)+IFNULL(S.p_AP, 0)+IFNULL(S.p_MEA, 0)) >=1) " +
 								"OR (? = 'WW' AND (IFNULL(S.p_NA, 0)+IFNULL(S.p_EU, 0)+IFNULL(S.p_JP, 0)+IFNULL(S.p_GCG, 0)+IFNULL(S.p_LA, 0)+IFNULL(S.p_AP, 0)+IFNULL(S.p_MEA, 0)) >=3) " +
								"OR (? = 'NA' AND p_NA=1) " +
								"OR (? = 'EU' AND p_EU=1) " +
								"OR (? = 'JP' AND p_JP=1) " +
								"OR (? = 'GCG' AND p_GCG=1) " +
								"OR (? = 'LA' AND p_LA=1) " +
								"OR (? = 'AP' AND p_AP=1) " +
								"OR (? = 'MEA' AND p_MEA=1) " +
							   "), 1, 0 " +
							") AS prioritySolution " + getMainSql(idInd) +
				   "ORDER BY S.Name, I.Name, SA.Imperative_Name LIMIT ?, ? " ;
	    
		Object[] param = new Object[] { geo, geo, geo, geo, geo, geo, geo, geo, geo, 
										keyWord, keyWord, keyWord, keyWord,keyWord, camss, camss, idInd, idIsa, idIsa, idIsn, idIsn, solOID, solOID, impOID, impOID, idBuyer, idBuyer, 
										priority, geo, geo, geo, geo, geo, geo, geo, geo, geo, startIndex, pageLimit };
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}

	public long queryBluePrintResultTotal(String keyWord, String camss, String idInd, String idIsa, String idIsn, String idBuyer, String geo, String priority, String solOID, 
																																		      String impOID) throws Exception {
		String sql ="SELECT COUNT(1) " + getMainSql(idInd) ;
		
		Object[] param = new Object[] { keyWord, keyWord, keyWord, keyWord,keyWord, camss, camss, idInd, idIsa, idIsa, idIsn, idIsn, solOID, solOID, impOID, impOID, idBuyer, idBuyer, 
										priority, geo, geo, geo, geo, geo, geo, geo, geo, geo };

		Long result = jdbcTemplate.queryForObject(sql, param, java.lang.Long.class);
		return result;
	}

	// Andy 2016.2.24 17:47
	private String getMainSql(String idInd) {
		String mainSql =   "FROM " + DB_NAME_ACCELERATORS + ".solution_blueprint AS S " +
				     "INNER JOIN " + DB_NAME_ACCELERATORS + ".solutionareas_blueprint AS SA " +
							 "ON S.idISA = SA.idKey " +
				     "INNER JOIN " + DB_NAME_ACCELERATORS + ".industry AS I " +
						   	 "ON S.idInd = I.idKey	  " +
				      "LEFT JOIN " + DB_NAME_ACCELERATORS + ".owner AS isnOwner " +
							 "ON isnOwner.idKey = S.idOwner " +
					  "LEFT JOIN " + DB_NAME_ACCELERATORS + ".owner AS isaOwner " +
							 "ON isaOwner.idKey = SA.idOwner " +
					  "LEFT JOIN " + DB_NAME_ACCELERATORS + ".owner AS indOwner " +
							 "ON indOwner.idKey = I.idCTO " +
						  "WHERE (   I.Name RLIKE ? " +
						 		 "OR SA.Imperative_Name RLIKE ?  " +
						 		 "OR S.Name RLIKE ?  " +
						 		 "OR S.Description RLIKE ? " +
						 		 "OR ? IS NULL " +
						 		 ") " + 
						 	"AND (S.Initiative RLIKE ? OR ? = '0') " +
						 	"AND (? IS NULL OR  I.idKey in (" + idInd + ")) " +
							"AND (? IS NULL OR  SA.idKey = ?) " +
							"AND (? IS NULL OR  S.idKey = ?) " +
							"AND (? IS NULL OR  S.SG_Sol_OID = ?) " +
							"AND (? IS NULL OR  SA.SG_Imp_OID = ?) " +
							"AND (? IS NULL OR  EXISTS ( " +
											            "SELECT 1 " +
											              "FROM " + DB_NAME_ACCELERATORS + ".buyers AS buy " +
											        "INNER JOIN " + DB_NAME_ACCELERATORS + ".buyer2offering AS b2o " +
											            	"ON buy.idKey = b2o.idBuyer " +
											             "WHERE b2o.idISN = S.idKey " +
											           	   "AND buy.idKey = ? " +
						            				") " +
							     ") " +														    
							"AND (   (? = 0) " +
								 "OR (? = 'ALL' AND (IFNULL(S.p_NA, 0)+IFNULL(S.p_EU, 0)+IFNULL(S.p_JP, 0)+IFNULL(S.p_GCG, 0)+IFNULL(S.p_LA, 0)+IFNULL(S.p_AP, 0)+IFNULL(S.p_MEA, 0)) >=1) " +
							     "OR (? = 'WW' AND (IFNULL(S.p_NA, 0)+IFNULL(S.p_EU, 0)+IFNULL(S.p_JP, 0)+IFNULL(S.p_GCG, 0)+IFNULL(S.p_LA, 0)+IFNULL(S.p_AP, 0)+IFNULL(S.p_MEA, 0)) >=3) " +
							     "OR (? = 'NA' AND p_NA=1) " +
							     "OR (? = 'EU' AND p_EU=1) " +
							     "OR (? = 'JP' AND p_JP=1) " +
							     "OR (? = 'GCG' AND p_GCG=1) " +
							     "OR (? = 'LA' AND p_LA=1) " +
							     "OR (? = 'AP' AND p_AP=1) " +
							     "OR (? = 'MEA' AND p_MEA=1) " +
							    ") " ;
				
		return mainSql;
	}
	
	@Override
	public Map<String, Object> queryKeyInformation(String idIsn) throws Exception {
		String sql ="SELECT I.thinkIndustry, " +
				  	   	   "I.CollaborationHub 			as industryCollaborationHub, " +
					       "SA.Master_Sales_Kit 		as imperativeSalesKit, " +
					       "S.QRG_URL 					as solutionQuickReferenceGuide " +
					  "FROM " + DB_NAME_ACCELERATORS + ".solution_blueprint AS S " +
				"INNER JOIN " + DB_NAME_ACCELERATORS + ".solutionareas_blueprint AS SA " +
					    "ON SA.idKey = S.idISA " +
				"INNER JOIN " + DB_NAME_ACCELERATORS + ".industry AS I " +
					    "ON I.idKey = S.idInd " +
				     "WHERE S.idKey = ? " ;
				
		Object[] param = new Object[] { idIsn };
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
	
		Map<String, Object> keyInformation = new HashMap<String, Object>();
		
		if (!result.isEmpty()) {
			keyInformation = result.get(0);
		}
		return keyInformation;
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
	
	// Andy 2016.1.15 16:19
	@Override
	public List<Map<String, Object>> querySolutionPart(final String isLaptopEnv, final String reg, boolean isUnion, String whereSqlForUnion, Object[] args) throws Exception {
		String union = "";
		if(isUnion && whereSqlForUnion != null && !"".equals(whereSqlForUnion)) {
			union =  	   "UNION " +
						  "SELECT acc.idAccelerators               				AS sgoId, " +
							     "NULL                 							AS listId, " +
							     "NULL                 							AS assetID, " +
							     "acc.Accelerator_Name 							AS title, " +
							     "acc.Accelerator_Name 							AS linkText, " +
							     "acc.Download_URL     							AS linkURL " +
							"FROM " + DB_NAME_ACCELERATORS + ".accelerators_w AS acc " + whereSqlForUnion ;
		}
		
		String sql =    "SELECT s.sg_sol_oid 						AS sgoId, " +
							    "sp.listId, " +
							    "sp.iRAM_key                       	AS assetId, " +
							    "sp.Name                           	AS title, " +
							    "IFNULL(sp.Name, sp.Description) 	AS linkText, " +
							    "sp.URL                            	AS linkURL " +
						   "FROM " + DB_NAME_ACCELERATORS + "." + TABLE_SOLUTION_PART + " AS sp " +
				     "INNER JOIN " + DB_NAME_ACCELERATORS + ".solution_blueprint AS s " +
							 "ON sp.idISN = s.idKey " +
					 "INNER JOIN " + DB_NAME_ACCELERATOR_WRITE + ".listid_map_blueprint AS lm " +
							 "ON sp.listId = lm.listId " +
						  "WHERE sp.idISN = ? " +
						    "AND lm.details_abbrev = ? " + union;
		
		List<Map<String, Object>> result = jdbcTemplate.query(sql, args,
				new RowMapper<Map<String, Object>>() {
					public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("sgoId", rs.getString("sgoId"));
						map.put("listId", rs.getString("listId"));
						map.put("assetId", rs.getString("assetId"));
						map.put("title", rs.getString("title"));
						String linkText = rs.getString("linkText");
						String linkURL = rs.getString("linkURL").trim();
						if (!isLaptopEnv.equals("1")) {
							linkText = appendStrIfNeed(linkText, "(laptop only)", linkURL, reg);
						}
						map.put("text", linkText);
						map.put("linkURL", linkURL);
						return map;
					}
				});
		
		return result;	
	}
	
	@Override
	public List<Map<String, Object>> querySolutionPartWithName(Object[] args) throws Exception {
		String sql =    "SELECT s.sg_sol_oid 						AS sgoId, " +
								"sp.listId, " +
							    "sp.iRAM_key                       	AS assetId, " +
							    "sp.Name                           	AS title, " +
							    "sp.Description 					AS description, " +
							    "sp.URL                            	AS linkURL " +
				    	  "FROM " + DB_NAME_ACCELERATORS + "." + TABLE_SOLUTION_PART + " AS sp " +
			    	"INNER JOIN " + DB_NAME_ACCELERATORS + ".solution_blueprint AS s " +
		    				"ON sp.idISN = s.idKey " +
	    			"INNER JOIN " + DB_NAME_ACCELERATOR_WRITE + ".listid_map_blueprint AS lm " +
	    					"ON sp.listId = lm.listId and lm.SG_description=sp.name " +
	    				 "WHERE sp.idISN = ? " +
	    				   "AND lm.SG_description = ? ";
		
		List<Map<String, Object>> result = jdbcTemplate.query(sql, args,
				new RowMapper<Map<String, Object>>() {
					public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("sgoId", rs.getString("sgoId"));
						map.put("listId", rs.getString("listId"));
						map.put("assetId", rs.getString("assetId"));
						map.put("title", rs.getString("title"));
						map.put("text", rs.getString("description"));
						map.put("linkURL", rs.getString("linkURL"));
						return map;
					}
				});
		
		return result;
	}
		
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
	
	@Override
	public List<Map<String, Object>> queryEducationMaterial(String idInd, String idIsa, String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception {
		String whereSqlForUnion = "WHERE acc.idInd = ? " +
								    "AND (acc.idisa = 0 OR  acc.idisa = ?) " +
								    "AND (acc.idISN = 0 OR  acc.idiSN = ?) " +
								    "AND acc.Type = 'Education' " ;
		
	    Object[] param = new Object[] { idIsn, detailsAbbrev, idInd, idIsa, idIsn };
	    return querySolutionPart(isLaptopEnv, reg, true, whereSqlForUnion, param); 
	}

	@Override
	public List<Map<String, Object>> queryDemonstrations(String idInd, String idIsa, String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception {
		String whereSqlForUnion = "WHERE acc.idInd = ? " +
									"AND (acc.idisa = 0 OR  acc.idisa = ?) " +
									"AND (acc.idISN = 0 OR  acc.idiSN = ?) " +
									"AND acc.Type = 'Demonstration' " ;
		
		Object[] param = new Object[] { idIsn, detailsAbbrev, idInd, idIsa, idIsn };
		return querySolutionPart(isLaptopEnv, reg, true, whereSqlForUnion, param); 
	}
	
	// Andy 2016.5.12 12:42
	@Override
	public List<Map<String, Object>> queryGISCAsset(String idIsn) throws Exception {
		String sql ="SELECT idIsn, id_asset assetId, concat('GISC ', asset_name) text, 1 isGISC " +
					  "FROM " + DB_NAME_ACCELERATORS + ".giscasset " +
				 	 "WHERE idISN = ? " ;
					
	    Object[] param = new Object[] { idIsn };
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
	
	@Override
	public List<Map<String, Object>> querySolutionDesignAccelerators(String idIsn, String detailsAbbrev, String isLaptopEnv, String reg) throws Exception {
		String whereSqlForUnion = "WHERE acc.idISN = ? " ;
		
		Object[] param = new Object[] { idIsn, detailsAbbrev, idIsn };
		return querySolutionPart(isLaptopEnv, reg, true, whereSqlForUnion, param); 
	}

	
	
	public List<Map<String, Object>> queryContacts(String idIsn, String idInd, String idIsa) throws Exception {
		
		//John Zhu for story 1360051, changes to four contacts from previous three contacts
		//Use Case Expert = solution.idBusinessOwner (tablename.columname)
		//Software Expert = solution.idSoftwareOwner (tablename.columname)
		//Service Expert = solution.idServiceOwner (tablename.columname)
		//Solution Owner = solutionareas.idOwner (tablename.columname)
		//John Zhu 2016-12-02 story 1360051, change the label "Solution Area Owner" to "Solution Owner".
		String sql = "SELECT 'Use Case Expert' AS OwnerTitle, " +
				"isuOwner.Name AS OwnerName, " +
				"TRIM(isuOwner.eMail) AS OwnerEmail, " +
				"'1' AS seq " +
				"FROM " + DB_NAME_ACCELERATORS + ".solution_blueprint AS isu " +
				"LEFT JOIN " + DB_NAME_ACCELERATORS + ".owner AS isuOwner " +
				"ON isuOwner.eMail = isu.idBusinessOwner " +
				"WHERE isu.idKey = ? " +
				"UNION ALL " +
				"SELECT 'Software Expert' AS OwnerTitle, " +
				"iswOwner.Name AS OwnerName, " +
				"TRIM(iswOwner.eMail) AS OwnerEmail, " +
				"'2' AS seq " +
				"FROM " + DB_NAME_ACCELERATORS + ".solution_blueprint AS isw " +
				"LEFT JOIN " + DB_NAME_ACCELERATORS + ".owner AS iswOwner " +
				"ON iswOwner.eMail = isw.idSoftwareOwner " +
				"WHERE isw.idKey = ? " +
				"UNION ALL " +
				"SELECT 'Services Expert' AS OwnerTitle, " +
				"isvOwner.Name AS OwnerName, " +
				"TRIM(isvOwner.eMail) AS OwnerEmail, " +
				"'3' AS seq " +
				"FROM " + DB_NAME_ACCELERATORS + ".solution_blueprint AS isv " +
				"LEFT JOIN " + DB_NAME_ACCELERATORS + ".owner AS isvOwner " +
				"ON isvOwner.eMail = isv.idServiceOwner " +
				"WHERE isv.idKey = ? " +
				"UNION ALL " +
				"SELECT 'Solution Owner' AS OwnerTitle, " +
				"isaOwner.Name AS OwnerName, " +
				"TRIM(isaOwner.eMail) AS OwnerEmail, " +
				"'4' AS seq " +
				"FROM " + DB_NAME_ACCELERATORS + ".solutionareas_blueprint AS isa " +
				"INNER JOIN " + DB_NAME_ACCELERATORS + ".owner AS isaOwner " +
				"ON isaOwner.idKey = isa.idOwner " +
				"WHERE isa.idKey = ? " +
				"ORDER BY seq ";

		Object[] param = new Object[] {idIsn, idIsn, idIsn, idIsa};
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
		return result;
	}
	
	public List<Map<String, String>> queryLaptopOnlys()	throws Exception {
		String sql = "SELECT docType, mimeType, location from " + DB_NAME_ACCELERATOR_WRITE + ".laptop_only" ;

		List<Map<String, String>> result = jdbcTemplate.query(sql, new Object[] {}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("docType", rs.getString("docType"));
						map.put("mimeType", rs.getString("mimeType"));
						map.put("location", rs.getString("location"));
						return map;
					}
				});
		return result;
	}
	
	// Andy 2016.5.27 16:49
	@Override
	public List<Map<String, Object>> queryMappingRelationshipForSolutionImperativeIndustry()  {
		String sql = "SELECT S.idKey            	AS idISN, " +
				    		"S.Name             	AS solutionName, " +
						    "SA.idKey           	AS idISA, " +
						    "SA.imperative_name 	AS imperativeName, " +
						    "I.idKey            	AS idIND, " +
						    "I.Name             	AS industryName " +
				       "FROM " + DB_NAME_ACCELERATORS + ".solution_blueprint AS S " +
				 "INNER JOIN " + DB_NAME_ACCELERATORS + ".solutionareas_blueprint AS SA " +
				 		 "ON S.idISA = SA.idKey " +
				 "INNER JOIN " + DB_NAME_ACCELERATORS + ".industry AS I " +
				         "ON S.idInd = I.idKey " +
				   "ORDER BY solutionName, imperativeName, industryName " ;
					
	    Object[] param = new Object[] { };
		return jdbcTemplate.queryForList(sql, param);
	}
}
