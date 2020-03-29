package dpozinen.manager.security;

import dpozinen.manager.model.user.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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

	@Value("${enableH2}")
	private Boolean enableH2;

	public SecurityConfig(UserDetailsService service) {
		this.service = service;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service);
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/md/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		(enableH2 ? enableH2(http) : http)
				.authorizeRequests()
				.antMatchers("/user/register", "/user/login").permitAll()
				.antMatchers("/h2/**").hasRole(Role.DEV.toString())
				.antMatchers("/user/all", "/user/worker/save").hasAnyRole(Role.ADMIN.toString(), Role.DEV.toString())
				.antMatchers("/user/*").hasAnyRole(Role.USER.toString(), Role.ADMIN.toString(), Role.DEV.toString())
				.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/user/login")
				.failureForwardUrl("/user/login/fail")
				.permitAll()
			.and()
				.exceptionHandling().accessDeniedPage("/forbidden")
			.and()
				.logout()
				.logoutUrl("/user/logout")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID");
	}

	protected HttpSecurity enableH2(HttpSecurity http) throws Exception {
		return http.csrf().disable()
			   .headers().frameOptions().sameOrigin().and();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
