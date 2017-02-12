package org.hermesrtc.signalingserver.eventbus;

import org.hermesrtc.signalingserver.Names;
import org.hermesrtc.signalingserver.api.HermesRTCEventBus;
import org.hermesrtc.signalingserver.api.annotation.HermesRTCEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("nextRTCEventBusSetup")
@Scope("singleton")
public class EventBusSetup {

    @Autowired
    @Qualifier(Names.EVENT_BUS)
    private HermesRTCEventBus eventBus;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void setupHandlers() {
        context.getBeansWithAnnotation(HermesRTCEventListener.class).values()
                .forEach(eventBus::register);
    }
}
