package ru.taskaev.job.artemis_integration.jms.consumer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
@ConditionalOnProperty(value = "app.enable-two-artemis", havingValue = "true", matchIfMissing = false)
public class SecondInstanceMessageConsumers {

    @JmsListener(destination = "kekTopic", containerFactory = "secondTopicListenerContainerFactory")
    public void secondListenTopic(Message message) {
        System.out.println("Received from 2 topic: " + message);
    }

    @JmsListener(destination = "kekQueue", containerFactory = "secondQueueListenerContainerFactory")
    public void secondListenQueue(Message message) {
        System.out.println("Received from 2 queue: " + message);
    }
}
