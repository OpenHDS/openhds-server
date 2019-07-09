package org.openhds.config.modules;

import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.openhds.dao.Dao;
import org.openhds.dao.GenericDao;
import org.openhds.dao.UserDao;
import org.openhds.dao.finder.Advisor;
import org.openhds.dao.finder.AnnotationNamingStrategy;
import org.openhds.dao.finder.SimpleArgumentTypeFactory;
import org.openhds.dao.impl.BaseDaoImpl;
import org.openhds.dao.impl.FieldWorkerDaoImpl;
import org.openhds.dao.impl.GenericDaoImpl;
import org.openhds.dao.impl.IndividualDaoImpl;
import org.openhds.dao.impl.LocationHierarchyDaoImpl;
import org.openhds.dao.impl.RoleDaoImpl;
import org.openhds.domain.AdultVPM;
import org.openhds.domain.AsyncTask;
import org.openhds.domain.ClassExtension;
import org.openhds.domain.Death;
import org.openhds.domain.DemRates;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.Form;
import org.openhds.domain.InMigration;
import org.openhds.domain.Individual;
import org.openhds.domain.Location;
import org.openhds.domain.LocationHierarchy;
import org.openhds.domain.LocationHierarchyLevel;
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
import org.openhds.service.SitePropertiesService;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:database.properties"})
public class DaoConfig {

	@Autowired
	private Environment env;

