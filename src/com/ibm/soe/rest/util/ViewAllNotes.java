package com.ibm.soe.rest.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ibm.soe.rest.service.PovEducationService;

@Service
public class ViewAllNotes {
	
	private static Logger logger = Logger.getLogger(ViewAllNotes.class);
	
	@Autowired
	private MailUtil mailUtil;
	
	private String notesFileName = null;
	
	/*Test version*/
	String rootPath = "/opt/IBM/WebSphere/AppServer/profiles/AppSrv02/installedApps/sbybz2159Cell01/IBM_Worklight_project_runtime_soeSOE_MFP_70_3.ear/soe.war/ViewAllNotes"; 
//	String rootPath = "C:/ViewAllNotes"; 
	String accessUrlRoot = "https://lmc3.watson.ibm.com:15027/soe";
		
	/*Prod version*/
//	String rootPath = "/opt/IBM/WebSphere/AppServer/profiles/AppSrv01/installedApps/cv01a105Cell01/IBM_Worklight_project_runtime_soeSOE_MFP_70_1.ear/soe.war/ViewAllNotes";
//	String accessUrlRoot = "https://mobile.us.ibm.com:15044/soe";
		
	public String handleViewAllNotes(List<Map<String, String>> obj, String fromEmail, String toEmail) throws Exception {
				
		List<Map<String, String>> allNotesList = obj;
		String fromMail = fromEmail;
		String toMail = toEmail;
		//String fileName = "POV Education Notes for glcraig_us.html";
		String name = "POV_Education_Notes_for_";
		String nameTmp = "";
		String[] temp = fromMail.split("@");
		nameTmp = temp[0];
		String[] country = temp[1].toString().split("\\u002E");
		String cty = country[0].toString();
		String isSendMailSucc = "true";
		try {
			
			String fileName = name+ nameTmp+ "_" + cty ;
			notesFileName = fileName + ".html";
			
			File path = new File(rootPath+"/");
			if (!path.exists() && !path.isDirectory()){
				System.out.println("Directory is not Exist!");
				path.mkdir();
				//return;
			}
			
			File file = new File(rootPath, notesFileName);
			if (!file.exists()){
				file.createNewFile();
			} else {
				file.delete();
				file.createNewFile();
			}
			
			String fileContent = createMailContextAndSendMail(allNotesList, fromMail, toMail);
			FileWriter fWriter = new FileWriter(rootPath +"/"+ notesFileName);
			fWriter.write(fileContent);
			fWriter.close();
			
			return isSendMailSucc;

		} catch(IOException e){
			isSendMailSucc = "false";
			logger.error("/ViewAllNotes throws exception: ", e);
			e.printStackTrace();
			return isSendMailSucc;
		}		
	}
	
	public String handleViewAllNotes(List<Map<String, String>> obj, List<Map<String, String>> subObj, String fromEmail, String toEmail,String oppID) throws Exception {
		String[] emailConstituent = fromEmail.split("@");
		String[] country = emailConstituent[1].toString().split("\\u002E");
		
		// Andy 2016.10.18 15:21
		notesFileName = "EG_Notes_for_" + oppID + "_" + emailConstituent[0] + "_" + country[0] + ".html";
		
		File path = new File(rootPath + "_EG/");
		if (!path.exists() && !path.isDirectory()){
			System.out.println("Directory is not Exist!");
			path.mkdir();
		}
		
		File file = new File(rootPath + "_EG/", notesFileName);
		if (!file.exists()){
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}
		
		String fileContent = createMailContextAndSendMail(obj, subObj, fromEmail, toEmail);
		
		FileWriter fWriter = new FileWriter(rootPath+"_EG/" + notesFileName);
		fWriter.write(fileContent);
		fWriter.close();
		
		return "true";
	}
	
