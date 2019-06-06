package org.openhds.service.refactor.crudhelper;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.refactor.FieldWorkerService;
import org.openhds.domain.FieldWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created by wolfe on 9/19/14.
 */
@Component("FieldWorkerCrudHelper")
public class FieldWorkerCrudHelper extends
		AbstractEntityCrudHelperImpl<FieldWorker> {
	@Autowired
	FieldWorkerService fieldWorkerService;

	@Override
	protected void preCreateSanityChecks(FieldWorker fieldWorker)
			throws ConstraintViolations {
	}

	@Override
	protected void cascadeReferences(FieldWorker fieldWorker)
			throws ConstraintViolations {
		fieldWorkerService.generateId(fieldWorker);
		fieldWorkerService.generatePasswordHash(fieldWorker);
	}

	@Override
	protected void validateReferences(FieldWorker fieldWorker)
			throws ConstraintViolations {
		ConstraintViolations constraintViolations = new ConstraintViolations();
		if (!fieldWorkerService.isEligibleForCreation(fieldWorker,
				constraintViolations)) {
			throw (constraintViolations);
		}
	}

	@Override
	public List<FieldWorker> getAll() {
		return genericDao.findAll(FieldWorker.class, true);
	}

	@Override
	public FieldWorker read(String id) {
		return genericDao.findByProperty(FieldWorker.class, "extId", id);
	}
}