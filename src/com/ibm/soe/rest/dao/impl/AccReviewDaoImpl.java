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

import com.ibm.soe.rest.dao.AccReviewDao;

@Repository
public class AccReviewDaoImpl implements AccReviewDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messages;
	
	@Value("${DB_NAME_ACCELERATOR_WRITE}")
	private String ACCELERATOR_WRITE;
	
	public Map<String, String> queryAcceleratorDetail(String idAcc)
			throws Exception {
		
		Map<String, String> acceleratorDetail = null;
		
		String sqlQueryAcceleratorDetail = messages.getMessage("SQL_QueryAcceleratorDetail", new String[]{}, null);
		List<Map<String, String>> acceleratorlist = jdbcTemplate.query(sqlQueryAcceleratorDetail, new Object[] {idAcc}, 
				new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", rs.getString("name"));
				map.put("descr", rs.getString("descr"));
				map.put("AREA", rs.getString("AREA"));
				map.put("solu", rs.getString("solu"));
				map.put("Industry", rs.getString("Industry"));
				map.put("cvm", rs.getString("cvm"));
				map.put("Type", rs.getString("Type"));
				map.put("Last_Updated", rs.getString("Last_Updated"));
				map.put("dlURL", rs.getString("dlURL"));
				map.put("CAMSS", rs.getString("CAMSS"));
				map.put("Cloud", rs.getString("Cloud"));
				map.put("Analytics", rs.getString("Analytics"));
				map.put("Mobile", rs.getString("Mobile"));
				map.put("Social", rs.getString("Social"));
				map.put("Security", rs.getString("Security"));
				map.put("Top_Asset", rs.getString("Top_Asset"));
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
	
	public Map<String, String> queryReviewsStatistics(String idAcc)
			throws Exception {
		
		Map<String, String> ratingStat = null;
		
		String sqlQueryReviewsStat = messages.getMessage("SQL_QueryReviewsStat", new String[]{ACCELERATOR_WRITE}, null);
		List<Map<String, String>> list = jdbcTemplate.query(sqlQueryReviewsStat, new Object[] {idAcc}, 
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
		if (list.isEmpty()) {
			ratingStat = new HashMap<String, String>();
		} else {
			ratingStat = list.get(0);
		}
		return ratingStat;
	}

	public List<Map<String, String>> queryAcceleratorReviews(String idUser, String idAcc, long lastIdRating, int limit)
			throws Exception {
		
		String sqlQueryAcceleratorReviews = messages.getMessage("SQL_QueryAcceleratorReviews", new String[]{ACCELERATOR_WRITE}, null);
		List<Map<String, String>> reviewsList = jdbcTemplate.query(sqlQueryAcceleratorReviews, new Object[] {idUser, idAcc, lastIdRating, limit}, 
				new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("idRating", rs.getString("idRating"));
				map.put("idAccelerator", rs.getString("idAccelerator"));
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
	
	public List<Map<String, String>> queryStarReviews(String idUser, String idAcc, int star, long lastIdRating, int limit)
			throws Exception {
		
		String sqlQueryStarReviews = messages.getMessage("SQL_QueryStarReviews", new String[]{ACCELERATOR_WRITE}, null);
		List<Map<String, String>> reviewsList = jdbcTemplate.query(sqlQueryStarReviews, new Object[] {idUser, star, idAcc, lastIdRating, limit}, 
				new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("idRating", rs.getString("idRating"));
				map.put("idAccelerator", rs.getString("idAccelerator"));
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
	
	public List<Map<String, String>> queryMostLikedReviews(String idUser, String idAcc, long lastIndex, int limit)
			throws Exception {
		
		List<Map<String, String>> reviewsList = new ArrayList<Map<String, String>>();
		String sqlQueryMostLikedReviews = messages.getMessage("SQL_QueryMostLikedReviews", new String[]{ACCELERATOR_WRITE}, null);
		reviewsList = jdbcTemplate.query(sqlQueryMostLikedReviews, new Object[] {idUser, idAcc, lastIndex, limit}, 
			new RowMapper<Map<String, String>>() {
				public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("idRating", rs.getString("idRating"));
					map.put("idAccelerator", rs.getString("idAccelerator"));
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
	
	public void insertRatingsLike(String idUser, long idRating) throws Exception {
		String sqlInsertRatingsLike = messages.getMessage("SQL_InsertRatingsLike", new String[]{ACCELERATOR_WRITE}, null);
		jdbcTemplate.update(sqlInsertRatingsLike, new Object[] {idRating, idUser});
	}
	
	public void deleteRatingsLike(String idUser, long idRating) throws Exception {
		String sqlDeleteRatingsLike = messages.getMessage("SQL_DeleteRatingsLike", new String[]{ACCELERATOR_WRITE}, null);
		jdbcTemplate.update(sqlDeleteRatingsLike, new Object[] {idRating, idUser});
	}

}
