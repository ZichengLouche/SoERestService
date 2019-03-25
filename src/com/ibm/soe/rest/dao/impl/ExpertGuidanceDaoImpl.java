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

@Repository
@Transactional
public class ExpertGuidanceDaoImpl implements ExpertGuidanceDao {
	
	private static Logger logger = Logger.getLogger(ExpertGuidanceDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplateCISF;
	
	@Autowired
	private MessageSource messages;
	
	@Value("${DB_NAME_METHOD_GUIDANCE}")
	private String DB_NAME_METHOD_GUIDANCE;
	
	@Value("${DB_NAME_ACCELERATOR_WRITE}")
	private String DB_NAME_ACCELERATOR_WRITE;
	
	public List<Map<String, String>> getClientOpportunities(String uid) throws Exception {
		List<Map<String, String>> clientOpportunities = new ArrayList<Map<String, String>>();
//		String sql = messages.getMessage("SQL_GETCLIENTOPPORTUNITIES", new String[] {METHOD_GUIDANCE}, null);
		String fieds = messages.getMessage("SQL_GET_OPP_FIELDS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		String subSql = messages.getMessage("SQL_GET_OPP_STATES", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		String sql = messages.getMessage("SQL_GET_OPP_LIST", new String[] {fieds, subSql, DB_NAME_METHOD_GUIDANCE}, null);
		try {
			clientOpportunities = jdbcTemplate.query(sql, new Object[] {uid, uid, uid}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("CID", rs.getString("CID"));
						map.put("OID", rs.getString("OID"));
						map.put("CNM", rs.getString("CNM"));
						map.put("CIDENTIFIER", rs.getString("CIDENTIFIER"));
						map.put("LOCATION", rs.getString("LOCATION"));
						map.put("ONM", rs.getString("ONM"));
						map.put("ODP", rs.getString("ODP"));
						map.put("LU", rs.getString("LU"));
						map.put("U", rs.getString("U"));
						map.put("E", rs.getString("E"));
						map.put("D", rs.getString("D"));
						map.put("I", rs.getString("I"));
						map.put("CV", rs.getString("CV"));
						map.put("SHR", rs.getString("SHR"));
						map.put("UID", rs.getString("uid"));
						map.put("ACTIVE", rs.getString("active"));
						map.put("SALESID", rs.getString("salesID"));
						map.put("LU_SORT", rs.getString("LastUpdated"));
						return map;
					}
				});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return clientOpportunities;
	}
	
	public Map<String, String> getClientOpportunity(String uid, String oid) throws Exception {
		Map<String, String> clientOpportunity = new HashMap<String, String>();
//		String sql = messages.getMessage("SQL_GET_CLIENT_OPP", new String[] {METHOD_GUIDANCE}, null);
		String fieds = messages.getMessage("SQL_GET_OPP_FIELDS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		String subSql = messages.getMessage("SQL_GET_OPP_STATES", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		String sql = messages.getMessage("SQL_GET_OPP_ITEM", new String[] {fieds, subSql}, null);
		try {
			clientOpportunity = jdbcTemplate.queryForObject(sql, new Object[] {uid, oid}, 
					new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("CID", rs.getString("CID"));
					map.put("OID", rs.getString("OID"));
					map.put("CNM", rs.getString("CNM"));
					map.put("CIDENTIFIER", rs.getString("CIDENTIFIER"));
					map.put("LOCATION", rs.getString("LOCATION"));
					map.put("ONM", rs.getString("ONM"));
					map.put("ODP", rs.getString("ODP"));
					map.put("LU", rs.getString("LU"));
					map.put("U", rs.getString("U"));
					map.put("E", rs.getString("E"));
					map.put("D", rs.getString("D"));
					map.put("I", rs.getString("I"));
					map.put("CV", rs.getString("CV"));
					map.put("SHR", rs.getString("SHR"));
					map.put("UID", rs.getString("uid"));
					map.put("ACTIVE", rs.getString("active"));
					map.put("SALESID", rs.getString("salesID"));
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
	
	public Map<String, String> insertClientOpportunities(final Map<String, String> map) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		try {
			KeyHolder clientKeyHolder = new GeneratedKeyHolder();
			int cid = 0;
			// If client Id already exists, no need to insert data into client table
			if (map.get("cid") == null || "".equals((String)map.get("cid"))) {
				//Insert data into client table
				jdbcTemplate.update(new PreparedStatementCreator(){
					String newClientSql = messages.getMessage("SQL_NEW_CLIENT", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
					String uid = map.get("uid");
					String clientName = map.get("cnm");
					String clientDescription = map.get("cnn");
					String clientIdentifier = map.get("clientIdentifier");
					String location = map.get("location");
					
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(newClientSql, Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, uid);
						ps.setString(2, clientName);
	                    ps.setString(3, clientDescription);
	                    ps.setString(4, uid);
	                    ps.setString(5, clientIdentifier);
	                    ps.setString(6, location);
	                    return ps;
					}
				}, clientKeyHolder);
				cid = clientKeyHolder.getKey().intValue();
			} else {
				jdbcTemplate.update(new PreparedStatementCreator(){
					String editClientSql = messages.getMessage("SQL_EDIT_CLIENT", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
					String clientName = map.get("cnm");
					String clientDescription = map.get("cnn");
					String cid = map.get("cid");
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(editClientSql, Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, clientName);
						ps.setString(2, clientDescription);
	                    ps.setInt(3, Integer.parseInt(cid));
	                    return ps;
					}
				}, clientKeyHolder);
				
				cid = Integer.valueOf((String)map.get("cid")).intValue();
			}
			
			//Insert data into opportunities table
			final int clientId = cid;
			KeyHolder oppKeyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator(){
				String newClientOppSql = messages.getMessage("SQL_NEW_CLIENT_OPP", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
				String uid = map.get("uid");
				String oppIdSalesConn = map.get("oppID");
				String oppName = map.get("oppDesp");
				String oppDescription = map.get("oppComment");
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(newClientOppSql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, uid);
					ps.setInt(2, clientId);
                    ps.setString(3, oppIdSalesConn);
                    ps.setString(4, oppName);
                    ps.setString(5, oppDescription);
                    ps.setString(6, uid);
                    ps.setString(7, new java.sql.Date(new java.util.Date().getTime()).toString());
                    return ps;
				}
			}, oppKeyHolder);
			
			//Get the opportunity object that just inserted
			final int oppId = oppKeyHolder.getKey().intValue();
			String getClientOppSql = messages.getMessage("SQL_NEW_GET_CLIENTOPP", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			result = jdbcTemplate.queryForObject(getClientOppSql, new String[] {String.valueOf(oppId)}, 
					new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("CID", rs.getString("CID"));
						dataMap.put("OID", rs.getString("OID"));
						dataMap.put("CNM", rs.getString("CNM"));
						dataMap.put("CIDENTIFIER", rs.getString("CIDENTIFIER"));
						dataMap.put("LOCATION", rs.getString("LOCATION"));
						dataMap.put("ODP", rs.getString("ODP"));
						dataMap.put("ONM", rs.getString("ONM"));
						dataMap.put("LU", rs.getString("LU"));
						dataMap.put("U", rs.getString("U"));
						dataMap.put("E", rs.getString("E"));
						dataMap.put("D", rs.getString("D"));
						dataMap.put("I", rs.getString("I"));
						dataMap.put("CV", rs.getString("CV"));
						return dataMap;
					}
			});
			
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return result;
	}
	
	@Transactional
	public void updateClientOpportunities(final Map<String, String> map) {
			KeyHolder clientKeyHolder = new GeneratedKeyHolder();
			// If client Id already exists, no need to insert data into client table
			jdbcTemplate.update(new PreparedStatementCreator(){
				String editClientSql = messages.getMessage("SQL_EDIT_CLIENT", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
				String clientName = map.get("cnm");
				String clientDescription = map.get("cnn");
				String cid = map.get("cid");
				String clientIdentifier = map.get("clientIdentifier");
				String location = map.get("location");
				
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(editClientSql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, clientName);
					ps.setString(2, clientDescription);
					ps.setString(3, clientIdentifier);
					ps.setString(4, location);
                    ps.setInt(5, Integer.parseInt(cid));
                    return ps;
				}
			}, clientKeyHolder);
			
			//update data in opportunities table
			KeyHolder oppKeyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator(){
				String editClientOppSql = messages.getMessage("SQL_EDIT_CLIENT_OPP", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
				String oppIdSalesConn = map.get("oppID");
				String oppName = map.get("oppDesp");
				String oppDescription = map.get("oppComment");
				String oid = map.get("oid");
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(editClientOppSql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, oppIdSalesConn);
					ps.setString(2, oppName);
                    ps.setString(3, oppDescription);
                    ps.setInt(4, Integer.parseInt(oid));
                    return ps;
				}
			}, oppKeyHolder);
			
	}
	
	public Map<String, String> getClientOpportunityByOppId(String oid) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		try {
			String getEditClientOppSql = messages.getMessage("SQL_EDIT_GET_CLIENTOPP", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			result = jdbcTemplate.queryForObject(getEditClientOppSql, new String[] {oid}, 
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
						dataMap.put("clientIdentifier", rs.getString("clientIdentifier") == null ? "" : rs.getString("clientIdentifier"));
						dataMap.put("location", rs.getString("location") == null ? "" : rs.getString("location"));
						return dataMap;
					}
			});
			
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return result;
	}
	
	public List<Map<String, String>> getClientNames(String uid) throws Exception {
		List<Map<String, String>> clientNames = new ArrayList<Map<String, String>>();
		
		try {
			String sql = messages.getMessage("SQL_GET_CLIENTNAMES", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			clientNames = jdbcTemplate.query(sql, new Object[] {uid, uid}, 
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> map = new HashMap<String, String>();
							map.put("CID", rs.getString("CID"));
							map.put("CNM", rs.getString("CNM"));
							map.put("SHR", rs.getString("SHR"));
							return map;
						}
					});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		
		//Add 'All' as first element of filter
		Map<String, String> itemAll = new HashMap<String, String>();
		itemAll.put("CID", "-1");
		itemAll.put("CNM", "All");
		itemAll.put("SHR", "1");
		clientNames.add(0, itemAll);
		itemAll = new HashMap<String, String>();
		itemAll.put("CID", "-1");
		itemAll.put("CNM", "All");
		itemAll.put("SHR", "2");
		clientNames.add(0, itemAll);
		
		return clientNames;
	}
	
	public List<Map<String, String>> getUserClientNames(String uid) throws Exception {
		List<Map<String, String>> clientNames = new ArrayList<Map<String, String>>();
		
		try {
			String sql = messages.getMessage("SQL_GET_USER_CLIENTNAMES", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			clientNames = jdbcTemplate.query(sql, new Object[] {uid}, 
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> map = new HashMap<String, String>();
							map.put("CID", rs.getString("CID"));
							map.put("CNM", rs.getString("CNM"));
							return map;
						}
					});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		
		//Add 'All' as first element of filter
		Map<String, String> itemAll = new HashMap<String, String>();
		itemAll.put("CID", "-1");
		itemAll.put("CNM", "All");
		clientNames.add(0, itemAll);
		
		return clientNames;
	}
	
	public List<Map<String, Object>> getOppDetails(String uid, String oid) throws Exception {
		List<Map<String, Object>> activities = new ArrayList<Map<String, Object>>();
		List<Map<String, String>> tasks = new ArrayList<Map<String, String>>();
		
		String activitySQL = messages.getMessage("SQL_GET_ACTIVITIES", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		String taskSQL = messages.getMessage("SQL_GET_TASKS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		
		activities = jdbcTemplate.query(activitySQL, new Object[] {oid}, 
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
		
		tasks = jdbcTemplate.query(taskSQL, new Object[] {oid}, 
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
		
		for (int i = 0; i < activities.size(); i ++) {
			List<Map<String, String>> subTaskList = new ArrayList<Map<String, String>>();
			Map<String, Object> activityMap = activities.get(i);
			String activityId = (String)activityMap.get("AID");
			for (int j = 0; j < tasks.size(); j ++) {
				Map<String, String> taskMap = tasks.get(j);
				String parentId = taskMap.get("PID");
				if (activityId.equals(parentId)) {
					subTaskList.add(taskMap);
				}
			}
			activityMap.put("TASKS", subTaskList);
		}
			
		return activities;
	}
	
	public long insertActivityOrTask(final Map<String, String> map) throws Exception {
		long stateId = 0;
		try {
			KeyHolder clientKeyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator(){
				String sql = messages.getMessage("SQL_NEW_ACTIVITY_TASK", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
				String uid = map.get("uid");
				String oid = map.get("oid");
				String aid = map.get("aid");
				String tid = map.get("tid");
				String status = map.get("status");
				
				//nodeActivity = true means Activity, nodeActivity = false means Task
				boolean nodeActivity = tid == null || "".equals(tid) ? true : false;
				
				String notes = map.get("notes");
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, uid);
					ps.setString(2, oid);
					if (nodeActivity) {
						ps.setString(3, "1");
						ps.setString(4, aid);
					} else {
						ps.setString(3, "2");
						ps.setString(4, tid);
					}
                    ps.setString(5, status);
                    ps.setString(6, notes);
                    
                    if ("1".equals(status) && nodeActivity) {
                    	ps.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
                    } else {
                    	ps.setDate(7, null);
                    }
                    
                    ps.setString(8, uid);
                    return ps;
				}
			}, clientKeyHolder);
			stateId = clientKeyHolder.getKey().longValue();
			
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return stateId;
	}
	
	public void updateActivityOrTask(final Map<String, String> map) throws Exception {
		try {
			KeyHolder clientKeyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator(){
				String sql = "";
				String status = map.get("status");
				String notes = map.get("notes");
				String sid = map.get("sid");
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					
					String checkbox = map.get("checkbox") == null ? "0" : map.get("checkbox");
					
					if ("0".equals(checkbox)) {
						sql = messages.getMessage("SQL_EDIT_ACTIVITY_TASK", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
					} else {
						sql = messages.getMessage("SQL_EDIT_ACTIVITY_TASK_WITHOUT_NOTES", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
					}
					
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					if ("0".equals(checkbox)) {
						ps.setString(1, status);
						ps.setString(2, notes);
						
						if (status != null && status.equals("1")) {
							ps.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
						} else {
							ps.setDate(3, null);
						}
	                    ps.setString(4, sid);
					} else {
						ps.setString(1, status);
						
						if (status != null && status.equals("1")) {
							ps.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
						} else {
							ps.setDate(2, null);
						}
	                    ps.setString(3, sid);
					}
					
                    return ps;
				}
			}, clientKeyHolder);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
	
	
	public void syncTasksForActivity(Map<String, String> map) throws Exception {
		try {
			List<Map<String, String>> commTaskList = getCommTasksOfActivity(map.get("aid"));
			List<String> commTaskIds = new ArrayList<String>();
			
			List<String> userTaskIds = new ArrayList<String>();
			List<String> notExistTaskIds = new ArrayList<String>();
			
			for (Map<String, String> commTaskMap : commTaskList) {
				commTaskIds.add(commTaskMap.get("ID"));
			}
			
			List<Map<String, String>> userOwnedTasksList = getUserOwnedTasks(map.get("uid"), map.get("oid"), commTaskIds);
			notExistTaskIds.addAll(commTaskIds);
			
			String viacheckbox = map.get("checkbox");
			String activityStatus = map.get("status");
			for (Map<String, String> userTaskMap : userOwnedTasksList) {
				String sid = userTaskMap.get("ID");
				String taskStatus = userTaskMap.get("VIACHECKBOX");
				String idItem = userTaskMap.get("IDITEM");
				userTaskIds.add(idItem);
				/*
				 * check/uncheck the activity complete/inprogress, all the tasks under it should be the same status as it.
				 * save activity notes and complete, all the tasks under it should be the same status as it.
				 * save activity notes only, the existed tasks under it won't change(the new tasks should be the same status as it).
				 * if the task status has been already the same as the activity, no need to update this task.
				 */
				if (!taskStatus.equals(activityStatus)) {
					Map<String, String> param = new HashMap<String, String>();
					param.putAll(map);
					param.put("sid",sid);
					if ("1".equals(viacheckbox) || "1".equals(activityStatus) || "0".equals(activityStatus)) {
						param.put("status", activityStatus);
					} else {
						param.put("status", taskStatus);
					}
//					param.put("notes", userTaskMap.get("NOTES"));
					param.put("checkbox", "1");//not update the task notes/rtNotes
					updateActivityOrTask(param);
				}
				
			}
			
			notExistTaskIds.removeAll(userTaskIds);
			
			for (String notExistTaskId : notExistTaskIds) {
				Map<String, String> param = new HashMap<String, String>();
				param.putAll(map);
				param.put("tid", notExistTaskId);
				param.put("status", activityStatus);
				param.put("notes", "");
				insertActivityOrTask(param);
			}
			
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
	
	public void syncActivityForTasksAllChecked(Map<String, String> map) throws Exception {
		try {
			List<Map<String, String>> commTaskList = getCommTasksOfActivity(map.get("aid"));
			List<String> commTaskIds = new ArrayList<String>();
			String tid = map.get("tid");
			
			
			for (Map<String, String> commTaskMap : commTaskList) {
				commTaskIds.add(commTaskMap.get("ID"));
			}
			
			List<Map<String, String>> userCompletedTasksList = getUserCompletedTasks(map.get("uid"), map.get("oid"), commTaskIds);
			List<String> userCompletedTaskIds = new ArrayList<String>();
			List<String> operatingTaskId = new ArrayList<String>();
			operatingTaskId.add(tid);
			
			for (Map<String, String> userCompletedTaskMap : userCompletedTasksList) {
				String idItem = userCompletedTaskMap.get("IDITEM");
				userCompletedTaskIds.add(idItem);
			}
			
			//Combined user completed task Ids with the task id that user currently operates
			userCompletedTaskIds.addAll(operatingTaskId);
			commTaskIds.removeAll(userCompletedTaskIds);
			
			
			Map<String, String> activityMap = getActivityByTask(map.get("oid"), map.get("uid"), map.get("aid"));
			Map<String, String> param = new HashMap<String, String>();
			
			String status = "0";
			if (commTaskIds.size() == 0) {
				status = map.get("status");
			} 
			
			if (activityMap != null &&  activityMap.size() > 0) {
				//size = 0 means all tasks related to this activity has been marked as completed
				// then need to update activity
				if (!status.equals(activityMap.get("VIACHECKBOX"))) {
					param.putAll(map);
					param.put("sid", activityMap.get("SID"));
					param.put("status", status);
//					param.put("notes", activityMap.get("NOTES"));
					param.put("checkbox", "1");//not update the activity notes/rtNotes
					updateActivityOrTask(param);
				}
			} else {
				param.putAll(map);
				param.put("tid", "");
				param.put("status", status);
				param.put("notes", "");
				insertActivityOrTask(param);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
	
	private Map<String, String> getActivityByTask(String oid, String uid, String aid) throws Exception {
		String sql = messages.getMessage("SQL_GET_ACTIVITY_BY_TASK", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		
		List<Map<String, String>> list = jdbcTemplate.query(sql, new String[] {oid, uid, aid}, 
				new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("SID", rs.getString("idKey"));
//					dataMap.put("NOTES", rs.getString("rtNotes"));
					dataMap.put("VIACHECKBOX", rs.getString("viaCheckBox"));
					return dataMap;
				}
		});
		
		return list == null || list.size() == 0 ? null : list.get(0);
	}
	
	public List<Map<String, String>> getCommTasksOfActivity(String aid) throws Exception {
		List<Map<String, String>> taskList = new ArrayList<Map<String, String>>();
		String sqlGetCommTasks = messages.getMessage("SQL_GET_COMM_TASKS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		
		taskList = jdbcTemplate.query(sqlGetCommTasks, new Object[] {aid}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("ID", rs.getString("idKey"));
						dataMap.put("NAME", rs.getString("Name"));
						dataMap.put("PURPOSE", rs.getString("Purpose"));
						dataMap.put("URL", rs.getString("URL"));
						dataMap.put("DESCRIPTION", rs.getString("Description"));
						return dataMap;
					}
				});
		return taskList;
	}
	
	private List<Map<String, String>> getUserOwnedTasks(String uid, String oid, List<String> taskIds) throws Exception {
		List<Map<String, String>> userTasksList = new ArrayList<Map<String, String>>();
		try {
			String sqlGetUserTasks = messages.getMessage("SQL_GET_USER_TASKS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("uid", uid);
			parameters.addValue("oid", oid);
			parameters.addValue("idItems", taskIds);
			
			NamedParameterJdbcTemplate namedParameterJdbcTemplate =   
				    new NamedParameterJdbcTemplate(jdbcTemplate);
			
			userTasksList = namedParameterJdbcTemplate.query(sqlGetUserTasks, parameters,
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> dataMap = new HashMap<String, String>();
							dataMap.put("ID", rs.getString("idKey"));
							dataMap.put("UID", rs.getString("uid"));
							dataMap.put("OPPID", rs.getString("oppID"));
							dataMap.put("CATEGORY", rs.getString("Category"));
							dataMap.put("IDITEM", rs.getString("idItem"));
							dataMap.put("VIACHECKBOX", rs.getString("viaCheckBox"));
//							dataMap.put("NOTES", rs.getString("notes"));
							dataMap.put("STARTDATE", rs.getString("startDate"));
							dataMap.put("COMPLETEDATE", rs.getString("completeDate"));
							dataMap.put("LAST_UPDATED", rs.getString("last_Updated"));
							return dataMap;
						}
					});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return userTasksList;
	}
	
	public List<Map<String, String>> getUserCompletedTasks(String uid, String oid,List<String> taskIds) throws Exception {
		List<Map<String, String>> userCompletedTasksList = new ArrayList<Map<String, String>>();
		try {
			String sqlGetUserCompletedTasks = messages.getMessage("SQL_GET_USER_COMPLETED_TASKS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("uid", uid);
			parameters.addValue("oid", oid);
			parameters.addValue("idItems", taskIds);
			
			NamedParameterJdbcTemplate namedParameterJdbcTemplate =   
				    new NamedParameterJdbcTemplate(jdbcTemplate);
			
			userCompletedTasksList = namedParameterJdbcTemplate.query(sqlGetUserCompletedTasks, parameters, 
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> dataMap = new HashMap<String, String>();
							dataMap.put("ID", rs.getString("idKey"));
							dataMap.put("UID", rs.getString("uid"));
							dataMap.put("OPPID", rs.getString("oppID"));
							dataMap.put("CATEGORY", rs.getString("Category"));
							dataMap.put("IDITEM", rs.getString("idItem"));
							dataMap.put("VIACHECKBOX", rs.getString("viaCheckBox"));
							dataMap.put("NOTES", rs.getString("notes"));
							dataMap.put("STARTDATE", rs.getString("startDate"));
							dataMap.put("COMPLETEDATE", rs.getString("completeDate"));
							dataMap.put("LAST_UPDATED", rs.getString("last_Updated"));
							return dataMap;
						}
					});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return userCompletedTasksList;
	}
	
	public List<Map<String, String>> getOppIdSalesConnList(String idSalesConn, String uid) throws Exception {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		try {
			String sql = messages.getMessage("SQL_GET_OPP_SALES_CONN", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			result = jdbcTemplate.query(sql, new Object[] {idSalesConn, uid}, 
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> map = new HashMap<String, String>();
							map.put("OID", rs.getString("OID"));
							return map;
						}
					});
			
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return result;
	}
	
	public void updateOppLastUpdate(final String oid) throws Exception {
		try {
			KeyHolder clientKeyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator(){
				String sql = messages.getMessage("SQL_EDIT_OPP_DT", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, oid);
                    return ps;
				}
			}, clientKeyHolder);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public void insertShareOppRecord(final String oppId, final String shareUId) throws Exception {
		try {
			KeyHolder clientKeyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator(){
				String sql = messages.getMessage("SQL_NEW_SHAREDWITH", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, Long.parseLong(oppId));
					ps.setString(2, shareUId);
                    return ps;
				}
			}, clientKeyHolder);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	public int checkShareWith(String oppId, String shareUId)
			throws Exception {
		String sql = messages.getMessage("SQL_CHECK_SHAREDPERSON", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		Integer status = jdbcTemplate.queryForObject(sql, new String[] {shareUId, shareUId, oppId}, Integer.class);
		return status.intValue();
	}

	public List<String> getSharedWith(String oppId) throws Exception {
		String sql = messages.getMessage("SQL_GET_SHAREDWITH", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		return jdbcTemplate.queryForList(sql, new String[] {oppId}, String.class);
	}
	
	public List<String> getSharedBy(String oppId, String shareUId) throws Exception {
		String sql = messages.getMessage("SQL_GET_SHAREDBY", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		return jdbcTemplate.queryForList(sql, new String[] {oppId}, String.class);
	}
	
	
	public Map<String, String> getOppDetailById(String sid) {
		if (sid == null || "".equals(sid.trim())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("SID", "");
			map.put("NOTES", "");
			return map;
		}
		
		String sql = messages.getMessage("SQL_GET_OPP_DETAIL_BY_ID", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		
		List<Map<String, String>> list = jdbcTemplate.query(sql, new String[] {sid}, 
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

	public Map<String, Object> getAssociateDate(String uid) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String sql = messages.getMessage("SQL_GET_ASSOCIATEDATE", 
				new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, uid);
		if (list != null && !list.isEmpty()) {
			for (Map<String, Object> opp : list) {
				map.put((String) opp.get("idSalesConnect"), opp);
			}
		}
		return map;
	}

	public List<Map<String, String>> getMyClientOpps(String uid)
			throws Exception {
		String sql = messages.getMessage("SQL_GET_MY_CLIENTOPPS", 
				new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		List<Map<String, String>> list = jdbcTemplate.query(sql, new String[] {uid}, 
				new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("CID", rs.getString("CID"));
					dataMap.put("CNM", rs.getString("CNM"));
					dataMap.put("OID", rs.getString("OID"));
					dataMap.put("ONM", rs.getString("ONM"));
					dataMap.put("ODP", rs.getString("ODP"));
					return dataMap;
				}
		});
		return list;
	}
	
	public String addSalesConnectClient(final Map<String, String> scOpp, final String uid)
			throws Exception {
		//INSERT INTO {0}.teamsdclients(uid, Name, sc_account_id, byWhom, last_Updated, createDate, associateDate) VALUES(?,?,?,?,now(),now(),now())
		KeyHolder clientKeyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			String newClientSql = messages.getMessage("SQL_ADD_SC_CLIENT", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			String cid = scOpp.get("sc_cid");
			String clientName = scOpp.get("sc_cnm");
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(newClientSql, Statement.RETURN_GENERATED_KEYS);
				//uid, Name, sc_account_id, byWhom
				ps.setString(1, uid);
				ps.setString(2, clientName);
                ps.setString(3, cid);
                ps.setString(4, uid);
                return ps;
			}
		}, clientKeyHolder);
		return String.valueOf(clientKeyHolder.getKey());
	}

	public String addSalesConnectOpportunity(
			final Map<String, String> scOpp, final String cid, final String uid) throws Exception {
		//INSERT INTO {0}.teamsd_opp(uid, byWhom, idClient, idSalesConnect, oppName, startDate, last_Updated, associateDate) VALUE(?,?,?,?,?,date(now()),now(),now())
		KeyHolder clientKeyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			String newOppSql = messages.getMessage("SQL_ADD_SC_OPP", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			String idSalesConnect = scOpp.get("sc_oid");
			String oppName = scOpp.get("sc_odp");
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(newOppSql, Statement.RETURN_GENERATED_KEYS);
				//uid, byWhom, idClient, idSalesConnect, oppName
				ps.setString(1, uid);
				ps.setString(2, uid);
                ps.setString(3, cid);
                ps.setString(4, idSalesConnect);
                ps.setString(5, oppName);
                return ps;
			}
		}, clientKeyHolder);
		return String.valueOf(clientKeyHolder.getKey());
	}

	public void associateSalesConnectClient(Map<String, String> scClient, String cid)
			throws Exception {
		//UPDATE {0}.teamsdclients SET Name=?, sc_account_id=?, associateDate=now(), last_Updated=now() WHERE idKey=?
		String sql = messages.getMessage("SQL_ASSOCIATE_SC_CLIENT", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		jdbcTemplate.update(sql, new Object[]{scClient.get("sc_cnm"), scClient.get("sc_cid"), cid});
	}

	public void associateSalesConnectOpportunity(
			Map<String, String> scOpp, String cid, String oid) throws Exception {
		//UPDATE {0}.teamsdstate SET Name=?, idSalesConnect=?, associateDate=now(), last_Updated=now() WHERE idKey=?
//		String sql = messages.getMessage("SQL_ASSOCIATE_SC_OPP", new String[] {METHOD_GUIDANCE}, null);
//		jdbcTemplate.update(sql, new Object[]{scOpp.get("sc_odp"), scOpp.get("sc_oid"), oid});
		String sql = messages.getMessage("SQL_ASSOCIATE_SC_ClIENTOPP", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		jdbcTemplate.update(sql, new Object[]{scOpp.get("sc_odp"), scOpp.get("sc_oid"), cid, oid});
	}
	
	public String checkClientAssociated(String scCid, String uid) throws Exception {
		String sql = messages.getMessage("SQL_CHECK_CLIENT", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] {uid, scCid});
		
		String cid = null;
		if (!list.isEmpty()) {
			cid = String.valueOf(list.get(0).get("idKey"));
		}
		
		return cid;
	}

	public List<Map<String, String>> getClientOppsAdded(String uid, String oidMin, String oidMax)
			throws Exception {
		List<Map<String, String>> clientOpportunities = new ArrayList<Map<String, String>>();
		String fieds = messages.getMessage("SQL_GET_OPP_FIELDS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		String subSql = messages.getMessage("SQL_GET_OPP_STATES", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		String sql = messages.getMessage("SQL_GET_OPP_LIST_ADDED", new String[] {fieds, subSql, DB_NAME_METHOD_GUIDANCE}, null);
		try {
			clientOpportunities = jdbcTemplate.query(sql, new Object[] {uid, uid, oidMin, oidMax}, 
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
						map.put("SHR", rs.getString("SHR"));
						map.put("UID", rs.getString("uid"));
						return map;
					}
				});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return clientOpportunities;
	}
	
	
	public List<Map<String, String>> getGuidanceQuestions(String idTask,
			String idState) throws Exception {
		String sql = messages.getMessage("SQL_GET_GUIDANCE_QUESTIONS", 
				new String[] {DB_NAME_METHOD_GUIDANCE}, null);
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

	@Override
	public long insertTaskNotesAnswer(final String idState, final String idQuestion,
			final String choice, final String notes) throws Exception {
		
		KeyHolder clientKeyHolder = new GeneratedKeyHolder();
		//Insert data into client table
		jdbcTemplate.update(new PreparedStatementCreator(){
			String sql = messages.getMessage("SQL_ADD_GUIDANCE_ANSWERS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, idState);
				ps.setString(2, idQuestion);
                ps.setString(3, choice);
                ps.setString(4, notes);
                return ps;
			}
		}, clientKeyHolder);
		return clientKeyHolder.getKey().longValue();
	}

	@Override
	public void updateTaskNotesAnswer(String idAnswer, String choice,
			String notes) throws Exception {
		String sql = messages.getMessage("SQL_EDIT_GUIDANCE_ANSWERS", 
				new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		jdbcTemplate.update(sql, new Object[] {choice, notes, idAnswer});
	}
	
	
	
	
	
	
	
	
	
	public int deleteOpp(final Map<String, String> map) throws Exception {
		KeyHolder oppKeyHolder = new GeneratedKeyHolder();
		int resultStatus = jdbcTemplate.update(new PreparedStatementCreator(){
			String deleteOppSql = messages.getMessage("SQL_DELETE_OPPS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			String idUser = map.get("UID");
			String idOpp = map.get("OID");
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(deleteOppSql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, idUser);
				ps.setString(2, idOpp);
                return ps;
			}
		}, oppKeyHolder);
		return resultStatus;
	}
	
	public int archiveOpp(final Map<String, String> map) throws Exception {
		KeyHolder oppKeyHolder = new GeneratedKeyHolder();
		int resultStatus = jdbcTemplate.update(new PreparedStatementCreator(){
			String archiveOppSql = messages.getMessage("SQL_ARCHIVE_OPPS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			String idUser = map.get("UID");
			String idOpp = map.get("OID");
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(archiveOppSql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, idUser);
				ps.setString(2, idOpp);
                return ps;
			}
		}, oppKeyHolder);
		return resultStatus;
	}
	
	public int retrieveOpps(final Map<String, String> map) throws Exception {
		KeyHolder oppKeyHolder = new GeneratedKeyHolder();
		int resultStatus = jdbcTemplate.update(new PreparedStatementCreator(){
			String oidGroup = map.get("OID");
			String retrieveOppSql = messages.getMessage("SQL_RETRIEVE_OPPS", new String[] {DB_NAME_METHOD_GUIDANCE, oidGroup}, null);
			String idUser = map.get("UID");
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(retrieveOppSql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, idUser);
                return ps;
			}
		}, oppKeyHolder);
		return resultStatus;
	}

	public List<Map<String, String>> getArchiveOpps(String idUser) throws Exception {
		String sql = messages.getMessage("SQL_GET_ARCHIVE_OPPS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		List<Map<String, String>> list = jdbcTemplate.query(sql, new String[] {idUser}, 
				new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("CID", rs.getString("CID"));
					dataMap.put("OID", rs.getString("OID"));
					dataMap.put("CNM", rs.getString("CNM"));
					dataMap.put("ONM", rs.getString("ONM"));
					dataMap.put("ODP", rs.getString("ODP"));
					dataMap.put("archiveDate", rs.getString("archiveDate"));
					return dataMap;
				}
		});
		return list;
	}

	@Override
	public int transferOpps(String oppId, String ownerUId,
			String toUid, String type) {
		String sql = messages.getMessage("SQL_TRANSFER_OPPS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		return jdbcTemplate.update(sql, new Object[]{type, toUid, ownerUId, oppId});
	}

	@Override
	public List<Map<String, String>> getAllTransferOpps(String uid) throws Exception {
		List<Map<String, String>> clientOpportunities = new ArrayList<Map<String, String>>();
		String sql = messages.getMessage("SQL_GET_ALL_TRANSFER_OPPS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		try {
			clientOpportunities = jdbcTemplate.query(sql, new Object[] {uid}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("CID", rs.getString("CID"));
						map.put("CNM", rs.getString("CNM"));
						map.put("CDP", rs.getString("CDP"));
						map.put("OID", rs.getString("OID"));
						map.put("ONM", rs.getString("ONM"));
						map.put("ODP", rs.getString("ODP"));
						map.put("idSalesConnect", rs.getString("idSalesConnect"));
						map.put("transferBy", rs.getString("transferBy"));
						map.put("transferDate", rs.getString("transferDate"));
						return map;
					}
				});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return clientOpportunities;
	}

	@Override
	public int getAllTransferOppCount(String uid) throws Exception {
		int result = 0;
		String sql = messages.getMessage("SQL_GET_ALL_TRANSFER_OPP_COUNT", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		try {
			List<Integer> list = jdbcTemplate.queryForList(sql, new Object[] {uid}, Integer.class);
			result = list.get(0);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return result;
	}

	@Override
	public void processTransferOpp(final Map<String, String> map) throws Exception {
		//oid, uid, type, clientId, clientName
		try {
			String idUser = map.get("uid");
			String idOpp = map.get("oid");
			String type = map.get("type");
			if (type.equals("0")) {//decline
				//change active to be 1
				String sql = messages.getMessage("SQL_SET_OPP_ACTIVE", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
				jdbcTemplate.update(sql, new Object[]{idOpp});
			} else if (type.equals("1")) {//accept
				int cid = 0;
				String clientId = map.get("clientId");
				KeyHolder clientKeyHolder = new GeneratedKeyHolder();
				if (clientId == null || "".equals(clientId) || "0".equals(clientId)) {
					//Insert data into client table
					jdbcTemplate.update(new PreparedStatementCreator(){
						String newClientSql = messages.getMessage("SQL_NEW_CLIENT", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
						String clientName = map.get("clientName");
						String uid = map.get("uid");
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = connection.prepareStatement(newClientSql, Statement.RETURN_GENERATED_KEYS);
							ps.setString(1, uid);
							ps.setString(2, clientName);
		                    ps.setString(3, clientName);
		                    ps.setString(4, uid);
		                    return ps;
						}
					}, clientKeyHolder);
					cid = clientKeyHolder.getKey().intValue();
				}
				else {
					cid = Integer.parseInt(clientId);
				}
				
				//Transfer opportunity
				String sql = messages.getMessage("SQL_ACCEPT_TRANSFER_OPP", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
				jdbcTemplate.update(sql, new Object[]{idUser, cid, idUser, idOpp});
				//Delete share for previous uid
				sql = messages.getMessage("SQL_DELETE_OPP_SHARE", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
				jdbcTemplate.update(sql, new Object[]{idOpp, idUser});
				
				String getTypeSql = messages.getMessage("SQL_GET_TRANSFER_TYPE", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
				List<Map<String, String>> list = jdbcTemplate.query(getTypeSql, new String[] {idOpp}, 
						new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> dataMap = new HashMap<String, String>();
							dataMap.put("transferType", rs.getString("transferType"));
							dataMap.put("shareWith", rs.getString("transferTouid"));
							return dataMap;
						}
				});
				Map<String, String> rMap = list.get(0);
				String transferType = rMap.get("transferType");
				String shareWith = rMap.get("shareWith");
				
				if (transferType.equals("1")) {//Keep as collaborator
					this.insertShareOppRecord(idOpp, shareWith);
				}
			}			
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public int updateTransferOppStatus(String uid) {
		String sql = messages.getMessage("SQL_UPDATE_TRANSFER_OPPS_STATUS", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
		return jdbcTemplate.update(sql, new Object[]{uid, uid});
	}
	
	public void updateShareListForOpp(String oppId, List<String> addedUsers, List<String> removedUsers) throws Exception {
		for (String user : addedUsers) {
			this.insertShareOppRecord(oppId, user);
		}
		for (String user : removedUsers) {
			String sql = messages.getMessage("SQL_DELETE_OPP_SHARE", new String[] {DB_NAME_METHOD_GUIDANCE}, null);
			jdbcTemplate.update(sql, new Object[]{oppId, user});
		}
	}
	
	
	public List<Map<String, String>> getClientIdentifier(String clientName) throws Exception {
		// Andy 2016.10.10 17:42
		//John Zhu 2016.11.21. list the GBG results first and then Global Clients. In each group, name is in alphabetic order. See story 1270999
	
		// John Zhu 2016.11.21 For GLOBAL_BUYING_GROUP table, only search rows with MANDT=100 and ACTIVE_STATUS=1 (see Story 1270999) 
		String sql_GBG = "SELECT * FROM SOE.GLOBAL_BUYING_GROUP WHERE UPPER(GLOBAL_BUYING_GROUP_NAME) LIKE UPPER(?) AND MANDT=100 AND ACTIVE_STATUS=1 ORDER BY GLOBAL_BUYING_GROUP_NAME FETCH FIRST 100 ROWS ONLY";
		List<Map<String, String>> list_GBG = jdbcTemplateCISF.query(sql_GBG, new String[] {clientName}, 
				new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("clientName", rs.getString("GLOBAL_BUYING_GROUP_NAME"));
					dataMap.put("clientIdentifier", rs.getString("GLOBAL_BUYING_GROUP_ID"));
					dataMap.put("location", rs.getString("LAND1"));
					dataMap.put("rowNum", rowNum + "");
					return dataMap;
				}
		});
		
		// John Zhu 2016.11.21 For LEGAL_GBL_CLIENT table, only search rows with MANDT=100 (see Story 1270999) 
		String sql_GC = "SELECT * FROM SOE.legal_gbl_client where UPPER(LEGAL_GBL_CLIENT_NAME) like UPPER(?) AND MANDT=100 ORDER BY LEGAL_GBL_CLIENT_NAME FETCH FIRST 100 ROWS ONLY";
		List<Map<String, String>> list_GC = jdbcTemplateCISF.query(sql_GC, new String[] {clientName}, 
				new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("clientName", rs.getString("LEGAL_GBL_CLIENT_NAME"));
					dataMap.put("clientIdentifier", rs.getString("GLOBAL_CLIENT_ID"));
					dataMap.put("location", "");
					dataMap.put("rowNum", rowNum + "");
					return dataMap;
				}
		});
		
		list_GBG.addAll(list_GC);
		return list_GBG;
		
		
	}
}
