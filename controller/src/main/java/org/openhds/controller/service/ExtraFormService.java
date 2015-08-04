package org.openhds.controller.service;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.ExtraForm;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.TableDummy;

public interface ExtraFormService {

	public enum FormStatus { NEW, CREATING, READY, TRANSFERRING, COMPLETE };
	
	static class FormStatusCodes {
	    public static final int NEW = 0;
	    public static final int CREATING = 1;
	    public static final int READY = 2;
	    public static final int TRANSFERRING = 3;
	    public static final int COMPLETE = 4;
	}
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean isValidKey(String key) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean createTable(TableDummy table) throws ConstraintViolations;
	
	@Authorized({PrivilegeConstants.CREATE_ENTITY})
	boolean insertExtraFormData(ExtraForm extraForm) throws ConstraintViolations;
}
