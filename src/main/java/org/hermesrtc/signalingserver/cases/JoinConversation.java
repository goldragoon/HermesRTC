package org.hermesrtc.signalingserver.cases;

import static org.hermesrtc.signalingserver.exception.Exceptions.CONVERSATION_NOT_FOUND;

import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Signals;
import org.hermesrtc.signalingserver.repository.Conversations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Signals.JOIN_HANDLER)
public class JoinConversation implements SignalHandler {

    @Autowired
    private Conversations conversations;

    public void execute(InternalMessage context) {
        Conversation conversation = findConversationToJoin(context);

        conversation.join(context.getFrom());
    }

    private Conversation findConversationToJoin(InternalMessage message) {
        return conversations.findBy(message.getContent()).orElseThrow(CONVERSATION_NOT_FOUND::exception);
    }

}
