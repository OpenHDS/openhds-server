package org.openhds.web.beans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openhds.service.EntityValidationService;
import org.openhds.dao.GenericDao;
import org.openhds.exception.ConstraintViolations;
import org.openhds.idgeneration.IndividualGenerator;
import org.openhds.idgeneration.LocationGenerator;
import org.openhds.idgeneration.SocialGroupGenerator;
import org.openhds.idgeneration.VisitGenerator;
import org.openhds.domain.AuditableCollectedEntity;
import org.openhds.domain.Death;
import org.openhds.domain.InMigration;
import org.openhds.domain.Individual;
import org.openhds.domain.Location;
import org.openhds.domain.Membership;
import org.openhds.domain.OutMigration;
import org.openhds.domain.PregnancyObservation;
import org.openhds.domain.PregnancyOutcome;
import org.openhds.domain.Relationship;
import org.openhds.domain.Residency;
import org.openhds.domain.SocialGroup;
import org.openhds.domain.Visit;
import org.openhds.service.DeathService;
import org.openhds.service.InMigrationService;
import org.openhds.service.IndividualService;
import org.openhds.service.LocationHierarchyService;
import org.openhds.service.MembershipService;
import org.openhds.service.OutMigrationService;
import org.openhds.service.PregnancyService;
import org.openhds.service.RelationshipService;
import org.openhds.service.ResidencyService;
import org.openhds.service.SocialGroupService;
import org.openhds.service.VisitService;
import org.openhds.service.SitePropertiesService;

public class ValidationRoutineBean {
	
	IndividualService individualService;
	LocationHierarchyService locationService;
	SocialGroupService socialgroupService;
	RelationshipService relationshipService;
	MembershipService membershipService;
	PregnancyService pregnancyService;
	VisitService visitService;
	DeathService deathService;
	InMigrationService migrationService;
	OutMigrationService outmigrationService;
	InMigrationService inmigrationService;
	ResidencyService residencyService;
	GenericDao genericDao;
	EntityValidationService<?> entityValidator;
	SitePropertiesService properties;
	IndividualGenerator indivGenerator;
	LocationGenerator locGenerator;
	SocialGroupGenerator sgGenerator;
	VisitGenerator visitGenerator;
	
	boolean run = false;
	Map<String, List<String>> errors = new HashMap<String, List<String>>();
	
	public void setup() {
		errors.clear();
		run = false;
	}
		
	public void validateIndividuals() throws ConstraintViolations, SQLException {
		setup();
		run = true;
		List<String> list = new ArrayList<String>();
		List<Individual> indivs = genericDao.findAll(Individual.class, false);
		for (Individual i : indivs) {
			if (processDeletedEntity(i))
				continue;
			
			if (!i.getExtId().equals(properties.getUnknownIdentifier())) {
				
				try {
					List<String> violations = entityValidator.validateType(i);
					
					individualService.validateGeneralIndividual(i);
					if (indivGenerator.isGenerated())
						indivGenerator.validateIdLength(i.getExtId(), indivGenerator.getIdScheme());
	
					if (violations.size() > 0) {
						i.setStatus(properties.getDataStatusFatalCode());
						list.add("individual/details/" + i.getUuid());
						i.setStatusMessage(violations.get(0));
						genericDao.update(i);
					}	
					else {
						i.setStatus(properties.getDataStatusValidCode());
						i.setStatusMessage("");
						genericDao.update(i);
					}
				}
				catch (Exception e) {
					i.setStatus(properties.getDataStatusFatalCode());
					list.add("individual/details/" + i.getUuid());
					i.setStatusMessage(e.getMessage());
					genericDao.update(i);
				}
			}
		}
		if (list.size() > 0)
			errors.put("individual", list);
	}
	
	public void validateLocations() throws ConstraintViolations, SQLException {
		setup();
		run = true;
		List<String> list = new ArrayList<String>();
		List<Location> locs = genericDao.findAll(Location.class, false);
		for (Location loc : locs) {		
			if (processDeletedEntity(loc))
				continue;
			
			list.addAll(validateLocation(loc));
		}
		if (list.size() > 0)
			errors.put("location", list);
	}
		
