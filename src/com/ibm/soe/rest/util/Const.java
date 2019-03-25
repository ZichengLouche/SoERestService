package com.ibm.soe.rest.util;

import java.util.HashMap;
import java.util.Map;

public class Const {
	public static final int STATUS_NOT_START = 0;
	public static final int STATUS_IN_PROGRESS = 1;
	public static final int STATUS_COMPLETED = 2;
	
	public static Map<String,String> SGCODEMap=new HashMap<String,String>();
	public static Map<String,String> repotypeMap=new HashMap<String,String>();
	
	public static String key="soe)(*RS";
	
	public static void initSGCode(String repotype) {
		String[] repotypeArr=repotype.split(",");
		
		for(int i=0;i<repotypeArr.length;i++) {
			repotypeMap.put(repotypeArr[i], repotypeArr[i]);
		}
		
	}
	
	public static final String  lastMonth = "1";
	public static final String  last3Month = "2";
	public static final String  last6Month = "3";
	public static final String  lastYear = "4";
	public static final String  last5Year = "5";
	public static final String  greater5Year = "6";
	
	
}
