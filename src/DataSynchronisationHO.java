import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DataSynchronisationHO {
    //coordonnées de la base
    public String url = "jdbc:mysql://localhost:3306/ho?useSSL=false";
    public String user="root";
    public String password = "nadamaalma";
    //query for insert
    public String queryInsert = "INSERT INTO product_sale(date, region, product, qty, cost, amt, tax, total) VALUES (?,?,?,?,?,?,?,?);";
    //Methode pour inserer dans la base
    public void insert(List<Product> productList) throws SQLException {
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = connection.prepareStatement(queryInsert)
        ){
            System.out.println("hello again");
            for(int i=0; i<productList.size(); i++) {
                System.out.println(i);
                Product p = productList.get(i);
                System.out.println(p);
                pst.setDate(1, new Date(p.getDate().getTime()));
                pst.setString(2, p.getRegion());
                pst.setString(3, p.getProduct());
                pst.setInt(4, p.getQty());
                pst.setFloat(5, p.getCost());
                pst.setDouble(6, p.getAmt());
                pst.setFloat(7, p.getTax());
                pst.setDouble(8, p.getTotal());
                System.out.println("hello world");
                pst.executeUpdate();
                System.out.println("hello from firas ");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    //query for retrieve
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
                        rs.getDouble("total"));

                res.add(product);
            }

            return res;
        }
    }
}
