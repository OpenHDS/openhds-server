package org.openhds.report.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.domain.service.SitePropertiesService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Ime Asangansi
 * @Description this class keeps different HQL, SQL and special Hibernate Queries 
 * that may need to be maintained together.
 */
public class SpecialQueries {

    @Autowired
    SessionFactory sessionFactory;
    
    @Autowired
    GenericDao genericDao;
    
    @Autowired
    SitePropertiesService siteProperties;
    
    @Autowired
    LocationHierarchyService locationService;
    
    Session sess;
    
	@SuppressWarnings("unchecked")
	public List<Individual> getMaleBirths(LocationHierarchy lh, int year){
		
	   	this.sess = sessionFactory.openSession();	
		Query query = sess.createQuery("select month(i.dob) as birthmonth from Individual i where year(i.dob) = :year and i.gender = :male");

		query.setParameter("year", year);
		query.setParameter("male", siteProperties.getMaleCode());
		
		List<Individual> result = query.list();
        if(result == null || !(result.size() > 0)) {
        	return null;
        }
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Individual> getInMigrationsFromIndividuals(Set<Individual> passedIn) {

	   	this.sess = sessionFactory.openSession();	
		Query query = sess.createQuery("select inm.individual from InMigration inm where inm.individual in (:individual) and inm.migType = 'EXTERNAL_INMIGRATION'");
		query.setParameterList("individual", new ArrayList<Individual>(passedIn));
		List<Individual> result = query.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Individual> getInternalMigrationsFromIndividuals(Set<Individual> passedIn) {

	   	this.sess = sessionFactory.openSession();	
		Query query = sess.createQuery("select inm.individual from InMigration inm where inm.individual in (:individual) and inm.migType = MigrationType.INTERNAL_INMIGRATION");
		query.setParameterList("individual", new ArrayList<Individual>(passedIn));
		List<Individual> result = query.list();
		return result;	
	}
	@SuppressWarnings("unchecked")
	public List<Individual> getOutMigrationsFromIndividuals(Set<Individual> passedIn) {

	   	this.sess = sessionFactory.openSession();	
		Query query = sess.createQuery("select out.individual from OutMigration out where out.individual in (:individual)");
		query.setParameterList("individual", new ArrayList<Individual>(passedIn));	
		List<Individual> result = query.list();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Individual> getDeathsFromIndividuals(Set<Individual> indivs){
		
	   	this.sess = sessionFactory.openSession();	
		Query query = sess.createQuery("select d.individual from Death d where d.individual in (:individual)");
		query.setParameterList("individual", new ArrayList<Individual>(indivs));
		List<Individual> result = query.list();
		return result;
	}
	
	// queries for marriageCount, divorceCount, widowingsCount, sepCount, reconCount
	public Long getMaritalCount(Set<Individual> indivs, String type, Calendar firstDate, Calendar endDate){
		
	   	this.sess = sessionFactory.openSession();	
		Query query = sess.createQuery("select count(i.uuid) from Individual i, Relationship r where i = r.individualA or i = r.individualB " +
				"and r.aIsToB = (:type) and r.deleted = false and r.startDate >= :firstDate and r.startDate <= :endDate and i in (:individuals)");
		
		query.setParameterList("individuals", new ArrayList<Individual>(indivs));
		query.setParameter("type", type);
		query.setParameter("firstDate", firstDate);
		query.setParameter("endDate", endDate);
		
		Long i = (Long)query.uniqueResult(); 
		return i;
	}
	
	@SuppressWarnings("unchecked")
	public List<Individual> getLiveBirthsFromIndividuals(Set<Individual> selectedPop, Calendar firstDate, Calendar lastDate) {
		this.sess = sessionFactory.openSession();	
		Query query = sess.createQuery("select i from Individual i, Outcome o where o.type = :livebirths and i.uuid = o.child.uuid and i in (:individuals)");
		query.setParameterList("individuals", new ArrayList<Individual>(selectedPop));
		query.setParameter("livebirths", siteProperties.getLiveBirthCode());
		List<Individual> result = query.list();
		
        if(result == null || !(result.size() > 0)) {
        	return null;
        }
		return result;	
	}
}
