package org.openhds.specialstudy.util;

import java.io.File;
import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.openhds.specialstudy.domain.EndUser;
import org.openhds.specialstudy.domain.Individual;
import org.openhds.specialstudy.domain.Location;
import org.openhds.specialstudy.domain.SocialGroup;
import org.openhds.specialstudy.domain.Visit;


/**
 * Utility class that will generate the database schema
 */
public class Ejb3SchemaExport {

	private static final String RESOURCES_PATH = "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar;
	
	public static void main(String[] args) {
		Ejb3Configuration ejb3Cfg = new Ejb3Configuration();
		
		// set propeties (these were set based on the persistence.xml file)
		Properties props = new Properties();
		props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		ejb3Cfg.addProperties(props);
		
		ejb3Cfg.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
		
		// add annotated classes
		ejb3Cfg.addAnnotatedClass(EndUser.class);
		ejb3Cfg.addAnnotatedClass(Individual.class);
		ejb3Cfg.addAnnotatedClass(Location.class);
		ejb3Cfg.addAnnotatedClass(SocialGroup.class);
		ejb3Cfg.addAnnotatedClass(Visit.class);
		
		Configuration cfg = ejb3Cfg.getHibernateConfiguration();
		
		SchemaExport se = new SchemaExport(cfg).setOutputFile(RESOURCES_PATH + "specialstudy-schema-ddl.sql");
		se.execute(false, false, false, true);
	}
}
