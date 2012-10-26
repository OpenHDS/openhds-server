package org.openhds.controller.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openhds.controller.service.WhitelistService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Whitelist;

public class WhitelistServiceImpl implements WhitelistService {

	private GenericDao genericDao;
	
	public WhitelistServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}

	public boolean evaluateAddress(HttpServletRequest request) {
		
		List<Whitelist> list = genericDao.findListByProperty(Whitelist.class, "address", request.getRemoteAddr());		
		if (list.size() > 0)
			return true;		
		return false;	
	}
}
