package com.ibm.soe.rest.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ibm.soe.rest.dao.ProductionInformationDao;
import com.ibm.soe.rest.util.Const;

@Repository
public class ProductionInformationDaoImpl implements ProductionInformationDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplateT;

	@Autowired
	private MessageSource messages;
	
	@Value("${DB_NAME_ACCELERATOR_WRITE}")
	private String ACCELERATOR_WRITE;
	
	@Value("${DB_NAME_REDBOOKS}")
	private String DB_NAME_REDBOOKS;
	
	@Value("${DB_NAME_TECHLINE}")
	private String DB_NAME_TECHLINE;
	
	@Value("${DB_NAME_FAQ}")
	private String DB_NAME_FAQ;
	
	public List<Map<String, String>> queryProductionTeclineResults(String brand,String[] growthPlays,String source,String date,String keyword, String idUser) throws Exception {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, List<String>> map = getConditionProductionResult(brand,growthPlays,source,date,keyword);
		List<String> sqllist=(List<String>) map.get("sql");
		List<String> conditionParamlist=(List<String>) map.get("param");
		
		String wheresql="";
		if(sqllist.size()>0) {
			wheresql=getConditionSQL(sqllist);
		}
		
		String baseData = messages.getMessage("SQL_QUERY_PRODUCTION_BASIC_TECHLINE_DATE", new String[]{DB_NAME_FAQ}, null);
		String review = messages.getMessage("SQL_QUERY_PRODUCTION_TECHLINE_LEFT_REVIEW", new String[]{DB_NAME_FAQ}, null);
		String mainsql = messages.getMessage("SQL_QUERY_PRODUCTION_RESULTS", new String[]{baseData,review}, null);
		
		List<Object> paramlist=new ArrayList<Object>();
		paramlist.add(idUser);
		paramlist.add(idUser);
		paramlist.addAll(conditionParamlist);
		StringBuffer sql=new StringBuffer();
		sql.append(mainsql);
		sql.append(wheresql);
		
		result = jdbcTemplateT.query(sql.toString(), paramlist.toArray(), new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("idEntity", rs.getString("idEntity"));
				map.put("title", rs.getString("name"));
				map.put("lastUpdate", rs.getString("lastUpdate"));
				map.put("description", rs.getString("description"));
				map.put("description1", rs.getString("description1"));
				map.put("source", rs.getString("source"));
				map.put("entityType", rs.getString("source"));
				map.put("brand", rs.getString("brand"));
				map.put("publishType", rs.getString("DocType"));
				map.put("confidential", rs.getString("confidential"));
				map.put("star", rs.getString("satrs"));
				map.put("reviewCount", rs.getString("resCount")==null?"0":rs.getString("resCount"));
				map.put("hasReviewed", rs.getString("isReviewed"));
				map.put("useful", rs.getString("isUseful"));
				map.put("downloadURL", rs.getString("url"));
				map.put("urlFlag", rs.getString("pdfSize"));
				map.put("serialNumber", rs.getString("FormNumber"));
				map.put("cloud", rs.getString("cloud"));
				map.put("analytics", rs.getString("analytics"));
				map.put("mobile", rs.getString("mobile"));
				map.put("social", rs.getString("social"));
				map.put("security", rs.getString("security"));
				return map;
			}
		});
		return result;
	}
	
	public List<Map<String, String>> queryProductionRedbookResults(String brand,String[] growthPlays,String source,String date,String keyword, String idUser) throws Exception {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, List<String>> map = getConditionProductionResult(brand,growthPlays,source,date,keyword);
		List<String> sqllist=(List<String>) map.get("sql");
		List<String> conditionParamlist=(List<String>) map.get("param");
		
		String wheresql="";
		if(sqllist.size()>0) {
			wheresql=getConditionSQL(sqllist);
		}
		
		String baseData = messages.getMessage("SQL_QUERY_PRODUCTION_BASIC_REDBOOK_DATE", new String[]{DB_NAME_REDBOOKS,ACCELERATOR_WRITE}, null);
		String review = messages.getMessage("SQL_QUERY_PRODUCTION_LEFT_REVIEW", new String[]{ACCELERATOR_WRITE}, null);
		String mainsql = messages.getMessage("SQL_QUERY_PRODUCTION_RESULTS", new String[]{baseData,review}, null);
		
		List<Object> paramlist=new ArrayList<Object>();
		paramlist.add(idUser);
		paramlist.add(idUser);
		paramlist.addAll(conditionParamlist);
		StringBuffer sql=new StringBuffer();
		sql.append(mainsql);
		sql.append(wheresql);
		
		result = jdbcTemplate.query(sql.toString(), paramlist.toArray(), new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("idEntity", rs.getString("idEntity"));
				map.put("title", rs.getString("name"));
				map.put("lastUpdate", rs.getString("lastUpdate"));
				map.put("description", rs.getString("description"));
				map.put("description1", rs.getString("description1"));
				map.put("source", rs.getString("source"));
				map.put("entityType", rs.getString("source"));
				map.put("brand", rs.getString("brand"));
				map.put("publishType", rs.getString("DocType"));
				map.put("confidential", rs.getString("confidential"));
				map.put("star", rs.getString("satrs"));
				map.put("reviewCount", rs.getString("resCount")==null?"0":rs.getString("resCount"));
				map.put("hasReviewed", rs.getString("isReviewed"));
				map.put("useful", rs.getString("isUseful"));
				map.put("downloadURL", rs.getString("url"));
				map.put("urlFlag", rs.getString("pdfSize"));
				map.put("serialNumber", rs.getString("FormNumber"));
				map.put("cloud", rs.getString("cloud"));
				map.put("analytics", rs.getString("analytics"));
				map.put("mobile", rs.getString("mobile"));
				map.put("social", rs.getString("social"));
				map.put("security", rs.getString("security"));
				return map;
			}
		});
		return result;
	}

	public List<Map<String,String>> queryTeclineBrandFilterCount(String brand,String[] growthPlays,String source,String date,String keyword) throws Exception {
		String wheresql="";
		Map<String, List<String>> map = getConditionFilterResult(null,growthPlays,source,date,keyword);
		List<String> sqllist=(List<String>) map.get("sql");
		List<String> paramlist=(List<String>) map.get("param");
		
		if(sqllist.size()>0) {
			wheresql=getConditionSQL(sqllist);
		}
		
		String[] param=null;
		if(paramlist.size()>0) {
			param=new String[paramlist.size()];
			paramlist.toArray(param);
		}
		String techline = messages.getMessage("SQL_FILTER_PRODUCTION_BASIC_TECHLINE_DATA", new String[]{DB_NAME_FAQ}, null);
		
		String mainsql = messages.getMessage("SQL_QUERY_BRAND_TECLINE_fILTER", new String[]{techline,wheresql,DB_NAME_FAQ}, null);
		List<Map<String, String>> result = jdbcTemplateT.query(mainsql, param, new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("brand", rs.getString("brand"));
				map.put("name", rs.getString("name"));
				map.put("num", rs.getString("num"));
				return map;
			}
		});
		return result;
	}
	
	public List<Map<String,String>> queryRedbookBrandFilterCount(String brand,String[] growthPlays,String source,String date,String keyword) throws Exception {
		String wheresql="";
		Map<String, List<String>> map = getConditionFilterResult(null,growthPlays,source,date,keyword);
		List<String> sqllist=(List<String>) map.get("sql");
		List<String> paramlist=(List<String>) map.get("param");
		
		if(sqllist.size()>0) {
			wheresql=getConditionSQL(sqllist);
		}
		
		String[] param=null;
		if(paramlist.size()>0) {
			param=new String[paramlist.size()];
			paramlist.toArray(param);
		}
		String redbook = messages.getMessage("SQL_FILTER_PRODUCTION_BASIC_REDBOOK_DATA", new String[]{DB_NAME_REDBOOKS,ACCELERATOR_WRITE}, null);
		
		String mainsql = messages.getMessage("SQL_QUERY_BRAND_REDBOOK_fILTER", new String[]{redbook,wheresql,ACCELERATOR_WRITE}, null);
		List<Map<String, String>> result = jdbcTemplate.query(mainsql, param, new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("brand", rs.getString("brand"));
				map.put("name", rs.getString("name"));
				map.put("num", rs.getString("num"));
				return map;
			}
		});
		return result;
	}

	public List<Map<String,String>> queryTeclineSourceFilterCount(String brand,String[] growthPlays,String source,String date,String keyword) throws Exception {
		String wheresql="";
		Map<String, List<String>> map = getConditionFilterResult(brand,growthPlays,null,date,keyword);
		List<String> sqllist=(List<String>) map.get("sql");
		List<String> paramlist=(List<String>) map.get("param");
		
		if(sqllist.size()>0)
		{
			wheresql=getConditionSQL(sqllist);
		}
		
		String[] param=null;
		if(paramlist.size()>0)
		{
			param=new String[paramlist.size()];
			paramlist.toArray(param);
		}
				
		String techline = messages.getMessage("SQL_FILTER_PRODUCTION_BASIC_TECHLINE_DATA_SHORT", new String[]{DB_NAME_FAQ}, null);
		
		String mainsql = messages.getMessage("SQL_QUERY_SOURCE_TECLINE_fILTER", new String[]{techline,wheresql}, null);
		List<Map<String, String>> result = jdbcTemplateT.query(mainsql, param, new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("source", rs.getString("source"));
				map.put("name", "Techline");
				map.put("num", rs.getString("num"));
				return map;
			}
		});
		return result;
	}
	
	public List<Map<String,String>> queryRedbookSourceFilterCount(String brand,String[] growthPlays,String source,String date,String keyword) throws Exception {
		String wheresql="";
		Map<String, List<String>> map = getConditionFilterResult(brand,growthPlays,null,date,keyword);
		List<String> sqllist=(List<String>) map.get("sql");
		List<String> paramlist=(List<String>) map.get("param");
		
		if(sqllist.size()>0)
		{
			wheresql=getConditionSQL(sqllist);
		}
		
		String[] param=null;
		if(paramlist.size()>0)
		{
			param=new String[paramlist.size()];
			paramlist.toArray(param);
		}
				
		String redbook = messages.getMessage("SQL_FILTER_PRODUCTION_BASIC_REDBOOK_DATA", new String[]{DB_NAME_REDBOOKS,ACCELERATOR_WRITE}, null);
		
		String mainsql = messages.getMessage("SQL_QUERY_SOURCE_REDBOOK_fILTER", new String[]{redbook,wheresql,ACCELERATOR_WRITE}, null);
		List<Map<String, String>> result = jdbcTemplate.query(mainsql, param, new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("source", rs.getString("source"));
				map.put("name", "Redbooks");
				map.put("num", rs.getString("num"));
				return map;
			}
		});
		return result;
	}

	public List<Map<String,String>> queryTeclineDateFilterCount(String brand,String[] growthPlays,String source,String date,String keyword) throws Exception
	{
		String wheresql="";
		Map<String, List<String>> map = getConditionFilterResult(brand,growthPlays,source,null,keyword);
		List<String> sqllist=(List<String>) map.get("sql");
		List<String> paramlist=(List<String>) map.get("param");
		
		if(sqllist.size()>0)
		{
			wheresql=getConditionSQL(sqllist);
		}
		
		String[] param=null;
		if(paramlist.size()>0)
		{
			param=new String[paramlist.size()];
			paramlist.toArray(param);
		}
		
		String techline = messages.getMessage("SQL_FILTER_PRODUCTION_BASIC_TECHLINE_DATA", new String[]{DB_NAME_FAQ}, null);
		
		String mainsql = messages.getMessage("SQL_QUERY_DATE_TECLINE_fILTER", new String[]{techline,wheresql}, null);
		List<Map<String, String>> result = jdbcTemplateT.query(mainsql, param, new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("1", rs.getString("MONTH1")==null ? "0":rs.getString("MONTH1"));
				map.put("2", rs.getString("MONTH2")==null ?"0":rs.getString("MONTH2"));
				map.put("3", rs.getString("MONTH3")==null? "0" :rs.getString("MONTH3"));
				map.put("4", rs.getString("MONTH4")==null?"0":rs.getString("MONTH4"));
				map.put("5", rs.getString("MONTH5")==null?"0":rs.getString("MONTH5"));
				map.put("6", rs.getString("MONTH6")==null?"0":rs.getString("MONTH6"));
				return map;
			}
		});
		return result;
	}
	
	public List<Map<String,String>> queryRedbookDateFilterCount(String brand,String[] growthPlays,String source,String date,String keyword) throws Exception
	{
		String wheresql="";
		Map<String, List<String>> map = getConditionFilterResult(brand,growthPlays,source,null,keyword);
		List<String> sqllist=(List<String>) map.get("sql");
		List<String> paramlist=(List<String>) map.get("param");
		
		if(sqllist.size()>0)
		{
			wheresql=getConditionSQL(sqllist);
		}
		
		String[] param=null;
		if(paramlist.size()>0)
		{
			param=new String[paramlist.size()];
			paramlist.toArray(param);
		}
		
		String redbook = messages.getMessage("SQL_FILTER_PRODUCTION_BASIC_REDBOOK_DATA", new String[]{DB_NAME_REDBOOKS,ACCELERATOR_WRITE}, null);
		
		String mainsql = messages.getMessage("SQL_QUERY_DATE_REDBOOK_fILTER", new String[]{redbook,wheresql}, null);
		List<Map<String, String>> result = jdbcTemplate.query(mainsql, param, new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("1", rs.getString("MONTH1")==null ? "0":rs.getString("MONTH1"));
				map.put("2", rs.getString("MONTH2")==null ?"0":rs.getString("MONTH2"));
				map.put("3", rs.getString("MONTH3")==null? "0" :rs.getString("MONTH3"));
				map.put("4", rs.getString("MONTH4")==null?"0":rs.getString("MONTH4"));
				map.put("5", rs.getString("MONTH5")==null?"0":rs.getString("MONTH5"));
				map.put("6", rs.getString("MONTH6")==null?"0":rs.getString("MONTH6"));
				return map;
			}
		});
		return result;
	}
	
 
	   private String getConditionSQL(List<String> paramSql) {
		   StringBuffer sql=new StringBuffer();
		  
		   if(null!=paramSql&& paramSql.size()>0) {
			   sql.append(" WHERE ");
			   for(String item:paramSql) {
				   sql.append(item);
				   sql.append(" AND ");
			   }
			   sql.append(" 1=1 ");
		   }
		   
		   return sql.toString();
	   }
	   
	   private Map<String,List<String>> getConditionFilterResult(String brand,String[] growthPlays,String source,String date,String keyword) {
			List<String> sqllist=new ArrayList<String>();
			List<String> paramlist=new ArrayList<String>();
		      if(keyword!=null) {
		    	  String namesql = messages.getMessage("SQL_NAME_CONDITION", new String[]{}, null);
		    	  sqllist.add(namesql) ;
		    	  paramlist.add(keyword);
		    	  paramlist.add(keyword);
		    	  paramlist.add(keyword);
		    	  paramlist.add(keyword);
		      }
		      
		      if(growthPlays!=null) {
		    	  StringBuffer tempsql=new StringBuffer();
		    	  for(int i=0;i<growthPlays.length;i++) {
		    		  tempsql.append("?,");
		    		  paramlist.add(growthPlays[i]);
		    	  }
		    	  StringBuffer ss=tempsql.deleteCharAt(tempsql.length()-1);
		          String growthPlayssqltemp=ss.toString();
		    	  String growthPlayssql = messages.getMessage("SQL_GROWTHPLAYS_CONDITION", new String[]{growthPlayssqltemp}, null);
		    	  sqllist.add(growthPlayssql) ;	
		      }
		      
		      if(brand!=null) {
		    	  String brandsql = messages.getMessage("SQL_BRAND_CONDITION", new String[]{}, null);
		    	  sqllist.add(brandsql) ;
		    	  paramlist.add(brand);
		      }
		      
		      if(date!=null) {
		    	  String datesql=null;
		    	  if(Const.lastMonth.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 1 MONTH) <= date(lastUpdate) ";
		    		  
		    	  } else if(Const.last3Month.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 3 MONTH) <= date(lastUpdate) ";
		    		  
		    	  } else if(Const.last6Month.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 6 MONTH) <= date(lastUpdate) ";
		    		  
		    	  } else if(Const.lastYear.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 1 YEAR) <= date(lastUpdate) ";
		    		  
		    	  } else if(Const.last5Year.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 5 YEAR) <= date(lastUpdate) ";
		    		  
		    	  } else if(Const.greater5Year.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 5 YEAR) > date(lastUpdate) ";
		    	  }
		    	  sqllist.add(datesql) ;
		      }
		      
		      if(source!=null) {
		    	  String sourcesql = messages.getMessage("SQL_SOURCE_CONDITION", new String[]{}, null);
		    	  sqllist.add(sourcesql) ;
		    	  paramlist.add(source);
		      }
		 
			Map<String,List<String>> result=new HashMap<String,List<String>>();
			result.put("sql", sqllist);
			result.put("param", paramlist);
			
			return result;
		}
	   
	   private Map<String,List<String>> getConditionProductionResult(String brand,String[] growthPlays,String source,String date,String keyword) {
			List<String> sqllist=new ArrayList<String>();
			List<String> paramlist=new ArrayList<String>();
		      if(keyword!=null) {
		    	  String namesql = messages.getMessage("SQL_NAME_CONDITION", new String[]{}, null);
		    	  sqllist.add(namesql) ;
		    	  paramlist.add(keyword);
		    	  paramlist.add(keyword);
		    	  paramlist.add(keyword);
		    	  paramlist.add(keyword);
		      }
		      
		      if(growthPlays!=null) {
		    	  StringBuffer tempsql=new StringBuffer();
		    	  for(int i=0;i<growthPlays.length;i++) {
		    		  tempsql.append("?,");
		    		  paramlist.add(growthPlays[i]);
		    	  }
		    	  StringBuffer ss=tempsql.deleteCharAt(tempsql.length()-1);
		          String growthPlayssqltemp=ss.toString();
		    	  String growthPlayssql = messages.getMessage("SQL_GROWTHPLAYS_CONDITION", new String[]{growthPlayssqltemp}, null);
		    	  sqllist.add(growthPlayssql) ;	    	 
		      }
		      
		      if(brand!=null) {
		    	  String brandsql = messages.getMessage("SQL_RESULTS_BRAND_CONDITION", new String[]{}, null);
		    	  sqllist.add(brandsql) ;
		    	  paramlist.add(brand);
		      }
		      
		      if(date!=null) {
		    	  String datesql=null;
		    	  if(Const.lastMonth.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 1 MONTH) <= date(lastUpdate) ";
		    		  
		    	  } else if(Const.last3Month.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 3 MONTH) <= date(lastUpdate) ";
		    		  
		    	  } else if(Const.last6Month.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 6 MONTH) <= date(lastUpdate) ";
		    		  
		    	  } else if(Const.lastYear.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 1 YEAR) <= date(lastUpdate) ";
		    		  
		    	  } else if(Const.last5Year.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 5 YEAR) <= date(lastUpdate) ";
		    		  
		    	  } else if(Const.greater5Year.equals(date)) {
		    		  datesql=" DATE_SUB(NOW(),INTERVAL 5 YEAR) > date(lastUpdate) ";
		    	  }
		    	  sqllist.add(datesql) ;
		      }
		      
		      if(source!=null) {
		    	  String sourcesql = messages.getMessage("SQL_RESULTS_SOURCE_CONDITION", new String[]{}, null);
		    	  sqllist.add(sourcesql) ;
		    	  paramlist.add(source);
		      }
		 
			Map<String,List<String>> result=new HashMap<String,List<String>>();
			result.put("sql", sqllist);
			result.put("param", paramlist);
			
			return result;
		}
		
		public void insertuseful(String idEntity,String entityType,String idUser,String camss,String keyword,String brand,String source,String datelastUpdate) throws Exception {
			// Andy 2016.9.19 18:52
			Object[] param = new String[]{idEntity, entityType, idUser, camss, keyword, brand, source, datelastUpdate};
			
			if (entityType.equals("1")) {
				String sql = "INSERT INTO " + ACCELERATOR_WRITE + ".usefuldocs(idEntity, entityType, idUser, CAMSS, KeyWords, Brand, Source, DateLastUpdated) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
				jdbcTemplate.update(sql, param);
				
			} else if (entityType.equals("2")) {
				String sql = "INSERT INTO " + DB_NAME_FAQ + ".usefuldocs(idEntity, entityType, idUser, CAMSS, KeyWords, Brand, Source, DateLastUpdated) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
				jdbcTemplateT.update(sql, param);
			}
		}
		
		public void deleteUseful(String idEntity,String entityType,String idUser)throws Exception {
			Object[] param = new Object[] {idEntity, entityType,idUser};
			if (entityType.equals("1")) {
				String sql = "DELETE FROM " + ACCELERATOR_WRITE + ".usefuldocs WHERE idEntity=? AND entityType=? AND idUser=? ";
				jdbcTemplate.update(sql, param);
				
			} else if (entityType.equals("2")) {
				String sql = "DELETE FROM " + DB_NAME_FAQ + ".usefuldocs WHERE idEntity=? AND entityType=? AND idUser=? ";
				jdbcTemplateT.update(sql, param);
			}
		}
}
