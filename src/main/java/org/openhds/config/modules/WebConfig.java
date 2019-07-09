package org.openhds.config.modules;
import java.beans.IntrospectionException;

import javax.faces.convert.Converter;

import org.openhds.constraint.AppContextAware;
import org.openhds.converter.CalendarConverter;
import org.openhds.converter.DateConverter;
import org.openhds.converter.EntityConverterImpl;
import org.openhds.converter.EntityPropertyConverterImpl;
import org.openhds.converter.MaritalStatusEndTypeCodeConverter;
import org.openhds.converter.MembershipEndTypeCodeConverter;
import org.openhds.converter.MembershipStartTypeCodeConverter;
import org.openhds.converter.PregnancyTypeCodeConverter;
import org.openhds.converter.ResidencyEndTypeCodeConverter;
import org.openhds.converter.ResidencyStartTypeCodeConverter;
import org.openhds.converter.TimeStampConverter;
import org.openhds.dao.Dao;
import org.openhds.dao.GenericDao;
import org.openhds.domain.AdultVPM;
import org.openhds.domain.ClassExtension;
import org.openhds.domain.Death;
import org.openhds.domain.DemRates;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.Form;
import org.openhds.domain.InMigration;
import org.openhds.domain.Individual;
import org.openhds.domain.IndividualMerge;
import org.openhds.domain.Location;
import org.openhds.domain.LocationHierarchy;
import org.openhds.domain.Membership;
import org.openhds.domain.NeoNatalVPM;
import org.openhds.domain.Note;
import org.openhds.domain.OutMigration;
import org.openhds.domain.PostNeoNatalVPM;
import org.openhds.domain.PregnancyObservation;
import org.openhds.domain.PregnancyOutcome;
import org.openhds.domain.Relationship;
import org.openhds.domain.Residency;
import org.openhds.domain.Role;
import org.openhds.domain.Round;
import org.openhds.domain.SocialGroup;
import org.openhds.domain.User;
import org.openhds.domain.Vaccination;
import org.openhds.domain.Visit;
import org.openhds.domain.Whitelist;
import org.openhds.idgeneration.Generator;
import org.openhds.idgeneration.IndividualGenerator;
import org.openhds.idgeneration.LocationGenerator;
import org.openhds.idgeneration.SocialGroupGenerator;
import org.openhds.idgeneration.VisitGenerator;
import org.openhds.service.AsyncTaskService;
import org.openhds.service.BaselineService;
import org.openhds.service.DeathService;
import org.openhds.service.DemRatesService;
import org.openhds.service.EntityService;
import org.openhds.service.EntityValidationService;
import org.openhds.service.ExtensionService;
import org.openhds.service.FormService;
import org.openhds.service.InMigrationService;
import org.openhds.service.IndividualMergeService;
import org.openhds.service.IndividualService;
import org.openhds.service.JsfService;
import org.openhds.service.LocationHierarchyService;
import org.openhds.service.MembershipService;
import org.openhds.service.OutMigrationService;
import org.openhds.service.PregnancyService;
import org.openhds.service.RelationshipService;
import org.openhds.service.ResidencyService;
import org.openhds.service.RoleService;
import org.openhds.service.RoundService;
import org.openhds.service.SitePropertiesService;
import org.openhds.service.SocialGroupService;
import org.openhds.service.UserService;
import org.openhds.service.VisitService;
import org.openhds.service.WebFlowService;
import org.openhds.service.impl.UserServiceImpl;
import org.openhds.task.support.TaskExecutor;
import org.openhds.validator.CheckMinimumEnumerationStartDateValidator;
import org.openhds.validator.DateValidator;
import org.openhds.web.beans.BaselineFlowBean;
import org.openhds.web.beans.DatabaseConfigBean;
import org.openhds.web.beans.DeathHOHBean;
import org.openhds.web.beans.IndividualHistoryBean;
import org.openhds.web.beans.LocaleBean;
import org.openhds.web.beans.ModifyHOHBean;
import org.openhds.web.beans.TaskBean;
import org.openhds.web.beans.UpdateBean;
import org.openhds.web.beans.ValidationRoutineBean;
import org.openhds.web.crud.EntityCrud;
import org.openhds.web.crud.impl.AdultVPMCrudImpl;
import org.openhds.web.crud.impl.DeathCrudImpl;
import org.openhds.web.crud.impl.DemRatesCrudImpl;
import org.openhds.web.crud.impl.EntityCrudImpl;
import org.openhds.web.crud.impl.ExtensionCrudImpl;
import org.openhds.web.crud.impl.FieldWorkerCrudImpl;
import org.openhds.web.crud.impl.FormCrudImpl;
import org.openhds.web.crud.impl.InMigrationCrudImpl;
import org.openhds.web.crud.impl.IndividualCrudImpl;
import org.openhds.web.crud.impl.IndividualMergeCrudImpl;
import org.openhds.web.crud.impl.LocationCrudImpl;
import org.openhds.web.crud.impl.LocationHierarchyCrudImpl;
import org.openhds.web.crud.impl.MembershipCrudImpl;
import org.openhds.web.crud.impl.NeoNatalVPMCrudImpl;
import org.openhds.web.crud.impl.NoteCrudImpl;
import org.openhds.web.crud.impl.OutMigrationCrudImpl;
import org.openhds.web.crud.impl.PostNeoNatalVPMCrudImpl;
import org.openhds.web.crud.impl.PregnancyObservationCrudImpl;
import org.openhds.web.crud.impl.PregnancyOutcomeCrudImpl;
import org.openhds.web.crud.impl.RelationshipCrudImpl;
import org.openhds.web.crud.impl.ResidencyCrudImpl;
import org.openhds.web.crud.impl.RoleCrudImpl;
import org.openhds.web.crud.impl.RoundCrudImpl;
import org.openhds.web.crud.impl.SocialGroupCrudImpl;
import org.openhds.web.crud.impl.UserCrudImpl;
import org.openhds.web.crud.impl.VaccinationCrudImpl;
import org.openhds.web.crud.impl.VisitCrudImpl;
import org.openhds.web.crud.impl.WhitelistCrudImpl;
import org.openhds.web.ui.FacesNavigation;
import org.openhds.web.ui.NavigationMenuBean;
import org.openhds.web.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;