	@Bean
	public LocalSessionFactoryBean sessionFactory()
	{
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setAnnotatedClasses(new Class[] {
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
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}


	@Bean(destroyMethod="close")
	public BasicDataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("dbDriver"));
		dataSource.setUrl(env.getProperty("dbUrl"));
		dataSource.setUsername(env.getProperty("dbUser"));
		dataSource.setPassword(env.getProperty("dbPass"));
		dataSource.setValidationQuery(env.getProperty("validationQuery"));
		dataSource.setTestOnBorrow(Boolean.parseBoolean(env.getProperty("testOnBorrow")));
		return dataSource;
	}

	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
	}

	@Bean
	public Advisor dynamicFinderAdvisor() {
		return new Advisor();
	}
	
	@Bean
	public AnnotationNamingStrategy namingStrategy() {
		return new AnnotationNamingStrategy();
	}
	
	
	@Bean
	public SimpleArgumentTypeFactory argumentTypeFactory() {
		return new SimpleArgumentTypeFactory();
	}

	@Bean
	public ProxyFactoryBean userDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {
		ProxyFactoryBean userDao = new ProxyFactoryBean();
		userDao.setProxyInterfaces(new Class[] {org.openhds.dao.UserDao.class});
		userDao.setInterceptorNames("dynamicFinderAdvisor");
		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.User.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);
		userDao.setTarget(baseDao);

		return userDao;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	

	@Bean
	public Dao<User, String> openhdsUserDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<User, String> baseDao = new BaseDaoImpl<User, String>(User.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<AdultVPM, String> adultVPMDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<AdultVPM, String> baseDao = new BaseDaoImpl<AdultVPM, String>(AdultVPM.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Death, String> deathDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Death, String> baseDao = new BaseDaoImpl<Death, String>(Death.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<DemRates, String> demRatesDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<DemRates, String> baseDao = new BaseDaoImpl<DemRates, String>(DemRates.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<ClassExtension, String> extensionDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<ClassExtension, String> baseDao = new BaseDaoImpl<ClassExtension, String>(ClassExtension.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<FieldWorker, String> fieldWorkerDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory,
			SitePropertiesService properties) 
					throws ClassNotFoundException {

		FieldWorkerDaoImpl fwDao = new FieldWorkerDaoImpl(FieldWorker.class);
		fwDao.setSessionFactory(sessionFactory);
		fwDao.setNamingStrategy(namingStrategy);
		fwDao.setArgumentTypeFactory(argumentTypeFactory);
		fwDao.setProperties(properties);
		return fwDao;
	}

	@Bean
	public Dao<Form, String> formDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Form, String> baseDao = new BaseDaoImpl<Form, String>(Form.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Individual, String> individualDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory,
			SitePropertiesService properties) 
					throws ClassNotFoundException {

		IndividualDaoImpl indDao = new IndividualDaoImpl(Individual.class);
		indDao.setSessionFactory(sessionFactory);
		indDao.setNamingStrategy(namingStrategy);
		indDao.setArgumentTypeFactory(argumentTypeFactory);
		indDao.setProperties(properties);
		return indDao;
	}

	@Bean
	public Dao<InMigration, String> inMigrationDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<InMigration, String> baseDao = new BaseDaoImpl<InMigration, String>(InMigration.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Location,String> locationDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Location, String> baseDao = new BaseDaoImpl<Location, String>(Location.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<LocationHierarchy, String> locationHierarchyDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		LocationHierarchyDaoImpl lhDao = new LocationHierarchyDaoImpl(LocationHierarchy.class);
		lhDao.setSessionFactory(sessionFactory);
		lhDao.setNamingStrategy(namingStrategy);
		lhDao.setArgumentTypeFactory(argumentTypeFactory);

		return lhDao;
	}

	@Bean
	public Dao<LocationHierarchyLevel, String> locationHierarchyLevelDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<LocationHierarchyLevel, String> baseDao = new BaseDaoImpl<LocationHierarchyLevel, String>(LocationHierarchyLevel.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Membership, String> membershipDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Membership, String> baseDao = new BaseDaoImpl<Membership, String>(Membership.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<NeoNatalVPM, String> neoNatalVPMDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<NeoNatalVPM, String> baseDao = new BaseDaoImpl<NeoNatalVPM, String>(NeoNatalVPM.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Note, String> noteDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Note, String> baseDao = new BaseDaoImpl<Note, String>(Note.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<OutMigration, String> outMigrationDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<OutMigration, String> baseDao = new BaseDaoImpl<OutMigration, String>(OutMigration.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<PostNeoNatalVPM, String> postNeoNatalVPMDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<PostNeoNatalVPM, String> baseDao = new BaseDaoImpl<PostNeoNatalVPM, String>(PostNeoNatalVPM.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<PregnancyOutcome, String> pregnancyDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<PregnancyOutcome, String>baseDao = new BaseDaoImpl<PregnancyOutcome, String>(PregnancyOutcome.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<PregnancyObservation, String> pregnancyObservationDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<PregnancyObservation, String> baseDao = new BaseDaoImpl<PregnancyObservation, String>(PregnancyObservation.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Relationship, String> relationshipDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Relationship, String> baseDao = new BaseDaoImpl<Relationship, String>(Relationship.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Residency, String> residencyDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Residency, String> baseDao = new BaseDaoImpl<Residency, String>(Residency.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Role, String> roleDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		RoleDaoImpl roleDao = new RoleDaoImpl(Role.class);
		roleDao.setSessionFactory(sessionFactory);
		roleDao.setNamingStrategy(namingStrategy);
		roleDao.setArgumentTypeFactory(argumentTypeFactory);

		return roleDao;
	}

	@Bean
	public Dao<Round, String> roundDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Round, String> baseDao = new BaseDaoImpl<Round, String>(Round.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<SocialGroup, String> socialGroupDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<SocialGroup, String> baseDao = new BaseDaoImpl<SocialGroup, String>(SocialGroup.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<AsyncTask, String>taskDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<AsyncTask, String> baseDao = new BaseDaoImpl<AsyncTask, String>(AsyncTask.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Vaccination, String> vaccinationDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Vaccination, String> baseDao = new BaseDaoImpl<Vaccination, String>(Vaccination.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Visit, String> visitDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Visit, String> baseDao = new BaseDaoImpl<Visit, String>(Visit.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<Whitelist, String> whitelistDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<Whitelist, String> baseDao = new BaseDaoImpl<Whitelist, String>(org.openhds.domain.Whitelist.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	private Properties hibernateProperties() {
		Properties prop = new Properties();
		prop.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		prop.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		prop.setProperty("hibernate.export", env.getProperty("hibernate.hbm2ddl.auto"));
		prop.setProperty("hibernate.transaction.factory_class", env.getProperty("hibernate.transaction.factory_class"));
		prop.setProperty("hibernate.jdbc.use_streams_for_binary", env.getProperty("hibernate.jdbc.use_streams_for_binary"));
		prop.setProperty("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));

		return prop;
	}
}
