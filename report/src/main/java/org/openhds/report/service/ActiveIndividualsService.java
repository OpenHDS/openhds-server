package org.openhds.report.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public interface ActiveIndividualsService {

	ModelAndView getActiveIndividuals(HttpServletRequest request, HttpServletResponse response);
}
