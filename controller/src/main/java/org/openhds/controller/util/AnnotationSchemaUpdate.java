package org.openhds.controller.util;

import java.io.File;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

/**
 * This class will generate the schema DDL that users can import 
 * into their databases. The hibernate.cfg.xml file is used to determine
 * the dialect the schema export script will be generated in.
 */
public class AnnotationSchemaUpdate {
	
private static final String RESOURCES_PATH = "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar;
	
	public static void main(String[] args) {
		File currentDirectory = new File("");
		File config = new File(currentDirectory.getAbsolutePath() + File.separatorChar + RESOURCES_PATH + "hibernate.cfg.xml");

		Configuration cfg = new AnnotationConfiguration();
		cfg.configure(config);
		
		SchemaUpdate se = new SchemaUpdate(cfg);
		se.setOutputFile(RESOURCES_PATH + "openhds-schema-update.sql");
		se.execute(false, false);
	}
}
