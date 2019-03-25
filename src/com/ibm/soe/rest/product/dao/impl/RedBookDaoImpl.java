package com.ibm.soe.rest.product.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.product.dao.RedBookDao;

@Repository
public class RedBookDaoImpl implements RedBookDao {

	private static Logger logger = Logger.getLogger(RedBookDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MessageSource messages;

	@Value("${DB_NAME_REDBOOKS}")
	private String REDBOOKS;

	@Override
	public long saveRedBook(final Map<String, String> redBook) throws Exception {
		KeyHolder clientKeyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			String sql = messages.getMessage("SQL_INSERT_REDBOOKS", new String[] {REDBOOKS}, null);
			
			String pubDate = redBook.get("PubDate");
			String lastUpdate = redBook.get("LastUpdate");
			String pageCount = redBook.get("PageCount");
			String pdfSize = redBook.get("PDFSize");
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, redBook.get("Brand"));//table field name: Category
				ps.setString(2, redBook.get("FormNumber"));
                ps.setString(3, redBook.get("DocType"));
                ps.setString(4, redBook.get("Security"));
                ps.setString(5, redBook.get("ISBN"));
                ps.setString(6, redBook.get("Title"));
                ps.setString(7, redBook.get("Authors"));
                ps.setString(8, redBook.get("Abstract"));
                ps.setString(9, redBook.get("TOC"));
                ps.setDate(10, (pubDate == null || "".equals(pubDate)) ? null : Date.valueOf(pubDate));
                ps.setDate(11, lastUpdate == null || "".equals(lastUpdate) ? null : Date.valueOf(lastUpdate));
                ps.setInt(12, pageCount == null || "".equals(pageCount) ? 0 : Integer.parseInt(pageCount));
                ps.setString(13, redBook.get("GrowthPlays"));
                ps.setString(14, redBook.get("SubCat"));
                ps.setString(15, redBook.get("PDFURL"));
                ps.setLong(16, pdfSize == null || "".equals(pdfSize) ? 0 : Long.parseLong(pdfSize));
                ps.setString(17, redBook.get("URL"));
                ps.setString(18, redBook.get("ISBN13"));
                return ps;
			}
		}, clientKeyHolder);
		logger.info("INSERT_REDBOOKS done.");
		
		return clientKeyHolder.getKey().longValue();
		
	}
	
	
	public void deleteRedBooks() throws Exception {
		String sql = messages.getMessage("SQL_DELETE_REDBOOKS", new String[] {REDBOOKS}, null);
		jdbcTemplate.update(sql);
		logger.info("DELETE_REDBOOKS done.");
	}

	
	

}
