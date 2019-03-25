package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface AccReviewDao {

	public Map<String, String> queryAcceleratorDetail(String idAcc)
			throws Exception;
	
	public Map<String, String> queryReviewsStatistics(String idAcc)
			throws Exception;

	public List<Map<String, String>> queryAcceleratorReviews(String idUser,
			String idAcc, long lastIdRating, int limit) throws Exception;

	public List<Map<String, String>> queryStarReviews(String idUser,
			String idAcc, int star, long lastIdRating, int limit)
			throws Exception;

	public List<Map<String, String>> queryMostLikedReviews(String idUser,
			String idAcc, long lastIndex, int limit) throws Exception;

	public void insertRatingsLike(String idUser, long idRating) throws Exception;

	public void deleteRatingsLike(String idUser, long idRating) throws Exception;
}
