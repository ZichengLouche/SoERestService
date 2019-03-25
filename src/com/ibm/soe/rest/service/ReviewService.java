package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface ReviewService {
	
	 public Map<String, String> queryNotRatedCount(Map<String,String> param)
			throws Exception;
	
	
	
	 public List<Map<String, String>> queryNotRatedRecords(Map<String,String> param)
				throws Exception;
	 
	 public boolean addReview(Map<String,String> param)
				throws Exception;
	 
	 public Map<String,String> queryReviewsCount(Map<String,String> param)
				throws Exception;
	
	

	public Map<String, String> queryEntityDetail(String idEntity,String entityType)
			throws Exception;

	public List<Map<String, String>> queryEntityReviews(String idUser,
			String idAcc, long lastIdRating, int limit,String entityType) throws Exception;

	public List<Map<String, String>> queryStarReviews(String idUser,
			String idAcc, int star, long lastIdRating, int limit,String entityType)
			throws Exception;

	public List<Map<String, String>> queryMostLikedReviews(String idUser,
			String idAcc, long lastIndex, int limit,String entityType) throws Exception;

	public void updateRatingsLike(boolean iflike, String idUser, long idRating, String entityType)
			throws Exception;
}
