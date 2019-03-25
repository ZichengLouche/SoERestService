package com.ibm.soe.rest.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.ProductionInformationDao;
import com.ibm.soe.rest.service.ProductInformationService;
import com.ibm.soe.rest.util.ReturnCodeMessage;
import com.ibm.soe.rest.vo.AssistVoLocal;
@Service
@Transactional
public class ProductInformationServiceImpl implements ProductInformationService {
	private static Logger logger = Logger.getLogger(ProductInformationServiceImpl.class);
	
	@Autowired
	private ProductionInformationDao productionInformationDao;

	@Override
	public Map<String, Object> queryProductInformationResults(String brand,String[] growthPlays,String source,String date,String keyword,String sortName,String sortBy,String idUser, 
			String pageIndex, String pageLimit) throws Exception {
	
		int intPageLimitt = Integer.parseInt(pageLimit) - 1;
		int startIndex = Integer.parseInt(pageIndex) * intPageLimitt;
		List<Map<String, String>> redbooklist = new ArrayList<Map<String, String>>();
		List<Map<String, String>> teclinelist = new ArrayList<Map<String, String>>();
		
		// Andy 2016.8.30 16:52
		if ("1".equals(source)) {
			try {
				redbooklist = productionInformationDao.queryProductionRedbookResults(brand, growthPlays, "1", date, keyword, idUser);
			} catch (Exception e) {
				logger.error("productionInformationDao.queryProductionRedbookResults throws exception: ", e);
				AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_REDBOOK, ReturnCodeMessage.FAIL_DEP_DB_REDBOOK_MESSAGE);
			}
		} else if ("2".equals(source)){	
			try {
				teclinelist = productionInformationDao.queryProductionTeclineResults(brand, growthPlays, "2", date, keyword, idUser);
			} catch (Exception e) {
				logger.error("productionInformationDao.queryProductionTeclineResults throws exception: ", e);
				AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_TECHLINE, ReturnCodeMessage.FAIL_DEP_DB_TECHLINE_MESSAGE);
			}
		} else {
			CountDownLatch count = new CountDownLatch(2);
	        for (int i = 1; i <= 2; i++) {
	        	if (i == 1) {
	        		ProductInforThread my = new ProductInforThread(brand, growthPlays, "1", date, keyword, idUser, count, redbooklist);  
		            my.start(); 
	        	} else {
	        		ProductInforThread my = new ProductInforThread(brand, growthPlays, "2", date, keyword, idUser, count, teclinelist);  
		            my.start(); 
	        	}
	        }
	        try {
	        	TimeUnit unit = TimeUnit.SECONDS;
	            count.await(60, unit);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.addAll(teclinelist);
		list.addAll(redbooklist);
		
		//Sort - title/date/reviews/source
		if(sortName.equalsIgnoreCase("title")) {
			if (sortBy.toUpperCase().equals("DESC")) {
		        Collections.sort(list, new Comparator<Map<String, String>>() {
		            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
		            	return 0 - arg0.get("title").compareTo(arg1.get("title"));
		            }
		        });
			} else {
		        Collections.sort(list, new Comparator<Map<String, String>>() {
		            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
		            	return arg0.get("title").compareTo(arg1.get("title"));
		            }
		        });
			}
		} else if (sortName.equalsIgnoreCase("date")) {
			if (sortBy.toUpperCase().equals("DESC")) {
		        Collections.sort(list, new Comparator<Map<String, String>>() {
		            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
		            	int c = 0 - new Date(arg0.get("lastUpdate")).compareTo(new Date(arg1.get("lastUpdate")));
		            	if (c == 0) {
		            		return arg0.get("title").compareTo(arg1.get("title"));
		            	} else {
		            		return c;
		            	}
		            }
		        });
			} else {
		        Collections.sort(list, new Comparator<Map<String, String>>() {
		            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
		            	int c = new Date(arg0.get("lastUpdate")).compareTo(new Date(arg1.get("lastUpdate")));
		            	if (c == 0) {
		            		return arg0.get("title").compareTo(arg1.get("title"));
		            	}
		            	else {
		            		return c;
		            	}
		            }
		        });
			}
			
		} else if (sortName.equalsIgnoreCase("reviews")) {
			if (sortBy.toUpperCase().equals("DESC")) {
		        Collections.sort(list, new Comparator<Map<String, String>>() {
		            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
		            	int c = 0 - Integer.valueOf(arg0.get("reviewCount")).compareTo(Integer.valueOf(arg1.get("reviewCount")));
		            	if (c == 0) {
		            		return arg0.get("title").compareTo(arg1.get("title"));
		            	} else {
		            		return c;
		            	}
		            }
		        });
			} else {
		        Collections.sort(list, new Comparator<Map<String, String>>() {
		            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
		            	int c = Integer.valueOf(arg0.get("reviewCount")).compareTo(Integer.valueOf(arg1.get("reviewCount")));
		            	if (c == 0) {
		            		return arg0.get("title").compareTo(arg1.get("title"));
		            	} else {
		            		return c;
		            	}
		            }
		        });
			}
			
		} else if (sortName.equalsIgnoreCase("source")) {
			if (sortBy.toUpperCase().equals("DESC")) {
		        Collections.sort(list, new Comparator<Map<String, String>>() {
		            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
		            	int c = 0 - Integer.valueOf(arg0.get("entityType")).compareTo(Integer.valueOf(arg1.get("entityType")));
		            	if (c == 0) {
		            		return arg0.get("title").compareTo(arg1.get("title"));
		            	} else {
		            		return c;
		            	}
		            }
		        });
			} else {
		        Collections.sort(list, new Comparator<Map<String, String>>() {
		            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
		            	int c = Integer.valueOf(arg0.get("entityType")).compareTo(Integer.valueOf(arg1.get("entityType")));
		            	if (c == 0) {
		            		return arg0.get("title").compareTo(arg1.get("title"));
		            	} else {
		            		return c;
		            	}
		            }
		        });
			}
		}
		
		int total =	list.size();
		Map<String, Object> results = new HashMap<String, Object>();
		int endIndex = startIndex + intPageLimitt;
		if (endIndex > total) {
			endIndex = total;
		}
		results.put("list", total == 0 ? list : list.subList(startIndex, endIndex));
		results.put("total", total);
		return results;
	}
	
	public Map<String, Object> queryProductionFilter(String brand, String[] growthPlays,String source,String date,String keyword) throws Exception {
		Map<String,Object> fileterResultsMap = new HashMap<String,Object>();
		
		//Get brand
		List<Map<String,String>> brandTecList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> brandRedList = new ArrayList<Map<String,String>>();
		if ("1".equals(source)) {
			try {
				brandRedList = productionInformationDao.queryRedbookBrandFilterCount(brand, growthPlays, source, date, keyword);
			} catch (Exception e) {
				logger.error("productionInformationDao.queryRedbookBrandFilterCount throws exception: ", e);
				AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_REDBOOK, ReturnCodeMessage.FAIL_DEP_DB_REDBOOK_MESSAGE);
			}
		} else if ("2".equals(source)){
			try {
				brandTecList = productionInformationDao.queryTeclineBrandFilterCount(brand, growthPlays, source, date, keyword);
			} catch (Exception e) {
				logger.error("productionInformationDao.queryTeclineBrandFilterCount throws exception: ", e);
				AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_TECHLINE, ReturnCodeMessage.FAIL_DEP_DB_TECHLINE_MESSAGE);
			}
		} else {
			CountDownLatch count = new CountDownLatch(2);
	        for (int i = 1; i <= 2; i++) {
	        	if (i == 1) {
	        		ProductFilterThread my = new ProductFilterThread(brand, growthPlays, "1", date, keyword, count, brandRedList);  
		            my.start(); 
	        	} else {
	        		ProductFilterThread my = new ProductFilterThread(brand, growthPlays, "2", date, keyword, count, brandTecList);  
		            my.start(); 
	        	}
	        }
	        try {  
	        	TimeUnit unit = TimeUnit.SECONDS;
	            count.await(60, unit);
	        } catch (InterruptedException e) {  
	            e.printStackTrace();
	        }
		}

		Integer brandTotal = 0;
		HashMap<String, Map<String,String>> allBrandMap = new HashMap<String, Map<String,String>>();
		for(Map<String,String> m : brandTecList) {
			allBrandMap.put(m.get("brand"), m);
			brandTotal = m.get("num")==null ? 0 : Integer.parseInt(m.get("num")) + brandTotal;
		}
		
		for(Map<String,String> m : brandRedList) {
			if (allBrandMap.containsKey(m.get("brand"))) {
				Map<String,String> exist = allBrandMap.get(m.get("brand"));
				Integer total = (exist.get("num")==null ? 0 : Integer.parseInt(exist.get("num"))) + (m.get("num")==null ? 0 : Integer.parseInt(m.get("num")));
				exist.put("num", total.toString());
			} else {
				allBrandMap.put(m.get("brand"), m);
			}
			brandTotal = m.get("num")==null ? 0 : Integer.parseInt(m.get("num")) + brandTotal;
		}
		
		List<Map<String,String>> allBrandList = new ArrayList<Map<String,String>>();
		for(Map<String,String> obj : allBrandMap.values()){
			allBrandList.add(obj);
		}
		
        Collections.sort(allBrandList, new Comparator<Map<String, String>>() {
            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
            	return arg0.get("brand").compareTo(arg1.get("brand"));
            }
        });
        
		Map<String,String> brandTotalMap = new HashMap<String,String>();
		brandTotalMap.put("brand", "0");
		brandTotalMap.put("name", "All Brands");
		brandTotalMap.put("num", String.valueOf(brandTotal));
		allBrandList.add(0, brandTotalMap);
		fileterResultsMap.put("brandFilter", allBrandList);

		//source Andy 2016.8.30 16:52
		List<Map<String, String>> sourceRedList = new ArrayList<Map<String,String>>();
		try {
			sourceRedList = productionInformationDao.queryRedbookSourceFilterCount(brand, growthPlays, source, date, keyword);
		} catch (Exception e) {
			logger.error("productionInformationDao.queryRedbookSourceFilterCount throws exception: ", e);
			AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_REDBOOK, ReturnCodeMessage.FAIL_DEP_DB_REDBOOK_MESSAGE);
		}
		
		List<Map<String, String>> sourceTecList = new ArrayList<Map<String,String>>();
		try {
			sourceTecList = productionInformationDao.queryTeclineSourceFilterCount(brand, growthPlays, source, date, keyword);
		} catch (Exception e) {
			logger.error("productionInformationDao.queryTeclineSourceFilterCount throws exception: ", e);
			AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_TECHLINE, ReturnCodeMessage.FAIL_DEP_DB_TECHLINE_MESSAGE);
		}

		int sourceTotal = 0;
		if (sourceTecList.size() > 0) {
			sourceTotal = sourceTecList.get(0).get("num")==null ? 0 : Integer.parseInt(sourceTecList.get(0).get("num"));
		} else {
			Map<String,String> tecMap = new HashMap<String,String>();
			tecMap.put("source", "2");
			tecMap.put("name", "Techline");
			tecMap.put("num", "0");
			sourceTecList.add(tecMap);
		}
		
		if (sourceRedList.size() > 0) {
			sourceTotal = sourceTotal + (sourceRedList.get(0).get("num")==null ? 0 : Integer.parseInt(sourceRedList.get(0).get("num")));
		} else {
			Map<String,String> redMap = new HashMap<String,String>();
			redMap.put("source", "1");
			redMap.put("name", "Redbooks");
			redMap.put("num", "0");
			sourceRedList.add(redMap);
		}
		
		List<Map<String,String>> allSourceList = new ArrayList<Map<String,String>>();
		Map<String,String> sourceTotalMap = new HashMap<String,String>();
		sourceTotalMap.put("source", "0");
		sourceTotalMap.put("name", "All Sources");
		sourceTotalMap.put("num", String.valueOf(sourceTotal));
		
		allSourceList.add(sourceTotalMap);
		allSourceList.addAll(sourceRedList);
		allSourceList.addAll(sourceTecList);
		fileterResultsMap.put("sourceFilter", allSourceList);
	   
		//get date 2016.8.30 16:52
		List<Map<String,String>> dateTecList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> dateRedList = new ArrayList<Map<String,String>>();
		if ("1".equals(source)) {
			try {
				dateRedList=  productionInformationDao.queryRedbookDateFilterCount(brand, growthPlays, source, date, keyword);
			} catch (Exception e) {
				logger.error("productionInformationDao.queryRedbookDateFilterCount throws exception: ", e);
				AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_REDBOOK, ReturnCodeMessage.FAIL_DEP_DB_REDBOOK_MESSAGE);
			}
		} else if ("2".equals(source)){
			try {
				dateTecList=  productionInformationDao.queryTeclineDateFilterCount(brand, growthPlays, source, date, keyword);
			} catch (Exception e) {
				logger.error("productionInformationDao.queryTeclineDateFilterCount throws exception: ", e);
				AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_TECHLINE, ReturnCodeMessage.FAIL_DEP_DB_TECHLINE_MESSAGE);
			}
		} else {
			try {
				dateRedList=  productionInformationDao.queryRedbookDateFilterCount(brand, growthPlays, source, date, keyword);
			} catch (Exception e) {
				logger.error("productionInformationDao.queryRedbookDateFilterCount throws exception: ", e);
				AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_REDBOOK, ReturnCodeMessage.FAIL_DEP_DB_REDBOOK_MESSAGE);
			}
			
			try {
				dateTecList = productionInformationDao.queryTeclineDateFilterCount(brand, growthPlays, source, date, keyword);
			} catch (Exception e) {
				logger.error("productionInformationDao.queryTeclineDateFilterCount throws exception: ", e);
				AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_TECHLINE, ReturnCodeMessage.FAIL_DEP_DB_TECHLINE_MESSAGE);
			}
		}
		
		List<Map<String,String>> dateList=new ArrayList<Map<String,String>>();
		Map<String,String> dateTotalMap=new HashMap<String,String>();
		dateTotalMap.put("date", "0");
		dateTotalMap.put("name", "All Dates");
		dateList.add(dateTotalMap);
		
		Map<String, String> tecMap = null;
		Map<String, String> redMap = null;
		if (dateTecList.size() > 0) {
			tecMap = dateTecList.get(0);
		}
		if(dateRedList.size() > 0) {
			redMap = dateRedList.get(0);
		}

		Map<String,String> dateSortMap = new HashMap<String,String>();
		dateSortMap.put("1", "Last Month");
		dateSortMap.put("2", "Last 3 Months");
		dateSortMap.put("3", "Last 6 Months");
		dateSortMap.put("4", "Last Year");
		dateSortMap.put("5", "Last 5 Years");
		dateSortMap.put("6", "Greater than 5 Years");
		int dateTotal = 0;
		for (int i = 1; i <= 6; i++) {
			Map<String, String> allDateMap = new HashMap<String, String>();
			allDateMap.put("date", String.valueOf(i));
			allDateMap.put("name", dateSortMap.get(String.valueOf(i)));
			Integer t = (tecMap==null ? 0 : Integer.parseInt(tecMap.get(String.valueOf(i)))) + (redMap==null ? 0 : Integer.parseInt(redMap.get(String.valueOf(i))));
			if (i ==5 || i==6) {
				dateTotal = dateTotal + t;
			}
			allDateMap.put("num", t.toString());
			dateList.add(allDateMap);
		}
		dateList.get(0).put("num",String.valueOf(dateTotal));
		fileterResultsMap.put("dateFilter", dateList);
		return fileterResultsMap;
	}
	
	public void insertUseful(Map<String,String> param)throws Exception {
		String idEntity = param.get("idEntity");
		String entityType = param.get("entityType");
		String idUser = param.get("idUser");
		String flag = param.get("flag");
	  
		//useful
	    if(flag.equalsIgnoreCase("1")) {
	    	String camss = param.get("CAMSS") == null ? "" : param.get("CAMSS");
	    	String keyword = param.get("KeyWords") == null ? "" : param.get("KeyWords");
	    	String brand = param.get("Brand") == null ? "" : param.get("Brand");
	    	String source = param.get("Source") == null ? "" : param.get("Source");
	    	String datelastUpdate = param.get("DateLastUpdated") == null ? "" : param.get("DateLastUpdated");
	    			
	    	productionInformationDao.insertuseful(idEntity, entityType, idUser,camss,keyword,brand,source,datelastUpdate);
	    	
        } else if(flag.equalsIgnoreCase("0")) { //uncheck
	    	productionInformationDao.deleteUseful(idEntity, entityType, idUser);
	    }
	}

	class ProductFilterThread extends Thread { 
		String brand;
		String[] growthPlays;
		String source;
		String date;
		String keyword;
		CountDownLatch count;
		List<Map<String, String>> result;
		
	    public ProductFilterThread(String brand,String[] growthPlays,String source,String date,String keyword,  CountDownLatch count, List<Map<String, String>> result){  
	        this.brand = brand; 
	        this.growthPlays = growthPlays;
	        this.source = source;
	        this.date = date;
	        this.keyword = keyword;
	        this.count = count;
	        this.result = result;
	    }  
	    
	    public void run(){  
	        if (source.equals("1")) {
	        	try {							
	        		List<Map<String, String>> res = productionInformationDao.queryRedbookBrandFilterCount(brand, growthPlays, source, date, keyword);
	        		this.result.addAll(res);
				} catch (Exception e) {
					logger.error("productionInformationDao.queryRedbookBrandFilterCount throws exception: ", e);
//					AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_REDBOOK, "Some error occurred in the system, please contact the administrator. ErrorCode:" + ReturnCodeMessage.FAIL_DEP_DB_REDBOOK);
				} finally{
					this.count.countDown();
				}
	        } else if (source.equals("2")) {
	        	try {
	        		List<Map<String, String>> res = productionInformationDao.queryTeclineBrandFilterCount(brand, growthPlays, source, date, keyword);
	        		this.result.addAll(res);
				} catch (Exception e) {
					logger.error("productionInformationDao.queryTeclineBrandFilterCount throws exception: ", e);
//					AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_TECHLINE, "Some error occurred in the system, please contact the administrator. ErrorCode:" + ReturnCodeMessage.FAIL_DEP_DB_TECHLINE);
				} finally{
					this.count.countDown();
				}
	        }
	    }  
	}
	
	class ProductInforThread extends Thread{ 
		String brand;
		String source;
		String date;
		String[] growthPlays;
		String keyword;
		String idUser;
		CountDownLatch count;
		List<Map<String, String>> result;
		
	    public ProductInforThread(String brand,String[] growthPlays,String source,String date,String keyword, String idUser, CountDownLatch count, List<Map<String, String>> result){  
	        this.brand = brand; 
	        this.source = source;
	        this.date = date;
	        this.growthPlays = growthPlays;
	        this.keyword = keyword;
	        this.idUser = idUser;
	        this.count = count;
	        this.result = result;
	    }  
	    
	    public void run(){  
	        if (source.equals("1")) {
	        	try {
	        		List<Map<String, String>> res = productionInformationDao.queryProductionRedbookResults(brand, growthPlays, "1", date, keyword, idUser);
	        		this.result.addAll(res);
				} catch (Exception e) {
//					AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_REDBOOK, "Some error occurred in the system, please contact the administrator. ErrorCode:" + 
//													ReturnCodeMessage.FAIL_DEP_DB_REDBOOK);
				} finally{
					this.count.countDown();
				}
	        } else if (source.equals("2")) {
	        	try {
	        		List<Map<String, String>> res = productionInformationDao.queryProductionTeclineResults(brand, growthPlays, "2", date, keyword, idUser);
	        		this.result.addAll(res);
				} catch (Exception e) {
//					AssistVoLocal.getAssistVo().put(ReturnCodeMessage.FAIL_DEP_DB_TECHLINE, "Some error occurred in the system, please contact the administrator. ErrorCode:" + 
//													ReturnCodeMessage.FAIL_DEP_DB_TECHLINE);
				} finally{
					this.count.countDown();
				}
	        }
	    }  
	}
}
