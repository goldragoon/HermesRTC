package org.hermesrtc.signalingserver.api;

import org.hermesrtc.signalingserver.api.dto.HermesRTCEvent;

public interface HermesRTCHandler {

    void handleEvent(HermesRTCEvent event);

}
