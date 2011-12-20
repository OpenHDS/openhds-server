package org.openhds.report.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openhds.dao.service.GenericDao;
import org.openhds.dao.service.GenericDao.ValueProperty;
import org.openhds.domain.model.InMigration;
import org.openhds.domain.model.MigrationType;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.report.beans.MigrationEntReconciliationBean;
import org.openhds.report.service.MigrationEntReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MigrationEntReconciliationServiceImpl implements MigrationEntReconciliationService {

	GenericDao genericDao;
	SitePropertiesService properties;
	CalendarUtil calendarUtil;
	
	@Autowired
	public MigrationEntReconciliationServiceImpl(GenericDao genericDao, SitePropertiesService properties,
			CalendarUtil calendarUtil) {
		this.genericDao = genericDao;
		this.properties = properties;
		this.calendarUtil = calendarUtil;
	}
	
	@RequestMapping("/migration-ent-reconciliation.report")
	public ModelAndView getMigrationEntReconciliation() {
		ModelAndView mv = new ModelAndView("migrationEntReconciliation");
		Collection<MigrationEntReconciliationBean> beans = new ArrayList<MigrationEntReconciliationBean>();
		
		ValueProperty migrationType = new ValueProperty() {

			public String getPropertyName() {
				return "migType";
			}

			public Object getValue() {
				return MigrationType.INTERNAL_INMIGRATION;
			}	
		};
		
		ValueProperty unkIndiv = new ValueProperty() {

			public String getPropertyName() {
				return "unknownIndividual";
			}

			public Object getValue() {
				return new Boolean(true);
			}
		};
		
		// grab all in migrations that were created with a temporary individual id
		List<InMigration> migrations = genericDao.findListByMultiProperty(InMigration.class, migrationType, unkIndiv);
		
		for(InMigration mig : migrations) {
			MigrationEntReconciliationBean bean = new MigrationEntReconciliationBean();
			bean.setDate(calendarUtil.formatDate(mig.getRecordedDate()));
			bean.setFieldWorker(mig.getCollectedBy().getExtId());
			bean.setIndivId(mig.getIndividual().getExtId());
			String name = mig.getIndividual().getFirstName();
			name += (mig.getIndividual().getMiddleName() == null ? "" : " " + mig.getIndividual().getMiddleName());
			name += " " + mig.getIndividual().getLastName();
			bean.setName(name);
			
			beans.add(bean);
		}
		
		mv.addObject("theData", beans);
		return mv;
	}
}
