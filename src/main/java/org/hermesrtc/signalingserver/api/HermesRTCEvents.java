package org.hermesrtc.signalingserver.api;

import javax.websocket.Session;

import org.hermesrtc.signalingserver.api.dto.HermesRTCEvent;
import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.EventContext;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.exception.Exceptions;

public enum HermesRTCEvents {
    SESSION_OPENED,
    SESSION_CLOSED,
    CONVERSATION_CREATED,
    CONVERSATION_DESTROYED,
    UNEXPECTED_SITUATION,
    MEMBER_JOINED,
    MEMBER_LEFT,
    MEDIA_LOCAL_STREAM_REQUESTED,
    MEDIA_LOCAL_STREAM_CREATED,
    MEDIA_STREAMING,
    TEXT;

    public HermesRTCEvent basedOn(InternalMessage message, Conversation conversation) {
        return EventContext.builder()
                .from(message.getFrom())
                .to(message.getTo())
                .custom(message.getCustom())
                .conversation(conversation)
                .type(this)
                .build();
    }

    public HermesRTCEvent basedOn(EventContext.EventContextBuilder builder) {
        return builder
                .type(this)
                .build();
    }

    public HermesRTCEvent occurFor(Session session, String reason) {
        return EventContext.builder()
                .from(() -> session)
                .type(this)
                .reason(reason)
                .build();
    }

    public HermesRTCEvent occurFor(Session session) {
        return EventContext.builder()
                .type(this)
                .from(() -> session)
                .exception(Exceptions.UNKNOWN_ERROR.exception())
                .build();
    }

    public HermesRTCEvent basedOn(InternalMessage message) {
        return EventContext.builder()
                .from(message.getFrom())
                .to(message.getTo())
                .custom(message.getCustom())
                .content(message.getContent())
                .type(this)
                .build();
    }
}
