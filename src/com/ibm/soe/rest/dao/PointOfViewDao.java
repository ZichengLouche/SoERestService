package com.ibm.soe.rest.dao;

import java.util.List;
import java.util.Map;

public interface PointOfViewDao {

	public List<Map<String, String>> getPointOfViews(String uid) throws Exception;

}