	public void validateSocialGroups() {
		setup();
		run = true;
		List<String> list = new ArrayList<String>();
		List<SocialGroup> sgs = genericDao.findAll(SocialGroup.class, false);
		for (SocialGroup sg : sgs) {
			if (processDeletedEntity(sg))
				continue;
			
			list.addAll(validateSocialGroup(sg));
		}
		if (list.size() > 0)
			errors.put("socialgroup", list);
	}
	
	public void validateRelationships() {
		setup();
		run = true;	
		List<String> list = new ArrayList<String>();
		List<Relationship> rels = genericDao.findAll(Relationship.class, false);
		for (Relationship rel : rels) {
			
			if (processDeletedEntity(rel))
				continue;
			
			try {
				List<String> violations = entityValidator.validateType(rel);
				relationshipService.validateGeneralRelationship(rel);
				
				if (violations.size() > 0) {
					rel.setStatus(properties.getDataStatusFatalCode());
					list.add("relationship/details/" + rel.getUuid());
					rel.setStatusMessage(violations.get(0));
					genericDao.update(rel);
				}	
				else {
					rel.setStatus(properties.getDataStatusValidCode());
					rel.setStatusMessage("");
					genericDao.update(rel);
				}		
			} 
			catch (Exception e) { 
				rel.setStatus(properties.getDataStatusFatalCode());
				list.add("relationship/details/" + rel.getUuid());
				rel.setStatusMessage(e.getMessage());
				genericDao.update(rel);
			}	
		}
		if (list.size() > 0)
			errors.put("relationship", list);
	}
	

