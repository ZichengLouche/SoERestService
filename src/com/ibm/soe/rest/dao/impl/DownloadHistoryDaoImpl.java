package com.ibm.soe.rest.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.dao.DownloadHistoryDao;
@Repository
public class DownloadHistoryDaoImpl implements DownloadHistoryDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messages;

	@Value("${DB_NAME_TECHLINE}")
	private String TECHLINE;
	

	@Value("${DB_NAME_ACCELERATOR_WRITE}")
	private String ACCELERATOR_WRITE;
	
	
	@Override
	public String insertDownloadHistory(final Map<String, String> param) throws Exception {
		// TODO Auto-generated method stub
		KeyHolder clientKeyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			String childSql = messages.getMessage("SQL_QUERY_RATING_STATUS", new String[] {ACCELERATOR_WRITE}, null);
			String mainSql = messages.getMessage("SQL_INSERT_DOWNLOAHIST", new String[] {ACCELERATOR_WRITE,childSql}, null);
			
			
			String idUser = param.get("idUser");
			String idEntity = param.get("idEntity");
			String entityType = param.get("entityType");
			String url = param.get("url");
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(mainSql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, idUser);
				ps.setString(2, idEntity);
                ps.setString(3, entityType);
                ps.setString(4, idUser);
				ps.setString(5, idEntity);
                ps.setString(6, entityType);
                ps.setString(7, idUser);
				ps.setString(8, idEntity);
                ps.setString(9, entityType);
                ps.setString(10, url);
                return ps;
			}
		}, clientKeyHolder);
		
		return String.valueOf(clientKeyHolder.getKey());
	}
	
	
	public boolean  updateHistoryHasRated(String idEntity,String idUser,String status,String entityType ) throws Exception 
	{
		String sql = messages.getMessage("SQL_UPDATE_HIS_HASRATE", new String[] {ACCELERATOR_WRITE}, null);
		jdbcTemplate.update(sql, new Object[] {status,idUser,idEntity,entityType});
		return true;
	}

	@Override
	public List<Map<String, String>> getDownloadHistory(Map<String, String> param) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