@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Configuration
@ComponentScan(basePackages= {"org.openhds"})
public class WebConfig {
	Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Autowired
	private Environment env;
	
	@Bean
	public PropertyPlaceholderConfigurer databaseConfigurer() {
		PropertyPlaceholderConfigurer config = new PropertyPlaceholderConfigurer();

		Resource databaseLocation = new ClassPathResource("database.properties");
		Resource locationLevels = new ClassPathResource("location-levels.properties");
		Resource codes = new ClassPathResource("codes.properties");

		// linux-specific local configuration
		Resource linuxProperties = new FileSystemResource("/etc/openhds/*.properties");
		Resource linuxConfig= new FileSystemResource("${user.home}/.config/openhds/*.properties");
		// Mac-specific local configuration 
		Resource macConfig = new FileSystemResource("${user.home}/Library/Preferences/OpenHDS/*.properties");
		// Windows-specific local configuration 
		Resource windowsConfig = new FileSystemResource("${user.home}/Local Settings/Application Data/OpenHDS/*.properties");

		config.setIgnoreResourceNotFound(true);
		config.setLocations(databaseLocation, locationLevels, codes,
				linuxProperties, linuxConfig, macConfig, windowsConfig);
		return config;
	}

	@Bean 
	public Locale prefLocale() {
		SitePropertiesService prop = (SitePropertiesService) AppContextAware.getContext().getBean("siteProperties");
		Locale locale = new Locale();
		locale.setLocale(prop.getLocale());
		return locale;
	}

	@Bean
	public LocaleBean localeBean() {
		return new LocaleBean();
	}

	@Bean
	public String demoLoginString() {
		return "${demo.login.credentials}";
	}

	@Bean
	public CalendarConverter calendarConverter(SitePropertiesService siteProperties) {
		String dateFormat = siteProperties.getDateFormat();
		CalendarConverter calendar = new CalendarConverter();
		calendar.setDateFormat(dateFormat);

		return calendar;
	}

	@Bean
	public DateConverter dateConverter(SitePropertiesService siteProperties) {
		String dateFormat = siteProperties.getDateFormat();
		DateConverter dateConverter = new DateConverter();
		dateConverter.setDateFormat(dateFormat);

		return dateConverter;
	}

	@Bean
	public TimeStampConverter timestampConverter(SitePropertiesService siteProperties) {
		TimeStampConverter timestampConverter = new TimeStampConverter("yyyy.MM.dd.HH.mm.ss");
		return timestampConverter;
	}
	
