package com.ibm.soe.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.service.DownloadHistoryService;
import com.ibm.soe.rest.service.ReviewService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("review")
public class ReviewController {

	private static Logger logger = Logger.getLogger(ReviewController.class);
	 
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private DownloadHistoryService downloadService;
	
	@RequestMapping(value = "/queryNotRatedCount", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryNotRatedCount(@RequestBody Map<String, String> param) {
		logger.info("enter queryNotRatedCount method with request mapping /queryNotRatedCount");
		Map<String, String> queryNotRatedCount = null;
		try {
			queryNotRatedCount = reviewService.queryNotRatedCount(param);
			
		} catch (Exception e) {
			logger.error("/queryNotRatedCount throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(queryNotRatedCount);
	}
	
	
	
	@RequestMapping(value = "/queryNotRatedRecords", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryNotRatedRecords(@RequestBody Map<String, String> param) {
		logger.info("enter queryNotRatedRecords method with request mapping /queryNotRatedRecords");
		List<Map<String, String>> queryNotRatedRecords = null;
		try {
			queryNotRatedRecords = reviewService.queryNotRatedRecords(param);
			
		} catch (Exception e) {
			logger.error("/queryNotRatedRecords throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(queryNotRatedRecords);
	}
	
	@RequestMapping(value = "/addReview", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> addReview(@RequestBody Map<String, String> param) {
		logger.info("enter addReview method with request mapping /addReview");
		boolean flag=false;
		try {
			flag = reviewService.addReview(param);
			
		} catch (Exception e) {
			logger.error("/addReview throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(flag);
	}
	
	@RequestMapping(value = "/queryReviewsCount", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryReviewsCount(@RequestBody Map<String, String> param) {
		logger.info("enter queryReviewsCount method with request mapping /queryReviewsCount");
		Map<String,String> resultMap=null;
		try {
			resultMap = reviewService.queryReviewsCount(param);
			
		} catch (Exception e) {
			logger.error("/queryReviewsCount throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(resultMap);
	}
	
	
	
	@RequestMapping(value = "/queryEntity", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryEntityDetail(@RequestBody Map<String, String> param) {
		logger.info("enter queryEntity method with request mapping /queryEntity");
		Map<String, String> entityDetail = null;
		String idEntity=param.get("idEntity");
		String entityType=param.get("entityType");
		try {
			entityDetail = reviewService.queryEntityDetail(idEntity,entityType);
			
		} catch (Exception e) {
			logger.error("/queryEntity throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(entityDetail);
	}
	
	@RequestMapping(value = "/queryReviews", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryReviews(@RequestBody Map<String, String> param) {
		logger.info("enter queryAcceleratorReviews method with request mapping /queryReviews");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			String idUser = param.get("idUser");
			String idAcc = param.get("idEntity");
			long lastIdRating = Long.parseLong(param.get("lastIdRating"));
			int limit = Integer.parseInt(param.get("limit"));
			String entityType = param.get("entityType");
			
			if (param.get("star") == null || "".equals(param.get("star")) || "0".equals(param.get("star"))) {
				list = reviewService.queryEntityReviews(idUser, idAcc, lastIdRating, limit,entityType);
			} else {
				int star =  Integer.parseInt(param.get("star"));
				list = reviewService.queryStarReviews(idUser, idAcc, star, lastIdRating, limit,entityType);
			}
			
		} catch (Exception e) {
			logger.error("/queryReviews throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/queryMostLikedReviews", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryMostLikedReviews(@RequestBody Map<String, String> param) {
		logger.info("enter queryMostLikedReviews method with request mapping /queryMostLikedReviews");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			String idUser = param.get("idUser");
			String idAcc = param.get("idEntity");
			long lastIndex = Long.parseLong(param.get("lastIndex"));
			int limit = Integer.parseInt(param.get("limit"));
			String entityType = param.get("entityType");
			list = reviewService.queryMostLikedReviews(idUser, idAcc, lastIndex, limit,entityType);
			
		} catch (Exception e) {
			logger.error("/queryMostLikedReviews throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(list);
	}
	
	@RequestMapping(value = "/ratingsLike", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> ratingsLike(@RequestBody Map<String, String> param) {
		logger.info("enter ratingsLike method with request mapping /ratingsLike");
		try {
			boolean iflike = Boolean.valueOf(param.get("iflike")) || "1".equals(param.get("iflike"));
			String idUser = param.get("idUser");
			long idRating = Long.parseLong(param.get("idRating"));
			String entityType = param.get("entityType");
			if (entityType == null) {
				entityType = "1";
			}
			reviewService.updateRatingsLike(iflike, idUser, idRating, entityType);
		} catch (Exception e) {
			logger.error("/ratingsLike throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult("");
	}
	
	
	
	@RequestMapping(value = "/addDownloadHistory", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertDownloadHistory(@RequestBody Map<String, String> param) {
		logger.info("enter addDownloadHistory method with request mapping /addDownloadHistory");
		String key=null;
		try {
			
			key = downloadService.insertDownloadHistory(param);
			
		} catch (Exception e) {
			logger.error("/downloadHistory throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(key);
	}
	
	
	@RequestMapping(value = "/updateHistoryHasRated", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateHistoryHasRated(@RequestBody Map<String, String> param) {
		logger.info("enter updateHistoryHasRated method with request mapping /updateHistoryHasRated");
		try {
			
			downloadService.updateHistoryHasRated(param);
		} catch (Exception e) {
			logger.error("/updateHistoryHasRated throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult("");
	}
	
}
