package org.openhds.dao.util;

import org.openhds.dao.service.GenericDao.ValueProperty;

public class ValuePropertyHelper {

    public static ValueProperty getValueProperty(final String propertyName, final Object propertyValue) {
        return new ValueProperty() {

            public String getPropertyName() {
                return propertyName;
            }

            public Object getValue() {
                return propertyValue;
            }
        };      
    }
}
