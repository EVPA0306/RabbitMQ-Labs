package com.eap.rabbitmqlabs;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by pavlenko on 11/18/15.
 */
public class EmitLog
{
    private static final String EXCHANGE_NAME = "logs";
    private static final String HOST_NAME = "evgeny-Precision-M70.local";

    public static void main( String[] args ) throws IOException, TimeoutException
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_NAME);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //channel.queueDeclare(QUEUE_NAME_NEW,true,false,false,null);

        String message = getMessage(args);


        channel.basicPublish(EXCHANGE_NAME,"", null,message.getBytes());
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
