package org.openhds.service.impl;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.dbcp2.BasicDataSource;
import org.openhds.exception.ConstraintViolations;
import org.openhds.service.ExtraFormService;
import org.openhds.dao.GenericDao;
import org.openhds.domain.ColumnDummy;
import org.openhds.domain.ExtraForm;
import org.openhds.domain.Form;
import org.openhds.domain.Individual;
import org.openhds.domain.Location;
import org.openhds.domain.SocialGroup;
import org.openhds.domain.TableDummy;
import org.openhds.domain.Visit;
import org.openhds.domain.ExtraForm.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ExtraFormServiceImpl:
 * 
 * Service implementation that is used to create ExtraForm tables and 
 * insert ExtraForm data into the ExtraForm tables.
 * <p>
 * All the database commands are executed in the current database schema that
 * is taken from the connection url.
 *
 */
@Transactional
@Component("extraFormService")
public class ExtraFormServiceImpl implements ExtraFormService {

	@Autowired
    private GenericDao genericDao;
	
	@Autowired
	private BasicDataSource dataSource;
	
	private String schemaName = "openhds"; // Set to default
		
	@PostConstruct 
	private void setCatalog() throws ConstraintViolations{
		try (Connection connection = dataSource.getConnection()){	
			String catalog = connection.getCatalog();
			if(catalog.trim().length() > 0 ) schemaName = catalog;
			else throw new ConstraintViolations("Could not find Database schema name!");
		} catch (SQLException e) {
			throw new ConstraintViolations(e.getMessage());
			//throw new ConstraintViolations("Exception while trying to assign Database schema name!");
		}
	}
	/**
	 * Checks if the supplied key exists in the OpenHDS form table.
	 * 
	 *  @param  key UUID of an extra-form
	 * 	@return     boolean true if the specified UUID exists in the openHDS form table
	 *  @throws		ConstraintViolations if there is no Form with this UUID
	 *  @see		ConstraintViolations
	 * 	@see		GenericDao
	 */
	@Override
	public boolean isValidKey(String key) throws ConstraintViolations {
		Form form = genericDao.findByProperty(Form.class, "uuid", key);
		if(form == null) throw new ConstraintViolations("Form key not valid."); else return true;
	}
	
	/**
	 * Create new extra form table. If there was an Exception during creation, this method will
	 * return throw a ConstraintViolation with more details on why it failed.
	 * <p>
	 * First the SQL statements for creating and altering the extra-form table are created.
	 * Afterwards these SQL statements are executed. If this was successful, the CORE TABLE name
	 * is inserted into the forms database and and if this was successful, the status for this extra-
	 * form in the form table is set to READY (2). If the extra-form table could be created but the ALTER 
	 * command was not successful, will try to DROP the table again.
	 * 
	 *  @param  table	Object of type TableDummy
	 * 	@return      	A boolean indicating if the creation of the extraForm table was successful
	 	@throws			ConstraintViolations if there was a problem during creation of extra table
	 *  @see			ConstraintViolations
	 *  @see			TableDummy
	 */
	@Override
	public boolean createTable(TableDummy table) throws ConstraintViolations{
		ConstraintViolations constraintViolations = new ConstraintViolations();
		String createSQLCommand = createTableSQLCommand(table);
		String alterCommand = alterTableForeignKey(createSQLCommand, table.getName());
		boolean successfullyCreatedTable = executeMySQLStatement(createSQLCommand, constraintViolations);		
		if(successfullyCreatedTable){
			boolean successfullyAlteredTable = executeMySQLStatement(alterCommand, constraintViolations);
			if(successfullyAlteredTable){			
				boolean insertCoreTableNameSuccessfull = insertCoreTableName(table.getKey(), table.getName(), constraintViolations);		
				if(insertCoreTableNameSuccessfull){
					/*boolean changeStatus = */changeFormStatus(FormStatus.READY, table.getKey());
					return true;
				}
			}else{
				//Drop table.
				/*boolean dropSuccess =*/ dropTable(table.getName());
				throw new ConstraintViolations("Core table could not be altered.");
			}
		}	
		if(constraintViolations.hasViolations()){
			throw constraintViolations;
		}
		return false;
	}
	
