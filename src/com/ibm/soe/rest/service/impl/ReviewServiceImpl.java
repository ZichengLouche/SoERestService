package com.ibm.soe.rest.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.DownloadHistoryDao;
import com.ibm.soe.rest.dao.ReviewDao;
import com.ibm.soe.rest.service.ReviewService;
@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	private ReviewDao reviewDao;
	
	@Autowired
	private DownloadHistoryDao downloadHistoryDao;

	@Override
	public Map<String, String> queryNotRatedCount(Map<String, String> param)
			throws Exception {
		// TODO Auto-generated method stub
		String idUser=param.get("idUser");
		String entityType=param.get("entityType");
		Map<String,String> result = reviewDao.queryNotRatedCount(idUser,entityType );
		String ifAlert=param.get("ifAlert");
		String shouldRatedCount30=result.get("shouldRatedCount30");
		String shouldRatedCount15=result.get("shouldRatedCount15");
		if("true".equalsIgnoreCase(ifAlert) &&  result.size() > 0 && shouldRatedCount30!=null && shouldRatedCount15!=null) {
			int shouldRatedCountint30 = Integer.parseInt(shouldRatedCount30);
			int shouldRatedCountint15 = Integer.parseInt(shouldRatedCount15);;
			if (shouldRatedCountint30 > 0) {
				reviewDao.updateAlertStatus30(idUser, entityType);
			} else if (shouldRatedCountint15 > 0) {
				reviewDao.updateAlertStatus15(idUser, entityType);
			}
		}
		result.put("entityType", entityType);
		return result;
	}
	

	@Override
	public List<Map<String, String>> queryNotRatedRecords(
			Map<String, String> param) throws Exception {
		// TODO Auto-generated method stub
		String idUser=param.get("idUser");
		String entityType=param.get("entityType");
		List<Map<String,String>> result = reviewDao.queryNotRatedRecords(idUser,entityType);
		return result;
	}

	@Transactional
	public boolean addReview(Map<String,String> param)throws Exception
	{
		String idEntity=param.get("idEntity");
		String idUser=param.get("idUser");
		String status="1";
		String entityType=param.get("entityType");
		downloadHistoryDao.updateHistoryHasRated(idEntity, idUser, status,entityType);
		reviewDao.insertView(param);
		return true;
	}
	
	
	
	public Map<String, String> queryEntityDetail(String idEntity,String entityType)
			throws Exception {
		Map<String, String> entityDetail=null;
		Map<String, String> reviewsStat=null;
		if("1".equals(entityType)) //redbooks
		{
			entityDetail = reviewDao.queryEntityRedbookDetail(idEntity);
			reviewsStat = reviewDao.queryReviewsStatistics(idEntity,entityType);
		}else if("2".equals(entityType)) //techline
		{
			entityDetail = reviewDao.queryEntityTechlineDetail(idEntity);
			reviewsStat = reviewDao.queryReviewsStatistics(idEntity,entityType);
		}
		
		entityDetail.putAll(reviewsStat);
		return entityDetail;
	}

	public List<Map<String, String>> queryEntityReviews(String idUser,
			String idAcc, long lastIdRating, int limit,String entityType) throws Exception {
		return reviewDao.queryEntityReviews(idUser, idAcc,
				lastIdRating, limit,entityType);
	}

	public List<Map<String, String>> queryStarReviews(String idUser,
			String idAcc, int star, long lastIdRating, int limit,String entityType)
			throws Exception {
		return reviewDao.queryStarReviews(idUser, idAcc, star, lastIdRating,
				limit,entityType);
	}

	public List<Map<String, String>> queryMostLikedReviews(String idUser,
			String idAcc, long lastIndex, int limit,String entityType) throws Exception {
		return reviewDao.queryMostLikedReviews(idUser, idAcc, lastIndex,
				limit,entityType);
	}

	public void updateRatingsLike(boolean iflike, String idUser, long idRating, String entityType)
			throws Exception {
		if (iflike) {
			reviewDao.insertReviewLike(idUser, idRating, entityType);
		} else {
			reviewDao.deleteReviewLike(idUser, idRating, entityType);
		}
	}


	 public Map<String,String> queryReviewsCount(Map<String,String> param)
				throws Exception
	{
		 String idEntity=param.get("idEntity");
		 String entityType=param.get("entityType");
		 return reviewDao.queryReviewsCount(idEntity,entityType);
	}


	
}
