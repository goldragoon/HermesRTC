package org.hermesrtc.signalingserver.cases;

import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Member;
import org.hermesrtc.signalingserver.domain.Signals;
import org.springframework.stereotype.Component;

import static org.hermesrtc.signalingserver.exception.Exceptions.CONVERSATION_NOT_FOUND;

import java.util.Optional;

@Component(Signals.LEFT_HANDLER)
public class LeftConversation implements SignalHandler {

    public void execute(InternalMessage context) {
        final Member leaving = context.getFrom();
        Conversation conversation = checkPrecondition(leaving.getConversation());

        conversation.left(leaving);
    }

    private Conversation checkPrecondition(Optional<Conversation> conversation) {
        if (!conversation.isPresent()) {
            throw CONVERSATION_NOT_FOUND.exception();
        }
        return conversation.get();
    }

}
