package com.ibm.soe.rest.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.dao.PovEducationDao;

@Repository
public class PovEducationDaoImpl implements PovEducationDao {
	
	private static Logger logger = Logger.getLogger(PovEducationDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MessageSource messages;

	@Value("${DB_NAME_METHOD_GUIDANCE}")
	private String METHOD_GUIDANCE;

	@Override
	public List<Map<String, Object>> getPovModuleList(String idUser)
			throws Exception {
		List<Map<String, Object>> list = null;
		String sql = messages.getMessage("SQL_GET_POV_MODULE_LIST", new String[] {METHOD_GUIDANCE}, null);
		try {
			list = jdbcTemplate.query(sql, new Object[] {idUser}, 
				new RowMapper<Map<String, Object>>() {
					public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("idModule", rs.getString("idModule"));
						map.put("title", rs.getString("title"));
						map.put("content", rs.getString("content"));
						map.put("topicStates", rs.getString("topicStates"));
						map.put("status", rs.getString("status"));
						map.put("startDate", rs.getString("startDate"));
						map.put("lastUpdated", rs.getString("lastUpdated"));
						map.put("LU", rs.getString("LU"));
						return map;
					}
				});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return list;
	}

	@Override
	public List<Map<String, String>> getPovTopicList(String idUser,
			String idModule) throws Exception {
		List<Map<String, String>> list = null;
		String sql = messages.getMessage("SQL_GET_POV_TOPIC_LIST", new String[] {METHOD_GUIDANCE}, null);
		try {
			list = jdbcTemplate.query(sql, new Object[] {idUser, idModule}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("idTopic", rs.getString("idTopic"));
						map.put("idState", rs.getString("idState"));
						map.put("title", rs.getString("title"));
						map.put("status", rs.getString("status"));
						return map;
					}
				});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return list;
	}

	@Override
	public String getPovNotes(String idState) throws Exception {
		String notes = null;
		String sql = messages.getMessage("SQL_GET_POV_NOTES", new String[] {METHOD_GUIDANCE}, null);

		try {
			notes = jdbcTemplate.queryForObject(sql, new Object[]{idState}, java.lang.String.class);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}

		return notes;
	}