	public void validateMemberships() {
		setup();
		run = true;	
		List<String> list = new ArrayList<String>();
		List<Membership> mems = genericDao.findAll(Membership.class, false);
		for (Membership mem : mems) {
			
			if (processDeletedEntity(mem))
				continue;
			
			try {
				List<String> violations = entityValidator.validateType(mem);
				membershipService.validateGeneralMembership(mem);
								
				if (violations.size() > 0) {
					mem.setStatus(properties.getDataStatusFatalCode());
					list.add("membership/details/" + mem.getUuid());
					mem.setStatusMessage(violations.get(0));
					genericDao.update(mem);
				}	
				else {
					mem.setStatus(properties.getDataStatusValidCode());
					mem.setStatusMessage("");
					genericDao.update(mem);
				}	
			} 
			catch (Exception e) { 
				mem.setStatus(properties.getDataStatusFatalCode());
				list.add("membership/details/" + mem.getUuid());
				mem.setStatusMessage(e.getMessage());
				genericDao.update(mem);
			}		
		}
		if (list.size() > 0)
			errors.put("membership", list);
	}
	
	
	public void validatePregnancyOutcomes() {
		setup();
		run = true;	
		List<String> list = new ArrayList<String>();
		List<PregnancyOutcome> pos = genericDao.findAll(PregnancyOutcome.class, false);
		for (PregnancyOutcome po : pos) {
			
			if (processDeletedEntity(po))
				continue;
			
			try {
				List<String> violations = entityValidator.validateType(po);
				
				individualService.validateGeneralIndividual(po.getMother());
				validateVisit(po.getVisit());
				
				if (violations.size() > 0) {
					po.setStatus(properties.getDataStatusFatalCode());
					list.add("pregnancy-outcome/details/" + po.getUuid());
					po.setStatusMessage(violations.get(0));
					genericDao.update(po);
				}	
				else {
					po.setStatus(properties.getDataStatusValidCode());
					po.setStatusMessage("");
					genericDao.update(po);
				}	
			} 
			catch (Exception e) { 
				po.setStatus(properties.getDataStatusFatalCode());
				list.add("pregnancy-outcome/details/" + po.getUuid());
				po.setStatusMessage(e.getMessage());
				genericDao.update(po);
			}		
		}
		if (list.size() > 0)
			errors.put("pregnancyOutcome", list);
	}
	
	
	public void validatePregnancyObservations() {
		setup();
		run = true;	
		List<String> list = new ArrayList<String>();
		List<PregnancyObservation> pos = genericDao.findAll(PregnancyObservation.class, false);
		for (PregnancyObservation po : pos) {
			
			if (processDeletedEntity(po))
				continue;
			
			try {
				List<String> violations = entityValidator.validateType(po);
				individualService.validateGeneralIndividual(po.getMother());
				pregnancyService.validateGeneralPregnancyObservation(po);
				
				if (violations.size() > 0) {
					po.setStatus(properties.getDataStatusFatalCode());
					list.add("pregnancyobservation/details/" + po.getUuid());
					po.setStatusMessage(violations.get(0));
					genericDao.update(po);
				}	
				else {
					po.setStatus(properties.getDataStatusValidCode());
					po.setStatusMessage("");
					genericDao.update(po);
				}	
			} 
			catch (Exception e) { 
				po.setStatus(properties.getDataStatusFatalCode());
				list.add("pregnancyobservation/details/" + po.getUuid());
				po.setStatusMessage(e.getMessage());
				genericDao.update(po);
			}		
		}
		if (list.size() > 0)
			errors.put("pregnancyObservation", list);
	}
	
	
	public void validateInMigrations() {
		setup();
		run = true;	
		List<String> list = new ArrayList<String>();
		List<InMigration> inmigs = genericDao.findAll(InMigration.class, false);
		for (InMigration inmig : inmigs) {
			
			if (processDeletedEntity(inmig))
				continue;
			
			try {
				List<String> violations = entityValidator.validateType(inmig);
				individualService.validateGeneralIndividual(inmig.getIndividual());
				validateVisit(inmig.getVisit());
				
				if (violations.size() > 0) {
					inmig.setStatus(properties.getDataStatusFatalCode());
					list.add("inmigration/details/" + inmig.getUuid());
					inmig.setStatusMessage(violations.get(0));
					genericDao.update(inmig);
				}	
				else {
					inmig.setStatus(properties.getDataStatusValidCode());
					inmig.setStatusMessage("");
					genericDao.update(inmig);
				}	
			} 
			catch (Exception e) { 
				inmig.setStatus(properties.getDataStatusFatalCode());
				list.add("inmigration/details/" + inmig.getUuid());
				inmig.setStatusMessage(e.getMessage());
				genericDao.update(inmig);
			}		
		}
		if (list.size() > 0)
			errors.put("inMigration", list);
	}
	
	
	public void validateOutMigrations() {
		setup();
		run = true;	
		List<String> list = new ArrayList<String>();
		List<OutMigration> outmigs = genericDao.findAll(OutMigration.class, false);
		for (OutMigration outmig : outmigs) {
			
			if (processDeletedEntity(outmig))
				continue;
			
			try {
				List<String> violations = entityValidator.validateType(outmig);
				individualService.validateGeneralIndividual(outmig.getIndividual());
				validateVisit(outmig.getVisit());
				
				if (violations.size() > 0) {
					outmig.setStatus(properties.getDataStatusFatalCode());
					list.add("outmigration/details/" + outmig.getUuid());
					outmig.setStatusMessage(violations.get(0));
					genericDao.update(outmig);
				}	
				else {
					outmig.setStatus(properties.getDataStatusValidCode());
					outmig.setStatusMessage("");
					genericDao.update(outmig);
				}	
			} 
			catch (Exception e) { 
				outmig.setStatus(properties.getDataStatusFatalCode());
				list.add("inmigration/details/" + outmig.getUuid());
				outmig.setStatusMessage(e.getMessage());
				genericDao.update(outmig);
			}		
		}
		if (list.size() > 0)
			errors.put("outMigration", list);
	}
	
	public void validateResidencies() {
		setup();
		run = true;	
		List<String> list = new ArrayList<String>();
		List<Residency> residencies = genericDao.findAll(Residency.class, false);
		for (Residency res : residencies) {
			
			if (processDeletedEntity(res))
				continue;
			try {
				List<String> violations = entityValidator.validateType(res);
				validateLocation(res.getLocation());
				
				if (violations.size() > 0) {
					res.setStatus(properties.getDataStatusFatalCode());
					list.add("residency/details/" + res.getUuid());
					res.setStatusMessage(violations.get(0));
					genericDao.update(res);
				}	
				else {
					res.setStatus(properties.getDataStatusValidCode());
					res.setStatusMessage("");
					genericDao.update(res);
				}	
			} 
			catch (Exception e) { 
				res.setStatus(properties.getDataStatusFatalCode());
				list.add("residency/details/" + res.getUuid());
				res.setStatusMessage(e.getMessage());
				genericDao.update(res);
			}		
		}
		if (list.size() > 0)
			errors.put("residency", list);
	}
	