	@Bean
	public CheckMinimumEnumerationStartDateValidator checkMinimumEnumerationDate() {
		return new CheckMinimumEnumerationStartDateValidator();
	}
	
	@Bean
	public DateValidator dateValidator() {
		return new DateValidator();
	}
	
	@Bean
	public EntityPropertyConverterImpl<FieldWorker, String> fieldWorkerExtIdConverter(Dao<FieldWorker, String> dao, 
			String extId) throws IntrospectionException {
		EntityPropertyConverterImpl<FieldWorker, String> converter = 
				new EntityPropertyConverterImpl<FieldWorker, String>(dao, FieldWorker.class, extId);
		return converter;
	}
	
	@Bean
	public EntityPropertyConverterImpl<Form, String> formConverter(Dao<Form, String> dao, 
			String extId) throws IntrospectionException {
		EntityPropertyConverterImpl<Form, String> converter = 
				new EntityPropertyConverterImpl<Form, String>(dao, Form.class, extId);
		return converter;
	}
	
	@Bean
	public EntityPropertyConverterImpl<Individual, String> individualExtIdConverter(Dao<Individual, String> dao, 
			 String extId) throws IntrospectionException {
		EntityPropertyConverterImpl<Individual, String> converter = 
				new EntityPropertyConverterImpl<Individual, String>(dao, Individual.class, extId);
		return converter;
	}
	
	@Bean
	public EntityPropertyConverterImpl<Location, String> locationExtIdConverter(Dao<Location, String> dao, 
			String extId) throws IntrospectionException {
		EntityPropertyConverterImpl<Location, String> converter = 
				new EntityPropertyConverterImpl<Location, String>(dao, Location.class, extId);
		return converter;
	}
	
	@Bean
	public EntityPropertyConverterImpl<LocationHierarchy, String> locationHierarchyExtIdConverter(Dao<LocationHierarchy, String> dao, 
			 String extId) throws IntrospectionException {
		EntityPropertyConverterImpl<LocationHierarchy, String> converter = 
				new EntityPropertyConverterImpl<LocationHierarchy, String>(dao, LocationHierarchy.class, extId);
		return converter;
	}
	
	@Bean
	public EntityPropertyConverterImpl<Individual, String> optionalIndividualExtIdConverter(Dao<Individual, String> dao, 
			String extId) throws IntrospectionException {
		EntityPropertyConverterImpl<Individual, String> converter = 
				new EntityPropertyConverterImpl<Individual, String>(dao, Individual.class, extId);
		return converter;
	}
	
	@Bean
	public EntityPropertyConverterImpl<SocialGroup, String> socialGroupExtIdConverter(Dao<SocialGroup, String> dao, 
		    String extId) throws IntrospectionException {
		EntityPropertyConverterImpl<SocialGroup, String> converter = 
				new EntityPropertyConverterImpl<SocialGroup, String>(dao, SocialGroup.class, extId);
		return converter;
	}
	
	@Bean
	public EntityPropertyConverterImpl<Visit, String> visitExtIdConverter(Dao<Visit, String> dao, 
			String extId) throws IntrospectionException {
		EntityPropertyConverterImpl<Visit, String> converter = 
				new EntityPropertyConverterImpl<Visit, String>(dao, Visit.class, extId);
		return converter;
	}
	
	@Bean
	public MaritalStatusEndTypeCodeConverter maritalStatusEndTypeConverter(SitePropertiesService siteProperties) {
		MaritalStatusEndTypeCodeConverter converter = new MaritalStatusEndTypeCodeConverter(siteProperties);
		return converter;
	}
	
	@Bean
	public MembershipStartTypeCodeConverter membershipStartTypeCodeConverter(SitePropertiesService siteProperties) {
		MembershipStartTypeCodeConverter converter = new MembershipStartTypeCodeConverter(siteProperties);
		return converter;
	}
	
	@Bean
	public MembershipEndTypeCodeConverter membershipEndTypeCodeConverter(SitePropertiesService siteProperties) {
		MembershipEndTypeCodeConverter converter = new MembershipEndTypeCodeConverter(siteProperties);
		return converter;
	}
	
	@Bean
	public ResidencyStartTypeCodeConverter residencyStartEndTypeCodeConverter(SitePropertiesService siteProperties) {
		ResidencyStartTypeCodeConverter converter = new ResidencyStartTypeCodeConverter(siteProperties);
		return converter;
	}
	
