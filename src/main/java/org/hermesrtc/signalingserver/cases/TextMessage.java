package org.hermesrtc.signalingserver.cases;

import static org.hermesrtc.signalingserver.api.HermesRTCEvents.TEXT;

import org.hermesrtc.signalingserver.Names;
import org.hermesrtc.signalingserver.api.HermesRTCEventBus;
import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Member;
import org.hermesrtc.signalingserver.domain.Signals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(Signals.TEXT_HANDLER)
public class TextMessage implements SignalHandler {

    @Autowired
    @Qualifier(Names.EVENT_BUS)
    private HermesRTCEventBus eventBus;

    @Override
    public void execute(InternalMessage message) {
        Member from = message.getFrom();
        if (message.getTo() == null && from.getConversation().isPresent()) {
            Conversation conversation = from.getConversation().get();
            conversation.broadcast(from, message);
            eventBus.post(TEXT.basedOn(message));
        } else if (from.hasSameConversation(message.getTo())) {
            message.send();
            eventBus.post(TEXT.basedOn(message));
        }

    }
}
