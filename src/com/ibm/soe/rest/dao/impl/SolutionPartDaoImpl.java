package com.ibm.soe.rest.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.dao.SolutionPartDao;
@Repository
public class SolutionPartDaoImpl implements SolutionPartDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messages;

	@Value("${DB_NAME_SOLUTION_PART}")
	private String SOLUTION_PART;
	
	@Value("${TABLE_SOLUTION_BLUEPRINT}")
	private String TABLE_SOLUTION_BLUEPRINT;
	
	@Value("${TABLE_SOLUTION_BSU}")
	private String TABLE_SOLUTION_BSU;
	
	@Value("${TABLE_SOLUTION_PART}")
	private String TABLE_SOLUTION_PART;
	
	@Value("${TABLE_SOLUTION_PART_TEMP}")
	private String TABLE_SOLUTION_PART_TEMP;	
	
	@Value("${TABLE_LISTID_BLUEPRINT}")
	private String TABLE_LISTID_BLUEPRINT;
	
	@Value("${VIEW_OFFERING_WITH_INDUSTRY}")
	private String VIEW_OFFERING_WITH_INDUSTRY;

	@Value("${DB_NAME_ACCELERATOR_WRITE}")
	private String DB_NAME_ACCELERATOR_WRITE;
	
	@Value("${DB_NAME_ACCELERATORS}")
	private String DB_NAME_ACCELERATORS;
	
	@Override
	public boolean insertNewListToMapTable(){
		String sql = messages.getMessage("SQL_INSERT_LISTID_MAP", new String[] {SOLUTION_PART, TABLE_SOLUTION_PART, DB_NAME_ACCELERATOR_WRITE}, null);
		jdbcTemplate.execute(sql);
		return true;
	}
	
	@Override
	public boolean insertSolutionPartTemp(final List<Map<String, String>> param, final Integer type) {
		String sql = "INSERT INTO " + DB_NAME_ACCELERATORS + "." + TABLE_SOLUTION_PART_TEMP + " (idInd, idISA, idISN, idOffering, Name, Description, iRAM_key, URL, last_update, "
				+ "timestamp, isFromSG, listId) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?)" ;
		
		jdbcTemplate.batchUpdate(sql, 
				new BatchPreparedStatementSetter(){
					public void setValues(PreparedStatement ps,int i) throws SQLException {
			             String idInd = param.get(i).get("idInd");
			             String idISA = param.get(i).get("idISA");
			             String idISN = param.get(i).get("idISN");
			             String idOffering = param.get(i).get("idOffering");
			             String name = param.get(i).get("Name");
			             String description = param.get(i).get("Description");
			             String assetID = param.get(i).get("assetID");
			             String url = param.get(i).get("url");
			             String listid = param.get(i).get("listid");
			             String lastmodify = param.get(i).get("lastModified");
			             
			             ps.setString(1, idInd);
			             ps.setString(2, idISA);
			             ps.setString(3, idISN);
			             ps.setString(4, idOffering);
			             ps.setString(5, name);
			             ps.setString(6, description);
			             ps.setString(7, assetID);
			             ps.setString(8, url);
			             ps.setString(9, lastmodify);
			             ps.setInt(10, type);
			             ps.setString(11, listid);
		            }
					
		            public int getBatchSize() {
		            	return param.size();
		            }
		     	}
		);	
		return true;
	}

	// to-delete 2016.12.8 17:42
	@Override
	public boolean deleteSolutionPartTemp() {
		String sql = messages.getMessage("SQL_TRUNCATE_SOLUTIONPART_TEMP", new String[] {SOLUTION_PART,TABLE_SOLUTION_PART_TEMP}, null);
		jdbcTemplate.execute(sql);
		return true;
	}

	@Override
	public boolean clearSolutionPart(Integer type) {
		String sql = "DELETE FROM " + DB_NAME_ACCELERATORS + "." + TABLE_SOLUTION_PART + " WHERE isFromSG = ? " ;
//		jdbcTemplate.execute(sql);
		int result = jdbcTemplate.update(sql, new Object[]{type});
		return result > 1 ? true : false;
	}

	@Override
	public boolean copyTempToSolutionPart() {
		String sql = "INSERT INTO " + DB_NAME_ACCELERATORS + "." + TABLE_SOLUTION_PART + " SELECT * FROM " + DB_NAME_ACCELERATORS + "." + TABLE_SOLUTION_PART_TEMP ;
		jdbcTemplate.execute(sql);
		return true;
	}
	
	public List<String> getOfferingOID() {
		String sql = messages.getMessage("SQL_GET_OFFERING_KEY", new String[] {SOLUTION_PART, TABLE_SOLUTION_BLUEPRINT}, null);
		return jdbcTemplate.queryForList(sql, new String[] {}, String.class);
	}
	
	
	public String getSolutionpart() {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		result = jdbcTemplate.query("select name from accelerators.solutionpart_test t where t.idKey=8915  ", new Object[] {}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("name", rs.getString("name"));
						return dataMap;
					}
				});
			
			return result.get(0).get("name");
	}
	
	public List<Map<String, String>> queryListidMapBSU() {
		String sql = "SELECT distinct idBSU, listid FROM " + DB_NAME_ACCELERATOR_WRITE + ".listid_map_bsu" ;
		List<Map<String, String>> result = jdbcTemplate.query(sql, new Object[] {}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("listid", rs.getString("listid"));
						dataMap.put("IDBSU", rs.getString("idBSU"));
						return dataMap;
					}
				});
		return result;
	}
	
	public List<String> getBLUEPRINTListIds() {
		String sql = messages.getMessage("SQL_GET_SOLUTION_BLUEPRINT_LISTID", new String[] {DB_NAME_ACCELERATOR_WRITE, TABLE_LISTID_BLUEPRINT}, null);
		return jdbcTemplate.queryForList(sql, new String[] {}, String.class);
	}
	
	public List<String> getBLUEPRINTExceptionListIds() {
		String sql = messages.getMessage("SQL_GET_SOLUTION_BLUEPRINT_EXCEPTION_LISTID", new String[] {DB_NAME_ACCELERATOR_WRITE, TABLE_LISTID_BLUEPRINT}, null);
		return jdbcTemplate.queryForList(sql, new String[] {}, String.class);
	}
	
	public List<String> getBLUEPRINT0ListIds() {
		String sql = messages.getMessage("SQL_GET_SOLUTION_BLUEPRINT_0_LISTID", new String[] {DB_NAME_ACCELERATOR_WRITE, TABLE_LISTID_BLUEPRINT}, null);
		return jdbcTemplate.queryForList(sql, new String[] {}, String.class);
	}
	
	public List<Map<String, String>> getSolutionBsuOID() {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		String sql = "SELECT idKey, idBSU, SG_Sol_OID FROM " + DB_NAME_ACCELERATORS + ".solution_bsu";
		
		result = jdbcTemplate.query(sql, new Object[] {}, 
			new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("IDKEY", rs.getString("idKey"));
					dataMap.put("IDBSU", rs.getString("idBSU"));
					dataMap.put("Off_OID", rs.getString("SG_Sol_OID"));
					return dataMap;
				}
			});
		return result;
	}
	
	public List<Map<String, String>> getBSUToolID() {
		String sql = "SELECT distinct idBSU, ToolID FROM " + DB_NAME_ACCELERATORS + ".bsu" ;
		
		List<Map<String, String>> result = jdbcTemplate.query(sql, new Object[] {}, 
			new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("IDBSU", rs.getString("idBSU"));
					dataMap.put("ToolID", rs.getString("ToolID"));
					return dataMap;
				}
			});
		return result;
	}
	
	public List<Map<String, String>> getFourLevelCascadeIdsForBlueprint() {
		String sql = "SELECT idKey, idInd, idISA, SG_Sol_OID as idOffering FROM " + DB_NAME_ACCELERATORS + ".solution_blueprint" ;
		List<Map<String, String>> result = jdbcTemplate.query(sql, new Object[] {}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("idInd", rs.getString("idInd"));
						dataMap.put("idISA", rs.getString("idISA"));
						dataMap.put("idISN", rs.getString("idKey"));
						dataMap.put("idOffering", rs.getString("idOffering"));
						return dataMap;
					}
				}
		);
		return result ;
	}
	
	public List<Map<String, String>> getFourLevelCascadeIdsForBSU() {
		String sql = "SELECT idInd, idISA, idKey as idISN, idKey as idOffering FROM " + DB_NAME_ACCELERATORS + ".solution_bsu" + " order by idInd, idISA, idKey" ;
		List<Map<String, String>> result = jdbcTemplate.query(sql, new Object[] {}, 
				new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("idInd", rs.getString("idInd"));
					dataMap.put("idISA", rs.getString("idISA"));
					dataMap.put("idISN", rs.getString("idISN"));
					dataMap.put("idOffering", rs.getString("idOffering"));
					return dataMap;
				}
		});
		return result ;
	}
}
