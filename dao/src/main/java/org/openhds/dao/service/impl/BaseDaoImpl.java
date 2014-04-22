package org.openhds.dao.service.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.openhds.dao.finder.ArgumentTypeFactory;
import org.openhds.dao.finder.FinderExecutor;
import org.openhds.dao.finder.NamingStrategy;
import org.openhds.dao.service.Dao;
import org.openhds.domain.model.AuditableEntity;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.User;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<T, PK extends Serializable> implements Dao<T, PK>, FinderExecutor<T> {

    /** Model object type */
    Class<T> entityType;
    /** Hibernate session factory configured in applicationContext */
    SessionFactory sessFact;
    NamingStrategy namingStrategy;
    ArgumentTypeFactory argumentTypeFactory;

    public BaseDaoImpl(Class<T> entityType) {
        if (entityType == null) {
            throw new IllegalArgumentException(
                    "entity type not specified for dao");
        }
        this.entityType = entityType;
    }

    /** Persist the <code>newInstance</code> object into the database */
    public PK save(T newInstance) {
    	getSession().flush();
    	return (PK) getSession().save(newInstance);
    }
    
    public PK create(T newInstance) {	
     	getSession().flush(); 
    	return (PK) getSession().save(newInstance);
    }
    
    public <S> S merge(S newInstance){
     	return (S)getSession().merge(newInstance);
    	
    }
    
    public void saveOrUpdate(T newInstance){
    	getSession().saveOrUpdate(newInstance);
    }    

    /** Retrieve an object that was previously persisted to the
     * database using the indicated <code>id</code> as primary key */
    public T read(PK id) {
        return (T) getSession().get(entityType, id);
    }

    /** Save changes made to a persistent object */
    public void update(T transientObject) {
        getSession().update(transientObject);
    }

    /** Remove an object from persistent storage in the database */
    public void delete(T persistentObject) {
        getSession().delete(persistentObject);
    }
    
    public void evict(T persistentObject) {
        getSession().evict(persistentObject);
    }

    /** Retrieve an entity with the specified <code>propertyName</code>
     * with the associated <code>value</code> */
    public T findByProperty(String propertyName, Object value) {
    	Criteria criteria = getSession().createCriteria(entityType).add(Restrictions.eq(propertyName, value));
    	if (AuditableEntity.class.isAssignableFrom(entityType)) {
    		criteria = addImplicitRestrictions(criteria);
    	}
    	return (T) criteria.uniqueResult();    
    }

    /** Retrieve a list of all objects of the DAO's <code>entityType</code> */
    public List<T> findAll(boolean filterDeleted) {
		Criteria criteria = getSession().createCriteria(entityType);
        if (AuditableEntity.class.isAssignableFrom(entityType) || 
            LocationHierarchy.class.isAssignableFrom(entityType) ||
            FieldWorker.class.isAssignableFrom(entityType)) {
            criteria = addImplicitRestrictions(criteria);    		
        }
        if (filterDeleted) {
        	criteria = criteria.add(Restrictions.eq("deleted", false));
        }
		return criteria.list();
    }

    /** Retrieve a list of all objects of the DAO's <code>entityType</code>
     * and also limit the number or results returned specified by the index of
     * <code>firstResult</code> and the amount of <code>maxResults</code> */
    public List<T> findPaged(int maxResults, int firstResult) {
    	Criteria criteria = getSession().createCriteria(entityType);	
    	if (AuditableEntity.class.isAssignableFrom(entityType) || 
        	LocationHierarchy.class.isAssignableFrom(entityType) ||
        	FieldWorker.class.isAssignableFrom(entityType) ||
        	User.class.isAssignableFrom(entityType)) {
    		criteria = addImplicitRestrictions(criteria);
        }	
    	return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).
 	   		   setFirstResult(firstResult).setMaxResults(maxResults).
 	   		   list();
    }

    /** Retrieve the total number of unique entities in the database */
    public long getTotalCount() {
    	Criteria criteria = getSession().createCriteria(entityType);
    	if (AuditableEntity.class.isAssignableFrom(entityType) || 
            LocationHierarchy.class.isAssignableFrom(entityType) ||
            FieldWorker.class.isAssignableFrom(entityType) ||
            User.class.isAssignableFrom(entityType)) {
    		criteria = addImplicitRestrictions(criteria);
        }
    	
		return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }

    /** Looks at the name of the invoked class and method and matches these to the name
     * of the Hibernate query */
    public List<T> executeFinder(Method method, final Object[] queryArgs) {
        final Query namedQuery = prepareQuery(method, queryArgs);
        return (List<T>) namedQuery.list();
    }

    public Iterator<T> iterateFinder(Method method, final Object[] queryArgs) {
        final Query namedQuery = prepareQuery(method, queryArgs);
        return (Iterator<T>) namedQuery.iterate();
    }

    private Query prepareQuery(Method method, Object[] queryArgs) {
        final String queryName = getNamingStrategy().queryNameFromMethod(
                entityType, method);
        final Query namedQuery = getSession().getNamedQuery(queryName);
        String[] namedParameters = namedQuery.getNamedParameters();
        if (namedParameters.length == 0) {
            setPositionalParams(queryArgs, namedQuery);
        } else {
            setNamedParams(namedParameters, queryArgs, namedQuery);
        }
        return namedQuery;
    }

    private void setPositionalParams(Object[] queryArgs, Query namedQuery) {
        if (queryArgs != null) {
            for (int i = 0; i < queryArgs.length; i++) {
                Object arg = queryArgs[i];
                Type argType = getArgumentTypeFactory().getArgumentType(arg);
                if (argType != null) {
                    namedQuery.setParameter(i, arg, argType);
                } else {
                    namedQuery.setParameter(i, arg);
                }
            }
        }
    }

    private void setNamedParams(String[] namedParameters, Object[] queryArgs,
            Query namedQuery) {
        if (queryArgs != null) {
            for (int i = 0; i < queryArgs.length; i++) {
                Object arg = queryArgs[i];
                Type argType = getArgumentTypeFactory().getArgumentType(arg);
                if (argType != null) {
                    namedQuery.setParameter(namedParameters[i], arg, argType);
                } else {
                    if (arg instanceof Collection) {
                        namedQuery.setParameterList(namedParameters[i],
                                (Collection<?>) arg);
                    } else {
                        namedQuery.setParameter(namedParameters[i], arg);
                    }
                }
            }
        }
    }

    public List<T> findListByProperty(String propertyName, Object value, boolean filterDeleted) {
		Criteria criteria = getSession().createCriteria(entityType).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).
    	add(Restrictions.eq(propertyName, value));
		if (filterDeleted) {
			criteria = criteria.add(Restrictions.eq("deleted", false));
	    }
		return criteria.list();
    }

    public List<T> findListByPropertyPaged(String propertyName, Object value, int firstResult, int maxResults) {  
    	Criteria criteria = getSession().createCriteria(entityType).add(Restrictions.eq(propertyName, value));
    	if (AuditableEntity.class.isAssignableFrom(entityType)) {
    		criteria = addImplicitRestrictions(criteria);
        }	
    	return (List<T>) criteria.setFirstResult(firstResult).setMaxResults(maxResults).
    					 setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    public List<T> findListByPropertyPagedInsensitive(String propertyName, Object value, int firstResult, int maxResults) {
        Criteria criteria = getSession().createCriteria(entityType).add(Restrictions.ilike(propertyName, (String)value, MatchMode.ANYWHERE));
    	if (AuditableEntity.class.isAssignableFrom(entityType)) {
    		criteria = addImplicitRestrictions(criteria);
        }
    	return (List<T>) criteria.setFirstResult(firstResult).setMaxResults(maxResults).
    					 setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }    
    
    public List<T> findByExample(T exampleInstance,String... excludeProperty) {
        Criteria crit = getSession().createCriteria(entityType);
        Example example =  Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

	public long getCountByProperty(String propertyName, Object value) {
		Criteria criteria = getSession().createCriteria(entityType).add(Restrictions.eq(propertyName, value));	
    	if (AuditableEntity.class.isAssignableFrom(entityType)) {
    		criteria = addImplicitRestrictions(criteria);
        }
		return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	protected Criteria addImplicitRestrictions(Criteria criteria) {
		return criteria.add(Restrictions.eq("deleted", false));
	}
	
	public void setSessionFactory(SessionFactory sessFact) {
		this.sessFact = sessFact;
	}

    protected Session getSession() {
    	return sessFact.getCurrentSession();
    }
    
    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(NamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public ArgumentTypeFactory getArgumentTypeFactory() {
        return argumentTypeFactory;
    }

    public void setArgumentTypeFactory(ArgumentTypeFactory argumentTypeFactory) {
        this.argumentTypeFactory = argumentTypeFactory;
    }
}
