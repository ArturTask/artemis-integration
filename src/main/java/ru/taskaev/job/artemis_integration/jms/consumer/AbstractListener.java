package ru.taskaev.job.artemis_integration.jms.consumer;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.jms.*;

@Slf4j
@Getter
public abstract class AbstractListener implements MessageListener {
    private TopicSession session;
    private TopicSubscriber subscriber;

    public void initSession(TopicConnection connection) throws JMSException {
        ListenerInfo info = getListenerInfo();
        log.info("[tech] [INFO] Starting session topic name = {}", info.getTopicName());
        this.session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(info.getTopicName());
        // вообще  noLocal = true это добалвяет фильтр в очередь внутри топика которая фильтрует чтобы сообщения которые созданы под тем же clientId не были прочитаны в нашем onMessage
        // так что если через консоль Artemis смотреть то false - иначе можно true
        this.subscriber = session.createDurableSubscriber(topic, info.getSubscription(), info.getMessageSelector(), false);
        // this.subscriber = session.createDurableSubscriber(topic, info.getSubscription(), info.getMessageSelector(), true);
        subscriber.setMessageListener(this);
    }


    // each implementation should have its own topicName...
    protected abstract ListenerInfo getListenerInfo();

    @Data
    protected static class ListenerInfo {
        private final String topicName;
        private final String subscription;
        private final String messageSelector;
    }



}
