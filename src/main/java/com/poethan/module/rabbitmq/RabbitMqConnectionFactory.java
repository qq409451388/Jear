package com.poethan.gear.module.rabbitmq;

import com.poethan.gear.utils.SystemUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class RabbitMqConnectionFactory {
    @Value("${rabbitmq.userName}")
    private String userName;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.virtualHost}")
    private String virtualHost;

    @Value("${rabbitmq.hostName}")
    private String hostName;

    @Value("${rabbitmq.port}")
    private Integer port;

    @Value("${rabbitmq.connectionMaxNum}")
    private Integer connectionMaxNum;

    private static ConnectionFactory connectionFactory;

    private final List<Connection> threadLocal = new ArrayList<>();

    private final List<Receiver> receiverList = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException, TimeoutException {
        this.initConnection();
    }

    private void initConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(hostName);
        factory.setPort(port);
        connectionFactory = factory;
        for (int i = 0; i < connectionMaxNum; i++) {
            threadLocal.add(connectionFactory.newConnection());
        }
    }

    public void addConsumer(Receiver receiver) {
        this.receiverList.add(receiver);
    }

    public void initConsumers() throws IOException{
        Channel channel = getChannel();
        for (Receiver receiver : receiverList) {
            AMQP.Exchange.DeclareOk declareOk = channel.exchangeDeclarePassive(receiver.getExchangeName());
            channel.exchangeDeclare(receiver.getExchangeName(), receiver.getExchangeType());
            channel.queueDeclarePassive(receiver.getQueueName());
            channel.queueBind(receiver.getQueueName(), receiver.getExchangeName(), receiver.getRoutingKey());
            channel.basicConsume(receiver.getQueueName(), receiver.getCallback());
        }
    }

    private Connection getConnection() {
        long hashCode = Math.abs(SystemUtils.getTraceId().hashCode());
        int index = (int) hashCode%threadLocal.size();
        log.info("getConnection From index:{}", index);
        return threadLocal.get(index);
    }

    public Channel getChannel() throws IOException {
        return getConnection().createChannel();
    }

}
