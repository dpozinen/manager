package dpozinen.manager.service;

import dpozinen.manager.model.user.User;
import dpozinen.manager.security.DefaultUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author dpozinen
 */
@Service
public class DefaultUserDetailsService implements UserDetailsService {

	private final UserService service;

	public DefaultUserDetailsService(UserService service) {
		this.service = service;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = service.getByUsername(username);
		if (user == null) throw new UsernameNotFoundException(username);
		return new DefaultUserDetails(user);
	}
}
