package org.openhds.service.impl;

import org.openhds.service.WhitelistService;
import org.openhds.dao.GenericDao;
import org.openhds.domain.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("whitelistService")
public class WhitelistServiceImpl implements WhitelistService {

	private final static Logger log = LoggerFactory.getLogger(WhitelistServiceImpl.class);
	
	@Autowired
	private GenericDao genericDao;

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
