package org.openhds.datageneration.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.SocialGroup;

/**
 * Static utilities methods that are used for Data Generation
 */
public class DataGeneratorUtils {
	
	private final static Random random = new Random();

	public static Calendar getDateIncrementedBy(Calendar dateToIncrement, int daysToIncrementDateBy) {
		Date date = dateToIncrement.getTime();
		date = DateUtils.addDays(date, daysToIncrementDateBy);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		return cal;
	}
	
	public static int calculateDaysBetween(Calendar startDay, Calendar endDay) {
		Date startDayDate = startDay.getTime();
		Date endDayDate = endDay.getTime();
		
		long millisBetweenDays = endDayDate.getTime() - startDayDate.getTime();
		int daysBetween = (int) (millisBetweenDays / DateUtils.MILLIS_PER_DAY);
		
		return daysBetween;
	}
	
	public static Calendar getDateBetween(Calendar startDate, Calendar endDate) {
		int daysBetween = calculateDaysBetween(startDate, endDate);
		int randomDay = getRandomBetween(1, daysBetween);
		Calendar cal = Calendar.getInstance();
		Date newDate = DateUtils.addDays(startDate.getTime(), randomDay);
		cal.setTime(newDate);
		return cal;
	}
	
	public static int getRandomBetween(int min, int max) {
		int range = max - min;
		
		return random.nextInt(range) + min;
	}
	
	public static int calculateTotalEventCount(DataGeneratorUpdateParams params) {
		int total = 0;
		total += params.getNumberOfInternalInMigrations();
		total += params.getNumberOfExternalInMigrations();
		total += params.getNumberOfOutMigrations();
		total += params.getNumberOfDeaths();
		total += params.getNumberOfPregnancyObservations();
		total += params.getNumberOfPregnancyOutcomes();
		
		return total;
	}
	
	public static boolean randomlySelectMaleOrFemale() {
		return (random.nextInt(2) == 0);
	}
	
	public static <T> T randomlySelectItemFromList(List<T> canidateItems) {
		int selected = random.nextInt(canidateItems.size());
		return canidateItems.get(selected);
	}
	
	public static FieldWorker createRandomFieldWorker() {
		FieldWorker fw = new FieldWorker();
		fw.setFirstName(RandomStringUtils.randomAlphabetic(8));
		fw.setLastName(RandomStringUtils.randomAlphabetic(8));
		fw.setExtId(RandomStringUtils.randomAlphabetic(8));
		return fw;
	}
	
	public static Location createRandomLocation( List<LocationHierarchy> canidateParents, List<FieldWorker> canidateFieldWorkers) {
		Location location = new Location();
		location.setExtId(RandomStringUtils.randomAlphabetic(12));
		location.setLocationLevel(DataGeneratorUtils.randomlySelectItemFromList(canidateParents));
		location.setCollectedBy(DataGeneratorUtils.randomlySelectItemFromList(canidateFieldWorkers));
		location.setLocationName(RandomStringUtils.randomAlphabetic(10));
		location.setLocationType("RURAL");
		return location;
	}
	
	public static SocialGroup createSocialGroup(FieldWorker collectedBy, Individual individual) {
		SocialGroup sg = new SocialGroup();
		sg.setCollectedBy(collectedBy);
		sg.setExtId(RandomStringUtils.randomAlphanumeric(5));
		sg.setGroupHead(individual);
		sg.setGroupName(RandomStringUtils.randomAlphabetic(10));
		sg.setGroupType("FAM");
		return sg;
	}

	public static Residency createResidency(Location tagetLocation, FieldWorker collectedBy, 
			Individual individual, Calendar startDate, String startType) {
		Residency residency = new Residency();
		residency.setCollectedBy(collectedBy);
		residency.setIndividual(individual);
		residency.setLocation(tagetLocation);
		residency.setStartDate(startDate);
		residency.setStartType(startType);
		return residency;
	}
	
	public static Membership createMembership(SocialGroup socialGroup, FieldWorker collectedBy, Individual indiv) {
		Membership membership = new Membership();
		membership.setCollectedBy(collectedBy);
		membership.setSocialGroup(socialGroup);
		membership.setIndividual(indiv);
		membership.setbIsToA(getStartTypeFromGender(indiv.getGender()));
		membership.setStartType("ENU");
		membership.setStartDate(Calendar.getInstance());
		membership.setEndType("NA");
		return membership;
	}
	
	public static Calendar getDateInPast(int daysInPast) {
		Calendar cal = Calendar.getInstance();
		Date pastDate = DateUtils.addDays(cal.getTime(), -daysInPast);
		cal.setTime(pastDate);
		
		return cal;
	}

	private static String getStartTypeFromGender(String gender) {
		if (gender.equals("M")) {
			return "SON";
		} else {
			return "DAUGHTER";
		}
	}

	public static Calendar getDateInFuture(int daysInFuture) {
		Calendar cal = Calendar.getInstance();
		Date futureTime = DateUtils.addDays(cal.getTime(), daysInFuture);
		cal.setTime(futureTime);
		
		return cal;
	}
	
	public static Calendar generateRandomDob(int maxAge) {
		Date date = new Date();
		date = DateUtils.addYears(date, getMaxAgeWithRange(maxAge));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}	
	
	public static int getMaxAgeWithRange(int maxAge) {
		int MAX_AGE_RANGE = 20;
		int difference = maxAge - MAX_AGE_RANGE;
		int age = new Random().nextInt(MAX_AGE_RANGE + 1);
		return -(age + difference);
	}
	
	public static int generateRandomIntWithBound(int bound) {
		return random.nextInt(bound);
	}
}
