package org.openhds.web.cvt;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhds.dao.service.Dao;
import org.springframework.transaction.annotation.Transactional;

/**
 * A generic converter capable of transforming an entity into a string, and a
 * string into an entity.
 *
 * @author brent
 *
 * @param <T>
 * @param <PK>
 */

public class EntityConverterImpl<T, PK extends Serializable> implements EntityConverter<T> {

	private static Log log = LogFactory.getLog(EntityConverterImpl.class);

	Dao<T, PK> entityDao;
	Class<T> entityClass;
	Class<PK> keyClass;
	Constructor<PK> keyConstructor;
	Method keyGetter;

	public EntityConverterImpl(Dao<T, PK> entityDao, Class<T> entityClass,
			Class<PK> keyClass) {
		if (entityClass == null)
			throw new IllegalArgumentException("converter requires entity type");
		this.entityClass = entityClass;
		if (keyClass == null)
			throw new IllegalArgumentException("converter requires key class");
		this.keyClass = keyClass;

		try {
			this.keyConstructor = keyClass.getConstructor(String.class);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"failed to lookup key constructor", e);
		}
		if (entityDao == null)
			throw new IllegalArgumentException("converter requires entity dao");
		this.entityDao = entityDao;
		try {
			this.keyGetter = entityClass.getMethod("getUuid");
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"entity class missing uuid getter method");
		}
	}
	
	@Transactional
	public T getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value == null || value.length() == 0) {
			return null;
		}
		PK key = null;
		try {
			key = keyConstructor.newInstance(value);
		} catch (Exception e) {
			log.error("failed to create key", e);
		}
		return entityDao.read(key);
	}

	@SuppressWarnings("unchecked")
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null)
			return null;
		PK key = null;
		if (entityClass.isAssignableFrom(value.getClass())) {
			try {
				key = (PK) keyGetter.invoke(value);
			} catch (Exception e) {
				throw new RuntimeException("failed to get entity id", e);
			}
		}
		return key == null ? "" : key.toString();
	}
}
