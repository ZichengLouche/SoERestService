package com.ibm.soe.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.CommFunctionDao;
import com.ibm.soe.rest.dao.SolutionPartDao;
import com.ibm.soe.rest.service.SolutionGatewayService;

@Service
@Transactional
public class SolutionGatewayServiceImpl implements SolutionGatewayService{

	@Autowired
	private SolutionPartDao solutionPartDao;
	
	@Autowired
	private CommFunctionDao commFunctionDao;
	
	@Value("${TABLE_SOLUTION_PART}")
	private String TABLE_SOLUTION_PART;
	
	@Transactional
	@Override
	public boolean processSGData(List<Map<String, String>> sglist, int type, Map<String, Map<String,String>> fourLevelCascadeIdsMap) {
		for(Map<String, String> item : sglist) {
			String idOffering = item.get("idOffering");
			item.putAll(fourLevelCascadeIdsMap.get(idOffering));
		}
		
		boolean result = solutionPartDao.insertSolutionPartTemp(sglist, type);
		return result;
	}

	@Transactional
	@Override
	public boolean copyDataToSolutionpart(Integer type) throws Exception {
		if(type.intValue() == 0){
//			solutionPartDao.clearSolutionPart(1);
//			solutionPartDao.clearSolutionPart(2);
			commFunctionDao.truncateTableData(TABLE_SOLUTION_PART);
		} else {
			solutionPartDao.clearSolutionPart(type);
		}

		boolean result = solutionPartDao.copyTempToSolutionPart();
//		solutionPartDao.insertNewListToMapTable();
		return result;
	}


	@Override
	public Map<String, Map<String,String>> getFourLevelCascadeIds(int type) {
		Map<String, Map<String,String>> fourLevelCascadeIdsMap = new HashMap<String,Map<String,String>>();
		List<Map<String,String>> fourLevelCascadeIdsList = new ArrayList<Map<String,String>>();
		if (type == 1) {
			fourLevelCascadeIdsList = solutionPartDao.getFourLevelCascadeIdsForBlueprint();
		} else if(type ==2) {
			fourLevelCascadeIdsList = solutionPartDao.getFourLevelCascadeIdsForBSU();
		}

		for(Map<String,String> item: fourLevelCascadeIdsList) {
			fourLevelCascadeIdsMap.put(item.get("idOffering"), item);
		}
		
		return fourLevelCascadeIdsMap;
	}
	
	public List<String> getOfferingOID() {
		return solutionPartDao.getOfferingOID();
	}

	// to-delete 2016.12.8 17:42
	@Override
	public void clearSolutionTemp() {
		solutionPartDao.deleteSolutionPartTemp();
	}

	public Map<String,Map<String, String>> getBSUListIds() {
		Map<String,Map<String, String>> bsuListIdMap = new HashMap<String,Map<String, String>>();
		List<Map<String,String>> bsuListIds =  solutionPartDao.queryListidMapBSU();
		
		for(Map<String,String> item : bsuListIds) {			
			if (bsuListIdMap.containsKey(item.get("IDBSU"))) {
				Map<String,String> OfferMap = bsuListIdMap.get(item.get("IDBSU"));
				OfferMap.put(item.get("listid"), item.get("listid"));  
				
			} else {
				Map<String,String> OfferMap=new HashMap<String,String>();
				OfferMap.put(item.get("listid"), item.get("listid"));  
				bsuListIdMap.put(item.get("IDBSU"), OfferMap);
			}
		}
		return bsuListIdMap;
	}
	
	public Map<String,Map<String, String>> getSolutionBsuOID() {
		Map<String,Map<String, String>> solutionBsuOIDMap = new HashMap<String,Map<String, String>>();
		List<Map<String,String>> solutionBsuOIDList = solutionPartDao.getSolutionBsuOID();
		for(Map<String,String> item : solutionBsuOIDList ) {			
			if (solutionBsuOIDMap.containsKey(item.get("IDBSU"))) {
				Map<String,String> OfferMap = solutionBsuOIDMap.get(item.get("IDBSU"));
				OfferMap.put(item.get("Off_OID"), item.get("IDKEY"));  
				
			} else {
				Map<String,String> OfferMap = new HashMap<String,String>();
				OfferMap.put(item.get("Off_OID"), item.get("IDKEY"));  
				solutionBsuOIDMap.put(item.get("IDBSU"), OfferMap);
			}
		}
		return solutionBsuOIDMap;
	}

	@Override
	public List<Map<String, String>> getBSUToolID() {
		return solutionPartDao.getBSUToolID();
	}
	
	
	public List<String> getBLUEPRINTListIds() {
		return solutionPartDao.getBLUEPRINTListIds();
	}
	
	public List<String> getBLUEPRINTExceptionListIds() {
		return solutionPartDao.getBLUEPRINTExceptionListIds();
	}
	
	public List<String> getBLUEPRINT0ListIds() {
		return solutionPartDao.getBLUEPRINT0ListIds();
	}
}
