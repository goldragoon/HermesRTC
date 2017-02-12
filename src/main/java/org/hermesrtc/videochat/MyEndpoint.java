package org.hermesrtc.videochat;

import javax.websocket.server.ServerEndpoint;

import org.hermesrtc.signalingserver.api.HermesRTCEndpoint;
import org.hermesrtc.signalingserver.codec.MessageDecoder;
import org.hermesrtc.signalingserver.codec.MessageEncoder;

@ServerEndpoint(value = "/signaling",//
decoders = MessageDecoder.class,//
encoders = MessageEncoder.class)
public class MyEndpoint extends HermesRTCEndpoint {
}
