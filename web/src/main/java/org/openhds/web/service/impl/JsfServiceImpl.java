package org.openhds.web.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import org.openhds.web.service.JsfService;

public class JsfServiceImpl implements JsfService {	
    // this field was added because jsf 1.2 does not support the
    // the f:param element within commandButtons
    // Instead, need to use an alternative method which requires
    // a field on a bean
    // refer to: http://forums.sun.com/thread.jspa?threadID=5366506
    String itemId;

	public SelectItem[] getSelectItems(List<?> entities) {
		int size = entities.size();
		SelectItem[] items = new SelectItem[size];
		int i = 0;
		for (Object x : entities) {
			items[i++] = new SelectItem(x, x.toString());
		}
		return items;
	}

	// converts an array of items to a list of selectItems
	@SuppressWarnings("unchecked")
	public List<SelectItem> arrayToSelectItem(Object[] objectArray){
		List newList = Arrays.asList(objectArray);
		SelectItem[] newSL = getSelectItems(newList);
		List sList = Arrays.asList(newSL);
		return (List<SelectItem>)sList;
	}

	public List<SelectItem> getSomeList(String... x) {
		return arrayToSelectItem(x);
	}
	
	public <T> List<T> arrayToList(T[] arr) {
		if (arr == null) {
			return new ArrayList<T>();
		}
		return Arrays.asList(arr);
	}

	public <T> Set<T> arrayToSet(T[] arr) {
		if (arr == null) {
			return new HashSet<T>();
		}
		return new HashSet<T>(Arrays.asList(arr));
	}

	public Object[] collectionToArray(Collection<?> c) {
		if (c == null) {
			return new Object[0];
		}
		return c.toArray();
	}

	public <T> List<T> setToList(Set<T> set) {
		return new ArrayList<T>(set);
	}

	public String getReqParam(String name) {
		String value = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(name);
	
		if (value == null) {
			return itemId;
		}
		
		return value;
	}

	public Object getObjViaReqParam(String reqParam, Converter converter,
			UIComponent component) {
		String theId = getReqParam(reqParam);
		return converter.getAsObject(FacesContext.getCurrentInstance(),
				component, theId);
	}

	public void addError(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				msg, msg);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	public void addMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void addErrorForComponent(String errmsg, String componentId) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errmsg, errmsg);
		FacesContext.getCurrentInstance().addMessage(componentId, msg);		
	}
}
