package org.hermesrtc.videochat;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.hermesrtc.signalingserver.api.HermesRTCEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class RTCSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DeveloperRepository developerRepository;
	private static final Logger log = Logger.getLogger(RTCSecurityConfig.class);
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		.csrf()
	    	.disable()
		.authorizeRequests()
			.antMatchers("/", "/developer/login", "/developer/create" ,"/signaling", "/img/*", "/signal/read/*").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/developer/login")
			.defaultSuccessUrl("/key/read/")
			.and()
		.logout()
			.logoutUrl("/developer/logout")
			.logoutSuccessUrl("/developer/login")
			.invalidateHttpSession(true)
			.permitAll();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceBean());
	}
	
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return new UserDetailsService() {
		      @Override
		      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		        Developer developer = developerRepository.findById(username);
		        if(developer != null) {
		        	log.debug("develoepr exists!");
		        	return new User(developer.getId(), developer.getPw(), true, true, true, true,
		        					AuthorityUtils.createAuthorityList("USER"));
		        } else {
		        	throw new UsernameNotFoundException("could not find the user '" + username + "'");
		        }
		      }
		      
		    };
		    /*
		return new DeveloperDetailsService(developerRepository);
		*/
	}
/*
	private CsrfTokenRepository csrfTokenRepository() 
	{ 
	    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
	    repository.setSessionAttributeName("_csrf");
	    return repository; 
	}
	*/
}
