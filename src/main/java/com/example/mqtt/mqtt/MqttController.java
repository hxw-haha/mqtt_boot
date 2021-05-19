package com.example.mqtt.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/mqttTest")
public class MqttController {

    private static final String[] topics = {"00001", "00002", "00003"};
    private static final ExecutorService executor = Executors.newFixedThreadPool(50);

    private MqttGateway mqttGateway;

    @Autowired
    public void setMqttGateway(MqttGateway mqttGateway) {
        this.mqttGateway = mqttGateway;
    }

    @RequestMapping("/sendMqtt")
    public String sendMqtt(final String sendData) {
        final int size = new Random().nextInt(topics.length);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= size; i++) {
                    final String sendTopic = topics[i];
                    System.out.println("size:" + size + "...sendTopic：" + sendTopic);
                    mqttGateway.sendToMqtt(sendData, sendTopic);
                }
            }
        });
        return "发送成功：" + sendData;
    }
}
