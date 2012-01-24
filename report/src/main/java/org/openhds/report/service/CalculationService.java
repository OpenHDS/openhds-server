package org.openhds.report.service;

import java.util.Calendar;
import java.util.List;

import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.PrivilegeConstants;
import org.openhds.report.beans.ReportRecordBean;


public interface CalculationService {
	
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
}
