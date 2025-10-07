package ru.taskaev.job.artemis_integration.jms.config.instance;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;

@Configuration
@Slf4j
public class FirstInstanceJMSConfiguration {

    @Bean("firstConnectionFactory")
    public ConnectionFactory createConnectionFactory() {
        // 1️⃣ Artemis фабрика (НЕ IBM)
        ActiveMQConnectionFactory factory =
                new ActiveMQConnectionFactory("tcp://localhost:61616", "admin", "admin");

        // 2️⃣ Заворачиваем в CachingConnectionFactory, чтобы спрингу было удобно
        CachingConnectionFactory cachingFactory = new CachingConnectionFactory(factory);
        cachingFactory.setSessionCacheSize(10);
        return cachingFactory;
    }

    // это настройка для JMSListener (topic)
    @Bean("topicListenerContainerFactory")
    public JmsListenerContainerFactory<?> topicListenerFactory(@Qualifier("firstConnectionFactory") ConnectionFactory myConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(myConnectionFactory);
        factory.setPubSubDomain(true); // это важно! true => топики, false => очереди
        return factory;
    }

    // это настройка для JMSListener (queue)
    @Bean("queueListenerContainerFactory")
    public JmsListenerContainerFactory<?> queueListenerFactory(@Qualifier("firstConnectionFactory") ConnectionFactory myConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(myConnectionFactory);
        factory.setPubSubDomain(false); // false => очереди
        return factory;
    }

//    // transactions:
//    @Bean("firstJmsTransactionManager")
//    public PlatformTransactionManager jmsTransactionManager(@Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory) {
//        return new JmsTransactionManager(connectionFactory);
//    }
//
//
//    @Bean
//    public DefaultJmsListenerContainerFactory transactionalListenerContainerFactory(
//            @Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory,
//            @Qualifier("firstJmsTransactionManager") PlatformTransactionManager jmsTransactionManager) {
//
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//
//        // включаем транзакции JMS
//        factory.setSessionTransacted(true);
//        factory.setTransactionManager(jmsTransactionManager);
//        factory.setErrorHandler(throwable -> System.out.println("[ERROR] " + throwable.getMessage()));
//
//        // concurrency
//        factory.setConcurrency("1-5");
//
//        return factory;
//    }

}
