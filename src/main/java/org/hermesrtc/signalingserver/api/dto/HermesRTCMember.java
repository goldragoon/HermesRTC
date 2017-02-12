package org.hermesrtc.signalingserver.api.dto;

import javax.websocket.Session;

public interface HermesRTCMember {
    default String getId() {
        return getSession().getId();
    }

    Session getSession();
}
