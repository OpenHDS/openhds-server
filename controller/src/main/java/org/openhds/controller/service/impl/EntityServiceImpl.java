package org.openhds.controller.service.impl;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.CurrentUser;
import org.openhds.controller.service.EntityService;
import org.openhds.controller.service.EntityValidationService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.AuditableCollectedEntity;
import org.openhds.domain.model.AuditableEntity;
import org.openhds.domain.model.User;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base Entity Service Class Implementation
 * This class is meant to be a generic implementation that can be used by all entities
 * It also aims to provide "hooks" so that in specialized cases, business logic can be added
 * to a transaction. For example, if something needs to be checked before the transaction, a subclass
 * could override the onBeforeCommit method and add the necessary logic before the commit happens
 * 
 * @author Dave
 *
 * @param <T>
 * @param <PK>
 */
@SuppressWarnings("unchecked")
public class EntityServiceImpl implements EntityService {
	private GenericDao genericDao;
	private CurrentUser currentUser;
	private CalendarUtil calendarUtil;
	private SitePropertiesService siteProperties;
	private EntityValidationService classValidator;
	
	public EntityServiceImpl(GenericDao genericDao, CurrentUser currentUser, CalendarUtil calendarUtil, SitePropertiesService siteProperties, EntityValidationService classValidator) {
		this.genericDao = genericDao;
		this.currentUser = currentUser;
		this.calendarUtil = calendarUtil;
		this.siteProperties = siteProperties;
		this.classValidator = classValidator;
	}

	@Transactional
	public <T> void create(T entityItem) throws IllegalArgumentException, ConstraintViolations, SQLException {
		if (entityItem instanceof AuditableEntity) {
			try {
		        Method insertByMethod = null;
		        Method insertDateMethod = null;
		
		        insertByMethod = entityItem.getClass().getMethod("setInsertBy", User.class);
		        if (currentUser != null) {
		            insertByMethod.invoke(entityItem, currentUser.getCurrentUser());
		        }
		        
		        Calendar insertDate = calendarUtil.convertDateToCalendar(new Date());
		        
		        insertDateMethod = entityItem.getClass().getMethod("setInsertDate", Calendar.class);
		        insertDateMethod.invoke(entityItem, insertDate);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		setStatusPending(entityItem);	
		classValidator.validateEntity(entityItem);
			
		genericDao.create(entityItem);
	}
	
	@Transactional
	public <T> void delete(T persistentObject) throws SQLException {
        Method setDeletedMethod = null;
        if (persistentObject instanceof AuditableEntity) {
            Method voidedByMethod = null;
            Method voidedDateMethod = null;

            try {
                voidedByMethod = persistentObject.getClass().getMethod("setVoidBy", User.class);
                voidedByMethod.invoke(persistentObject, currentUser.getCurrentUser());

                Calendar voidDate = calendarUtil.convertDateToCalendar(new Date());
                voidedDateMethod = persistentObject.getClass().getMethod("setVoidDate", Calendar.class);
                voidedDateMethod.invoke(persistentObject, voidDate);

                setDeletedMethod = persistentObject.getClass().getMethod("setDeleted", boolean.class);
                setDeletedMethod.invoke(persistentObject, true);
            } catch (Exception e) {
            	e.printStackTrace();
            }
            setStatusVoided(persistentObject);
            genericDao.update(persistentObject);
        }
        else if (persistentObject instanceof User) {
        	try {
	        	setDeletedMethod = persistentObject.getClass().getMethod("setDeleted", boolean.class);
	            setDeletedMethod.invoke(persistentObject, true);
	            genericDao.update(persistentObject);
        	} catch (Exception e) {
            	e.printStackTrace();
            }
        }
        else {
            genericDao.delete(persistentObject);
        }
	}
	
	@Transactional
	public <T> void save(T entityItem) throws ConstraintViolations, SQLException {
		setStatusPending(entityItem);	
		classValidator.validateEntity(entityItem);
		genericDao.update( genericDao.merge(entityItem) );
	}

	public <T> T read(Class<T> entityType, String id) {
		return genericDao.read(entityType, id);
	}
		
	private <T> void setStatusVoided(T entityItem) {
		if (entityItem instanceof AuditableCollectedEntity) {
			((AuditableCollectedEntity)entityItem).setStatus(siteProperties.getDataStatusVoidCode());
			((AuditableCollectedEntity)entityItem).setStatusMessage("");
		}	
	}
	
	private <T> void setStatusPending(T entityItem) {
		if (entityItem instanceof AuditableCollectedEntity) {
			((AuditableCollectedEntity)entityItem).setStatus(siteProperties.getDataStatusPendingCode());
			((AuditableCollectedEntity)entityItem).setStatusMessage("");
		}	
	}
}