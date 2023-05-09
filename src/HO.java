

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class HO {
    //Définir sa queue
    public final static String QUEUE_NAME="product_sale_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        //Instancier son DAO
        InsertService insertService = new InsertService();




        //Preparation necessaire pour le receiver de RabbitMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel1 = connection.createChannel();
        Channel channel2 = connection.createChannel();
        Channel channel3 = connection.createChannel();
        channel1.queueDeclare(QUEUE_NAME + Integer.toString(1),false,false,false,null);
        channel2.queueDeclare(QUEUE_NAME + Integer.toString(2),false,false,false,null);
        channel3.queueDeclare(QUEUE_NAME + Integer.toString(3),false,false,false,null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            //La reception et la deserialisation de JSON à une liste du message
            String receivedMessage = new String(delivery.getBody(),"UTF-8");
            System.out.println(receivedMessage);
            List<Product> productList = deserialize(receivedMessage);
            System.out.println(productList);
            try {
                //insertion dans la base
                insertService.insert(productList);
                //Mettre à jour le tableau

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };


        channel1.basicConsume(QUEUE_NAME + Integer.toString(1),true,deliverCallback,consumerTag -> {
            System.out.println("ERROR");
        });
        channel2.basicConsume(QUEUE_NAME + Integer.toString(2),true,deliverCallback,consumerTag -> {
            System.out.println("ERROR");
        });
        channel3.basicConsume(QUEUE_NAME + Integer.toString(3),true,deliverCallback,consumerTag -> {
            System.out.println("ERROR");
        });
    }

    public static List<Product> deserialize(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(message, new TypeReference<List<Product>>(){});

    }
}
