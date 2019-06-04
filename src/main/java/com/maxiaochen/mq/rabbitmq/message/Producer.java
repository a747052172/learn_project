package com.maxiaochen.mq.rabbitmq.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static void main(String[] args) throws Exception {
        // 1、创建连接工厂 并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.19.30.91");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        ///2、通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();
        ///3、通过connection创建一个Channel
        // header
        Map<String, Object> headers = new HashMap<>();
        headers.put("a", "aaaaa");
        headers.put("b", "bbbbb");
        Channel channel = connection.createChannel();
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000")
                .headers(headers)   //自定义属性
                .build();
        // 4、通过channel发送数据
        for (int i = 0; i <5 ; i++) {
            String msg = "hello MQ" + i;
            //如果exchange为空串，那么默认的交换机会去找routingKey相同名称的queue，找不到消息就会delete掉了
            channel.basicPublish("",  "test001", properties, msg.getBytes());
        }
        // 5、关闭相关的连接
        channel.close();
        connection.close();
    }
}
