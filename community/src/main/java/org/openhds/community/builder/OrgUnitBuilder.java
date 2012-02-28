package org.openhds.community.builder;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dhis2.ns.schema.dxf2.MetadataDocument.Metadata;
import org.dhis2.ns.schema.dxf2.OrgUnitStructureDocument.OrgUnitStructure;
import org.dhis2.ns.schema.dxf2.OrganisationUnitDocument.OrganisationUnit;
import org.dhis2.ns.schema.dxf2.OrganisationUnitLevelDocument.OrganisationUnitLevel;
import org.dhis2.ns.schema.dxf2.OrganisationUnitLevelsDocument.OrganisationUnitLevels;
import org.dhis2.ns.schema.dxf2.OrganisationUnitRelationshipDocument.OrganisationUnitRelationship;
import org.dhis2.ns.schema.dxf2.OrganisationUnitRelationshipsDocument.OrganisationUnitRelationships;
import org.dhis2.ns.schema.dxf2.OrganisationUnitsDocument.OrganisationUnits;
import org.openhds.community.beans.OrgUnitBean;

/**
 * Builds the OrgUnitStructure section of the DHIS2 schema.
 * 
 * @author Brian
 */
public class OrgUnitBuilder {
	
	int count = 1;
	Calendar startDate;
	Calendar endDate;
	
	OrganisationUnits orgUnits;
	OrganisationUnitRelationships orgUnitRelationships;
	
	static Map<Integer, String> hierarchyCodes = new HashMap<Integer, String>();
	
	public static Map<Integer, String> getHierarchyCodes() {
		return hierarchyCodes;
	}
	
	public void buildOrgUnit(Metadata metadata, OrgUnitBean orgUnit, List<String> levels, Calendar startDate, Calendar endDate) {
		
		this.startDate = startDate;
		this.endDate = endDate;

		OrgUnitStructure orgUnitStructure = metadata.addNewOrgUnitStructure();
		buildUnitLevels(orgUnitStructure, levels);
		
		orgUnitRelationships = orgUnitStructure.addNewOrganisationUnitRelationships();

		orgUnits = orgUnitStructure.addNewOrganisationUnits();
		OrganisationUnit oUnit = orgUnits.addNewOrganisationUnit();
		
		buildOrgUnits(oUnit, orgUnit);
	}
	
	private void buildOrgUnits(OrganisationUnit oUnit, OrgUnitBean orgUnit) {

		oUnit.setId(count);
		oUnit.setUuid(orgUnit.getId());
		oUnit.setName(orgUnit.getName());
		oUnit.setShortName(oUnit.getName());
		oUnit.setAlternativeName(oUnit.getName());
		oUnit.setCode(orgUnit.getCode());
		oUnit.setComment(orgUnit.getLevel());
		oUnit.setActive(true);
		oUnit.setOpeningDate(startDate);
		oUnit.setClosedDate(endDate);
		oUnit.setGeoCode("");
		oUnit.setFeature("");
		oUnit.setLastUpdated(Calendar.getInstance());

		hierarchyCodes.put(count, orgUnit.getCode());
		
		count++;
				
		for (OrgUnitBean unit : orgUnit.getChildren()) {
			OrganisationUnitRelationship orgUnitRelationship = orgUnitRelationships.addNewOrganisationUnitRelationship();
			orgUnitRelationship.setChild(BigInteger.valueOf(unit.getIndex()));
			orgUnitRelationship.setParent(BigInteger.valueOf(unit.getParent().getIndex()));
			
			OrganisationUnit organisationUnit = orgUnits.addNewOrganisationUnit();
			buildOrgUnits(organisationUnit, unit);
		}
	}
	
	private void buildUnitLevels(OrgUnitStructure orgUnitStructure, List<String> levels) {

		OrganisationUnitLevels orgUnitLevels = orgUnitStructure.addNewOrganisationUnitLevels();
		
		int count = 1;
		for (String level : levels) {
			OrganisationUnitLevel orgUnitLevel = orgUnitLevels.addNewOrganisationUnitLevel();
			orgUnitLevel.setLevel(BigInteger.valueOf(count));
			orgUnitLevel.setName(level);
			orgUnitLevel.setId(count);
			count++;
		}	
	}
}
