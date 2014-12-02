package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.faces.context.FacesContext;

import org.openhds.controller.service.DemRatesService;
import org.openhds.domain.model.DemRates;

public class DemRatesCrudImpl extends EntityCrudImpl<DemRates, String> {
	
    // used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
    Date startDate;
    Date endDate;
    
    DemRatesService demRatesService;
	
	public DemRatesCrudImpl(Class<DemRates> entityClass) {
        super(entityClass);
    }
	
	@Override
    public String createSetup() {
        reset(false, true);
        showListing=false;
        entityItem = newInstance();
        navMenuBean.setNextItem(entityClass.getSimpleName());
        navMenuBean.addCrumb(entityClass.getSimpleName() + " Create");
        return outcomePrefix + "_create";
    }
		
	public Date getStartDate() {
	    	
		if (getItem().getStartDate() == null)
			return new Date();
    	
    	return getItem().getStartDate().getTime();
	}

	public void setStartDate(Date startDate) throws ParseException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		entityItem.setStartDate(cal);
	}
	
	public Date getEndDate() {
    	
		if (getItem().getEndDate() == null)
			return new Date();
    	
    	return getItem().getEndDate().getTime();
	}

	public void setEndDate(Date endDate) throws ParseException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		entityItem.setEndDate(cal);
	}
	
	// This method simple calls the the url of the demographic report and passes the uuid of the report
	// E.g. http://localhost:8088/openhds/demographic.report?ratesUUID=8ae07e7a2cdbcee0012cdbd3d3900001
	// calling this url tells spring to search registered controllers
	// for demographic reports, the DemographicReportsController gets called and the url is matched to a method
	public void redirectToReports() {
		
		String ratesUuid = jsfService.getReqParam("itemId");
				
		if (demRatesService.findDemRateByUuid(ratesUuid) != null) {
			String reportString = demRatesService.findDemRateByUuid(ratesUuid).getEvent().toLowerCase().replace("-", "").concat(".report?ratesUUID=");
		
			try {
				String context = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				FacesContext.getCurrentInstance().getExternalContext().redirect(context  + "/" + reportString + ratesUuid);
			} catch (Exception e) {
	            jsfService.addError(e.getMessage());
			}
		}
	}
	
	public DemRatesService getDemRatesService() {
		return demRatesService;
	}

	public void setDemRatesService(DemRatesService demRatesService) {
		this.demRatesService = demRatesService;
	}
	
	@Override
    public String create() {
		if(null == entityItem.getEvent() || entityItem.getEvent().isEmpty()){
			jsfService.addError("Please select an event rate to calculate");
			return null;
		}			
		if(null == entityItem.getDenominator() || entityItem.getDenominator().isEmpty()){
			jsfService.addError("Please select a denominator type");
			return null;
		}	
		return super.create();
	}
}
