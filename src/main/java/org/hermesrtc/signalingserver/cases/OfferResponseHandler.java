package org.hermesrtc.signalingserver.cases;

import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Signals;
import org.springframework.stereotype.Component;

@Component(Signals.OFFER_RESPONSE_HANDLER)
public class OfferResponseHandler extends Exchange {

    @Override
    protected void exchange(InternalMessage message, Conversation conversation) {
        conversation.exchangeSignals(message);
    }
}
