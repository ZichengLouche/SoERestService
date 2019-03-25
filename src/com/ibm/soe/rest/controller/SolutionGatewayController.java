package com.ibm.soe.rest.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.CharUtils;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.soe.rest.service.CommFunctionService;
import com.ibm.soe.rest.service.SolutionGatewayService;
import com.ibm.soe.rest.util.Const;
import com.ibm.soe.rest.util.DescryptCoder;
import com.ibm.soe.rest.util.HttpClientUtil;
import com.ibm.soe.rest.util.MailUtil;

@Controller
@RequestMapping("sysnSG")
public class SolutionGatewayController {
	private static Logger logger = Logger.getLogger(SolutionGatewayController.class);
	
	public final static String RET_SUCCESS_CODE="D00";
	
	@Value("${SG_FILETER}")
	private String SG_FILETER;

	@Value("${SG_GETASSETDETAIL}")
	private String SG_GETASSETDETAIL;
	
	@Value("${SG_CONSTRUCT_URL}")
	private String SG_CONSTRUCT_URL;
	
	@Value("${repotype}")
	private String repotype;
	
	@Value("${DATA_Fail_email}")
	private String emailList;
	
	@Value("${fromEmail}")
	private String fromEmail;
	
	@Value("${hostName}")
	private String hostName;
	
	@Value("${password}")
	private String password;
	
	@Value("${TABLE_SOLUTION_PART_TEMP}")
	private String TABLE_SOLUTION_PART_TEMP;
	
	@Autowired
	private SolutionGatewayService solutionGateWayService;
	
	@Autowired @Qualifier("threadCommFunctionService")
	private CommFunctionService commFunctionService;
	