	@Bean
	public ResidencyEndTypeCodeConverter residencyEndTypeCodeConverter(SitePropertiesService siteProperties) {
		ResidencyEndTypeCodeConverter converter = new ResidencyEndTypeCodeConverter(siteProperties);
		return converter;
	}
	
	@Bean
	public PregnancyTypeCodeConverter pregnancyTypeCodeConverter(SitePropertiesService siteProperties) {
		PregnancyTypeCodeConverter converter = new PregnancyTypeCodeConverter(siteProperties);
		return converter;
	}
	
	@Bean
	public UserService userService(GenericDao dao) {
		UserService userService = new UserServiceImpl(dao);
		return userService;
	}
	
	@Bean
	public EntityCrud baseCrud() {
		EntityCrudImpl base = new EntityCrudImpl(Object.class);
		base.setProperties(AppContextAware.getContext().getBean(SitePropertiesService.class));
		base.setNavMenuBean(AppContextAware.getContext().getBean(NavigationMenuBean.class));
		base.setGenericDao(AppContextAware.getContext().getBean(GenericDao.class));
		base.setJsfService((JsfService) AppContextAware.getContext().getBean(JsfService.class));
		base.setWebFlowService(AppContextAware.getContext().getBean(WebFlowService.class));
		base.setEntityService((EntityService) AppContextAware.getContext().getBean(EntityService.class));
		base.setEntityValidationService(AppContextAware.getContext().getBean(EntityValidationService.class));
		return base;
	}
	
	@Bean
	public Converter adultVPMConverter(Dao<AdultVPM, String> dao) {
		EntityConverterImpl<AdultVPM, String> converter = new EntityConverterImpl<AdultVPM, String>(dao, AdultVPM.class, String.class);
		return converter;
	}
	
	@Bean
	public Converter deathConverter(Dao<Death, String> dao) {
		EntityConverterImpl<Death, String> converter = new EntityConverterImpl<Death, String>(dao, Death.class, String.class);
		return converter;
	}
	
	@Bean
	public Converter demRatesConverter(Dao<DemRates, String> dao) {
		EntityConverterImpl<DemRates, String> converter = new EntityConverterImpl<DemRates, String>(dao, DemRates.class, String.class);
		return converter;
	}
	
