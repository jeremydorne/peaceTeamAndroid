package com.peaceteam.robertguthrie.model;

/**
 * Created by robertguthrie on 2/12/15.
 */
public class User {

    private final String email;
    private final String name;
    private final double rating;
    private final int numSalesReported;

    public User(String email, String name, double rating, int numSalesReported) {
        this.email = email;
        this.name = name;
        this.rating = rating;
        this.numSalesReported = numSalesReported;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public double getRating() {
        return this.rating;
    }

    public int getNumSalesReported() {
        return numSalesReported;
    }

    @Override
    public String toString() {
        return email + " " + name + " " + rating + " " + numSalesReported;
    }

}
