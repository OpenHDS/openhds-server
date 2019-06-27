package org.openhds.config;

import java.util.Properties;

import org.openhds.constraint.AppContextAware;
import org.openhds.domain.AppSettings;
import org.openhds.service.SitePropertiesService;
import org.openhds.service.ValueConstraintService;
import org.openhds.service.impl.SitePropertiesServiceImpl;
import org.openhds.service.impl.ValueConstraintServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@ComponentScan(basePackages = "org.openhds.domain")
public class DomainApplicationContext {
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller()
	{
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(new Class[] {
				org.openhds.domain.AdultVPM.class,
				org.openhds.domain.AsyncTask.class,
				org.openhds.domain.AuditableCollectedEntity.class,
				org.openhds.domain.AuditableEntity.class,
				org.openhds.domain.ClassExtension.class,
				org.openhds.domain.Death.class,
				org.openhds.domain.DemRates.class,
				org.openhds.domain.Extension.class,
				org.openhds.domain.FieldWorker.class,
				org.openhds.domain.Form.class,
				org.openhds.domain.Individual.class, 
				org.openhds.domain.InMigration.class,
				org.openhds.domain.Location.class,
				org.openhds.domain.LocationHierarchy.class,
				org.openhds.domain.LocationHierarchyLevel.class,
				org.openhds.domain.Membership.class,
				org.openhds.domain.MigrationType.class,
				org.openhds.domain.NeoNatalVPM.class,
				org.openhds.domain.Note.class,
				org.openhds.domain.Outcome.class,
				org.openhds.domain.OutMigration.class,
				org.openhds.domain.PostNeoNatalVPM.class,
				org.openhds.domain.PregnancyObservation.class,
				org.openhds.domain.PregnancyOutcome.class,
				org.openhds.domain.Privilege.class,
				org.openhds.domain.Relationship.class,
				org.openhds.domain.Residency.class,
				org.openhds.domain.Role.class,
				org.openhds.domain.Round.class,
				org.openhds.domain.SocialGroup.class,
				org.openhds.domain.User.class,
				org.openhds.domain.Vaccination.class,
				org.openhds.domain.Visit.class,
				org.openhds.domain.Whitelist.class,
		});
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
	public ValueConstraintService valueContraintService() 
	{
		return new ValueConstraintServiceImpl();
	}
}
