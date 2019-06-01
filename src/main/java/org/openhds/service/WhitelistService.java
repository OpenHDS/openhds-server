package org.openhds.service;

public interface WhitelistService {

	boolean isHostIpAddressWhitelisted(String hostIpAddress);
}
