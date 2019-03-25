package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface AccReviewService {

	public Map<String, String> queryAcceleratorDetail(String idAcc)
			throws Exception;

	public List<Map<String, String>> queryAcceleratorReviews(String idUser,
			String idAcc, long lastIdRating, int limit) throws Exception;

	public List<Map<String, String>> queryStarReviews(String idUser,
			String idAcc, int star, long lastIdRating, int limit)
			throws Exception;

	public List<Map<String, String>> queryMostLikedReviews(String idUser,
			String idAcc, long lastIndex, int limit) throws Exception;

	public void updateRatingsLike(boolean iflike, String idUser, long idRating)
			throws Exception;

}
