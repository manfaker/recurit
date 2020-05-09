package com.shenzhen.recurit.controller.test;

import com.rabbitmq.client.*;

import java.io.IOException;

class RabbitMqMessageConsumer extends DefaultConsumer {


    public RabbitMqMessageConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag,
                                    Envelope envelope,
                                    AMQP.BasicProperties properties,
                                    byte[] body) throws IOException {
        System.err.println("-----------consume message----------");
        System.err.println("consumerTag: " + consumerTag);
        System.err.println("envelope: " + envelope);
        System.err.println("properties: " + properties);
        System.err.println("body: " + new String(body));

    }
}