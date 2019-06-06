package org.openhds.web.beans;

import java.io.*;
import java.sql.DriverManager;
import java.util.Properties;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.event.ValueChangeEvent;
import org.openhds.service.JsfService;
import org.openhds.util.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.sql.Connection;

public class DatabaseConfigBean {
    Logger logger = LoggerFactory.getLogger(DatabaseConfigBean.class);

	String dbUsername;
	String dbPassword;
	String dbDriver;
	String dbUrl;
	String dbType;
	String dbDialect;
	String hibernateExport;
    String dbSaveTo;
    boolean showSql;
	
	JsfService jsfService;
	
	UIInput inputText = null;
    UIComponent saveToComponent = null;
	boolean testDataEnabled = false;
 //   private String mysqlVisibility;

    public DatabaseConfigBean() {
    }
    
    private void warnAndDisplay(String msg){
        logger.warn(msg);
        jsfService.addMessage(msg);
    }
    
    public String getDbSaveTo() {
        if( dbSaveTo == null ){
            // Probe OS. Probably doesn't cover all systems; also different paths
            // may be more appropriate on some versions of Windows and Mac.
            String osname = System.getProperty("os.name");
            if( osname.startsWith("Windows") ){
                dbSaveTo = "winUser";
            }else if( osname.startsWith("Mac") ){
                dbSaveTo = "macUser";
            }else{ /* Linux or unknown */
                dbSaveTo = "linuxUser";
            }
            logger.info("Initial DB Save To: "+dbSaveTo);
        }
        return dbSaveTo;
    }
    public void setDbSaveTo(String key){
        logger.info("Changing dbSaveTo from "+dbSaveTo+" to "+key);
        dbSaveTo = key;
    }
	
	public void updateType(ValueChangeEvent event) {
		dbType = event.getNewValue().toString();
		
		if (dbType.equals("H2")) {
			dbUrl = "jdbc:h2:mem:openhds";
            dbDialect = "";
			dbDriver = "org.h2.Driver";
			dbType = "H2";
			hibernateExport = "create";
		}
		else if (dbType.equals("MYSQL")) {
			dbUrl = "jdbc:mysql://localhost/openhds";
			dbDialect = "org.hibernate.dialect.MySQL5InnoDBDialect";
			dbDriver = "com.mysql.jdbc.Driver";
			dbType="MYSQL";
			hibernateExport = "update";
		}
	}
    
    public void saveChanges() {
        if( dbDriver == null || dbUrl == null ){
            warnAndDisplay("Error: DB URL or driver is not set");
            return;
        }
        if( dbUsername == null || dbPassword == null ){
            warnAndDisplay("Error: DB username or password is not set");
            return;
        }
        
        Resource res;
        if ("classpath".equals(dbSaveTo)) {
            res = new ClassPathResource("database.properties");
        }else{
            String home = System.getProperty("user.home");
            if ("linuxEtc".equals(dbSaveTo)){
                res = new FileSystemResource("/etc/openhds/database.properties");
            }else if ("linuxUser".equals(dbSaveTo)){
                res = new FileSystemResource(home+"/.config/openhds/database.properties");
            }else if ("macUser".equals(dbSaveTo)){
                res = new FileSystemResource(home+"/Library/Preferences/OpenHDS/database.properties");
            }else if ("winUser".equals(dbSaveTo)){
                res = new FileSystemResource(home+"/Local Settings/Application Data/OpenHDS/database.properties");
            }else{
                warnAndDisplay("Bad destination to save configuration to");
                return;
            }
        }
        
        Properties properties = new Properties();
        if( res.exists() ){
            if( readDatabaseProperties(res, properties) != true ){
                return; // error reading from res; abort (message already displayed)
            }
        }else{
            try{
                File path = res.getFile();
                File parent = path.getParentFile();
                if (parent == null){
                    warnAndDisplay("Error: parent of "+path.toString()+" is null");
                    return;
                }
                if (!parent.exists()){
                    if( !parent.mkdir() ){
                        warnAndDisplay("Error: unable to create directory "+parent.getName()+" in "+parent.getParent());
                        return;
                    }
                }
            }catch(IOException e){
                warnAndDisplay("Error creating file: "+e.getMessage());
                return;
            }
        }
        
        // Don't bother storing dbType since we can guess it
//        properties.put("dbType", dbType);
        properties.put("dbDriver", dbDriver);
        properties.put("dbUrl", dbUrl);
        properties.put("dbUser", dbUsername);
        properties.put("dbPass", dbPassword);
        if( dbDialect != null && dbDialect.length() > 0 )
            properties.put("hibernateDialect", dbDialect);
        if( hibernateExport != null )
            properties.put("hibernateExport", hibernateExport);
        properties.put("hibernateShowSql", getShowSql());
        
        writePropertyFile(res,properties);
    }
    
