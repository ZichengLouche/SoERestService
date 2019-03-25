package com.ibm.soe.rest.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.ExpertGuidanceDao;
import com.ibm.soe.rest.dao.ExpertGuidanceExampleOppDao;

@Repository
@Transactional
public class ExpertGuidanceExampleOppDaoImpl implements ExpertGuidanceExampleOppDao {
	
	private static Logger logger = Logger.getLogger(ExpertGuidanceExampleOppDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messages;
	
	@Value("${DB_NAME_METHOD_GUIDANCE}")
	private String METHOD_GUIDANCE;
	
	public String checkIfHaveOwnOpp(String idUser) throws Exception {
		String sql = messages.getMessage("SQL_CHECK_EXAMPLEOPPS", new String[] {METHOD_GUIDANCE}, null);
		String result = jdbcTemplate.queryForObject(sql, new String[] {idUser, idUser}, java.lang.String.class);
		return result;
	}
	
	public String getArchivedOppCnt(String idUser) throws Exception {
		String sql = messages.getMessage("SQL_GET_ARCHIVE_OPPS_CNT", new String[] {METHOD_GUIDANCE}, null);
		String result = jdbcTemplate.queryForObject(sql, new String[] {idUser}, java.lang.String.class);
		return result;
	}
	
	public Map<String, String> viewExampleOpp(final String idOpp) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		try {
			String getClientOppSql = messages.getMessage("SQL_NEW_GET_EXAMPLECLIENTOPP", new String[] {METHOD_GUIDANCE}, null);
			result = jdbcTemplate.queryForObject(getClientOppSql, new String[] {String.valueOf(idOpp)}, 
					new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("CID", rs.getString("CID"));
						dataMap.put("OID", rs.getString("OID"));
						dataMap.put("CNM", rs.getString("CNM"));
						dataMap.put("CNN", rs.getString("CNN"));
						dataMap.put("OPPID", rs.getString("OPPID"));
						dataMap.put("OPPDESP", rs.getString("OPPDESP"));
						dataMap.put("OPPCMT", rs.getString("OPPCMT"));
						return dataMap;
					}
			});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return result;
	}
	
	public Map<String, String> getExampleClientOppDetails(final String idUser, final String idOpp) throws Exception {
		Map<String, String> clientOpportunity = new HashMap<String, String>();
		String fieds = messages.getMessage("SQL_GET_EXAMPLE_OPP_FIELDS", new String[] {METHOD_GUIDANCE}, null);
		String subSql = messages.getMessage("SQL_GET_EXAMPLE_OPP_STATES", new String[] {METHOD_GUIDANCE}, null);
		String sql = messages.getMessage("SQL_GET_EXAMPLE_OPP_ITEM", new String[] {fieds, subSql}, null);
		try {
			clientOpportunity = jdbcTemplate.queryForObject(sql, new Object[] {idOpp}, 
			new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("CID", rs.getString("CID"));
					map.put("OID", rs.getString("OID"));
					map.put("CNM", rs.getString("CNM"));
					map.put("ONM", rs.getString("ONM"));
					map.put("ODP", rs.getString("ODP"));
					map.put("LU", rs.getString("LU"));
					map.put("U", rs.getString("U"));
					map.put("E", rs.getString("E"));
					map.put("D", rs.getString("D"));
					map.put("I", rs.getString("I"));
					map.put("CV", rs.getString("CV"));
					map.put("UID", rs.getString("uid"));
					map.put("LU_SORT", rs.getString("LastUpdated"));
					return map;
				}
			});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return clientOpportunity;
	}
	
	public List<Map<String, Object>> getExampleOppActivityAndTaskDetails(String idUser, String idOpp) throws Exception {
		List<Map<String, Object>> activities = new ArrayList<Map<String, Object>>();
		try {
			List<Map<String, String>> tasks = new ArrayList<Map<String, String>>();
			
			String activitySQL = messages.getMessage("SQL_GET_EXAMPLE_ACTIVITIES", new String[] {METHOD_GUIDANCE}, null);
			String taskSQL = messages.getMessage("SQL_GET_EXAMPLE_TASKS", new String[] {METHOD_GUIDANCE}, null);
			
			activities = jdbcTemplate.query(activitySQL, new Object[] {idOpp}, 
					new RowMapper<Map<String, Object>>() {
						public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("AID", rs.getString("AID"));
							map.put("OID", rs.getString("OID"));
							map.put("ANM", rs.getString("ANM"));
							map.put("APP", rs.getString("APP"));
							map.put("AURL", rs.getString("AURL"));
							map.put("ADP", rs.getString("ADP"));
							map.put("ASD", rs.getString("ASD"));
							map.put("ALU", rs.getString("ALU"));
							map.put("SID", rs.getString("SID"));
							map.put("CHECKED", rs.getString("CHECKED"));
							return map;
						}
					});
			
			tasks = jdbcTemplate.query(taskSQL, new Object[] {idOpp}, 
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> map = new HashMap<String, String>();
							map.put("TID", rs.getString("TID"));
							map.put("OID", rs.getString("OID"));
							map.put("PID", rs.getString("PID"));
							map.put("TNM", rs.getString("TNM"));
							map.put("TURL", rs.getString("TURL"));
							map.put("TDP", rs.getString("TDP"));
							map.put("SID", rs.getString("SID"));
							map.put("CHECKED", rs.getString("CHECKED"));
							return map;
						}
					});
			
			List<Map<String, String>> sublist;
			Map<String, Object> activityMap;
			Map<String, String> taskMap;
			for (int i = 0; i < activities.size(); i ++) {
				sublist = new ArrayList<Map<String, String>>();
				activityMap = activities.get(i);
				String activityId = (String)activityMap.get("AID");
				for (int j = 0; j < tasks.size(); j ++) {
					taskMap = tasks.get(j);
					String parentId = taskMap.get("PID");
					if (activityId.equals(parentId)) {
						sublist.add(taskMap);
					}
				}
				activityMap.put("TASKS", sublist);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return activities;
	}
	
	public Map<String, String> getATNotesById(String idState) {
		if (idState == null || "".equals(idState.trim())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("SID", "");
			map.put("NOTES", "");
			return map;
		}
		
		String sql = messages.getMessage("SQL_GET_EXAMPLE_ATNOTES_BY_ID", new String[] {METHOD_GUIDANCE}, null);
		
		List<Map<String, String>> list = jdbcTemplate.query(sql, new String[] {idState}, 
				new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("SID", rs.getString("idKey"));
					dataMap.put("NOTES", rs.getString("notes"));
					return dataMap;
				}
		});
		
		return list == null || list.size() == 0 ? null : list.get(0);
	}
	
	public List<Map<String, String>> getGuidanceQuestions(String idTask, String idState) throws Exception {
		String sql = messages.getMessage("SQL_GET_EXAMPLE_GUIDANCE_QUESTIONS", new String[] {METHOD_GUIDANCE}, null);
		List<Map<String, String>> list = jdbcTemplate.query(sql, new String[] {idState, idTask}, 
				new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("idAnswer", rs.getString("idAnswer"));
					dataMap.put("answerChoice", rs.getString("answerChoice"));
					dataMap.put("answerNotes", rs.getString("answerNotes"));
					dataMap.put("idQuestion", rs.getString("idQuestion"));
					dataMap.put("sequenceNumber", rs.getString("sequenceNumber"));
					dataMap.put("questionType", rs.getString("questionType"));
					dataMap.put("question", rs.getString("question"));
					return dataMap;
				}
		});
		return list;
	}
}
