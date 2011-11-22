package org.openhds.web.security;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openhds.dao.service.UserDao;
import org.openhds.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

/**
 * A custom logout success handler that clears saved session information for a user.
 */
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	private static final String LOGOUT_PAGE = "/logout.faces";
	private final UserDao userDao;

	public LogoutSuccessHandler(UserDao userDao) {
		this.userDao = userDao;
		setDefaultTargetUrl(LOGOUT_PAGE);
	}

	@Transactional
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
		List<User> users = userDao.findByUsername(user.getUsername());
		User u = users.get(0);
		
		u.setSessionId("");
		u.setLastLoginTime(0);
		userDao.saveOrUpdate(u);
		
		super.onLogoutSuccess(request, response, authentication);
	}
}
