package org.openhds.datageneration.utils;

import org.openhds.datageneration.service.DataGeneratorDao;

/**
 * Implementation of a Database Cursor that is used in the data generator service to get around
 * some of the Hibernate session issues.
 * 
 * It allows for lazily iterating over a collection of rows for a specified table
 */
public class Cursor<T> {

	private final Class<?> clazzTable;
	private final long recordCount;
	private final DataGeneratorDao genericDao;
	private int currentRecord;
	
	public Cursor(DataGeneratorDao dataGeneratorDao, Class<?> clazz, long recordCount) {
		this.genericDao = dataGeneratorDao;
		this.clazzTable = clazz;
		this.recordCount = recordCount;
	}
	
	public boolean hasNext() {
		if(currentRecord + 1 > recordCount) {
			return false;
		} else {
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	public T next() {
		T instance = (T) genericDao.findByRowNumber(clazzTable, currentRecord, "extId", true);
		currentRecord++;
		return instance;
	}
	
	public void reset() {
		currentRecord = 0;
	}
}
