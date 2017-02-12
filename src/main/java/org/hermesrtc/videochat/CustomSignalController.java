package org.hermesrtc.videochat;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.repository.Conversations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/signal")
public class CustomSignalController {

	@Autowired
	private CustomSignalRepository customSignalRepository;

	@Autowired
	private AppKeyRepository appKeyRepository;
	
    @RequestMapping(value="/create", method=RequestMethod.POST)
    public @ResponseBody CustomSignal createCustomSignal(@RequestBody Map<String, String> json, HttpSession session) {
    	CustomSignal signal = new CustomSignal();
    	signal.setName(json.get("name"));
    	signal.setValue(json.get("value"));
    	signal.setAppKey(appKeyRepository.findByAppKeyValue(json.get("appKey")));
    	
        return customSignalRepository.save(signal);
    }
    
    @RequestMapping(value="/read/{appKey}", method=RequestMethod.GET)
    public String renderCustomSignal (@PathVariable("appKey") String appKey, HttpSession session)  { 
        return "readCustomSignal";
    }
    
    @RequestMapping(value="/read/{appKey}", method=RequestMethod.POST)
    @ResponseBody
    public List<CustomSignal> readCustomSignal (@PathVariable("appKey") String appKey, HttpSession session)  {
    	List<CustomSignal> listCustomSignal = customSignalRepository.findByAppKeyValue(appKey);
    	return listCustomSignal;
    }
    
    @RequestMapping(value="/delete/{customSignal_id}", method=RequestMethod.POST)
    public @ResponseBody CustomSignal deleteCustomSignal(@PathVariable("customSignal_id") String customSignal_id, HttpSession session) { 	
    	CustomSignal cs = new CustomSignal();
    	cs.setCustomSignal_id(Long.parseLong(customSignal_id));
    	customSignalRepository.delete(cs);
        return cs;
    }
}
