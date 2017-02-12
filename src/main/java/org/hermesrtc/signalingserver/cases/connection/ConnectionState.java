package org.hermesrtc.signalingserver.cases.connection;

import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Signal;

public enum ConnectionState {
    OFFER_REQUESTED {
        @Override
        public boolean isValid(InternalMessage message) {
            return Signal.OFFER_RESPONSE.is(message.getSignal());
        }
    },
    ANSWER_REQUESTED {
        @Override
        public boolean isValid(InternalMessage message) {
            return Signal.ANSWER_RESPONSE.is(message.getSignal());
        }
    },
    EXCHANGE_CANDIDATES {
        @Override
        public boolean isValid(InternalMessage message) {
            return Signal.CANDIDATE.is(message.getSignal());
        }
    },
    NOT_INITIALIZED {
        @Override
        public boolean isValid(InternalMessage message) {
            return false;
        }
    };


    ConnectionState() {
    }

    public abstract boolean isValid(InternalMessage message);
}
