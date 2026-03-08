package ru.taskaev.job.artemis_integration.jms.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.JmsTransactionManager;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JMSConfiguration {


    /**
     * @param connectionFactory - autoconfigured by "spring-boot-starter-artemis" one of these methods:
     *                          <ul>
     *                              <li>method org.springframework.boot.autoconfigure.jms.artemis.ArtemisConnectionFactoryConfiguration.SimpleConnectionFactoryConfiguration.jmsConnectionFactory()</li>
     *                              <li>method org.springframework.boot.autoconfigure.jms.artemis.ArtemisConnectionFactoryConfiguration.SimpleConnectionFactoryConfiguration#cachingJmsConnectionFactory(org.springframework.boot.autoconfigure.jms.JmsProperties)</li>
     *                          </ul>
     * @return transaction manager
     */
    @Bean
    public JmsTransactionManager jmsTransactionManager(@Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }
}
