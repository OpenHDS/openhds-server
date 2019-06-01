package org.openhds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.openhds.service.FieldWorkerService;
import org.openhds.service.FormService;
import org.openhds.service.SettingsService;
import org.openhds.dao.Dao;
import org.openhds.annotations.Authorized;
import org.openhds.domain.AsyncTask;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.Form;
import org.openhds.domain.GeneralSettings;
import org.openhds.domain.GeneralSettings.SyncEntities;
import org.openhds.domain.GeneralSettings.SyncEntity;
import org.openhds.domain.PrivilegeConstants;
import org.openhds.service.SitePropertiesService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class SettingsServiceImpl implements SettingsService {

	private SitePropertiesService siteProperties;
	private Dao<AsyncTask, String> dao;
	private FormService formService;
	private FieldWorkerService fieldWorkerService;
	
	public SettingsServiceImpl(SitePropertiesService siteProperties, @Qualifier("taskDao") Dao<AsyncTask, String> dao, FormService formService,
			FieldWorkerService fieldWorkerService){
		this.siteProperties = siteProperties;
		this.dao = dao;
		this.formService = formService;
		this.fieldWorkerService = fieldWorkerService;
	}
		
	@Override
	@Authorized({PrivilegeConstants.VIEW_ENTITY})
	public GeneralSettings getSettings() {
		GeneralSettings gs = new GeneralSettings();
		int minimumAgeOfParents = siteProperties.getMinimumAgeOfParents();
		int minimumAgeOfHouseholdHead = siteProperties.getMinimumAgeOfHouseholdHead();
		int minMarriageAge = siteProperties.getMinimumAgeOfMarriage();
		int minimumAgeOfPregnancy = siteProperties.getMinimumAgeOfPregnancy();
		String visitAt = siteProperties.getVisitAt();
		String earliestEventDate = siteProperties.getEarliestEventDate(); 

		gs.setMinimumAgeOfParents(minimumAgeOfParents);
		gs.setMinimumAgeOfHouseholdHead(minimumAgeOfHouseholdHead);
		gs.setMinMarriageAge(minMarriageAge);
		gs.setMinimumAgeOfPregnancy(minimumAgeOfPregnancy);
		gs.setVisitAt(visitAt);
		gs.setEarliestEventDate(earliestEventDate);
		
		//Add no. of available entities
		List<SyncEntity> entityCountList = new ArrayList<SyncEntity>();
		List<AsyncTask> allTasks = findAllAsyncTask();
		for(AsyncTask asyncTask: allTasks){
			entityCountList.add(new SyncEntity(asyncTask.getTaskName(), (int)asyncTask.getTotalCount()));
		}
		
		//Add no. of available forms
		if(formService != null){
			List<Form> forms = formService.getAllActiveForms();
			entityCountList.add(new SyncEntity("Form", forms.size()));
		}
		
		//Add no. of available fieldworkers
		if(fieldWorkerService != null){
			List<FieldWorker> fieldworkers = fieldWorkerService.getAllFieldWorkers();
			entityCountList.add(new SyncEntity("Fieldworker", fieldworkers.size()));
		}
		
		gs.setSyncEntities(new SyncEntities(entityCountList));
		
		return gs;
	}
	
    @Transactional
    public List<AsyncTask> findAllAsyncTask() {
        return dao.findAll(false);
    }
}
