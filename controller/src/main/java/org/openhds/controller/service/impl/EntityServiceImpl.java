package org.openhds.controller.service.impl;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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
import org.openhds.events.domain.EventMetaData;
import org.openhds.events.domain.OpenHDSEvent;
import org.openhds.events.service.EventGateway;
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
	private static final String CREATE_TYPE = "create";
    private static final String UPDATE_TYPE = "update";
    private static final String VOID_TYPE = "void";
    private static final String USER_DELETED_TYPE = "user_delete";
    private static final String DELETE_TYPE = "delete";    private static final String UNABLE_TO_MARSHAL_ERROR = "Error - Unable to convert entity to an XML payload";
    private GenericDao genericDao;
	private CurrentUser currentUser;
	private CalendarUtil calendarUtil;
	private SitePropertiesService siteProperties;
	private EntityValidationService classValidator;
	private EventGateway eventGateway;
	
	public EntityServiceImpl(GenericDao genericDao, CurrentUser currentUser, CalendarUtil calendarUtil, SitePropertiesService siteProperties, EntityValidationService classValidator, EventGateway eventGateway) {
		this.genericDao = genericDao;
		this.currentUser = currentUser;
		this.calendarUtil = calendarUtil;
		this.siteProperties = siteProperties;
		this.classValidator = classValidator;
		this.eventGateway = eventGateway;
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

		publishEvent(entityItem, CREATE_TYPE);
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
            publishEvent(persistentObject, VOID_TYPE);
        }
        else if (persistentObject instanceof User) {
        	try {
	        	setDeletedMethod = persistentObject.getClass().getMethod("setDeleted", boolean.class);
	            setDeletedMethod.invoke(persistentObject, true);
	            genericDao.update(persistentObject);
	            publishEvent(persistentObject, USER_DELETED_TYPE);
        	} catch (Exception e) {
            	e.printStackTrace();
            }
        }
        else {
            genericDao.delete(persistentObject);
            publishEvent(persistentObject, DELETE_TYPE);
        }
	}
	
	@Transactional
	public <T> void save(T entityItem) throws ConstraintViolations, SQLException {
		setStatusPending(entityItem);	
		classValidator.validateEntity(entityItem);
		genericDao.update( genericDao.merge(entityItem) );
		publishEvent(entityItem, UPDATE_TYPE);
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

	private <T> void publishEvent(T entityItem, String actionType) {
        String result = null;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext carContext = JAXBContext.newInstance(entityItem.getClass());
            Marshaller carMarshaller = carContext.createMarshaller();
            carMarshaller.marshal(entityItem, sw);
            result = sw.toString();
        } catch (JAXBException e) {
            result = UNABLE_TO_MARSHAL_ERROR;
        }

	    OpenHDSEvent event = new OpenHDSEvent();
	    event.setActionType(actionType);
	    event.setEntityType(entityItem.getClass().getSimpleName());
	    event.setEventData(result);
	    event.setInsertDate(calendarUtil.convertDateToCalendar(new Date()));
	    event.setEventMetaData(generateMetaData());

	    eventGateway.publishEvent(event);
	}

    private List<EventMetaData> generateMetaData() {
        List<EventMetaData> metaDataList = new ArrayList<EventMetaData>();

        EventMetaData metaData = new EventMetaData();
        metaData.setInsertDate(calendarUtil.convertDateToCalendar(new Date()));
        metaData.setNumTimesRead(0);
        metaData.setStatus("Unread");
        metaData.setSystem("Default");

        metaDataList.add(metaData);

        return metaDataList;
    }
}