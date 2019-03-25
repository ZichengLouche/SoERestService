package com.ibm.soe.rest.vo;
/** 
 * @author Andy
 * @create 2016-6-8 17:41 
 */
public class SOEException extends RuntimeException {
	private static final long serialVersionUID = -2823735977135265164L;

	private String code;
	
	public SOEException() {
	}

	public SOEException(String code, String info) {
		super(info);
		this.code = code;
	}
	
	public SOEException(String message) {
		super(message);
	}

	public SOEException(Throwable cause) {
		super(cause);
	}

	public SOEException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public SOEException(String code, String info, Throwable cause) {
		super(info, cause);
		this.code = code;
	}
	

	public String getCode() {
		return code;
	}

}
