import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bo1 {

    //Définir sa queue
    public final static String QUEUE_NAME="bo1";
    //Definir son DAO

    public static DataSynchcronisationBO dataSynchcronisation;

    public static void main(String[] args) throws IOException, SQLException {



        //Instancier son DAO
        dataSynchcronisation =new DataSynchcronisationBO(1);


        //Preparation necessaire pour le sender de RabbitMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        List<Product> productList = dataSynchcronisation.retrieve();
        System.out.println(productList);
        //Definir le job
        TimerTask task = new TimerTask() {
            public void run(){
                try {
                    //Recuperer ses produits
                    List<Product> productList = dataSynchcronisation.retrieve();
                    System.out.println(productList);
                    //Serialiser ses produits en mode JSON

//                    String message1 = new String(serialize(productList), "UTF-8");
//                    System.out.println(message1+"hello");
//                    byte [] message = serialize(productList);
                    String message = serialize(productList);

                    try (Connection connection = connectionFactory.newConnection()) {
                        Channel channel = connection.createChannel();
                        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                        channel.basicPublish("", QUEUE_NAME , null, message.getBytes());
                        System.out.println(" [x] sent '"  + message + " '" + LocalDateTime.now().toString());
                        //Mise en TRUE de l'attribut sent dans la table de la base de données
                        dataSynchcronisation.update(productList);
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e){ }
            }
        };
        Timer timer = new Timer("Timer");

        //Ce job s'executera chaque minute
        long delay = 60*1000L;
        timer.schedule(task,0, delay);

    }

//    public static byte[] serialize(List<Product> productList) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ObjectOutputStream objOut = new ObjectOutputStream(out);
//        objOut.writeObject(productList);
//        objOut.flush();
//        return out.toByteArray();
//    }
public static String serialize(List<Product> productList) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(productList);
}


}
