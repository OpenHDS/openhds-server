package org.openhds.webservice.resources.json;
import java.io.Serializable;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.openhds.service.UserLookupService;
import org.openhds.domain.User;
import org.openhds.util.JsonShallowCopier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users2")
public class UserResource2 {
	private static final Logger logger = LoggerFactory.getLogger(UserResource2.class);

	private UserLookupService userService;

	@Autowired
	public UserResource2(UserLookupService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{username}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<? extends Serializable> getUsers(@PathVariable String username) {
		User user = this.userService.getUser(username);
		
		if(user == null) {
			return new ResponseEntity<UsernameNotFoundException>(
					new UsernameNotFoundException("user " + username + " was not found"), HttpStatus.NOT_FOUND);
		}
		
		//Hash user password before sending to client
		User copy = JsonShallowCopier.shallowCopyUser(user);
		copy.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(8)));

		return new ResponseEntity<User>(copy, HttpStatus.OK);
	}
}