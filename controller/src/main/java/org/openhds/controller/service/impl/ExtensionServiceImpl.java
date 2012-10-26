package org.openhds.controller.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.ExtensionService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.ClassExtension;
import org.openhds.domain.model.Extension;
import org.openhds.domain.model.PrimitiveType;
import org.openhds.domain.model.Round;
import org.openhds.domain.model.Visit;
import org.openhds.domain.service.SitePropertiesService;

public class ExtensionServiceImpl implements ExtensionService {
	
	GenericDao genericDao;
	SitePropertiesService properties;
		
	public ExtensionServiceImpl(GenericDao genericDao, SitePropertiesService properties) {
		this.genericDao = genericDao;
		this.properties = properties;
	}
	
	/**
	 * Provides basic validation according to the specified types. The actual value
	 * being persisted is a String but the rules for validation can be applied to
	 * keep data consistent and reliable.
	 */
	public Visit evaluateExtensions(Visit entityItem) throws ConstraintViolations {	
		
		for (Extension ext : entityItem.getExtensions()) {
			
			if (ext.getClassExtension().getPrimType() == PrimitiveType.BOOLEAN) {
				if (!ext.getExtensionValue().toLowerCase().equals(properties.getYesResponse().toLowerCase()) &&
					!ext.getExtensionValue().toLowerCase().equals(properties.getNoResponse().toLowerCase())) {
					throw new ConstraintViolations("Only the values of " + properties.getYesResponse() + 
					" and " + properties.getNoResponse() + " are permitted on Extensions of type BOOLEAN");
				}
			}
			else if (ext.getClassExtension().getPrimType() == PrimitiveType.DATE) {
				DateFormat format = new SimpleDateFormat(properties.getDateFormat());
				format.setLenient(false);
				try {
					format.parse(ext.getExtensionValue());
				} catch (ParseException e) {
					throw new ConstraintViolations("The date format specified does not match " +
					"the configured date format");
				}
			}
			else if (ext.getClassExtension().getPrimType() == PrimitiveType.NUMBER) {
				try {
					Double.parseDouble(ext.getExtensionValue());
				} catch (NumberFormatException e) {
					throw new ConstraintViolations("The number specified is not a valid number");
				}	
			}	
		}
		
		return entityItem;
	}
		
	public ClassExtension evaluateClassExtension(ClassExtension entityItem) throws ConstraintViolations {
		
		List<ClassExtension> list = genericDao.findListByProperty(ClassExtension.class, "name", entityItem.getName());
		
		if (list.size() > 0)
			throw new ConstraintViolations("Duplicate Class Extension names are not allowed");
		if (!checkValidRoundNumber(entityItem.getRoundNumber())) 
    		throw new ConstraintViolations("The Round Number specified is not a valid Round Number.");	
		
		return entityItem;
	}
	
	/**
	 * To delete a ClassExtension item, there must be no dependent entities using it.
	 */
	public ClassExtension deleteClassExtension(ClassExtension entityItem) throws ConstraintViolations {
		
		List<Extension> extensions = genericDao.findListByProperty(Extension.class, "classExtension", entityItem);
		
		for (Extension item : extensions) {
			if (item.getClassExtension().getName().equals(entityItem.getName())) {
				throw new ConstraintViolations("Unable to delete this Extension item " +
						"because it is dependent on an entity.");
			}
		}
		
		return entityItem;
	}	
	
	/**
	 * Checks if the provided round number exists
	 */
	public boolean checkValidRoundNumber(Integer roundNumber) {
		
		Round round = genericDao.findByProperty(Round.class, "roundNumber", roundNumber);
		if (round != null)
			return true;
		return false;
	}
}
