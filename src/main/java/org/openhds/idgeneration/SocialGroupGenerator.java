package org.openhds.idgeneration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.domain.Location;
import org.openhds.domain.LocationHierarchy;
import org.openhds.domain.SocialGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Brian
 * 
 * The Generator for SocialGroup Ids. It examines the siteProperties and 
 * based on the template provided, creates the id. Refer to the 
 * application-context with the different components involved with
 * the id. 
 */

@Component("socialGroupIdGenerator")
public class SocialGroupGenerator extends Generator<SocialGroup> {
	private Location location=null;
	
	public void setLocation(Location location){
		this.location = location;
	}
	
	@Override
	public String generateId(SocialGroup entityItem) throws ConstraintViolations  {
        StringBuilder sb = new StringBuilder();	
		
		IdScheme scheme = getIdScheme();
		HashMap<String, Integer> fields = scheme.getFields();
		Iterator<String> itr = fields.keySet().iterator();
		
		sb.append(scheme.getPrefix().toUpperCase());
		
		while(itr.hasNext()) {
			String key = itr.next();
			Integer filter = fields.get(key);
			
			if (filter != null && location!=null) {
			
				if (key.equals(IdGeneratedFields.SOCIALGROUP_NAME.toString())) {
					
					if(siteconfigService.getVisitAt().equalsIgnoreCase("location")){
						
						String extId =location.getExtId();
						sb.append(extId).append("00");
					
					/*	if (extId.length() >= filter) {
							
							if (filter > 0 && extId.length() >= filter) 
								sb.append(formatProperString(extId, filter));
							else if (filter == 0 || extId.length() < filter) 
								sb.append(formatProperString(extId, extId.length()));
							else
								throw new ConstraintViolations("An error occurred while attempting to generate " +
										"the id on the field specified as '" + extId + "'");
						}
						else
							throw new ConstraintViolations("Unable to generate the id.");*/
					
					}
					/* If visit at socialgroup level
					 * Check if there are already existing socialgroups associated with this location. 
					 * If there are, get the Id of the latest one and increment by +1. Otherwise, set to locId + 00
					 * */
					else{					
						LocationHierarchy ll = location.getLocationLevel();
						if(ll != null){						
							String sgPrefix = ll.getExtId() + location.getExtId().substring(3, 9);							
							List<SocialGroup> list = null;  
							list = genericDao.findListByPropertyPrefix(SocialGroup.class, "extId", sgPrefix, 0, false);	
							
							if(list.size() > 0){
								try {
									String lastGeneratedId = list.get(list.size()-1).getExtId(); //not correct yet, need to get last entry (sql ordered ASC by col extId)
									int increment = Integer.parseInt(lastGeneratedId.substring(9, 11));
									int nextIncrement = increment + 1;
									String genId = String.format(sgPrefix + "%02d", nextIncrement);
									sb.append(genId);
								}catch (NumberFormatException e) {
									sb.append(sgPrefix + "01");
						        }
							}
							else{
								String extId = location.getExtId();
								sb.append(extId).append("00");
							}
						}
					}
				}		
			}
		}

		extId = sb.toString();
		if (location==null) {
			if (scheme.getIncrementBound() > 0) 
				sb.append(buildNumberWithBound(entityItem, scheme));
			else
				sb.append(buildNumber(SocialGroup.class, sb.toString(), scheme.isCheckDigit()));
		}	
		validateIdLength(sb.toString(), scheme);
		
		return sb.toString();
	}

	@Override
	public String buildNumberWithBound(SocialGroup entityItem, IdScheme scheme) throws ConstraintViolations {

		SocialGroup tempSocialGroup = new SocialGroup();
		
		Integer size = 1;
		String result = "";
		
		// get length of the incrementBound
		Integer incBound = scheme.getIncrementBound();
		int incBoundLength = incBound.toString().length();

		while (tempSocialGroup != null) {
			
			result = "";
			String tempExtId = extId;
						
			while (result.toString().length() < incBoundLength) {
				if (result.toString().length()+ size.toString().length() < incBoundLength)
					result += "0";
				if (result.toString().length() + size.toString().length() == incBoundLength)
					result = result.concat(size.toString());
			}

			if (extId == null)
				tempExtId = entityItem.getExtId().concat(result);
			else
				tempExtId = tempExtId.concat(result);
			
			if (scheme.isCheckDigit()) {
				String resultChar = generateCheckCharacter(tempExtId).toString();
				result = result.concat(resultChar);
				tempExtId = tempExtId.concat(resultChar);
			}
			
			tempSocialGroup = genericDao.findByProperty(SocialGroup.class, "extId", tempExtId);
			size++;
		}
		return result;
	}
	
	public IdScheme getIdScheme() {
		int index = Collections.binarySearch(resource.getIdScheme(), new IdScheme("SocialGroup"));
		return resource.getIdScheme().get(index);
	}
	
	@Override
	@Autowired
	@Value("${openhds.sgIdUseGenerator}")
	public void setGenerated(boolean generated) {
		this.generated = generated;
	}
}
