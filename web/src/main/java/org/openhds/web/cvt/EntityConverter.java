package org.openhds.web.cvt;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public interface EntityConverter<T> extends Converter {

	T getAsObject(FacesContext context, UIComponent component, String value);

	String getAsString(FacesContext context, UIComponent component, T value);

}