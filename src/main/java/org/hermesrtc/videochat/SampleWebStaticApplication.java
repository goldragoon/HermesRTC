package org.hermesrtc.videochat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@ComponentScan
@Configuration
@EnableAutoConfiguration
@Import(EndpointConfig.class)
public class SampleWebStaticApplication extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(SampleWebStaticApplication.class, args);
        DeveloperRepository developerRepository = context.getBean(DeveloperRepository.class);
        AppKeyRepository keyRepository = context.getBean(AppKeyRepository.class);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SampleWebStaticApplication.class);
	}

	@Bean
	public MyEndpoint myEndpoint() {
		return new MyEndpoint();
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
