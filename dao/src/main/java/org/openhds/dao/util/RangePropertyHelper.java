package org.openhds.dao.util;

import org.openhds.dao.service.GenericDao.RangeProperty;

public class RangePropertyHelper {

    public static RangeProperty getRangeProperty(final String propertyName, final Object minRangeValue, final Object maxRangeValue) {
        return new RangeProperty() {

            public String getPropertyName() {
                return propertyName;
            }

            @Override
            public Object getMinRange() {
                return minRangeValue;
            }

            @Override
            public Object getMaxRange() {
                return maxRangeValue;
            }
        };      
    }
}
