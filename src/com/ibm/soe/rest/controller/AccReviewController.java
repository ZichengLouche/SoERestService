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

import com.ibm.soe.rest.service.AccReviewService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("accReview")
public class AccReviewController {

	private static Logger logger = Logger.getLogger(AccReviewController.class);
	 
	@Autowired
	private AccReviewService accReviewService;
	
	@RequestMapping(value = "/queryAccelerator", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryAcceleratorDetail(@RequestBody Map<String, String> param) {
		logger.info("enter queryAcceleratorDetail method with request mapping /queryAccelerator");
		Map<String, String> acceleratorDetail = null;
		try {
			acceleratorDetail = accReviewService.queryAcceleratorDetail(param.get("idAcc"));
			
		} catch (Exception e) {
			logger.error("/queryAccelerator throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult(acceleratorDetail);
	}
	
	@RequestMapping(value = "/queryReviews", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryAcceleratorReviews(@RequestBody Map<String, String> param) {
		logger.info("enter queryAcceleratorReviews method with request mapping /queryReviews");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			String idUser = param.get("idUser");
			String idAcc = param.get("idAcc");
			long lastIdRating = Long.parseLong(param.get("lastIdRating"));
			int limit = Integer.parseInt(param.get("limit"));
			
			if (param.get("star") == null || "".equals(param.get("star")) || "0".equals(param.get("star"))) {
				list = accReviewService.queryAcceleratorReviews(idUser, idAcc, lastIdRating, limit);
			} else {
				int star =  Integer.parseInt(param.get("star"));
				list = accReviewService.queryStarReviews(idUser, idAcc, star, lastIdRating, limit);
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
			String idAcc = param.get("idAcc");
			long lastIndex = Long.parseLong(param.get("lastIndex"));
			int limit = Integer.parseInt(param.get("limit"));
			list = accReviewService.queryMostLikedReviews(idUser, idAcc, lastIndex, limit);
			
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
			accReviewService.updateRatingsLike(iflike, idUser, idRating);
		} catch (Exception e) {
			logger.error("/ratingsLike throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		return RestUtil.handleResult("");
	}
	
	
}
