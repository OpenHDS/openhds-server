package org.openhds.web.beans;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.openhds.web.service.JsfService;
import org.openhds.web.util.ScriptRunner;
import org.springframework.core.io.ClassPathResource;
import com.mysql.jdbc.Connection;


public class DatabaseConfigBean {

	String dbUsername;
	String dbPassword;
	String dbDriver;
	String dbUrl;
	String dbType;
	String dbDialect;
	String hibernateExport;
	
	JsfService jsfService;
	
	public DatabaseConfigBean() {
		this.dbType = "HSQL";
		this.hibernateExport = "create";
	}
	
	public void updateType(ValueChangeEvent event) {
		dbType = event.getNewValue().toString();
		
		if (dbType.equals("HSQL")) {
			dbUrl = "jdbc:hsqldb:mem:openhds";
			dbDialect = "org.hibernate.dialect.HSQLDialect";
			dbDriver = "org.hsqldb.jdbcDriver";
			hibernateExport = "create";
		}
		else if (dbType.equals("MYSQL")) {
			dbUrl = "jdbc:mysql://localhost/openhds";
			dbDialect = "org.hibernate.dialect.MySQL5InnoDBDialect";
			dbDriver = "com.mysql.jdbc.Driver";
			hibernateExport = "update";
		}
		else if (dbType.equals("POSTGRES")) {
			dbUrl = "jdbc:postgresql://localhost/openhds";
			dbDialect = "org.hibernate.dialect.PostgreSQLDialect";
			dbDriver = "org.postgres.Driver";
			hibernateExport = "update";
		}
	}
	
	public void create() {
		Properties properties = readDatabaseProperties();		
		properties.put("dbDriver", dbDriver);
		properties.put("dbUrl", dbUrl);
		properties.put("dbUser", dbUsername);
		properties.put("dbPass", dbPassword);
		properties.put("hibernateDialect", dbDialect);
		properties.put("hibernateExport", hibernateExport);
		properties.put("hibernateShowSql", "true");
		writePropertyFile(properties);
		executeScript();
	}
	
	public Properties readDatabaseProperties() {
		FileInputStream fis = null;
		Properties prop = null;
		
		try {
			fis = new FileInputStream(new ClassPathResource("database.properties").getFile());
			if (fis != null) {
				prop = new Properties();
				prop.load(fis);
			}
			else {
				throw new Exception();
			}
			fis.close();	
		} catch (Exception e) {
			jsfService.addMessage("Error in reading Property file. Exception : " + e.getMessage());
			prop = null;
		}
		return prop;
	}
	
	public void writePropertyFile(Properties props) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("src/main/resources/database.properties");
			props.store(fos, "Database Configuration updated");
			FacesContext.getCurrentInstance().renderResponse();
		} catch (Exception e) {
			jsfService.addMessage("Error writing Property file. Exception : " + e.getMessage());
		}
	}
	
	public void executeScript() {
		
		if (dbType.equals("MYSQL")) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = (Connection) DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
				PreparedStatement stmt = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS OPENHDS");
				stmt.execute();
				stmt.close();
				
				ScriptRunner runner = new ScriptRunner(conn, false, true);
				runner.runScript(new BufferedReader(new FileReader("src/main/resources/openhds-required-data.sql")));
				
			} 
			catch (Exception e) {
				jsfService.addMessage("Error writing Property file. Exception : " + e.getMessage());
			}
		}
		jsfService.addMessage("Database Configuration updated successfully");
	}

	public String getDbUsername() {
		return dbUsername;
	}
	
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	
	public String getDbPassword() {
		return dbPassword;
	}
	
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	public String getDbDriver() {
		return dbDriver;
	}
	
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
	
	public String getDbUrl() {
		return dbUrl;
	}
	
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	
	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	public String getDbDialect() {
		return dbDialect;
	}

	public void setDbDialect(String dbDialect) {
		this.dbDialect = dbDialect;
	}
	
	public JsfService getJsfService() {
		return jsfService;
	}

	public void setJsfService(JsfService jsfService) {
		this.jsfService = jsfService;
	}
}
