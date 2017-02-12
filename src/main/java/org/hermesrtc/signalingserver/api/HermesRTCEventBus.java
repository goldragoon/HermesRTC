package org.hermesrtc.signalingserver.api;

import com.google.common.eventbus.EventBus;
import org.apache.log4j.Logger;
import org.hermesrtc.signalingserver.Names;
import org.hermesrtc.signalingserver.api.dto.HermesRTCEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service(Names.EVENT_BUS)
@Scope("singleton")
public class HermesRTCEventBus {

    private static final Logger log = Logger.getLogger(HermesRTCEventBus.class);
    private EventBus eventBus;

    public HermesRTCEventBus() {
        this.eventBus = new EventBus();
    }

    public void post(HermesRTCEvent event) {
        log.info("POSTED EVENT: " + event);
        eventBus.post(event);
    }

    @Deprecated
    public void post(Object o) {
        eventBus.post(o);
    }

    public void register(Object listeners) {
        log.info("REGISTERED LISTENER: " + listeners);
        eventBus.register(listeners);
    }

}
