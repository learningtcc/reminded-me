package com.homefellas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class AbsolutePathPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	
	// TODO Need to pull out properties from environment and put in new place
	// TODO Need to figure out how to do runtime changeable properties
	
	protected final Log log = LogFactory.getLog(getClass());
	private List<String> locations;
	private String basePropertiesHome;
	
	// getters and setters
	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	
	
	
	public String getBasePropertiesHome() {
		return basePropertiesHome;
	}
	public void setBasePropertiesHome(String basePropertiesHome) {
		this.basePropertiesHome = basePropertiesHome;
	}
	@Override
	protected void loadProperties(Properties props) throws IOException {
		
		File propertiesHome = new File(basePropertiesHome);
		
		if (!propertiesHome.exists())
			throw new RuntimeException("PROPERTIES_HOME folder:"+basePropertiesHome+" ("+propertiesHome.getAbsolutePath()+") does not exist.  You need to change the file hf-build/filters/rm-filter-{profile}.properties or create a new profile");
		
		for(String str : getLocations()) {
//			try {
				log.info(basePropertiesHome+str);
				if (str.startsWith("classpath:"))
				{
//					ClassPathResource classPathResource = new ClassPathResource(str.substring(10));
//					props.load(new FileInputStream(classPathResource.getFile()));
					props.load(getClass().getResourceAsStream("/"+str.substring(10)));
				}
				else
					props.load(new FileInputStream(basePropertiesHome+str));
				
//			} catch (FileNotFoundException f) {
//				log.error("Loading Prod version of the property files" + f);
//				setBasePropertiesHome("/home/www/properties/");
//				props.load(new FileInputStream(basePropertiesHome+str));
//			}
		}
			
	}
	
	
}
