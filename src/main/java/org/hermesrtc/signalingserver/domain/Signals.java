package org.hermesrtc.signalingserver.domain;

public interface Signals {
	String ERROR = "error";
    String EMPTY = "";
    String EMPTY_HANDLER = "nextRTC_EMPTY";
    String CUSTOM_SIGNAL = "customSignal";
    String CUSTOM_SIGNAL_HANDLER = "nextRTC_customSignal";
    String OFFER_REQUEST = "offerRequest";
    String OFFER_RESPONSE = "offerResponse";
    String OFFER_RESPONSE_HANDLER = "nextRTC_OFFER_RESPONSE";
    String ANSWER_REQUEST = "answerRequest";
    String ANSWER_RESPONSE = "answerResponse";
    String ANSWER_RESPONSE_HANDLER = "nextRTC_ANSWER_RESPONSE";
    String FINALIZE = "finalize";
    String CANDIDATE = "candidate";
    String CANDIDATE_HANDLER = "nextRTC_CANDIDATE";
    String PING = "ping";
    String LEFT = "left";
    String LEFT_HANDLER = "nextRTC_LEFT";
    String JOIN = "join";
    String JOIN_HANDLER = "nextRTC_JOIN";
    String REMOVE = "remove";
    String REMOVE_HANDLER = "nextRTC_REMOVE";
    String CREATE = "create";
    String CREATE_HANDLER = "nextRTC_CREATE";
    String CREATED = "created";
    String TEXT = "text";
    String ENMODIFY = "enmodify";
    String ENMODIFY_HANDLER = "nextRTC_ENMODIFY";
    String ENMODIFIED = "enmodified";
    String DEMODIFY = "demodify";
    String DEMODIFY_HANDLER = "nextRTC_DEMODIFY";
    String DEMODIFIED = "demodified";
    String ENVIDEO = "envideo";
    String ENVIDEO_HANDLER = "nextRTC_ENVIDEO";
    String ENVIDEOED = "envideoed";
    String DEVIDEO = "devideo";
    String DEVIDEO_HANDLER = "nextRTC_DEVIDEO";
    String DEVIDEOED = "devideoed";
    String ENSOUND = "ensound";
    String ENSOUND_HANDLER = "nextRTC_ENSOUND";
    String ENSOUNDED = "ensounded";
    String DESOUND = "desound";
    String DESOUND_HANDLER = "nextRTC_DESOUND";
    String DESOUNDED = "desounded";
    String DELEGATE = "delegate";
    String DELEGATE_HANDLER = "nextRTC_DELEGATE";
    String DELEGATED = "delegated";
    String TEXT_HANDLER = "nextRTC_TEXT";
    String NEW_JOINED = "newJoined";
}

