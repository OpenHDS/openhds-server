package org.openhds.controller.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.util.Properties;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.springframework.core.io.ClassPathResource;

import com.mysql.jdbc.Connection;

/**
 * This class will generate the schema DDL that users can import 
 * into their databases. The hibernate.cfg.xml file is used to determine
 * the dialect the schema export script will be generated in.
 */
public class AnnotationSchemaUpdate {
	
private static final String RESOURCES_PATH = "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar;
	
	static String[] dirArray;

	public static void main(String[] args) {
		File currentDirectory = new File("");

		dirArray = currentDirectory.getAbsolutePath().split("\\\\");			
				
		File config; 
		if (!dirArray[dirArray.length-1].equals("controller"))
			config = new File(currentDirectory.getAbsolutePath() + File.separatorChar + "controller" + File.separatorChar + RESOURCES_PATH + "hibernate.cfg.xml");
		else
			config = new File(currentDirectory.getAbsolutePath() + File.separatorChar + RESOURCES_PATH + "hibernate.cfg.xml");
		
		Configuration cfg = new AnnotationConfiguration();
		cfg.configure(config);	
		
		SchemaUpdate se = new SchemaUpdate(cfg);
		se.setOutputFile(RESOURCES_PATH + "openhds-schema-update.sql");
		se.execute(false, false);
		
		Properties props = cfg.getProperties();
		
		try {
			Class.forName(props.getProperty("connection.driver_class"));
			Connection conn = (Connection) DriverManager.getConnection(props.getProperty("connection.url"), props.getProperty("connection.username"), props.getProperty("connection.password"));
			
			ScriptRunner runner = new ScriptRunner(conn, false, true);
			
			File script; 
			if (!dirArray[dirArray.length-1].equals("controller"))
				script = new File(currentDirectory.getAbsolutePath() + File.separatorChar + "controller" + File.separatorChar + RESOURCES_PATH + "openhds-schema-update.sql");
			else
				script = new File(currentDirectory.getAbsolutePath() + File.separatorChar + RESOURCES_PATH + "openhds-schema-update.sql");
						
			runner.runScript(new BufferedReader(new FileReader(script)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
