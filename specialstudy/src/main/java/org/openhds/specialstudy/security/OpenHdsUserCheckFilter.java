package org.openhds.specialstudy.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.openhds.specialstudy.dao.OpenHdsUserDao;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * This is a custom filter that is inserted into the Spring Security filter chain.
 * The intent of the filter is to automatically login a user if they present a session id
 * that is valid for OpenHDS. This is done by looking up the session id in the OpenHDS 
 * database
 */
public class OpenHdsUserCheckFilter extends AbstractAuthenticationProcessingFilter {
	
	private static Logger logger = Logger.getLogger(OpenHdsUserCheckFilter.class);

	private static final String URL_TO_APPLY_FILTER_TO = "/specialStudyLogin";
	private static final String LOGIN_PAGE = "/login";

	private static final String OPENHDS_SESSION_ID_PARAM = "openhdsSessionId";
	private static final String TARGET_URL_PARAM = "targetUrl";
	
	private static final String ADMIN_USER = "admin";
	private static final String ADMIN_PASSWORD = "test";
	
	private final OpenHdsUserDao dao;
	private final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();

	protected OpenHdsUserCheckFilter(OpenHdsUserDao dao) {
		super(URL_TO_APPLY_FILTER_TO);
		setAuthenticationSuccessHandler(successHandler);
		setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(LOGIN_PAGE));
		this.dao = dao;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		String openhdsSessionId = request.getParameter(OPENHDS_SESSION_ID_PARAM);
		String targetUrl = request.getParameter(TARGET_URL_PARAM);
		
		if (!targetUrl.startsWith("/")) {
			targetUrl = "/" + targetUrl;
		}
		
		logger.debug("Target url is: " + targetUrl);
		
		successHandler.setDefaultTargetUrl(targetUrl);
		
		String user = "";
		String password = "";
		
		if (openhdsSessionId != null && dao.isSessionIdValidForOpenHds(openhdsSessionId)) {
			user = ADMIN_USER;
			password = ADMIN_PASSWORD;
			logger.debug("Found a session for OpenHDS, sessionId: " + openhdsSessionId);
		} 
		
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user, password);
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

		return this.getAuthenticationManager().authenticate(authRequest);
	}
}
