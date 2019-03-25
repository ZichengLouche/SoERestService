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

import com.ibm.soe.rest.dao.ReviewDao;
@Repository
public class ReviewDaoImpl implements ReviewDao{

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
	
	@Override
	public Map<String, String> queryNotRatedCount(String idUser,
			String entityType) throws Exception {
		// TODO Auto-generated method stub
	    Map<String, String> map = null;
		String mainsql = messages.getMessage("SQL_QUERY_NOTRATEDCOUNT", new String[]{}, null);
		String formRedbook = messages.getMessage("SQL_QUERY_NOTRATED_FROMREDBOOKS", new String[]{DB_NAME_REDBOOKS}, null);
		String formHis = messages.getMessage("SQL_QUERY_NOTRATED_FROMHIS", new String[]{ACCELERATOR_WRITE}, null);
		String formONHIS = messages.getMessage("SQL_QUERY_NOTRATED_ONHIS", new String[]{}, null);
		StringBuffer sql=new StringBuffer();
		sql.append(mainsql);
		sql.append(formRedbook);
		sql.append(formHis);
		sql.append(formONHIS);
		
		List<Map<String, String>> result = jdbcTemplate.query(sql.toString(), new Object[] {idUser,entityType}, 
				new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("notRatedCount", rs.getString("notRatedCount"));
				map.put("shouldRatedCount15", rs.getString("shouldRatedCount15"));
				map.put("shouldRatedCount30", rs.getString("shouldRatedCount30"));
				return map;
			}
		});
		if (result.isEmpty()) {
			map = new HashMap<String, String>();
		} else {
			map = result.get(0);
		}
		return map;
	}

	@Override
	public void updateAlertStatus30(String idUser, String entityType)
			throws Exception {
		// TODO Auto-generated method stub
		String sql = messages.getMessage("SQL_UPDATE_ALERTSTATUS_30", new String[] {ACCELERATOR_WRITE}, null);
		
		jdbcTemplate.update(sql, new Object[] {idUser,entityType});
		
	}

	@Override
	public void updateAlertStatus15(String idUser, String entityType)
			throws Exception {
		// TODO Auto-generated method stub
		String sql = messages.getMessage("SQL_UPDATE_ALERTSTATUS_15", new String[] {ACCELERATOR_WRITE}, null);
		jdbcTemplate.update(sql, new Object[] {idUser,entityType});
	
	}
	
	
	@Override
	public List<Map<String, String>> queryNotRatedRecords(String idUser,
			String entityType) throws Exception {
		// TODO Auto-generated method stub

		String sqlQuery = messages.getMessage("SQL_QUERY_RATED_RECORDS", new String[]{ACCELERATOR_WRITE}, null);
		String formRedbook = messages.getMessage("SQL_QUERY_NOTRATED_FROMREDBOOKS", new String[]{DB_NAME_REDBOOKS}, null);
		String formHis = messages.getMessage("SQL_QUERY_NOTRATED_FROMHIS", new String[]{ACCELERATOR_WRITE}, null);
		String formONHIS = messages.getMessage("SQL_QUERY_NOTRATED_ONHIS", new String[]{}, null);
		String formWhere = messages.getMessage("SQL_QUERY_RATED_RECORDS_WHERE", new String[]{}, null);
		StringBuffer sql=new StringBuffer();
		sql.append(sqlQuery);
		sql.append(formRedbook);
		sql.append(formHis);
		sql.append(formONHIS);
		sql.append(formWhere);
		List<Map<String, String>> resultlist = jdbcTemplate.query(sql.toString(), new Object[] {idUser,entityType}, 
				new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("idKey", rs.getString("idKey"));
				map.put("downloadTime", rs.getString("downloadTime"));
				map.put("idEntity", rs.getString("idAcc"));
				map.put("entityType", rs.getString("entityType"));
				map.put("title", rs.getString("name"));
				map.put("Abstract", rs.getString("Abstract"));
				map.put("TOC", rs.getString("TOC"));
				map.put("brand", rs.getString("brand"));
				map.put("lastUpdate", rs.getString("lastUpdate"));
				map.put("DocType", rs.getString("DocType"));
				map.put("Security", rs.getString("Security"));
				map.put("GrowthPlays", rs.getString("GrowthPlays"));
				map.put("dlURL", rs.getString("dlURL"));
				map.put("URL", rs.getString("URL"));
				map.put("PDFSize", rs.getString("PDFSize"));
				map.put("reviewCount", rs.getString("reviewCount"));
				map.put("avgStars", rs.getString("avgStars"));
				return map;
			}
		});
		if(resultlist==null)
		{
			resultlist=new ArrayList<Map<String, String>>();
		}
		return resultlist;
	}
	
	
	public void insertView(Map<String,String> param)	throws Exception
	{
		String entityType=param.get("entityType");
		String stars=param.get("stars");
		String category=(param.get("category")==null?"":param.get("category"));
		String comment=param.get("comment");
		String idEntity=param.get("idEntity");
		String idUser=param.get("idUser");
		if (entityType.equals("1")) {
			String sql = messages.getMessage("SQL_INSERT_REVIEW_REDBOOK", new String[] {ACCELERATOR_WRITE}, null);
			jdbcTemplate.update(sql, new Object[] {idEntity,entityType,category,idUser,stars,comment});
		} else if (entityType.equals("2")) {
			String sql = messages.getMessage("SQL_INSERT_REVIEW_TECLINE", new String[] {DB_NAME_FAQ}, null);
			jdbcTemplateT.update(sql, new Object[] {idEntity,entityType,category,idUser,stars,comment});
		}
	}

	
	public Map<String,String> queryReviewsCount(String idEntity,String entityType)throws Exception
	{
		 Map<String, String> map = null;
		 List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		 if (entityType.equals("2")) {
			String mainsql = messages.getMessage("SQL_QUERY_REVIEWS_COUNT_TECLINE", new String[]{DB_NAME_FAQ}, null);
			result = jdbcTemplateT.query(mainsql, new Object[] {idEntity,entityType}, 
					new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("idEntity", rs.getString("idAcc"));
					map.put("entityType",  rs.getString("entityType"));
					map.put("reviewCount", rs.getString("reviewCount"));
					map.put("avgStars", rs.getString("avgStars"));
					return map;
				}
			});
		 } else {
			String mainsql = messages.getMessage("SQL_QUERY_REVIEWS_COUNT_REDBOOKS", new String[]{ACCELERATOR_WRITE}, null);
			result = jdbcTemplate.query(mainsql, new Object[] {idEntity,entityType}, 
					new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("idEntity", rs.getString("idAcc"));
					map.put("entityType",  rs.getString("entityType"));
					map.put("reviewCount", rs.getString("reviewCount"));
					map.put("avgStars", rs.getString("avgStars"));
					return map;
				}
			});
		 }
		if (result!=null&&result.isEmpty()) {
			map = new HashMap<String, String>();
		} else {
			map = result.get(0);
		}
		return map;
	}
	
	public Map<String, String> queryEntityRedbookDetail(String idAcc)
			throws Exception {
		
		Map<String, String> acceleratorDetail = null;
		
		String sqlQueryAcceleratorDetail = messages.getMessage("SQL_QueryRedbookDetail", new String[]{DB_NAME_REDBOOKS}, null);
		List<Map<String, String>> acceleratorlist = jdbcTemplate.query(sqlQueryAcceleratorDetail, new Object[] {idAcc}, 
				new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("idEntity", rs.getString("idEntity"));
				map.put("title", rs.getString("name"));
				map.put("Abstract", rs.getString("Abstract"));
				map.put("TOC", rs.getString("TOC"));
				map.put("brand", rs.getString("brand"));
				map.put("lastUpdate", rs.getString("lastUpdate"));
				map.put("DocType", rs.getString("DocType"));
				map.put("Security", rs.getString("Security"));
				map.put("GrowthPlays", rs.getString("GrowthPlays"));
				map.put("dlURL", rs.getString("dlURL"));
				map.put("URL", rs.getString("URL"));
				map.put("PDFSize", rs.getString("PDFSize"));
				return map;
			}
		});
		if (acceleratorlist.isEmpty()) {
			acceleratorDetail = new HashMap<String, String>();
		} else {
			acceleratorDetail = acceleratorlist.get(0);
		}
		return acceleratorDetail;
	}
	
	
	public Map<String, String> queryEntityTechlineDetail(String idAcc)
			throws Exception {
		
		Map<String, String> techlineDetail = null;
		
		String sqlQueryTechlineDetail = messages.getMessage("SQL_QueryTechlineDetail", new String[]{DB_NAME_FAQ}, null);
		List<Map<String, String>> techlinelist = jdbcTemplateT.query(sqlQueryTechlineDetail, new Object[] {idAcc}, 
				new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("idEntity", rs.getString("idEntity"));
				map.put("title", rs.getString("name"));
				map.put("lastUpdate", rs.getString("lastUpdate"));
				map.put("faqId", rs.getString("faqId"));
				map.put("answerId", rs.getString("answerId"));
				map.put("answerBody", rs.getString("answerBody"));
				map.put("brand", rs.getString("brand"));
				return map;
			}
		});
		if (techlinelist.isEmpty()) {
			techlineDetail = new HashMap<String, String>();
		} else {
			techlineDetail = techlinelist.get(0);
		}
		return techlineDetail;
	}
	
	
	
	public Map<String, String> queryReviewsStatistics(String idAcc,String entityType)
			throws Exception {
		
		Map<String, String> ratingStat = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		if (entityType.equals("2")) {
			String sqlQueryReviewsStat = messages.getMessage("SQL_QUERY_REVIEWS_STAT_TECLINE", new String[]{DB_NAME_FAQ}, null);
			list = jdbcTemplateT.query(sqlQueryReviewsStat, new Object[] {idAcc,entityType}, 
					new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("avgStars", rs.getString("avgStars"));
					map.put("reviewCount", rs.getString("reviewCount"));
					map.put("star1Count", rs.getString("star1Count"));
					map.put("star2Count", rs.getString("star2Count"));
					map.put("star3Count", rs.getString("star3Count"));
					map.put("star4Count", rs.getString("star4Count"));
					map.put("star5Count", rs.getString("star5Count"));
					return map;
				}
			});
		} else {
			String sqlQueryReviewsStat = messages.getMessage("SQL_QUERY_REVIEWS_STAT_REDBOOKS", new String[]{ACCELERATOR_WRITE}, null);
			list = jdbcTemplate.query(sqlQueryReviewsStat, new Object[] {idAcc,entityType}, 
					new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("avgStars", rs.getString("avgStars"));
					map.put("reviewCount", rs.getString("reviewCount"));
					map.put("star1Count", rs.getString("star1Count"));
					map.put("star2Count", rs.getString("star2Count"));
					map.put("star3Count", rs.getString("star3Count"));
					map.put("star4Count", rs.getString("star4Count"));
					map.put("star5Count", rs.getString("star5Count"));
					return map;
				}
			});
		}

		if (list.isEmpty()) {
			ratingStat = new HashMap<String, String>();
		} else {
			ratingStat = list.get(0);
		}
		return ratingStat;
	}

	public List<Map<String, String>> queryEntityReviews(String idUser, String idAcc, long lastIdRating, int limit,String entityType)
			throws Exception {
		List<Map<String, String>> reviewsList = new ArrayList<Map<String, String>>();

		if (entityType.equals("2")) {
			String sqlQueryAcceleratorReviews = messages.getMessage("SQL_QUERY_ENTITY_REVIEWS_TECLINE", new String[]{DB_NAME_FAQ}, null);
			reviewsList = jdbcTemplateT.query(sqlQueryAcceleratorReviews, new Object[] {idUser, idAcc,entityType ,lastIdRating, limit}, 
					new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("idReview", rs.getString("idReview"));
					map.put("idEntity", rs.getString("idEntity"));
					map.put("entityType", rs.getString("entityType"));
					map.put("idUser", rs.getString("idUser"));
					map.put("stars", rs.getString("stars"));
					map.put("category", rs.getString("category"));
					map.put("comment", rs.getString("comment"));
					map.put("ratingTime", rs.getString("ratingTime"));
					map.put("totalLike", rs.getString("totalLike"));
					map.put("ifILike", (rs.getString("idILiked") == null? "0" : "1"));
					return map;
				}
			});
		} else {
			String sqlQueryAcceleratorReviews = messages.getMessage("SQL_QUERY_ENTITY_REVIEWS_REDBOOKS", new String[]{ACCELERATOR_WRITE}, null);
			reviewsList = jdbcTemplate.query(sqlQueryAcceleratorReviews, new Object[] {idUser, idAcc,entityType ,lastIdRating, limit}, 
					new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("idReview", rs.getString("idReview"));
					map.put("idEntity", rs.getString("idEntity"));
					map.put("entityType", rs.getString("entityType"));
					map.put("idUser", rs.getString("idUser"));
					map.put("stars", rs.getString("stars"));
					map.put("category", rs.getString("category"));
					map.put("comment", rs.getString("comment"));
					map.put("ratingTime", rs.getString("ratingTime"));
					map.put("totalLike", rs.getString("totalLike"));
					map.put("ifILike", (rs.getString("idILiked") == null? "0" : "1"));
					return map;
				}
			});
		}
		return reviewsList;
	}
	
	public List<Map<String, String>> queryStarReviews(String idUser, String idAcc, int star, long lastIdRating, int limit,String entityType)
			throws Exception {
		List<Map<String, String>> reviewsList = new ArrayList<Map<String, String>>();
		
		if (entityType.equals("2")) {
			String sqlQueryStarReviews = messages.getMessage("SQL_QUERY_STARS_REVIEWS_TECLINE", new String[]{DB_NAME_FAQ}, null);
			reviewsList = jdbcTemplateT.query(sqlQueryStarReviews, new Object[] {idUser, star, idAcc,entityType, lastIdRating, limit}, 
					new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("idReview", rs.getString("idReview"));
					map.put("idEntity", rs.getString("idEntity"));
					map.put("entityType", rs.getString("entityType"));
					map.put("idUser", rs.getString("idUser"));
					map.put("stars", rs.getString("stars"));
					map.put("category", rs.getString("category"));
					map.put("comment", rs.getString("comment"));
					map.put("ratingTime", rs.getString("ratingTime"));
					map.put("totalLike", rs.getString("totalLike"));
					map.put("ifILike", (rs.getString("idILiked") == null? "0" : "1"));
					return map;
				}
			});
		} else {
			String sqlQueryStarReviews = messages.getMessage("SQL_QUERY_STARS_REVIEWS_REDBOOKS", new String[]{ACCELERATOR_WRITE}, null);
			reviewsList = jdbcTemplate.query(sqlQueryStarReviews, new Object[] {idUser, star, idAcc,entityType, lastIdRating, limit}, 
					new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("idReview", rs.getString("idReview"));
					map.put("idEntity", rs.getString("idEntity"));
					map.put("entityType", rs.getString("entityType"));
					map.put("idUser", rs.getString("idUser"));
					map.put("stars", rs.getString("stars"));
					map.put("category", rs.getString("category"));
					map.put("comment", rs.getString("comment"));
					map.put("ratingTime", rs.getString("ratingTime"));
					map.put("totalLike", rs.getString("totalLike"));
					map.put("ifILike", (rs.getString("idILiked") == null? "0" : "1"));
					return map;
				}
			});
		}
		return reviewsList;
	}
	
	public List<Map<String, String>> queryMostLikedReviews(String idUser, String idAcc, long lastIndex, int limit,String entityType)
			throws Exception {
		if (entityType.equals("2")) {
			List<Map<String, String>> reviewsList = new ArrayList<Map<String, String>>();
			String sqlQueryMostLikedReviews = messages.getMessage("SQL_QUERY_MOSTLIKED_REVIEWS_TECLINE", new String[]{DB_NAME_FAQ}, null);
			reviewsList = jdbcTemplateT.query(sqlQueryMostLikedReviews, new Object[] {idUser, idAcc, entityType,lastIndex, limit}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("idReview", rs.getString("idReview"));
						map.put("idEntity", rs.getString("idEntity"));
						map.put("idUser", rs.getString("idUser"));
						map.put("stars", rs.getString("stars"));
						map.put("category", rs.getString("category"));
						map.put("comment", rs.getString("comment"));
						map.put("ratingTime", rs.getString("ratingTime"));
						map.put("totalLike", rs.getString("totalLike"));
						map.put("ifILike", (rs.getString("idILiked") == null? "0" : "1"));
						return map;
					}
				});
			return reviewsList;
		} else {
			List<Map<String, String>> reviewsList = new ArrayList<Map<String, String>>();
			String sqlQueryMostLikedReviews = messages.getMessage("SQL_QUERY_MOSTLIKED_REVIEWS_REDBOOKS", new String[]{ACCELERATOR_WRITE}, null);
			reviewsList = jdbcTemplate.query(sqlQueryMostLikedReviews, new Object[] {idUser, idAcc, entityType,lastIndex, limit}, 
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("idReview", rs.getString("idReview"));
						map.put("idEntity", rs.getString("idEntity"));
						map.put("idUser", rs.getString("idUser"));
						map.put("stars", rs.getString("stars"));
						map.put("category", rs.getString("category"));
						map.put("comment", rs.getString("comment"));
						map.put("ratingTime", rs.getString("ratingTime"));
						map.put("totalLike", rs.getString("totalLike"));
						map.put("ifILike", (rs.getString("idILiked") == null? "0" : "1"));
						return map;
					}
				});
			return reviewsList;
		}
	}
	
	public void insertReviewLike(String idUser, long idRating, String entityType) throws Exception {
		if (entityType.equals("2")) {
			String sqlInsertRatingsLike = messages.getMessage("SQL_INSERT_RARINGS_LIKE", new String[]{DB_NAME_FAQ}, null);
			jdbcTemplateT.update(sqlInsertRatingsLike, new Object[] {idRating, idUser});
		} else {
			String sqlInsertRatingsLike = messages.getMessage("SQL_INSERT_RARINGS_LIKE", new String[]{ACCELERATOR_WRITE}, null);
			jdbcTemplate.update(sqlInsertRatingsLike, new Object[] {idRating, idUser});
		}
	}
	
	public void deleteReviewLike(String idUser, long idRating, String entityType) throws Exception {
		if (entityType.equals("2")) {
			String sqlDeleteRatingsLike = messages.getMessage("SQL_DELETE_RATINGS_LIKE", new String[]{DB_NAME_FAQ}, null);
			jdbcTemplateT.update(sqlDeleteRatingsLike, new Object[] {idRating, idUser});
		} else {
			String sqlDeleteRatingsLike = messages.getMessage("SQL_DELETE_RATINGS_LIKE", new String[]{ACCELERATOR_WRITE}, null);
			jdbcTemplate.update(sqlDeleteRatingsLike, new Object[] {idRating, idUser});
		}
	}

	
	
}
