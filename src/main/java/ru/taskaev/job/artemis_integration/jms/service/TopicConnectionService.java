package ru.taskaev.job.artemis_integration.jms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.stereotype.Service;
import ru.taskaev.job.artemis_integration.jms.consumer.AbstractListener;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.TopicConnection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicConnectionService {

    private final List<AbstractListener> listeners;
    private final ConnectionFactory factory;

    private Connection connection;

    @PostConstruct
    public void myTopicConnectionFactory() throws JMSException {
        ActiveMQConnectionFactory artemisFactory = (ActiveMQConnectionFactory) factory;
        artemisFactory.setUser()

        TopicConnection topicConnection = artemisFactory.createTopicConnection();

    }


}
