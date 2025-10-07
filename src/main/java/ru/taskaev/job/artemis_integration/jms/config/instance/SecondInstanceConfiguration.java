package ru.taskaev.job.artemis_integration.jms.config.instance;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

@Configuration
@Slf4j
@ConditionalOnProperty(value = "app.enable-two-artemis", havingValue = "true", matchIfMissing = false)
public class SecondInstanceConfiguration {


    @Bean("secondConnectionFactory")
    public ConnectionFactory secondCreateConnectionFactory() {
        // 1️⃣ Artemis фабрика (НЕ IBM)
        ActiveMQConnectionFactory factory =
                new ActiveMQConnectionFactory("tcp://localhost:61617", "admin", "admin");

        // 2️⃣ Заворачиваем в CachingConnectionFactory, чтобы спрингу было удобно
        CachingConnectionFactory cachingFactory = new CachingConnectionFactory(factory);
        cachingFactory.setSessionCacheSize(10);
        return cachingFactory;
    }


    // это настройка для JMSListener (topic)
    @Bean("secondTopicListenerContainerFactory")
    public JmsListenerContainerFactory<?> secondTopicListenerFactory(@Qualifier("secondConnectionFactory") ConnectionFactory myConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(myConnectionFactory);
        factory.setPubSubDomain(true); // это важно! true => топики, false => очереди
        return factory;
    }

    // это настройка для JMSListener (queue)
    @Bean("secondQueueListenerContainerFactory")
    public JmsListenerContainerFactory<?> secondQueueListenerFactory(@Qualifier("secondConnectionFactory") ConnectionFactory myConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(myConnectionFactory);
        factory.setPubSubDomain(false); // false => очереди
        return factory;
    }

    @PostConstruct
    public void init() {
        log.info("[Tech] [INFO] second Artemis instance (SecondInstanceMessageConsumers) are active");
    }

//    // transactions:
//    @Bean("secondJmsTransactionManager")
//    public PlatformTransactionManager jmsTransactionManager(@Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory) {
//        return new JmsTransactionManager(connectionFactory);
//    }
//
//    @Bean
//    public DefaultJmsListenerContainerFactory transactionalListenerContainerFactory(
//            @Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory,
//            @Qualifier("secondJmsTransactionManager") PlatformTransactionManager jmsTransactionManager) {
//
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//
//        // включаем транзакции JMS
//        factory.setSessionTransacted(true);
//        factory.setTransactionManager(jmsTransactionManager);
//        factory.setErrorHandler(throwable -> System.out.println("[ERROR] " + throwable.getMessage()));
//        // ручной ack
//        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
//
//        // concurrency
//        factory.setConcurrency("1-5");
//
//        return factory;
//    }

}
