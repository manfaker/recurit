package com.shenzhen.recurit.controller.test;

import com.rabbitmq.client.*;
import com.shenzhen.recurit.application.ProduceApplication;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProduceApplication.class)
public class SpringBootTestDemo {

    @Autowired
    StringEncryptor stringEncryptor;

    @Test
    public void myTest(){
        System.out.println(stringEncryptor.encrypt("22222"));
        System.out.println(stringEncryptor.decrypt("I0oA+y/kMRgB+XFU35BQGQ=="));
    }

    @Test
    public void consumer(){
        ConnectionFactory connectionFactory=getConnectFactory();
        //2 创建Connection
        String queueName = "directQueue";
        String exchangeName = "direct.exchange";
        String routingKey = "test.direct";
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            //3 创建Channel
            Channel channel = connection.createChannel();
            //4 声明
            channel.queueDeclare(queueName, true, false, false, null);
            //5 绑定
            channel.queueBind(queueName,exchangeName,routingKey);
            channel.basicConsume(queueName, true, new RabbitMqMessageConsumer(channel));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void producer(){
        ConnectionFactory connectionFactory=getConnectFactory();
        //2 创建Connection
        String queueName = "directQueue";
        String exchangeName = "directxchange";
        String routingKey = "testDirect";
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            //3 创建Channel
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);
            //4 声明
            channel.queueBind(queueName,exchangeName,routingKey);
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.deliveryMode(2);
            builder.expiration("6000");
            AMQP.BasicProperties  properties = builder.build();
            //5 发送
            String msg = "{}";
            channel.basicPublish(exchangeName, routingKey , properties , msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println("消息已经推送");
    }



    private ConnectionFactory getConnectFactory(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("134.175.126.154");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("faker");
        connectionFactory.setPassword("faker");
        connectionFactory.setVirtualHost("recurit");
        return connectionFactory;
    }




}


