package dpozinen.manager.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author dpozinen
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService service;

	public SecurityConfig(UserDetailsService service) {
		this.service = service;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/user/all").hasAuthority("ADMIN")
			.antMatchers("/user").hasAnyAuthority("ADMIN", "USER")
			.antMatchers("/user/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
//			.loginPage("/user/login")
//			.loginProcessingUrl("/user/do_login")
//			.defaultSuccessUrl("/homepage", true)
//			.failureUrl("/forbidden")
			.and()
				.exceptionHandling().accessDeniedPage("/user/forbidden")
//////			.failureHandler(authenticationFailureHandler())
//			.and()
//			.logout()
//			.logoutUrl("/perform_logout")
//			.deleteCookies("JSESSIONID");
//			.logoutSuccessHandler();
		;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
