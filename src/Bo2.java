import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public class Bo2 {

    //Définir sa queue
    public final static String QUEUE_NAME="bo2";
    //Definir son DAO
    public static RetreiveUnsentDataService retreiveService;
    public static UpdateService updateService;

    public static void main(String[] args) throws IOException, SQLException {



        //Instancier son DAO
        retreiveService = new RetreiveUnsentDataService(2, false);
        updateService = new UpdateService(2);

        //Preparation necessaire pour le sender de RabbitMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        //Definir le job
        TimerTask task = new TimerTask() {
            public void run(){
                try {
                    //Recuperer ses produits
                    List<Product> productList = retreiveService.retrieve();
                    System.out.println(productList);
                    //Serialiser ses produits en mode JSON
                    String message = productList.toString();

                    try (Connection connection = connectionFactory.newConnection()) {
                        Channel channel = connection.createChannel();
                        channel.queueDeclare(QUEUE_NAME  + Integer.toString(2), false, false, false, null);

                        channel.basicPublish("", QUEUE_NAME  + Integer.toString(2), null, message.getBytes());
                        System.out.println(" [x] sent '" + message + " '" + LocalDateTime.now().toString());
                        //Mise en TRUE de l'attribut sent dans la table de la base de données
                        updateService.update(productList);
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


}
