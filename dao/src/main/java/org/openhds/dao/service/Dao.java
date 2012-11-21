package org.openhds.dao.service;

import java.io.Serializable;
import java.util.List;

/**
 * From the article http://www.ibm.com/developerworks/java/library/j-genericdao.html
 * <BR><BR>
 * A typesafe interface.<BR>
 * All database access in the system is made through a DAO to achieve encapsulation.<BR>
 * Each DAO instance is responsible for one primary domain object or entity.<BR>
 * If a domain object has an independent lifecycle, it should have its own DAO.<BR>
 * The DAO is responsible for creations, reads (by primary key), updates, and 
 * deletions -- that is, CRUD -- on the domain object.<BR>
 * The DAO may allow queries based on criteria other than the primary key such as 
 * finder methods or finders.<BR>
 * The return value of a finder is normally a collection of the domain object for
 * which the DAO is responsible.<BR>
 * The DAO is not responsible for handling transactions, sessions, or connections. 
 * These are handled outside the DAO to achieve flexibility.
 */
public interface Dao<T, PK extends Serializable> {

    /** Persist the newInstance object into the database */
    PK create(T newInstance);

    /** Retrieve an object that was previously persisted to the
     * database using the indicated id as primary key */
    T read(PK id);

    /** Save changes made to a persistent object */
    void update(T transientObject);

    /** Remove an object from persistent storage in the database */
    void delete(T persistentObject);

    /** Retrieve the total number of unique entities in the database */
    long getTotalCount();

    /** Retrieve an entity with the specified propertyName with the
     * associated value */
    T findByProperty(String propertyName, Object value);

    /** Retrieve a list of all objects of the DAO's entityType */
    List<T> findAll(boolean filterDeleted);
    
    public <S> S merge(S newInstance);
   
    public void saveOrUpdate(T newInstance);
   
    /** Retrieve a list of all objects of the DAO's entityType and also
     * limit the number or results returned specified by the index of
     * firstResult and the amount of maxResults */
    List<T> findPaged(int maxResults, int firstResult);

    List<T> findListByProperty(String propertyName, Object value, boolean filterDeleted);
    
    List<T> findListByPropertyPaged(String propertyName, Object value, int firstResult, int maxResults);
    
    long getCountByProperty(String propertyName, Object value);

    List<T> findByExample(T exampleInstance, String... excludeProperty);

	List<T> findListByPropertyPagedInsensitive(String propertyName,
			Object value, int pageIndex, int pageIncrement);
}
