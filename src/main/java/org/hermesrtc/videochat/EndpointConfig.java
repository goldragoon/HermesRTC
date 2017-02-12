package org.hermesrtc.videochat;

import org.hermesrtc.signalingserver.HermesRTCConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;

import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Import(HermesRTCConfig.class)
public class EndpointConfig {
}