package org.openhds.util;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.UUIDHexGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

public class UuidGenerator extends UUIDHexGenerator{
	private String entityName;

    public void configure(Type type, Properties params, ServiceRegistry d)
            throws MappingException {
        entityName = params.getProperty(ENTITY_NAME);
        super.configure(type, params, d);
    }

    public Serializable generate(SessionImplementor session, Object object)
            throws HibernateException {            
        Serializable id = session
            .getEntityPersister(entityName, object)
            .getIdentifier(object, session);       

        if (id == null)
            return super.generate(session, object);
        else
            return id;
    }
}
