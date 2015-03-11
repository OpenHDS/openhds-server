package org.openhds.integration.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.openhds.web.service.JsfService;

/**
 * A special JsfService Mock implementation to be used only for testing.
 * It strips the dependencies on the FacesContext.
 */
public class JsfServiceMock implements JsfService {
	
	List<String> errors = new ArrayList<String>();;

	@Override
	public void addError(String msg) {
		errors.add(msg);
	}
	
	@Override
	public void addErrorForComponent(String msg, String componentId) {
		errors.add(msg);
	}

	@Override
	public void addMessage(String msg) {
		errors.add(msg);
	}

	@Override
	public <T> List<T> arrayToList(T[] arr) {
		return null;
	}

	@Override
	public List<SelectItem> arrayToSelectItem(Object[] objectArray) {
		return null;
	}

	@Override
	public <T> Set<T> arrayToSet(T[] arr) {
		return null;
	}

	@Override
	public Object[] collectionToArray(Collection<?> c) {
		return null;
	}

	@Override
	public Object getObjViaReqParam(String reqParam, Converter converter,
			UIComponent component) {
		return null;
	}

	@Override
	public String getReqParam(String name) {
		return null;
	}

	@Override
	public SelectItem[] getSelectItems(List<?> entities) {
		return null;
	}

	@Override
	public <T> List<T> setToList(Set<T> set) {
		return null;
	}
	
	public List<String> getErrors() {
		return errors;
	}
	
	public void resetErrors(){
		errors = new ArrayList<String>();
	}
}
