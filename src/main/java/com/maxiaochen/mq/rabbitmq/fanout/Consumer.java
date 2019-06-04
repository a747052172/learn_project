package com.maxiaochen.mq.rabbitmq.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws Exception {
        try {
            // 1、创建连接工厂 并进行配置
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("172.19.30.91");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");
            ///2、通过连接工厂创建连接
            Connection connection = connectionFactory.newConnection();
            ///3、通过connection创建一个Channel
            Channel channel = connection.createChannel();
            // 4、声明（创建）一个队列
            // 可以声明交换机
            String exchangeName = "test_fanout";
            channel.exchangeDeclare(exchangeName, "fanout");
//        channel.exchangeDeclare()
            // queue队列名  是否持久化（mq停后还存在硬盘）  是否独占模式  自动删除没有bind到exchange的queue 扩展参数
            String queueName = "test004";
            channel.queueDeclare(queueName, false, false, false, null);
            channel.queueBind(queueName, exchangeName,"user");
            // 5、创建消费者
            QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
            // 6、设置channel
            // 名称  是否自动签收(告诉服务器已经收到) 消费者对象
            channel.basicConsume(queueName, true, queueingConsumer);
            // 7、获取消息
            while (true) {
                QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                String msg = new String(delivery.getBody());
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ShutdownSignalException e) {
            e.printStackTrace();
        } catch (ConsumerCancelledException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
