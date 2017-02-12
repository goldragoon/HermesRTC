package org.hermesrtc.signalingserver.cases;

import org.hermesrtc.signalingserver.domain.InternalMessage;

public interface SignalHandler {
    void execute(InternalMessage message);
}
