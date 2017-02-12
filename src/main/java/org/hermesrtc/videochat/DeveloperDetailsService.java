package org.hermesrtc.videochat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hermesrtc.signalingserver.api.HermesRTCEndpoint;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DeveloperDetailsService implements UserDetailsService {

	private DeveloperRepository developerRepository;
	private static final Logger LOGGER = Logger.getLogger(HermesRTCEndpoint.class);
	
	DeveloperDetailsService(DeveloperRepository developerRepository)
	{
		this.developerRepository = developerRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.debug("requested" + username + " developer");
        try {  
            Developer developer = developerRepository.findById(username);
            if (developer == null) {  
                LOGGER.debug("developer not found with the provided id");  
                return null;  
            }  
            LOGGER.debug(" developer from id " + developer.toString());  
            List<GrantedAuthority> gas = new ArrayList<GrantedAuthority>();
            gas.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new org.springframework.security.core.userdetails.User(developer.getId(), developer.getPw(), true, true, true, true, gas);  
        }  
        catch (Exception e){  
            throw new UsernameNotFoundException("User not found");  
        }  
	}
	
    private Set<GrantedAuthority> getAuthorities(Developer developer){  
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();  
        for(Role role : developer.getRoles()) {  
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());  
            authorities.add(grantedAuthority);  
        }    
        return authorities;  
    }  
  
}
