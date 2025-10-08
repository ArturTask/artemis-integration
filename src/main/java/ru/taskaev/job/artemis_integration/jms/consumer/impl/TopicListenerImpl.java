package ru.taskaev.job.artemis_integration.jms.consumer.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.taskaev.job.artemis_integration.jms.consumer.AbstractListener;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
//@Slf4j
public class TopicListenerImpl extends AbstractListener {

    @Value("${app.topicName}")
    private String topicName;

    @Value("${app.subscription}")
    private String subscription;


    @Override
    protected ListenerInfo getListenerInfo() {
        return new ListenerInfo(topicName, subscription, null);
    }

    @Override
    public void onMessage(Message message) {
//        log.info("Message from topic: {}", message);
        System.out.println(message);
        try {
            message.acknowledge();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
