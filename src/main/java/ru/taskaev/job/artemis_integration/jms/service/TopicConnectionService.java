package ru.taskaev.job.artemis_integration.jms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.stereotype.Service;
import ru.taskaev.job.artemis_integration.jms.consumer.AbstractListener;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicConnectionService {

    private final List<AbstractListener> listeners;
    private final ConnectionFactory factory;

    private TopicConnection topicConnection;

    @PostConstruct
    public void myTopicConnectionFactory() throws JMSException {
        ActiveMQConnectionFactory artemisFactory = (ActiveMQConnectionFactory) factory;
        artemisFactory.setClientID("BSB");

        topicConnection = artemisFactory.createTopicConnection();
        topicConnection.start();
        for (AbstractListener l : listeners) {
            l.initSession(topicConnection);
        }
    }

    @PreDestroy
    public void destroy() {
        for (AbstractListener listener : listeners) {
            TopicSubscriber subscriber = listener.getSubscriber();
            TopicSession session = listener.getSession();
            try {
                subscriber.close();
                session.close();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        if (topicConnection != null) {
            try {
                topicConnection.close();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }


}
