package com.maxiaochen.mq.rabbitmq.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
        Channel channel = connection.createChannel();
        // 4、通过channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "hello MQ fanout" + i;
            //如果exchange为空串，那么默认的交换机会去找routingKey相同名称的queue，找不到消息就会delete掉了
            channel.basicPublish("test_fanout", "user", null, msg.getBytes());
        }
        // 5、关闭相关的连接
        channel.close();
        connection.close();
    }
}
