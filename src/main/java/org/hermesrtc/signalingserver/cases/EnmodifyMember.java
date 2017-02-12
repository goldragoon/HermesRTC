package org.hermesrtc.signalingserver.cases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hermesrtc.signalingserver.exception.Exceptions.CONVERSATION_NOT_FOUND;

import org.apache.log4j.Logger;
import org.hermesrtc.signalingserver.api.HermesRTCEndpoint;
import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Signals;
import org.hermesrtc.signalingserver.exception.Exceptions;
import org.hermesrtc.signalingserver.repository.Conversations;

@Component(Signals.ENMODIFY_HANDLER)
public class EnmodifyMember implements SignalHandler {

	private static final Logger log = Logger.getLogger(HermesRTCEndpoint.class);
	
    @Autowired
    private Conversations conversations;

    private Conversation findConversationToEnmodify(InternalMessage message) {
        return conversations.findBy(message.getContent()).orElseThrow(CONVERSATION_NOT_FOUND::exception);
    }

	@Override
	public void execute(InternalMessage context) {
		// TODO Auto-generated method stub
		Conversation conversation = findConversationToEnmodify(context);
		log.info(context.getFrom().getId() + " -> "+ context.getTo().getId());
        conversation.grantModification(context.getFrom(), context.getTo());
        //System.out.println("Delegate Message Accepted");
	}

}
