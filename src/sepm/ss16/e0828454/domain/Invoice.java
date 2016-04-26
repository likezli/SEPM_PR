package sepm.ss16.e0828454.domain;



import java.time.LocalDate;

public class Invoice {

    private Integer id;
    private LocalDate date;
    private Double sum;
    private Integer articlesCount;
    private String payment;

    public Invoice() {

    }

    public Invoice(Integer id, LocalDate date, Double sum, Integer articlesCount, String payment) {
        this.id = id;
        this.date = date;
        this.sum = sum;
        this.articlesCount = articlesCount;
        this.payment = payment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Integer getArticlesCount() {
        return articlesCount;
    }

    public void setArticlesCount(Integer articlesCount) {
        this.articlesCount = articlesCount;
    }

    public String getPayment() { return payment; }

    public void setPayment(String payment) { this.payment = payment; }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", date=" + date +
                ", sum=" + sum +
                ", articlesCount=" + articlesCount +
                ", payment='" + payment + '\'' +
                '}';
    }
}
