import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class test {


    public static void main(String[] args) throws SQLException {

        String url = "jdbc:mysql://localhost:3306/ho?useSSL=false";
         String user = "root";
        String password = "nadamaalma";
        String query = "SELECT * FROM product_sale where sent ='0' ";
        String query1 = "INSERT INTO product_sale(date, region, product, qty, cost, amt, tax, total) VALUES ('2021-12-31','tunisie','pull',2,25,12.2,7.5,60);";
        List<Product> res = new ArrayList<>();
//        Product p= new Product(Date)

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement pst = connection.prepareStatement(query1);
        ) {

            int rsl= pst.executeUpdate();

                System.out.println(res);

            }
        }


    }

