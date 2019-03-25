package com.ibm.soe.rest.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.soe.rest.dao.DownloadHistoryDao;
import com.ibm.soe.rest.service.DownloadHistoryService;
@Service
public class DownloadHistoryServiceImpl implements DownloadHistoryService{
	
	@Autowired
	private DownloadHistoryDao downloadHistoryDao;

	@Override
	public String  insertDownloadHistory(Map<String, String> param)
			throws Exception {
		// TODO Auto-generated method stub
		return downloadHistoryDao.insertDownloadHistory(param);
	}

	@Override
	public List<Map<String, String>> getDownloadHistory(
			Map<String, String> param) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateHistoryHasRated(Map<String, String> param)
			throws Exception {
		// TODO Auto-generated method stub
		String idEntity=param.get("idEntity");
		String idUser=param.get("idUser");
		String status=param.get("hasRated");
		String entityType=param.get("entityType");
		 downloadHistoryDao.updateHistoryHasRated(idEntity, idUser, status,entityType);
	}

}
