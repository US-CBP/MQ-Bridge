/*
 * All Application code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package gov.gtas.JMSConfiguration;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConnectionBean {

    private final Logger logger = LoggerFactory.getLogger(MQConnectionBean.class);

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
    @Value("${servers.mq.username}")
    private String username;
    @Value("${servers.mq.password}")
    private String password;
    @Value("${servers.mq.userAuthMqcsp}")
    private Boolean userAuthMqcsp;

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
            mqQueueConnectionFactory.setStringProperty(WMQConstants.USERID, username);
            mqQueueConnectionFactory.setStringProperty(WMQConstants.PASSWORD, password);
            mqQueueConnectionFactory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, userAuthMqcsp);
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        } catch (Exception e) {
            logger.debug("Critical Error Instantiating QueueManager Bean!", e);
        }
        return mqQueueConnectionFactory;
    }
}
