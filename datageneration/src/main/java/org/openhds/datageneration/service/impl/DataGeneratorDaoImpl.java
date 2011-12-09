package org.openhds.datageneration.service.impl;

import org.hibernate.criterion.Order;
import org.openhds.dao.service.impl.GenericDaoImpl;
import org.openhds.datageneration.service.DataGeneratorDao;
import org.openhds.domain.model.Individual;

/**
 * Convenience DAO class for Data Generation utitlity
 *
 */
public class DataGeneratorDaoImpl extends GenericDaoImpl implements DataGeneratorDao {

	/**
	 * Locate a specific row in the database by an index
	 * @param entityType
	 * @param rowIndex the absolute index of a row to fetch
	 * @param orderByColumnName the name of the column to order results by
	 * @param ascendingOrder true for ascending order, false for descending order
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T findByRowNumber(Class<T> entityType, int rowIndex, String orderByColumnName, boolean ascendingOrder) {
		Order order = ascendingOrder ? Order.asc(orderByColumnName) : Order.desc(orderByColumnName);
		return (T) getSession().createCriteria(entityType)
						   .setMaxResults(1)
						   .setFirstResult(rowIndex)
						   .addOrder(order)
						   .uniqueResult();
	}

	/**
	 * Purge (delete all rows) from a table in the database
	 * @param clazz the entity type to purge
	 */
	@Override
	public void purgeTable(Class<?> clazz) {
		String objectName = clazz.getSimpleName();
		if (objectName.toLowerCase().equals("individual")) {
			Individual unknownIndividual = findByProperty(Individual.class, "extId", "UNK");
			// since individual has circular references to itself, the only way to reliably purge
			// the individual table is to set all individuals to reference the unknown individual
			// as there mother and father. otherwise the database will throw referential integrity
			// constraint errors
			getSession().createQuery("update Individual i set i.father = :father, i.mother = :mother")
						.setEntity("father", unknownIndividual)
						.setEntity("mother", unknownIndividual)
						.executeUpdate();
			// delete all but the unknown individual
			getSession().createQuery("delete from " + objectName + " where extId != :unk")
						.setString("unk", "UNK")
						.executeUpdate();
		} else if(objectName.toLowerCase().equals("fieldworker")) {
			// similar to individual, don't delete the unknown field worker
			getSession().createQuery("delete from " + objectName + " where extId != :unk")
						.setString("unk", "UNK")
						.executeUpdate();
		} else { 
			getSession().createQuery("delete from " + objectName).executeUpdate();
		}
	}
}
