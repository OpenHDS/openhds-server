package org.openhds.service.impl;

import java.util.List;

import org.openhds.service.LocationHierarchyLevelService;
import org.openhds.dao.GenericDao;
import org.openhds.domain.LocationHierarchyLevel;

public class LocationHierarchyLevelServiceImpl implements LocationHierarchyLevelService {

	private GenericDao genericDao;

	public LocationHierarchyLevelServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}


	@Override
	public List<LocationHierarchyLevel> getAllLevels() {
		return genericDao.findAll(LocationHierarchyLevel.class, false);
	}



















}