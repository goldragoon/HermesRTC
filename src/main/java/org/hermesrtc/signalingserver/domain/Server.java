package org.hermesrtc.signalingserver.domain;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.hermesrtc.signalingserver.cases.RegisterMember;
import org.hermesrtc.signalingserver.cases.SignalHandler;
import org.hermesrtc.signalingserver.domain.InternalMessage.InternalMessageBuilder;
import org.hermesrtc.signalingserver.exception.Exceptions;
import org.hermesrtc.signalingserver.exception.SignalingException;
import org.hermesrtc.signalingserver.repository.Members;
import org.hermesrtc.videochat.AppKeyRepository;
import org.hermesrtc.videochat.CustomSignal;
import org.hermesrtc.videochat.CustomSignalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hermesrtc.signalingserver.exception.Exceptions.MEMBER_NOT_FOUND;

import javax.websocket.CloseReason;
import javax.websocket.Session;

@Component
public class Server {

    private static final Logger log = Logger.getLogger(Server.class);
    
	@Autowired
	public AppKeyRepository keyRepository;
	
	@Autowired
	public CustomSignalRepository customSignalRepository;
	
    @Autowired
    public Members members;

    @Autowired
    private SignalResolver resolver;

    @Autowired
    private RegisterMember register;

    public void register(Session session) {
        register.incoming(session);
    }

    public void handle(Message external, Session session) {
        Pair<Signal, SignalHandler> resolve = resolver.resolve(external.getSignal());
        
        // check if signal is pre-defined by system.
        if (resolve.getKey().is(Signal.EMPTY)) {
        	InternalMessage internalMessage = buildInternalMessage(external, Signal.CUSTOM_SIGNAL, session);
        	CustomSignal custom = customSignalRepository.findByAppKeyValueAndSignalValue(external.getAppKey(), external.getSignal());
        	if(custom != null)
        	{
        		internalMessage.getCustom().put("value", external.getSignal());
        		processMessage(resolver.resolve(Signals.CUSTOM_SIGNAL.toString()).getValue(), internalMessage);
        	}
        	else
        	{
        		InternalMessage.create()//
	            .to(members.findBy(session.getId()).get())//
	            .content(Exceptions.SIGNAL_NOT_FOUND.getErrorCode())//
	            .signal(Signal.ERROR)//
	            .build()//
	            .send();
        	}
        }
        else
        {
        	InternalMessage internalMessage = buildInternalMessage(external, resolve.getKey(), session);
        	processMessage(resolve.getValue(), internalMessage);
        }
    }

    private void processMessage(SignalHandler handler, InternalMessage message) {
        log.info("Incoming: " + message);
        if (handler != null) {
            handler.execute(message);
        }
    }

    private InternalMessage buildInternalMessage(Message message, Signal signal, Session session) {
        InternalMessageBuilder bld = InternalMessage.create()//
                .from(findMember(session))//
                .content(message.getContent())//
                .signal(signal)//
                .custom(message.getCustom());
        members.findBy(message.getTo()).ifPresent(bld::to);
        return bld.build();
    }

    private Member findMember(Session session) {
        return members.findBy(session.getId()).orElseThrow(() -> new SignalingException(MEMBER_NOT_FOUND));
    }

    public void unregister(Session session, CloseReason reason) {
        members.unregisterBy(session, reason.getReasonPhrase());
    }


    public void handleError(Session session, Throwable exception) {
        members.dropOutAfterException(session, exception.getMessage());
    }

}
