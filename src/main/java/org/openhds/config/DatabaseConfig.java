package org.openhds.config;


import org.openhds.constraint.AppContextAware;
import org.openhds.service.JsfService;
import org.openhds.web.beans.DatabaseConfigBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:database.properties")
public class DatabaseConfig {
	@Value("${dbDriver}")
	String dbDriver;
	
	@Value("${dbUrl}") 
	String dbUrl;
	
	@Value("${dbUser}") 
	String dbUsername;
	
	@Value("${dbPass}") 
	String dbPassword; 
	
	@Value("${hibernate.dialect}") 
	String dbDialect;
	
	@Value("${hibernate.show_sql}") 
	String showSql;
	
	@Bean
	public DatabaseConfigBean databaseConfigBean(JsfService jsfService) {
		DatabaseConfigBean dbConfig = new DatabaseConfigBean();
		dbConfig.setDbDriver(dbDriver);
		dbConfig.setDbUrl(dbUrl);
		dbConfig.setDbUsername(dbUsername);
		dbConfig.setDbPassword(dbPassword);
		dbConfig.setDbDialect(dbDialect);
		dbConfig.setShowSql(showSql);
		
		dbConfig.setJsfService(jsfService);
		return dbConfig;
	}	
}
