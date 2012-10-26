package org.openhds.controller.service;

import javax.servlet.http.HttpServletRequest;


public interface WhitelistService {

	boolean evaluateAddress(HttpServletRequest request); 
}
