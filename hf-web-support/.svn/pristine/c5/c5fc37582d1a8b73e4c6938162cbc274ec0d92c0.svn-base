package com.hicom.web.filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractDoNotRunForFilter implements Filter{

	private String ignoreFilesOfType;
	private HashSet<String> ignoreFileTypes = new HashSet<String>();
	private String doNotRunFor;
    private ArrayList<String> doNotRunForList = new ArrayList<String>();
    
	public void destroy() {
		
	}

	public final void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String fileType = "";
        String requestURL = httpRequest.getRequestURL().toString();
        
        int lastIndexOfFileSeperator = requestURL.lastIndexOf(File.separator);
        String fileTypeWithFileName = requestURL.substring(lastIndexOfFileSeperator + 1 , requestURL.length());
        int lastIndexOfDot = fileTypeWithFileName.lastIndexOf(".");
        if(lastIndexOfDot != -1) fileType = fileTypeWithFileName.substring(lastIndexOfDot  + 1 , fileTypeWithFileName.length());
		
		boolean runFilter = false;
        if(fileType.equalsIgnoreCase("")) runFilter = true; // A blank filetype could be www.domain.com or domain.com/helloworld
        if( ! ignoreFileTypes.contains(fileType)) runFilter = true; // Not a filetype to ignore as defined in the applicationContext-security.xml
//        if(requestURL.indexOf(loginPage) != -1) runFilter = false; // Do not run if this is the login page URL
        if(httpRequest.getMethod().equalsIgnoreCase("POST")) runFilter = false; // Do not run for POST methods as we loose data on sending a GET to CAS
        for(String doNotRunFor : doNotRunForList){ // Do not run for urls in the doNotRunForList
            if(requestURL.indexOf(doNotRunFor) != -1){
                runFilter = false;
                break;
            }
        }
        if(runFilter){
        	processDoFilter(httpRequest, resp, chain);
        }
        else
        {
        	chain.doFilter(request, resp);
        }
	}
	
	public abstract void processDoFilter(ServletRequest request, ServletResponse resp, FilterChain chain) throws IOException, ServletException;

	public void init(FilterConfig arg0) throws ServletException {
		
	}

	public void setIgnoreFilesOfType(String ignoreFilesOfType) {
        this.ignoreFilesOfType = ignoreFilesOfType;
        
        StringTokenizer stringTokenizer = new StringTokenizer(ignoreFilesOfType , ",");
        while(stringTokenizer.hasMoreTokens()){
            ignoreFileTypes.add(stringTokenizer.nextToken());
        }
        
//        logger.debug("Will ignore files of type [" + ignoreFileTypes +"]. If empty, filter will run for all filetypes.");
    }
	
	 public void setDoNotRunFor(String doNotRunFor) {

		 if (doNotRunFor!=null)
		 {
	        StringTokenizer stringTokenizer = new StringTokenizer(doNotRunFor);
	        String thisToken;
	        while(stringTokenizer.hasMoreTokens()){
	            thisToken = (String) stringTokenizer.nextElement();
	            doNotRunForList.add(thisToken);
	        }
		 }
//	        logger.debug("doNotRunForList [" + doNotRunForList +"]");

	    } 
}