	/**
	 * Returns a SQL ALTER statement based on the supplied SQL CREATE statement input. 
	 * <p>
	 * This method will look for certain keywords in the CREATE statement and will insert
	 * FOREIGN KEY references to the OpenHDS tables visit, individual, socialgroup 
	 * or location accordingly.
	 * <p>
	 * Keywords that are checked for in the SQL CREATE statement are:
	 * - OPENHDS_VISIT_ID
	 * - OPENHDS_INDIVIDUAL_ID, INDIVIDUAL_INFO_INDIVIDUAL_ID
	 * - OPENHDS_HOUSEHOLD_ID
	 * - OPENHDS_LOCATION_ID
	 * 
	 *  @param  sql			SQL CREATE extra-form table statement
	 *  @param  coreTable	name of the extra-form CORE TABLE 
	 * 	@return      		A String containing the ALTER SQL statement for the coreTable 
	 * 						with foreign keys
	 */
	private String alterTableForeignKey(String sql, String coreTable){
		StringBuilder builder = new StringBuilder();		
		builder.append("ALTER TABLE " + schemaName + "." + coreTable +" ");
		
		//ADD FOREIGN KEY CONSTRAINTS
		if(sql.contains("OPENHDS_VISIT_ID")){
			builder.append("ADD VISIT_UUID VARCHAR(32) NOT NULL, ");
			builder.append("ADD CONSTRAINT "+ coreTable + "_vuuidfk_1 FOREIGN KEY (VISIT_UUID) ");
			builder.append("REFERENCES " + schemaName + ".visit(uuid), ");
		}		
		if(sql.contains("OPENHDS_INDIVIDUAL_ID") || sql.contains("INDIVIDUAL_INFO_INDIVIDUAL_ID")){
			builder.append("ADD INDIVIDUAL_UUID VARCHAR(32) NOT NULL, ");
			builder.append("ADD CONSTRAINT " + coreTable + "_iuuidfk_1 FOREIGN KEY (INDIVIDUAL_UUID) ");
			builder.append("REFERENCES " + schemaName + ".individual(uuid), ");
		}		
		if(sql.contains("OPENHDS_HOUSEHOLD_ID")){
			builder.append("ADD HOUSEHOLD_UUID VARCHAR(32) NOT NULL, ");
			builder.append("ADD CONSTRAINT " + coreTable + "_hhuuidfk_1 FOREIGN KEY (HOUSEHOLD_UUID) ");
			builder.append("REFERENCES " + schemaName + ".socialgroup(uuid), ");
		}		
		if(sql.contains("OPENHDS_LOCATION_ID")){
			builder.append("ADD LOCATION_UUID VARCHAR(32) NOT NULL, ");
			builder.append("ADD CONSTRAINT " + coreTable + "_luuidfk_1 FOREIGN KEY (LOCATION_UUID) ");
			builder.append("REFERENCES " + schemaName + ".location(uuid), ");
		}		
		if(builder.toString().contains(",")){
			builder.deleteCharAt(builder.lastIndexOf(","));
		}
		
		builder.append(";");		
		builder.append("\n");
		return builder.toString();
	}
	
	/**
	 * Creates the SQL CREATE Statement to be used to create a new table for an ExtraForm.
	 * <p>
	 * The SQL statement is created by iterating over all ColumnDummy entries in the
	 * TableDummy. The statement will also assign the PRIMARY KEY(s) to the table.
	 * 
	 *  @param  table	Object of type TableDummy
	 * 	@return      	A SQL CREATE TABLE statement for creating the new extra-form table 
	 *  @see			TableDummy
	 *  @see			ColumnDummy
	 */
	private String createTableSQLCommand(TableDummy table){
		StringBuilder builder = new StringBuilder();
		String tableName = table.getName();
		builder.append("CREATE TABLE `" + schemaName + "`.`" + tableName + "` ( ");
		int numberCol= table.getColumns().size();
		int i=1;
		for(ColumnDummy column: table.getColumns()){
			String columnName = column.getName();
			String defaultValue = column.getDefault_value();
			String typeName = column.getType();
			String nullable = column.getAllow_null();
			String columnSize = column.getSize();
			
			if(column.getType().equalsIgnoreCase("STRING")){
				typeName = "TEXT";
				columnSize = "255";
			}
			else if(column.getType().equalsIgnoreCase("JRDATE")){
				typeName = "DATETIME";
			}
			else if(column.getType().equalsIgnoreCase("JRDATETIME")){
				typeName = "DATETIME";
			}	
			else if(column.getType().equalsIgnoreCase("SELECTN") || column.getType().equalsIgnoreCase("SELECT1")){
				typeName = "TEXT";
				columnSize = "255";
			}	
			else if(column.getType().equalsIgnoreCase("INTEGER")){
				typeName = "INT";
				columnSize = "9";
			}
			else if(column.getType().equalsIgnoreCase("DECIMAL")){
				typeName = "DECIMAL";
				columnSize = "38,10";
			}

			builder.append("`" + columnName + "` " );
			builder.append(typeName + " ");
			
			if(!typeName.equalsIgnoreCase("DATETIME"))
				builder.append("(" + columnSize + ") ");
			
			if(nullable != null && nullable.equalsIgnoreCase("false")){
				builder.append("NOT NULL ");
			}
			
			if(defaultValue != null && defaultValue.trim().length() > 0 && defaultValue!= "null"){
				builder.append("DEFAULT '" + defaultValue + "' ");
			}
			if (i<numberCol){
			builder.append(", \n");
			i++;
			}
		}
		String primaryKey = table.getPrimaryKey();
		if(primaryKey != null && primaryKey.length() > 0){
			builder.append("PRIMARY KEY ( ");
			builder.append("`" + primaryKey + "` ");
			builder.append(")\n");
		}	
		builder.append(");");
		return builder.toString();
	}
		
