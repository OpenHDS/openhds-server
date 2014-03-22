package org.openhds.controller.service.impl;

import org.openhds.controller.service.WhitelistService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.model.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhitelistServiceImpl implements WhitelistService {

	private final static Logger log = LoggerFactory.getLogger(WhitelistServiceImpl.class);
	
	private GenericDao genericDao;

	public WhitelistServiceImpl(GenericDao genericDao) {
		this.genericDao = genericDao;
	}

	@Override
	public boolean isHostIpAddressWhitelisted(String hostIpAddress) {
		// TODO: enable white listed ip addresses again
//		Whitelist whitelistRecord = genericDao.findByProperty(Whitelist.class, "address", hostIpAddress);
//		if (whitelistRecord == null) {
//			return false;
//		}

		return true;
	}
}
