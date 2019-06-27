package org.openhds.service.refactor;

import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.FieldWorker;

public interface FieldWorkerCrudService extends EntityService<FieldWorker> {
	public void generatePasswordHash(FieldWorker fieldWorker)
			throws ConstraintViolations;

	public FieldWorker generateId(FieldWorker fieldWorker)
			throws ConstraintViolations;
}