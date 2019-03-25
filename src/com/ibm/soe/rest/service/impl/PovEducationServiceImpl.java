package com.ibm.soe.rest.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.soe.rest.dao.PovEducationDao;
import com.ibm.soe.rest.service.PovEducationService;

@Service
@Transactional
public class PovEducationServiceImpl implements PovEducationService{

	@Autowired
	private PovEducationDao povEducationDao;

	@Override
	public List<Map<String, Object>> getPovMainResults(String idUser) throws Exception {
		List<Map<String, Object>> moduleList = povEducationDao.getPovModuleList(idUser);
		for (Map<String, Object> module : moduleList) {
			module.put("topicList", povEducationDao.getPovTopicList(idUser, (String) module.get("idModule")));
		}
		return moduleList;
	}

	@Override
	public Map<String, Object> getPovTopicDetails(String idTopic, String idState) throws Exception {
		Map<String, Object> topicDetails = new HashMap<String, Object>();
		if (idState == null || "".equals(idState)) {
			topicDetails.put("notes", "");
		} else {
			topicDetails.put("notes", povEducationDao.getPovNotes(idState));
		}
		
		topicDetails.put("resources", povEducationDao.getPovResourceList(idTopic));
		return topicDetails;
	}

	@Override
	public String getPovResourceContent(String idResource) throws Exception {
		return povEducationDao.getPovResourceContent(idResource);
	}
	
	public Map<String, String> getModuleState(String idUser, String idModule) throws Exception {
		return povEducationDao.getModuleState(idUser, idModule);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void checkModule(String idUser, String topicStates,//idTopic1:idState1,idTopic2:idState2,...
			String status) throws Exception {
		JSONObject jsonObj = JSONObject.fromObject("{" + topicStates + "}");
		Iterator<?> iterator = jsonObj.keys();
		String idTopic = null, idState = null;
		while (iterator.hasNext()) {
			idTopic = (String) iterator.next();
			idState = jsonObj.getString(idTopic);
			idState = "null".equals(idState) ? null : idState;
			this.checkTopic(idUser, idState, idTopic, status);
		}
		
//		String[] arrayTopicStates = topicStates.split(",");
//		String[] arrayTopicAndState = null;
//		
//		for (String topicAndState : arrayTopicStates) {
//			arrayTopicAndState = topicAndState.split(":");
//			idTopic = arrayTopicAndState[0].replaceAll("\"", "");
//			idState = (arrayTopicAndState.length > 1) ? arrayTopicAndState[1] : null;
//			this.checkTopic(idUser, idState, idTopic, status);
//		}
		
	}

	@Override
	public void checkTopic(String idUser, String idState, String idTopic, String status) throws Exception {
		if ("1".equals(status)) {//DO UNCHECK
			if (idState == null || "".equals(idState)) {
				// do nothing
			} else {
				povEducationDao.editTopicStateInprogress(idState);
			}
		} else if ("2".equals(status)) {//DO CHECK
			if (idState == null || "".equals(idState)) {
				povEducationDao.addTopicStateCompleted(idUser, idTopic);
			} else {
				povEducationDao.editTopicStateCompleted(idState);
			}
		}
	}

	@Override
	public void writeTopicNotes(String idUser, String idState, String idTopic, String notes, String status) throws Exception {
		if (idState == null || "".equals(idState)) {
			povEducationDao.addTopicNotes(idUser, idTopic, notes, status);
		} else {
			povEducationDao.editTopicNotes(idState, notes, status);
		}
	}
	
	@Override
	public List<Map<String, String>> getViewAllNotes(String idUser) throws Exception {
		return povEducationDao.getViewAllNotes(idUser);
	}
	
	@Override
	public List<Map<String, String>> getViewAllNotes(String idUser,String type,String oppID) throws Exception {
		return povEducationDao.getViewAllNotes(idUser,type,oppID);
	}
	
	@Override
	public List<Map<String, String>> getMailSub(String oppID) throws Exception {
		return povEducationDao.getMailSub(oppID);
	}

}
