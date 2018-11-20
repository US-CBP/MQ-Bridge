package com.connector.qq;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQTopicConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

@Configuration
public class JMSAltConfig {

    @Value("${servers.mq.host}")
    private String host;
    @Value("${servers.mq.port}")
    private Integer port;
    @Value("${servers.mq.queue-manager}")
    private String queueManager;
    @Value("${servers.mq.channel}")
    private String channel;
    @Value("${servers.mq.queue}")
    private String queue;
    @Value("${servers.mq.topic}")
    private String topic;
    @Value("${servers.mq.timeout}")
    private long timeout;

    @Bean
    public MQTopicConnectionFactory mqTopicConnectionFactory() {
        MQTopicConnectionFactory mqTopicConnectionFactory = new MQTopicConnectionFactory();
        try {
            mqTopicConnectionFactory.setHostName(host);
            mqTopicConnectionFactory.setQueueManager(queueManager);
            mqTopicConnectionFactory.setPort(port);
            mqTopicConnectionFactory.setChannel(channel);
            mqTopicConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            mqTopicConnectionFactory.setCCSID(1208);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mqTopicConnectionFactory;
    }

    @Bean
    @Qualifier("QueueManager")
    public MQQueueConnectionFactory mqQueueConnectionFactory() {
        MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
        try {
            mqQueueConnectionFactory.setHostName(host);
            mqQueueConnectionFactory.setQueueManager(queueManager);
            mqQueueConnectionFactory.setPort(port);
            mqQueueConnectionFactory.setChannel(channel);
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mqQueueConnectionFactory;
    }

    @Bean
    public JmsTemplate queueTemplate(MQQueueConnectionFactory mqQueueConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(mqQueueConnectionFactory);
        jmsTemplate.setReceiveTimeout(timeout);
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate topicTemplate(MQTopicConnectionFactory mqTopiceConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(mqTopiceConnectionFactory);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setReceiveTimeout(timeout);
        return jmsTemplate;
    }


}
