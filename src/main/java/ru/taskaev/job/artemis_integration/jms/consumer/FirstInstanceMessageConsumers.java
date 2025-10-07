package ru.taskaev.job.artemis_integration.jms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
public class FirstInstanceMessageConsumers {

    @JmsListener(destination = "lolTopic", containerFactory = "topicListenerContainerFactory")
    public void listenTopic(Message message) {
        System.out.println("Received from 1 topic: " + message);
    }

    @JmsListener(destination = "lolQueue", containerFactory = "queueListenerContainerFactory")
    public void listenQueue(Message message) {
        System.out.println("Received from 1 queue: " + message);
    }
}
