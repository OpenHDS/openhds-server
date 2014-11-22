package org.openhds.controller.service.refactor.crudhelpers;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.domain.model.AuditableEntity;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Created by Wolfe 9/8/14.
 */
public interface EntityCrudHelper<T extends AuditableEntity> {
	public List<T> getAll();

	public T read(String id);

	@Transactional
	public void delete(T entity) throws IllegalArgumentException;

	@Transactional(rollbackFor = Exception.class)
	public void create(T entity) throws ConstraintViolations;

	@Transactional
	public void save(T entity) throws ConstraintViolations;
}