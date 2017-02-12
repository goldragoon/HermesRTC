package org.hermesrtc.signalingserver.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.hermesrtc.signalingserver.api.HermesRTCEvents;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE})
public @interface HermesRTCEventListener {

    HermesRTCEvents[] value() default {};
}
