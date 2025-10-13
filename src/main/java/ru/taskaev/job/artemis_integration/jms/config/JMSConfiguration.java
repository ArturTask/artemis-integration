
package ru.taskaev.job.artemis_integration.jms.config;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
@EnableScheduling
@EnableConfigurationProperties(value = {ArtemisProperties.class})
public class JMSConfiguration {

    /* == transactions == */
    @Bean
    public JmsTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }

    /* == jmTemplate - producer == */
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }

    /**
     * ConnectionFactory - фабрика для connection-ов к Artmeis
     * <p>
     * Настройки failover описаны в документации:
     * <a href="https://activemq.apache.org/components/artemis/documentation/latest/client-failover.html#reconnection-and-failover-attributes">documentation</a>
     */
    @Primary
    @Bean("artemisConnectionFactory")
    public ConnectionFactory artemisConnectionFactory(ArtemisProperties properties) {
        final String connectionString = "(" + properties.getBrokerUrl() + ")?" +
                "ha=true&" +                            // High Availability Cluster, если кластер ActiveMQ Artemis состоит из нескольких реплик
                "useTopologyForLoadBalancing=false&" +  // Получение топологии кластера из настроек брокера - false, т.к. все реплики указаны в URL
                "failoverAttempts=-1&" +                // Количество попыток переключений между репликами в случае аварийного отключения, "-1" - бесконечно
                "reconnectAttempts=3&" +                // Количество попыток переподключений к отлючившейся реплике, прежде чем переключиться на резервную
                "retryInterval=300&" +                  // Время ожидания между переключениями
                "retryIntervalMultiplier=1.3&" +        // Множитель время ожидания между переключениями
                "maxRetryInterval=2000&" +              // Максимальное время ожидания между переключениями
                "sslEnabled=true";                      // Подключение осуществляется по TLS

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(connectionString);
        connectionFactory.setUser(properties.getUser());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setClientID(properties.getClientId());

        return connectionFactory;
    }

    /* == Настройка для @JmsListener (каждому своя, если нужно по-разному настроить несколько @JmsListener методов) == */
    @Bean("artemisJmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory, JmsTransactionManager transactionManager) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setTransactionManager(transactionManager);
//        factory.setConcurrency("1-1");
        factory.setSessionTransacted(true);
//        factory.setMessageConverter(customMessageConverter());
        return factory;
    }

}