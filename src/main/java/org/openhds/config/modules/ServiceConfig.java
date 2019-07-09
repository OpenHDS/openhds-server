package org.openhds.config.modules;

import org.openhds.dao.Dao;
import org.openhds.dao.GenericDao;
import org.openhds.dao.RoleDao;
import org.openhds.domain.AsyncTask;
import org.openhds.domain.Location;
import org.openhds.domain.LocationHierarchy;
import org.openhds.idgeneration.Generator;
import org.openhds.idgeneration.IdSchemeResource;
import org.openhds.idgeneration.IndividualGenerator;
import org.openhds.idgeneration.SocialGroupGenerator;
import org.openhds.service.*;
import org.openhds.service.impl.*;
import org.openhds.service.refactor.FieldWorkerCrudService;
import org.openhds.service.refactor.impl.FieldWorkerCrudServiceImpl;
import org.openhds.util.CalendarUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({DaoConfig.class})
@ComponentScan(basePackages= {"org.openhds"})
public class ServiceConfig {
	
	
	@Bean
	public BaselineService baselineService(MembershipService membership, EntityService entityService, SitePropertiesService siteProperties) {
		return new BaselineServiceImpl(membership, entityService, siteProperties);
	}
	
	@Bean
	public CurrentUser currentUserService() {
		return new CurrentUserImpl();
	}
	
	@Bean
	public DeathService deathService(GenericDao dao, EntityService entityService, IndividualService individualService, SitePropertiesService siteProperties) {
		return new DeathServiceImpl(dao, individualService, entityService, siteProperties);
	}
	
	@Bean
	public DemRatesService demRatesService(GenericDao dao) {
		return new DemRatesServiceImpl(dao);
	}
	
	
	@Bean
	public ExtensionService extensionService(GenericDao dao, SitePropertiesService siteProperties) {
		return new ExtensionServiceImpl(dao, siteProperties);
	}
	
	@Bean
	public EntityService entityService(GenericDao dao, CurrentUser currentUser, CalendarUtil calendarUtil, SitePropertiesService sitePropertiesService, EntityValidationService validationService) {
		return new EntityServiceImpl(dao, currentUser, calendarUtil, sitePropertiesService, validationService);
	}
	
	@Bean
	public EntityValidationService entityValidationService(SitePropertiesService siteProperties, JsfService jsfService) {
		return new EntityValidationServiceImpl(siteProperties, jsfService);
	}
	@Bean
	public FormService formService(GenericDao dao, EntityService entityService) {
		return new FormServiceImpl(dao, entityService);
	}
	
	@Bean
	public FieldWorkerService fieldWorkerService(IdSchemeResource idSchemeResource) {
		return new FieldWorkerServiceImpl();
	}
	
	@Bean
	public FieldWorkerCrudService fieldWorkerCrudService(IdSchemeResource idSchemeResource) {
		return new FieldWorkerCrudServiceImpl();
	}
	
	@Bean
	public HeadOfHouseholdService headOfHouseholdService() {
		return new HeadOfHouseholdServiceImpl();
	}
	
	@Bean
	public IndividualMergeService individualMergeService(EntityService entityService, GenericDao genericDao, MembershipService membershipService, InMigrationService inmigrationService) {
		return new IndividualMergeServiceImpl(entityService, genericDao, membershipService, inmigrationService);
	}
	
	@Bean
	public IndividualService individualService(GenericDao dao, IndividualGenerator individualGenerator, 
			SitePropertiesService siteProperties, EntityService entityService, IdSchemeResource idSchemeResource) {
		return new IndividualServiceImpl(dao, individualGenerator, siteProperties, entityService, idSchemeResource);
	}
	
	@Bean
	public InMigrationService inMigrationService(ResidencyService residencyService, EntityService entityService, IndividualService individualService,
			GenericDao dao, SitePropertiesService siteProperties) {
		return new InMigrationServiceImpl(residencyService, entityService, individualService, dao, siteProperties);
	}
	
	@Bean
	public JsfService jsfService() {
		return new JsfServiceImpl();
	}
	
	@Bean
	public LocationHierarchyLevelService locationHierarchyLevelService(GenericDao dao) {
		return new LocationHierarchyLevelServiceImpl(dao);
	}
	
	@Bean
	public LocationHierarchyService locationHierarchyService(GenericDao dao, EntityService entityService, 
			Generator<Location> locationGenerator, Generator<LocationHierarchy> locationHierarchyGenerator) {
		return new LocationHierarchyServiceImpl(dao, entityService, locationGenerator, locationHierarchyGenerator);
	}
	
	@Bean
	public MembershipService membershipService(GenericDao dao, EntityService entityService, IndividualService individualService, SitePropertiesService siteProperties) {
		return new MembershipServiceImpl(dao, entityService, individualService, siteProperties);
	}
	
	@Bean
	public OutMigrationService outMigrationService(ResidencyService residencyService, IndividualService individualService, GenericDao dao, EntityService entityService, SitePropertiesService siteProperties) {
		return new OutMigrationServiceImpl(residencyService, individualService, dao, siteProperties, entityService);
	}
	
	@Bean
	public PregnancyService pregnancyService(EntityService entityService, IndividualService individualService, GenericDao dao, SitePropertiesService siteProperties) {
		return new PregnancyServiceImpl(entityService, individualService, dao, siteProperties);
	}
	
	@Bean
	public RelationshipService relationshipService(EntityService entityService, IndividualService individualService, GenericDao dao, SitePropertiesService siteProperties) {
		return new RelationshipServiceImpl(dao, entityService, individualService, siteProperties);
	}
	
	@Bean
	public ResidencyService residencyService(GenericDao dao, EntityService entityService, SitePropertiesService siteProperties) {
		return new ResidencyServiceImpl(dao, entityService, siteProperties);
	}
	
	@Bean
	public RoleService roleService(RoleDao roleDao, GenericDao genericDao, EntityService entityService) {
		return new RoleServiceImpl(roleDao, genericDao, entityService);
	}
	
	@Bean
	public RoundService roundService(GenericDao genericDao, EntityService entityService) {
		return new RoundServiceImpl(genericDao, entityService);
	}
	
	@Bean
	public SettingsService settingsService(SitePropertiesService siteProperties, Dao<AsyncTask, String> asyncTask, FormService formService,
			FieldWorkerService fieldworkerService) {
		return new SettingsServiceImpl(siteProperties, asyncTask, formService, fieldworkerService);
	}
	
	
	@Bean
	public SiteConfigService siteConfigService() {
		return new SiteConfigServiceImpl();
	}
	  
	@Bean
	public SocialGroupService socialGroupService(GenericDao dao, IndividualService individualService, EntityService entityService, 
			SocialGroupGenerator sgGenerator) {
		return new SocialGroupServiceImpl(dao, individualService, entityService, sgGenerator);
	}
	
	@Bean
	public UserLookupService userLookupService(GenericDao dao) {
		return new UserLookupServiceImpl(dao);
	}
	
	@Bean
	public ValueConstraintService valueConstraintService() {
		return new ValueConstraintServiceImpl();
	}
	
	
	@Bean
	public VisitService visitService() {
		return new VisitServiceImpl();
	}
	
	@Bean
	public WebFlowService webFlowService() {
		return new WebFlowServiceImpl();
	}
}
