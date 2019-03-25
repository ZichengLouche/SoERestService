package com.ibm.soe.rest.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/*
 * Andy 2016.4.5 17:15
 */
public class DOM4JUtil {
	private static Logger logger = Logger.getLogger(DOM4JUtil.class);
	public static String DATA_VERSION = "";

	@SuppressWarnings("unchecked")
	public static List<Element> getChildElements(String pathName) throws DocumentException, MalformedURLException, URISyntaxException {
		SAXReader reader = new SAXReader();
		URL url = new URL(pathName);
//		File file = new File(url.toURI());
		Document document = reader.read(url);
		
		Element root = document.getRootElement();
		DATA_VERSION = root.element("Version").getText();
		List<Element> childElements = root.elements("IBMRedbooksDoc");
		return childElements;
	}

	// Andy 2016.4.7 15:45
	public static String getFilePath(String urlString) {
		BufferedReader br = null;
		FileWriter fw = null;
		BufferedWriter bfw = null;
		File file = null;
		try {
			file = new File("data.xml");
			fw = new FileWriter(file);
			bfw = new BufferedWriter(fw);
	
			URL url = new URL(urlString);  
			java.net.HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        conn.setDoOutput(true);  
	        // conn.setRequestMethod("POST");  
	        conn.connect();
	        br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	        	bfw.write(line);
	        	bfw.newLine();
	        }  
	        
	        fw.flush();

		} catch (IOException e) {
			logger.error("get file occured IOException!", e);
		} finally {
			try {
				bfw.close();
				br.close();
			} catch (IOException e) {
				logger.error("close IO stream Exception!", e);
			}  
		}
		
		String filePath = "";
		if(file != null && file.exists() && file.length() > 0) {
			filePath = file.getAbsolutePath();
			logger.info(file.getAbsolutePath());
		}
			
		return filePath;
	}
}
