package org.openhds.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.openhds.dao.GenericDao;
import org.openhds.domain.LocationHierarchy;

public class LocationHierarchyConverter implements Converter {
	
	GenericDao genericDao;
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
	
		LocationHierarchy loc = new LocationHierarchy();	
		LocationHierarchy item;

		if (value == null || value.trim().isEmpty()) 		
			return loc;

		else {
			
			item = genericDao.findByProperty(LocationHierarchy.class, "name", value);
								
			if (item == null) {		
				FacesMessage msg = new FacesMessage("Invalid Parent Location specified.");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ConverterException(msg);
			}
		}
		return item;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		LocationHierarchy item = (LocationHierarchy)value;
		return item.getName();
	}
	
	public GenericDao getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
}
