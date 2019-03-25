package com.ibm.soe.rest.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.BSUDao;
import com.ibm.soe.rest.service.BSUService;

@Service
@Transactional
public class BSUServiceImpl implements BSUService {

	@Autowired
	private BSUDao bsuDao;
	
	// Andy 2016.1.12 22:02
	@Override
	public Map<String, Object> queryBSUResults(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String pageIndex, String pageLimit, String idIsn) throws Exception {
		int intPageLimit = Integer.parseInt(pageLimit);
		long startIndex = Long.parseLong(pageIndex) * intPageLimit;

		List<Map<String, Object>> list = bsuDao.queryBSUResults(keyword, idInd, idIsa, idCnt, idBuyer, idBSU, startIndex, intPageLimit, idIsn);
		long total = bsuDao.queryFilterAllCount(keyword, idInd, idIsa, idCnt, idBuyer, idBSU, idIsn);

		Map<String, Object> results = new HashMap<String, Object>();
		results.put("list", list);
		results.put("total", total);

		return results;
	}

	@Override
	public Map<String, Object> queryBSUFilter(String keyword, String idInd, String idIsa, String idCnt, String idBuyer, String idBSU, String idIsn, String multipleIndustry) throws Exception {
		Map<String, Object> filterResultMap = new HashMap<String, Object>();

		// getBSU
		long bsuTotal = bsuDao.queryFilterAllCount(keyword, idInd, idIsa, idCnt, idBuyer, null, idIsn);
		List<Map<String, Object>> bsuFilterlist = bsuDao.queryBSUFilterCount(keyword, idInd, idIsa, idCnt, idBuyer, null, idIsn);
		Map<String, Object> bsuTotalMap = new HashMap<String, Object>();
		bsuTotalMap.put("id", "0");
		bsuTotalMap.put("name", "All Business Units");
		bsuTotalMap.put("num", bsuTotal);
		bsuFilterlist.add(0, bsuTotalMap);
		filterResultMap.put("bsu", bsuFilterlist);

		// getIndustries
		long industryTotal = bsuDao.queryFilterAllCount(keyword, multipleIndustry, null, idCnt, idBuyer, idBSU, idIsn);
		List<Map<String, Object>> industryFilterlist = bsuDao.queryIndustryFilterCount(keyword, multipleIndustry, null, idCnt, idBuyer, idBSU, idIsn);
		Map<String, Object> industryTotalMap = new HashMap<String, Object>();
		industryTotalMap.put("id", "0");
		industryTotalMap.put("num", industryTotal);
		industryFilterlist.add(0, industryTotalMap);
		// Andy 2016.8.5 15:56
		if(multipleIndustry != null) {
			industryTotalMap.put("name", "Selected Industries");
		} else {
			industryTotalMap.put("name", "All Industries");
		}
		filterResultMap.put("industry", industryFilterlist);

		// getImperatives
		long imperativeTotal = bsuDao.queryFilterAllCount(keyword, idInd, null, idCnt, idBuyer, idBSU, idIsn);
		List<Map<String, Object>> imperativeFilterlist = bsuDao.queryImperativeFilterCount(keyword, idInd, null, idCnt, idBuyer, idBSU, idIsn);
		Map<String, Object> imperativeTotalMap = new HashMap<String, Object>();
		imperativeTotalMap.put("id", "0");
		imperativeTotalMap.put("name", "All Imperatives");
		imperativeTotalMap.put("num", imperativeTotal);
		imperativeFilterlist.add(0, imperativeTotalMap);
		filterResultMap.put("imperative", imperativeFilterlist);

		// getContentType
		long contentTypeTotal = bsuDao.queryFilterAllCount(keyword, idInd, idIsa, null, idBuyer, idBSU, idIsn);
		List<Map<String, Object>> contentTypeFilterlist = bsuDao.queryContentTypeFilterCount(keyword, idInd, idIsa, null, idBuyer, idBSU, idIsn);
		Map<String, Object> contentTypeTotalMap = new HashMap<String, Object>();
		contentTypeTotalMap.put("id", "0");
		contentTypeTotalMap.put("name", "All Content Types");
		contentTypeTotalMap.put("num", contentTypeTotal);
		contentTypeFilterlist.add(0, contentTypeTotalMap);
		filterResultMap.put("contentType", contentTypeFilterlist);

		// getBuyers
		long buyerTotal = bsuDao.queryFilterAllCount(keyword, idInd, idIsa, idCnt, null, idBSU, idIsn);
		List<Map<String, Object>> buyerFilterlist = bsuDao.queryBuyerFilterCount(keyword, idInd, idIsa, idCnt, null, idBSU, idIsn);
		Map<String, Object> buyerTotalMap = new HashMap<String, Object>();
		buyerTotalMap.put("id", "0");
		buyerTotalMap.put("name", "All Buyers");
		buyerTotalMap.put("num", buyerTotal);
		buyerFilterlist.add(0, buyerTotalMap);
		filterResultMap.put("buyer", buyerFilterlist);

		return filterResultMap;
	}
	
	// Andy 2016.1.14 22:25
	@Override
	public List<Map<String, Object>> querySolutionPart(String idIsn, String detailsAbbrev, final String isLaptopEnv, final String reg) throws Exception {
		return bsuDao.querySolutionPart(idIsn, detailsAbbrev, isLaptopEnv, reg);
	}
	
	public List<Map<String, Object>> queryContacts(String idIsn, String idInd, String idISA) throws Exception {
		return bsuDao.queryContacts(idIsn, idInd, idISA);
	}

	@Override
	public List<Map<String, Object>> querySolutionMapForUseCase(String idIsn) throws Exception {
		return bsuDao.querySolutionMapForUseCase(idIsn);
	}

	@Override
	public List<Map<String, Object>> querySolutionMapForSolution(String idIsn) throws Exception {
		return bsuDao.querySolutionMapForSolution(idIsn);
	}

	@Override
	public List<Map<String, Object>> queryIdentifiedBuyers(String idIsn) throws Exception {
		return bsuDao.queryIdentifiedBuyers(idIsn);
	}
}