	@Override
	public List<Map<String, String>> getPovResourceList(String idTopic)
			throws Exception {
		List<Map<String, String>> list = null;
		String sql = messages.getMessage("SQL_GET_POV_RESOURCE_LIST", new String[] {METHOD_GUIDANCE}, null);
		try {
			list = jdbcTemplate.query(sql, new Object[] {idTopic}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("idResource", rs.getString("idResource"));
						map.put("title", rs.getString("title"));
						map.put("content", rs.getString("content"));
						return map;
					}
				});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return list;
	}

	@Override
	public String getPovResourceContent(String idResource) throws Exception {
		String content = null;
		String sql = messages.getMessage("SQL_GET_POV_RESOURCE_CONTENT", new String[] {METHOD_GUIDANCE}, null);

		try {
			content = jdbcTemplate.queryForObject(sql, new Object[]{idResource}, java.lang.String.class);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}

		return content;
	}

	@Override
	public Map<String, String> getModuleState(String idUser, String idModule)
			throws Exception {
		Map<String, String> module = new HashMap<String, String>();
		String sql = messages.getMessage("SQL_GET_MODULE_STATE", new String[] {METHOD_GUIDANCE}, null);
		try {
			module = jdbcTemplate.queryForObject(sql, new Object[] {idUser, idModule}, 
					new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("topicStates", rs.getString("topicStates"));
					map.put("status", rs.getString("status"));
					map.put("startDate", rs.getString("startDate"));
					map.put("lastUpdated", rs.getString("lastUpdated"));
					map.put("LU", rs.getString("LU"));
					return map;
				}
			});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return module;
	}

	@Override
	public long addTopicStateCompleted(final String idUser, final String idTopic)
			throws Exception {
		long idState = 0;
		KeyHolder clientKeyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator(){
				String sql = messages.getMessage("SQL_ADD_TOPIC_CHECKED", new String[] {METHOD_GUIDANCE}, null);
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, idUser);
					ps.setString(2, idTopic);
                    return ps;
				}
			}, clientKeyHolder);
			idState = clientKeyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return idState;
	}

	@Override
	public int editTopicStateCompleted(String idState) throws Exception {
		String sql = messages.getMessage("SQL_EDIT_TOPIC_CHECKED", new String[] {METHOD_GUIDANCE}, null);
		return jdbcTemplate.update(sql, new Object[]{idState});
	}

	@Override
	public int editTopicStateInprogress(String idState) throws Exception {
		String sql = messages.getMessage("SQL_EDIT_TOPIC_UNCHECK", new String[] {METHOD_GUIDANCE}, null);
		return jdbcTemplate.update(sql, new Object[]{idState});
	}

	@Override
	public long addTopicNotes(final String idUser, final String idTopic, final String notes,
			final String status) throws Exception {
		long idState = 0;
		KeyHolder clientKeyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator(){
				String sql = messages.getMessage("SQL_ADD_TOPIC_NOTES", new String[] {METHOD_GUIDANCE}, null);
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, idUser);
					ps.setString(2, idTopic);
					ps.setString(3, notes);
					ps.setString(4, status);
					ps.setString(5, status);
                    return ps;
				}
			}, clientKeyHolder);
			idState = clientKeyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return idState;
	}

	@Override
	public int editTopicNotes(String idState, String notes, String status)
			throws Exception {
		String sql = messages.getMessage("SQL_EDIT_TOPIC_NOTES", new String[] {METHOD_GUIDANCE}, null);
		return jdbcTemplate.update(sql, new Object[]{notes, status, status, status, idState});
	}
	
	@Override
	public List<Map<String, String>> getViewAllNotes(String idUser) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listIdtopic = null;
		String sql = messages.getMessage("SQL_GET_POV_VIEW_ALL_NOTES", new String[] {METHOD_GUIDANCE}, null);
		String allIdTopic = messages.getMessage("SQL_GET_ALL_IDTOPIC", new String[] {METHOD_GUIDANCE}, null);
		try {
			Date dateBefore=new Date();
			//DateFormat formatBefore=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	//System.out.println(formatBefore.format(dateBefore));
			System.out.println(dateBefore);
			listIdtopic = jdbcTemplate.query(allIdTopic, new Object[] {idUser}, 
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> mapIdtopic = new HashMap<String, String>();
							mapIdtopic.put("idTopic", rs.getString("idTopic"));
							return mapIdtopic;
						}
					});
			for (int i = 0; i < listIdtopic.size(); i++){
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap = jdbcTemplate.queryForObject(sql, new Object[] {idUser, listIdtopic.get(i).get("idTopic")}, 
						new RowMapper<Map<String, String>>() {
							public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
								Map<String, String> map = new HashMap<String, String>();
								map.put("idModule", rs.getString("idModule"));
								map.put("moduleTitle", rs.getString("moduleTitle"));
								map.put("idTopic", rs.getString("idTopic"));
								map.put("topicTitle", rs.getString("topicTitle"));
								map.put("rtNotes", rs.getString("rtNotes"));
								return map;
							}
						});
				list.add(tempMap);
			}
			Date dateAfter=new Date();
			logger.info(dateAfter);
			System.out.println(dateAfter);
	    	int timedifference = Math.round((dateAfter.getTime()- dateBefore.getTime())/1000/60);
	    	logger.info("query for get all view notes took :" + timedifference + " minutes");
	    	System.out.println("query for get all view notes took :" + timedifference + " minutes");
	    	
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return list;
	}	
	
	@Override
	public List<Map<String, String>> getViewAllNotes(String idUser,String type, String oppID) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> taskList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> taskQaList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> activietList = null;
		
		String activityIds = messages.getMessage("SQL_GET_ALL_ACTIVITY", new String[] {METHOD_GUIDANCE}, null);
		String notesSql = messages.getMessage("SQL_GET_EG_VIEW_ALL_NOTES", new String[] {METHOD_GUIDANCE}, null);
		String qaSql = messages.getMessage("SQL_GET_EG_NOTES_QA", new String[] {METHOD_GUIDANCE}, null);
		
		try {
			Date dateBefore=new Date();
			//DateFormat formatBefore=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	//System.out.println(formatBefore.format(dateBefore));
			System.out.println(dateBefore);
			activietList = jdbcTemplate.query(activityIds, new Object[] {oppID}, 
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> mapActiviey = new HashMap<String, String>();
							mapActiviey.put("idKey", rs.getString("idKey"));
							mapActiviey.put("category", rs.getString("category"));
							mapActiviey.put("Name", rs.getString("Name"));
							mapActiviey.put("rtNotes", rs.getString("rtNotes"));
							mapActiviey.put("type", rs.getString("type"));// type=ACT
							return mapActiviey;
						}
					});
			for (int i = 0; i < activietList.size(); i++){
				list.add(activietList.get(i));
				taskList = jdbcTemplate.query(notesSql, new Object[] {oppID, activietList.get(i).get("idKey")}, 
						new RowMapper<Map<String, String>>() {
							public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
								Map<String, String> map = new HashMap<String, String>();
								map.put("idKey", rs.getString("idKey"));
								map.put("category", rs.getString("category"));
								map.put("Name", rs.getString("Name"));
								map.put("rtNotes", rs.getString("rtNotes"));
								map.put("type", rs.getString("type"));// type=stateID
								return map;
							}
						});
				for (int j = 0; j < taskList.size(); j++){
					list.add(taskList.get(j));
					taskQaList = jdbcTemplate.query(qaSql, new Object[] {taskList.get(j).get("type"),taskList.get(j).get("idKey")}, 
							new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> map = new HashMap<String, String>();
							map.put("idKey", rs.getString("idKey"));
							map.put("category", rs.getString("category"));//q_choice
							map.put("Name", rs.getString("Name"));//q_name
							map.put("rtNotes", rs.getString("rtNotes"));//q_answer
							map.put("type", rs.getString("type"));// type=QA
							return map;
						}
					});
					list.addAll(taskQaList);
				}
				
			}
			Date dateAfter=new Date();
			logger.info(dateAfter);
			System.out.println(dateAfter);
	    	int timedifference = Math.round((dateAfter.getTime()- dateBefore.getTime())/1000/60);
	    	logger.info("query for get all view notes took :" + timedifference + " minutes");
	    	System.out.println("query for get all view notes took :" + timedifference + " minutes");
	    	
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return list;
	}	
	
	@Override
	public List<Map<String, String>> getMailSub(String oppID) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = messages.getMessage("SQL_GET_EG_NOTES_SUB", new String[] {METHOD_GUIDANCE}, null);
		try {
			list = jdbcTemplate.query(sql, new Object[] {oppID}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("clientName", rs.getString("name"));
						map.put("idSalesConnect", rs.getString("idSalesConnect"));
						map.put("oppName", rs.getString("oppName"));
						map.put("description", rs.getString("Description"));
						return map;
					}
				});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return list;
	}	
}
