package com.ibm.soe.rest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestUtil {

	public static Map<String, Object> handleResult(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (obj != null) {
			map.put("resultCode", "1");
			map.put("result", obj);
		}
		return map;
	}
	
	public static Map<String, Object> handleError(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", "0");
		map.put("result", obj);
		return map;
	}
	
	// Andy 2016.1.15 16:14
	public static String constructReg(List<Map<String, String>> laptopOnlys) {
		List<String> prefixList = new ArrayList<String>();
		List<String> suffixList = new ArrayList<String>();
		for (Map<String, String> obj : laptopOnlys) {
			String location = obj.get("location");
			if (location.equals("0")) {
				suffixList.add(obj.get("docType"));
			} else if (location.equals("1")) {
				prefixList.add(obj.get("docType"));
			}
		}

		StringBuilder suffixReg = new StringBuilder();
		suffixReg.append(".+(");
		int size = suffixList.size();
		for (int i = 0; i< size; i++) {
			suffixReg.append("\\").append(suffixList.get(i));
			if (i != size -1) {
				suffixReg.append("|");
			}
		}
		suffixReg.append(")$");
		
		StringBuilder prefixReg = new StringBuilder();
		prefixReg.append("^(http[s]{0,1}://(");
		size = prefixList.size();
		for (int i = 0; i< size; i++) {
			prefixReg.append(prefixList.get(i));
			if (i != size -1) {
				prefixReg.append("|");
			}
		}
		prefixReg.append(")).+");
		
		String reg;
		if (prefixList.size() == 0) {
			reg = suffixReg.toString();
		} else {
			if (suffixList.size() == 0) {
				reg = prefixReg.toString();
			} else {
				reg = "(" + suffixReg.toString() + ")" + "|" + "(" + prefixReg.toString() + ")";
			}
		}
		return reg;
	}
}