	public String createMailContextAndSendMail(List<Map<String, String>> obj, String fromMail, String toMail) {
		String fromEmail = fromMail;
		String toEmail = toMail;
		String subject = "Solution Seeker : CAMSS Point of View Notes";
		String msgHtml = "";
		String hostUrl = "";
		String idModuleTemp = "";
		int indexNum = 1;
		
		for (int i = 0; i < obj.size(); i ++){
			Map<String, String> temp = new HashMap<String, String>();
			temp = obj.get(i);
			String idModule = temp.get("idModule").toString();
			String moduleTitle = temp.get("moduleTitle");
			String topicTitle = temp.get("topicTitle");
			String rtNotes = temp.get("rtNotes");
			String noteTemp = "";
			if (i == 0) {
				idModuleTemp  = idModule;
				msgHtml = "<p style='font-weight: bold;'>" + msgHtml + indexNum +". " + moduleTitle + "</p>" 
						+ "<p style='font-weight: bold;'>&nbsp;" + topicTitle + "</p>" + "<div style='padding-left:60px;'><p>" + rtNotes + "</p></div>";
			} else {
				if (idModuleTemp.equals(idModule)){
					noteTemp = "<p style='font-weight: bold;'>&nbsp;" + topicTitle + "</p>" + "<div style='padding-left:60px;'><p>" + rtNotes + "</p></div>";
				} else {
					idModuleTemp = idModule;
					indexNum = indexNum +1;
					msgHtml = msgHtml + "<hr></hr><p style='font-weight: bold;'>" + indexNum +". " + moduleTitle + "</p>" 
							+ "<p style='font-weight: bold;'>&nbsp;" + topicTitle + "</p>" + "<div style='padding-left:60px;'><p>" + rtNotes + "</p></div>";
				}
			}
			msgHtml = msgHtml + noteTemp;
			noteTemp ="";
		}

		String fileUrl = "<hr></hr><br><p>The notes you have taken have been saved.  Tap on the link below to view your notes.</p>"  
						+ "<br><p><a href='"+ accessUrlRoot + "/ViewAllNotes/"+ notesFileName + "'>" + accessUrlRoot + "/ViewAllNotes/"+ notesFileName + "</a></p>";
		
		mailUtil.sendMail(toEmail, fromEmail, subject, fileUrl, hostUrl);
		String header = "<!DOCTYPE HTML><html><head><meta charset='UTF-8'><title>Solution Seeker</title><style type='text/css'>img {width:55%!important}</style></head><body>" + 
				"<p style='text-align: center;font-weight: bold;font-size: 26px;'>CAMSS Point of View Notes</p>";
		String footer = "</body></html>";
		msgHtml = header + msgHtml + footer;
		return msgHtml;
		
	}
	
