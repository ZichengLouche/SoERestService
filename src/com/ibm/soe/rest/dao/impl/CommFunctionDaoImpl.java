package com.ibm.soe.rest.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.dao.CommFunctionDao;

@Repository
public class CommFunctionDaoImpl implements CommFunctionDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messages;
	
	@Value("${DB_NAME_ACCELERATOR_WRITE}")
	private String DB_NAME_ACCELERATOR_WRITE;
	
	@Value("${DB_NAME_ACCELERATORS}")
	private String DB_NAME_ACCELERATORS;
	
	@Value("${TABLE_REDBOOKS}")
	private String TABLE_REDBOOKS;
	
	public Map<String, String> getMyProfile(String uid) {
		Map<String, String> map = new HashMap<String, String>();
		String sql = messages.getMessage("SQL_GET_MY_PROFILE_COMMON", new String[] {DB_NAME_ACCELERATOR_WRITE}, null);
		
		List<Map<String, String>> list = jdbcTemplate.query(sql, new Object[] {uid}, 
			new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("UID", rs.getString("uid"));
					dataMap.put("IDIND", rs.getString("idInd"));
					dataMap.put("P_GEO", rs.getString("p_GEO"));
					dataMap.put("IND_DEF", rs.getString("ind_def"));
					dataMap.put("GEO_DEF", rs.getString("geo_def"));
					dataMap.put("SALESCYCLE", rs.getString("salesCycle"));
					dataMap.put("JOBROLE", rs.getString("jobRole"));
					dataMap.put("PROFESSION", rs.getString("profession"));
					dataMap.put("QUESTION_DEF", rs.getString("question_def"));
					return dataMap;
				}
			});
		
		if (list != null && list.size() > 0) {
			map = list.get(0);
		}
		
		return map;
	}
	
	public List<Map<String, String>> getIndustries() {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		String sql = messages.getMessage("SQL_GET_INDUSTRIES_COMMON", new String[] {DB_NAME_ACCELERATORS}, null);
		
		result = jdbcTemplate.query(sql, new Object[] {}, 
			new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("IDKEY", rs.getString("idKey"));
					dataMap.put("NAME", rs.getString("Name"));
					return dataMap;
				}
			});
		
		return result;
	}
	
	public Map<String, String> insertProfile(Map<String, String> param) {
		String sql = messages.getMessage("SQL_INS_MY_PROFILE_COMMON", new String[] {DB_NAME_ACCELERATOR_WRITE}, null);
		
		jdbcTemplate.update(sql, new Object[] {
				param.get("uid"),
				param.get("idInd"),
				param.get("p_GEO"),
				param.get("ind_def"),
				param.get("geo_def"),
				param.get("salesCycle"),
				param.get("jobRole"),
				param.get("profession"),
				param.get("question_def")
		});
		
		return getMyProfile(param.get("uid"));
	}
	
	public Map<String, String> insertLoginHistory(Map<String, String> param) {
		Map<String, String> map = new HashMap<String, String>();
		
		String sql = messages.getMessage("SQL_INS_LOGIN_HISTORY_COMMON", new String[] {DB_NAME_ACCELERATOR_WRITE}, null);
		
		String cnt = jdbcTemplate.update(sql, new Object[] {
				param.get("uid"),
				param.get("platForm"),
				param.get("osVersion"),
				param.get("ssVersion")
		}) + "";
		
		map.put("insertCnt", cnt);
		
		return map;
	}
	
	public Map<String, String> updateProfile(Map<String, String> param) {
		String sql = messages.getMessage("SQL_UPD_MY_PROFILE_COMMON", new String[] {DB_NAME_ACCELERATOR_WRITE}, null);
		
		jdbcTemplate.update(sql, new Object[] {
				param.get("idInd"),
				param.get("p_GEO"),
				param.get("ind_def"),
				param.get("geo_def"),
				param.get("salesCycle"),
				param.get("jobRole"),
				param.get("profession"),
				param.get("question_def"),
				param.get("uid")
		});
		
		return getMyProfile(param.get("uid"));
	}
	
	// Andy 2016.3.30 17:20
	@Override
	public boolean autoUpdateRedbooks(String filePath) {
		String sql = 
					"LOAD DATA LOCAL INFILE '" + filePath + "' " + 
					"INTO TABLE " + DB_NAME_ACCELERATORS + "." + TABLE_REDBOOKS +
					" CHARACTER SET 'utf8' " +
	
					"LINES STARTING BY '<IBMRedbooksDoc>' TERMINATED BY '</IBMRedbooksDoc>' " +
					"(@tmp) " +
					"SET " +
					  "Category = ExtractValue(@tmp, '//Brand'), " +
					  "FormNumber = ExtractValue(@tmp, '//FormNumber'), " +
					  "DocType = ExtractValue(@tmp, '//DocType'), " +
					  "Security = ExtractValue(@tmp, '//Security'), " +
					  "ISBN = ExtractValue(@tmp, '//ISBN'), " +
					  "Title = ExtractValue(@tmp, '//Title'), " +
					  "Authors = ExtractValue(@tmp, '//Authors'), " +
					  "Abstract = ExtractValue(@tmp, '//Abstract'), " +
					  "TOC = ExtractValue(@tmp, '//TOC'), " +
					  "PubDate = if(ExtractValue(@tmp, '//PubDate')='',null,ExtractValue(@tmp, '//PubDate')), " +
					  "LastUpdate = if(ExtractValue(@tmp, '//LastUpdate')='',null,ExtractValue(@tmp, '//LastUpdate')), " +
					  "PageCount = if(ExtractValue(@tmp, '//PageCount')='',0,if(ExtractValue(@tmp, '//PageCount')=null,0,ExtractValue(@tmp, '//PageCount'))), " +
					  "GrowthPlays = ExtractValue(@tmp, '//GrowthPlays'), " +
					  "SubCat = ExtractValue(@tmp, '//ProductPrimary'), " +
					  "PDFURL = ExtractValue(@tmp, '//PDFURL'), " +
					  "PDFSize = if(ExtractValue(@tmp, '//PDFSize')='',0,ExtractValue(@tmp, '//PDFSize')), " +
					  "URL = ExtractValue(@tmp, '//URL'), " +
					  "ISBN13 = ExtractValue(@tmp, '//ISBN13') " ;
		
		jdbcTemplate.execute(sql);   
		return true;
	}
	
	@Override
	public boolean truncateTableData(String table) {
		String sql = "truncate " + DB_NAME_ACCELERATORS + "." + table;
		jdbcTemplate.execute(sql);
		return true;
	}
	
	// Andy 2016.4.5 18:38
	@Override
	public boolean autoBatchUpdateRedbooks(final List<Element> childElements) {
		String sql = "INSERT INTO " + DB_NAME_ACCELERATORS + "." + TABLE_REDBOOKS + " (Category, FormNumber, DocType, Security, ISBN, Title, Authors, Abstract, TOC, " +
				 					 "PubDate, LastUpdate, PageCount, GrowthPlays, SubCat, PDFURL, PDFSize, URL, ISBN13) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter(){
					public void setValues(PreparedStatement ps,int i) throws SQLException {
						 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						 Element childElement = childElements.get(i);
						 String brand = childElement.elementText("Brand");
						 String formNumber = childElement.elementText("FormNumber");
						 String docType = childElement.elementText("DocType");
						 String security = childElement.elementText("Security");
						 String isbn = childElement.elementText("ISBN");
						 String title = childElement.elementText("Title");
						 String authors = childElement.elementText("Authors");
						 String abstrac = childElement.elementText("Abstract");
						 String toc = childElement.elementText("TOC");
						 String pubDate = childElement.elementText("PubDate");
						 String lastUpdate = childElement.elementText("LastUpdate");
						 String pageCount = childElement.elementText("PageCount");
						 String growthPlays = childElement.elementText("GrowthPlays");
						 String productPrimary = childElement.elementText("ProductPrimary");
						 String pdfURL = childElement.elementText("PDFURL");
						 String pdfSize = childElement.elementText("PDFSize");
						 String url = childElement.elementText("URL");
						 String isbn13 = childElement.elementText("ISBN13");
			             
			             ps.setString(1, brand);
			             ps.setString(2, formNumber);
			             ps.setString(3, docType);
			             ps.setString(4, security);
			             ps.setString(5, isbn);
			             ps.setString(6, title);
			             ps.setString(7, authors);
			             ps.setString(8, abstrac);
			             ps.setString(9, toc);
			             try {
							ps.setDate(10, new java.sql.Date(df.parse(pubDate).getTime()) );
						} catch (ParseException e) {
							ps.setDate(10, null);
							e.printStackTrace();
						}
			             ps.setDate(11, lastUpdate == null || "".equals(lastUpdate) ? null : java.sql.Date.valueOf(lastUpdate));
			             ps.setInt(12, pageCount == null || "".equals(pageCount) ? 0 : Integer.parseInt(pageCount));
			             ps.setString(13, growthPlays);
			             ps.setString(14, productPrimary);
			             ps.setString(15, pdfURL);
			             ps.setInt(16, pdfSize == null || "".equals(pdfSize) ? 0 : Integer.parseInt(pdfSize)); 
			             ps.setString(17, url);
			             ps.setString(18, isbn13);
		            }
					
		            public int getBatchSize() {
		            	return childElements.size();
		            }
		     	}
		);	
		
		return true;
	}
	
	// Andy 2016.5.27 16:49
	@Override
	public boolean updateGISCAsset(Integer idIsn, Integer idAsset, String assetName) {
		String sql = "INSERT INTO " + DB_NAME_ACCELERATORS + ".giscasset" + " (idISN, id_asset, asset_name) VALUES(?, ?, ?)";
		
		int result = jdbcTemplate.update(sql, new Object[] { idIsn, idAsset, assetName });
		return result > 0 ? true : false;
	}
	
	@Override
	public boolean batchUpdateGISCAsset(final List<Map<String,Object>> giscAssetList)  {
		String sql = "INSERT INTO " + DB_NAME_ACCELERATORS + ".giscasset" + " (idISN, id_asset, asset_name) VALUES(?, ?, ?)";
			
		int[] result  = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter(){
			public void setValues(PreparedStatement ps,int i) throws SQLException {
				Map<String,Object> newGiscAssetMap = giscAssetList.get(i);
	             
	            ps.setInt(1, (Integer) newGiscAssetMap.get("idISN"));
	            ps.setInt(2, (Integer) newGiscAssetMap.get("idAsset"));
	            ps.setString(3, (String) newGiscAssetMap.get("assetName"));
            }
			
            public int getBatchSize() {
            	return giscAssetList.size();
            }
     	});	
		
		return true;
	}

}
