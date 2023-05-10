

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class HO {
    //Définir sa queue
    public final static String QUEUE_NAME1="bo1";
    public final static String QUEUE_NAME2="bo2";

    public static void main(String[] args) throws IOException, TimeoutException {

        //Instancier son DAO
        DataSynchronisationHO dataSynchronisationHO = new DataSynchronisationHO();
        //Instancier l'interface
        DashbordHo dashbordHo =new DashbordHo();

        //read from rabbit mq
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel1 = connection.createChannel();
        Channel channel2 = connection.createChannel();

        channel1.queueDeclare(QUEUE_NAME1,false,false,false,null);
        channel2.queueDeclare(QUEUE_NAME2,false,false,false,null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            //deserialisation
            String receivedMessage = new String(delivery.getBody(),"UTF-8");
            System.out.println(receivedMessage);
            List<Product> productList = null;
            productList = SerealisationDeseralisation.deserialize(delivery.getBody());
            System.out.println("hello nada ");
            System.out.println(productList);
            try {
                //insertion dans la base
                dataSynchronisationHO.insert(productList);
                //update tableau
//                displayHo.setMessage("success added this "+productList );
                dashbordHo.fillTable(productList.size(),delivery.getEnvelope().getRoutingKey());

//                tableHo.fillTable();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };


        channel1.basicConsume(QUEUE_NAME1,true,deliverCallback,consumerTag -> {
            System.out.println("ERROR");
        });
        channel2.basicConsume(QUEUE_NAME2,true,deliverCallback,consumerTag -> {
            System.out.println("ERROR");
        });
    }



}
