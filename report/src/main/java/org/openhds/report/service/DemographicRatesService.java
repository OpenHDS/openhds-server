package org.openhds.report.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface DemographicRatesService {
	
	ModelAndView getPopulationRates(HttpServletRequest request); 
}
