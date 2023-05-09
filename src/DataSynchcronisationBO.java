import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSynchcronisationBO {
    private int bo_num;
    public String url;
    public String user="root";
    public String password = "nadamaalma";
    //Requete pour recuperer les données
    public String queryRetrieve="SELECT * FROM product_sale where sent='0'";
    public DataSynchcronisationBO(int bo_num) {
        this.bo_num = bo_num;
        this.url = "jdbc:mysql://localhost:3306/bo" + Integer.toString(bo_num) +"?useSSL=false" ;

    }
    public List<Product> retrieve() throws SQLException{
        System.out.println(this.url);
        List<Product> res = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = connection.prepareStatement(queryRetrieve);
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
    public String queryUpdate = "UPDATE product_sale set sent = TRUE where id = ?";
    //Methode pour la mise en TRUE de l'attribut sent
    public void update(List<Product> productList) throws SQLException {
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = connection.prepareStatement(queryUpdate)
        ){
            for(int i=0; i<productList.size(); i++) {
                pst.setInt(1, productList.get(i).getId());
                pst.executeUpdate();
            }
        }
    }
}
