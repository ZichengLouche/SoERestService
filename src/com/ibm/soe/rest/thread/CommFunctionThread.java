package com.ibm.soe.rest.thread;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import com.ibm.soe.rest.service.CommFunctionService;

/** 
 * @author xmt
 * @create 2016-5-27 16:58
 */
public class CommFunctionThread extends Thread {
	private static final Logger log = Logger.getLogger(CommFunctionThread.class);
	
	@Resource(name="commFunctionService")
	private CommFunctionService commFunctionService;
	
	private Map<String,Object> giscAssetMap; 
	private List<Map<String, Object>> idIsnRelationshipList;
	private CountDownLatch countDownLatch;
	

	public CommFunctionThread(CommFunctionService commFunctionService, Map<String,Object> giscAssetMap, List<Map<String, Object>> idIsnRelationshipList, CountDownLatch countDownLatch){
		this.commFunctionService = commFunctionService;
		this.giscAssetMap = giscAssetMap;
		this.idIsnRelationshipList = idIsnRelationshipList;
		this.countDownLatch = countDownLatch;
	}


	@Override
	public void run() {
		try {
			commFunctionService.updateGISCAsset(giscAssetMap, idIsnRelationshipList);
			countDownLatch.countDown(); 
		} catch (Exception e) {
			log.error("The data synchronize occured exception,idAsset:" + giscAssetMap.get("idAsset") + ",assetName:" + giscAssetMap.get("assetName") + ".Case：" + e.getMessage());
		}
		
	}

}
