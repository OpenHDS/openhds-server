package org.openhds.web.security;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openhds.dao.service.UserDao;
import org.openhds.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

/**
 * A custom authentication success handler that records the session id for a user.
 * The session id saved is used by the special studies to verify a user has successfully
 * logged into openhds
 */
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private static final String WELCOME_PAGE = "/demowelcome.faces";
	private final UserDao userDao;
	
	public AuthenticationSuccessHandler(UserDao userDao) {
		this.userDao = userDao;
		setDefaultTargetUrl(WELCOME_PAGE);
	}

	@Transactional
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User)authentication.getPrincipal();
		List<User> users = userDao.findByUsername(u.getUsername());
		
		String sessId = request.getSession().getId();
		
		User user = users.get(0);
		user.setSessionId(sessId);
		user.setLastLoginTime(System.currentTimeMillis());
		
		userDao.saveOrUpdate(user);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
