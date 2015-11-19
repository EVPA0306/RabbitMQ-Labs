package com.eap.rabbitmqlabs;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by pavlenko on 11/16/15.
 */
public class NewTask {
    private final static String QUEUE_NAME = "hello";
    private final static String QUEUE_NAME_NEW = "task_queue";

    public static void main( String[] args ) throws IOException, TimeoutException
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("evgeny-Precision-M70.local");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME_NEW,true,false,false,null);

        String message = getMessage(args);


        channel.basicPublish("",QUEUE_NAME_NEW, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        System.out.println( " [x] Sent '" + message +"'");
        channel.close();
        connection.close();
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 1)
            return "Hello World!";
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0)
            return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i=1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
