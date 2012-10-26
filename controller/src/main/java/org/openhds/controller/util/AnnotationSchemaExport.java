package org.openhds.controller.util;

import java.io.File;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * This class will generate the schema DDL that users can import 
 * into their databases. The hibernate.cfg.xml file is used to determine
 * the dialect the schema export script will be generated in.
 */
public class AnnotationSchemaExport {
	
	private static final String RESOURCES_PATH = "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar;
	
	public static void main(String[] args) {
		File currentDirectory = new File("");
		File config = new File(currentDirectory.getAbsolutePath() + File.separatorChar + RESOURCES_PATH + "hibernate.cfg.xml");

		Configuration cfg = new AnnotationConfiguration();
		cfg.configure(config);
		
		SchemaExport se = new SchemaExport(cfg).setOutputFile(RESOURCES_PATH + "openhds-schema-ddl.sql");
		se.execute(false, false, false, true);
	}
}
