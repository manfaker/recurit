package com.shenzhen.recurit.utils;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AMQPUtils {

   private static final String QUEUENAME = "directQueue";
   private static final  String EXCHANGENAME = "directxchange";
    private static VaribaleUtils varibaleUtils = null;

    static {
        if(EmptyUtils.isEmpty(varibaleUtils)){
            varibaleUtils=SpringUtils.getBean(VaribaleUtils.class);
        }
    }

    public static void producer(String userCode){
        ConnectionFactory connectionFactory=getConnectFactory();
        //2 创建Connection
        String routingKey = userCode;
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            //3 创建Channel
            Channel channel = connection.createChannel();
            channel.queueDeclare(AMQPUtils.QUEUENAME, true, false, false, null);
            //4 声明
            channel.queueBind(AMQPUtils.QUEUENAME,AMQPUtils.EXCHANGENAME,routingKey);
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.deliveryMode(2);
            builder.expiration("6000");
            AMQP.BasicProperties  properties = builder.build();
            //5 发送
            String msg = "{}";
            channel.basicPublish(AMQPUtils.EXCHANGENAME, routingKey , properties , msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println("消息已经推送");
    }


    private static ConnectionFactory getConnectFactory(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(varibaleUtils.getRabbitmqHost());
        connectionFactory.setPort(varibaleUtils.getRabbitmqPort());
        connectionFactory.setUsername(varibaleUtils.getRabbitmqUsername());
        connectionFactory.setPassword(varibaleUtils.getRabbitmqPassword());
        connectionFactory.setVirtualHost(varibaleUtils.getRabbitmqVirtualHost());
        return connectionFactory;
    }
}
