package com.example.mqtt.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
@IntegrationComponentScan
public class MqttConfig {
    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String[] hostUrl;

    @Value("${spring.mqtt.client.id}")
    private String clientId;

    @Value("${spring.mqtt.default.topic.push}")
    private String defaultTopicPush;

    @Value("${spring.mqtt.default.topic.pull}")
    private String[] defaultTopicPull;

    @Value("${spring.mqtt.completionTimeout}")
    private int completionTimeout;   //连接超时

    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(hostUrl);
        //最大并发数
//        mqttConnectOptions.setMaxInflight(50);
        //心跳检查
        mqttConnectOptions.setKeepAliveInterval(60);
        mqttConnectOptions.setConnectionTimeout(completionTimeout);
        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(defaultTopicPush);
        messageHandler.setDefaultRetained(false);
        /**
         * 0,1,2
         * 其中0表示的是订阅者没收到消息不会再次发送,消息会丢失,
         * 1表示的是会尝试重试,一直到接收到消息,但这种情况可能导致订阅者收到多次重复消息,
         * 2相比多了一次去重的动作,确保订阅者收到的消息有一次
         * qos=0性能是最好的,2是最差的
         */
        messageHandler.setDefaultQos(2);
        return messageHandler;
    }


    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        clientId + "_inbound", mqttClientFactory(), defaultTopicPull);
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        //消息消费
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                final Object mqtt_receivedTopic = message.getHeaders().get("mqtt_receivedTopic");
                if (mqtt_receivedTopic != null) {
                    String topic = mqtt_receivedTopic.toString();
                    String type = topic.substring(topic.lastIndexOf("/") + 1);
                    System.out.println("topic:" + topic + ",type:" + type + ",message:" + message.getPayload().toString());
                }
            }
        };
    }
}
