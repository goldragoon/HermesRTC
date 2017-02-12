package org.hermesrtc.signalingserver.cases;

import org.hermesrtc.signalingserver.cases.connection.ConnectionContext;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Member;
import org.hermesrtc.signalingserver.domain.RTCConnections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.BiPredicate;

@Component
@Scope("prototype")
public class ExchangeSignalsBetweenMembers {

    @Autowired
    private RTCConnections connections;

    @Autowired
    private ApplicationContext context;

    public synchronized void begin(Member from, Member to, BiPredicate<Member, Member> filter) {
        connections.put(from, to, context.getBean(ConnectionContext.class, from, to, filter));
        connections.get(from, to).ifPresent(ConnectionContext::begin);
    }

    public synchronized void execute(InternalMessage message) {
        connections.get(message.getFrom(), message.getTo()).ifPresent(context -> context.process(message));
    }
}
