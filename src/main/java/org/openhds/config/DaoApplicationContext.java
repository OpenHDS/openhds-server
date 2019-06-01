package org.openhds.config;

import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.openhds.dao.Dao;
import org.openhds.dao.finder.Advisor;
import org.openhds.dao.finder.AnnotationNamingStrategy;
import org.openhds.dao.finder.SimpleArgumentTypeFactory;
import org.openhds.dao.impl.BaseDaoImpl;
import org.openhds.dao.impl.FieldWorkerDaoImpl;
import org.openhds.dao.impl.IndividualDaoImpl;
import org.openhds.dao.impl.LocationHierarchyDaoImpl;
import org.openhds.dao.impl.RoleDaoImpl;
import org.openhds.domain.AsyncTask;
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
@Import({DomainApplicationContext.class} )
@PropertySource({"classpath:database.properties"})
public class DaoApplicationContext {

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
	public Dao openhdsUserDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.User.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao adultVPMDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.AdultVPM.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao deathDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Death.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao demRatesDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.DemRates.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao extensionDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.ClassExtension.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao fieldWorkerDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory,
			SitePropertiesService properties) 
					throws ClassNotFoundException {

		FieldWorkerDaoImpl fwDao = new FieldWorkerDaoImpl(org.openhds.domain.FieldWorker.class);
		fwDao.setSessionFactory(sessionFactory);
		fwDao.setNamingStrategy(namingStrategy);
		fwDao.setArgumentTypeFactory(argumentTypeFactory);
		fwDao.setProperties(properties);
		return fwDao;
	}

	@Bean
	public Dao formDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Form.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao individualDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory,
			SitePropertiesService properties) 
					throws ClassNotFoundException {

		IndividualDaoImpl indDao = new IndividualDaoImpl(org.openhds.domain.Individual.class);
		indDao.setSessionFactory(sessionFactory);
		indDao.setNamingStrategy(namingStrategy);
		indDao.setArgumentTypeFactory(argumentTypeFactory);
		indDao.setProperties(properties);
		return indDao;
	}

	@Bean
	public Dao inMigrationDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.InMigration.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao locationDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Location.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao locationHierarchyDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		LocationHierarchyDaoImpl lhDao = new LocationHierarchyDaoImpl(org.openhds.domain.LocationHierarchy.class);
		lhDao.setSessionFactory(sessionFactory);
		lhDao.setNamingStrategy(namingStrategy);
		lhDao.setArgumentTypeFactory(argumentTypeFactory);

		return lhDao;
	}

	@Bean
	public Dao locationHierarchyLevelDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.LocationHierarchyLevel.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao membershipDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Membership.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao neoNatalVPMDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.NeoNatalVPM.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao noteDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Note.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao outMigrationDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.OutMigration.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao postNeoNatalVPMDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.PostNeoNatalVPM.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao pregnancyDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.PregnancyOutcome.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao pregnancyObservationDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.PregnancyObservation.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao relationshipDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Relationship.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao residencyDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Residency.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao roleDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		RoleDaoImpl roleDao = new RoleDaoImpl(org.openhds.domain.Role.class);
		roleDao.setSessionFactory(sessionFactory);
		roleDao.setNamingStrategy(namingStrategy);
		roleDao.setArgumentTypeFactory(argumentTypeFactory);

		return roleDao;
	}

	@Bean
	public Dao roundDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Round.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao socialGroupDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.SocialGroup.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao<AsyncTask, String>taskDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl<AsyncTask, String> baseDao = new BaseDaoImpl<AsyncTask, String>(org.openhds.domain.AsyncTask.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao vaccinationDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Vaccination.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao visitDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Visit.class);
		baseDao.setSessionFactory(sessionFactory);
		baseDao.setNamingStrategy(namingStrategy);
		baseDao.setArgumentTypeFactory(argumentTypeFactory);

		return baseDao;
	}

	@Bean
	public Dao whitelistDao(SessionFactory sessionFactory,
			AnnotationNamingStrategy namingStrategy, SimpleArgumentTypeFactory argumentTypeFactory) 
					throws ClassNotFoundException {

		BaseDaoImpl baseDao = new BaseDaoImpl(org.openhds.domain.Whitelist.class);
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
