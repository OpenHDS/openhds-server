package org.openhds.service.refactor;

import org.openhds.exception.ConstraintViolations;
import org.openhds.annotations.Authorized;
import org.openhds.domain.AuditableEntity;
import static org.openhds.domain.PrivilegeConstants.*;

import java.util.List;

public interface EntityService<T extends AuditableEntity> {
	@Authorized({ VIEW_ENTITY })
	public List<T> getAll();

	@Authorized({ VIEW_ENTITY })
	public T read(String id);

	@Authorized({ VIEW_ENTITY })
	public boolean isEligibleForCreation(T entity, ConstraintViolations cv);

	@Authorized({ DELETE_ENTITY })
	public void delete(T entity) throws IllegalArgumentException;

	@Authorized({ CREATE_ENTITY })
	public void create(T entity) throws ConstraintViolations;

	@Authorized({ EDIT_ENTITY })
	public void save(T entity) throws ConstraintViolations;
}