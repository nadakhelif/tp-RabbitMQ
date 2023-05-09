
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RetreiveUnsentDataService {
    private int bo_num;
    //retourne tout les produits si sent=true ou bien if sent=false il va retournée juste les produit non snet before
    public RetreiveUnsentDataService(int bo_num, boolean sent) {
        this.bo_num = bo_num;
        this.url = "jdbc:mysql://localhost:3306/bo" + Integer.toString(bo_num) ;
        query = "SELECT * FROM product_sale" + (sent ? "" : " where sent=FALSE");
    }
    //nriglouha aala hsebna ahna
    //Coordonnées de la base
    public String url;
    public String user="root";
    public String password = "nadamaalma";
    //Requete pour recuperer les données
    public String query;
    //Methodes pour recuperer les produits
    public List<Product> retrieve() throws SQLException{
        System.out.println(this.url);
        List<Product> res = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery()
        ) {

            while(rs.next()) {
                Product product = new Product(rs.getInt("id"),rs.getDate("date"),rs.getString("region"),
                        rs.getString("product"),rs.getInt("qty"),rs.getFloat("cost"),
                        rs.getDouble("amt"),rs.getFloat("tax"),
                        rs.getDouble("total"),bo_num);

                res.add(product);
            }

            return res;
        }

    }
}
