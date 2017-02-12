package org.hermesrtc.signalingserver.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import org.hermesrtc.signalingserver.Names;
import org.hermesrtc.signalingserver.api.HermesRTCEvents;
import org.hermesrtc.signalingserver.api.HermesRTCHandler;
import org.hermesrtc.signalingserver.api.annotation.HermesRTCEventListener;
import org.hermesrtc.signalingserver.api.dto.HermesRTCEvent;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

import static org.springframework.core.annotation.AnnotationUtils.getValue;

@Component(Names.EVENT_DISPATCHER)
@Scope("singleton")
@HermesRTCEventListener
public class EventDispatcher {

    @Autowired
    private ApplicationContext context;

    @Subscribe
    @AllowConcurrentEvents
    public void handle(HermesRTCEvent event) {
        getNextRTCEventListeners().stream()
                .filter(listener -> isNextRTCHandler(listener) && supportsCurrentEvent(listener, event))
                .forEach(listener -> {
                    ((HermesRTCHandler) listener).handleEvent(event);
                });
    }

    private boolean isNextRTCHandler(Object listener) {
        return listener instanceof HermesRTCHandler;
    }

    private boolean supportsCurrentEvent(Object listener, HermesRTCEvent event) {
        HermesRTCEvents[] events = getSupportedEvents(listener);
        for (HermesRTCEvents supportedEvent : events) {
            if (isSupporting(event, supportedEvent)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSupporting(HermesRTCEvent msg, HermesRTCEvents supportedEvent) {
        return supportedEvent.equals(msg.type());
    }

    private HermesRTCEvents[] getSupportedEvents(Object listener) {
        try {
            if (AopUtils.isJdkDynamicProxy(listener)) {
                listener = ((Advised) listener).getTargetSource().getTarget();
            }
        } catch (Exception e) {
            return new HermesRTCEvents[0];
        }
        return (HermesRTCEvents[]) getValue(listener.getClass().getAnnotation(HermesRTCEventListener.class));
    }

    private Collection<Object> getNextRTCEventListeners() {
        Map<String, Object> beans = context.getBeansWithAnnotation(HermesRTCEventListener.class);
        beans.remove(Names.EVENT_DISPATCHER);
        return beans.values();
    }
}
