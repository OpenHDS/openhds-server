package org.openhds.community.builder;

import java.util.List;
import org.dhis2.ns.schema.dxf2.ChildrenDocument.Children;
import org.dhis2.ns.schema.dxf2.MetadataDocument.Metadata;
import org.dhis2.ns.schema.dxf2.OrgUnitStructureDocument.OrgUnitStructure;
import org.dhis2.ns.schema.dxf2.OrganisationUnitDocument.OrganisationUnit;
import org.dhis2.ns.schema.dxf2.OrganisationUnitLevelDocument.OrganisationUnitLevel;
import org.dhis2.ns.schema.dxf2.OrganisationUnitLevelsDocument.OrganisationUnitLevels;
import org.dhis2.ns.schema.dxf2.OrganisationUnitsDocument.OrganisationUnits;
import org.openhds.community.beans.OrgUnit;

/**
 * Builds the OrgUnitStructure section of the DHIS2 schema.
 * 
 * @author Brian
 */
public class OrgUnitBuilder {
	
	public void buildOrgUnit(Metadata metadata, OrgUnit orgUnit, List<String> levels) {
		
		OrgUnitStructure orgUnitStructure = metadata.addNewOrgUnitStructure();
		buildUnitLevels(orgUnitStructure, levels);
		
		OrganisationUnits orgUnits = orgUnitStructure.addNewOrganisationUnits();
		OrganisationUnit oUnit = orgUnits.addNewOrganisationUnit();
		buildOrgUnits(oUnit, orgUnit);
	}
	
	private void buildOrgUnits(OrganisationUnit oUnit, OrgUnit orgUnit) {
				
		oUnit.setUuid(orgUnit.getId());
		oUnit.setName(orgUnit.getName());
		oUnit.setAlternativeName(orgUnit.getName());
		oUnit.setShortName(oUnit.getName());
		oUnit.setCode(orgUnit.getCode());
		oUnit.setComment(orgUnit.getLevel());
		
		for (OrgUnit unit : orgUnit.getChildren()) {
			Children children = oUnit.addNewChildren();
			OrganisationUnit childUnit = children.addNewOrganisationUnit();
			
			buildOrgUnits(childUnit, unit);
		}
	}
	
	private void buildUnitLevels(OrgUnitStructure orgUnitStructure, List<String> levels) {

		OrganisationUnitLevels orgUnitLevels = orgUnitStructure.addNewOrganisationUnitLevels();
		
		int count = 0;
		for (String level : levels) {
			
			OrganisationUnitLevel orgUnitLevel = orgUnitLevels.addNewOrganisationUnitLevel();
			orgUnitLevel.setName(level);
			orgUnitLevel.setId(count);
			count++;
		}	
	}
}