	public void editUrl() {	
		HtmlInputText text = (HtmlInputText) inputText;
		text.setDisabled(false);
	}

    /** Read a properties file, returning the result.
     * 
     * @param res Descriptor of the resource's location
     * @return True on success, false if a read error occurred
     */
	public boolean readDatabaseProperties(Resource res, Properties prop) {
        try {
            FileInputStream fis = new FileInputStream(res.getFile());
			if (fis != null) {
				prop.load(fis);
                fis.close();
            }
            logger.debug("Read properties from "+res.getURL());
            return true;
		} catch (IOException e) {
            warnAndDisplay("Error in reading Property file: " + e.getMessage());
			return false;
		}
	}
	
	public void writePropertyFile(Resource res, Properties props) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(res.getFile());
			props.store(fos, "Database Configuration updated");
            jsfService.addMessage("Database Configuration successfully saved to "+res.getURL()+
                    ". Redeploy the web application for changes to take effect.");
            logger.info("Database properties saved to "+res.getURL());
		} catch (Exception e) {
            warnAndDisplay("Error writing Property file: " + e.getMessage());
			return;
		}
	}
	
	public boolean initializeDB() {
		if (dbType.equals("MYSQL")) {
			try {
				Class.forName("com.mysql.jdbc.Driver"); // initialise the driver
				Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

				ScriptRunner runner = new ScriptRunner(conn, false, true);
				runner.runScript(new BufferedReader(
					      new InputStreamReader(
					      new ClassPathResource("openhds-schema.sql").getInputStream())));
				runner.runScript(new BufferedReader(
						  new InputStreamReader(
						  new ClassPathResource("openhds-required-data.sql").getInputStream())));
                jsfService.addMessage("MYSQL DB initialised successfully.");
                logger.info("MYSQL DB initialised successfully.");
			} 
			catch (Exception e) {
                warnAndDisplay("Error executing DB setup script: " + e.getMessage());
				return false;
			}
		}else{
            warnAndDisplay("Error: only MYSQL DBs can be initialised this way.");
        }
		return true;
	}
	
	public boolean executeTestScript() {
		if (dbType.equals("H2")) {
			try {
				Class.forName("org.h2.Driver"); // initialise the driver
				Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

				ScriptRunner runner = new ScriptRunner(conn, false, true);
				runner.runScript(new BufferedReader(
					      new InputStreamReader(
					      new ClassPathResource("testing-data.sql").getInputStream())));
				
				testDataEnabled = true;
			}
			catch (Exception e) {
				warnAndDisplay("Error executing DB setup script: " + e.getMessage());
				return false;
			}
		}
		else {
			jsfService.addMessage("To run the test data script, the database must be the default in-memory H2.");
			return false;
		}
		jsfService.addMessage("Test data loaded successfully.");
                logger.info("Test data loaded successfully.");
		return true;
	}
	
	public boolean isTestDataEnabled() { return testDataEnabled; }
	public void setTestDataEnabled(boolean testDataEnabled) { this.testDataEnabled = testDataEnabled; }

	public String getDbUsername() { return dbUsername; }
	public void setDbUsername(String dbUsername) { this.dbUsername = dbUsername; }
	
	public String getDbPassword() { return dbPassword; }
	public void setDbPassword(String dbPassword) { this.dbPassword = dbPassword; }
	
	public String getDbDriver() { return dbDriver; }
	public void setDbDriver(String dbDriver) { this.dbDriver = dbDriver; }
	
	public String getDbUrl() { return dbUrl; }
	public void setDbUrl(String dbUrl) { this.dbUrl = dbUrl; }
	
	public String getDbType() {
        if( dbType == null ){
            // It's possible (even likely) this value was not stored in a
            // properties file but we can still guess what it should be.
            if( dbDriver != null && dbDriver.startsWith("com.mysql") )
                dbType = "MYSQL";
            else dbType = "H2";
        }
		return dbType;
	}
	public void setDbType(String dbType) { this.dbType = dbType; }
	
	public String getDbDialect() { return dbDialect; }
	public void setDbDialect(String dbDialect) { this.dbDialect = dbDialect; }
	
	public JsfService getJsfService() { return jsfService; }
	public void setJsfService(JsfService jsfService) {
		this.jsfService = jsfService;
	}
	
	public void setInputText(UIInput inputText) { this.inputText = inputText; }
	public UIInput getInputText() { return inputText; }

    public String getShowSql() { return showSql ? "true" : "false"; }
    public void setShowSql(String b) {
        if( b.toLowerCase().startsWith("t") ) showSql = true;
        else showSql = false;
    }

    public String getMysqlDisplay(){
        if( getDbType().equals("H2") ) return "none";
        else return "block";
    }
    public String getH2Display(){
        if( getDbType().equals("H2") ) return "block";
        else return "none";
    }
}
