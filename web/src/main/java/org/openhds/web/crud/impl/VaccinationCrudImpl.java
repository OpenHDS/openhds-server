package org.openhds.web.crud.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.openhds.domain.model.Vaccination;

public class VaccinationCrudImpl extends EntityCrudImpl<Vaccination, String> {

	Date bcg;
	Date polio1;
	Date polio2;
	Date polio3;
	Date polio4;
	Date dpt1;
	Date dpt2;
	Date dpt3;
	Date measels;

	public VaccinationCrudImpl(Class<Vaccination> entityClass) {
		super(entityClass);
	}
	
	 @Override
     public String create() {
        try {
             super.create();
        } catch (Exception e) {
            jsfService.addError(e.getMessage());
        }
        return null;
     }
	
    @Override
    public String edit() {
    	String outcome = super.edit();
    	
    	if (outcome != null) {
    		return "pretty:vaccinationEdit";
    	}
    	
    	return null;
    }
	
	public void setBcg(Date bcg) throws ParseException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(bcg);
		entityItem.setBcg(cal);
	}
	
	public Date getBcg() {
    	
    	if (entityItem.getBcg() == null)
    		return null;
    	
    	return entityItem.getBcg().getTime();
	}
	
	public void setPolio1(Date polio1) throws ParseException {
		
		if (polio1 == null)
			return;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(polio1);
		entityItem.setPolio1(cal);
	}
	
	public Date getPolio1() {
    	
    	if (entityItem.getPolio1() == null)
    		return null;
    	
    	return entityItem.getPolio1().getTime();
	}
	
	public void setPolio2(Date polio2) throws ParseException {
		
		if (polio2 == null)
			return;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(polio2);
		entityItem.setPolio2(cal);
	}
	
	public Date getPolio2() {
    	
    	if (entityItem.getPolio2() == null)
    		return null;
    	
    	return entityItem.getPolio2().getTime();
	}
	
	public void setPolio3(Date polio3) throws ParseException {
		
		if (polio3 == null)
			return;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(polio3);
		entityItem.setPolio3(cal);
	}
	
	public Date getPolio3() {
    	
    	if (entityItem.getPolio3() == null)
    		return null;
    	
    	return entityItem.getPolio3().getTime();
	}
	
	public void setPolio4(Date polio4) throws ParseException {
		
		if (polio4 == null)
			return;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(polio4);
		entityItem.setPolio4(cal);
	}
	
	public Date getPolio4() {
    	
    	if (entityItem.getPolio4() == null)
    		return null;
    	
    	return entityItem.getPolio4().getTime();
	}
	
	public void setDpt1(Date dpt1) throws ParseException {
		
		if (dpt1 == null)
			return;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dpt1);
		entityItem.setDpt1(cal);
	}
	
	public Date getDpt1() {
    	
    	if (entityItem.getDpt1() == null)
    		return null;
    	
    	return entityItem.getDpt1().getTime();
	}
	
	public void setDpt2(Date dpt2) throws ParseException {
		
		if (dpt2 == null)
			return;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dpt2);
		entityItem.setDpt2(cal);
	}
	
	public Date getDpt2() {
    	
    	if (entityItem.getDpt2() == null)
    		return null;
    	
    	return entityItem.getDpt2().getTime();
	}
	
	public void setDpt3(Date dpt3) throws ParseException {
		
		if (dpt3 == null)
			return;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dpt3);
		entityItem.setDpt3(cal);
	}
	
	public Date getDpt3() {
    	
    	if (entityItem.getDpt3() == null)
    		return null;
    	
    	return entityItem.getDpt3().getTime();
	}
	
	public void setMeasels(Date measels) throws ParseException {
		
		if (measels == null)
			return;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(measels);
		entityItem.setMeasels(cal);
	}
	
	public Date getMeasels() {
    	
    	if (entityItem.getMeasels() == null)
    		return null;
    	
    	return entityItem.getMeasels().getTime();
	}
}
