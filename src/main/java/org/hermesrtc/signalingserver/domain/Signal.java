package org.hermesrtc.signalingserver.domain;

import org.hermesrtc.signalingserver.domain.Signals;

public enum Signal {
	ERROR(Signals.ERROR),
    EMPTY(Signals.EMPTY),
    CUSTOM_SIGNAL(Signals.CUSTOM_SIGNAL, Signals.CUSTOM_SIGNAL_HANDLER),
    OFFER_REQUEST(Signals.OFFER_REQUEST),
    OFFER_RESPONSE(Signals.OFFER_RESPONSE, Signals.OFFER_RESPONSE_HANDLER),
    ANSWER_REQUEST(Signals.ANSWER_REQUEST),
    ANSWER_RESPONSE(Signals.ANSWER_RESPONSE, Signals.ANSWER_RESPONSE_HANDLER),
    FINALIZE(Signals.FINALIZE),
    CANDIDATE(Signals.CANDIDATE, Signals.CANDIDATE_HANDLER),
    PING(Signals.PING),
    LEFT(Signals.LEFT, Signals.LEFT_HANDLER),
    JOIN(Signals.JOIN, Signals.JOIN_HANDLER),
    CREATE(Signals.CREATE, Signals.CREATE_HANDLER),
    DELEGATE(Signals.DELEGATE, Signals.DELEGATE_HANDLER),
    DELEGATED(Signals.DELEGATED),
    REMOVE(Signals.REMOVE, Signals.REMOVE_HANDLER),
    ENMODIFY(Signals.ENMODIFY, Signals.ENMODIFY_HANDLER),
    ENMODIFIED(Signals.ENMODIFIED),
    DEMODIFY(Signals.DEMODIFY, Signals.DEMODIFY_HANDLER),
    DEMODIFIED(Signals.DEMODIFIED),
    ENVIDEO(Signals.ENVIDEO, Signals.ENVIDEO_HANDLER),
    ENVIDEOED(Signals.ENVIDEOED),
    DEVIDEO(Signals.DEVIDEO, Signals.DEVIDEO_HANDLER),
    DEVIDEOED(Signals.DEVIDEOED),
    ENSOUND(Signals.ENSOUND, Signals.ENSOUND_HANDLER),
    ENSOUNDED(Signals.ENSOUNDED),
    DESOUND(Signals.DESOUND, Signals.DESOUND_HANDLER),
    DESOUNDED(Signals.DESOUNDED),
    NEW_JOINED(Signals.NEW_JOINED),
    CREATED(Signals.CREATED),
    TEXT(Signals.TEXT, Signals.TEXT_HANDLER);

    private String signalName;
    private String signalHandler;

    Signal(String signalName) {
        this.signalName = signalName;
        this.signalHandler = Signals.EMPTY_HANDLER;
    }

    Signal(String signalName, String signalHandler) {
        this.signalName = signalName;
        this.signalHandler = signalHandler;
    }

    public boolean is(String string) {
        return ordinaryName().equalsIgnoreCase(string);
    }

    public boolean is(Signal signal) {
        return this.equals(signal);
    }

    public String ordinaryName() {
        return signalName;
    }

    public String handlerName() {
        return signalHandler;
    }
}
