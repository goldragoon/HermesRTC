package org.hermesrtc.signalingserver.cases;

import org.apache.log4j.Logger;
import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Member;
import org.hermesrtc.signalingserver.domain.Server;
import org.hermesrtc.signalingserver.domain.Signals;
import org.springframework.stereotype.Component;

import static org.hermesrtc.signalingserver.exception.Exceptions.CONVERSATION_NOT_FOUND;

import java.util.Optional;

@Component(Signals.CUSTOM_SIGNAL_HANDLER)
public class CustomSignalHandler implements SignalHandler {
    private static final Logger log = Logger.getLogger(CustomSignalHandler.class);
    
    public void execute(InternalMessage context) {
        Conversation conversation = checkPrecondition(context.getFrom().getConversation());
        
        if(context.getTo() == null)
        {
        	conversation.broadcast(context.getFrom(), context);	
        }
        else
        {
        	context.send();
        }
        
        log.info("To?" + context.getTo());
        log.info(context.getSignal().name());
    }

    private Conversation checkPrecondition(Optional<Conversation> conversation) {
        if (!conversation.isPresent()) {
            throw CONVERSATION_NOT_FOUND.exception();
        }
        return conversation.get();
    }

}
