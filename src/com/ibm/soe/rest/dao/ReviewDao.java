package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface ReviewDao {
	
	public Map<String, String> queryNotRatedCount(String idUser,String entityType)
			throws Exception;
	
	public void updateAlertStatus30(String idUser,String entityType)
			throws Exception;
	
	public void updateAlertStatus15(String idUser,String entityType)
			throws Exception;
	
	public List<Map<String, String>> queryNotRatedRecords(String idUser,String entityType)
			throws Exception;
	
	public void insertView(Map<String,String> param)	throws Exception;
	
	public Map<String,String> queryReviewsCount(String idEntity,String entityType)throws Exception;
	


	public Map<String, String> queryEntityRedbookDetail(String idAcc)
			throws Exception;
	
	public Map<String, String> queryEntityTechlineDetail(String idAcc)
			throws Exception;
	
	public Map<String, String> queryReviewsStatistics(String idAcc,String entityType)
			throws Exception;
	


	public List<Map<String, String>> queryEntityReviews(String idUser,
			String idAcc, long lastIdRating, int limit,String entityType) throws Exception;

	public List<Map<String, String>> queryStarReviews(String idUser,
			String idAcc, int star, long lastIdRating, int limit,String entityType)
			throws Exception;

	public List<Map<String, String>> queryMostLikedReviews(String idUser,
			String idAcc, long lastIndex, int limit,String entityType) throws Exception;

	public void insertReviewLike(String idUser, long idRating, String entityType) throws Exception;

	public void deleteReviewLike(String idUser, long idRating, String entityType) throws Exception;
}
