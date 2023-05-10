

        import javax.swing.*;
        import javax.swing.table.DefaultTableModel;
        import javax.swing.table.TableColumn;
        import java.awt.*;
        import java.sql.*;
        import java.util.ArrayList;
        import java.util.List;

public class TableHo{
    final Object[] column = {"Date","Region","Product","Quantity","Cost","AMT","Tax","Total"};
    private JScrollPane scrollPane;
    private JTable dataTable;
    DefaultTableModel dtm;

    private Connection connection = null;
    private Statement statement = null;

    public TableHo(){
        Object[][] data = {};
        this.dtm = new DefaultTableModel(data, this.column);
        this.dataTable =new JTable(dtm);
        this.dataTable.setBounds(30,40,200,300);
        this.scrollPane = new JScrollPane(this.dataTable);
        try {
            this.fillTable();
        } catch (SQLException sqlException){

        }

    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void fillTable() throws SQLException {
        dtm.setRowCount(0);
        DataSynchronisationHO dataSynchronisationHO = new DataSynchronisationHO();
        List<Product> productList = dataSynchronisationHO.retrieve();
        for (Product p : productList){
            dtm.addRow(new Object[]{p.getDate().toString(),
                    p.getRegion(),
                    p.getProduct(),
                    Integer.toString(p.getQty()),
                    Float.toString(p.getCost()),
                    Double.toString(p.getAmt()),
                    Float.toString(p.getTax()),
                    Double.toString(p.getTotal()),

            });
        }

    }
}
