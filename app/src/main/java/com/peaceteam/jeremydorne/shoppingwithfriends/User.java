package com.peaceteam.jeremydorne.shoppingwithfriends;

/**
 * Created by robertguthrie on 2/12/15.
 */
public class User {

    private String email;
    private String name;
    private double rating;
    private int numSalesReported;

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
