package com.biosmile.timeerworkorder.Model;

public class Clients {

    String address,city,companyName,customerEmail,  customerID,customerName, customerPhone,postal,state;

    public Clients(){

    }

    public Clients(String address, String city, String companyName, String customerEmail, String customerID, String customerName, String customerPhone, String postal, String state) {
        this.address = address;
        this.city = city;
        this.companyName = companyName;
        this.customerEmail = customerEmail;
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.postal = postal;
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
