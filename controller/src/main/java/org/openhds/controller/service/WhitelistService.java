package org.openhds.controller.service;

public interface WhitelistService {

	boolean isHostIpAddressWhitelisted(String hostIpAddress);
}
