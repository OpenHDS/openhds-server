package org.openhds.service.refactor.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.openhds.exception.ConstraintViolations;
import org.openhds.idgeneration.FieldWorkerGenerator;
import org.openhds.service.refactor.FieldWorkerService;
import org.openhds.service.refactor.crudhelper.EntityCrudHelper;
import org.openhds.domain.FieldWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FieldWorkerServiceImpl implements FieldWorkerService {
	@Autowired
	@Qualifier("FieldWorkerCrudHelper")
	private EntityCrudHelper<FieldWorker> fieldWorkerCrudHelper;
	@Autowired
	@Qualifier("fieldWorkerIdGenerator")
	private FieldWorkerGenerator fieldWorkerGenerator;

	@Override
	public List<FieldWorker> getAll() {
		return fieldWorkerCrudHelper.getAll();
	}

	@Override
	public FieldWorker read(String id) {
		return fieldWorkerCrudHelper.read(id);
	}

	@Override
	public boolean isEligibleForCreation(FieldWorker fieldWorker,
			ConstraintViolations cv) {
		boolean alreadyExists = read(fieldWorker.getExtId()) != null;
		if (alreadyExists) {
			ConstraintViolations.addViolationIfNotNull(cv,
					"FieldWorker with given ExtId Already Exists.");
		}
		return alreadyExists;
	}

	@Override
	public void delete(FieldWorker fieldWorker) throws IllegalArgumentException {
		fieldWorkerCrudHelper.delete(fieldWorker);
	}

	@Override
	public void create(FieldWorker fieldWorker) throws ConstraintViolations {
		fieldWorkerCrudHelper.create(fieldWorker);
	}

	@Override
	public void save(FieldWorker fieldWorker) throws ConstraintViolations {
		fieldWorkerCrudHelper.save(fieldWorker);
	}

	@Override
	public void generatePasswordHash(FieldWorker fieldWorker)
			throws ConstraintViolations {
		if (null == fieldWorker.getPassword()
				|| null == fieldWorker.getConfirmPassword()) {
			throw new ConstraintViolations("Password or Confirmation is null.");
		}
		if (fieldWorker.getPassword().isEmpty()
				|| fieldWorker.getConfirmPassword().isEmpty()) {
			throw new ConstraintViolations("Password or Confirmation is empty.");
		}
		if (!fieldWorker.getPassword().equals(fieldWorker.getConfirmPassword())) {
			throw new ConstraintViolations("Passwords do not match.");
		}
		// HASH THAT BABY
		fieldWorker.setPasswordHash(BCrypt.hashpw(fieldWorker.getPassword(),
				BCrypt.gensalt(8)));
		fieldWorker.setPassword(null);
		fieldWorker.setConfirmPassword(null);
	}

	@Override
	public FieldWorker generateId(FieldWorker fieldWorker)
			throws ConstraintViolations {
		if (fieldWorkerGenerator.generated && null == fieldWorker.getExtId()) {
			fieldWorker.setExtId(fieldWorkerGenerator.generateId(fieldWorker));
		}
		return fieldWorker;
	}
}