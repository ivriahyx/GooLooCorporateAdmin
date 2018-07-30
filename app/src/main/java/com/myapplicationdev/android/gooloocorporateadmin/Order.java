package com.myapplicationdev.android.gooloocorporateadmin;

public class Order {
    private int orderId;
    private String orderRef;
    private String firstName;
    private String lastName;
    private double finalPrice;

    public Order(int orderId, String orderRef, String firstName, String lastName, double finalPrice) {
        this.orderId = orderId;
        this.orderRef = orderRef;
        this.firstName = firstName;
        this.lastName = lastName;
        this.finalPrice = finalPrice;
    }

    public Order() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }
}
