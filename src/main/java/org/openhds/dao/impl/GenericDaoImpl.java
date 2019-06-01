package org.openhds.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.openhds.dao.GenericDao;
import org.openhds.domain.AuditableEntity;
import org.openhds.domain.Individual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * A generic implementation of a Dao that simplifies Dao/BaseDaoImpl
 * This class differs from the Dao/BaseDaoImpl in that it is not have
 * generic parameters. Instead it uses generic methods so that only one instance
 * of this class needs to be created and can be shared by any entity
 *
 * @author dave
 *
 */
@Transactional
@Repository("genericDao")
public class GenericDaoImpl implements GenericDao {
	
	@Autowired
	protected SessionFactory sessFact;
	
	public Session getSession() {
		return sessFact.getCurrentSession();
	}

	public <T> String create(T newInstance) {
		return (String) getSession().save(newInstance);
	}

	public <T> void delete(T persistentObject) {
		getSession().delete(persistentObject);
	}

	@SuppressWarnings("unchecked")
	public <T> T read(Class<T> entityType, String id) {
		return (T) getSession().get(entityType, id);
	}

	public <T> void update(T transientObject) {
		getSession().update(transientObject);
	}
	
	public <T> void refresh(T entityItem) {
		getSession().refresh(entityItem);
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T merge(T entityItem) {
		return (T) getSession().merge(entityItem);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> entityType, boolean filterDeleted) {
		Criteria criteria = getSession().createCriteria(entityType);
		
        if (filterDeleted) {
        	criteria = criteria.add(Restrictions.eq("deleted", false));
        }
		 return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findMax(Class<T> entityType, boolean filterDeleted, String orderByCol, boolean ascending) {
		Criteria criteria = getSession().createCriteria(entityType);
		Order order = (ascending ? Order.asc(orderByCol) : Order.desc(orderByCol));
		
		criteria.addOrder(order).setMaxResults(1).uniqueResult();
        if (filterDeleted) {
        	criteria = criteria.add(Restrictions.eq("deleted", false));
        }
		 return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findAllDistinct(Class<T> entityType) {
		return getSession().createCriteria(entityType).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	public <T> T findByProperty(Class<T> entityType, String propertyName, Object value) {
		return findByProperty(entityType, propertyName, value, false);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public <T> T findByProperty(Class<T> entityType, String propertyName, Object value, boolean filterDeleted) {
        Criteria criteria = getSession().createCriteria(entityType).add(Restrictions.eq(propertyName, value));
        
        if (filterDeleted) {
        	criteria = criteria.add(Restrictions.eq("deleted", false));
        }
        
        return (T) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public <T> List<T> findListByProperty(Class<T> entityType, String propertyName, Object value) {
        return (List<T>) getSession().createCriteria(entityType).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).
        	add(Restrictions.eq(propertyName, value)).list();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findListByProperty(Class<T> entityType, String propertyName, Object value, boolean filterDeleted) {
        
		Criteria criteria = getSession().createCriteria(entityType).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).
        	add(Restrictions.eq(propertyName, value));
		
		 if (filterDeleted) {
	        	criteria = criteria.add(Restrictions.eq("deleted", false));
	        }
		 return criteria.list();
	}

	public SessionFactory getSessionFactory() {
		return sessFact;
	}

	public void setSessionFactory(SessionFactory sessFact) {
		this.sessFact = sessFact;
	}

	@SuppressWarnings("unchecked")
	public <T> T findUniqueByPropertyWithOrder(Class<T> entityType, String propertyName, Object value, 
			String orderByCol, boolean ascending) {
		Order order = (ascending ? Order.asc(orderByCol) : Order.desc(orderByCol));
		
		return (T) getSession().createCriteria(entityType).
			add(Restrictions.eq(propertyName, value)).
			add(Restrictions.eq("deleted", false)).
			addOrder(order).setMaxResults(1).uniqueResult();
	}

	public void clear() {
		getSession().clear();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findListByPropertyWithOrder(Class<T> entityType, String propertyName, Object value, OrderProperty... orderProps) {
		Criteria criteria = getSession().createCriteria(entityType).add(Restrictions.eq(propertyName, value));
		criteria = addOrderPropertiesToCriteria(criteria, orderProps);
		
		return (List<T>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAllWithOrder(Class<T> entityType, OrderProperty... orderProps) {
		Criteria criteria = getSession().createCriteria(entityType);
		return (List<T>) addOrderPropertiesToCriteria(criteria, orderProps).list();
	}
	
	private Criteria addOrderPropertiesToCriteria(Criteria criteria, OrderProperty... orderProps) {
		for(OrderProperty prop : orderProps) {
			Order order = (prop.isAscending() ? Order.asc(prop.getPropertyName()) : Order.desc(prop.getPropertyName()));
			criteria = criteria.addOrder(order);
		}
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findListByMultiProperty(Class<T> entityType, ValueProperty... properties) {
		Criteria criteria = getSession().createCriteria(entityType);
		
		criteria = addPropertiesToCriteria(criteria, properties);
		
		return (List<T>) criteria.list();
	}

	private Criteria addPropertiesToCriteria(Criteria criteria, ValueProperty... properties) {
		for(ValueProperty prop : properties) {
			criteria = criteria.add(Restrictions.eq(prop.getPropertyName(), prop.getValue()));
		}
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T findByMultiProperty(Class<T> entityType, ValueProperty... properties) {
		Criteria criteria = getSession().createCriteria(entityType);
		criteria = addPropertiesToCriteria(criteria, properties);
		
		return (T) criteria.uniqueResult();
	}
	
    public <T> long getTotalCount(Class<T> entityType) {
        return (Long) getSession().createCriteria(entityType).setProjection(
                Projections.rowCount()).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findByExample(Class<T> entityType, T exampleInstance, String... excludeProperty) {
        Criteria crit = getSession().createCriteria(entityType);
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }
    
    public <T> Map<String,ClassMetadata> getClassMetaData() {
    	return getSession().getSessionFactory().getAllClassMetadata();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findListByPropertyPrefix(Class<T> entityType, String property, String value, int limit,
            boolean filterDeleted) {
        Criteria crit = getSession().createCriteria(entityType);
        if (filterDeleted) {
            crit = crit.add(Restrictions.eq("deleted", false));
        }
        
        crit.addOrder(Order.asc(property));
        
        if (limit > 0) {
            crit = crit.setMaxResults(limit);
        }

        return (List<T>) crit.add(Restrictions.like(property, value, MatchMode.START)).list();
    }

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAllWithoutProperty(Class<T> entityType, String property, String value) {
	    Criteria crit = getSession().createCriteria(entityType);
		crit.add(Restrictions.ne(property, value));
		
        if (AuditableEntity.class.isAssignableFrom(entityType)) {
            crit.add(Restrictions.eq("deleted", false));
        }
		
		return (List<T>) crit.list();
	}

    @SuppressWarnings("unchecked")
	@Override
    public <T> List<T> findPaged(Class<?> entityType, String orderProperty, int start, int size) {
        Criteria crit = getSession().createCriteria(entityType);
        
        if (Individual.class.isAssignableFrom(entityType)) {
            crit.add(Restrictions.ne("extId", "UNK"));
        }
        
        crit.add(Restrictions.eq("deleted", false));
        crit.addOrder(Order.asc(orderProperty));
        crit.setFirstResult(start).setMaxResults(size);
        return (List<T>) crit.list();
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> List<T> findPagedFiltered(Class<?> entityType, String orderProperty, String filterProperty,
            Object filterValue, int start, int size) {
        Criteria crit = getSession().createCriteria(entityType);

        crit.add(Restrictions.eq(filterProperty, filterValue));
        crit.add(Restrictions.eq("deleted", false));
        crit.addOrder(Order.asc(orderProperty));
        crit.setFirstResult(start).setMaxResults(size);
        return (List<T>) crit.list();
    }
    
    
    @SuppressWarnings("unchecked")
	@Override
    public <T> List<T> findPagedFilteredgt(Class<?> entityType, String orderProperty, String filterProperty,
            Object filterValue, int start, int size) {
        Criteria crit = getSession().createCriteria(entityType);

        crit.add(Restrictions.ge(filterProperty, filterValue));
        crit.add(Restrictions.eq("deleted", false));
        crit.addOrder(Order.asc(orderProperty));
        crit.setFirstResult(start).setMaxResults(size);
        return (List<T>) crit.list();
    }

    @Override
    public <T> long getTotalCountWithFilter(Class<T> entityType, String filterProperty, Object filterValue) {
        long total =(Long) getSession().createCriteria(entityType).add(Restrictions.ge(filterProperty, filterValue))
                .setProjection(Projections.rowCount()).uniqueResult();
        return total;
    }
}
