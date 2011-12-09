package org.openhds.datageneration.service;

import org.openhds.dao.service.GenericDao;

public interface DataGeneratorDao extends GenericDao {

	public <T> T findByRowNumber(Class<T> entityType, int rowIndex, String orderByColumnName, boolean ascendingOrder);
	
	public void purgeTable(Class<?> clazz);
}
