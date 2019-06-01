package org.openhds.service;

import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.PrivilegeConstants;

public interface FieldWorkerService {

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	FieldWorker evaluateFieldWorker(FieldWorker entityItem) throws ConstraintViolations;

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<String> getFieldWorkerExtIds(String term);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	FieldWorker findFieldWorkerById(String fieldWorkerId, String msg) throws Exception;
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	FieldWorker findFieldWorkerById(String fieldWorkerId) throws ConstraintViolations;

	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<FieldWorker> getAllFieldWorkers();
}
