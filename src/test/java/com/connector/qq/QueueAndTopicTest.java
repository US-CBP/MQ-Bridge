package com.connector.qq;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueueAndTopicTest {

    @Autowired
    private JmsTemplate queueTemplate;
    @Autowired
    private JmsTemplate topicTemplate;
    @Autowired
    private JMSAltConfig altConfig;

    private String queue = "ONE.REQ";
    private String topic = "ONE/TOPIC";


    @Test
    public void testQQ(){
        JmsTemplate jmsTemp = altConfig.queueTemplate(new MQQueueConnectionFactory());
    }

}
