package ir.climaxweb.visitorapp;

public class Product {
    private int id;
    private String Name;
    private boolean In_stock;
    private double Price;
    private double Sale_price;
    private String Description;
    private String Picture;

    public Product(int id, String Name, boolean In_stock, double price, double sale_price, String Description, String Picture){
        this.id=id;
        this.Name=Name;
        this.In_stock=In_stock;
        this.Price=price;
        this.Sale_price=sale_price;
        this.Description=Description;
        this.Picture=Picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isIn_stock() {
        return In_stock;
    }

    public void setIn_stock(boolean in_stock) {
        In_stock = in_stock;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getSale_price() {
        return Sale_price;
    }

    public void setSale_price(double sale_price) {
        Sale_price = sale_price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }
}
