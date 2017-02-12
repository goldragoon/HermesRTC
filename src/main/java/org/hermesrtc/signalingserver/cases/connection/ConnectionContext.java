package org.hermesrtc.signalingserver.cases.connection;

import org.hermesrtc.signalingserver.Names;
import org.hermesrtc.signalingserver.api.HermesRTCEventBus;
import org.hermesrtc.signalingserver.api.HermesRTCEvents;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Member;
import org.hermesrtc.signalingserver.domain.Signal;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.BiPredicate;

@Component
@Scope("prototype")
public class ConnectionContext {

    @Value(Names.MAX_CONNECTION_SETUP_TIME)
    private int maxConnectionSetupTime;

    private ConnectionState state = ConnectionState.NOT_INITIALIZED;
    private DateTime lastUpdated = DateTime.now();

    @Autowired
    @Qualifier(Names.EVENT_BUS)
    private HermesRTCEventBus bus;

    private Member master;
    private Member slave;
    private BiPredicate<Member, Member> filter;


    public ConnectionContext(Member master, Member slave, BiPredicate<Member, Member> filter) {
        this.master = master;
        this.slave = slave;
        this.filter = filter;
    }


    public void process(InternalMessage message) {
        if (is(message, ConnectionState.OFFER_REQUESTED)) {
            answerRequest(message);
            setState(ConnectionState.ANSWER_REQUESTED);
        } else if (is(message, ConnectionState.ANSWER_REQUESTED)) {
            finalize(message);
            setState(ConnectionState.EXCHANGE_CANDIDATES);
        } else if (is(message, ConnectionState.EXCHANGE_CANDIDATES)) {
            if (filter.test(master, message.getFrom())) {
                exchangeCandidates(message);
                setState(ConnectionState.EXCHANGE_CANDIDATES);
            }
        }
    }


    private void exchangeCandidates(InternalMessage message) {
        message.copy().signal(Signal.CANDIDATE).build().send();
    }


    private void finalize(InternalMessage message) {
        message.copy()//
                .from(slave)//
                .to(master)//
                .signal(Signal.FINALIZE)//
                .build()//
                .send();
        bus.post(HermesRTCEvents.MEDIA_LOCAL_STREAM_CREATED.occurFor(slave.getSession()));
        bus.post(HermesRTCEvents.MEDIA_STREAMING.occurFor(master.getSession()));
        bus.post(HermesRTCEvents.MEDIA_STREAMING.occurFor(slave.getSession()));
    }


    private void answerRequest(InternalMessage message) {
        bus.post(HermesRTCEvents.MEDIA_LOCAL_STREAM_CREATED.occurFor(master.getSession()));
        message.copy()//
                .from(master)//
                .to(slave)//
                .signal(Signal.ANSWER_REQUEST)//
                .addCustom("modify", master.getConversation().get().isMemberModifiable(master) + "")
                .addCustom("delegated", master.getConversation().get().getMaster().equals(master) + "")
                .build()//
                .send();
        bus.post(HermesRTCEvents.MEDIA_LOCAL_STREAM_REQUESTED.occurFor(slave.getSession()));
    }

    private boolean is(InternalMessage message, ConnectionState state) {
        return state.equals(this.state) && state.isValid(message);
    }

    public void begin() {
        InternalMessage.create()//
                .from(slave)//
                .to(master)//
                .signal(Signal.OFFER_REQUEST)
                .build()//
                .send();
        setState(ConnectionState.OFFER_REQUESTED);
        bus.post(HermesRTCEvents.MEDIA_LOCAL_STREAM_REQUESTED.occurFor(master.getSession()));
    }

    private void setState(ConnectionState state) {
        this.state = state;
        lastUpdated = DateTime.now();
    }

    public boolean isCurrent() {
        return lastUpdated.plusSeconds(maxConnectionSetupTime).isAfter(DateTime.now());
    }

    public Member getMaster() {
        return master;
    }

    public Member getSlave() {
        return slave;
    }

    public ConnectionState getState() {
        return state;
    }
}
