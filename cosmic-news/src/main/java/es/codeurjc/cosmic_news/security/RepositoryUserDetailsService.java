package es.codeurjc.cosmic_news.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.service.UserService;



@Service
public class RepositoryUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		
		List<GrantedAuthority> roles = new ArrayList<>();
		User user = userService.findUserByMail(mail);
    	if (user != null) {
			for (String role : user.getRoles()) {
				roles.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
        	return new org.springframework.security.core.userdetails.User(user.getMail(),user.getPass(),roles);
    	}

		else 		throw new UsernameNotFoundException("User not found");

	}
}
