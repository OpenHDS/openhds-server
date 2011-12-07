package org.openhds.web.beans;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.sql.DriverManager;
import java.util.Properties;
import javax.faces.component.html.HtmlInputText;
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
		Properties props = readDatabaseProperties();
		this.dbType = props.getProperty("dbType");
		
		if (dbType.equals("HSQL"))
			this.hibernateExport = "create";
		else
			this.hibernateExport = "update";
	}
	
	public void updateType(ValueChangeEvent event) {
		dbType = event.getNewValue().toString();
		
		if (dbType.equals("HSQL")) {
			dbUrl = "jdbc:hsqldb:mem:openhds";
			dbDialect = "org.hibernate.dialect.HSQLDialect";
			dbDriver = "org.hsqldb.jdbcDriver";
			dbType = "HSQL";
			hibernateExport = "create";
		}
		else if (dbType.equals("MYSQL")) {
			dbUrl = "jdbc:mysql://localhost/openhds";
			dbDialect = "org.hibernate.dialect.MySQL5InnoDBDialect";
			dbDriver = "com.mysql.jdbc.Driver";
			dbType="MYSQL";
			hibernateExport = "update";
		}
		else if (dbType.equals("POSTGRES")) {
			dbUrl = "jdbc:postgresql://localhost/openhds";
			dbDialect = "org.hibernate.dialect.PostgreSQLDialect";
			dbDriver = "org.postgres.Driver";
			dbType="POSTGRES";
			hibernateExport = "update";
		}
	}
	
	public void create() {
		if (executeScript()) {
			Properties properties = readDatabaseProperties();	
			properties.put("dbType", dbType);
			properties.put("dbDriver", dbDriver);
			properties.put("dbUrl", dbUrl);
			properties.put("dbUser", dbUsername);
			properties.put("dbPass", dbPassword);
			properties.put("hibernateDialect", dbDialect);
			properties.put("hibernateExport", hibernateExport);
			properties.put("hibernateShowSql", "true");
			writePropertyFile(properties);
		}
	}
	
	public void editUrl() {
		HtmlInputText text = (HtmlInputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("j_id45:url");
		text.setDisabled(false);
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
		} catch (Exception e) {
			jsfService.addMessage("Error writing Property file. Exception : " + e.getMessage());
			return;
		}
		jsfService.addMessage("Database Configuration updated successfully. Redeploy the web application for changes to take effect.");
	}
	
	public boolean executeScript() {
		
		if (dbType.equals("MYSQL")) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = (Connection) DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

				ScriptRunner runner = new ScriptRunner(conn, false, true);
				runner.runScript(new BufferedReader(new FileReader("src/main/resources/openhds-schema.sql")));
				runner.runScript(new BufferedReader(new FileReader("src/main/resources/openhds-required-data.sql")));
				
			} 
			catch (Exception e) {
				jsfService.addMessage("Error executing script. Exception : " + e.getMessage());
				return false;
			}
		}
		return true;
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
