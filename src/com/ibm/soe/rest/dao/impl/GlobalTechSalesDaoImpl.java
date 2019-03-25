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

import com.ibm.soe.rest.dao.GlobalTechSalesDao;

@Repository
public class GlobalTechSalesDaoImpl implements GlobalTechSalesDao {
	
	private static Logger logger = Logger.getLogger(GlobalTechSalesDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messages;

	@Value("${DB_NAME_GLOBALTECHSALES}")
	private String DB_NAME_GLOBALTECHSALES;

	public List<Map<String, String>> getAllWebNavigators() throws Exception {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		String sql = messages.getMessage("SQL_QueryAllWebNavigators", new String[] {DB_NAME_GLOBALTECHSALES}, null);
		try {
			result = jdbcTemplate.query(sql, new Object[] {}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("idKey", rs.getString("idKey"));
						map.put("Name", rs.getString("Name"));
						map.put("Description", rs.getString("Description"));
						map.put("primaryURL", rs.getString("primaryURL"));
						map.put("page", rs.getString("page"));
						map.put("type", rs.getString("type"));
						map.put("iconName", rs.getString("iconName"));
						map.put("icon", rs.getString("icon"));
						map.put("shortDesc", rs.getString("shortDesc"));
//						String name = rs.getString("Name");
//						if(name.equals("Global Technical Sales Leadership")){
//							map.put("class", "leadership");
//						} else if (name.equals("Technical Account Planning")){
//							map.put("class", "planning");
//						} else if (name.equals("Technical Collaboration Hub")){
//							map.put("class", "hub");
//						} else if (name.equals("Techlin")){
//							map.put("class", "techline");
//						} else if (name.equals("ThinkLab")){
//							map.put("class", "thinklab");
//						} else if (name.equals("Global Industry Solution Centers")){
//							map.put("class", "giscenter");
//						} else if (name.equals("Solution Seeker")){
//							map.put("class", "solution");
//						} else if (name.equals("Global Solution Design")){
//							map.put("class", "education");
//						} else if (name.equals("First of a Kind (FOAK)")){
//							map.put("class", "firstofkind");
//						} else if (name.equals("Global Technical Seller Education")){
//							map.put("class", "");
//						}
						
						return map;
					}
				});
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return result;
	}
}
