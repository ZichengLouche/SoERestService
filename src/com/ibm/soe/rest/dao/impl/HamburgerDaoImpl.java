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

import com.ibm.soe.rest.dao.HamburgerDao;

@Repository
@Transactional
public class HamburgerDaoImpl implements HamburgerDao {
	
	private static Logger logger = Logger.getLogger(HamburgerDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messages;
	
	@Value("${DB_NAME_HAMBURGER}")
	private String DB_NAME_HAMBURGER;
	
	@Value("${TABLE_NAME_HELP}")
	private String TABLE_NAME_HELP;

	@Value("${TABLE_NAME_ABOUT}")
	private String TABLE_NAME_ABOUT;
	
	@Override
	public List<Map<String, String>> getHelpContent() throws Exception {
		List<Map<String, String>> helps = new ArrayList<Map<String, String>>();
		
		try {
			String sql = messages.getMessage("SQL_GET_HELP", new String[] {DB_NAME_HAMBURGER, TABLE_NAME_HELP}, null);
			helps = jdbcTemplate.query(sql, new Object[] {}, 
					new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> map = new HashMap<String, String>();
							map.put("idKey", rs.getString("idKey"));
							map.put("name", rs.getString("name"));
							map.put("title", rs.getString("title"));
							map.put("content", rs.getString("content"));
							return map;
						}
					});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		
		return helps;
	}

	@Override
	public Map<String, String> getAboutContent() {
		Map<String, String> about = null;
		
		String sql = messages.getMessage("SQL_GET_ABOUT", new String[]{DB_NAME_HAMBURGER, TABLE_NAME_ABOUT}, null);
		List<Map<String, String>> list = jdbcTemplate.query(sql, new Object[] {}, 
				new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("idKey", rs.getString("idKey"));
				map.put("contact", rs.getString("contact"));
				map.put("objectives", rs.getString("objectives"));
				map.put("capabilities", rs.getString("capabilities"));
				map.put("version", rs.getString("version"));
				map.put("latestUpdate", rs.getString("latestUpdate"));
				return map;
			}
		});
		if (list.isEmpty()) {
			about = new HashMap<String, String>();
		} else {
			about = list.get(0);
		}
		return about;
	}
	

}
