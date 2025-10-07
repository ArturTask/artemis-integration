package ru.taskaev.job.artemis_integration.jms.consumer;

import lombok.extern.slf4j.Slf4j;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractListener implements MessageListener {

    private static final Map<Object, Object> subscriberAndSessionMap = new HashMap<>();

    private String topicName;

    public void initSession(Connection connection) {
      log.info("starting session topic name = {}", topicName);
      connection.cre
    }

}
