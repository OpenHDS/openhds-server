package org.openhds.config;

import java.util.Properties;

import org.openhds.constraint.AppContextAware;
import org.openhds.domain.AppSettings;
import org.openhds.service.SitePropertiesService;
import org.openhds.service.ValueConstraintService;
import org.openhds.service.impl.SitePropertiesServiceImpl;
import org.openhds.service.impl.ValueConstraintServiceImpl;
import org.openhds.util.CalendarAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@ComponentScan(basePackages = "org.openhds.domain")
public class DomainApplicationContextConfiguration {
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller()
	{
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(new String[] { "org.openhds.domain" });
		return marshaller;
	}
	
	@Bean
	@Scope(value="prototype")
	public AppSettings appSettingsBean() 
	{
		AppSettings appSettings = new AppSettings();
		Properties appSettingProperties = new Properties();
		appSettings.setVersionNumber(appSettingProperties.getProperty("openhdsVersion"));
		return appSettings;
	}
	
	@Bean 
	public AppContextAware appContextAwareBean() 
	{
		return new AppContextAware();
	}
	
	@Bean
	public ValueConstraintService valueContraintServiceBean() 
	{
		return new ValueConstraintServiceImpl();
	}
	
	@Bean
	public SitePropertiesService sitePropertiesServiceBean() {
		return new SitePropertiesServiceImpl();
	}
}
