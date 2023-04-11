package com.poethan.jear.module.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Consumer;
import javafx.util.Callback;
import lombok.Data;

import java.io.Serializable;

@Data
public class Receiver implements Serializable {
    private String exchangeName;
    private BuiltinExchangeType exchangeType;
    private String queueName;
    private String routingKey;
    //private Callback callback;
    private Consumer callback;
}
