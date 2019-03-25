package com.ibm.soe.rest.service;

import java.util.List;
import java.util.Map;

public interface SolutionGatewayService {
	
	public boolean processSGData(List<Map<String,String>> sglist, int type, Map<String, Map<String,String>> fourLevelCascadeIdsMap) ;
	
	public boolean copyDataToSolutionpart(Integer type) throws Exception;

	public Map<String,Map<String,String>> getFourLevelCascadeIds(int type);
	
	public void clearSolutionTemp();
	
	public List<String> getOfferingOID();
	
	public Map<String, Map<String, String>> getBSUListIds();
	public List<Map<String, String>> getBSUToolID();
	
	public Map<String, Map<String, String>> getSolutionBsuOID();

	public List<String> getBLUEPRINTListIds();

	public List<String> getBLUEPRINTExceptionListIds();

	public List<String> getBLUEPRINT0ListIds();
}
