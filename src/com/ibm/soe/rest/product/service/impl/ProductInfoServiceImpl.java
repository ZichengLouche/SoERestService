package com.ibm.soe.rest.product.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.product.dao.RedBookDao;
import com.ibm.soe.rest.product.service.ProductInfoService;

@Service
@Transactional
public class ProductInfoServiceImpl implements ProductInfoService {
	
	private static Logger logger = Logger.getLogger(ProductInfoServiceImpl.class);
	
	@Autowired
	private RedBookDao redBookDao;

	@Transactional(rollbackFor=Exception.class)
	public int saveRedBooks(List<Map<String, String>> redBooks)
			throws Exception {
		redBookDao.deleteRedBooks();
		for (Map<String, String> redBook : redBooks) {
			redBookDao.saveRedBook(redBook);
		}
		logger.info("saveRedBooks method done.");
		return redBooks.size();
	}

}
