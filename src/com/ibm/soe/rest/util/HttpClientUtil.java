package com.ibm.soe.rest.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

public class HttpClientUtil {

	public static HttpClient initHttpClient() throws Exception { 
		HttpClient httpclient=null;
	    if(httpclient == null){  
            X509TrustManager tm = new X509TrustManager() {  
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {  
                }  
  
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {  
                }  
  
                public X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
            };  
            SSLContext sslcontext = SSLContext.getInstance("TLS");  
            sslcontext.init(null, new TrustManager[] { tm }, null);  
            SSLSocketFactory ssf = new SSLSocketFactory(sslcontext);
            // Andy 2016.5.27 16:49
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = new DefaultHttpClient().getConnectionManager();  
            SchemeRegistry sr = ccm.getSchemeRegistry();  
            sr.register(new Scheme("https", 443, ssf));  
            
            HttpParams params = new BasicHttpParams();  
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);  
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);  
            httpclient = new DefaultHttpClient(ccm,params); 
	    }  
	    return httpclient;  
	}   
	
	
}
