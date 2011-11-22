package org.openhds.web.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

public interface JsfService {

	SelectItem[] getSelectItems(List<?> entities);

	<T> List<T> arrayToList(T[] arr);

	<T> Set<T> arrayToSet(T[] arr);

	Object[] collectionToArray(Collection<?> c);

	<T> List<T> setToList(Set<T> set);

	List<SelectItem> arrayToSelectItem(Object[] objectArray);
	
	String getReqParam(String name);

	Object getObjViaReqParam(String reqParam, Converter converter, UIComponent component);

	void addError(String msg);

	void addMessage(String msg);
	
	void addErrorForComponent(String msg, String componentId);
}
