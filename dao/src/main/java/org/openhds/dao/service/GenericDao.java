package org.openhds.dao.service;

import java.util.List;
import java.util.Map;
import org.hibernate.Session;

/** Interface for GenericDaoImpl.
 */
public interface GenericDao {

    interface OrderProperty {

        String getPropertyName();

        boolean isAscending();
    }

    interface ValueProperty {

        String getPropertyName();

        Object getValue();
    }

    interface RangeProperty {

        String getPropertyName();

        Object getMinRange();

        Object getMaxRange();
    }

    <T> String create(T newInstance);

    <T> T read(Class<T> entityType, String id);

    <T> void refresh(T entityItem);

    <T> void update(T transientObject);

    <T> void delete(T persistentObject);

    <T> T merge(T entityItem);

    <T> List<T> findAll(Class<T> entityType, boolean filterDeleted);
    
	<T> List<T> findAllDistinct(Class<T> entityType);
    
    <T> List<T> findAllWithOrder(Class<T> entityType, OrderProperty... orderProps);

    /**
     * Find an entry of some type from the database. Does not exclude "deleted" entries.
     *
     * @param entityType The class of the entry we're looking for (e.g. SocialGroup.class)
     * @param propertyName The column name to filter by (e.g. "extId")
     * @param value The value to match in this column (e.g. an extId)
     * @return an object of the class type passed
     */
    <T> T findByProperty(Class<T> entityType, String propertyName, Object value);

    /**
     * Find an entry of some type from the database.
     *
     * @param entityType The class of the entry we're looking for (e.g. SocialGroup.class)
     * @param propertyName The column name to filter by (e.g. "extId")
     * @param value The value to match in this column (e.g. an extId)
     * @param filterDeleted True to only return entries which haven't been marked deleted
     * @return an object of the class type passed
     */
    <T> T findByProperty(Class<T> entityType, String propertyName, Object value, boolean filterDeleted);
    
    <T> T findByMultiProperty(Class<T> entityType, ValueProperty... properties);

    <T> List<T> findListByProperty(Class<T> entityType, String propertyName, Object value);

    <T> List<T> findListByProperty(Class<T> entityType, String propertyName, Object value, boolean filterDeleted);
    
    <T> List<T> findListByPropertyWithOrder(Class<T> entityType, String propertyName, Object value, OrderProperty... orderProps);

    <T> T findUniqueByPropertyWithOrder(Class<T> entityType, String propertyName, Object value,
            String orderByCol, boolean ascending);

    void clear();

    <T> List<T> findListByMultiProperty(Class<T> entityType, ValueProperty... properties);
    
    <T> List<T> findListByMultiPropertyAndRange(Class<T> entityType, RangeProperty range, ValueProperty... properties);

    <T> long getTotalCount(Class<T> entityType);

    <T> List<T> findByExample(Class<T> entityType, T exampleInstance, String... excludeProperty);

    <T> Map<T,T> getClassMetaData();
    
    Session getSession();

    <T> List<T> findListByPropertyPrefix(Class<T> entityType, String property, String value, int limit, boolean filteredDeleted);

	<T> List<T> findAllWithoutProperty(Class<T> entityType, String property, String value);

    <T> List<T> findPaged(Class<?> entityType, String orderProperty, int start, int size);
    
    <T> List<T> findPagedFiltered(Class<?> entityType, String orderProperty, String filterProperty, Object filterValue, int start, int size);

    <T> long getTotalCountWithFilter(Class<T> entityType, String filterProperty, Object filterValue);

    <T> List<T> findPagedFilteredgt(Class<?> entityType, String orderProperty, String filterProperty, Object filterValue, int start, int size);
}
