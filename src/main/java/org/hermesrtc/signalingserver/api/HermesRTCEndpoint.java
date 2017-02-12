package org.hermesrtc.signalingserver.api;

import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Message;
import org.hermesrtc.signalingserver.domain.Server;
import org.hermesrtc.signalingserver.domain.Signal;
import org.hermesrtc.signalingserver.exception.Exceptions;
import org.hermesrtc.signalingserver.exception.SignalingException;
import org.hermesrtc.videochat.AppKey;
import org.hermesrtc.videochat.AppKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;

import static org.hermesrtc.signalingserver.exception.Exceptions.MEMBER_NOT_FOUND;

import java.util.Set;

@Component
public class HermesRTCEndpoint {

    private static final Logger log = Logger.getLogger(HermesRTCEndpoint.class);
    private Server server;
    
    private static Set<HermesRTCEndpoint> endpoints = Sets.newConcurrentHashSet();

    public HermesRTCEndpoint() {
        endpoints.add(this);
        endpoints.stream().filter(e -> e.server != null).findFirst().ifPresent(s -> this.setServer(s.server));
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        log.info("Opening: " + session.getId());
        server.register(session);
    }

    @OnMessage
    public void onMessage(Message message, Session session) {
    	// Handle all incoming RTC packets from end users 
    	AppKey appKey = server.keyRepository.findByAppKeyValue(message.getAppKey());
    	if(appKey != null)
    	{
            log.info("Handling message from[" + message.getAppKey() +"]: " + session.getId());
            server.handle(message, session);
    	} else
    	{
    		log.info("invalid appKey : " + message.getAppKey());
            InternalMessage.create()//
	            .to(server.members.findBy(session.getId()).get())//
	            .content(Exceptions.APPKEY_NOT_FOUND.getErrorCode())//
	            .signal(Signal.ERROR)//
	            .build()//
	            .send();
    		server.handleError(session, new SignalingException(Exceptions.APPKEY_NOT_FOUND));
    	}

    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.info("Closing: " + session.getId() + " with reason: " + reason.getReasonPhrase());
        server.unregister(session, reason);
    }

    @OnError
    public void onError(Session session, Throwable exception) {
        log.info("Occured exception for session: " + session.getId());
        log.error(exception);
        server.handleError(session, exception);
    }

    @Autowired
    public void setServer(Server server) {
        log.info("Setted server: " + server + " to " + this);
        this.server = server;
    }
}
