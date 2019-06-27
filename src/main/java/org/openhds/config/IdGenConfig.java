package org.openhds.config;

import java.util.HashMap;

import org.openhds.idgeneration.FieldWorkerGenerator;
import org.openhds.idgeneration.IdScheme;
import org.openhds.idgeneration.IndividualGenerator;
import org.openhds.idgeneration.LocationGenerator;
import org.openhds.idgeneration.LocationHierarchyGenerator;
import org.openhds.idgeneration.SocialGroupGenerator;
import org.openhds.idgeneration.VisitGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.google.common.base.Preconditions;


@Configuration
@PropertySource({"classpath:site-config.properties"})
@ComponentScan(basePackages= {"org.openhds.idgeneration"})
public class IdGenConfig {
	@Autowired
	Environment env;

	@Bean
	public FieldWorkerGenerator fieldWorkerGenerator() {
		FieldWorkerGenerator generator = new FieldWorkerGenerator();
		generator.setGenerated(Preconditions.checkNotNull(Boolean.parseBoolean(env.getProperty("openhds.fwIdUseGenerator"))));
		return generator;
	}
	
	@Bean
	public IndividualGenerator individualGenerator() {
		IndividualGenerator generator = new IndividualGenerator();
		generator.setGenerated(Preconditions.checkNotNull(Boolean.parseBoolean(env.getProperty("openhds.individualIdUseGenerator"))));
		return generator;
	}
	
	@Bean
	public LocationGenerator locationGenerator() {
		LocationGenerator generator = new LocationGenerator();
		generator.setGenerated(Preconditions.checkNotNull(Boolean.parseBoolean(env.getProperty("openhds.locationIdUseGenerator"))));
		return generator;
	}
	

	
	
	@Bean
	public IdScheme locationIdScheme() {
		IdScheme idScheme = new IdScheme();
		idScheme.setName("Location");
		idScheme.setPrefix(Preconditions.checkNotNull(env.getProperty("openhds.locationPrefix")));
		idScheme.setCheckDigit(Preconditions.checkNotNull(Boolean.parseBoolean(env.getProperty("openhds.locationCheckDigit"))));
		idScheme.setIncrementBound(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.locationIncrementBound"))));
		idScheme.setLength(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.locationIdLength"))));

		HashMap<String, Integer> fields = new HashMap<String, Integer>();
		fields.put("LOCATION_NAME", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.locationNameLength"))));
		fields.put("LOCATION_HIERARCHY_ID", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.locationHierarchyIdLength"))));
		idScheme.setFields(fields);

		return idScheme;
	}

	@Bean
	public IdScheme locationHierarchyIdScheme() {
		IdScheme idScheme = new IdScheme();
		idScheme.setName("LocationHierarchy");
		idScheme.setPrefix(Preconditions.checkNotNull(env.getProperty("openhds.locationHPrefix")));
		idScheme.setCheckDigit(Preconditions.checkNotNull(Boolean.parseBoolean(env.getProperty("openhds.locationHCheckDigit"))));
		idScheme.setIncrementBound(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.locationHIncrementBound"))));
		idScheme.setLength(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.locationHIdLength"))));

		HashMap<String, Integer> fields = new HashMap<String, Integer>();
		fields.put("CHILD_LOC_NAME", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.locationHChildLocNameLength"))));
		fields.put("CHILD_LOC_NAME_FIRST", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.locationHChildLocFirstNameLength"))));
		fields.put("CHILD_LOC_NAME_LAST", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.locationHChildLocLastNameLength"))));

		idScheme.setFields(fields);

		return idScheme;
	}

	@Bean
	public IdScheme individualIdScheme() {
		IdScheme idScheme = new IdScheme();
		idScheme.setName("Individual");
		idScheme.setPrefix(Preconditions.checkNotNull(env.getProperty("openhds.individualPrefix")));
		idScheme.setCheckDigit(Preconditions.checkNotNull(Boolean.parseBoolean(env.getProperty("openhds.individualCheckDigit"))));
		idScheme.setIncrementBound(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.individualIncrementBound"))));
		idScheme.setLength(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.individualIdLength"))));

		HashMap<String, Integer> fields = new HashMap<String, Integer>();
		fields.put("LOCATION_PREFIX", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.locationIdLength"))));
		fields.put("INDIVIDUAL_FNAME", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.individualFirstNameLength"))));
		fields.put("INDIVIDUAL_MNAME", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.individualMiddleNameLength"))));
		fields.put("INDIVIDUAL_LNAME", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.individualLastNameLength"))));

		idScheme.setFields(fields);

		return idScheme;
	}

	@Bean
	public IdScheme socialGroupIdScheme() {
		IdScheme idScheme = new IdScheme();
		idScheme.setName("SocialGroup");
		idScheme.setPrefix(Preconditions.checkNotNull(env.getProperty("openhds.sgPrefix")));
		idScheme.setCheckDigit(Preconditions.checkNotNull(Boolean.parseBoolean(env.getProperty("openhds.sgCheckDigit"))));
		idScheme.setIncrementBound(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.sgIncrementBound"))));
		idScheme.setLength(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.sgIdLength"))));

		HashMap<String, Integer> fields = new HashMap<String, Integer>();
		fields.put("SOCIALGROUP_NAME", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.sgIdLength"))));
		idScheme.setFields(fields);

		return idScheme;
	}

	@Bean
	public IdScheme visitIdScheme() {
		IdScheme idScheme = new IdScheme();
		idScheme.setName("Visit");
		idScheme.setPrefix(Preconditions.checkNotNull(env.getProperty("openhds.visitPrefix")));
		idScheme.setCheckDigit(Preconditions.checkNotNull(Boolean.parseBoolean(env.getProperty("openhds.visitCheckDigit"))));
		idScheme.setIncrementBound(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.visitIncrementBound"))));
		idScheme.setLength(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.visitIdLength"))));

		HashMap<String, Integer> fields = new HashMap<String, Integer>();
		fields.put("VISIT_LOCID", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.visitLocationIdLength"))));
		fields.put("VISIT_ROUND", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.visitRound"))));
		idScheme.setFields(fields);

		return idScheme;
	}

	@Bean
	public IdScheme fieldWorkerIdScheme() {
		IdScheme idScheme = new IdScheme();
		idScheme.setName("FieldWorker");
		idScheme.setPrefix(Preconditions.checkNotNull(env.getProperty("openhds.fwPrefix")));
		idScheme.setCheckDigit(Preconditions.checkNotNull(Boolean.parseBoolean(env.getProperty("openhds.fwCheckDigit"))));
		idScheme.setIncrementBound(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.fwIncrementBound"))));
		idScheme.setLength(Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.fwIdLength"))));

		HashMap<String, Integer> fields = new HashMap<String, Integer>();
		fields.put("FIELDWORKER_FNAME", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.fwFirstNameLength"))));
		fields.put("FIELDWORKER_LNAME", Preconditions.checkNotNull(Integer.parseInt(env.getProperty("openhds.fwLastNameLength"))));
		idScheme.setFields(fields);

		return idScheme;
	}
}
