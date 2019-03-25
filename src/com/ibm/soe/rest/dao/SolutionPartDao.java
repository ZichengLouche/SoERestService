package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;


public interface SolutionPartDao {

	public boolean insertNewListToMapTable();
	
	public  boolean insertSolutionPartTemp(final List<Map<String, String>> param, final Integer type);
	
	public  boolean deleteSolutionPartTemp() ;
	
	public boolean clearSolutionPart(Integer type);
	
	public boolean copyTempToSolutionPart();
	
	public List<Map<String, String>> getFourLevelCascadeIdsForBlueprint();
	
	public List<String> getOfferingOID();
	
	public String getSolutionpart();
	
	public List<Map<String, String>> queryListidMapBSU();
	
	public List<Map<String, String>> getBSUToolID();
	
	public List<Map<String, String>> getSolutionBsuOID();
	
	public List<Map<String, String>> getFourLevelCascadeIdsForBSU();

	public List<String> getBLUEPRINTListIds();

	public List<String> getBLUEPRINTExceptionListIds();

	public List<String> getBLUEPRINT0ListIds();
}
