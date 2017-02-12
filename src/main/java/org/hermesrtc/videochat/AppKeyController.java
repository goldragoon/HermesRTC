package org.hermesrtc.videochat;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/key")
public class AppKeyController {
	private static final Logger LOGGER = Logger.getLogger(HermesRTCEndpoint.class);
	
	@Autowired
	private AppKeyRepository keyRepository;
	
	@Autowired
	private DeveloperRepository developerRepository;
	
    @RequestMapping(value="/create", method=RequestMethod.GET)
    public String renderCreateKey() {
        return "createKey";
    }
    
    @RequestMapping(value="/create", method=RequestMethod.POST)
    public @ResponseBody AppKey createKey(@RequestBody Map<String, String> json, HttpSession session) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	AppKey key = new AppKey();
    	key.setDeveloper(developerRepository.findById(auth.getName()));
    	key.setName(json.get("name"));
    	key.setValue(new KeyGenerator().nextSessionId());
    	AppKey k = keyRepository.save(key);
        return k;
    }
	
    @RequestMapping(value="/read", method=RequestMethod.GET)
    public String renderCustomSignal (HttpSession session)  {
    	
        return "readKey";
    }
    
    @RequestMapping(value="/read", method=RequestMethod.POST)
    @ResponseBody
    public List<AppKey> readKey (HttpSession session)  {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	return keyRepository.findByDeveloperUsername(auth.getName());
    }
    
    @RequestMapping(value="/delete/{appKey}", method=RequestMethod.POST)
    public @ResponseBody AppKey deleteKey(@PathVariable("appKey") String appKey, HttpSession session) { 	
    	AppKey key = new AppKey();
    	key.setAppKey_id(Long.parseLong(appKey));
    	keyRepository.delete(key);
        return key;
    }
	
}
