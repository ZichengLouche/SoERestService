package com.ibm.soe.rest.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.dao.AcceleratorsDao;

@Repository
public class AcceleratorsDaoImpl implements AcceleratorsDao {
	
	private static Logger logger = Logger.getLogger(AcceleratorsDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Map<String, String>> getInitIndustry() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT -1 AS ID_I, CONCAT('ALL INDUSTRIES', ' (', SUM(SUBTABLE.X), ')') AS X FROM (SELECT ");
		sb.append("COUNT(ACC.INDUSTRY) AS X ");
		sb.append("FROM INDUSTRY AS IND LEFT JOIN ACCELERATORS_W ACC ON (IND.IDKEY = ACC.IDIND) ");
		sb.append(") AS SUBTABLE UNION ALL ");
		sb.append("SELECT IND.IDKEY AS ID_I, CONCAT(IND.NAME, ' (', COUNT(ACC.INDUSTRY), ')') AS X ");
		sb.append("FROM ACCELERATORS_W ACC LEFT JOIN INDUSTRY AS IND ON (IND.IDKEY = ACC.IDIND) ");
		sb.append("GROUP BY ID_I ORDER BY ID_I ");
		
		List<Map<String, String>> industryList = new ArrayList<Map<String, String>>();
		
		try {
			industryList = jdbcTemplate.query(sb.toString(), new Object[] {}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("id_I", rs.getString("ID_I"));
						map.put("X", rs.getString("X"));
						return map;
					}
				});
		} catch (Exception e) {
			logger.error(e);
		}
		return industryList;
	}
	
	public List<Map<String, String>> getInitDocType() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT -1 AS TYPE, CONCAT('ALL TYPES', ' (', SUM(SUBTABLE.X), ')') AS X FROM (SELECT  ");
		sb.append("COUNT(*) AS X FROM ACCELERATORS_W ACC) AS SUBTABLE  ");
		sb.append("UNION ALL  ");
		sb.append("SELECT TYPE, CONCAT(ACC.TYPE, ' (', COUNT(*), ')') AS X  ");
		sb.append("FROM ACCELERATORS_W ACC GROUP BY TYPE ORDER BY TYPE  ");
		
		List<Map<String, String>> docTypeList = new ArrayList<Map<String, String>>();
		try {
			docTypeList = jdbcTemplate.query(sb.toString(), new Object[] {}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("type", rs.getString("TYPE"));
						map.put("X", rs.getString("X"));
						return map;
					}
				});
		} catch (Exception e) {
			logger.error(e);
		}
		return docTypeList;
	}
	
	
	

}
