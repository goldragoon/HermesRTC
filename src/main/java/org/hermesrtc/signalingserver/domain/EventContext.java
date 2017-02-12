package org.hermesrtc.signalingserver.domain;

import com.google.common.collect.Maps;

import org.hermesrtc.signalingserver.api.HermesRTCEvents;
import org.hermesrtc.signalingserver.api.dto.HermesRTCConversation;
import org.hermesrtc.signalingserver.api.dto.HermesRTCEvent;
import org.hermesrtc.signalingserver.api.dto.HermesRTCMember;
import org.hermesrtc.signalingserver.exception.SignalingException;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

public class EventContext implements HermesRTCEvent {

    private final HermesRTCEvents type;
    private final DateTime published = DateTime.now();
    private final Map<String, String> custom = Maps.newHashMap();
    private final Optional<HermesRTCMember> from;
    private final Optional<HermesRTCMember> to;
    private final Optional<HermesRTCConversation> conversation;
    private final Optional<SignalingException> exception;
    private final String reason;
    private final String content;

    private EventContext(HermesRTCEvents type, HermesRTCMember from, HermesRTCMember to, HermesRTCConversation conversation, SignalingException exception, String reason, String content) {
        this.type = type;
        this.from = ofNullable(from);
        this.to = ofNullable(to);
        this.conversation = ofNullable(conversation);
        this.exception = ofNullable(exception);
        this.reason = reason;
        this.content = content;
    }

    @Override
    public HermesRTCEvents type() {
        return type;
    }

    @Override
    public DateTime published() {
        return published;
    }

    @Override
    public Optional<HermesRTCMember> from() {
        return from;
    }

    @Override
    public Optional<HermesRTCMember> to() {
        return to;
    }

    @Override
    public Optional<HermesRTCConversation> conversation() {
        return conversation;
    }

    @Override
    public Optional<SignalingException> exception() {
        return exception;
    }

    @Override
    public Map<String, String> custom() {
        return unmodifiableMap(custom);
    }

    @Override
    public Optional<String> reason() {
        return Optional.ofNullable(reason);
    }

    @Override
    public Optional<String> content() {
        return Optional.ofNullable(content);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) <- %s -> (%s)", type, from, conversation, to);
    }

    public static EventContextBuilder builder() {
        return new EventContextBuilder();
    }

    public static class EventContextBuilder {
        private Map<String, String> custom;
        private HermesRTCEvents type;
        private HermesRTCMember from;
        private HermesRTCMember to;
        private HermesRTCConversation conversation;
        private SignalingException exception;
        private String reason;
        private String content;

        public EventContextBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public EventContextBuilder content(String content) {
            this.content = content;
            return this;
        }

        public EventContextBuilder type(HermesRTCEvents type) {
            this.type = type;
            return this;
        }

        public EventContextBuilder custom(Map<String, String> custom) {
            this.custom = custom;
            return this;
        }

        public EventContextBuilder from(HermesRTCMember from) {
            this.from = from;
            return this;
        }

        public EventContextBuilder to(HermesRTCMember to) {
            this.to = to;
            return this;
        }

        public EventContextBuilder conversation(HermesRTCConversation conversation) {
            this.conversation = conversation;
            return this;
        }

        public EventContextBuilder exception(SignalingException exception) {
            this.exception = exception;
            return this;
        }

        public HermesRTCEvent build() {
            if (type == null) {
                throw new IllegalArgumentException("Type is required");
            }
            EventContext eventContext = new EventContext(type, from, to, conversation, exception, reason, content);
            if (custom != null) {
                eventContext.custom.putAll(custom);
            }
            return eventContext;
        }
    }
}
