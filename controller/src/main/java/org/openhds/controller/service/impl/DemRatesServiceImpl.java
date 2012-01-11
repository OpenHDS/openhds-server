package org.openhds.controller.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openhds.controller.service.DemRatesService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.DemRates;
import org.openhds.domain.model.Residency;
import org.openhds.domain.util.CalendarUtil;

public class DemRatesServiceImpl implements DemRatesService {

	private GenericDao genericDao;
	
	public DemRatesServiceImpl(GenericDao genericDao, CalendarUtil calendarUtil) {
		this.genericDao = genericDao;
	}
	
	public DemRates findDemRateByUuid(String itemId) {
		return genericDao.findByProperty(DemRates.class, "uuid", itemId);
	}
	
	public List<Residency> getResidenciesAtMidPoint(Calendar midpoint) {
		
		List<Residency> residencies = genericDao.findAll(Residency.class, true);
		List<Residency> midPointResidencies = new ArrayList<Residency>();
			
		Calendar resEndDate;
		for (Residency res : residencies) {
			// handle residencies that haven't ended, if null then use the date of the last visit
			if (res.getEndDate() == null) 
				resEndDate = Calendar.getInstance();
			else 
				resEndDate = (Calendar) res.getEndDate().clone();

			// for residency to be added to analysis set, its end date has to be on or after the midpoint
			// NEVER endDate before midpoint...!endDate.before(midpoint)
			if (!(resEndDate.before(midpoint)) && (!(res.getStartDate().after(midpoint)))) {
				// add residencies that qualify into the list
				midPointResidencies.add(res);
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
	
	private long daysBetween(Calendar startDate, Calendar endDate) {  
		Calendar date = (Calendar) startDate.clone();  
		long daysBetween = 0;  
		while (date.before(endDate)) {  
			date.add(Calendar.DAY_OF_MONTH, 1);  
		    daysBetween++;  
		}  
		return daysBetween;  
	}  		
}
