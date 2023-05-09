import java.util.Date;
public class Product {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getBo_num() {
        return bo_num;
    }

    public void setBo_num(int bo_num) {
        this.bo_num = bo_num;
    }

    private int id;
    private Date date;
    private String region;
    private String product;
    private int qty;
    private float cost;
    private double amt;
    private float tax;
    private double total;
    private int bo_num;

    public Product() {}

    public Product(int id, Date date, String region, String product, int qty, float cost, double amt, float tax, double total, int bo_num) {
        this.id = id;
        this.date = date;
        this.region = region;
        this.product = product;
        this.qty = qty;
        this.cost = cost;
        this.amt = amt;
        this.tax = tax;
        this.total = total;
        this.bo_num=bo_num;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", date=" + date +
                ", region='" + region + '\'' +
                ", product='" + product + '\'' +
                ", qty=" + qty +
                ", cost=" + cost +
                ", amt=" + amt +
                ", tax=" + tax +
                ", total=" + total +
                ", bo_num=" + bo_num+
                '}';
    }
}

