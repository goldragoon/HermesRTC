package org.hermesrtc.signalingserver.cases;

import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Signal;
import org.hermesrtc.signalingserver.domain.Signals;
import org.hermesrtc.signalingserver.repository.Conversations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Signals.CREATE_HANDLER)
public class CreateConversation implements SignalHandler {

    @Autowired
    private Conversations conversations;

    public void execute(InternalMessage context) {
        Conversation conversation = conversations.create(context);
        InternalMessage.create()//
        .to(context.getFrom())//
        .signal(Signal.CREATE)//
        .content(conversation.getId())
        .build()//
        .send();
        //conversation.join(context.getFrom());
        //conversation.setMaster(context.getFrom());
    }

}
