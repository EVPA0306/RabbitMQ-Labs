package com.eap.rabbitmqlabs;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * Created by pavlenko on 11/18/15.
 */
public class ReceiveLogs {

    private static final String EXCHANGE_NAME = "logs";
    private static final String HOST_NAME = "evgeny-Precision-M70.local";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_NAME);
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        //channel.queueDeclare(QUEUE_NAME_NEW, true, false, false, null);
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,EXCHANGE_NAME,"");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                Date time = new Date();
                System.out.println(time + " [x] Recevied '" + message + "'");
                try {
                    doWork(message);
                } finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
    private static void doWork(String task)  {
        for (char ch: task.toCharArray()) {
            if (ch == '.')
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
        }
    }
}
