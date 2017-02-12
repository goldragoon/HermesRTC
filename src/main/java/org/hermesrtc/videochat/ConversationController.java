package org.hermesrtc.videochat;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

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
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private Conversations conversations;

    @RequestMapping(value="/read/{appKey}", method=RequestMethod.GET)
    public @ResponseBody String readKey (@PathVariable("appKey") String appKey, HttpSession session)  {
    	String result = "";
    	
    	// direct return of ArrayList is not supported on spring?
    	ArrayList<String> conversation_list = conversations.findByAppKey(appKey);
    	if(conversation_list != null) result = new Gson().toJson(conversation_list); 
        return result;
    }
}
