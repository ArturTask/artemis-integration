package ru.taskaev.job.artemis_integration.jms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import ru.taskaev.job.artemis_integration.jms.config.instance.FirstInstanceJMSConfiguration;
import ru.taskaev.job.artemis_integration.jms.config.instance.SecondInstanceConfiguration;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
@Import({FirstInstanceJMSConfiguration.class, SecondInstanceConfiguration.class})
public class JMSConfiguration {
}
