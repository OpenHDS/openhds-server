package org.openhds.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openhds.service.WhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor that will check whether the incoming request host IP address is
 * allowed with the {@link WhitelistService}
 */
@Component
public class WhitelistAuthenticationInteceptor extends HandlerInterceptorAdapter {

	private WhitelistService whitelistService;

	@Autowired
	public WhitelistAuthenticationInteceptor(WhitelistService whitelistService) {
		this.whitelistService = whitelistService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// allowing GET requests so that the tablet app can download data
	    // TODO: possibly incorporate a way the tablet can identify itself so it is more secure
	    if (request.getMethod().equalsIgnoreCase("GET")) {
		    return true;
		}
		
	    boolean hasAccess = whitelistService.isHostIpAddressWhitelisted(request.getRemoteAddr());
		if (!hasAccess) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}
		
		return true;
	}
}
