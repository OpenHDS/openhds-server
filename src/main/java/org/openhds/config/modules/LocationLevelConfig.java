package org.openhds.config.modules;

import java.io.InputStream;
import java.util.Properties;

import org.openhds.constraint.AppContextAware;
import org.openhds.dao.GenericDao;
import org.openhds.dao.LocationLevelsSetter;
import org.openhds.dao.impl.LocationLevelsSetterImpl;
import org.openhds.service.JsfService;
import org.openhds.web.beans.LocationLevelConfigBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value="classpath:location-levels.properties", ignoreResourceNotFound = false)
public class LocationLevelConfig {
	
	@Value("${locationHierarchyLevel1}") 
	String locationHierarchyLevel1;
	
	@Value("${locationHierarchyLevel2}") 
	String locationHierarchyLevel2; 
	
	@Value("${locationHierarchyLevel3}")
	String locationHierarchyLevel3;
	
	@Value("${locationHierarchyLevel4}") 
	String locationHierarchyLevel4;
	
	@Value("${locationHierarchyLevel5}") 
	String locationHierarchyLevel5;
	
	@Value("${locationHierarchyLevel6}") 
	String locationHierarchyLevel6;
	
	@Value("${locationHierarchyLevel7}") 
	String locationHierarchyLevel7;
	
	@Value("${locationHierarchyLevel8}") 
	String locationHierarchyLevel8;
	
	@Value("${locationHierarchyLevel9}") 
	String locationHierarchyLevel9;
	
	@Bean
	public LocationLevelConfigBean locationLevelConfigBean() 
	{
		LocationLevelConfigBean locLevelConfig = new LocationLevelConfigBean();
		locLevelConfig.setLevel1(locationHierarchyLevel1);
		locLevelConfig.setLevel2(locationHierarchyLevel2);
		locLevelConfig.setLevel3(locationHierarchyLevel3);
		locLevelConfig.setLevel4(locationHierarchyLevel4);
		locLevelConfig.setLevel5(locationHierarchyLevel5);
		locLevelConfig.setLevel6(locationHierarchyLevel6);
		locLevelConfig.setLevel7(locationHierarchyLevel7);
		locLevelConfig.setLevel8(locationHierarchyLevel8);
		locLevelConfig.setLevel9(locationHierarchyLevel9);
		locLevelConfig.setJsfService((JsfService) AppContextAware.getContext().getBean("jsfService"));

		return locLevelConfig;
	}
	
	@Bean
	public LocationLevelsSetter locationLevelSetter() {
		LocationLevelsSetterImpl locLevelSetter = new LocationLevelsSetterImpl();
		locLevelSetter.setGenericDao((GenericDao) AppContextAware.getContext().getBean("genericDao"));
		
		Properties prop = new Properties();
		prop.setProperty("1", locationHierarchyLevel1);
		prop.setProperty("2", locationHierarchyLevel2);
		prop.setProperty("3", locationHierarchyLevel3);
		prop.setProperty("4", locationHierarchyLevel4);
		prop.setProperty("5", locationHierarchyLevel5);
		prop.setProperty("6", locationHierarchyLevel6);
		prop.setProperty("7", locationHierarchyLevel7);
		prop.setProperty("8", locationHierarchyLevel8);
		prop.setProperty("9", locationHierarchyLevel9);
		
		locLevelSetter.setLocationLevels(prop);
		return locLevelSetter;
	}
	
}
