package com.peaceteam.robertguthrie.model;

/**
 * Created by robertguthrie on 3/5/15.
 */
public class Sale {

    private String itemName;
    private Double price;
    private String location;

    public Sale(String name, Double price, String location) {
        this.itemName = name;
        this.price = price;
        this.location = location;
    }

    public String getItemName() {
        return itemName;
    }

    public Double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return itemName + "\t" + price.toString() + "\t" + location;
    }

}
