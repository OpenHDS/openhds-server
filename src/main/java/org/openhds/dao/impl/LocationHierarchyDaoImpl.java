package org.openhds.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openhds.domain.LocationHierarchy;

/**
 * A specialized class for the LocationHierarchy entity
 * This was introduced because LocationHierarchy has to filter out the root
 * whenever it makes a query for searching and/or paging results
 *
 */
public class LocationHierarchyDaoImpl extends BaseDaoImpl<LocationHierarchy, String> {
	
	public LocationHierarchyDaoImpl(Class<LocationHierarchy> entityType) {
		super(entityType);
	}
	
	@Override
	protected Criteria addImplicitRestrictions(Criteria criteria) {
		return criteria.add(Restrictions.ne("extId", "HIERARCHY_ROOT"));
	}
}
