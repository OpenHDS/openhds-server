package org.openhds.converter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class TimeStampConverter implements Converter {

    private Format formatter;

    public TimeStampConverter(String dateFormat) {
        formatter = new SimpleDateFormat(dateFormat + " HH:mm:ss");
    }
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return formatter.format(((Calendar)value).getTime());
    }

}
