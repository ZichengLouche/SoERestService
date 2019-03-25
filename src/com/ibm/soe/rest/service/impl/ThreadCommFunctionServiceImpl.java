package com.ibm.soe.rest.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.ibm.soe.rest.dao.CommFunctionDao;
import com.ibm.soe.rest.service.CommFunctionService;
import com.ibm.soe.rest.thread.CommFunctionThread;

/** 
 * @author Andy
 * @create 2016-5-27 16:58
 */
@Service("threadCommFunctionService")
public class ThreadCommFunctionServiceImpl implements CommFunctionService {
	private static final Logger log = Logger.getLogger(ThreadCommFunctionServiceImpl.class);
	
	@Resource(name="threadPoolTaskExecutor")
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	@Resource(name="commFunctionService")
	private CommFunctionService commFunctionService;
	
	@Autowired
	private CommFunctionDao commFunctionDao;
	
	
	@Override
	public boolean updateGISCAsset(Map<String, Object> giscAssetMap, List<Map<String, Object>> idIsnRelationshipList, CountDownLatch countDownLatch) {
		CommFunctionThread task = new CommFunctionThread(commFunctionService, giscAssetMap, idIsnRelationshipList, countDownLatch);
		try {
			threadPoolTaskExecutor.execute(task);
		} catch (Exception e) {
			log.error("exportTaskExecutor.execute error " , e );
		}
		
		return true;
	}

	
	
	@Override
	public Map<String, Object> getDropDownListData() {
		return null;
	}

	@Override
	public Map<String, String> getMyProfile(String uid) {
		return null;
	}

	@Override
	public Map<String, String> insertMyProfile(Map<String, String> param) {
		return null;
	}
	
	public Map<String, String> insertLoginHistory(Map<String, String> param) {
		return null;
	}

	@Override
	public Map<String, String> updateMyProfile(Map<String, String> param) {
		return null;
	}

	@Override
	public boolean autoUpdateRedbooks(String table, String filePath) {
		return false;
	}

	@Override
	public boolean truncateTableData(String table) {
		return commFunctionDao.truncateTableData(table);
	}

	@Override
	public boolean autoBatchUpdateRedbooks(String table, List<Element> childElements) {
		return false;
	}

	@Override
	public boolean updateGISCAsset(Map<String, Object> giscAssetMap, List<Map<String, Object>> idIsnRelationshipList) {
		return false;
	}

}
