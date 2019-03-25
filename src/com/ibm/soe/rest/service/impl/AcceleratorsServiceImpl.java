package com.ibm.soe.rest.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.soe.rest.dao.AcceleratorsDao;
import com.ibm.soe.rest.service.AcceleratorsService;

@Service
public class AcceleratorsServiceImpl implements AcceleratorsService {
	
	@Autowired
	private AcceleratorsDao acceleratorsDao;
	
	public List<Map<String, String>> getInitIndustry() {
		return acceleratorsDao.getInitIndustry();
	}

	public List<Map<String, String>> getInitDocType() {
		return acceleratorsDao.getInitDocType();
	}
	
}
