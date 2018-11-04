package org.openhds.domain.util;

import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonShallowCopier {
	 private static final Logger logger = LoggerFactory.getLogger(JsonShallowCopier.class);
	 
	 public static Location copyLocation(Location loc) {
	        Location copy = new Location();

	        copy.setAccuracy(getEmptyStringIfBlank(loc.getAccuracy()));
	        copy.setAltitude(getEmptyStringIfBlank(loc.getAltitude()));
	        copy.setLatitude(getEmptyStringIfBlank(loc.getLatitude()));
	        copy.setLongitude(getEmptyStringIfBlank(loc.getLongitude()));

	        LocationHierarchy level = new LocationHierarchy();
	        level.setExtId(loc.getLocationLevel().getExtId());
	        level.setLevel(loc.getLocationLevel().getLevel());
	        level.setName(loc.getLocationLevel().getName());
	        level.setExtId(loc.getLocationLevel().getExtId());
	        level.setParent(loc.getLocationLevel().getParent());
	        copy.setLocationLevel(level);
	        copy.setUuid(loc.getUuid());
	        copy.setExtId(loc.getExtId());
	        copy.setLocationName(loc.getLocationName());
	        copy.setLocationType(loc.getLocationType());
	        	copy.setInsertDate(loc.getInsertDate());
	        	copy.setServerInsertTime(loc.getServerInsertTime());
	        	copy.setServerUpdateTime(loc.getServerUpdateTime());
	        FieldWorker fw = new FieldWorker();
	        fw.setExtId(loc.getCollectedBy().getExtId());
	        copy.setCollectedBy(fw);
	        return copy;
	    }
	 
	 public static Individual shallowCopyIndividual(Individual individual) {

	        Individual copy = new Individual();
	        try {
	            copy.setDob(individual.getDob());
	            copy.setExtId(individual.getExtId());
	            copy.setUuid(individual.getUuid());
	            copy.setFirstName(individual.getFirstName());
	            copy.setGender(individual.getGender());
	            copy.setLastName(individual.getLastName());
	            String middleName = individual.getMiddleName() == null ? "" : individual.getMiddleName();
	            copy.setMiddleName(middleName);
	            String religion = individual.getReligion() == null ? "" : individual.getReligion();
	            copy.setReligion(religion);
	            Individual father = shallowCopyIndividual(individual.getFather());
	            copy.setMother(father);
	            Individual mother = shallowCopyIndividual(individual.getMother());
	            copy.setMother(mother);
	            

	            
	            if (individual.getCurrentMembership() != null /*&& individual.getCurrentMembership().getEndDate()==null*/) {
	            	Membership memCopy = new Membership();
	                SocialGroup sgCopy = new SocialGroup();
	                sgCopy.setExtId(individual.getCurrentMembership().getSocialGroup().getExtId());

	                memCopy.setSocialGroup(sgCopy);
	                memCopy.setbIsToA(individual.getCurrentMembership().getbIsToA());
	                copy.getAllMemberships().add(memCopy);
	            }

	            if (individual.getCurrentResidency() != null) {
	                Residency resCopy = new Residency();
	                resCopy.setEndType(individual.getCurrentResidency().getEndType());
	                Location locCopy = new Location();
	                locCopy.setLocationLevel(null);
	                locCopy.setExtId(individual.getCurrentResidency().getLocation().getExtId());
	                resCopy.setLocation(locCopy);
	                copy.getAllResidencies().add(resCopy);
	            }
	        } catch (Exception e) {
	            System.out.println(copy.getExtId());
	        }
	        return copy;
	    }
	 
	 public static SocialGroup copySocialGroup(SocialGroup original) {
	        SocialGroup copy = new SocialGroup();
	        copy.setExtId(original.getExtId());
	        copy.setUuid(original.getUuid());
	        Individual groupHead = shallowCopyIndividual(original.getGroupHead());
	        copy.setGroupHead(groupHead);
	        copy.setGroupName(original.getGroupName());
	        return copy;
	    }
	 
	  private static String getEmptyStringIfBlank(String accuracy) {
	        if (accuracy == null || accuracy.trim().isEmpty()) {
	            return "";
	        }

	        return accuracy;
	    }

	public static FieldWorker shallowCopyFieldWorker(FieldWorker fieldWorker) {
		FieldWorker copy = new FieldWorker();
		copy.setUuid(fieldWorker.getUuid());
		copy.setExtId(fieldWorker.getExtId());
		copy.setFirstName(fieldWorker.getFirstName());
		copy.setLastName(fieldWorker.getLastName());
		copy.setPasswordHash(fieldWorker.getPasswordHash());
		return copy;
	}
	
	public static User shallowCopyUser(User user) {
		User copy = new User();
		copy.setUuid(user.getUuid());
		copy.setUsername(user.getUsername());
		copy.setPassword(user.getPassword());
		copy.setFirstName(user.getFirstName());
		copy.setLastName(user.getLastName());
		copy.setRoles(user.getRoles());

		return copy;
	}
}
