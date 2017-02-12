package org.hermesrtc.signalingserver.api.dto;

import org.hermesrtc.signalingserver.api.HermesRTCEvents;
import org.hermesrtc.signalingserver.exception.SignalingException;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.Optional;

public interface HermesRTCEvent {

    HermesRTCEvents type();

    DateTime published();

    Optional<HermesRTCMember> from();

    Optional<HermesRTCMember> to();

    Optional<HermesRTCConversation> conversation();

    Optional<SignalingException> exception();

    Map<String, String> custom();

    Optional<String> content();

    Optional<String> reason();

}
