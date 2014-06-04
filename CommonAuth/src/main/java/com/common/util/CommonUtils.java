package com.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	
	public static String getUrlResponse(String urlToRead)throws Exception {
		logger.trace("getUrlResponse(String) - Start");
		StringBuilder sb = new StringBuilder();
		String token = null;
		
		try {

			ClientRequest request = new ClientRequest(urlToRead);
			request.accept("application/json");
			ClientResponse<String> response = request.get(String.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(response.getEntity().getBytes())));

			
			while ((token = br.readLine()) != null) {
				sb.append(token);
			}

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return sb.toString();
	}

	public static String getBrowser(String Information) {
		String browsername = "";
        String browserversion = "";
        String browser = Information  ;
        if(browser.contains("MSIE")){
            String subsString = browser.substring( browser.indexOf("MSIE"));
            String Info[] = (subsString.split(";")[0]).split(" ");
            browsername = Info[0];
            browserversion = Info[1];
         }
       else if(browser.contains("Firefox")){

            String subsString = browser.substring( browser.indexOf("Firefox"));
            String Info[] = (subsString.split(" ")[0]).split("/");
            browsername = Info[0];
            browserversion = Info[1];
       }
       else if(browser.contains("Chrome")){

            String subsString = browser.substring( browser.indexOf("Chrome"));
            String Info[] = (subsString.split(" ")[0]).split("/");
            browsername = Info[0];
            browserversion = Info[1];
       }
       else if(browser.contains("Opera")){

            String subsString = browser.substring( browser.indexOf("Opera"));
            String Info[] = (subsString.split(" ")[0]).split("/");
            browsername = Info[0];
            browserversion = Info[1];
       }
       else if(browser.contains("Safari")){

            String subsString = browser.substring( browser.indexOf("Safari"));
            String Info[] = (subsString.split(" ")[0]).split("/");
            browsername = Info[0];
            browserversion = Info[1];
       }          
       return browsername + "-" +browserversion;
	}
}
