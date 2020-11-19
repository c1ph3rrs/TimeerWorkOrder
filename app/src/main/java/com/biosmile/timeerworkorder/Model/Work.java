package com.biosmile.timeerworkorder.Model;

public class Work {

    String customerHardwareDetail, customerJobDetail, customerName, customerTravelDetail, customerWorkDateDetail, customerWorkTimeDetail;

    public Work(){

    }

    public Work(String customerHardwareDetail, String customerJobDetail, String customerName, String customerTravelDetail, String customerWorkDateDetail, String customerWorkTimeDetail) {
        this.customerHardwareDetail = customerHardwareDetail;
        this.customerJobDetail = customerJobDetail;
        this.customerName = customerName;
        this.customerTravelDetail = customerTravelDetail;
        this.customerWorkDateDetail = customerWorkDateDetail;
        this.customerWorkTimeDetail = customerWorkTimeDetail;
    }

    public String getCustomerHardwareDetail() {
        return customerHardwareDetail;
    }

    public void setCustomerHardwareDetail(String customerHardwareDetail) {
        this.customerHardwareDetail = customerHardwareDetail;
    }

    public String getCustomerJobDetail() {
        return customerJobDetail;
    }

    public void setCustomerJobDetail(String customerJobDetail) {
        this.customerJobDetail = customerJobDetail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTravelDetail() {
        return customerTravelDetail;
    }

    public void setCustomerTravelDetail(String customerTravelDetail) {
        this.customerTravelDetail = customerTravelDetail;
    }

    public String getCustomerWorkDateDetail() {
        return customerWorkDateDetail;
    }

    public void setCustomerWorkDateDetail(String customerWorkDateDetail) {
        this.customerWorkDateDetail = customerWorkDateDetail;
    }

    public String getCustomerWorkTimeDetail() {
        return customerWorkTimeDetail;
    }

    public void setCustomerWorkTimeDetail(String customerWorkTimeDetail) {
        this.customerWorkTimeDetail = customerWorkTimeDetail;
    }
}
