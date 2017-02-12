package org.hermesrtc.signalingserver.exception;

public enum Exceptions {
    MEMBER_NOT_FOUND("0001"), //
    INVALID_RECIPIENT("0002"), //
    APPKEY_NOT_FOUND("0003"), //
    SIGNAL_NOT_FOUND("0004"), //
    CONVERSATION_NOT_FOUND("0005"), //
    
    INVALID_CONVERSATION_NAME("0101"), //
    CONVERSATION_NAME_OCCUPIED("0102"), //
    
    UNAUTHORIZED_ACCESS("0401"),
    UNKNOWN_ERROR("0501"),;

    private String code;
    private String message;

    Exceptions(String code) {
        this.code = code;
    }

    public String getErrorCode() {
        return code;
    }

    public SignalingException exception() {
        return new SignalingException(this);
    }

    public SignalingException exception(String customMesage) {
        return new SignalingException(this, customMesage);
    }

    public SignalingException exception(Exception reason) {
        return new SignalingException(this, reason);
    }

}