	@Autowired
	private MailUtil mailUtil;
	
	
	@RequestMapping(value = "/getData", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody void getData(@RequestBody Map<String, String> param) {
		String name = param.get("name");
		String pwd = param.get("password");
		if(name==null || pwd==null) {
			return;
		}
		
		String key = Const.key;
		String skey = DescryptCoder.decrypt(password, key);
		if(!pwd.equals(skey)) {
			sendFailedEmail("Manual execute data synchronization password wrong");
			return;
		}
		
		Const.initSGCode(repotype);
		commFunctionService.truncateTableData(TABLE_SOLUTION_PART_TEMP);
		getBluePrintData(name, param);
		getBSUData(name, param);
		copyDataToSolutionPart(param);
	}
	
	private void getBSUData(String name,Map<String, String> param) {
		HttpClient httpClient = null;
		try {
			logger.info("Manual execute bsu data synchronization:"+ name);

			Map<String,Map<String, String>> bsu_listIds = solutionGateWayService.getBSUListIds();
			List<Map<String, String>> bsuToolIdList = solutionGateWayService.getBSUToolID();
			Map<String,Map<String, String>> bsuOfferingMap=solutionGateWayService.getSolutionBsuOID();	
			
			// Andy 2016.6.7 22:04
			if(bsuToolIdList != null && !bsuToolIdList.isEmpty()) {
				Map<String, Map<String,String>> fourLevelCascadeIdsMapForBSU = solutionGateWayService.getFourLevelCascadeIds(2);
				for (Map<String, String> toolIdMap : bsuToolIdList) {
					String toolID = toolIdMap.get("ToolID");
					String IDBSU = toolIdMap.get("IDBSU");
					if (toolID != null && !toolID.trim().equals("")) {
						Map<String, String> bsuListMap = bsu_listIds.get(IDBSU);
						Map<String, String> offeringMap = bsuOfferingMap.get(IDBSU);
						
						Set<String> OID = offeringMap.keySet();
						logger.info("Manual execute data synchronization:"+SG_FILETER+"******************************"+OID.size());
						for (String  o :OID) {
							httpClient = HttpClientUtil.initHttpClient();
							HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
							RestTemplate template = new RestTemplate(httpFactory);
							HttpHeaders headers = new HttpHeaders();
							headers.setContentType(MediaType.APPLICATION_JSON);
							Map<String, String> map = new HashMap<String, String>();
							map.put("toolID", toolID);
							map.put("spec version", "");
							map.put("oid", o);
							HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
							String result = template.postForObject(SG_FILETER, request,String.class);
							logger.info("Manual-----------OID-"+o+"------"+offeringMap.get(o)+"-----");
							List<Map<String, String>> resultMap = parseData(result, offeringMap.get(o), bsuListMap, new HashMap<String,String>(), new ArrayList<String>());
							boolean ret = solutionGateWayService.processSGData(resultMap, 2, fourLevelCascadeIdsMapForBSU);
							if(!ret) {
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info("Manual Call API and Parse data Exception",e);
			logger.error("Manual Call API and Parse data Exception",e);
			sendFailedEmail("Manual Call API and Parse data Exception"+e.getMessage());
			
		}
	}
	
	private void getBluePrintData(String name, Map<String, String> param) {
		HttpClient httpClient = null;
		try {
			logger.info("Manual execute blueprint data synchronization:"+ name);
			//get list id
			Map<String,String> blueprintListMap=new HashMap<String,String>();
			List<String> blueprint_listIds = solutionGateWayService.getBLUEPRINTListIds();
			
			Map<String,String> blueprintexceptionListMap=new HashMap<String,String>();
			List<String> blueprint_exception_listIds = solutionGateWayService.getBLUEPRINTExceptionListIds();
			
			List<String> blueprint_listIds_0 = solutionGateWayService.getBLUEPRINT0ListIds();
			
			for(String lisid: blueprint_listIds) {
				blueprintListMap.put(lisid, lisid);
			}
			
			for(String lisid: blueprint_exception_listIds) {
				blueprintexceptionListMap.put(lisid, lisid);
			}

			List<String> offeringIDs = solutionGateWayService.getOfferingOID();
			logger.info("Manual execute data synchronization:"+SG_FILETER+"******************************"+offeringIDs.size());
			
			// Andy 2016.6.7 22:04
			if(offeringIDs != null && !offeringIDs.isEmpty()) {
				Map<String, Map<String,String>> fourLevelCascadeIdsMapForBlueprint = solutionGateWayService.getFourLevelCascadeIds(1);
				for (String  o : offeringIDs) {
					httpClient = HttpClientUtil.initHttpClient();
					HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
					RestTemplate template = new RestTemplate(httpFactory);
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("toolID", "iSolutions");
					map.put("spec version", "");
					map.put("oid", o);
					HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
					String result = template.postForObject(SG_FILETER, request, String.class);
					
					List<Map<String, String>> resultMap = parseData(result, o, blueprintListMap, blueprintexceptionListMap, blueprint_listIds_0);
					boolean ret = solutionGateWayService.processSGData(resultMap, 1, fourLevelCascadeIdsMapForBlueprint);
					if(!ret) {
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.info("Manual Call API and Parse data Exception",e);
			logger.error("Manual Call API and Parse data Exception",e);
			sendFailedEmail("Manual Call API and Parse data Exception"+e.getMessage());
		}
	}
	
	@RequestMapping(value = "/copyData", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody void copyDataToSolutionPart(@RequestBody Map<String, String> param) {
		String name=param.get("name");
		String pwd=param.get("password");
		String type = param.get("type");
		Integer typeInt;
		if(type==null || type.equals("")){
			typeInt = 0;
		} else {
			typeInt = Integer.valueOf(type);
		}
		if(name==null||pwd==null) {
			return;
		}
		
		String key=Const.key;
		String skey=DescryptCoder.decrypt(password, key);
		if(!pwd.equals(skey)) {
			sendFailedEmail("Manual execute data synchronization password wrong");
			return;
		}
		
		logger.info("Manual execute data synchronization:"+name);
		boolean flag;
		try {
			flag = solutionGateWayService.copyDataToSolutionpart(typeInt);
			if(!flag) {
				logger.error("Manual Copy data from the temp table to the SS table Exception");
				sendFailedEmail("Manual Copy data from the temp table to the SS table Exception");
			} else {
				logger.info("Manual Copy data from the temp table to the solutionpart table success");
			}
		} catch (Exception e) {
			logger.error("Manual Copy data from the temp table to the SS table Exception",e);
			sendFailedEmail("Manual Copy data from the temp table to the SS table Exception"+e.getMessage());
		}
	}

	private void sendFailedEmail(String msg) {
		String[] emaillist=emailList.split(",");
		
		for(int i=0;i<emaillist.length;i++) {
			mailUtil.sendMail(emaillist[i], fromEmail, "SG Data synchronization exception", hostName+msg, null);
		}
	}
	
	
	/**
	 * 解析json
	 * @param json
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String,String>> parseData(String json,String offeringid, Map<String,String> listMap, Map<String,String> exceptionMap, List<String> description_listIds_0) 
																													throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapTool = new ObjectMapper();
	    Map<?,?>  resultMap = mapTool.readValue(json, Map.class); 
	    String retStatus = (String) resultMap.get("rc");
	    
		List<Map<String,String>> solutionpartList = new ArrayList<Map<String,String>>();
	    if(RET_SUCCESS_CODE.equals(retStatus)){
	    	 Map<String,Object> item =(Map<String, Object>)resultMap.get("item data");
	    	 Map<String,Object> port = (Map<String,Object>)item.get("PortfolioItem");
	    	 
	    	 //attributes
	    	 Map<String,Object> attributes = (Map<String,Object>)port.get("attributes");
	    	 List <Map<String,Object>> attributelist =(List <Map<String,Object>>) attributes.get("attribute");
	    	 for(Map<String,Object> listItem : attributelist) {
	    		 String name= (String) listItem.get("name");
	    		 String content= (String) listItem.get("content").toString().trim();
	    		 if(description_listIds_0.contains(name) && !content.equals("")) {
						Map<String, String> itemMap = new HashMap<String, String>();
						itemMap.put("listid", "0");
						itemMap.put("Description", content);
						itemMap.put("Name", name);
						itemMap.put("lastModified",	"");
						itemMap.put("url", "");
						itemMap.put("idOffering", offeringid);
						itemMap.put("assetID", null);
						solutionpartList.add(itemMap);
	    		 }
	    	 }
	    	 
	    	 //boms
	    	 Map<String,Object> boms = (Map<String, Object>) port.get("boms");
	    	 List <Map<String,Object>> bomlist = (List <Map<String,Object>>) boms.get("bom");
	    	 for(Map<String,Object> listItem : bomlist) {
	    		String listid = (String) listItem.get("listid");
	    		if(listMap.containsKey(listid)){
	    			Map<String, String> assetIdMap = new HashMap<String, String>();
		    		List <Map<String,Object>> itemlist = (List <Map<String,Object>>)listItem.get("item");
		    		
		    		for(Map<String,Object> itemnode : itemlist) {
						Integer stat = (Integer) itemnode.get("repotype");

						if (Const.repotypeMap.containsKey(String.valueOf(stat))) {
							String description = (String) itemnode.get("assetDescription");
							String assetID = (String) itemnode.get("assetid");
							if (assetID == null || assetID.trim().equals("") || assetIdMap.containsKey(assetID.trim())) {
								continue;
							} else {
								assetIdMap.put(assetID.trim(), assetID);
								if(String.valueOf(stat).equals("1")) {
									if(exceptionMap.containsKey(listid)) {
										String title = (String) itemnode.get("name");
										String oid = (String) itemnode.get("oid");
										if(oid == null || oid.trim().equals("")){
											continue;
										} else {
											String url = SG_CONSTRUCT_URL  + oid;
											Map<String, String> itemMap = new HashMap<String, String>();
											itemMap.put("listid", listid);
											itemMap.put("Description", description);
											itemMap.put("Name",title);
											itemMap.put("lastModified",	"");
											itemMap.put("url", url);
											itemMap.put("idOffering", offeringid);
											itemMap.put("assetID", assetID);
											solutionpartList.add(itemMap);
										}
									}
								} else {
									if (!assetID.startsWith("AHUB")) {
										try {
											HttpClient httpClient = HttpClientUtil.initHttpClient();
											HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
											RestTemplate template = new RestTemplate(httpFactory);
											HttpHeaders headers = new HttpHeaders();
											headers.setContentType(MediaType.APPLICATION_JSON);
											Map<String, String> map = new HashMap<String, String>();
											map.put("assetID", assetID);
											HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
											String result = template.postForObject(SG_GETASSETDETAIL, request, String.class);
											
											Map<?,?>  detailMap = mapTool.readValue(result, Map.class);
											Map<String,Object> assetData = (Map<String, Object>)detailMap.get("asset data");
											String lastModified = (String) assetData.get("lastModified");
											List<Map<String, Object>> linklist = (List<Map<String, Object>>) assetData.get("link");
											if (linklist != null) {
												for(Map<String,Object> link : linklist) {
													String url = (String) link.get("url");
													String title = (String) link.get("title");
													if (url != null) {
														Map<String, String> itemMap = new HashMap<String, String>();
														itemMap.put("listid", listid);
														itemMap.put("Description", description);
														itemMap.put("Name",title);
														itemMap.put("lastModified",	lastModified);
														itemMap.put("url", url);
														itemMap.put("idOffering", offeringid);
														itemMap.put("assetID", assetID);
														solutionpartList.add(itemMap);
													}
												}
											}
										} catch (Exception e) {
											logger.info("Manual Call API and Parse data Exception",e);
											logger.error("Manual Call API and Parse data Exception",e);
											sendFailedEmail("Manual Call API and Parse data Exception"+e.getMessage());
										}
									} else {
										String name = (String) itemnode.get("name");
										Map<String, Object> asset = (Map<String, Object>) itemnode.get("asset");
										if (asset == null || description == null || name == null) {
											continue;
										} else {
											String lastModified = (String) asset.get("lastModified");
											List<Map<String, Object>> linklist = (List<Map<String, Object>>) asset.get("link");
											if (linklist != null) {
												Map<String, Object> linkMap = linklist.get(0);
												String url = (String) linkMap.get("url");
												if (url == null) {

												} else {
													Map<String, String> itemMap = new HashMap<String, String>();
													itemMap.put("listid", listid);
													itemMap.put("Description", description);
													itemMap.put("Name",name);
													itemMap.put("lastModified",	lastModified);
													itemMap.put("url", url);
													itemMap.put("idOffering", offeringid);
													itemMap.put("assetID", assetID);
													solutionpartList.add(itemMap);
												}
											}
										}
									}
								}
							}
						}
					}
	    		}
	    	 }
	    }
	    return solutionpartList;
	}
	
	
	 public static void check4Byte(String name) throws UnsupportedEncodingException {  
	        String nickName = name;  
	  
	        byte[] t = nickName.substring(0, 1).getBytes("UTF-8");  
	        for (byte tt : t) {  
	            System.out.println(tt);  
	        }  
	        byte[] t1 = nickName.getBytes("UTF-8");  
	        for (int i = 0; i < t1.length;) {  
	            byte tt = t1[i];  
	            if (CharUtils.isAscii((char) tt)) {  
	                byte[] ba = new byte[1];  
	                ba[0] = tt;  
	                i++;  
	                String result = new String(ba);  
	                System.out.println("1个字节的字符");  
	                System.out.println("字符为：" + result);  
	            }  
	            if ((tt & 0xE0) == 0xC0) {  
	                byte[] ba = new byte[2];  
	                ba[0] = tt;  
	                ba[1] = t1[i+1];  
	                i++;  
	                i++;  
	                String result = new String(ba);  
	                System.out.println("2个字节的字符");  
	                System.out.println("字符为：" + result);  
	            }  
	            if ((tt & 0xF0) == 0xE0) {  
	                byte[] ba = new byte[3];  
	                ba[0] = tt;  
	                ba[1] = t1[i+1];  
	                ba[2] = t1[i+2];  
	                i++;  
	                i++;  
	                i++;  
	                String result = new String(ba);  
	                System.out.println("3个字节的字符");  
	                System.out.println("字符为：" + result);  
	            }  
	            if ((tt & 0xF8) == 0xF0) {  
	                byte[] ba = new byte[4];  
	                ba[0] = tt;  
	                ba[1] = t1[i+1];  
	                ba[2] = t1[i+2];  
	                ba[3] = t1[i+3];  
	                i++;  
	                i++;  
	                i++;  
	                i++;  
	                String result = new String(ba);  
	                System.out.println("4个字节的字符");  
	                System.out.println("字符为：" + result);  
	            }  
	        }  
	    }  
	
}
