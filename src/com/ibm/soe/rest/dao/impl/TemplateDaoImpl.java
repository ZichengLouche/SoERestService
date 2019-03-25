package com.ibm.soe.rest.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.dao.TemplateDao;
@Repository
public class TemplateDaoImpl implements TemplateDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messages;
	
	@Value("${DB_NAME_METHOD_GUIDANCE}")
	private String METHOD_GUIDANCE;
	
	
	@Override
	public List<Map<String, String>> getIndustry(String industryId)
			throws Exception {
		// TODO Auto-generated method stub
		
		String mainSQl = messages.getMessage("SQL_QUERY_INDUSTRY", new String[]{METHOD_GUIDANCE}, null);
		List<Map<String, String>> industryList = jdbcTemplate.query(mainSQl, new Object[] {}, 
				new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("industryName", rs.getString("title"));
				map.put("industryId", rs.getString("idKey"));
				map.put("lastUpdate", rs.getString("lastUpdate"));
				map.put("hasChild", rs.getString("hasChild"));
				map.put("downloadURL", rs.getString("downloadURL"));
				map.put("mobileURL", rs.getString("mobileURL"));
				return map;
			}
		});
		
		return industryList;
	}

	@Override
	public List<Map<String, String>> getTemplate(String industryId)
			throws Exception {
		// TODO Auto-generated method stub
		 String mainSQl="";
		 String[] param=null;
		 
		if (industryId == null) {
			mainSQl = messages.getMessage("SQL_QUERY_TEMPLATE",
					new String[] { METHOD_GUIDANCE,""}, null);
		} else {
			param=new String[1];
			param[0]=industryId;
		    String whereSql = messages.getMessage("SQL_Query_TEMPLATE_WHERE",
					new String[] { , }, null);
			mainSQl = messages.getMessage("SQL_QUERY_TEMPLATE",
					new String[] { METHOD_GUIDANCE,whereSql}, null);
		}

		List<Map<String, String>> templateList = jdbcTemplate.query(mainSQl, param, 
				new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("industryName", rs.getString("industryName"));
				map.put("industryId", rs.getString("industryId"));
				map.put("level1Id", rs.getString("level1Id"));
				map.put("level1Seq", rs.getString("level1Seq"));
				map.put("level1Title", rs.getString("level1Title"));
				map.put("level1Content", rs.getString("level1Content"));
				map.put("level2Id", rs.getString("level2Id"));
				map.put("level2Seq", rs.getString("level2Seq"));
				map.put("level2Title", rs.getString("level2Title"));
				map.put("level2Parent", rs.getString("level2Parent"));
				map.put("level2Content", rs.getString("level2Content"));
				map.put("level3Id", rs.getString("level3Id"));
				map.put("level3Seq", rs.getString("level3Seq"));
				map.put("level3Title", rs.getString("level3Title"));
				map.put("level3Content", rs.getString("level3Content"));
				map.put("level3Parent", rs.getString("level3Parent"));
				map.put("lastUpdate", rs.getString("lastUpdate"));
				return map;
			}
		});
		
		
		return templateList;
	}

}
