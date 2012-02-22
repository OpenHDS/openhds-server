package org.openhds.web.cvt;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.openhds.dao.service.Dao;
import org.springframework.transaction.annotation.Transactional;

/**
 * A generic converter capable of transforming an entity property into a string,
 * and a string into an entity, by looking it up by property. The property value
 * must uniquely identify the entity. This is slightly different in that an error
 * is not reported in the entity appears as null.
 * 
 * @author brent
 * 
 * @param <T>
 * @param <PK>
 */
public class OptionalEntityPropertyConverterImpl<T, PK extends Serializable> implements
		Converter {

	Dao<T, PK> entityDao;
	String propertyName;
	Class<T> entityClass;
	Method propertyGetter;

	public OptionalEntityPropertyConverterImpl(Dao<T, PK> entityDao,
			Class<T> entityClass, String propertyName)
			throws IntrospectionException {
		if (entityClass == null)
			throw new IllegalArgumentException("converter requires entity type");
		if (entityDao == null)
			throw new IllegalArgumentException("converter requires dao");
		if (propertyName == null)
			throw new IllegalArgumentException(
					"converter requires property name for class " + entityClass);

		this.entityDao = entityDao;
		this.entityClass = entityClass;
		this.propertyName = propertyName;

		// Find getter for specified property
		PropertyDescriptor[] propDescriptors = Introspector.getBeanInfo(
				entityClass).getPropertyDescriptors();

		for (PropertyDescriptor pd : propDescriptors) {
			if (propertyName.equals(pd.getName())) {
				this.propertyGetter = pd.getReadMethod();
				break;
			}
		}
	}

	@Transactional
	public T getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		T entity = entityDao.findByProperty(propertyName, value);
		
		if (entity == null) {	
			try {
				return entityClass.newInstance();
			} catch (Exception e) {
				e.getMessage();
			}
		}
		
		return entity;
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null)
			return null;
		Object propertyValue = null;
		if (entityClass.isAssignableFrom(value.getClass())) {
			try {
				propertyValue = propertyGetter.invoke(value);
			} catch (Exception e) {
				throw new RuntimeException("failed to get entity id", e);
			}
		} else
			throw new RuntimeException("value of type " + value.getClass()
					+ " not assignable to type " + entityClass);
		return propertyValue == null ? "" : propertyValue.toString();
	}
}
