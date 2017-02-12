package org.hermesrtc.signalingserver.cases;

import static org.hermesrtc.signalingserver.exception.Exceptions.INVALID_RECIPIENT;

import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Member;

public abstract class Exchange implements SignalHandler {

    @Override
    public final void execute(InternalMessage message) {
        Conversation conversation = checkPrecondition(message.getFrom());
        exchange(message, conversation);
    }

    protected abstract void exchange(InternalMessage message, Conversation conversation);

    private Conversation checkPrecondition(Member from) {
        if (!from.getConversation().isPresent()) {
            throw INVALID_RECIPIENT.exception();
        }
        return from.getConversation().get();
    }
}