	@Bean
	public Converter extensionConverter(Dao<ClassExtension, String> dao) {
		EntityConverterImpl<ClassExtension, String> converter = new EntityConverterImpl<ClassExtension, String>(dao, ClassExtension.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<FieldWorker, String> fieldWorkerConverter(Dao<FieldWorker, String> dao) {
		EntityConverterImpl<FieldWorker, String> converter = new EntityConverterImpl<FieldWorker, String>(dao, FieldWorker.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Individual, String> individualConverter(Dao<Individual, String> dao) {
		EntityConverterImpl<Individual, String> converter = new EntityConverterImpl<Individual, String>(dao, Individual.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<InMigration, String> inMigrationConverter(Dao<InMigration, String> dao) {
		EntityConverterImpl<InMigration, String> converter = new EntityConverterImpl<InMigration, String>(dao, InMigration.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Location, String> locationConverter(Dao<Location, String> dao) {
		EntityConverterImpl<Location, String> converter = new EntityConverterImpl<Location, String>(dao, Location.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<LocationHierarchy, String> locationHierarchyConverter(Dao<LocationHierarchy, String> dao) {
		EntityConverterImpl<LocationHierarchy, String> converter = new EntityConverterImpl<LocationHierarchy, String>(dao, LocationHierarchy.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Membership, String> membershipConverter(Dao<Membership, String> dao) {
		EntityConverterImpl<Membership, String> converter = new EntityConverterImpl<Membership, String>(dao, Membership.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<NeoNatalVPM, String> neoNatalVPMConverter(Dao<NeoNatalVPM, String> dao) {
		EntityConverterImpl<NeoNatalVPM, String> converter = new EntityConverterImpl<NeoNatalVPM, String>(dao, NeoNatalVPM.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Note, String> noteConverter(Dao<Note, String> dao) {
		EntityConverterImpl<Note, String> converter = new EntityConverterImpl<Note, String>(dao, Note.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<OutMigration, String> outMigrationConverter(Dao<OutMigration, String> dao) {
		EntityConverterImpl<OutMigration, String> converter = new EntityConverterImpl<OutMigration, String>(dao, OutMigration.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<PostNeoNatalVPM, String> postNeoNatalVPMConverter(Dao<PostNeoNatalVPM, String> dao) {
		EntityConverterImpl<PostNeoNatalVPM, String> converter = new EntityConverterImpl<PostNeoNatalVPM, String>(dao, PostNeoNatalVPM.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<PregnancyOutcome, String> pregnancyConverter(Dao<PregnancyOutcome, String> dao) {
		EntityConverterImpl<PregnancyOutcome, String> converter = new EntityConverterImpl<PregnancyOutcome, String>(dao, PregnancyOutcome.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<PregnancyObservation, String> pregnancyObservationConverter(Dao<PregnancyObservation, String> dao) {
		EntityConverterImpl<PregnancyObservation, String> converter = new EntityConverterImpl<PregnancyObservation, String>(dao, PregnancyObservation.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Relationship, String> relationshipConverter(Dao<Relationship, String> dao) {
		EntityConverterImpl<Relationship, String> converter = new EntityConverterImpl<Relationship, String>(dao, Relationship.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Residency, String> residencyConverter(Dao<Residency, String> dao) {
		EntityConverterImpl<Residency, String> converter = new EntityConverterImpl<Residency, String>(dao, Residency.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Role, String> roleConverter(Dao<Role, String> dao) {
		EntityConverterImpl<Role, String> converter = new EntityConverterImpl<Role, String>(dao, Role.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Round, String> roundConverter(Dao<Round, String> dao) {
		EntityConverterImpl<Round, String> converter = new EntityConverterImpl<Round, String>(dao, Round.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<SocialGroup, String> socialGroupConverter(Dao<SocialGroup, String> dao) {
		EntityConverterImpl<SocialGroup, String> converter = new EntityConverterImpl<SocialGroup, String>(dao, SocialGroup.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<User, String> userConverter(Dao<User, String> dao) {
		EntityConverterImpl<User, String> converter = new EntityConverterImpl<User, String>(dao, User.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Vaccination, String> vaccinationConverter(Dao<Vaccination, String> dao) {
		EntityConverterImpl<Vaccination, String> converter = new EntityConverterImpl<Vaccination, String>(dao, Vaccination.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Visit, String> visitConverter(Dao<Visit, String> dao) {
		EntityConverterImpl<Visit, String> converter = new EntityConverterImpl<Visit, String>(dao, Visit.class, String.class);
		return converter;
	}
	
	@Bean
	public EntityConverterImpl<Whitelist, String> whitelistConverter(Dao<Whitelist, String> dao) {
		EntityConverterImpl<Whitelist, String> converter = new EntityConverterImpl<Whitelist, String>(dao, Whitelist.class, String.class);
		return converter;
	}
	
	@Bean
	public FacesNavigation faceNavigation() {
		return new FacesNavigation();
	}
	
	@Bean
	public NavigationMenuBean navController() {
		return new NavigationMenuBean();
	}
	
	
	//Crud beans
	
	@Bean
	public EntityCrud<AdultVPM, String> adultVPMCrud() {
		AdultVPMCrudImpl crud = new AdultVPMCrudImpl(AdultVPM.class);
		crud.setDao((Dao) AppContextAware.getContext().getBean("adultVPMDao"));
		crud.setConverter((Converter)AppContextAware.getContext().getBean("adultVPMConverter"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Death, String> deathCrud() {
		DeathCrudImpl crud = new DeathCrudImpl(Death.class);
		crud.setConverter((Converter)AppContextAware.getContext().getBean("deathConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("deathDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((DeathService) AppContextAware.getContext().getBean("deathService"));
		return crud;
	}
	
	@Bean
	public EntityCrudImpl<DemRates, String> demRatesCrud() {
		DemRatesCrudImpl crud = new DemRatesCrudImpl(DemRates.class);
		crud.setConverter((Converter)AppContextAware.getContext().getBean("demRatesConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("demRatesDao"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setDemRatesService((DemRatesService) AppContextAware.getContext().getBean("demRatesService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<ClassExtension, String> extensionCrud() {
		ExtensionCrudImpl crud = new ExtensionCrudImpl(ClassExtension.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("extensionConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("extensionDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((ExtensionService) AppContextAware.getContext().getBean("extensionService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Form, String> formCrud() {
		FormCrudImpl crud = new FormCrudImpl(Form.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("formConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("formDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((FormService) AppContextAware.getContext().getBean("formService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<FieldWorker, String> fieldWorkerCrud() {
		FieldWorkerCrudImpl crud = new FieldWorkerCrudImpl(FieldWorker.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("fieldWorkerConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("fieldWorkerDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setFieldWorkerService((org.openhds.service.refactor.FieldWorkerCrudService) AppContextAware.getContext().getBean("fieldWorkerCrudService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Individual, String> individualCrud() {
		IndividualCrudImpl crud = new IndividualCrudImpl(Individual.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("individualConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("individualDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((IndividualService) AppContextAware.getContext().getBean("individualService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<IndividualMerge, String> individualMergeCrud() {
		IndividualMergeCrudImpl crud = new IndividualMergeCrudImpl(IndividualMerge.class);
		crud.setIndivMergeService((IndividualMergeService) AppContextAware.getContext().getBean("individualMergeService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<InMigration, String> inMigrationCrud() {
		InMigrationCrudImpl crud = new InMigrationCrudImpl(InMigration.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("inMigrationConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("inMigrationDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((InMigrationService) AppContextAware.getContext().getBean("inMigrationService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Location, String> locationCrud() {
		LocationCrudImpl crud = new LocationCrudImpl(Location.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("individualConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("locationDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((LocationHierarchyService) AppContextAware.getContext().getBean("locationHierarchyService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<LocationHierarchy, String> locationHierarchyCrud() {
		LocationHierarchyCrudImpl crud = new LocationHierarchyCrudImpl(LocationHierarchy.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("locationHierarchyConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("locationHierarchyDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((LocationHierarchyService) AppContextAware.getContext().getBean("locationHierarchyService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Membership, String> membershipCrud() {
		MembershipCrudImpl crud = new MembershipCrudImpl(Membership.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("membershipConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("membershipDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((MembershipService) AppContextAware.getContext().getBean("membershipService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<NeoNatalVPM, String> neoNatalVPMCrud() {
		NeoNatalVPMCrudImpl crud = new NeoNatalVPMCrudImpl(NeoNatalVPM.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("neoNatalVPMConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("neoNatalVPMDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Note, String> NoteCrud() {
		NoteCrudImpl crud = new NoteCrudImpl(Note.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("noteConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("noteDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<OutMigration, String> outMigrationCrud() {
		OutMigrationCrudImpl crud = new OutMigrationCrudImpl(OutMigration.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("outMigrationConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("outMigrationDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((OutMigrationService) AppContextAware.getContext().getBean("outMigrationService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<PostNeoNatalVPM, String> postNeoNatalVPM() {
		PostNeoNatalVPMCrudImpl crud = new PostNeoNatalVPMCrudImpl(PostNeoNatalVPM.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("postNeoNatalVPMConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("postNeoNatalVPMDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<PregnancyOutcome, String> pregnancyOutcomeCrud() {
		PregnancyOutcomeCrudImpl crud = new PregnancyOutcomeCrudImpl(PregnancyOutcome.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("pregnancyConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("pregnancyDao"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((PregnancyService) AppContextAware.getContext().getBean("pregnancyService"));
		crud.setSgService((SocialGroupService) AppContextAware.getContext().getBean("socialGroupService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<PregnancyObservation, String> pregnancyObservationCrud() {
		PregnancyObservationCrudImpl crud = new PregnancyObservationCrudImpl(PregnancyObservation.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("pregnancyObservationConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("pregnancyObservationDao"));
		crud.setEntityValidationService((EntityValidationService) AppContextAware.getContext().getBean("entityValidationService"));
		crud.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		crud.setService((PregnancyService) AppContextAware.getContext().getBean("pregnancyService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Relationship, String> relationshipCrud() {
		RelationshipCrudImpl crud = new RelationshipCrudImpl(Relationship.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("relationshipConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("relationshipDao"));
		crud.setService((RelationshipService) AppContextAware.getContext().getBean("relationshipService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Residency, String> residencyCrud() {
		ResidencyCrudImpl crud = new ResidencyCrudImpl(Residency.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("residencyConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("residencyDao"));
		crud.setResidencyService((ResidencyService) AppContextAware.getContext().getBean("residencyService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Role, String> roleCrud() {
		RoleCrudImpl crud = new RoleCrudImpl(Role.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("roleConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("roleDao"));
		crud.setService((RoleService) AppContextAware.getContext().getBean("roleService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Round, String> roundCrud() {
		RoundCrudImpl crud = new RoundCrudImpl(Round.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("roundConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("roundDao"));
		crud.setService((RoundService) AppContextAware.getContext().getBean("roundService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<SocialGroup, String> socialGroupCrud() {
		SocialGroupCrudImpl crud = new SocialGroupCrudImpl(SocialGroup.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("socialGroupConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("socialGroupDao"));
		crud.setSocialGroupService((SocialGroupService) AppContextAware.getContext().getBean("socialGroupService"));
		crud.setIndividualService((IndividualService) AppContextAware.getContext().getBean("individualService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<User, String> userCrud() {
		UserCrudImpl crud = new UserCrudImpl(User.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("userConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("userDao"));
		crud.setService((UserService) AppContextAware.getContext().getBean("userService"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Vaccination, String> vaccinationCrud() {
		VaccinationCrudImpl crud = new VaccinationCrudImpl(Vaccination.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("vaccinationConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("vaccinationDao"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Visit, String> visitCrud() {
		VisitCrudImpl crud = new VisitCrudImpl(Visit.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("visitConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("visitDao"));
		crud.setService((VisitService) AppContextAware.getContext().getBean("visitService"));
		crud.setExtensionService((ExtensionService) AppContextAware.getContext().getBean("extensionService"));
		crud.setSiteProperties((SitePropertiesService) AppContextAware.getContext().getBean("siteProperties"));
		return crud;
	}
	
	@Bean
	public EntityCrud<Whitelist, String> whitelistCrud() {
		WhitelistCrudImpl crud = new WhitelistCrudImpl(Whitelist.class);
		crud.setConverter((Converter) AppContextAware.getContext().getBean("whitelistConverter"));
		crud.setDao((Dao) AppContextAware.getContext().getBean("whitelistDao"));
		return crud;
	}
	
	//Beans
	@Bean
	public UpdateBean updateBean() {
		UpdateBean updateBean = new UpdateBean();
		updateBean.setResidencyService((ResidencyService) AppContextAware.getContext().getBean("residencyService"));
		updateBean.setProperties((SitePropertiesService) AppContextAware.getContext().getBean("siteProperties"));
		updateBean.setGenericDao((GenericDao) AppContextAware.getContext().getBean("genericDao"));
		updateBean.setWebFlowService((WebFlowService) AppContextAware.getContext().getBean("webFlowService"));
		updateBean.setJsfService((JsfService) AppContextAware.getContext().getBean("jsfService"));
		
		return updateBean;
	}
	
	@Bean
	public TaskBean taskBean(TaskExecutor executor, AsyncTaskService taskService) {
		TaskBean taskBean = new TaskBean(executor, taskService);
		return taskBean;
	}
	
	@Bean
	public BaselineFlowBean baselineFlowBean(ResidencyService residencyService, SitePropertiesService siteProperties,
			GenericDao genericDao, WebFlowService webFlowService, BaselineService baselineService, 
			IndividualService individualService, Generator<SocialGroup> socialGroupGenerator) {
		BaselineFlowBean baselineFlow = new BaselineFlowBean();
		baselineFlow.setResidencyService(residencyService);
		baselineFlow.setProperties(siteProperties);
		baselineFlow.setGenericDao(genericDao);
		baselineFlow.setWebFlowService(webFlowService);
		baselineFlow.setBaselineService(baselineService);
		baselineFlow.setIndividualService(individualService);
		baselineFlow.setSocialGroupGenerator(socialGroupGenerator);
		return baselineFlow;
	}
	
	@Bean
	public IndividualHistoryBean individualHistoryBean() {
		IndividualHistoryBean indHistBean = new IndividualHistoryBean();
		indHistBean.setGenericDao((GenericDao) AppContextAware.getContext().getBean("genericDao"));
		indHistBean.setMembershipService((MembershipService) AppContextAware.getContext().getBean("membershipService"));
		indHistBean.setIndividualService((IndividualService) AppContextAware.getContext().getBean("individualService"));
		indHistBean.setRelationshipService((RelationshipService) AppContextAware.getContext().getBean("relationshipService"));
		indHistBean.setSocialGroupService((SocialGroupService) AppContextAware.getContext().getBean("socialGroupService"));
		indHistBean.setResidencyService((ResidencyService) AppContextAware.getContext().getBean("residencyService"));
		indHistBean.setInMigrationService((InMigrationService) AppContextAware.getContext().getBean("inMigrationService"));
		indHistBean.setOutMigrationService((OutMigrationService) AppContextAware.getContext().getBean("outMigrationService"));
		indHistBean.setPregnancyService((PregnancyService) AppContextAware.getContext().getBean("pregnancyService"));
		indHistBean.setDeathService((DeathService) AppContextAware.getContext().getBean("deathService"));
		indHistBean.setWebFlowService((WebFlowService) AppContextAware.getContext().getBean("webFlowService"));

		return indHistBean;
	}
	
	@Bean
	public ModifyHOHBean modifyHOHBean() {
		ModifyHOHBean modifyHoh = new ModifyHOHBean();
		modifyHoh.setGenericDao((GenericDao) AppContextAware.getContext().getBean("genericDao"));
		modifyHoh.setMembershipService((MembershipService) AppContextAware.getContext().getBean("membershipService"));
		modifyHoh.setSocialGroupService((SocialGroupService) AppContextAware.getContext().getBean("socialGroupService"));
		modifyHoh.setWebFlowService((WebFlowService) AppContextAware.getContext().getBean("webFlowService"));
		modifyHoh.setEntityService((EntityService) AppContextAware.getContext().getBean("entityService"));
		return modifyHoh;
	}
	
	@Bean
	public DeathHOHBean deathHOHBean() {
		DeathHOHBean deathHoh = new DeathHOHBean();
		deathHoh.setGenericDao((GenericDao) AppContextAware.getContext().getBean("genericDao"));
		deathHoh.setMembershipService((MembershipService) AppContextAware.getContext().getBean("membershipService"));
		deathHoh.setSocialGroupService((SocialGroupService) AppContextAware.getContext().getBean("socialGroupService"));
		deathHoh.setDeathService((DeathService) AppContextAware.getContext().getBean("deathService"));
		deathHoh.setWebFlowService((WebFlowService) AppContextAware.getContext().getBean("webFlowService"));

		return deathHoh;
	}
	
	@Bean
	public ValidationRoutineBean validationRoutineBean() {
		ValidationRoutineBean validationRoutineBean = new ValidationRoutineBean();
		validationRoutineBean.setIndividualService((IndividualService) AppContextAware.getContext().getBean("individualService"));
		validationRoutineBean.setLocationService((LocationHierarchyService) AppContextAware.getContext().getBean("locationHierarchyService"));
		validationRoutineBean.setSocialgroupService((SocialGroupService) AppContextAware.getContext().getBean("socialGroupService"));
		validationRoutineBean.setRelationshipService((RelationshipService) AppContextAware.getContext().getBean("relationshipService"));
		validationRoutineBean.setMembershipService((MembershipService) AppContextAware.getContext().getBean("membershipService"));
		validationRoutineBean.setPregnancyService((PregnancyService) AppContextAware.getContext().getBean("pregnancyService"));
		validationRoutineBean.setVisitService((VisitService) AppContextAware.getContext().getBean("visitService"));
		validationRoutineBean.setDeathService((DeathService) AppContextAware.getContext().getBean("deathService"));
		validationRoutineBean.setInmigrationService((InMigrationService) AppContextAware.getContext().getBean("inMigrationService"));
		validationRoutineBean.setOutmigrationService((OutMigrationService) AppContextAware.getContext().getBean("outMigrationService"));
		validationRoutineBean.setResidencyService((ResidencyService) AppContextAware.getContext().getBean("residencyService"));
		validationRoutineBean.setGenericDao((GenericDao) AppContextAware.getContext().getBean("genericDao"));
		validationRoutineBean.setEntityValidator((EntityValidationService<?>) AppContextAware.getContext().getBean("entityValidationService"));
		validationRoutineBean.setProperties((SitePropertiesService) AppContextAware.getContext().getBean("siteProperties"));
		validationRoutineBean.setIndivGenerator((IndividualGenerator) AppContextAware.getContext().getBean("individualGenerator"));
		validationRoutineBean.setLocGenerator((LocationGenerator) AppContextAware.getContext().getBean("locationGenerator"));
		validationRoutineBean.setSgGenerator((SocialGroupGenerator) AppContextAware.getContext().getBean("socialGroupIdGenerator"));
		validationRoutineBean.setVisitGenerator((VisitGenerator) AppContextAware.getContext().getBean("visitIdGenerator"));

		return validationRoutineBean;
	}
	
}
