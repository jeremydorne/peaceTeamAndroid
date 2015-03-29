package com.peaceteam.robertguthrie.model;

/**
 * Created by robertguthrie on 3/2/15.
 */
public class Interest {

    private final String itemName;
    private final Double desiredPrice;

    public Interest(String name, Double price) {
        itemName = name;
        desiredPrice = price;
    }

    public String getItemName() {
        return itemName;
    }

    public Double getDesiredPrice() {
        return desiredPrice;
    }

    @Override
    public String toString() {
        return itemName + "\t" + desiredPrice;
    }

}
