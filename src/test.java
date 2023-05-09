import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class test {


    public static void main(String[] args) throws SQLException {

        String url = "jdbc:mysql://localhost:3306/bo1?useSSL=false";
         String user = "root";
        String password = "nadamaalma";
        String query = "SELECT * FROM product_sale where sent ='0' ";
        List<Product> res = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement pst = connection.prepareStatement(query);
                ResultSet rs = pst.executeQuery()

        ) {

            while (rs.next()) {
                Product product = new Product(rs.getInt("id"), rs.getDate("date"), rs.getString("region"),
                        rs.getString("product"), rs.getInt("qty"), rs.getFloat("cost"),
                        rs.getDouble("amt"), rs.getFloat("tax"),
                        rs.getDouble("total"), 1);
                System.out.println(product.toString());

                res.add(product);
                System.out.println(res);

            }
        }


    }
}
