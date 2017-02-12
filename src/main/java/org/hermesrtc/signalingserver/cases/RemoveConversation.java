package org.hermesrtc.signalingserver.cases;

import static org.hermesrtc.signalingserver.exception.Exceptions.CONVERSATION_NOT_FOUND;

import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Signal;
import org.hermesrtc.signalingserver.domain.Signals;
import org.hermesrtc.signalingserver.exception.Exceptions;
import org.hermesrtc.signalingserver.repository.Conversations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Signals.REMOVE_HANDLER)
public class RemoveConversation implements SignalHandler {

    @Autowired
    private Conversations conversations;

    public void execute(InternalMessage context) {
        Conversation conversation = findConversationToRemove(context);
        conversations.remove(context.getContent(), context.getFrom());
        InternalMessage.create()//
        .to(context.getFrom())//
        .content("SUCCESS")//
        .signal(Signal.REMOVE)//
        .build()//
        .send();
    }

    private Conversation findConversationToRemove(InternalMessage message) {
        return conversations.findBy(message.getContent()).orElseThrow(CONVERSATION_NOT_FOUND::exception);
    }

}
