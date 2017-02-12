package org.hermesrtc.videochat;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hermesrtc.signalingserver.api.HermesRTCEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/developer")
public class DeveloperController {
	private static final Logger log = Logger.getLogger(HermesRTCEndpoint.class);
	
	@Autowired
	private DeveloperRepository developerRepository;
 
	@RequestMapping(value="/mypage", method=RequestMethod.GET)
    public String renderMyPage(HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
    public String renderLoginDeveloper(HttpSession session) {		
        return "loginDeveloper";
    }
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginDeveloper(HttpSession session) {
        return "loginDeveloper";
    }
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
    public String renderLogoutDeveloper(HttpSession session) {
        return "loginDeveloper";
    }
	
    @RequestMapping(value="/create", method=RequestMethod.GET)
    public String renderCreateDeveloper(HttpSession session) {
        return "createDeveloper";
    }
    
    @RequestMapping(value="/create", method=RequestMethod.POST)
    @ResponseBody
    public Developer createDeveloper (@RequestBody Developer developer, HttpSession session)  {
    	Developer d = developerRepository.save(developer);
        return d;
    }
    
    @RequestMapping(value="/read/{developer_id}", method=RequestMethod.POST)
    @ResponseBody
    public Developer readDeveloper (@PathVariable("developer_id") String developer_id, HttpSession session)  {
        return developerRepository.findOne(Long.parseLong(developer_id));
    }
    
}
