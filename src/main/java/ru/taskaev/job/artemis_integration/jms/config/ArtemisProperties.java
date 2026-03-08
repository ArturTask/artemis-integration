package ru.taskaev.job.artemis_integration.jms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.artemis")
public class ArtemisProperties {
    private String brokerUrl = "tcp://localhost:61616";
    private String user = "artemis";
    private String password = "artemis";

}
