package org.openhds.service.refactor.crudhelper;

import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.AuditableEntity;
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