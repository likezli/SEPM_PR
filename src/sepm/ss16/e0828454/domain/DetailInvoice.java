package sepm.ss16.e0828454.domain;

/**
 * Created by Li on 02.04.2016.
 */
public class DetailInvoice {



    private Integer invoiceID;
    private Integer articleID;
    private String name;
    private Double purchasePrice;
    private Integer quantity;
    private String cashier;

    public DetailInvoice() {

    }

    public Integer getInvoiceID() {return invoiceID;}

    public void setInvoiceID(Integer invoiceID) {this.invoiceID = invoiceID;}

    public Integer getArticleID(){return articleID; }

    public void setArticleID(Integer articleID) {this.articleID = articleID; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    @Override
    public String toString() {
        return "DetailInvoice{" +
                "invoiceID=" + invoiceID +
                ", articleID=" + articleID +
                ", name='" + name + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", quantity=" + quantity +
                '}';
    }
}
