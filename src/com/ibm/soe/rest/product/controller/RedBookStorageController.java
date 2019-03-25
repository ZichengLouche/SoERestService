package com.ibm.soe.rest.product.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.soe.rest.product.service.ProductInfoService;
import com.ibm.soe.rest.util.RestUtil;

@Controller
@RequestMapping("redBookStorage")
public class RedBookStorageController {

	private static Logger logger = Logger.getLogger(RedBookStorageController.class);
	
	@Autowired
	private ProductInfoService productInfoService;

	@RequestMapping(value = "/saveRedBooks", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveRedBooks(@RequestBody Map<String, String> param) {
		int savedCount = 0;
		
		try {
			//xmlFilePath
			String xmlPath = param.get("xml");
			List<Map<String, String>> redbooks = this.parseXmlData(xmlPath);
			if (!redbooks.isEmpty()) {
				savedCount = productInfoService.saveRedBooks(redbooks);
			}
			
		} catch (Exception e) {
			logger.error("/saveRedBooks throws exception: ", e);
			return RestUtil.handleError(e.getMessage());
		}
		
		return RestUtil.handleResult(savedCount);
	}
	
	private List<Map<String, String>> parseXmlData(String xmlPath) throws Exception {
		SAXReader reader = new SAXReader();
		List<Map<String, String>> redbooks = new ArrayList<Map<String, String>>();
		try {
			Document document = reader.read(xmlPath);
			@SuppressWarnings("unchecked")
			List<Element> redbookElements = document.getRootElement().elements("IBMRedbooksDoc");//.selectNodes("IBMRedbooksDocList/IBMRedbooksDoc");
			for (Element redbookElement : redbookElements) {
				Map<String, String> map = new HashMap<String, String>();
				Iterator<?> it = redbookElement.elementIterator();
				while (it.hasNext()) {
					Element fieldElement = (Element) it.next();
					String fieldName = fieldElement.getName();
					String fieldValue = fieldElement.getTextTrim();
					String previousValue = map.get(fieldName);
					if (previousValue == null) {
						map.put(fieldName, fieldValue);
					} else {
						map.put(fieldName, previousValue + ", " + fieldValue);
					}
					
				}
				redbooks.add(map);
			}
		} catch(DocumentException ex) {
			logger.error("/saveRedBooks/parseXmlData error.", ex);
		}
		return redbooks;
	}

}
