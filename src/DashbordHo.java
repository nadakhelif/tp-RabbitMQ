
import javax.swing.*;
        import javax.swing.table.DefaultTableModel;
        import java.awt.*;
        import java.sql.SQLException;
        import java.util.List;

public class DashbordHo {
    final Object[] column = {"Date", "Region", "Product", "Quantity", "Cost", "AMT", "Tax", "Total"};
    private JScrollPane scrollPane;
    private JTable dataTable;
    DefaultTableModel dtm;
    JLabel titleLabel = new JLabel("waiting for updates", SwingConstants.CENTER);
    public DashbordHo(){
        JFrame frame = new JFrame("HO Dashboard");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Head Office Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        Object[][] data = {};
        this.dtm = new DefaultTableModel(data, this.column);
        this.dataTable = new JTable(dtm);
        this.dataTable.setBounds(30, 40, 200, 300);
        this.scrollPane = new JScrollPane(this.dataTable);
        try {
            this.fillTable(0,"");
        } catch (SQLException sqlException) {

        }
        frame.setLayout(new BorderLayout());
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void fillTable(int size ,String bo) throws SQLException {
        dtm.setRowCount(0);
        DataSynchronisationHO dataSynchronisationHO = new DataSynchronisationHO();
        List<Product> productList = dataSynchronisationHO.retrieve();
        if(size>0){
        titleLabel.setText("Update done successfully: Added " +size+ " rows to the database.from "+ bo);}else{
            titleLabel.setText("Head Office DB up to date");
        }
        for (Product p : productList) {
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