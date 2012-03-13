package org.openhds.report.service;

import java.util.Calendar;
import java.util.List;

import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.Death;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.OutMigration;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.domain.model.Residency;
import org.openhds.report.beans.ReportRecordBean;


public interface CalculationService {
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void initializeGroups();
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void setAgeGroups(long age, Individual individual, boolean denominator); 
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void setNumeratorTotals();
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void setDenominatorTotals();
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void completeReportRecords(Calendar startDate, Calendar endDate);
		
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<ReportRecordBean> getReportRecords();
		
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<Residency> getResidenciesInBetween(Calendar startDate, Calendar endDate);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<Residency> getResidenciesAtMidPoint(Calendar midpoint);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<InMigration> getInMigrationsBetweenInterval(Calendar startDate, Calendar endDate);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<OutMigration> getOutMigrationsBetweenInterval(Calendar startDate, Calendar endDate);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	List<Death> getDeathsBetweenInterval(Calendar startDate, Calendar endDate);
	
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	void setIntervalsOfResidencies(List<Residency> list, Calendar startDate, Calendar endDate);
}