	public void validateVisits() {
		setup();
		run = true;	
		List<String> list = new ArrayList<String>();
		List<Visit> visits = genericDao.findAll(Visit.class, false);
		for (Visit visit : visits) {		
			processDeletedEntity(visit);	
			list.addAll(validateVisit(visit));
		}
		if (list.size() > 0)
			errors.put("visit", list);
	}
	
	
	public void validateDeaths() {
		setup();
		run = true;	
		List<String> list = new ArrayList<String>();
		List<Death> deaths = genericDao.findAll(Death.class, false);
		for (Death death : deaths) {
			
			processDeletedEntity(death);	
			try {
				List<String> violations = entityValidator.validateType(death);
				individualService.validateGeneralIndividual(death.getIndividual());
				validateVisit(death.getVisitDeath());
				
				if (violations.size() > 0) {
					death.setStatus(properties.getDataStatusFatalCode());
					list.add("death/details/" + death.getUuid());
					death.setStatusMessage(violations.get(0));
					genericDao.update(death);
				}	
				else {
					death.setStatus(properties.getDataStatusValidCode());
					death.setStatusMessage("");
					genericDao.update(death);
				}	
			} 
			catch (Exception e) { 
				death.setStatus(properties.getDataStatusFatalCode());
				list.add("death/details/" + death.getUuid());
				death.setStatusMessage(e.getMessage());
				genericDao.update(death);
			}		
		}
		if (list.size() > 0)
			errors.put("death", list);
	}
	
	
	private List<String> validateLocation(Location loc) {
		List<String> list = new ArrayList<String>();
		try {
			List<String> violations = entityValidator.validateType(loc);
			
			if (locGenerator.isGenerated())
				locGenerator.validateIdLength(loc.getExtId(), locGenerator.getIdScheme());

			if (violations.size() > 0) {
				loc.setStatus(properties.getDataStatusFatalCode());
				list.add("location/details/" + loc.getUuid());
				loc.setStatusMessage(violations.get(0));
				genericDao.update(loc);
			}	
			else {
				loc.setStatus(properties.getDataStatusValidCode());
				loc.setStatusMessage("");
				genericDao.update(loc);
			}	
		}
		catch (Exception e) {
			loc.setStatus(properties.getDataStatusFatalCode());
			list.add("location/details/" + loc.getUuid());
			loc.setStatusMessage(e.getMessage());
			genericDao.update(loc);
		}
		if (list.size() > 0)
			errors.put("location", list);
		return list;
	}
	
	
	private List<String> validateVisit(Visit visit) {
		List<String> list = new ArrayList<String>();
		try {
			List<String> violations = entityValidator.validateType(visit);
			
			validateLocation(visit.getVisitLocation());
			if (visitGenerator.isGenerated())
				locGenerator.validateIdLength(visit.getExtId(), visitGenerator.getIdScheme());
			
			visitService.validateGeneralVisit(visit);
			
			if (violations.size() > 0) {
				visit.setStatus(properties.getDataStatusFatalCode());
				list.add("visit/details/" + visit.getUuid());
				visit.setStatusMessage(violations.get(0));
				genericDao.update(visit);
			}	
			else {
				visit.setStatus(properties.getDataStatusValidCode());
				visit.setStatusMessage("");
				genericDao.update(visit);
			}	
		}
		catch (Exception e) {
			visit.setStatus(properties.getDataStatusFatalCode());
			list.add("visit/details/" + visit.getUuid());
			visit.setStatusMessage(e.getMessage());
			genericDao.update(visit);
		}
		if (list.size() > 0)
			errors.put("visit", list);	
		return list;
	}
	
	
	private List<String> validateSocialGroup(SocialGroup sg) {
		List<String> list = new ArrayList<String>();
		try {
			List<String> violations = entityValidator.validateType(sg);
			
			if (sgGenerator.isGenerated())
				sgGenerator.validateIdLength(sg.getExtId(), sgGenerator.getIdScheme());
			
			if (violations.size() > 0) {
				sg.setStatus(properties.getDataStatusFatalCode());
				list.add("socialgroup/details/" + sg.getUuid());
				sg.setStatusMessage(violations.get(0));
				genericDao.update(sg);
			}	
			else {
				sg.setStatus(properties.getDataStatusValidCode());
				sg.setStatusMessage("");
				genericDao.update(sg);
			}	
		}
		catch (Exception e) {
			sg.setStatus(properties.getDataStatusFatalCode());
			list.add("socialgroup/details/" + sg.getUuid());
			sg.setStatusMessage(e.getMessage());
			genericDao.update(sg);
		}
		if (list.size() > 0)
			errors.put("socialgroup", list);
		return list;
	}
	
