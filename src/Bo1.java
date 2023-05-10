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


public class Bo1 {

    //Définir sa queue
    public final static String QUEUE_NAME="bo1";
    //Definir son DAO

    public static DataSynchcronisationBO dataSynchcronisation;

    public static void main(String[] args) throws IOException, SQLException {
        //Instancier son DAO
        dataSynchcronisation =new DataSynchcronisationBO(1);
        // sender de RabbitMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        List<Product> productList = dataSynchcronisation.retrieve();
        System.out.println(productList);
        //timer thread
        TimerTask job = new TimerTask() {
            public void run(){
                try {
                    //Recuperer ses produits
                    List<Product> productList = dataSynchcronisation.retrieve();
                    System.out.println(productList);
                    String message = SerealisationDeseralisation.serialize(productList);

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

        //every minute
        long minute = 60*1000L;
        timer.schedule(job,0, minute);

    }
}
