package sepm.ss16.e0828454.domain;


import javafx.beans.binding.StringExpression;

public class Article {

    private Integer id;
    private String name;
    private Double price;
    private String producer;
    private Integer sold;
    private String picture;
    private Boolean deleted;



    public Article() {

    }

    public Article(Integer id, String name, Double price, String producer, Integer sold, String picture, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.producer = producer;
        this.sold = sold;
        this.picture = picture;
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", producer='" + producer + '\'' +
                ", sold=" + sold +
                ", picture='" + picture + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