	private boolean processDeletedEntity(AuditableCollectedEntity entity) {
		if (entity.isDeleted()) {
			entity.setStatus(properties.getDataStatusClosedCode());
			entity.setStatusMessage("");
			genericDao.update(entity);
			return true;
		}
		return false;
	}
	
	public IndividualService getIndividualService() {
		return individualService;
	}

	public void setIndividualService(IndividualService individualService) {
		this.individualService = individualService;
	}

	public LocationHierarchyService getLocationService() {
		return locationService;
	}

	public void setLocationService(LocationHierarchyService locationService) {
		this.locationService = locationService;
	}

	public SocialGroupService getSocialgroupService() {
		return socialgroupService;
	}

	public void setSocialgroupService(SocialGroupService socialgroupService) {
		this.socialgroupService = socialgroupService;
	}

	public RelationshipService getRelationshipService() {
		return relationshipService;
	}

	public void setRelationshipService(RelationshipService relationshipService) {
		this.relationshipService = relationshipService;
	}

	public MembershipService getMembershipService() {
		return membershipService;
	}

	public void setMembershipService(MembershipService membershipService) {
		this.membershipService = membershipService;
	}

	public PregnancyService getPregnancyService() {
		return pregnancyService;
	}

	public void setPregnancyService(PregnancyService pregnancyService) {
		this.pregnancyService = pregnancyService;
	}

	public VisitService getVisitService() {
		return visitService;
	}

	public void setVisitService(VisitService visitService) {
		this.visitService = visitService;
	}

	public DeathService getDeathService() {
		return deathService;
	}

	public void setDeathService(DeathService deathService) {
		this.deathService = deathService;
	}

	public InMigrationService getMigrationService() {
		return migrationService;
	}

	public void setMigrationService(InMigrationService migrationService) {
		this.migrationService = migrationService;
	}

	public OutMigrationService getOutmigrationService() {
		return outmigrationService;
	}

	public void setOutmigrationService(OutMigrationService outmigrationService) {
		this.outmigrationService = outmigrationService;
	}
	
	public InMigrationService getInmigrationService() {
		return inmigrationService;
	}

	public void setInmigrationService(InMigrationService inmigrationService) {
		this.inmigrationService = inmigrationService;
	}

	public ResidencyService getResidencyService() {
		return residencyService;
	}

	public void setResidencyService(ResidencyService residencyService) {
		this.residencyService = residencyService;
	}

	public GenericDao getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
		
	
	public SitePropertiesService getProperties() {
		return properties;
	}

	public void setProperties(SitePropertiesService properties) {
		this.properties = properties;
	}
	
	public List<?> getErrorKeys() {
		return new ArrayList<Object>(errors.entrySet());
	}
	
	public Map<String, List<String>> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, List<String>> errors) {
		this.errors = errors;
	}
	
	public boolean isRun() {
		return run;
	}
	
	public EntityValidationService<?> getEntityValidator() {
		return entityValidator;
	}

	public void setEntityValidator(EntityValidationService<?> entityValidator) {
		this.entityValidator = entityValidator;
	}
	
	public IndividualGenerator getIndivGenerator() {
		return indivGenerator;
	}

	public void setIndivGenerator(IndividualGenerator indivGenerator) {
		this.indivGenerator = indivGenerator;
	}

	public LocationGenerator getLocGenerator() {
		return locGenerator;
	}

	public void setLocGenerator(LocationGenerator locGenerator) {
		this.locGenerator = locGenerator;
	}
	
	public SocialGroupGenerator getSgGenerator() {
		return sgGenerator;
	}

	public void setSgGenerator(SocialGroupGenerator sgGenerator) {
		this.sgGenerator = sgGenerator;
	}

	public VisitGenerator getVisitGenerator() {
		return visitGenerator;
	}

	public void setVisitGenerator(VisitGenerator visitGenerator) {
		this.visitGenerator = visitGenerator;
	}
}
