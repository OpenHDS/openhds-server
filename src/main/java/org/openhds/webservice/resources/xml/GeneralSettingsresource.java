package org.openhds.webservice.resources.xml;

import org.openhds.service.SettingsService;
import org.openhds.domain.GeneralSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/settings")
public class GeneralSettingsresource {

	SettingsService settingsService;
	
	@Autowired
	public GeneralSettingsresource(SettingsService settingsService){
		this.settingsService = settingsService;
	}
	
    @RequestMapping(method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public GeneralSettings getSettings() {
    	GeneralSettings gs = settingsService.getSettings();
    	return gs;
    }
}
