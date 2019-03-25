package com.ibm.soe.rest.vo;

import java.util.Map;

/** 
 * @author Andy 2016-8-31 12:03 
 */
public class AssistVoLocal {
	private static final ThreadLocal<Map<String, Object>> ASSISTVO_LOCAL = new ThreadLocal<Map<String, Object>>();
	
	public static void setAssistVo(Map<String, Object> assistVo){
		ASSISTVO_LOCAL.set(assistVo);
	}
	
	public static Map<String, Object> getAssistVo(){
		return ASSISTVO_LOCAL.get();
	}
	
	public static void removeAssistVo(){
		ASSISTVO_LOCAL.remove();
	}
}
