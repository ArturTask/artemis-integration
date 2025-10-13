package ru.taskaev.job.artemis_integration.jms.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeclarativeConsumer {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = "example-queue",  containerFactory = "artemisJmsListenerContainerFactory")
    public void receive(Message message) {
        log.info("Received Message = {}", message);
    }

    @Scheduled(fixedRate = 5_000, initialDelay = 0)
    private void send() {
        log.info("[Scheduler] Sending message...");
        jmsTemplate.send("example-queue", session -> session.createTextMessage("lol"));
    }
}
