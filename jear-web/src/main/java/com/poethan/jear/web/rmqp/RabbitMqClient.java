package com.poethan.jear.web.rmqp;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqClient {
    private final RabbitMqConnectionFactory rabbitMqConnectionFactory;

    public RabbitMqClient(RabbitMqConnectionFactory rabbitMqConnectionFactory) {
        this.rabbitMqConnectionFactory = rabbitMqConnectionFactory;
    }

    public void publish(String msg, String exchangeName, String routingKey) throws IOException {
        this.publish(msg, exchangeName, routingKey, null);
    }

    public void publish (String msg, String exchangeName, String routingKey, AMQP.BasicProperties basicProperties) throws IOException {
        Channel channel = rabbitMqConnectionFactory.getChannel();
        channel.basicPublish(exchangeName, routingKey, basicProperties, msg.getBytes());
    }

    public void publish(String msg, String exchangeName, String routingKey, int expire) throws IOException {
        this.publish(msg, exchangeName, routingKey, new AMQP.BasicProperties().builder().expiration(String.valueOf(expire*1000)).build());
    }

    public void consume (String queueName) throws IOException {
    }
}
