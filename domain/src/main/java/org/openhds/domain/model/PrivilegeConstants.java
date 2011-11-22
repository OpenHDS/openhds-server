package org.openhds.domain.model;

import org.openhds.domain.annotations.Authorized;

/**
 * These constants correspond to the privileges that are created and defined within the import.sql script
 * Instead of using Strings, this class provides better type safety when declaring privileges
 * within the {@link Authorized} annotation
 * 
 * NOTE: This class should be updated/changed whenever the privileges inside import.sql change
 * @author Dave Roberge
 *
 */
public class PrivilegeConstants {
	// basic creation privileges
	public final static String CREATE_ENTITY = "CREATE_ENTITY";
	public final static String EDIT_ENTITY = "EDIT_ENTITY";
	public final static String DELETE_ENTITY = "DELETE_ENTITY";
	public final static String VIEW_ENTITY = "VIEW_ENTITY";
	
	// baseline and update
	public final static String ACCESS_BASELINE = "ACCESS_BASELINE";
	public final static String ACCESS_UPDATE = "ACCESS_UPDATE";
	
	public final static String CREATE_USER = "CREATE_USER";
	public final static String DELETE_USER = "DELETE_USER";
}
