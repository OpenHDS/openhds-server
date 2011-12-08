package org.openhds.report.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.openhds.controller.util.CalendarUtil;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.PregnancyObservation;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.report.beans.PregObservReconciliationBean;
import org.openhds.report.service.PregnancyObservReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PregnancyObservReconciliationServiceImpl implements PregnancyObservReconciliationService {
	
	GenericDao genericDao;
	SitePropertiesService properties;
	CalendarUtil calendarUtil;

	@Autowired
	public PregnancyObservReconciliationServiceImpl(GenericDao genericDao, SitePropertiesService properties,
			CalendarUtil calendarUtil) {
		this.genericDao = genericDao;
		this.properties = properties;
		this.calendarUtil = calendarUtil;
	}
	
	@RequestMapping("/pregnancy-observ-reconciliation.report")
	public ModelAndView getPregnancyObservReconciliation() {
		ModelAndView mv = new ModelAndView("pregnancyObservReconciliation");
		Collection<PregObservReconciliationBean> beans = new ArrayList<PregObservReconciliationBean>();
					
		PregObservReconciliationBean bean = new PregObservReconciliationBean();
		bean.setCurrentDate(calendarUtil.formatDate(Calendar.getInstance()));
		
		// grab all Pregnancy Observations
		List<PregnancyObservation> list = genericDao.findListByProperty(PregnancyObservation.class, "status", properties.getDataStatusPendingCode());
		Collections.sort(list, new PregnancyObservationComparator());
		
		for(PregnancyObservation item : list) {
			
			if (item.getExpectedDeliveryDate().before(Calendar.getInstance())) {
				bean.setDate(calendarUtil.formatDate(item.getRecordedDate()));
				bean.setIndivId(item.getMother().getExtId());	
				beans.add(bean);
			}
		}
		
		if (list.size() == 0) {
			bean.setDate("");
			bean.setIndivId("");
			beans.add(bean);
		}
		
		mv.addObject("theData", beans);
		return mv;
	}
	
    private class PregnancyObservationComparator implements Comparator<PregnancyObservation> {

        public int compare(PregnancyObservation po1, PregnancyObservation po2) {
            return po1.getMother().getExtId().compareTo(po2.getMother().getExtId());
        }
    }
}

