import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RetrieveServiceForHo {
    //Coordonnées de la base
    public String user="postgres";
    public String password = "root";
    public String url = "jdbc:postgresql://localhost:5432/ho";

    //Requete pour recuperer les données
    public String query = "SELECT * FROM product_sale";
    //Methodes pour recuperer les produits
    public List<Product> retrieve() throws SQLException {
        List<Product> res = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery()
        ) {

            while(rs.next()) {
                Product product = new Product(rs.getInt("id"),rs.getDate("date"),rs.getString("region"),
                        rs.getString("product"),rs.getInt("qty"),rs.getFloat("cost"),
                        rs.getDouble("amt"),rs.getFloat("tax"),
                        rs.getDouble("total"),rs.getInt("bo_num"));

                res.add(product);
            }

            return res;
        }
    }
}

