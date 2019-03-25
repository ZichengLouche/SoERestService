package com.ibm.soe.rest.product.dao;

import java.util.Map;

public interface RedBookDao {

	public long saveRedBook(Map<String, String> redBook) throws Exception;	
	
	public void deleteRedBooks() throws Exception;

}