	/**
	 * Executes the supplied SQL statement on the OpenHDS database. 
	 * 
	 *  @param  sql		SQL statement that will be executed
	 *  @param  constraintViolations		Will be used to store failure reason in case of exception
	 * 	@return      	A boolean, indicating if the execution was successful
	 *  @see			ConstraintViolations
	 *  @see			SQLException
	 *  @see			SQLSyntaxErrorException
	 */
	private boolean executeMySQLStatement(String sql, ConstraintViolations constraintViolations){
		boolean success = false;
		try (Connection connection = dataSource.getConnection();
				Statement stmt = connection.createStatement();){	
			/*int resultCount = */stmt.executeUpdate(sql); //Will return 0
			success = true;
		}
		catch(SQLException sqlException){
			if (sqlException.getErrorCode() == 1050 ) {
		        // Database already exists error
		        constraintViolations.addViolations("Duplicate: " + sqlException.getMessage());
		    } 
			else if(sqlException instanceof SQLSyntaxErrorException){
				constraintViolations.addViolations("SQLSyntaxErrorException: " + sqlException.getMessage());
			} 
			else{
				constraintViolations.addViolations("Exception: " + sqlException.getMessage());
			}
		}
		return success;
	}
	
	/**
	 * Inserts the supplied CORE TABLE name into the OpenHDS form database for the entry with the 
	 * specified UUID.
	 * 
	 *  @param  key		UUID of the form table entry that should be updated
	 *  @param  tableName		CORE TABLE name that should be inserted into the table
	 *  @param  constraintViolations		Will be used to store failure reason in case of exception
	 * 	@return      	A boolean, indicating if the insertion of the CORE TABLE name was successful
	 *  @see			ConstraintViolations
	 *  @see			SQLSyntaxErrorException
	 */
	private boolean insertCoreTableName(String key, String tableName, ConstraintViolations constraintViolations){
		boolean success = false;
		String sql = "UPDATE " + schemaName + ".form SET coreTable = ? WHERE uuid = ?";	
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setString(1, tableName);
			ps.setString(2, key);
			int rowCount = ps.executeUpdate();
			if(rowCount > 0){
				success = true;
			}
			else{
				constraintViolations.addViolations("COULD NOT INSERT CORE_TABLE DATA");
			}
		}
		catch(SQLException sqlException){
			constraintViolations.addViolations("Exception while trying to insert CORE_TABLE name into forms.");
		}
		return success;
	}
	
	/**
	 * Drop the specified table from the database.
	 * 
	 *  @param  table	Name of the table that should be dropped
	 * 	@return      	A boolean, indicating if the drop command could be
	 * 					executed successfully
	 *  @throws			ConstraintViolations
	 *  @see			SQLSyntaxErrorException
	 */
	private boolean dropTable(String table) throws ConstraintViolations{			
		String tableName = schemaName + "." + table;	
		String sql = "DROP TABLE "+ tableName + ";";		
		try (Connection connection = dataSource.getConnection();
				Statement stmt = connection.createStatement()){
			/*int rowCount =*/ stmt.executeUpdate(sql);			
			return true;
		}
		catch(SQLException sqlException){
			throw new ConstraintViolations("Could not drop table " + table);
		}
	}
	
	
	/**
	 * Sets the status of the form entry with specified UUID to FormStatus. 
	 * The possible statuses are defined in the FormStatus enum and are mapped before
	 * inserting into the database to the int values defined in FormStatusCodes.
	 * <p>
	 * In case status could not be set, will throw a ConstraintViolations.
	 * 
	 *  @param  formStatus	The status that this form entry will be set to
	 *  @param  uuid	The UUID of the entry in form table whose status should be changed
	 * 	@return      	A boolean, indicating if the form status could be changed
	 *  @throws			ConstraintViolations
	 *  @see			FormStatus
	 *  @see			FormStatusCodes
	 *  @see			SQLException
	 */
	private boolean changeFormStatus(FormStatus formStatus, String uuid) throws ConstraintViolations{	
		String sql = "UPDATE `"+ schemaName + "`.`form` SET `status`= ? WHERE `uuid`= ? ;";		
		int statusCode = -1;	
		switch(formStatus){
			case NEW:{
				statusCode = FormStatusCodes.NEW;
				break;
			}
			case CREATING:{
				statusCode = FormStatusCodes.CREATING;
				break;
			}
			case READY:{
				statusCode = FormStatusCodes.READY;
				break;
			}
			case TRANSFERRING:{
				statusCode = FormStatusCodes.TRANSFERRING;
				break;
			}
			case COMPLETE:{
				statusCode = FormStatusCodes.COMPLETE;
				break;
			}
			default:{
				return false;
			}	
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, new Integer(statusCode));
			ps.setString(2, uuid);
			int rowCount = ps.executeUpdate();
			if(rowCount > 0){
				return true;
			}
		}
		catch(SQLException sqlException){	
			throw new ConstraintViolations("Could not change form status.");
		}
		return false;
	}
	
	/**
	 * ---------------------------------------------------------------------------------------------------------------------------------------
	 */

	/**
	 * Inserts the supplied ExtraForm data submission into the extra-form table.
	 * <p>
	 * First the CORE TABLE name will be read from the forms table. Then the SQL
	 * INSERT statement is prepared with placeholders for values and foreign key(s).
	 * This SQL INSERT statement is then parsed for certain keywords to get the foreign
	 * key references. Afterwards the final SQL INSERT statement is created, ready to be 
	 * used in a PreparedStatement. Finally the insert statement is executed.
	 * 
	 *  @param  extraForm	Object that contains the extra-form submission information that
	 *  					should be inserted into the extra-form table
	 * 	@return      	A boolean, indicating if the insertion of the supplied extra-form 
	 * 					submission was successful
	 *  @throws			ConstraintViolations if could not insert extra-form data
	 *  @see			ExtraForm
	 */
	@Override
	public boolean insertExtraFormData(ExtraForm extraForm) throws ConstraintViolations {
		String formName = extraForm.getFormName();
		String tableName = getCoreTableNameFromExtraForm(formName);
		String query = createInsertSqlStatementWithPlaceholders(extraForm, tableName);
		//Check for foreign key and insert FK reference data
		Map<String, String> foreignKeyData = getForeignKeyData(extraForm, query);
		query = buildInsertStatement(query, foreignKeyData);
		insertData(extraForm, query);
		return false;
	}
	
	/**
	 * Returns a SQL INSERT statement with foreign key references.  
	 * <p>
	 * Appends the foreign key data (column names and values) to the specified
	 * input query string, ready to be used in a PreparedStatement.
	 * 
	 *  @param  query	
	 * 	@return      	A String that can be used in the insertData function 
	 *  @see	insertData
	 */
	private String buildInsertStatement(String query, Map<String, String> foreignKeyData){
		StringBuilder sbColumn = new StringBuilder();
		StringBuilder sbData = new StringBuilder();
		Set<String> keys = foreignKeyData.keySet();
		for(String key: keys){
			sbColumn.append(", ");
			sbData.append(", ");
			sbColumn.append(key);
			sbData.append(foreignKeyData.get(key));
		}
		query = String.format(query, sbColumn.toString(), sbData.toString());
		return query;
	}

	/**
	 * Inserts the supplied ExtraForm data submission into the extra-form table.
	 * <p>
	 * Will go through the ExtraForm.Data objects and inject the values into the
	 * the PreparedStatement according to the data type (INT, DECIMAL, etc.).
	 * 
	 *  @param  extraForm	Object that contains the extra-form submission information that
	 *  					should be inserted into the extra-form table
	 *  @param	query		SQL INSERT statement with placeholders for the PreparedStatement
	 *  @throws			ConstraintViolations if extra-form data could not be inserted 
	 *  @see			ExtraForm.Data
	 */
	private void insertData(ExtraForm extraForm, String query) throws ConstraintViolations {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(query);) {		
			int pointer = 1;
			for (ExtraForm.Data d : extraForm.getData()) {
				if (d.value == null || d.value.length() == 0) {
					
					String type = d.type;
					
					if(type.equalsIgnoreCase("STRING")){
//						typeName = "VARCHAR";
						type = "TEXT";
					}
					else if(type.equalsIgnoreCase("JRDATE")){
						type = "DATETIME";
					}
					else if(type.equalsIgnoreCase("JRDATETIME")){
						type = "DATETIME";
					}	
					else if(type.equalsIgnoreCase("SELECTN") || type.equalsIgnoreCase("SELECT1")){
						type = "TEXT";
					}	
					else if(type.equalsIgnoreCase("INTEGER")){
						type = "INT";
					}
					
					if (type.equalsIgnoreCase("INT")) {
						pstmt.setNull(pointer, java.sql.Types.INTEGER);
					} else if (type.equalsIgnoreCase("DECIMAL")) {
						pstmt.setNull(pointer, java.sql.Types.DECIMAL);
					} else if (type.equalsIgnoreCase("DATETIME")) {
						pstmt.setNull(pointer, java.sql.Types.DATE);
					} else if (type.equalsIgnoreCase("VARCHAR")) {
						pstmt.setNull(pointer, java.sql.Types.VARCHAR);
					} else {
						pstmt.setString(pointer, d.value);
					}
				} else {
					if((d.type.equalsIgnoreCase("INTEGER") || d.type.equalsIgnoreCase("INT")) && d.value.equals("null")){
						pstmt.setNull(pointer, java.sql.Types.INTEGER);
					}
					else
						pstmt.setString(pointer, d.value);
				}
				pointer++;
			}
			/*int rowCount =*/ pstmt.executeUpdate();
		}
		catch (SQLException e) {	
			if(e instanceof SQLIntegrityConstraintViolationException ){
				throw new ConstraintViolations(e.getMessage());
			}
			else{
				throw new ConstraintViolations(e.getMessage());
			}
		}
		catch(Exception e){
			throw new ConstraintViolations(e.getMessage());
		}
	}
	
	/**
	 * Creates a SQL Insert Statement stub with placeholders for inserting values in a 
	 * PreparedStatement and placeholders foreign key columns and values.
	 * 
	 *  @param  extraForm	Object that contains the extra-form submission information that
	 *  					should be inserted into the extra-form table
	 *  @param tableName	The name of the CORE TABLE into which the extra-form data is inserted
	 * 	@return      	The SQL INSERT with placeholders
	 *  @see			ExtraForm
	 */
	private String createInsertSqlStatementWithPlaceholders(ExtraForm extraForm, String tableName){		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		sb1.append("INSERT INTO " + schemaName + "." + tableName);
		sb1.append(" ( ");
		sb2.append(" VALUES (");
		int count = 1;
		for(ExtraForm.Data d: extraForm.getData()){
			sb1.append(d.columnName);
			sb2.append("?");
			
			if(count < extraForm.getData().size()){
				sb1.append(", ");
				sb2.append(", ");
			}
			count++;
		}		
		//ADD FOREIGN KEY DATA placeholders
		sb1.append(" %s");
		sb2.append(" %s");
		
		sb1.append(" )");
		sb2.append(" );");
		
		String query = sb1.toString() + " " + sb2.toString();		
		return query.toString();
	}
	
	/**
	 * Creates a SQL Insert Statement stub with placeholders for inserting values in a 
	 * PreparedStatement and placeholders foreign key columns and values.
	 * 
	 *  @param formName	The id of the form we are searching the CORE TABLE name for
	 * 	@return      	CORE TABLE name for this ford id
	 *  @throws	ConstraintViolations	If the CORE TABLE name is null or empty or DB Exception
	 */
	private String getCoreTableNameFromExtraForm(String formName) throws ConstraintViolations{
		String coreTableName = null;
		try (Connection connection = dataSource.getConnection()) {  
		    Statement stmt = connection.createStatement();
		    ResultSet rs;
		    
		    rs = stmt.executeQuery("SELECT formName, active, deleted, gender, insertDate, coreTable FROM " + schemaName + ".form WHERE formName = '" + formName + "'");
		    while (rs.next()) {
		        String CORE_TABLE = rs.getString("coreTable");
		        if(CORE_TABLE != null)
		        	coreTableName = CORE_TABLE;
		    } 
		} catch (SQLException e) {
		    throw new ConstraintViolations("Cannot connect the database!");
		}
		if(coreTableName == null || coreTableName.trim().length() == 0){
			throw new ConstraintViolations("Could not find coreTable name.");
		}	
		return coreTableName;
	}	
	
	/**
	 * Parses through SQL INSERT query, looking for certain keyword, extracts FOREIGN KEY references 
	 * according to the values found and returns them in a Map for further processing.
	 * <p>
	 * Will look for these keywords:
	 * - OPENHDS_VISIT_ID
	 * - OPENHDS_INDIVIDUAL_ID
	 * - OPENHDS_HOUSEHOLD_ID
	 * - OPENHDS_LOCATION_ID
	 * If one of these is found, gets the appropriate extId from the extraform object and puts the 
	 * value into a map for further processing.
	 * 
	 *  @param extraForm	Object that contains the extra-form submission information that
	 *  					should be inserted into the extra-form table
	 *  @param query		SQL INSERT statement with placeholders for FK data and VALUES
	 * 	@return A Map<String, String> containing the UUID of the references found 
	 *  @throws	ConstraintViolations	If an object could not be found with the given extId
	 */
	private Map<String, String> getForeignKeyData(ExtraForm extraForm, String query) throws ConstraintViolations{
		Map<String, String> foreignKeyData = new HashMap<String, String>();
		if(query.contains("OPENHDS_VISIT_ID")){
			Visit visit = genericDao.findByProperty(Visit.class, "extId", extraForm.getVisitId());
			if(visit==null) throw new ConstraintViolations("Could not find visit with extId " + extraForm.getVisitId());
			foreignKeyData.put("VISIT_UUID", "'" + visit.getUuid() + "'");
		}
		if(query.contains("OPENHDS_INDIVIDUAL_ID") || query.contains("INDIVIDUAL_INFO_INDIVIDUAL_ID")){
			Individual individual = genericDao.findByProperty(Individual.class, "extId", extraForm.getIndividualId());
			if(individual==null) throw new ConstraintViolations("Could not find individual with extId " + extraForm.getIndividualId());
			foreignKeyData.put("INDIVIDUAL_UUID", "'" + individual.getUuid() + "'");
		}
		if(query.contains("OPENHDS_HOUSEHOLD_ID")){
			SocialGroup socialGroup = genericDao.findByProperty(SocialGroup.class, "extId", extraForm.getSocialGroupId());
			if(socialGroup==null) throw new ConstraintViolations("Could not find socialGroup with extId " + extraForm.getSocialGroupId());
			foreignKeyData.put("HOUSEHOLD_UUID", "'" + socialGroup.getUuid() + "'");
		}
		if(query.contains("OPENHDS_LOCATION_ID")){
			Location location = genericDao.findByProperty(Location.class, "extId", extraForm.getLocationId());
			if(location==null) throw new ConstraintViolations("Could not find location with extId " + extraForm.getLocationId());
			foreignKeyData.put("LOCATION_UUID", "'" + location.getUuid() + "'");
		}
		return foreignKeyData;
	}
	
	@Override
	public List<ExtraForm> getForms(String formId) throws Exception{
		List<ExtraForm> extraFormList = null;
		try {
			extraFormList = getSubmissionsForExtraFormId(formId);
		} catch (Exception e) {
			throw new Exception("Error while trying to read extra form submissions");
		}
		return extraFormList;
	}
	
	private List<ExtraForm> getSubmissionsForExtraFormId(String formId) throws Exception{		
		List<ExtraForm> extraFormList = new ArrayList<ExtraForm>();		
		try (Connection connection = dataSource.getConnection();
				Statement stmt = connection.createStatement();){	
			
			String coreTable = getCoreTableNameFromExtraForm(formId);
			String sql = "SELECT * FROM " + coreTable;		
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
						
			while(rs.next()){					
				ExtraForm extraForm = new ExtraForm();
				extraForm.setFormName(formId);
				List<Data> dataList = new ArrayList<Data>();

				for(int i = 1; i<= rsmd.getColumnCount(); i++){
					String columnName = rsmd.getColumnName(i);
					String columnTypeName = rsmd.getColumnTypeName(i);
					String value = rs.getString(columnName);
					
					Data data = new Data();
					data.columnName = columnName;
					data.value = value;
					data.type = columnTypeName;
					dataList.add(data);
				}
				
				extraForm.setData(dataList);
				extraFormList.add(extraForm);
			}
		}
		catch (Exception e) {
			throw e;
		}
		return extraFormList;
	}
}
