package org.openhds.controller.export;

import java.util.ArrayList;
import java.util.List;
import org.openhds.community.beans.OrgUnit;
import org.openhds.community.service.DHISService;
import org.openhds.controller.service.LocationHierarchyService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.LocationHierarchy;
import org.openhds.domain.model.LocationHierarchyLevel;

/**
 * References:
 * 
 * DHIS2 Reference Guide:
 * http://dhis2.org/doc/snapshot/en/user/html/
 * 
 * @author Brian
 */

public class DHISController {
	
	GenericDao genericDao;
	LocationHierarchyService locationService;
	DHISService dhisService;
	
	public DHISController(GenericDao genericDao, LocationHierarchyService locationService, DHISService dhisService) {
		this.genericDao = genericDao;
		this.locationService = locationService;
		this.dhisService = dhisService;
	}
	
	public String buildDHISDocument() throws ClassNotFoundException {
		
		dhisService.createDxfDocument();
		buildOrgUnit();
		
		return dhisService.getDxfDocument().toString();
	}
	
	private void buildOrgUnit() {
		
		LocationHierarchy root = locationService.getHierarchyItemHighestLevel();

		String id = root.getUuid();
		String level = root.getLevel().getName();
		String code = root.getExtId();
		String name = root.getName();
		
		OrgUnit orgUnit = new OrgUnit(id, name, code, level);
		buildOrgUnitStructure(root, orgUnit);	
		
		List<String> levels = new ArrayList<String>();
		List<LocationHierarchyLevel> locHLevels = locationService.getAllLevels();
		for (int i = locHLevels.size()-1; i >= 0; i--) {
			levels.add(locHLevels.get(i).getName());
		}
		
		dhisService.createOrgUnit(orgUnit, levels);
	}
	
	/**
	 * A structure is needed in order to build the DHIS OrgUnit element. Since the OpenHDS
	 * location hierarchy only has parent references and the org unit has children references,
	 * it must be converted over.
	 */
    private void buildOrgUnitStructure(LocationHierarchy item, OrgUnit unit) {
    	
    	// find children of this locationHierarchy item
    	List<LocationHierarchy> hierarchyList = genericDao.findListByProperty(LocationHierarchy.class, "parent", item);
    	
    	for (LocationHierarchy hierarchyItem : hierarchyList) {
    	
	    	// build OrgUnit
	    	String id = hierarchyItem.getUuid();
			String level = hierarchyItem.getLevel().getName();
			String code = hierarchyItem.getExtId();
			String name = hierarchyItem.getName();
			
			OrgUnit orgUnit = new OrgUnit(id, name, code, level);
			unit.getChildren().add(orgUnit);
			
			buildOrgUnitStructure(hierarchyItem, orgUnit);
    	}
   }
  
}
