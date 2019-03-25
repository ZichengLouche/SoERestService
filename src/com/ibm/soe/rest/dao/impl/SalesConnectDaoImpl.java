package com.ibm.soe.rest.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.SalesConnectDao;

@Repository
@Transactional
public class SalesConnectDaoImpl implements SalesConnectDao {

	private static Logger logger = Logger.getLogger(SalesConnectDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplateSC;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MessageSource messages;

	@Value("${DB_SPACE_SALES_CONNECT}")
	private String SALES_CONNECT;

	@Value("${DB_NAME_ACCELERATOR_WRITE}")
	private String ACCELERATOR_WRITE;
	
	
	public List<Map<String, String>> getClientOpportunities(String uid)
			throws Exception {
		List<Map<String, String>> clientOpportunities = new ArrayList<Map<String, String>>();

		try {
			String fields = messages.getMessage("SQL_QUERY_CLIENT_OPPS_FIELDS",
					new String[] {}, null);
			String filter = messages.getMessage("SQL_QUERY_CLIENT_OPPS_FILTER",
					new String[] {}, null);
			String subSql = messages.getMessage("SQL_QUERY_CLIENT_OPPS",
					new String[] { SALES_CONNECT, fields, filter }, null);
			String sql = messages.getMessage("SQL_QUERY_CLIENT_OPPS_ALL",
					new String[] { subSql }, null);

			clientOpportunities = jdbcTemplateSC.query(sql, new Object[] {uid, uid},
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs,
								int rowNum) throws SQLException {
							Map<String, String> map = new HashMap<String, String>();
							map.put("ROWNUM", rs.getString("ROWNUM"));
							map.put("SC_CID", rs.getString("CID"));
							map.put("SC_OID", rs.getString("OID"));
							map.put("SC_CNM", rs.getString("CNM"));
//							map.put("SC_ONM", rs.getString("ONM"));
							map.put("SC_ODP", rs.getString("ODP"));
							map.put("SC_LU", rs.getString("LU"));
							map.put("LAST_UPDATED", rs.getString("DATE_MODIFIED"));
//							map.put("CCMS_CID", rs.getString("CCMS_ID"));
							return map;
						}
					});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return clientOpportunities;
	}

	public List<Map<String, String>> getClientOpportunities(String uid, String orderBy, 
			String order, int seqFrom, int seqTo) throws Exception {
		List<Map<String, String>> clientOpportunities = new ArrayList<Map<String, String>>();

		try {
			String fields = messages.getMessage("SQL_QUERY_CLIENT_OPPS_FIELDS",
					new String[] {}, null);
			String filter = messages.getMessage("SQL_QUERY_CLIENT_OPPS_FILTER",
					new String[] {}, null);
			String subSql = messages.getMessage("SQL_QUERY_CLIENT_OPPS",
					new String[] { SALES_CONNECT, fields, filter }, null);
			String FieldOrderBy = "DATE_MODIFIED";
			String orderType = "DESC";
			if ("CNM".equals(orderBy)) {
				FieldOrderBy = messages.getMessage("SQL_QUERY_CLIENT_OPPS_ORDERBY_CNM",
						new String[] {}, null);
			} else if ("OID".equals(orderBy)) {
				FieldOrderBy = messages.getMessage("SQL_QUERY_CLIENT_OPPS_ORDERBY_OID",
						new String[] {}, null);
			}
			if ("".equals(order) || "ASC".equalsIgnoreCase(order)) {
				orderType = "ASC";
			}
			String sql = messages.getMessage("SQL_QUERY_CLIENT_OPPS_PAGE",
					new String[] { subSql, FieldOrderBy, orderType }, null);

			clientOpportunities = jdbcTemplateSC.query(sql, new Object[] { uid,
					uid, seqFrom, seqTo },
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs,
								int rowNum) throws SQLException {
							Map<String, String> map = new HashMap<String, String>();
							map.put("ROWNUM", rs.getString("ROWNUM"));
							map.put("SC_CID", rs.getString("CID"));
							map.put("SC_OID", rs.getString("OID"));
							map.put("SC_CNM", rs.getString("CNM"));
//							map.put("SC_ONM", rs.getString("ONM"));
							map.put("SC_ODP", rs.getString("ODP"));
							map.put("SC_LU", rs.getString("LU"));
							map.put("LAST_UPDATED", rs.getString("DATE_MODIFIED"));
//							map.put("CCMS_CID", rs.getString("CCMS_ID"));
							return map;
						}
					});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return clientOpportunities;
	}

	public long getClientOpportunitiesCount(String uid) throws Exception {

		long oppCount = 0;
		try {
			String fields = messages.getMessage("SQL_QUERY_CLIENT_OPPS_FIELDS",
					new String[] {}, null);
			String filter = messages.getMessage("SQL_QUERY_CLIENT_OPPS_FILTER",
					new String[] {}, null);
			String subSql = messages.getMessage("SQL_QUERY_CLIENT_OPPS",
					new String[] { SALES_CONNECT, fields, filter }, null);
			String sql = messages.getMessage("SQL_QUERY_CLIENT_OPPS_COUNT",
					new String[] { subSql }, null);

			String strOppCount = jdbcTemplateSC.queryForObject(sql,
					new Object[] { uid, uid }, java.lang.String.class);
			oppCount = Long.parseLong(strOppCount);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return oppCount;
	}

	public Map<String, String> checkIfUserExsitInSC(String uid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String sql = messages.getMessage("SQL_QUERY_USER_IF_EXSIT", new String[] { SALES_CONNECT }, null);
			String strOppCount = jdbcTemplateSC.queryForObject(sql, new Object[] { uid }, java.lang.String.class);
			map.put("userExsitFlag", strOppCount);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return map;
	}
	
	public  Map<String, Integer> reportConnectIssue(Map<String, String> param) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			String uid = param.get("uid");
			String tileName = param.get("tileName");
			String errorMessage = param.get("errorMessage");
			String sql = messages.getMessage("SQL_INSERT_REPORT_CONNECT_ISSUE", new String[] {ACCELERATOR_WRITE}, null);
			Integer strOppCount = jdbcTemplate.update(sql, new Object[] {uid,tileName,errorMessage});
			map.put("reportConnectIssueFlag", strOppCount);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return map;
	}
}
