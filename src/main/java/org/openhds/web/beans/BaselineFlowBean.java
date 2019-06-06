package org.openhds.web.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.openhds.dao.GenericDao;
import org.openhds.exception.ConstraintViolations;
import org.openhds.idgeneration.Generator;
import org.openhds.idgeneration.IdSchemeResource;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.Individual;
import org.openhds.domain.Location;
import org.openhds.domain.LocationHierarchy;
import org.openhds.domain.Membership;
import org.openhds.domain.SocialGroup;
import org.openhds.service.BaselineService;
import org.openhds.service.IndividualService;
import org.openhds.service.ResidencyService;
import org.openhds.service.SitePropertiesService;
import org.openhds.service.WebFlowService;
import org.springframework.binding.message.MessageContext;
import java.text.ParseException;

public class BaselineFlowBean implements Serializable {
	
    private static final long serialVersionUID = 2721358112424491641L;
    
    SocialGroup currentSocialGroup;
    Individual selectedIndividual;
	LocationHierarchy currentVillage;
	Location currentLocation;
	
	Individual headOfHouse;
	Individual headOfHousehold;
	FieldWorker collectedBy;
	SocialGroup socialGroup;
	
	// used for manual conversion between Date and Calendar since the openFaces Calendar doesn't support JSF Converters
	Date entryDate;
	Calendar calDate;

    GenericDao genericDao;
    ResidencyService residencyService;
    BaselineService baselineService;
    IndividualService individualService;
	WebFlowService webFlowService;
	Generator<SocialGroup> socialGroupGenerator;
	SitePropertiesService properties;
	IdSchemeResource idSchemeResource;
	String locationName ="NOT SELECTED";
	String sgName ="NOT SELECTED";
			
	public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getSgName() {
        return sgName;
    }

    public void setSgName(String sgName) {
        this.sgName = sgName;
    }

    public void clear() {
    	currentVillage = null;
        currentLocation = null;
        currentSocialGroup = null;
        collectedBy = null;
        selectedIndividual = null;
        entryDate = null;
    }
    	
	public boolean createResidencyAndMembership(Individual individual, Membership membership, MessageContext context) throws IllegalArgumentException, SQLException, ConstraintViolations {
    	try {
    		baselineService.createResidencyAndMembershipForIndividual(individual, membership, currentLocation, collectedBy, getConvertedEntryDate());
    	}
    	catch(Exception e) {
    		webFlowService.createMessage(context, e.getMessage());
    		return false;
    	}
    	return true;
    }
	
	public boolean createSocialGroupAndIndividual(Individual individual, SocialGroup socialGroup, MessageContext context) throws IllegalArgumentException, SQLException, ConstraintViolations {
		
		try {
			baselineService.createSocialGroupAndResidencyForIndividual(individual, socialGroup, currentLocation, collectedBy, getConvertedEntryDate());
		}
		catch(Exception e) {
    		webFlowService.createMessage(context, e.getMessage());
    		return false;
    	}
    	return true;
	}
	
	public boolean determineValidDate(MessageContext messageContext) {
		
		if (entryDate != null)
			return true;
		
		webFlowService.createMessage(messageContext, 
		"The entry date must be specified.");
		return false;
	}
	
	public boolean determineValidLocation(MessageContext messageContext) {
		
		if (currentLocation.isDeleted()) {
			webFlowService.createMessage(messageContext, 
			"The specified Location cannot be modified if it was previously marked as deleted.");
			return false;
		}	
		return true;
	}
	
	public boolean determineValidSocialGroup(MessageContext messageContext) {
		
		if (currentSocialGroup.isDeleted()) {
			webFlowService.createMessage(messageContext, 
			"The specified Social Group cannot be modified if it was previously marked as deleted.");
			return false;
		}	
		return true;
	}
	
	public boolean checkBean(MessageContext messageContext) {
		if (selectedIndividual != null)
			return true;
		
		webFlowService.createMessage(messageContext, 
				"Cannot proceed to create a new Membership to the Social Group Head without specifying the related Individual.");
		return false;
	}
	
    public Calendar getConvertedEntryDate() {
    	if (entryDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(entryDate);
			return cal;
    	}
    	return null;
    }
    
    public int getSizeOfIndividualsAtLocation() {
        if (getIndividualsAtLocation() != null) {
            return getIndividualsAtLocation().size();
        }
        return 0;
    }
    
    public List<Individual> getIndividualsAtLocation() {
        if (currentLocation == null) {
            return null;
        }
        return residencyService.getIndividualsByLocation(currentLocation);
    }

    public Integer getMembershipListSize() {
        if (getMembershipsAtLocation() != null) {
            return getMembershipsAtLocation().size();
        }
        return 0;
    }
    
    public List<Membership> getMembershipsAtLocation() {
        if (currentSocialGroup == null) {
        	return null;
        }
       
        return genericDao.findListByProperty(Membership.class, "socialGroup", currentSocialGroup);
    }
    
    public Date getEntryDate() {
    	return new Date();
	}

	public void setEntryDate(Date entryDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(entryDate);
		calDate = cal;
		this.entryDate = entryDate;
	}
    
    public Individual getSelectedIndividual() {
		return selectedIndividual;
	}

	public void setSelectedIndividual(Individual selectedIndividual) {
		this.selectedIndividual = selectedIndividual;
	}
	
	public FieldWorker getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(FieldWorker collectedBy) {
        this.collectedBy = collectedBy;
    }
    
    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        setLocationName(currentLocation.getLocationName());
    }
    
    public SocialGroup getCurrentSocialGroup() {
        return currentSocialGroup;
    }

    public void setCurrentSocialGroup(SocialGroup currentSocialGroup) {
        this.currentSocialGroup = currentSocialGroup;
        setSgName(currentSocialGroup.getGroupName());
    }

    public GenericDao getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(GenericDao genericDao) {
        this.genericDao = genericDao;
    }

    public ResidencyService getResidencyService() {
        return residencyService;
    }

    public void setResidencyService(ResidencyService residencyService) {
        this.residencyService = residencyService;
    }
    	
	public BaselineService getBaselineService() {
		return baselineService;
	}

	public void setBaselineService(BaselineService baselineService) {
		this.baselineService = baselineService;
	}
	
	public IndividualService getIndividualService() {
		return individualService;
	}

	public void setIndividualService(IndividualService individualService) {
		this.individualService = individualService;
	}
	
    public WebFlowService getWebFlowService() {
		return webFlowService;
	}

	public void setWebFlowService(WebFlowService webFlowService) {
		this.webFlowService = webFlowService;
	}
	
	public void setSelectedIndividual(String individualId) {
		this.selectedIndividual = genericDao.read(Individual.class, individualId);
	}

	public LocationHierarchy getCurrentVillage() {
		return currentVillage;
	}

	public void setCurrentVillage(LocationHierarchy currentVillage) {
		this.currentVillage = currentVillage;
	}
	
	public IdSchemeResource getIdSchemeResource() {
		return idSchemeResource;
	}

	public void setIdSchemeResource(IdSchemeResource idSchemeResource) {
		this.idSchemeResource = idSchemeResource;
	}
	
	public Generator<SocialGroup> getSocialGroupGenerator() {
		return socialGroupGenerator;
	}

	public void setSocialGroupGenerator(Generator<SocialGroup> socialGroupGenerator) {
		this.socialGroupGenerator = socialGroupGenerator;
	}

	public SitePropertiesService getProperties() {
		return properties;
	}

	public void setProperties(SitePropertiesService properties) {
		this.properties = properties;
	}
}
