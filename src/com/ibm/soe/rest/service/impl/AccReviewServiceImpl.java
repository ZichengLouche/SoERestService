package com.ibm.soe.rest.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.soe.rest.dao.AccReviewDao;
import com.ibm.soe.rest.service.AccReviewService;

@Service
public class AccReviewServiceImpl implements AccReviewService {

	@Autowired
	private AccReviewDao accReviewDao;

	public Map<String, String> queryAcceleratorDetail(String idAcc)
			throws Exception {
		Map<String, String> acceleratorDetail = accReviewDao.queryAcceleratorDetail(idAcc);
		Map<String, String> reviewsStat = accReviewDao.queryReviewsStatistics(idAcc);
		acceleratorDetail.putAll(reviewsStat);
		return acceleratorDetail;
	}

	public List<Map<String, String>> queryAcceleratorReviews(String idUser,
			String idAcc, long lastIdRating, int limit) throws Exception {
		return accReviewDao.queryAcceleratorReviews(idUser, idAcc,
				lastIdRating, limit);
	}

	public List<Map<String, String>> queryStarReviews(String idUser,
			String idAcc, int star, long lastIdRating, int limit)
			throws Exception {
		return accReviewDao.queryStarReviews(idUser, idAcc, star, lastIdRating,
				limit);
	}

	public List<Map<String, String>> queryMostLikedReviews(String idUser,
			String idAcc, long lastIndex, int limit) throws Exception {
		return accReviewDao.queryMostLikedReviews(idUser, idAcc, lastIndex,
				limit);
	}

	public void updateRatingsLike(boolean iflike, String idUser, long idRating)
			throws Exception {
		if (iflike) {
			accReviewDao.insertRatingsLike(idUser, idRating);
		} else {
			accReviewDao.deleteRatingsLike(idUser, idRating);
		}
	}

}