	public String createMailContextAndSendMail(List<Map<String, String>> obj, List<Map<String, String>> subObj, String fromEmail, String toEmail) {
		String clientName = subObj.get(0).get("clientName");
		String salesConnectID = subObj.get(0).get("idSalesConnect");
		String description = subObj.get(0).get("description");
		String oppName = subObj.get(0).get("oppName");
		
		String subject = "TeamSD Next <" + clientName + "> Opportunity Notes for SalesConnect Opportunity ID <" + salesConnectID + ">";
		String msgHtml = "<p style='font-weight: bold; font-style:italic'>Client Name: " + clientName + "</p>" + 
						 "<p style='font-weight: bold; font-style:italic'>SalesConnect Opportunity ID: " + salesConnectID + "</p>" + 
						 "<p style='font-weight: bold; font-style:italic'>Opportunity Name: " + oppName + "</p>" + 
						 "<p style='font-weight: bold; font-style:italic'>Opportunity Comments: " + description + "</p>";

		String hostUrl = "";
		
		for (int i = 0; i < obj.size(); i ++){
			Map<String, String> temp = obj.get(i);
			String idkey = temp.get("idKey").toString();
			String category = temp.get("category");
			String Name = temp.get("Name");
			String rtNotes = temp.get("rtNotes");
			String type = temp.get("type");
			
			if(null == rtNotes || "null".equals(rtNotes)){
				rtNotes = "";
			}
			
			if(null != category && !"null".equals(category)){
				if("1".equals(category)){
					category = "Yes";
				}else if("2".equals(category)){
					category = "No";
				}else{
					category = "Don’t know";
				}
			}else{
				category = "";
			}
			
			if("ACT".equals(type)){
				if(i > 0){
					msgHtml += "<hr/>";					
				}
				msgHtml += "<p style='font-weight: bold;'><h2>" + Name + "</h2></p><p style='font-weight: bold;font-style:italic'>General Notes:</p>" + "<p>" + rtNotes + "</p>";
				
			} else if ("QA".equals(type)){
				msgHtml += "<p style='font-weight: bold;font-style:italic;margin-left:40px;'>" + Name + "</p>" + 
						   "<p style='margin-left:40px;'>" + rtNotes + category + "</p>";
				
			} else {
				msgHtml += "<p style='font-weight: bold;'><h3>" + Name + "</h3></p><p style='font-weight: bold;font-style:italic;margin-left:40px;'>General Notes:</p>" + 
						   "<p style='margin-left:40px;'>" + rtNotes + "</p>";
			}
		}

		String fileUrl = "<hr></hr><br><p>The notes you have taken have been saved.  Tap on the link below to view your notes.</p>" + 
						 "Client Name: " + clientName + "</br>" + "SalesConnect Opportunity ID: " + salesConnectID + "</br>" + 
						 "Opportunity Name: " + oppName + "</br>" + "Opportunity Comments: " + description + "</br>" +
						 "<p><a href='"+ accessUrlRoot + "/ViewAllNotes_EG/" + notesFileName + "'>" + accessUrlRoot + "/ViewAllNotes_EG/" + notesFileName + "</a></p>";
		
		mailUtil.sendMail(toEmail, fromEmail, subject, fileUrl, hostUrl);
		
		String header = "<!DOCTYPE HTML>" + 
						"<html>" + 
							"<head>" + 
								"<meta charset='UTF-8'>"
								+ "<title>Solution Seeker</title>"
								+ "<style type='text/css'>img {width:55%!important}</style>"
						  + "</head>"
						  + "<body>" + 
						  		"<p style='text-align: center;font-weight: bold;font-size: 28px;'>TeamSD Next Notes</p>"
						  	  + "<div style='margin-left:40px;'>";
		String footer = 		"</div>"
						  + "</body>"
					  + "</html>";
		msgHtml = header + msgHtml + footer;
		return msgHtml;
	}
	
	@Async
	public void handleViewhandleViewAllNotesAsync(String fromEmail, String toEmail, PovEducationService povEducationService, Map<String, String> taskIds) {
		List<Map<String, String>> allNotesList = null;

		try {

			allNotesList = povEducationService.getViewAllNotes(fromEmail);
			this.handleViewAllNotes(allNotesList, fromEmail, toEmail);
		} catch (Exception e) {
			logger.error("/getViewAllNotes throws exception: ", e);
			mailUtil.sendMail(toEmail, fromEmail, "Solution Seeker : CAMSS Point of View Notes", "<hr></hr><br>Create all notes file process was failed, please try it again.", "");
			
		} finally {
			taskIds.remove(fromEmail);
		}

	}
	
	@Async
	public void handleViewhandleViewAllNotesAsync(String fromEmail, String toEmail, PovEducationService povEducationService, Map<String, String> egTaskIds,String type,String oppID) {
		try {
			List<Map<String, String>> allNotesList = povEducationService.getViewAllNotes(fromEmail,type,oppID);
			List<Map<String, String>> subList = povEducationService.getMailSub(oppID);
			this.handleViewAllNotes(allNotesList, subList, fromEmail, toEmail, oppID);
			
		} catch (Exception e) {
			logger.error("/getViewAllNotes EG throws exception: ", e);
			mailUtil.sendMail(toEmail, fromEmail, "Solution Seeker : TeamSD Next Gather Notes", "<hr></hr><br>Create all notes file process was failed, message:" + e.getMessage(), "");
			
		} finally {
			egTaskIds.remove(fromEmail);
		}

	}
	
}
