package org.openhds.controller.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.openhds.controller.service.DemRatesService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.DemRates;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.Visit;

public class DemRatesServiceImpl implements DemRatesService {

	private GenericDao genericDao;
	
	public DemRatesServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	public DemRates findDemRateByUuid(String itemId) {
		return genericDao.findByProperty(DemRates.class, "uuid", itemId);
	}
	
	public List<Residency> getResidenciesAtMidPoint(Calendar midpoint) {
		
		List<Residency> residencies = genericDao.findAll(Residency.class, true);
		List<Residency> midPointResidencies = new ArrayList<Residency>();
			
		Calendar resEndDate = null;
		for (Residency res : residencies) {
			// handle residencies that haven't ended, if null then use the date of the last visit
			if (res.getEndDate() == null) {
	
				// get all visits for this residency
				List<Visit> visits = genericDao.findListByProperty(Visit.class, "visitLocation", res.getLocation());
				
				if (visits.size() > 0) {
					// grab latest visit
					Calendar latestDate = new GregorianCalendar(1900, Calendar.JANUARY, 1);
					for (Visit visit : visits) {
						Calendar visitDate = visit.getVisitDate();
						if (visitDate.after(latestDate))
							latestDate = visitDate;
					}		
					resEndDate = latestDate;	
				}
			}
			else 
				resEndDate = res.getEndDate();

			if (resEndDate != null) {
				// for residency to be added to analysis set, 
				// its end date has to be on or after the midpoint
				// and its start date has to be on or before the midpoint
				if ((resEndDate.after(midpoint) || resEndDate.equals(midpoint)) && 
					((res.getStartDate().before(midpoint) || res.getStartDate().equals(midpoint)))) {
					midPointResidencies.add(res);
				}
			}
			resEndDate = null;
		}
		return midPointResidencies;
	}
	
	public Calendar getMidPointDate(Calendar startDate, Calendar endDate) {
		int daysBtw = (int)daysBetween(startDate, endDate);
		Calendar midPoint = (Calendar)startDate.clone();
		midPoint.add(Calendar.DATE, (int) (daysBtw * 0.5));
		return midPoint;
	}
	
	public long daysBetween(Calendar startDate, Calendar endDate) {  
		Calendar date = (Calendar) startDate.clone();  
		long daysBetween = 0;  
		while (date.before(endDate)) {  
			date.add(Calendar.DAY_OF_MONTH, 1);  
		    daysBetween++;  
		}  
		return daysBetween;  
	}  		
}
