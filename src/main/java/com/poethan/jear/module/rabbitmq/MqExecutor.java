package com.poethan.jear.module.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.function.Function;

abstract public class MqExecutor {
    private RabbitMqConnectionFactory rabbitMqConnectionFactory;
    public MqExecutor(RabbitMqConnectionFactory rabbitMqConnectionFactory) {
        this.rabbitMqConnectionFactory = rabbitMqConnectionFactory;
    }

    public void addReceiver(String exchangeName, String routingKey, String queueName, Function<String, Boolean> callback) {
        Receiver receiver = new Receiver();
        receiver.setExchangeName(exchangeName);
        receiver.setExchangeType(BuiltinExchangeType.DIRECT);
        receiver.setQueueName(queueName);
        try {
            final Channel channel = rabbitMqConnectionFactory.getChannel();
            receiver.setCallback(new DefaultConsumer(channel){
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException
                {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    long deliveryTag = envelope.getDeliveryTag();
                    // (process the message components here ...)
                    callback.apply(new String(body));
                    channel.basicAck(deliveryTag, false);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        receiver.setRoutingKey(routingKey);
        this.rabbitMqConnectionFactory.addConsumer(receiver);
    }

    protected void init() {
        try {
            this.rabbitMqConnectionFactory.initConsumers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
