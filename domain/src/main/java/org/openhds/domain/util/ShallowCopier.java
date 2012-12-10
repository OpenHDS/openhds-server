package org.openhds.domain.util;

import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Relationship;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.SocialGroup;
import org.openhds.domain.model.Visit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShallowCopier {

    private static final Logger logger = LoggerFactory.getLogger(ShallowCopier.class);

    public static Individual shallowCopyIndividual(Individual individual) {

        Individual copy = new Individual();
        try {
            copy.setDob(individual.getDob());
            copy.setExtId(individual.getExtId());

            copy.setFather(copyExtId(individual.getFather()));
            copy.setFirstName(individual.getFirstName());
            copy.setGender(individual.getGender());
            copy.setLastName(individual.getLastName());
            String middleName = individual.getMiddleName() == null ? "" : individual.getMiddleName();
            copy.setMiddleName(middleName);
            copy.setMother(copyExtId(individual.getMother()));

            for (Membership membership : individual.getAllMemberships()) {
                Membership memCopy = new Membership();

                SocialGroup sgCopy = new SocialGroup();
                sgCopy.setExtId(membership.getSocialGroup().getExtId());

                memCopy.setSocialGroup(sgCopy);
                memCopy.setbIsToA(membership.getbIsToA());
                copy.getAllMemberships().add(memCopy);
            }

            if (individual.getCurrentResidency() != null) {
                Residency resCopy = new Residency();
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

    private static Individual copyExtId(Individual individual) {
        Individual copy = new Individual();
        if (individual == null) {
            logger.warn("Individual had a null father or mother - using UNK as default value");
            copy.setExtId("UNK");
        } else {
            copy.setExtId(individual.getExtId());
        }

        return copy;
    }

    public static Location copyLocation(Location loc) {
        Location copy = new Location();

        copy.setAccuracy(getEmptyStringIfBlank(loc.getAccuracy()));
        copy.setAltitude(getEmptyStringIfBlank(loc.getAltitude()));
        copy.setLatitude(getEmptyStringIfBlank(loc.getLatitude()));
        copy.setLongitude(getEmptyStringIfBlank(loc.getLongitude()));

        LocationHierarchy level = new LocationHierarchy();
        level.setExtId(loc.getLocationLevel().getExtId());
        copy.setLocationLevel(level);

        copy.setExtId(loc.getExtId());
        copy.setLocationName(loc.getLocationName());
        copy.setLocationType(loc.getLocationType());

        FieldWorker fw = new FieldWorker();
        fw.setExtId(loc.getCollectedBy().getExtId());
        copy.setCollectedBy(fw);
        return copy;
    }

    private static String getEmptyStringIfBlank(String accuracy) {
        if (accuracy == null || accuracy.trim().isEmpty()) {
            return "";
        }

        return accuracy;
    }

    public static Relationship copyRelationship(Relationship original) {
        Relationship copy = new Relationship();
        copy.setaIsToB(original.getaIsToB());

        Individual individual = new Individual();
        individual.setExtId(original.getIndividualA().getExtId());
        copy.setIndividualA(individual);

        individual = new Individual();
        individual.setExtId(original.getIndividualB().getExtId());
        copy.setIndividualB(individual);

        copy.setStartDate(original.getStartDate());
        return copy;
    }

    public static SocialGroup copySocialGroup(SocialGroup original) {
        SocialGroup copy = new SocialGroup();
        copy.setExtId(original.getExtId());

        Individual groupHead = new Individual();
        groupHead.setExtId(original.getGroupHead().getExtId());
        copy.setGroupHead(groupHead);
        copy.setGroupName(original.getGroupName());
        return copy;
    }

    public static Visit copyVisit(Visit original) {
        FieldWorker fw = new FieldWorker();
        fw.setExtId(original.getCollectedBy().getExtId());

        Location location = new Location();
        location.setLocationLevel(null);
        location.setExtId(original.getVisitLocation().getExtId());

        Visit copy = new Visit();
        copy.setCollectedBy(fw);
        copy.setVisitLocation(location);
        copy.setExtId(original.getExtId());
        copy.setRoundNumber(original.getRoundNumber());
        copy.setVisitDate(original.getVisitDate());

        return copy;
    }
}
